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
package com.mycollab.module.crm.service;

import com.mycollab.core.cache.CacheArgs;
import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.domain.*;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ContactService extends IDefaultService<Integer, Contact, ContactSearchCriteria> {

    @Cacheable
    SimpleContact findById(Integer contactId, @CacheKey Integer sAccountId);

    @CacheEvict
    @CacheArgs(values = {OpportunityService.class, ContactOpportunityService.class})
    void removeContactOpportunityRelationship(ContactOpportunity associateOpportunity, @CacheKey Integer sAccountId);

    @CacheEvict
    @CacheArgs(values = {OpportunityService.class, ContactOpportunityService.class})
    void saveContactOpportunityRelationship(List<ContactOpportunity> associateOpportunities, @CacheKey Integer accountId);

    @CacheEvict
    @CacheArgs(values = {LeadService.class})
    void saveContactLeadRelationship(List<ContactLead> associateLeads, @CacheKey Integer accountId);

    @CacheEvict
    @CacheArgs(values = {CaseService.class})
    void saveContactCaseRelationship(List<ContactCase> associateCases, @CacheKey Integer accountId);

    @CacheEvict
    @CacheArgs(values = {CaseService.class})
    void removeContactCaseRelationship(ContactCase associateCase, @CacheKey Integer sAccountId);

    @CacheEvict
    @Cacheable
    SimpleContact findContactAssoWithConvertedLead(int leadId, @CacheKey Integer accountId);
}
