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

package com.mycollab.module.project.view.settings;

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugVersionEvent;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.module.tracker.service.VersionService;
import com.mycollab.reporting.FormReportLayout;
import com.mycollab.reporting.PrintButton;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class VersionReadPresenter extends AbstractPresenter<VersionReadView> {
    private static final long serialVersionUID = 1L;

    public VersionReadPresenter() {
        super(VersionReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<Version>() {
            @Override
            public void onEdit(Version data) {
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(Version data) {
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(Version data) {
                VersionService versionService = AppContextUtil.getSpringBean(VersionService.class);
                versionService.removeWithSession(data, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoList(this, null));
            }

            @Override
            public void onClone(Version data) {
                Version cloneData = (Version) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoList(this, null));
            }

            @Override
            public void onPrint(Object source, Version data) {
                PrintButton btn = (PrintButton) source;
                btn.doPrint(data, new FormReportLayout(ProjectTypeConstants.BUG_VERSION, Version.Field.name.name(),
                        VersionDefaultFormLayoutFactory.getForm(), Version.Field.id.name()));
            }

            @Override
            public void gotoNext(Version data) {
                VersionService componentService = AppContextUtil.getSpringBean(VersionService.class);
                VersionSearchCriteria criteria = new VersionSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER()));
                Integer nextId = componentService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void gotoPrevious(Version data) {
                VersionService componentService = AppContextUtil.getSpringBean(VersionService.class);
                VersionSearchCriteria criteria = new VersionSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN()));
                Integer nextId = componentService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.VERSIONS)) {
            if (data.getParams() instanceof Integer) {
                VersionService componentService = AppContextUtil.getSpringBean(VersionService.class);
                Version version = componentService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
                if (version != null) {
                    VersionContainer versionContainer = (VersionContainer) container;
                    versionContainer.removeAllComponents();
                    versionContainer.addComponent(view);
                    view.previewItem(version);

                    ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                    breadcrumb.gotoVersionRead(version);
                } else {
                    NotificationUtil.showRecordNotExistNotification();
                }
            } else {
                throw new MyCollabException("Unhanddle this case yet");
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
