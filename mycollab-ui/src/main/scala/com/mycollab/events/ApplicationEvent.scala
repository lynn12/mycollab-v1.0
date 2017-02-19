/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.events

import java.util.EventObject

import scala.beans.BeanProperty

/**
  * Serves as a parent for all application level event. It holds the source that
  * triggered the event and enforces each event implementation to provide an
  * appropriate description for the event.
  *
  * @author MyCollab Ltd.
  * @since 5.0.3
  */
class ApplicationEvent(source: AnyRef, @BeanProperty val data: Any) extends EventObject(source) {}
