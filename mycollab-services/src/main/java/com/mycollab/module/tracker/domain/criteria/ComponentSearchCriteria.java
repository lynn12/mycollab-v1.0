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
package com.mycollab.module.tracker.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

public class ComponentSearchCriteria extends SearchCriteria {

    private NumberSearchField projectId;
    private StringSearchField componentName;
    private NumberSearchField id;
    private StringSearchField status;
    private StringSearchField userlead;

    public StringSearchField getUserlead() {
		return userlead;
	}

	public void setUserlead(StringSearchField userlead) {
		this.userlead = userlead;
	}

    public NumberSearchField getProjectId() {
        return projectId;
    }

    public void setProjectId(NumberSearchField projectId) {
        this.projectId = projectId;
    }

    public StringSearchField getComponentName() {
        return componentName;
    }

    public void setComponentName(StringSearchField componentName) {
        this.componentName = componentName;
    }

    public NumberSearchField getId() {
        return id;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

	public StringSearchField getStatus() {
		return status;
	}

	public void setStatus(StringSearchField status) {
		this.status = status;
	}
}
