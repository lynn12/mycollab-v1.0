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
package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.crm.events.CampaignEvent;
import com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.mycollab.mobile.module.crm.ui.CrmRelatedItemsScreenData;
import com.mycollab.mobile.module.crm.view.activity.ActivityRelatedItemView;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.IconConstants;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@ViewComponent
public class CampaignReadViewImpl extends AbstractPreviewItemComp<SimpleCampaign> implements CampaignReadView {
    private static final long serialVersionUID = 1718241963843463323L;

    private ActivityRelatedItemView associateActivities;
    private CampaignRelatedAccountView associateAccounts;
    private CampaignRelatedContactView associateContacts;
    private CampaignRelatedLeadView associateLeads;

    @Override
    public HasPreviewFormHandlers<SimpleCampaign> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected void initRelatedComponents() {
        associateActivities = new ActivityRelatedItemView(CrmTypeConstants.CAMPAIGN);
        associateAccounts = new CampaignRelatedAccountView();
        associateContacts = new CampaignRelatedContactView();
        associateLeads = new CampaignRelatedLeadView();
    }

    @Override
    protected void afterPreviewItem() {
        associateActivities.displayActivity(beanItem.getId());
        associateAccounts.displayAccounts(beanItem);
        associateContacts.displayContacts(beanItem);
        associateLeads.displayLeads(beanItem);
    }

    @Override
    protected String initFormHeader() {
        return beanItem.getCampaignname();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleCampaign> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.CAMPAIGN, CampaignDefaultDynaFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleCampaign> initBeanFormFieldFactory() {
        return new CampaignReadFormFieldFactory(this.previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm).createButtonControls(RolePermissionCollections.CRM_CAMPAIGN);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        HorizontalLayout toolbarLayout = new HorizontalLayout();
        toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        toolbarLayout.setSpacing(true);

        Button relatedAccounts = new Button();
        relatedAccounts.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_ACCOUNT
                + "\"></span><div class=\"screen-reader-text\">"
                + UserUIContext.getMessage(AccountI18nEnum.LIST)
                + "</div>");
        relatedAccounts.setHtmlContentAllowed(true);
        relatedAccounts.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new CampaignEvent.GoToRelatedItems(this,
                new CrmRelatedItemsScreenData(associateAccounts))));
        toolbarLayout.addComponent(relatedAccounts);

        Button relatedContacts = new Button();
        relatedContacts.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_CONTACT
                + "\"></span><div class=\"screen-reader-text\">"
                + UserUIContext.getMessage(ContactI18nEnum.LIST)
                + "</div>");
        relatedContacts.setHtmlContentAllowed(true);
        relatedContacts.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new CampaignEvent.GoToRelatedItems(this,
                new CrmRelatedItemsScreenData(associateContacts))));
        toolbarLayout.addComponent(relatedContacts);

        Button relatedLeads = new Button();
        relatedLeads.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_LEAD
                + "\"></span><div class=\"screen-reader-text\">"
                + UserUIContext.getMessage(LeadI18nEnum.LIST) + "</div>");
        relatedLeads.setHtmlContentAllowed(true);
        relatedLeads.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new CampaignEvent.GoToRelatedItems(this,
                new CrmRelatedItemsScreenData(associateLeads))));
        toolbarLayout.addComponent(relatedLeads);

        Button relatedActivities = new Button();
        relatedActivities.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_ACTIVITY
                + "\"></span><div class=\"screen-reader-text\">"
                + UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY)
                + "</div>");
        relatedActivities.setHtmlContentAllowed(true);
        relatedActivities.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new CampaignEvent.GoToRelatedItems(this,
                new CrmRelatedItemsScreenData(associateActivities))));
        toolbarLayout.addComponent(relatedActivities);

        return toolbarLayout;
    }

    @Override
    public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
        return associateActivities;
    }

    @Override
    public IRelatedListHandlers<SimpleAccount> getRelatedAccountHandlers() {
        return associateAccounts;
    }

    @Override
    public IRelatedListHandlers<SimpleContact> getRelatedContactHandlers() {
        return associateContacts;
    }

    @Override
    public IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers() {
        return associateLeads;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.CAMPAIGN;
    }
}
