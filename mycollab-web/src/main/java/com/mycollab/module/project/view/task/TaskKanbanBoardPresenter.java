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
package com.mycollab.module.project.view.task;

import com.mycollab.core.SecureAccessException;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.module.project.view.ticket.TicketContainer;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class TaskKanbanBoardPresenter extends ProjectGenericPresenter<TaskKanbanBoardView> {

    public TaskKanbanBoardPresenter() {
        super(TaskKanbanBoardView.class);
    }

    @Override
    protected void postInitView() {
        view.getSearchHandlers().addSearchHandler(this::doSearch);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.TASKS)) {
            TicketContainer ticketContainer = (TicketContainer) container;
            ticketContainer.navigateToContainer(ProjectTypeConstants.TASK);
            ticketContainer.setContent(view);
            view.display();

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.gotoTaskKanbanView();
        } else {
            throw new SecureAccessException();
        }
    }

    private void doSearch(TaskSearchCriteria searchCriteria) {
        view.queryTask(searchCriteria);
    }
}
