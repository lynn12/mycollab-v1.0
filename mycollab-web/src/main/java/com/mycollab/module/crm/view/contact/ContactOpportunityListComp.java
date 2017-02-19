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
package com.mycollab.module.crm.view.contact;

import com.google.common.base.MoreObjects;
import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.domain.ContactOpportunity;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunitySalesStage;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.RelatedListComp2;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ContactOpportunityListComp extends RelatedListComp2<OpportunityService, OpportunitySearchCriteria, SimpleOpportunity> {
    private static final long serialVersionUID = 8849210168154580096L;

    private Contact contact;

    public ContactOpportunityListComp() {
        super(AppContextUtil.getSpringBean(OpportunityService.class), 20);
        setMargin(true);
        this.setBlockDisplayHandler(new ContactOpportunityBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        VerticalLayout controlsBtnWrap = new VerticalLayout();
        controlsBtnWrap.setWidth("100%");

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
            final SplitButton controlsBtn = new SplitButton();
            controlsBtn.addStyleName(WebThemes.BUTTON_ACTION);
            controlsBtn.setCaption(UserUIContext.getMessage(OpportunityI18nEnum.NEW));
            controlsBtn.setIcon(FontAwesome.PLUS);
            controlsBtn.addClickListener(event -> fireNewRelatedItem(""));
            controlsBtn.setSizeUndefined();
            MButton selectBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                ContactOpportunitySelectionWindow opportunitiesWindow = new ContactOpportunitySelectionWindow(
                        ContactOpportunityListComp.this);
                OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                UI.getCurrent().addWindow(opportunitiesWindow);
                opportunitiesWindow.setSearchCriteria(criteria);
                controlsBtn.setPopupVisible(false);
            }).withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));

            OptionPopupContent buttonControlsLayout = new OptionPopupContent();
            buttonControlsLayout.addOption(selectBtn);
            controlsBtn.setContent(buttonControlsLayout);
            controlsBtnWrap.addComponent(controlsBtn);
            controlsBtnWrap.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
        }
        return controlsBtnWrap;
    }

    void displayOpportunities(final Contact contact) {
        this.contact = contact;
        loadOpportunities();
    }

    private void loadOpportunities() {
        OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        criteria.setContactId(new NumberSearchField(contact.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadOpportunities();
    }

    private class ContactOpportunityBlockDisplay implements BlockDisplayHandler<SimpleOpportunity> {

        @Override
        public Component generateBlock(final SimpleOpportunity opportunity, int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout().withFullWidth();
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel opportunityIcon = ELabel.fontIcon(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
            iconWrap.addComponent(opportunityIcon);
            blockTop.addComponent(iconWrap);

            VerticalLayout opportunityInfo = new VerticalLayout();
            opportunityInfo.setSpacing(true);

            MButton btnDelete = new MButton("", clickEvent ->
                    ConfirmDialogExt.show(UI.getCurrent(),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, MyCollabUI.getSiteName()),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                            confirmDialog -> {
                                if (confirmDialog.isConfirmed()) {
                                    ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                                    ContactOpportunity associateOpportunity = new ContactOpportunity();
                                    associateOpportunity.setContactid(contact.getId());
                                    associateOpportunity.setOpportunityid(opportunity.getId());
                                    contactService.removeContactOpportunityRelationship(associateOpportunity, MyCollabUI.getAccountId());
                                    ContactOpportunityListComp.this.refresh();
                                }
                            })
            ).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_ICON_ONLY)
                    .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_CONTACT));

            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            Label opportunityName = ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_NAME) + ": " + new A(CrmLinkGenerator.generateCrmItemLink(
                    CrmTypeConstants.OPPORTUNITY, opportunity.getId())).appendText(opportunity.getOpportunityname()).write());

            opportunityInfo.addComponent(opportunityName);

            Label opportunityAmount = new Label(UserUIContext.getMessage(OpportunityI18nEnum.FORM_AMOUNT) + " : " +
                    MoreObjects.firstNonNull(opportunity.getAmount(), ""));
            opportunityInfo.addComponent(opportunityAmount);

            Label opportunitySaleStage = new Label(UserUIContext.getMessage(OpportunityI18nEnum.FORM_SALE_STAGE) + ": " +
                    UserUIContext.getMessage(OpportunitySalesStage.class, opportunity.getSalesstage()));
            opportunityInfo.addComponent(opportunitySaleStage);

            ELabel opportunityExpectedCloseDate = new ELabel(UserUIContext.getMessage(OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE) + ": " +
                    UserUIContext.formatPrettyTime(opportunity.getExpectedcloseddate())).withDescription(UserUIContext.formatDate(opportunity.getExpectedcloseddate()));
            opportunityInfo.addComponent(opportunityExpectedCloseDate);

            blockTop.with(opportunityInfo).expand(opportunityInfo);
            blockContent.addComponent(blockTop);

            blockContent.setWidth("100%");

            beanBlock.addComponent(blockContent);
            return beanBlock;
        }

    }
}
