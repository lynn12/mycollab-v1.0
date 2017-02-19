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
package com.mycollab.module.crm.view.cases;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.module.crm.domain.CaseWithBLOBs;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.module.crm.view.account.AccountSelectionField;
import com.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import org.vaadin.viritin.fields.MTextField;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
class CaseEditFormFieldFactory<B extends CaseWithBLOBs> extends AbstractBeanFieldGroupEditFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    CaseEditFormFieldFactory(GenericBeanForm<B> form) {
        super(form);
    }

    CaseEditFormFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
        super(form, isValidateForm);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("priority")) {
            return new CasePriorityComboBox();
        } else if (propertyId.equals("status")) {
            return new CaseStatusComboBox();
        } else if (propertyId.equals("reason")) {
            return new CaseReasonComboBox();
        } else if (propertyId.equals("origin")) {
            return new CasesOriginComboBox();
        } else if (propertyId.equals("type")) {
            return new CaseTypeComboBox();
        } else if (propertyId.equals("description")) {
            return new RichTextArea();
        } else if (propertyId.equals("resolution")) {
            return new RichTextArea();
        } else if (propertyId.equals("accountid")) {
            AccountSelectionField accountField = new AccountSelectionField();
            accountField.setRequired(true);
            return accountField;
        } else if (propertyId.equals("subject")) {
            MTextField tf = new MTextField();
            if (isValidateForm) {
                tf.withNullRepresentation("").withRequired(true)
                        .withRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                                UserUIContext.getMessage(CaseI18nEnum.FORM_SUBJECT)));
            }

            return tf;
        } else if (propertyId.equals("assignuser")) {
            ActiveUserComboBox userBox = new ActiveUserComboBox();
            userBox.select(attachForm.getBean().getAssignuser());
            return userBox;
        }

        return null;
    }
}
