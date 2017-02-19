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
package com.mycollab.module.user.accountsettings.view

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
  * @author MyCollab Ltd
  * @since 5.2.12
  */
object RoleTableFieldDef {
  val selected = new TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH)
  val rolename = new TableViewField(GenericI18Enum.FORM_NAME, "rolename", WebUIConstants.TABLE_EX_LABEL_WIDTH)
  val isDefault = new TableViewField(RoleI18nEnum.FORM_IS_DEFAULT, "isdefault", WebUIConstants.TABLE_M_LABEL_WIDTH)
  val description = new TableViewField(GenericI18Enum.FORM_DESCRIPTION, "description", WebUIConstants.TABLE_EX_LABEL_WIDTH)
}
