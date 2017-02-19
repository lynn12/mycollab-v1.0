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
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.ActivityEvent;
import com.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.mycollab.mobile.ui.ConfirmDialog;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.mycollab.module.crm.domain.SimpleMeeting;
import com.mycollab.module.crm.domain.criteria.MeetingSearchCriteria;
import com.mycollab.module.crm.service.MeetingService;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class MeetingReadPresenter extends AbstractCrmPresenter<MeetingReadView> {
    private static final long serialVersionUID = 464611840929906889L;

    public MeetingReadPresenter() {
        super(MeetingReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleMeeting>() {
            @Override
            public void onEdit(SimpleMeeting data) {
                EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(this, data));
            }

            @Override
            public void onDelete(final SimpleMeeting data) {
                ConfirmDialog.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        dialog -> {
                            if (dialog.isConfirmed()) {
                                MeetingService campaignService = AppContextUtil.getSpringBean(MeetingService.class);
                                campaignService.removeWithSession(data, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                                EventBusFactory.getInstance().post(new ActivityEvent.GotoList(this, null));
                            }
                        });
            }

            @Override
            public void onClone(SimpleMeeting data) {
                MeetingWithBLOBs cloneData = (MeetingWithBLOBs) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ActivityEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleMeeting data) {
                MeetingService accountService = AppContextUtil.getSpringBean(MeetingService.class);
                MeetingSearchCriteria criteria = new MeetingSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER()));
                Integer nextId = accountService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ActivityEvent.MeetingRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }

            }

            @Override
            public void gotoPrevious(SimpleMeeting data) {
                MeetingService accountService = AppContextUtil.getSpringBean(MeetingService.class);
                MeetingSearchCriteria criteria = new MeetingSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN()));
                Integer nextId = accountService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ActivityEvent.MeetingRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canRead(RolePermissionCollections.CRM_MEETING)) {

            SimpleMeeting meeting;
            if (data.getParams() instanceof Integer) {
                MeetingService meetingService = AppContextUtil.getSpringBean(MeetingService.class);
                meeting = meetingService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
                if (meeting == null) {
                    NotificationUtil.showRecordNotExistNotification();
                    return;
                }
            } else {
                throw new MyCollabException("Invalid data: " + data);
            }
            view.previewItem(meeting);
            super.onGo(container, data);

            MyCollabUI.addFragment(CrmLinkGenerator.generateMeetingPreviewLink(meeting.getId()), UserUIContext
                    .getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                            "Meeting", meeting.getSubject()));
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
