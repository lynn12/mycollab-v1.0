/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.ui.components;

import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.ImageUtil;
import com.vaadin.ui.UI;
import org.vaadin.easyuploads.UploadField;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class UploadImageField extends UploadField {
    private ImagePreviewCropWindow.ImageSelectionCommand imageSelectionCommand;

    public UploadImageField(ImagePreviewCropWindow.ImageSelectionCommand imageSelectionCommand) {
        this.imageSelectionCommand = imageSelectionCommand;
        this.addStyleName("upload-field");
        this.setFieldType(FieldType.BYTE_ARRAY);
        this.setSizeUndefined();
    }

    @Override
    protected void updateDisplay() {
        byte[] imageData = (byte[]) this.getValue();
        String mimeType = this.getLastMimeType();
        if (mimeType.equals("image/jpeg") || mimeType.equals("image/jpg")) {
            imageData = ImageUtil.convertJpgToPngFormat(imageData);
            if (imageData == null) {
                throw new UserInvalidInputException("Can not convert image to jpg format");
            } else {
                mimeType = "image/png";
            }
        }

        if (mimeType.equals("image/png")) {
            UI.getCurrent().addWindow(new ImagePreviewCropWindow(imageSelectionCommand, imageData));
        } else {
            throw new UserInvalidInputException("Upload file does not have valid image format. The supported formats are jpg/png");
        }
    }
}
