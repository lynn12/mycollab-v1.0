/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public final class GenericLinkUtils {
    public static final String URL_PREFIX_PARAM = "#";

    private GenericLinkUtils() {
    }

    /**
     * @param params
     * @return
     */
    public static String encodeParam(Object... params) {
        StringBuilder paramStr = new StringBuilder("");
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                return "";
            }
            paramStr.append(params[i].toString());
            if (i != params.length - 1) {
                paramStr.append("/");
            }
        }
        return UrlEncodeDecoder.encode(paramStr.toString());
    }
}
