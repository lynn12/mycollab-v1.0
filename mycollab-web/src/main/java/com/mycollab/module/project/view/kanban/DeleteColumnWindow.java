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
package com.mycollab.module.project.view.kanban;

import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.view.IKanbanView;
import com.mycollab.vaadin.UserUIContext;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class DeleteColumnWindow extends MWindow {
    public DeleteColumnWindow(final IKanbanView kanbanView, final String type) {
        super(UserUIContext.getMessage(TaskI18nEnum.ACTION_DELETE_COLUMNS));
        this.withWidth("800px").withModal(true).withResizable(false).withCenter();
    }
}
