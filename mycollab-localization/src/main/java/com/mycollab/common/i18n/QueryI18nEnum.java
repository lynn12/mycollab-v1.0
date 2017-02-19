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
 * @author MyColab Ltd
 * @since 5.4.1
 */
public class QueryI18nEnum {
    @BaseName("common-querytring")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum StringI18nEnum {
        IS,
        IS_NOT,
        CONTAINS,
        NOT_CONTAINS,
        IS_EMPTY,
        IS_NOT_EMPTY
    }

    @BaseName("common-querynumber")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum NumberI18nEnum {
        EQUAL,
        NOT_EQUAL,
        LESS_THAN,
        LESS_THAN_EQUAL,
        GREATER_THAN,
        GREATER_THAN_EQUAL,
        IS_EMPTY,
        IS_NOT_EMPTY
    }

    @BaseName("common-querycollection")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum CollectionI18nEnum {
        IN,
        NOT_IN
    }
}
