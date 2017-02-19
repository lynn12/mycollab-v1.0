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
package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.ContactEvent;
import com.mycollab.mobile.module.crm.ui.AbstractListViewComp;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class ContactListViewImpl extends AbstractListViewComp<ContactSearchCriteria, SimpleContact> implements ContactListView {
    private static final long serialVersionUID = 8271856163726726780L;

    public ContactListViewImpl() {
        super();
        setCaption(UserUIContext.getMessage(ContactI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<ContactSearchCriteria, SimpleContact> createBeanTable() {
        ContactListDisplay contactListDisplay = new ContactListDisplay();
        return contactListDisplay;
    }

    @Override
    protected Component createRightComponent() {
        Button addContact = new Button();
        addContact.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent arg0) {
                EventBusFactory.getInstance().post(new ContactEvent.GotoAdd(this, null));
            }
        });
        addContact.setStyleName("add-btn");
        return addContact;
    }
}
