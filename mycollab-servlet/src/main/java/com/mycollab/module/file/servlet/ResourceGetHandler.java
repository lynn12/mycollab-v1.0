/**
 * This file is part of mycollab-servlet.
 *
 * mycollab-servlet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-servlet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-servlet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.file.servlet;

import com.mycollab.core.utils.MimeTypesUtil;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.servlet.GenericHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@WebServlet(urlPatterns = "/file/*", name = "resourceGetHandler")
public class ResourceGetHandler extends GenericHttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceGetHandler.class);

    @Autowired
    private ResourceService resourceService;

    @Override
    protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        InputStream inputStream = resourceService.getContentStream(path);

        if (inputStream != null) {
            response.setHeader("Content-Type", MimeTypesUtil.detectMimeType(path));
            response.setHeader("Content-Length", String.valueOf(inputStream.available()));

            try (BufferedInputStream input = new BufferedInputStream(inputStream);
                 BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
                byte[] buffer = new byte[8192];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
        } else {
            LOG.error("Can not find resource has path {}", path);
        }
    }
}
