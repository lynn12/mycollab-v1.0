/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunityLeadSource;
import com.mycollab.module.user.ui.format.UserHistoryFieldFormat;
import com.mycollab.vaadin.ui.formatter.CountryHistoryFieldFormat;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class ContactFieldFormatter extends FieldGroupFormatter {
    private static final ContactFieldFormatter _instance = new ContactFieldFormatter();

    private ContactFieldFormatter() {
        generateFieldDisplayHandler("firstname", GenericI18Enum.FORM_FIRSTNAME);
        generateFieldDisplayHandler("lastname", GenericI18Enum.FORM_LASTNAME);
        generateFieldDisplayHandler("title", ContactI18nEnum.FORM_TITLE);
        generateFieldDisplayHandler("department", ContactI18nEnum.FORM_DEPARTMENT);
        generateFieldDisplayHandler("email", GenericI18Enum.FORM_EMAIL);
        generateFieldDisplayHandler("assistant", ContactI18nEnum.FORM_ASSISTANT);
        generateFieldDisplayHandler("assistantphone", ContactI18nEnum.FORM_ASSISTANT_PHONE);
        generateFieldDisplayHandler("leadsource", ContactI18nEnum.FORM_LEAD_SOURCE, new I18nHistoryFieldFormat(OpportunityLeadSource.class));
        generateFieldDisplayHandler("officephone", ContactI18nEnum.FORM_OFFICE_PHONE);
        generateFieldDisplayHandler("mobile", ContactI18nEnum.FORM_MOBILE);
        generateFieldDisplayHandler("homephone", ContactI18nEnum.FORM_HOME_PHONE);
        generateFieldDisplayHandler("otherphone", ContactI18nEnum.FORM_OTHER_PHONE);
        generateFieldDisplayHandler("birthday", ContactI18nEnum.FORM_BIRTHDAY, DATE_FIELD);
        generateFieldDisplayHandler(Contact.Field.fax.name(), ContactI18nEnum.FORM_FAX);
        generateFieldDisplayHandler("iscallable", ContactI18nEnum.FORM_IS_CALLABLE);
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
        generateFieldDisplayHandler("primaddress", ContactI18nEnum.FORM_PRIMARY_ADDRESS);
        generateFieldDisplayHandler("primcity", ContactI18nEnum.FORM_PRIMARY_CITY);
        generateFieldDisplayHandler("primstate", ContactI18nEnum.FORM_PRIMARY_STATE);
        generateFieldDisplayHandler("primpostalcode", ContactI18nEnum.FORM_PRIMARY_POSTAL_CODE);
        generateFieldDisplayHandler("primcountry", ContactI18nEnum.FORM_PRIMARY_COUNTRY, new CountryHistoryFieldFormat());
        generateFieldDisplayHandler("otheraddress", ContactI18nEnum.FORM_OTHER_ADDRESS);
        generateFieldDisplayHandler("othercity", ContactI18nEnum.FORM_OTHER_CITY);
        generateFieldDisplayHandler("otherstate", ContactI18nEnum.FORM_OTHER_STATE);
        generateFieldDisplayHandler("otherpostalcode", ContactI18nEnum.FORM_OTHER_POSTAL_CODE);
        generateFieldDisplayHandler("othercountry", ContactI18nEnum.FORM_OTHER_COUNTRY, new CountryHistoryFieldFormat());
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
        generateFieldDisplayHandler(Contact.Field.accountid.name(), ContactI18nEnum.FORM_ACCOUNTS, new AccountHistoryFieldFormat());
    }

    public static ContactFieldFormatter instance() {
        return _instance;
    }
}
