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
package com.mycollab.mobile.module.project.view;

import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectDashboardPresenter extends AbstractProjectPresenter<ProjectDashboardView> {
    private static final long serialVersionUID = -2645763046888609751L;

    public ProjectDashboardPresenter() {
        super(ProjectDashboardView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (data instanceof ProjectScreenData.Edit) {
            // TODO: Handle edit project
        } else {
            if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.PROJECT)) {
                super.onGo(container, data);
                view.displayDashboard();
            } else {
                NotificationUtil.showMessagePermissionAlert();
            }
        }
    }
}
