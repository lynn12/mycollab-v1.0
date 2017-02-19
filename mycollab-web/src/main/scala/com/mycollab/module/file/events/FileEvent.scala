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
package com.mycollab.module.file.events

import com.mycollab.events.ApplicationEvent
import com.mycollab.module.ecm.domain.Resource
import com.mycollab.module.ecm.domain.{ExternalDrive, Resource}

/**
  * @author MyCollab Ltd
  * @since 5.1.1
  */
object FileEvent {

  class GotoList(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class ResourceRemovedEvent(source: AnyRef, data: Resource) extends ApplicationEvent(source, data) {}

  class ExternalDriveConnectedEvent(source: AnyRef, data: ExternalDrive) extends ApplicationEvent(source, data) {}

  class ExternalDriveDeleteEvent(source: AnyRef, data: ExternalDrive) extends ApplicationEvent(source, data) {}

}
