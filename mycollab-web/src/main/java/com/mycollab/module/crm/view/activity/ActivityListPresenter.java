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
package com.mycollab.module.crm.view.activity;

import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.dao.CallMapper;
import com.mycollab.module.crm.dao.CrmTaskMapper;
import com.mycollab.module.crm.dao.MeetingMapper;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.service.EventService;
import com.mycollab.module.crm.view.CrmGenericListPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.ViewItemAction;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.mycollab.vaadin.web.ui.MailFormWindow;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivityListPresenter extends CrmGenericListPresenter<ActivityListView, ActivitySearchCriteria, SimpleActivity> {
    private static final long serialVersionUID = 1L;

    public ActivityListPresenter() {
        super(ActivityListView.class);
    }

    @Override
    protected void postInitView() {
        super.postInitView();

        view.getPopupActionHandlers().setMassActionHandler(
                new DefaultMassEditActionHandler(this) {

                    @Override
                    protected void onSelectExtra(String id) {
                        if (ViewItemAction.MAIL_ACTION().equals(id)) {
                            UI.getCurrent().addWindow(new MailFormWindow());
                        }
                    }

                    @Override
                    protected String getReportTitle() {
                        return "Event List";
                    }

                    @Override
                    protected Class<?> getReportModelClassType() {
                        return SimpleActivity.class;
                    }
                });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.ACTIVITY);
        if (UserUIContext.canRead(RolePermissionCollections.CRM_MEETING)
                || UserUIContext.canRead(RolePermissionCollections.CRM_TASK)
                || UserUIContext.canRead(RolePermissionCollections.CRM_CALL)) {
            searchCriteria = (ActivitySearchCriteria) data.getParams();
            this.displayListView(container, data);
            doSearch(searchCriteria);

            MyCollabUI.addFragment("crm/activity/todo", "Assignments");
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    @Override
    protected void deleteSelectedItems() {
        Collection<SimpleActivity> currentDataList = view.getPagedBeanTable().getCurrentDataList();
        List<Integer> keyListCall = new ArrayList<>();
        List<Integer> keyListMeeting = new ArrayList<>();
        List<Integer> keyListTask = new ArrayList<>();
        if (!isSelectAll) {
            for (SimpleActivity item : currentDataList) {
                if (item.isSelected()) {
                    if (CrmTypeConstants.CALL.equals(item.getEventType())) {
                        keyListCall.add(item.getId());
                    } else if (CrmTypeConstants.MEETING.equals(item.getEventType())) {
                        keyListMeeting.add(item.getId());
                    } else if (CrmTypeConstants.TASK
                            .equals(item.getEventType())) {
                        keyListTask.add(item.getId());
                    }
                }
            }
        } else {
            for (SimpleActivity item : currentDataList) {
                if ((CrmTypeConstants.CALL.equals(item.getEventType()))) {
                    keyListCall.add(item.getId());
                } else if (CrmTypeConstants.MEETING.equals(item.getEventType())) {
                    keyListMeeting.add(item.getId());
                } else if (CrmTypeConstants.TASK.equals(item.getEventType())) {
                    keyListTask.add(item.getId());
                }
            }
        }

        if (keyListCall.size() > 0) {
            CallMapper callService = AppContextUtil.getSpringBean(CallMapper.class);
            callService.removeKeysWithSession(keyListCall);
        }

        if (keyListMeeting.size() > 0) {
            MeetingMapper meetingService = AppContextUtil.getSpringBean(MeetingMapper.class);
            meetingService.removeKeysWithSession(keyListMeeting);
        }

        if (keyListTask.size() > 0) {
            CrmTaskMapper taskService = AppContextUtil.getSpringBean(CrmTaskMapper.class);
            taskService.removeKeysWithSession(keyListTask);
        }
        doSearch(searchCriteria);
        checkWhetherEnableTableActionControl();
    }

    @Override
    public ISearchableService<ActivitySearchCriteria> getSearchService() {
        return AppContextUtil.getSpringBean(EventService.class);
    }

}
