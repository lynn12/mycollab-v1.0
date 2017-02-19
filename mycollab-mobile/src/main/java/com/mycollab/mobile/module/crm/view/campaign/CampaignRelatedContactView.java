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

import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CampaignRelatedContactView extends AbstractRelatedListView<SimpleContact, ContactSearchCriteria> {
    private static final long serialVersionUID = 366429952188752174L;
    private SimpleCampaign campaign;

    public CampaignRelatedContactView() {
        super();
        setCaption(UserUIContext.getMessage(ContactI18nEnum.M_TITLE_RELATED_CONTACTS));
        this.itemList = new ContactListDisplay();
        this.setContent(this.itemList);
    }

    private void loadContacts() {
        final ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        searchCriteria.setCampaignId(new NumberSearchField(campaign.getId()));
        this.itemList.search(searchCriteria);
    }

    public void displayContacts(SimpleCampaign campaign) {
        this.campaign = campaign;
        loadContacts();
    }

    @Override
    public void refresh() {
        loadContacts();
    }

    @Override
    protected Component createRightComponent() {
        final NavigationBarQuickMenu addContact = new NavigationBarQuickMenu();
        addContact.setStyleName("add-btn");

        MVerticalLayout addButtons = new MVerticalLayout().withFullWidth();

        Button newContactBtn = new Button(UserUIContext.getMessage(ContactI18nEnum.NEW), clickEvent -> fireNewRelatedItem(""));
        addButtons.addComponent(newContactBtn);

        Button selectContactBtn = new Button(UserUIContext.getMessage(ContactI18nEnum.M_TITLE_SELECT_CONTACTS), clickEvent -> {
            CampaignContactSelectionView contactSelectionView = new CampaignContactSelectionView(CampaignRelatedContactView.this);
            ContactSearchCriteria criteria = new ContactSearchCriteria();
            criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
            contactSelectionView.setSearchCriteria(criteria);
            EventBusFactory.getInstance().post(new ShellEvent.PushView(CampaignRelatedContactView.this, contactSelectionView));
        });
        addButtons.addComponent(selectContactBtn);
        addContact.setContent(addButtons);
        return addContact;
    }
}
