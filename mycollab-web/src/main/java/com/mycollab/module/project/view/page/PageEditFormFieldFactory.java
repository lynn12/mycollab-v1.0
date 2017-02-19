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
package com.mycollab.module.project.view.page;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.WikiI18nEnum;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.project.i18n.PageI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
class PageEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Page> {
    private static final long serialVersionUID = 1L;

    PageEditFormFieldFactory(GenericBeanForm<Page> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        Page page = attachForm.getBean();
        if (propertyId.equals("content")) {
            CKEditorConfig config = new CKEditorConfig();
            config.useCompactTags();
            config.setResizeDir(CKEditorConfig.RESIZE_DIR.HORIZONTAL);
            config.disableSpellChecker();
            config.disableResizeEditor();
            config.disableElementsPath();
            config.setToolbarCanCollapse(true);
            config.setWidth("100%");

            String appUrl = MyCollabUI.getSiteUrl();
            String params = String.format("path=%s&createdUser=%s&sAccountId=%d", page.getPath(),
                    UserUIContext.getUsername(), MyCollabUI.getAccountId());
            if (appUrl.endsWith("/")) {
                config.setFilebrowserUploadUrl(String.format("%spage/upload?%s", appUrl, params));
            } else {
                config.setFilebrowserUploadUrl(String.format("%s/page/upload?%s", appUrl, params));
            }

            CKEditorTextField ckEditorTextField = new CKEditorTextField(config);
            ckEditorTextField.setHeight("450px");
            ckEditorTextField.setRequired(true);
            ckEditorTextField.setRequiredError("Content must be not null");
            return ckEditorTextField;
        } else if (propertyId.equals("status")) {
            page.setStatus(WikiI18nEnum.status_public.name());
            return new I18nValueComboBox(false, WikiI18nEnum.status_public,
                    WikiI18nEnum.status_private, WikiI18nEnum.status_archieved);
        } else if (propertyId.equals("subject")) {
            TextField subjectField = new TextField();
            subjectField.setRequired(true);
            subjectField.setRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                    UserUIContext.getMessage(PageI18nEnum.FORM_SUBJECT)));
            return subjectField;
        }

        return null;
    }

}
