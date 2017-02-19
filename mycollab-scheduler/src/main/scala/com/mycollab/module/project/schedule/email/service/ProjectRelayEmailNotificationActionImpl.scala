/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.schedule.email.service

import com.mycollab.schedule.email.SendingRelayEmailNotificationAction
import com.mycollab.common.domain.SimpleRelayEmailNotification

/**
  * @author MyCollab Ltd
  * @since 5.1.2
  */
class ProjectRelayEmailNotificationActionImpl extends SendingRelayEmailNotificationAction {
  override def sendNotificationForCreateAction(notification: SimpleRelayEmailNotification): Unit = {

  }

  override def sendNotificationForUpdateAction(notification: SimpleRelayEmailNotification): Unit = {

  }

  override def sendNotificationForCommentAction(notification: SimpleRelayEmailNotification): Unit = {

  }
}
