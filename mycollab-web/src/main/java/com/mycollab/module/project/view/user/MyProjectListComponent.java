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
package com.mycollab.module.project.view.user;

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.SearchTextField;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collections;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MyProjectListComponent extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private ProjectSearchCriteria searchCriteria;

    private ProjectPagedList projectList;
    private Label titleLbl;
    private Enum currentTitleMsg;
    private boolean isSortAsc = true;

    public MyProjectListComponent() {
        withSpacing(false).withMargin(new MarginInfo(true, false, true, false));
        this.addStyleName("myprojectlist");

        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true))
                .withStyleName(WebThemes.PANEL_HEADER);
        titleLbl = new Label(UserUIContext.getMessage(ProjectCommonI18nEnum.WIDGET_ACTIVE_PROJECTS_TITLE, 0));

        final MButton sortBtn = new MButton("").withIcon(FontAwesome.SORT_ALPHA_ASC).withStyleName(WebThemes.BUTTON_ICON_ONLY);
        sortBtn.addClickListener(clickEvent -> {
            isSortAsc = !isSortAsc;
            if (searchCriteria != null) {
                if (isSortAsc) {
                    sortBtn.setIcon(FontAwesome.SORT_ALPHA_ASC);
                    searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("name", SearchCriteria.ASC)));
                } else {
                    sortBtn.setIcon(FontAwesome.SORT_ALPHA_DESC);
                    searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("name", SearchCriteria.DESC)));
                }
                displayResults();
            }
        });

        final SearchTextField searchTextField = new SearchTextField() {
            @Override
            public void doSearch(String value) {
                searchCriteria = getAllProjectsSearchCriteria();
                searchCriteria.setProjectName(StringSearchField.and(value));
                displayResults();
            }

            @Override
            public void emptySearch() {
                searchCriteria = getAllProjectsSearchCriteria();
                searchCriteria.setProjectName(null);
                displayResults();
            }
        };
        searchTextField.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        final PopupButton projectsPopup = new PopupButton("");
        projectsPopup.setIcon(FontAwesome.CARET_SQUARE_O_DOWN);
        projectsPopup.addStyleName(WebThemes.BUTTON_ICON_ONLY);

        OptionPopupContent filterBtnLayout = new OptionPopupContent();

        ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
        int allProjectCount = projectService.getTotalCount(getAllProjectsSearchCriteria());
        Button allProjectsBtn = new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.BUTTON_ALL_PROJECTS, allProjectCount), clickEvent -> {
            displayAllProjects();
            projectsPopup.setPopupVisible(false);
        });
        filterBtnLayout.addOption(allProjectsBtn);

        int activeProjectsCount = projectService.getTotalCount(getActiveProjectsSearchCriteria());
        Button activeProjectsBtn = new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.BUTTON_ACTIVE_PROJECTS, activeProjectsCount), clickEvent -> {
            displayActiveProjects();
            projectsPopup.setPopupVisible(false);
        });
        filterBtnLayout.addOption(activeProjectsBtn);

        int archiveProjectsCount = projectService.getTotalCount(getArchivedProjectsSearchCriteria());
        Button archiveProjectsBtn = new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.BUTTON_ARCHIVE_PROJECTS, archiveProjectsCount), clickEvent -> {
            displayArchiveProjects();
            projectsPopup.setPopupVisible(false);
        });
        filterBtnLayout.addOption(archiveProjectsBtn);
        projectsPopup.setContent(filterBtnLayout);

        header.with(titleLbl, sortBtn, searchTextField, projectsPopup).expand(titleLbl).alignAll(Alignment.MIDDLE_LEFT);

        this.projectList = new ProjectPagedList();
        this.with(header, projectList);
    }

    public void displayDefaultProjectsList() {
        displayActiveProjects();
    }

    private ProjectSearchCriteria getAllProjectsSearchCriteria() {
        ProjectSearchCriteria prjSearchCriteria = new ProjectSearchCriteria();
        prjSearchCriteria.setInvolvedMember(StringSearchField.and(UserUIContext.getUsername()));
        prjSearchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("name", SearchCriteria.ASC)));
        return prjSearchCriteria;
    }

    private ProjectSearchCriteria getActiveProjectsSearchCriteria() {
        ProjectSearchCriteria prjSearchCriteria = new ProjectSearchCriteria();
        prjSearchCriteria.setInvolvedMember(StringSearchField.and(UserUIContext.getUsername()));
        prjSearchCriteria.setProjectStatuses(new SetSearchField<>(StatusI18nEnum.Open.name()));
        prjSearchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("name", SearchCriteria.ASC)));
        return prjSearchCriteria;
    }

    private ProjectSearchCriteria getArchivedProjectsSearchCriteria() {
        ProjectSearchCriteria prjSearchCriteria = new ProjectSearchCriteria();
        prjSearchCriteria.setInvolvedMember(StringSearchField.and(UserUIContext.getUsername()));
        prjSearchCriteria.setProjectStatuses(new SetSearchField<>(StatusI18nEnum.Archived.name()));
        prjSearchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("name", SearchCriteria.ASC)));
        return prjSearchCriteria;
    }

    private void displayResults() {
        projectList.setSearchCriteria(searchCriteria);
        int totalCount = projectList.getTotalCount();
        titleLbl.setValue(UserUIContext.getMessage(currentTitleMsg, totalCount));
    }

    private void displayAllProjects() {
        searchCriteria = getAllProjectsSearchCriteria();
        currentTitleMsg = ProjectCommonI18nEnum.WIDGET_ALL_PROJECTS_TITLE;
        displayResults();
    }

    private void displayActiveProjects() {
        searchCriteria = getActiveProjectsSearchCriteria();
        currentTitleMsg = ProjectCommonI18nEnum.WIDGET_ACTIVE_PROJECTS_TITLE;
        displayResults();
    }

    private void displayArchiveProjects() {
        searchCriteria = getArchivedProjectsSearchCriteria();
        currentTitleMsg = ProjectCommonI18nEnum.WIDGET_ARCHIVE_PROJECTS_TITLE;
        displayResults();
    }
}