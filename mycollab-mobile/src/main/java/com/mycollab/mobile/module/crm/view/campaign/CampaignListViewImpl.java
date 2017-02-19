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
import com.mycollab.mobile.module.crm.events.CampaignEvent;
import com.mycollab.mobile.module.crm.ui.AbstractListViewComp;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class CampaignListViewImpl extends AbstractListViewComp<CampaignSearchCriteria, SimpleCampaign> implements CampaignListView {
    private static final long serialVersionUID = -8743010493576179868L;

    public CampaignListViewImpl() {
        super();
        setCaption(UserUIContext.getMessage(CampaignI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<CampaignSearchCriteria, SimpleCampaign> createBeanTable() {
        return new CampaignListDisplay();
    }

    @Override
    protected Component createRightComponent() {
        return new MButton("", clickEvent -> EventBusFactory.getInstance().post(new CampaignEvent.GotoAdd(this, null)))
                .withStyleName("add-btn");
    }

}
