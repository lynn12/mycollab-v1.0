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
package com.mycollab.mobile.module.crm.view.account;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.mobile.module.crm.view.opportunity.OpportunityListDisplay;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AccountRelatedOpportunityView extends AbstractRelatedListView<SimpleOpportunity, OpportunitySearchCriteria> {
    private static final long serialVersionUID = -5900127054425653263L;
    private Account account;

    public AccountRelatedOpportunityView() {
        initUI();
    }

    private void initUI() {
        this.setCaption(UserUIContext.getMessage(OpportunityI18nEnum.M_TITLE_RELATED_OPPORTUNITIES));
        itemList = new OpportunityListDisplay();
        this.setContent(itemList);
    }

    public void displayOpportunities(final Account account) {
        this.account = account;
        loadOpportunities();
    }

    private void loadOpportunities() {
        final OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        criteria.setAccountId(new NumberSearchField(account.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadOpportunities();
    }

    @Override
    protected Component createRightComponent() {
        return new MButton("", clickEvent -> fireNewRelatedItem("")).withStyleName("add-btn");
    }

}
