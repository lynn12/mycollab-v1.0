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
package com.mycollab.premium.module.user.accountsettings.view

import com.mycollab.common.UrlTokenizer
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.user.events.UserEvent
import com.mycollab.module.user.service.UserService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.MyCollabUI

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class UserUrlResolver extends AccountSettingUrlResolver {
  this.addSubResolver("list", new ListUrlResolver)
  this.addSubResolver("add", new AddUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)
  this.addSubResolver("preview", new PreviewUrlResolver)
  
  private class ListUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new UserEvent.GotoList(ListUrlResolver.this, null))
    }
  }
  
  private class AddUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new UserEvent.GotoAdd(AddUrlResolver.this, null))
    }
  }
  
  private class EditUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      val username = UrlTokenizer(params(0)).getString
      val userService = AppContextUtil.getSpringBean(classOf[UserService])
      val user = userService.findUserByUserNameInAccount(username, MyCollabUI.getAccountId)
      EventBusFactory.getInstance().post(new UserEvent.GotoEdit(EditUrlResolver.this, user))
    }
  }
  
  private class PreviewUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      val username = UrlTokenizer(params(0)).getString
      EventBusFactory.getInstance().post(new UserEvent.GotoRead(PreviewUrlResolver.this, username))
    }
  }
  
}