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

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.mobile.ui.AbstractSelectionView;
import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.IBeanList;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CampaignSelectionView extends AbstractSelectionView<CampaignWithBLOBs> {
    private static final long serialVersionUID = 1L;

    private CampaignListDisplay itemList;

    private CampaignRowDisplayHandler rowHandler = new CampaignRowDisplayHandler();

    public CampaignSelectionView() {
        super();
        createUI();
        this.setCaption(UserUIContext.getMessage(CampaignI18nEnum.M_VIEW_CAMPAIGN_NAME_LOOKUP));
    }

    private void createUI() {
        itemList = new CampaignListDisplay();
        itemList.setWidth("100%");
        itemList.setRowDisplayHandler(rowHandler);
        this.setContent(itemList);
    }

    @Override
    public void load() {
        CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        itemList.search(searchCriteria);
        SimpleCampaign clearCampaign = new SimpleCampaign();
        itemList.addComponentAtTop(rowHandler.generateRow(itemList, clearCampaign, 0));
    }

    private class CampaignRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleCampaign> {

        @Override
        public Component generateRow(IBeanList<SimpleCampaign> host, final SimpleCampaign campaign, int rowIndex) {
            return new Button(campaign.getCampaignname(), clickEvent -> {
                selectionField.fireValueChange(campaign);
                CampaignSelectionView.this.getNavigationManager().navigateBack();
            });
        }
    }
}
