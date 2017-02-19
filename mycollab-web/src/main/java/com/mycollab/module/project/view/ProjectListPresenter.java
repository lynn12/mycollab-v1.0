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
package com.mycollab.module.project.view;

import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.mycollab.vaadin.web.ui.ListSelectionPresenter;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HasComponents;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ProjectListPresenter extends ListSelectionPresenter<ProjectListView, ProjectSearchCriteria, SimpleProject> {

    private ProjectService projectService;

    public ProjectListPresenter() {
        super(ProjectListView.class);
        projectService = AppContextUtil.getSpringBean(ProjectService.class);
    }

    @Override
    protected void viewAttached() {
        super.viewAttached();

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {
            @Override
            protected void onSelectExtra(String id) {

            }

            @Override
            protected String getReportTitle() {
                return UserUIContext.getMessage(ProjectI18nEnum.LIST);
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleProject.class;
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ComponentContainer componentContainer = (ComponentContainer) container;
        componentContainer.removeAllComponents();
        componentContainer.addComponent(view);
        ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
        doSearch(searchCriteria);
    }

    @Override
    public void doSearch(ProjectSearchCriteria searchCriteria) {
        Collection<Integer> prjKeys = projectService.getProjectKeysUserInvolved(UserUIContext.getUsername(), MyCollabUI.getAccountId());
        if (CollectionUtils.isNotEmpty(prjKeys)) {
            searchCriteria.setProjectKeys(new SetSearchField<>(prjKeys));
            super.doSearch(searchCriteria);
        }
    }

    @Override
    public ISearchableService<ProjectSearchCriteria> getSearchService() {
        return projectService;
    }

    @Override
    protected void deleteSelectedItems() {
        throw new UnsupportedOperationException("Not supported");
    }
}
