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

import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.module.crm.domain.*;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public interface AccountReadView extends IPreviewView<SimpleAccount> {
    HasPreviewFormHandlers<SimpleAccount> getPreviewFormHandlers();

    IRelatedListHandlers<SimpleContact> getRelatedContactHandlers();

    IRelatedListHandlers<SimpleOpportunity> getRelatedOpportunityHandlers();

    IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers();

    IRelatedListHandlers<SimpleCase> getRelatedCaseHandlers();

    IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers();
}
