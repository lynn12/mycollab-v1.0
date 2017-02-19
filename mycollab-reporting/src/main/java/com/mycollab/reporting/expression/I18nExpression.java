/**
 * This file is part of mycollab-reporting.
 *
 * mycollab-reporting is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-reporting is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-reporting.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.reporting.expression;

import com.mycollab.i18n.LocalizationHelper;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 5.0.5
 */
public class I18nExpression extends SimpleFieldExpression {
    private Class keyCls;

    public I18nExpression(String field, Class keyCls) {
        super(field);
        this.keyCls = keyCls;
    }

    @Override
    public String evaluate(ReportParameters reportParameters) {
        Locale locale = reportParameters.getLocale();
        String stringValue = reportParameters.getFieldValue(field).toString();
        try {
            return LocalizationHelper.getMessage(locale, keyCls, stringValue);
        } catch (Exception e) {
            return stringValue;
        }
    }
}
