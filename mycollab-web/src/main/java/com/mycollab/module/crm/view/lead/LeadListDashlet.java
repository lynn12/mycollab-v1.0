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

package com.mycollab.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.fielddef.LeadTableFieldDef;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.Depot;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadListDashlet extends Depot {

    private LeadTableDisplay tableItem;

    public LeadListDashlet() {
        super(UserUIContext.getMessage(LeadI18nEnum.MY_ITEMS), new VerticalLayout());
        this.setMargin(new MarginInfo(true, false, false, false));

        tableItem = new LeadTableDisplay(Arrays.asList(LeadTableFieldDef.name(),
                LeadTableFieldDef.email(), LeadTableFieldDef.phoneoffice()));
        bodyContent.addComponent(tableItem);

        MButton customizeViewBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(new LeadListCustomizeWindow(tableItem)))
                .withIcon(FontAwesome.ADJUST).withStyleName(WebThemes.BUTTON_SMALL_PADDING);
        customizeViewBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.OPT_LAYOUT_OPTIONS));
        this.addHeaderElement(customizeViewBtn);
    }

    public void display() {
        final LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        criteria.setAssignUsers(new SetSearchField<>(UserUIContext.getUsername()));
        tableItem.setSearchCriteria(criteria);
    }
}
