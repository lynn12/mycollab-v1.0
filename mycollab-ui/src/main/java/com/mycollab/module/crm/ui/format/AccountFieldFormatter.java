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
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum.AccountType;
import com.mycollab.module.user.ui.format.UserHistoryFieldFormat;
import com.mycollab.vaadin.ui.formatter.CountryHistoryFieldFormat;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;

import static com.mycollab.module.crm.i18n.OptionI18nEnum.AccountIndustry;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class AccountFieldFormatter extends FieldGroupFormatter {
    private static final AccountFieldFormatter _instance = new AccountFieldFormatter();

    private AccountFieldFormatter() {
        generateFieldDisplayHandler("accountname", AccountI18nEnum.FORM_ACCOUNT_NAME);
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
        generateFieldDisplayHandler("phoneoffice", AccountI18nEnum.FORM_OFFICE_PHONE);
        generateFieldDisplayHandler("website", AccountI18nEnum.FORM_WEBSITE);
        generateFieldDisplayHandler("fax", AccountI18nEnum.FORM_FAX);
        generateFieldDisplayHandler("numemployees", AccountI18nEnum.FORM_EMPLOYEES);
        generateFieldDisplayHandler("alternatephone", AccountI18nEnum.FORM_OTHER_PHONE);
        generateFieldDisplayHandler("industry", AccountI18nEnum.FORM_INDUSTRY, new I18nHistoryFieldFormat(AccountIndustry.class));
        generateFieldDisplayHandler("email", GenericI18Enum.FORM_EMAIL);
        generateFieldDisplayHandler("type", GenericI18Enum.FORM_TYPE, new I18nHistoryFieldFormat(AccountType.class));
        generateFieldDisplayHandler("ownership", AccountI18nEnum.FORM_OWNERSHIP);
        generateFieldDisplayHandler("annualrevenue", AccountI18nEnum.FORM_ANNUAL_REVENUE);
        generateFieldDisplayHandler("billingaddress", AccountI18nEnum.FORM_BILLING_ADDRESS);
        generateFieldDisplayHandler("shippingaddress", AccountI18nEnum.FORM_SHIPPING_ADDRESS);
        generateFieldDisplayHandler("city", AccountI18nEnum.FORM_BILLING_CITY);
        generateFieldDisplayHandler("shippingcity", AccountI18nEnum.FORM_SHIPPING_CITY);
        generateFieldDisplayHandler("state", AccountI18nEnum.FORM_BILLING_STATE);
        generateFieldDisplayHandler("shippingstate", AccountI18nEnum.FORM_SHIPPING_STATE);
        generateFieldDisplayHandler("postalcode", AccountI18nEnum.FORM_BILLING_POSTAL_CODE);
        generateFieldDisplayHandler("shippingpostalcode", AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE);
        generateFieldDisplayHandler(Account.Field.billingcountry.name(), AccountI18nEnum.FORM_BILLING_COUNTRY, new CountryHistoryFieldFormat());
        generateFieldDisplayHandler(Account.Field.shippingcountry.name(), AccountI18nEnum.FORM_SHIPPING_COUNTRY, new CountryHistoryFieldFormat());
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
    }

    public static AccountFieldFormatter instance() {
        return _instance;
    }
}
