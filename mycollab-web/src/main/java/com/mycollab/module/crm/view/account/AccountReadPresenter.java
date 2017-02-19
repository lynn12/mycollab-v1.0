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
package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.event.*;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.reporting.FormReportLayout;
import com.mycollab.reporting.PrintButton;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.AbstractRelatedListHandler;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountReadPresenter extends CrmGenericPresenter<AccountReadView> {
    private static final long serialVersionUID = 1L;

    public AccountReadPresenter() {
        super(AccountReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleAccount>() {
            @Override
            public void onEdit(SimpleAccount data) {
                EventBusFactory.getInstance().post(new AccountEvent.GotoEdit(this, data));
            }

            @Override
            public void onDelete(final SimpleAccount data) {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, MyCollabUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                                accountService.removeWithSession(data, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                                EventBusFactory.getInstance().post(new AccountEvent.GotoList(this, null));
                            }
                        });
            }

            @Override
            public void onAdd(SimpleAccount data) {
                EventBusFactory.getInstance().post(new AccountEvent.GotoAdd(this, null));
            }

            @Override
            public void onClone(SimpleAccount data) {
                Account cloneData = (Account) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new AccountEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onPrint(Object source, SimpleAccount data) {
                PrintButton btn = (PrintButton) source;
                btn.doPrint(data, new FormReportLayout(CrmTypeConstants.ACCOUNT, Account.Field.accountname.name(),
                        AccountDefaultDynaFormLayoutFactory.getForm()));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new AccountEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleAccount data) {
                AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                AccountSearchCriteria criteria = new AccountSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER()));
                Integer nextId = accountService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new AccountEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void gotoPrevious(SimpleAccount data) {
                AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                AccountSearchCriteria criteria = new AccountSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN()));
                Integer nextId = accountService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new AccountEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });

        view.getRelatedContactHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleContact>() {
            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleContact contact = new SimpleContact();
                contact.setAccountid(view.getItem().getId());
                EventBusFactory.getInstance().post(new ContactEvent.GotoEdit(this, contact));
            }

            @Override
            public void selectAssociateItems(Set<SimpleContact> items) {
                if (items.size() > 0) {
                    ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                    SimpleAccount account = view.getItem();
                    for (SimpleContact contact : items) {
                        contact.setAccountid(account.getId());
                        contactService.updateWithSession(contact, UserUIContext.getUsername());
                    }

                    view.getRelatedContactHandlers().refresh();
                }
            }
        });

        view.getRelatedOpportunityHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleOpportunity>() {
            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleOpportunity opportunity = new SimpleOpportunity();
                opportunity.setAccountid(view.getItem().getId());
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoEdit(this, opportunity));
            }
        });

        view.getRelatedLeadHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleLead>() {
            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleLead lead = new SimpleLead();
                lead.setAccountname(view.getItem().getAccountname());
                EventBusFactory.getInstance().post(new LeadEvent.GotoEdit(this, lead));
            }

            @Override
            public void selectAssociateItems(Set<SimpleLead> items) {
                if (items.size() > 0) {
                    SimpleAccount account = view.getItem();
                    List<AccountLead> associateLeads = new ArrayList<>();
                    for (SimpleLead contact : items) {
                        AccountLead assoLead = new AccountLead();
                        assoLead.setAccountid(account.getId());
                        assoLead.setLeadid(contact.getId());
                        assoLead.setCreatetime(new GregorianCalendar().getTime());
                        associateLeads.add(assoLead);
                    }

                    AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                    accountService.saveAccountLeadRelationship(associateLeads, MyCollabUI.getAccountId());

                    view.getRelatedLeadHandlers().refresh();
                }
            }
        });

        view.getRelatedCaseHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleCase>() {
            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleCase cases = new SimpleCase();
                cases.setAccountid(view.getItem().getId());
                EventBusFactory.getInstance().post(new CaseEvent.GotoEdit(this, cases));
            }
        });

        view.getRelatedActivityHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleActivity>() {
            @Override
            public void createNewRelatedItem(final String itemId) {
                if (itemId.equals("task")) {
                    SimpleCrmTask task = new SimpleCrmTask();
                    task.setType(CrmTypeConstants.ACCOUNT);
                    task.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(AccountReadPresenter.this, task));
                } else if (itemId.equals("meeting")) {
                    SimpleMeeting meeting = new SimpleMeeting();
                    meeting.setType(CrmTypeConstants.ACCOUNT);
                    meeting.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(AccountReadPresenter.this, meeting));
                } else if (itemId.equals("call")) {
                    SimpleCall call = new SimpleCall();
                    call.setType(CrmTypeConstants.ACCOUNT);
                    call.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.CallEdit(AccountReadPresenter.this, call));
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.ACCOUNT);
        if (UserUIContext.canRead(RolePermissionCollections.CRM_ACCOUNT)) {
            if (data.getParams() instanceof Integer) {
                AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                SimpleAccount account = accountService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
                if (account != null) {
                    super.onGo(container, data);
                    view.previewItem(account);
                    MyCollabUI.addFragment(CrmLinkGenerator.generateAccountPreviewLink(account.getId()),
                            UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                                    UserUIContext.getMessage(AccountI18nEnum.SINGLE), account.getAccountname()));
                } else {
                    throw new ResourceNotFoundException();
                }
            }
        } else {
            throw new SecureAccessException();
        }
    }
}
