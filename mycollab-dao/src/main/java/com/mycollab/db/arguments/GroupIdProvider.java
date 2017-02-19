/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.arguments;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class GroupIdProvider {
    private static GroupIdProvider instance;

    abstract public Integer getGroupId();

    abstract public String getGroupRequestedUser();

    public static void registerAccountIdProvider(GroupIdProvider provider) {
        instance = provider;
    }

    public static Integer getAccountId() {
        if (instance != null) {
            try {
                return instance.getGroupId();
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static String getRequestedUser() {
        if (instance != null) {
            try {
                return instance.getGroupRequestedUser();
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }
}
