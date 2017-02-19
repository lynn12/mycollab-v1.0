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

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.domain.CallWithBLOBs;
import com.mycollab.module.crm.event.ActivityEvent;
import com.mycollab.module.crm.i18n.CallI18nEnum;
import com.mycollab.module.crm.service.CallService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.IEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CallAddPresenter extends CrmGenericPresenter<CallAddView> {
    private static final long serialVersionUID = 1L;

    public CallAddPresenter() {
        super(CallAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<CallWithBLOBs>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final CallWithBLOBs item) {
                save(item);
                EventBusFactory.getInstance().post(new ActivityEvent.CallRead(this, item.getId()));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ActivityEvent.GotoTodoList(this, null));
            }

            @Override
            public void onSaveAndNew(final CallWithBLOBs item) {
                save(item);
                EventBusFactory.getInstance().post(new ActivityEvent.CallAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CALL)) {
            CallWithBLOBs call;

            if (data.getParams() instanceof Integer) {
                CallService callService = AppContextUtil.getSpringBean(CallService.class);
                call = callService.findByPrimaryKey((Integer) data.getParams(), MyCollabUI.getAccountId());
                if (call == null) {
                    NotificationUtil.showRecordNotExistNotification();
                    return;
                }
            } else if (data.getParams() instanceof CallWithBLOBs) {
                call = (CallWithBLOBs) data.getParams();
            } else {
                throw new MyCollabException("Invalid data: " + data);
            }

            super.onGo(container, data);
            view.editItem(call);

            if (call.getId() == null) {
                MyCollabUI.addFragment("crm/activity/call/add/", UserUIContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                        UserUIContext.getMessage(CallI18nEnum.SINGLE)));
            } else {
                MyCollabUI.addFragment("crm/activity/call/edit/" + UrlEncodeDecoder.encode(call.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                UserUIContext.getMessage(CallI18nEnum.SINGLE), call.getSubject()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    public void save(CallWithBLOBs item) {
        CallService taskService = AppContextUtil.getSpringBean(CallService.class);

        item.setSaccountid(MyCollabUI.getAccountId());
        if (item.getId() == null) {
            taskService.saveWithSession(item, UserUIContext.getUsername());
        } else {
            taskService.updateWithSession(item, UserUIContext.getUsername());
        }
    }
}
