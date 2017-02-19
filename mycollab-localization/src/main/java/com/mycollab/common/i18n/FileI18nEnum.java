/**
 * This file is part of mycollab-localization.
 *
 * mycollab-localization is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-localization is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-localization.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * @author MyCollab Ltd.
 * @since 4.1.1
 */
@BaseName("common-file")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum FileI18nEnum {
    SINGLE,
    LIST,
    EXCEL,
    PDF,
    CSV,
    IMPORT_FILE,
    EXPORT_FILE,

    ACTION_NEW_FOLDER,
    ACTION_MOVE_ASSETS,

    ATTACH_FILES,
    NOT_ATTACH_FILE_WARNING,
    IMPORT_FILE_SUCCESS,
    CHOOSE_SUPPORT_FILE_TYPES_WARNING,

    OPT_MY_DOCUMENTS,
    OPT_FAVICON_FORMAT_DESCRIPTION,
    OPT_SIZE,
    OPT_SIZE_VALUE,
    OPT_DRAG_OR_CLICK_TO_UPLOAD,
    OPT_DELETE_RESOURCES_SUCCESSFULLY,
    OPT_EDIT_FOLDER_FILE_NAME,
    OPT_UPLOAD_FILE_SUCCESSFULLY,
    OPT_MOVE_ASSETS_SUCCESSFULLY,

    ACTION_CHANGE_LOGO,

    ERROR_FILE_IS_EXISTED,
    ERROR_INVALID_SUPPORTED_IMAGE_FORMAT,
    ERROR_UPLOAD_INVALID_SUPPORTED_IMAGE_FORMAT,
    ERROR_NO_SELECTED_FILE_TO_DOWNLOAD,
    ERROR_INVALID_FILE_NAME,
    ERROR_SOME_FILES_MOVING_ERROR
}
