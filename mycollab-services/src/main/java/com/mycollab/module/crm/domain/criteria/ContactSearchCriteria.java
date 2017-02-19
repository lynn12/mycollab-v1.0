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
package com.mycollab.module.crm.domain.criteria;

import com.mycollab.common.CountryValueFactory;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.*;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.i18n.ContactI18nEnum;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final Param p_name = CacheParamMapper.register(CrmTypeConstants.CONTACT, GenericI18Enum.FORM_NAME,
            new ConcatStringParam("firstname", "m_crm_contact", new String[]{"firstname", "lastname"}));

    public static final I18nStringListParam p_leadsource = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_LEAD_SOURCE,
            new I18nStringListParam("leadsource", "m_crm_contact", "leadSource",
                    Arrays.asList(CrmDataTypeFactory.getLeadSourceList())));

    public static final Param p_billingCountry = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_PRIMARY_COUNTRY,
            new StringListParam("billingCountry", "m_crm_contact", "primCountry",
                    Arrays.asList(CountryValueFactory.getCountryList())));

    public static final Param p_shippingCountry = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_OTHER_COUNTRY,
            new StringListParam("shippingCountry", "m_crm_contact", "otherCountry",
                    Arrays.asList(CountryValueFactory.getCountryList())));

    public static final Param p_anyPhone = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_ANY_PHONE,
            new CompositionStringParam("anyPhone",
                    new StringParam("", "m_crm_contact", "officePhone"),
                    new StringParam("", "m_crm_contact", "mobile"),
                    new StringParam("", "m_crm_contact", "homePhone"),
                    new StringParam("", "m_crm_contact", "otherPhone"),
                    new StringParam("", "m_crm_contact", "fax"),
                    new StringParam("", "m_crm_contact", "assistantPhone")));

    public static final Param p_anyEmail = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_ANY_EMAIL,
            new CompositionStringParam("anyEmail", new StringParam("", "m_crm_contact", "email")));

    public static final Param p_anyCity = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_ANY_CITY,
            new CompositionStringParam("anyCity",
                    new StringParam("", "m_crm_contact", "primCity"),
                    new StringParam("", "m_crm_contact", "otherCity")));

    public static final Param p_account = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_ACCOUNTS,
            new PropertyParam("account", "m_crm_contact", "accountId"));

    public static final Param p_assignee = CacheParamMapper.register(CrmTypeConstants.CONTACT, GenericI18Enum.FORM_ASSIGNEE,
            new PropertyListParam("assignuser", "m_crm_contact", "assignUser"));

    public static final Param p_createdtime = CacheParamMapper.register(CrmTypeConstants.CONTACT, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("createdtime", "m_crm_contact", "createdTime"));

    public static final Param p_lastupdatedtime = CacheParamMapper.register(CrmTypeConstants.CONTACT, GenericI18Enum.FORM_LAST_UPDATED_TIME,
            new DateParam("lastupdatedtime", "m_crm_contact", "lastUpdatedTime"));

    private StringSearchField contactName;
    private SetSearchField<String> assignUsers;
    private NumberSearchField accountId;
    private StringSearchField firstname;
    private StringSearchField lastname;
    private StringSearchField anyEmail;
    private StringSearchField anyAddress;
    private StringSearchField anyState;
    private SetSearchField<String> countries;
    private StringSearchField anyPhone;
    private StringSearchField anyCity;
    private StringSearchField anyPostalCode;
    private SetSearchField<String> leadSources;
    private NumberSearchField id;
    private NumberSearchField campaignId;
    private NumberSearchField opportunityId;
    private NumberSearchField caseId;

    public StringSearchField getContactName() {
        return contactName;
    }

    public void setContactName(StringSearchField contactName) {
        this.contactName = contactName;
    }

    public void setAssignUsers(SetSearchField<String> assignUsers) {
        this.assignUsers = assignUsers;
    }

    public SetSearchField<String> getAssignUsers() {
        return assignUsers;
    }

    public NumberSearchField getAccountId() {
        return accountId;
    }

    public void setAccountId(NumberSearchField accountId) {
        this.accountId = accountId;
    }

    public StringSearchField getFirstname() {
        return firstname;
    }

    public void setFirstname(StringSearchField firstname) {
        this.firstname = firstname;
    }

    public StringSearchField getLastname() {
        return lastname;
    }

    public void setLastname(StringSearchField lastname) {
        this.lastname = lastname;
    }

    public StringSearchField getAnyEmail() {
        return anyEmail;
    }

    public void setAnyEmail(StringSearchField anyEmail) {
        this.anyEmail = anyEmail;
    }

    public StringSearchField getAnyAddress() {
        return anyAddress;
    }

    public void setAnyAddress(StringSearchField anyAddress) {
        this.anyAddress = anyAddress;
    }

    public StringSearchField getAnyState() {
        return anyState;
    }

    public void setAnyState(StringSearchField anyState) {
        this.anyState = anyState;
    }

    public void setCountries(SetSearchField<String> countries) {
        this.countries = countries;
    }

    public SetSearchField<String> getCountries() {
        return countries;
    }

    public StringSearchField getAnyPhone() {
        return anyPhone;
    }

    public void setAnyPhone(StringSearchField anyPhone) {
        this.anyPhone = anyPhone;
    }

    public StringSearchField getAnyCity() {
        return anyCity;
    }

    public void setAnyCity(StringSearchField anyCity) {
        this.anyCity = anyCity;
    }

    public StringSearchField getAnyPostalCode() {
        return anyPostalCode;
    }

    public void setAnyPostalCode(StringSearchField anyPostalCode) {
        this.anyPostalCode = anyPostalCode;
    }

    public void setLeadSources(SetSearchField<String> leadSources) {
        this.leadSources = leadSources;
    }

    public SetSearchField<String> getLeadSources() {
        return leadSources;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getId() {
        return id;
    }

    public NumberSearchField getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(NumberSearchField campaignId) {
        this.campaignId = campaignId;
    }

    public NumberSearchField getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(NumberSearchField opportunityId) {
        this.opportunityId = opportunityId;
    }

    public NumberSearchField getCaseId() {
        return caseId;
    }

    public void setCaseId(NumberSearchField caseId) {
        this.caseId = caseId;
    }
}
