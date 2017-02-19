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
package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.mobile.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.mobile.ui.CountryListSelect;
import com.mycollab.mobile.ui.IndustryListSelect;
import com.mycollab.mobile.ui.PrefixNameListSelect;
import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.CompoundCustomField;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 4.1
 */
class LeadEditFormFieldFactory<B extends Lead> extends AbstractBeanFieldGroupEditFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    private LeadFirstNamePrefixField firstNamePrefixField;

    LeadEditFormFieldFactory(GenericBeanForm<B> form) {
        super(form);
        firstNamePrefixField = new LeadFirstNamePrefixField();
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("firstname") || propertyId.equals("prefixname")) {
            return firstNamePrefixField;
        } else if (propertyId.equals("primcountry") || propertyId.equals("othercountry")) {
            return new CountryListSelect();
        } else if (propertyId.equals("status")) {
            return new LeadStatusListSelect();
        } else if (propertyId.equals("industry")) {
            return new IndustryListSelect();
        } else if (propertyId.equals("assignuser")) {
            return new ActiveUserComboBox();
        } else if (propertyId.equals("source")) {
            return new LeadSourceListSelect();
        } else if (propertyId.equals("lastname")) {
            TextField tf = new TextField();
            if (isValidateForm) {
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError("Last name must not be null");
            }

            return tf;
        } else if (propertyId.equals("description")) {
            TextArea descArea = new TextArea();
            descArea.setNullRepresentation("");
            return descArea;
        } else if (propertyId.equals("accountname")) {
            MTextField txtField = new MTextField();
            if (isValidateForm) {
                txtField.withNullRepresentation("").withRequired(true).withRequiredError(UserUIContext.getMessage
                        (ErrorI18nEnum.FIELD_MUST_NOT_NULL, UserUIContext.getMessage(AccountI18nEnum.FORM_ACCOUNT_NAME)));
            }

            return txtField;
        }

        return null;
    }

    private class LeadFirstNamePrefixField extends CompoundCustomField<Lead> {
        private static final long serialVersionUID = 1L;

        @Override
        protected Component initContent() {
            MHorizontalLayout layout = new MHorizontalLayout().withFullWidth();

            final PrefixNameListSelect prefixSelect = new PrefixNameListSelect();
            prefixSelect.setValue(attachForm.getBean().getPrefixname());
            layout.addComponent(prefixSelect);

            prefixSelect.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    attachForm.getBean().setPrefixname((String) prefixSelect.getValue());
                }
            });

            TextField firstnameTxtField = new TextField();
            firstnameTxtField.setWidth("100%");
            firstnameTxtField.setNullRepresentation("");
            layout.addComponent(firstnameTxtField);
            layout.setExpandRatio(firstnameTxtField, 1.0f);

            // binding field group
            fieldGroup.bind(prefixSelect, "prefixname");
            fieldGroup.bind(firstnameTxtField, "firstname");

            return layout;
        }

        @Override
        public Class<? extends Lead> getType() {
            return Lead.class;
        }

    }
}
