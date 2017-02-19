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
package com.mycollab.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.fielddef.ContactTableFieldDef;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.mycollab.module.crm.view.contact.ContactSearchPanel;
import com.mycollab.module.crm.view.contact.ContactTableDisplay;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CaseContactSelectionWindow extends RelatedItemSelectionWindow<SimpleContact, ContactSearchCriteria> {

    public CaseContactSelectionWindow(CaseContactListComp associateContactList) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(ContactI18nEnum.LIST)), associateContactList);
        this.setWidth("1000px");
    }

    @Override
    protected void initUI() {
        tableItem = new ContactTableDisplay(ContactTableFieldDef.selected(),
                Arrays.asList(ContactTableFieldDef.name(), ContactTableFieldDef.email(),
                        ContactTableFieldDef.phoneOffice(), ContactTableFieldDef.account()));

        tableItem.addGeneratedColumn("contactName", (source, itemId, columnId) -> {
            final SimpleContact contact = tableItem.getBeanByIndex(itemId);

            return new ELabel(contact.getContactName()).withStyleName(WebThemes.BUTTON_LINK)
                    .withDescription(CrmTooltipGenerator.generateToolTipContact(UserUIContext.getUserLocale(), MyCollabUI.getDateFormat(),
                            contact, MyCollabUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
        });

        MButton selectBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_ACTION);

        ContactSearchPanel searchPanel = new ContactSearchPanel();
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));

        bodyContent.with(searchPanel, selectBtn, tableItem);
    }
}
