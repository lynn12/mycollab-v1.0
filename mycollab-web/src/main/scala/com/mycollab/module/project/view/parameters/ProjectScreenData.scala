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
package com.mycollab.module.project.view.parameters

import com.mycollab.module.project.domain.Project
import com.mycollab.vaadin.mvp.ScreenData

/**
  * @author MyCollab Ltd.
  * @since 5.0.3
  */
object ProjectScreenData {

  class GotoList() extends ScreenData(null) {}

  class Goto(params: Integer) extends ScreenData[Integer](params) {}

  class Edit(params: Project) extends ScreenData[Project](params) {}

  class GotoTagList(params: Object) extends ScreenData[Object](params) {}

  class GotoFavorite() extends ScreenData[Object](null) {}

  class SearchItem(params: String) extends ScreenData[String](params) {}

  class GotoGanttChart extends ScreenData {}

  class GotoReportConsole extends ScreenData {}

  class GotoCalendarView extends ScreenData {}

}
