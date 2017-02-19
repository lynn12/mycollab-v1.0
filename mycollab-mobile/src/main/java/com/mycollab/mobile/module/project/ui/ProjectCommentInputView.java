/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.ui;

import com.mycollab.common.domain.CommentWithBLOBs;
import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.core.utils.ImageUtil;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.mobile.ui.MobileAttachmentUtils;
import com.mycollab.mobile.ui.TempFileFactory;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.*;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.easyuploads.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectCommentInputView extends AbstractMobilePageView {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectCommentInputView.class.getName());

    private TextArea commentInput;

    private String type;
    private String typeId;
    private Integer extraTypeId;

    private FileBuffer receiver;
    private MultiUpload uploadField;
    private Map<String, File> fileStores;

    private ResourceService resourceService;
    private CssLayout statusWrapper;

    public ProjectCommentInputView(String typeVal, String typeIdVal, Integer extraTypeIdVal) {
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.ACTION_ADD_COMMENT));
        resourceService = AppContextUtil.getSpringBean(ResourceService.class);
        MVerticalLayout content = new MVerticalLayout().withFullWidth().withStyleName("comment-input");
        this.setContent(content);

        type = typeVal;
        typeId = typeIdVal;
        extraTypeId = extraTypeIdVal;

        prepareUploadField();

        commentInput = new TextArea();
        commentInput.setWidth("100%");
        commentInput.setInputPrompt("---");

        MButton postBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
            final CommentWithBLOBs comment = new CommentWithBLOBs();
            comment.setComment(commentInput.getValue());
            comment.setCreatedtime(new GregorianCalendar().getTime());
            comment.setCreateduser(UserUIContext.getUsername());
            comment.setSaccountid(MyCollabUI.getAccountId());
            comment.setType(type);
            comment.setTypeid("" + typeId);
            comment.setExtratypeid(extraTypeId);

            final CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
            int commentId = commentService.saveWithSession(comment, UserUIContext.getUsername());

            String attachmentPath = AttachmentUtils.getCommentAttachmentPath(type, MyCollabUI.getAccountId(),
                    CurrentProjectVariables.getProjectId(), typeId, commentId);
            if (!"".equals(attachmentPath)) {
                saveContentsToRepo(attachmentPath);
            }

            getNavigationManager().navigateBack();
        });
        this.setRightComponent(postBtn);
        content.with(commentInput, ELabel.hr(), uploadField, statusWrapper);
    }

    private void prepareUploadField() {
        statusWrapper = new CssLayout();
        statusWrapper.setWidth("100%");
        statusWrapper.setStyleName("upload-status-wrap");
        receiver = createReceiver();

        uploadField = new MultiUpload();
        uploadField.setButtonCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPLOAD));
        uploadField.setImmediate(true);
        uploadField.setHandler(new MobileUploadHandler());
    }

    private Component createAttachmentRow(String fileName) {
        final MHorizontalLayout uploadSucceedLayout = new MHorizontalLayout().withFullWidth();
        Label uploadResult = new Label(fileName);
        uploadSucceedLayout.with(uploadResult).expand(uploadResult);

        MButton removeAttachment = new MButton("", clickEvent -> statusWrapper.removeComponent(uploadSucceedLayout))
                .withIcon(FontAwesome.TRASH_O).withStyleName(MobileUIConstants.BUTTON_LINK);
        uploadSucceedLayout.addComponent(removeAttachment);
        return uploadSucceedLayout;
    }

    private void saveContentsToRepo(String attachmentPath) {
        if (MapUtils.isNotEmpty(fileStores)) {
            for (Map.Entry<String, File> entry : fileStores.entrySet()) {
                try {
                    String fileName = entry.getKey();
                    File file = entry.getValue();
                    String fileExt = "";
                    int index = fileName.lastIndexOf(".");
                    if (index > 0) {
                        fileExt = fileName.substring(index + 1, fileName.length());
                    }

                    if ("jpg".equalsIgnoreCase(fileExt) || "png".equalsIgnoreCase(fileExt)) {
                        try {
                            BufferedImage bufferedImage = ImageIO.read(file);
                            int imgHeight = bufferedImage.getHeight();
                            int imgWidth = bufferedImage.getWidth();

                            BufferedImage scaledImage;

                            float scale;
                            float destWidth = 974;
                            float destHeight = 718;

                            float scaleX = Math.min(destHeight / imgHeight, 1);
                            float scaleY = Math.min(destWidth / imgWidth, 1);
                            scale = Math.min(scaleX, scaleY);
                            scaledImage = ImageUtil.scaleImage(bufferedImage, scale);

                            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                            ImageIO.write(scaledImage, fileExt, outStream);

                            resourceService.saveContent(MobileAttachmentUtils.constructContent(fileName, attachmentPath),
                                    UserUIContext.getUsername(), new ByteArrayInputStream(outStream.toByteArray()), MyCollabUI.getAccountId());
                        } catch (IOException e) {
                            LOG.error("Error in upload file", e);
                            resourceService.saveContent(MobileAttachmentUtils.constructContent(fileName, attachmentPath),
                                    UserUIContext.getUsername(), new FileInputStream(file), MyCollabUI.getAccountId());
                        }
                    } else {
                        resourceService.saveContent(MobileAttachmentUtils.constructContent(fileName, attachmentPath),
                                UserUIContext.getUsername(), new FileInputStream(file), MyCollabUI.getAccountId());
                    }

                } catch (FileNotFoundException e) {
                    LOG.error("Error when attach content in UI", e);
                }
            }
        }
    }

    private FileBuffer createReceiver() {
        FileBuffer receiver = new FileBuffer(UploadField.FieldType.FILE) {
            private static final long serialVersionUID = 1L;

            @Override
            public FileFactory getFileFactory() {
                return new TempFileFactory();
            }

            @Override
            public void setLastMimeType(String mimeType) {

            }

            @Override
            public void setLastFileName(String fileName) {

            }
        };
        receiver.setDeleteFiles(false);
        return receiver;
    }

    private void receiveFile(File file, String fileName, String mimeType, long length) {
        if (fileStores == null) {
            fileStores = new HashMap<>();
        }
        if (fileStores.containsKey(fileName)) {
            NotificationUtil.showWarningNotification(UserUIContext.getMessage(FileI18nEnum.ERROR_FILE_IS_EXISTED, fileName));
        } else {
            fileStores.put(fileName, file);
        }
    }

    private class MobileUploadHandler implements MultiUploadHandler {
        private LinkedList<ProgressBar> indicators;

        @Override
        public void streamingStarted(StreamVariable.StreamingStartEvent event) {
        }

        @Override
        public void streamingFinished(StreamVariable.StreamingEndEvent event) {
            String fileName = event.getFileName();
            int index = fileName.lastIndexOf(".");
            if (index > 0) {
                String fileExt = fileName.substring(index + 1, fileName.length());
                fileName = MobileAttachmentUtils.ATTACHMENT_NAME_PREFIX + System.currentTimeMillis() + "." + fileExt;
            }

            if (!indicators.isEmpty()) {
                statusWrapper.replaceComponent(indicators.remove(0), createAttachmentRow(fileName));
            }

            File file = receiver.getFile();
            receiveFile(file, fileName, event.getMimeType(), event.getBytesReceived());
            receiver.setValue(null);
        }

        @Override
        public void streamingFailed(StreamVariable.StreamingErrorEvent event) {
            if (!indicators.isEmpty()) {
                Label uploadResult = new Label("Upload failed! File: " + event.getFileName());
                uploadResult.setStyleName("upload-status");
                statusWrapper.replaceComponent(indicators.remove(0), uploadResult);
            }
        }

        @Override
        public void onProgress(StreamVariable.StreamingProgressEvent event) {
            long readBytes = event.getBytesReceived();
            long contentLength = event.getContentLength();
            float f = (float) readBytes / (float) contentLength;
            indicators.get(0).setValue(f);
        }

        @Override
        public OutputStream getOutputStream() {
            MultiUpload.FileDetail next = uploadField.getPendingFileNames().iterator().next();
            return receiver.receiveUpload(next.getFileName(), next.getMimeType());
        }

        @Override
        public void filesQueued(Collection<MultiUpload.FileDetail> pendingFileNames) {
            UI.getCurrent().setPollInterval(500);
            if (indicators == null) {
                indicators = new LinkedList<>();
            }
            for (MultiUpload.FileDetail f : pendingFileNames) {
                ProgressBar pi = new ProgressBar();
                pi.setValue(0f);
                pi.setStyleName("upload-progress");
                pi.setWidth("100%");
                statusWrapper.addComponent(pi);
                pi.setEnabled(true);
                pi.setVisible(true);
                indicators.add(pi);
            }
        }

        @Override
        public boolean isInterrupted() {
            return false;
        }
    }

}
