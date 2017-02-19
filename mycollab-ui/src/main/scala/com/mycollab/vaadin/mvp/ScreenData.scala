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
package com.mycollab.vaadin.mvp

import com.mycollab.db.arguments.SearchCriteria

import scala.beans.BeanProperty

/**
  * @author MyCollab Ltd.
  * @since 5.0.3
  */
class ScreenData[P](@BeanProperty var params: P) {}

object ScreenData {

  class Add[P](params: P) extends ScreenData[P](params) {}

  class Edit[P](params: P) extends ScreenData[P](params) {}

  class Preview[P](params: P) extends ScreenData[P](params) {}

  class Search[P <: SearchCriteria](params: P) extends ScreenData(params) {}

}