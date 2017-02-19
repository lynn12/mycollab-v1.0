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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.SecureAccessException;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.module.crm.service.CampaignService;
import com.mycollab.module.crm.view.CrmGenericListPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.MassUpdateCommand;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.mycollab.vaadin.web.ui.MailFormWindow;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignListPresenter extends CrmGenericListPresenter<CampaignListView, CampaignSearchCriteria, SimpleCampaign>
        implements MassUpdateCommand<CampaignWithBLOBs> {

    private static final long serialVersionUID = 1L;
    private CampaignService campaignService;

    public CampaignListPresenter() {
        super(CampaignListView.class, CampaignCrmListNoItemView.class);
    }

    @Override
    protected void postInitView() {
        super.postInitView();
        campaignService = AppContextUtil.getSpringBean(CampaignService.class);

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {

            @Override
            protected void onSelectExtra(String id) {
                if ("mail".equals(id)) {
                    UI.getCurrent().addWindow(new MailFormWindow());
                } else if ("massUpdate".equals(id)) {
                    MassUpdateCampaignWindow massUpdateWindow = new MassUpdateCampaignWindow(
                            UserUIContext.getMessage(GenericI18Enum.WINDOW_MASS_UPDATE_TITLE,
                                    UserUIContext.getMessage(CampaignI18nEnum.LIST)), CampaignListPresenter.this);
                    UI.getCurrent().addWindow(massUpdateWindow);
                }
            }

            @Override
            protected String getReportTitle() {
                return UserUIContext.getMessage(CampaignI18nEnum.LIST);
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleCampaign.class;
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.CAMPAIGN);

        if (UserUIContext.canRead(RolePermissionCollections.CRM_CAMPAIGN)) {
            searchCriteria = (CampaignSearchCriteria) data.getParams();
            int totalCount = campaignService.getTotalCount(searchCriteria);
            if (totalCount > 0) {
                this.displayListView(container, data);
                doSearch(searchCriteria);
            } else {
                this.displayNoExistItems(container, data);
            }

            MyCollabUI.addFragment("crm/campaign/list", UserUIContext.getMessage(CampaignI18nEnum.LIST));
        } else {
            throw new SecureAccessException();
        }
    }

    @Override
    protected void deleteSelectedItems() {
        if (!isSelectAll) {
            Collection<SimpleCampaign> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<CampaignWithBLOBs> keyList = new ArrayList<>();
            for (SimpleCampaign item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item);
                }
            }

            if (keyList.size() > 0) {
                campaignService.massRemoveWithSession(keyList, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                doSearch(searchCriteria);
                checkWhetherEnableTableActionControl();
            }
        } else {
            campaignService.removeByCriteria(searchCriteria, MyCollabUI.getAccountId());
            doSearch(searchCriteria);
        }
    }

    @Override
    public void massUpdate(CampaignWithBLOBs value) {
        if (!isSelectAll) {
            Collection<SimpleCampaign> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Integer> keyList = new ArrayList<>();
            for (SimpleCampaign item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item.getId());
                }
            }
            if (keyList.size() > 0) {
                campaignService.massUpdateWithSession(value, keyList, MyCollabUI.getAccountId());
                doSearch(searchCriteria);
            }
        } else {
            campaignService.updateBySearchCriteria(value, searchCriteria);
            doSearch(searchCriteria);
        }
    }

    @Override
    public ISearchableService<CampaignSearchCriteria> getSearchService() {
        return AppContextUtil.getSpringBean(CampaignService.class);
    }
}
