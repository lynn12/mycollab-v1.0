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
package com.mycollab.module.project.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MilestoneSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField assignUser;
    private SetSearchField<String> statuses;
    private SetSearchField<Integer> projectIds;
    private NumberSearchField id;
    private StringSearchField milestoneName;

    public StringSearchField getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(StringSearchField assignUser) {
        this.assignUser = assignUser;
    }

    public SetSearchField<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(SetSearchField<String> statuses) {
        this.statuses = statuses;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getId() {
        return id;
    }

    public void setMilestoneName(StringSearchField milestoneName) {
        this.milestoneName = milestoneName;
    }

    public StringSearchField getMilestoneName() {
        return milestoneName;
    }

    public SetSearchField<Integer> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(SetSearchField<Integer> projectIds) {
        this.projectIds = projectIds;
    }
}
