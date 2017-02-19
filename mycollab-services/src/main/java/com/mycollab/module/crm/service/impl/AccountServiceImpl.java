/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.service.impl;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.aspect.ClassInfo;
import com.mycollab.aspect.ClassInfoMap;
import com.mycollab.aspect.Traceable;
import com.mycollab.aspect.Watchable;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.dao.AccountLeadMapper;
import com.mycollab.module.crm.dao.AccountMapper;
import com.mycollab.module.crm.dao.AccountMapperExt;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.module.crm.service.CampaignService;
import com.mycollab.module.crm.domain.*;
import com.mycollab.spring.AppContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "accountname")
@Watchable(userFieldName = "assignuser")
public class AccountServiceImpl extends DefaultService<Integer, Account, AccountSearchCriteria> implements AccountService {
    static {
        ClassInfoMap.put(AccountServiceImpl.class, new ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.ACCOUNT));
    }

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountMapperExt accountMapperExt;

    @Autowired
    private AccountLeadMapper accountLeadMapper;

    @Override
    public ICrudGenericDAO<Integer, Account> getCrudMapper() {
        return accountMapper;
    }

    @Override
    public ISearchableDAO<AccountSearchCriteria> getSearchMapper() {
        return accountMapperExt;
    }

    @Override
    public SimpleAccount findById(Integer id, Integer accountId) {
        return accountMapperExt.findById(id);
    }

    @Override
    public Integer saveWithSession(Account record, String username) {
        int result = super.saveWithSession(record, username);

        if (record.getExtraData() != null && record.getExtraData() instanceof SimpleCampaign) {
            CampaignAccount assoAccount = new CampaignAccount();
            assoAccount.setAccountid(record.getId());
            assoAccount.setCampaignid(((SimpleCampaign) record.getExtraData()).getId());
            assoAccount.setCreatedtime(new GregorianCalendar().getTime());

            CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
            campaignService.saveCampaignAccountRelationship(Collections.singletonList(assoAccount), record.getSaccountid());
        }
        return result;
    }

    @Override
    public void saveAccountLeadRelationship(List<AccountLead> associateLeads, Integer accountId) {
        for (AccountLead associateLead : associateLeads) {
            AccountLeadExample ex = new AccountLeadExample();
            ex.createCriteria().andAccountidEqualTo(associateLead.getAccountid())
                    .andLeadidEqualTo(associateLead.getLeadid());
            if (accountLeadMapper.countByExample(ex) == 0) {
                associateLead.setCreatetime(new GregorianCalendar().getTime());
                accountLeadMapper.insert(associateLead);
            }
        }
    }

    @Override
    public void removeAccountLeadRelationship(AccountLead associateLead, Integer accountId) {
        AccountLeadExample ex = new AccountLeadExample();
        ex.createCriteria().andAccountidEqualTo(associateLead.getAccountid()).andLeadidEqualTo(associateLead.getLeadid());
        accountLeadMapper.deleteByExample(ex);
    }

    @Override
    public SimpleAccount findAccountAssoWithConvertedLead(Integer leadId, Integer accountId) {
        return accountMapperExt.findAccountAssoWithConvertedLead(leadId);
    }

}
