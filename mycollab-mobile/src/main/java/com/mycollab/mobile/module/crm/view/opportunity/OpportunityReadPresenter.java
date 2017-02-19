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
package com.mycollab.mobile.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.ActivityEvent;
import com.mycollab.mobile.module.crm.events.ContactEvent;
import com.mycollab.mobile.module.crm.events.LeadEvent;
import com.mycollab.mobile.module.crm.events.OpportunityEvent;
import com.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.ConfirmDialog;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.RelatedListHandler;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class OpportunityReadPresenter extends AbstractCrmPresenter<OpportunityReadView> {
    private static final long serialVersionUID = -1981281467054000670L;

    public OpportunityReadPresenter() {
        super(OpportunityReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleOpportunity>() {
            @Override
            public void onEdit(SimpleOpportunity data) {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(SimpleOpportunity data) {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleOpportunity data) {
                ConfirmDialog.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        dialog -> {
                            if (dialog.isConfirmed()) {
                                OpportunityService OpportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                                OpportunityService.removeWithSession(data, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                                EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
                            }
                        });
            }

            @Override
            public void onClone(SimpleOpportunity data) {
                SimpleOpportunity cloneData = (SimpleOpportunity) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleOpportunity data) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER()));
                Integer nextId = opportunityService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new OpportunityEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void gotoPrevious(SimpleOpportunity data) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN()));
                Integer nextId = opportunityService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new OpportunityEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
        view.getRelatedContactHandlers().addRelatedListHandler(new RelatedListHandler<SimpleContact>() {

            @Override
            public void selectAssociateItems(Set<SimpleContact> items) {
                List<ContactOpportunity> associateContacts = new ArrayList<>();
                SimpleOpportunity opportunity = view.getItem();
                for (SimpleContact contact : items) {
                    ContactOpportunity associateContact = new ContactOpportunity();
                    associateContact.setContactid(contact.getId());
                    associateContact.setOpportunityid(opportunity.getId());
                    associateContact.setCreatedtime(new GregorianCalendar().getTime());
                    associateContacts.add(associateContact);
                }

                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                contactService.saveContactOpportunityRelationship(associateContacts, MyCollabUI.getAccountId());
                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
            }

            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleContact contact = new SimpleContact();
                contact.setExtraData(view.getItem());
                EventBusFactory.getInstance().post(new ContactEvent.GotoEdit(OpportunityReadPresenter.this, contact));
            }
        });
        view.getRelatedLeadHandlers().addRelatedListHandler(new RelatedListHandler<SimpleLead>() {

            @Override
            public void selectAssociateItems(Set<SimpleLead> items) {
                SimpleOpportunity opportunity = view.getItem();
                List<OpportunityLead> associateLeads = new ArrayList<>();
                for (SimpleLead lead : items) {
                    OpportunityLead associateLead = new OpportunityLead();
                    associateLead.setLeadid(lead.getId());
                    associateLead.setOpportunityid(opportunity.getId());
                    associateLead.setCreatedtime(new GregorianCalendar().getTime());
                    associateLeads.add(associateLead);
                }

                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                opportunityService.saveOpportunityLeadRelationship(associateLeads, MyCollabUI.getAccountId());
                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
            }

            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleLead lead = new SimpleLead();
                lead.setExtraData(view.getItem());
                EventBusFactory.getInstance().post(new LeadEvent.GotoEdit(OpportunityReadPresenter.this, lead));
            }
        });
        view.getRelatedActivityHandlers().addRelatedListHandler(new RelatedListHandler<SimpleActivity>() {

            @Override
            public void selectAssociateItems(Set<SimpleActivity> items) {
                // TODO Auto-generated method stub

            }

            @Override
            public void createNewRelatedItem(String itemId) {
                if (itemId.equals(CrmTypeConstants.TASK)) {
                    final SimpleCrmTask task = new SimpleCrmTask();
                    task.setType(CrmTypeConstants.ACCOUNT);
                    task.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(OpportunityReadPresenter.this, task));
                } else if (itemId.equals(CrmTypeConstants.MEETING)) {
                    final SimpleMeeting meeting = new SimpleMeeting();
                    meeting.setType(CrmTypeConstants.ACCOUNT);
                    meeting.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(OpportunityReadPresenter.this, meeting));
                } else if (itemId.equals(CrmTypeConstants.CALL)) {
                    final SimpleCall call = new SimpleCall();
                    call.setType(CrmTypeConstants.ACCOUNT);
                    call.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.CallEdit(OpportunityReadPresenter.this, call));
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {

            if (data.getParams() instanceof Integer) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                SimpleOpportunity opportunity = opportunityService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
                if (opportunity != null) {
                    view.previewItem(opportunity);
                    super.onGo(container, data);

                    MyCollabUI.addFragment(CrmLinkGenerator.generateOpportunityPreviewLink(opportunity.getId()), UserUIContext.getMessage(
                            GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE, "Opportunity", opportunity.getOpportunityname()));
                } else {
                    NotificationUtil.showRecordNotExistNotification();
                }
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
