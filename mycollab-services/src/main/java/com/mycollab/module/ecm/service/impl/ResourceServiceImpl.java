/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm.service.impl;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.core.utils.ImageUtil;
import com.mycollab.core.utils.MimeTypesUtil;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.billing.service.BillingPlanCheckerService;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.ecm.domain.Resource;
import com.mycollab.module.ecm.esb.DeleteResourcesEvent;
import com.mycollab.module.ecm.esb.SaveContentEvent;
import com.mycollab.module.ecm.service.ContentJcrDao;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.service.RawContentService;
import com.google.common.eventbus.AsyncEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

@Service(value = "resourceService")
public class ResourceServiceImpl implements ResourceService {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    private ContentJcrDao contentJcrDao;

    @Autowired
    private RawContentService rawContentService;

    @Autowired
    private BillingPlanCheckerService billingPlanCheckerService;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public List<Resource> getResources(String path) {
        List<Resource> resources = contentJcrDao.getResources(path);
        if (CollectionUtils.isNotEmpty(resources)) {
            Collections.sort(resources);
            return resources;
        }

        return new ArrayList<>();
    }

    @Override
    public List<Content> getContents(String path) {
        return contentJcrDao.getContents(path);
    }

    @Override
    public List<Folder> getSubFolders(String path) {
        return contentJcrDao.getSubFolders(path);
    }

    @Override
    public Folder createNewFolder(String baseFolderPath, String folderName, String description, String createdBy) {
        if (FileUtils.isValidFileName(folderName)) {
            String folderPath = baseFolderPath + "/" + folderName;
            Folder folder = new Folder(folderPath);
            folder.setName(folderName);
            folder.setDescription(description);
            folder.setCreatedBy(createdBy);
            folder.setCreated(new GregorianCalendar());
            contentJcrDao.createFolder(folder, createdBy);
            return folder;
        }
        throw new UserInvalidInputException("Invalid file name");
    }

    @Override
    public void saveContent(Content content, String createdUser, InputStream refStream, Integer sAccountId) {
        Integer fileSize = 0;
        if (sAccountId != null) {
            try {
                fileSize = refStream.available();
                billingPlanCheckerService.validateAccountCanUploadMoreFiles(sAccountId, fileSize);
            } catch (IOException e) {
                LOG.error("Can not get available bytes", e);
            }
        }

        // detect mimeType and set to content
        String mimeType = MimeTypesUtil.detectMimeType(content.getPath());
        content.setMimeType(mimeType);
        content.setSize(Long.valueOf(fileSize));

        String contentPath = content.getPath();
        rawContentService.saveContent(contentPath, refStream);

        if (MimeTypesUtil.isImage(mimeType)) {
            try (InputStream newInputStream = rawContentService.getContentStream(contentPath)) {
                BufferedImage image = ImageUtil.generateImageThumbnail(newInputStream);
                if (image != null) {
                    String thumbnailPath = String.format(".thumbnail/%d/%s.%s",
                            sAccountId, StringUtils.generateSoftUniqueId(), "png");
                    File tmpFile = File.createTempFile("tmp", "png");
                    ImageIO.write(image, "png", new FileOutputStream(tmpFile));
                    rawContentService.saveContent(thumbnailPath, new FileInputStream(tmpFile));
                    content.setThumbnail(thumbnailPath);
                }
            } catch (IOException e) {
                LOG.error("Error when generating thumbnail", e);
            }
        }

        contentJcrDao.saveContent(content, createdUser);

        SaveContentEvent event = new SaveContentEvent(content, createdUser, sAccountId);
        asyncEventBus.post(event);
    }

    @Override
    public void removeResource(String path) {
        Resource res = contentJcrDao.getResource(path);
        if (res == null) {
            return;
        }
        contentJcrDao.removeResource(path);
        rawContentService.removePath(path);
    }

    @Override
    public void removeResource(String path, String deleteUser, Boolean isUpdateDriveInfo, Integer sAccountId) {
        Resource res = contentJcrDao.getResource(path);
        if (res == null) {
            return;
        }

        DeleteResourcesEvent event;
        if (res instanceof Folder) {
            event = new DeleteResourcesEvent(new String[]{path}, deleteUser, isUpdateDriveInfo, sAccountId);
        } else {
            event = new DeleteResourcesEvent(new String[]{path, ((Content) res).getThumbnail()}, deleteUser, isUpdateDriveInfo, sAccountId);
        }
        asyncEventBus.post(event);
        contentJcrDao.removeResource(path);
    }

    @Override
    public InputStream getContentStream(String path) {
        return rawContentService.getContentStream(path);
    }

    @Override
    public void rename(String oldPath, String newPath, String userUpdate) {
        contentJcrDao.rename(oldPath, newPath);
        rawContentService.renamePath(oldPath, newPath);
    }

    @Override
    public List<Resource> searchResourcesByName(String baseFolderPath, String resourceName) {
        return contentJcrDao.searchResourcesByName(baseFolderPath, resourceName);
    }

    @Override
    public void moveResource(String oldPath, String destinationFolderPath, String userMove) {
        String oldResourceName = oldPath.substring(oldPath.lastIndexOf("/") + 1, oldPath.length());

        Resource oldResource = contentJcrDao.getResource(oldPath);

        if ((oldResource instanceof Folder) && destinationFolderPath.contains(oldPath)) {
            throw new UserInvalidInputException("Can not move asset(s) to folder " + destinationFolderPath);
        } else {
            String destinationPath = destinationFolderPath + "/" + oldResourceName;
            contentJcrDao.moveResource(oldPath, destinationPath);
            rawContentService.movePath(oldPath, destinationPath);
        }
    }

    @Override
    public Folder getParentFolder(String path) {
        try {
            String parentPath = path.substring(0, path.lastIndexOf("/"));
            Resource res = contentJcrDao.getResource(parentPath);
            if (res instanceof Folder)
                return (Folder) res;
            return null;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    public Resource getResource(String path) {
        return contentJcrDao.getResource(path);
    }
}
