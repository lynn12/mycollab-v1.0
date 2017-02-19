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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.mycollab.module.project.event.ProjectRoleEvent;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRoleReadPresenter extends AbstractPresenter<ProjectRoleReadView> {
    private static final long serialVersionUID = 1L;

    private ProjectRoleService projectRoleService = AppContextUtil.getSpringBean(ProjectRoleService.class);

    public ProjectRoleReadPresenter() {
        super(ProjectRoleReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleProjectRole>() {
            @Override
            public void onEdit(SimpleProjectRole data) {
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(SimpleProjectRole data) {
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleProjectRole role) {
                if (Boolean.FALSE.equals(role.getIssystemrole())) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, MyCollabUI.getSiteName()),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                            confirmDialog -> {
                                if (confirmDialog.isConfirmed()) {
                                    projectRoleService.removeWithSession(role, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                                    EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoList(this, null));
                                }
                            });
                } else {
                    NotificationUtil.showErrorNotification(UserUIContext
                            .getMessage(ProjectMemberI18nEnum.CAN_NOT_DELETE_ROLE_MESSAGE, role.getRolename()));
                }
            }

            @Override
            public void onClone(SimpleProjectRole data) {
                SimpleProjectRole cloneData = (SimpleProjectRole) data.copy();
                cloneData.setRolename(null);
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoAdd(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleProjectRole data) {
                ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER()));
                Integer nextId = projectRoleService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void gotoPrevious(SimpleProjectRole data) {
                ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN()));
                Integer nextId = projectRoleService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectRoleContainer roleContainer = (ProjectRoleContainer) container;

        if (data.getParams() instanceof Integer) {
            SimpleProjectRole role = projectRoleService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
            if (role == null) {
                NotificationUtil.showRecordNotExistNotification();
            } else {
                roleContainer.removeAllComponents();
                roleContainer.addComponent(view);
                view.previewItem(role);
                ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                breadCrumb.gotoRoleRead(role);
            }
        } else {
            throw new MyCollabException("Do not support screen data: " + data);
        }
    }
}
