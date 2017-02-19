/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.activity

import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.mobile.module.crm.CrmUrlResolver
import com.mycollab.mobile.module.crm.CrmModuleScreenData
import com.mycollab.mobile.module.crm.events.CrmEvent
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum
import com.mycollab.vaadin.UserUIContext

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class ActivityUrlResolver extends CrmUrlResolver {
    this.addSubResolver("list", new ActivityListUrlResolver)
    this.addSubResolver("task", new ActivityTaskUrlResolver)
    this.addSubResolver("meeting", new MeetingUrlResolver)
    this.addSubResolver("call", new CallUrlResolver)

    class ActivityListUrlResolver extends CrmUrlResolver {
        protected override def handlePage(params: String*) {
            EventBusFactory.getInstance().post(new CrmEvent.GotoContainer(this,
                new CrmModuleScreenData.GotoModule(UserUIContext.getMessage(CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER))))
        }
    }

}
