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
package com.mycollab.module.crm.view.campaign;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.fielddef.CampaignTableFieldDef;
import com.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class CampaignListViewImpl extends AbstractListItemComp<CampaignSearchCriteria, SimpleCampaign> implements CampaignListView {
    private static final long serialVersionUID = 1L;

    @Override
    protected void buildExtraControls() {
        MButton customizeViewBtn = ComponentUtils.createCustomizeViewButton()
                .withListener(clickEvent -> UI.getCurrent().addWindow(new CampaignListCustomizeWindow(tableItem)));
        this.addExtraButton(customizeViewBtn);
    }

    @Override
    protected DefaultGenericSearchPanel<CampaignSearchCriteria> createSearchPanel() {
        return new CampaignSearchPanel(true);
    }

    @Override
    protected AbstractPagedBeanTable<CampaignSearchCriteria, SimpleCampaign> createBeanTable() {
        return new CampaignTableDisplay(CrmTypeConstants.CAMPAIGN, CampaignTableFieldDef.selected(),
                Arrays.asList(CampaignTableFieldDef.campaignname(), CampaignTableFieldDef.status(),
                        CampaignTableFieldDef.type(), CampaignTableFieldDef.expectedRevenue(),
                        CampaignTableFieldDef.endDate(), CampaignTableFieldDef.assignUser()));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

        if (UserUIContext.canAccess(RolePermissionCollections.CRM_CAMPAIGN)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN)) {
            container.addMassUpdateActionItem();
        }

        return container;
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        addComponent(new CampaignCrmListNoItemView());
    }
}
