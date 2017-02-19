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
package com.mycollab.module.user.view.component;

import com.mycollab.core.MyCollabException;
import com.mycollab.security.AccessPermissionFlag;
import com.mycollab.security.BooleanPermissionFlag;
import com.mycollab.security.PermissionFlag;
import com.mycollab.vaadin.web.ui.KeyCaptionComboBox;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PermissionComboBoxFactory {
    public static KeyCaptionComboBox createPermissionSelection(Class<? extends PermissionFlag> flag) {
        if (AccessPermissionFlag.class.isAssignableFrom(flag)) {
            return new AccessPermissionComboBox();
        } else if (BooleanPermissionFlag.class.isAssignableFrom(flag)) {
            return new YesNoPermissionComboBox();
        } else {
            throw new MyCollabException("Do not support permission flag " + flag);
        }
    }
}
