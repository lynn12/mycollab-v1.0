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

import com.mycollab.mobile.module.project.view.ProjectModuleController;
import com.mycollab.mobile.ui.AbstractMobileMenuPageView;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.IModule;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
@ViewComponent
public class ProjectModule extends AbstractMobileMenuPageView implements IModule {
    private static final long serialVersionUID = -537762284500231520L;

    public ProjectModule() {
        ControllerRegistry.addController(new ProjectModuleController((NavigationManager) UI.getCurrent().getContent()));
    }

    @Override
    protected void buildNavigateMenu() {

    }
}
