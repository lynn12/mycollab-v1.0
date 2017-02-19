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
package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.LeadEvent;
import com.mycollab.mobile.module.crm.ui.AbstractListViewComp;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@ViewComponent
public class LeadListViewImpl extends AbstractListViewComp<LeadSearchCriteria, SimpleLead> implements LeadListView {
    private static final long serialVersionUID = -5311139413938817084L;

    public LeadListViewImpl() {
        setCaption(UserUIContext.getMessage(LeadI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<LeadSearchCriteria, SimpleLead> createBeanTable() {
        return new LeadListDisplay();
    }

    @Override
    protected Component createRightComponent() {
        return new MButton("", clickEvent -> EventBusFactory.getInstance().post(new LeadEvent.GotoAdd(this, null)))
                .withStyleName("add-btn");
    }

}
