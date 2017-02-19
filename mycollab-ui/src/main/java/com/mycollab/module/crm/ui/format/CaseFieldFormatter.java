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
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum.*;
import com.mycollab.module.user.ui.format.UserHistoryFieldFormat;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;

/**
 * @author MyCollab LTd
 * @since 5.1.4
 */
public class CaseFieldFormatter extends FieldGroupFormatter {
    private static final CaseFieldFormatter _instance = new CaseFieldFormatter();

    private CaseFieldFormatter() {
        generateFieldDisplayHandler("priority", CaseI18nEnum.FORM_PRIORITY, new I18nHistoryFieldFormat(CasePriority.class));
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS, new I18nHistoryFieldFormat(CaseStatus.class));
        generateFieldDisplayHandler("accountid", CaseI18nEnum.FORM_ACCOUNT, new AccountHistoryFieldFormat());
        generateFieldDisplayHandler("phonenumber", GenericI18Enum.FORM_PHONE);
        generateFieldDisplayHandler("origin", CaseI18nEnum.FORM_ORIGIN, new I18nHistoryFieldFormat(CaseOrigin.class));
        generateFieldDisplayHandler("type", GenericI18Enum.FORM_TYPE, new I18nHistoryFieldFormat(CaseType.class));
        generateFieldDisplayHandler("reason", CaseI18nEnum.FORM_REASON, new I18nHistoryFieldFormat(CaseReason.class));
        generateFieldDisplayHandler("subject", CaseI18nEnum.FORM_SUBJECT);
        generateFieldDisplayHandler("email", GenericI18Enum.FORM_EMAIL);
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
        generateFieldDisplayHandler("resolution", CaseI18nEnum.FORM_RESOLUTION, TRIM_HTMLS);
    }

    public static CaseFieldFormatter instance() {
        return _instance;
    }
}
