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
package com.mycollab.module.project.view;

import com.mycollab.common.TableViewField;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.fielddef.ProjectTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.CustomizedTableWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ProjectListCustomizeWindow extends CustomizedTableWindow {
    private static final long serialVersionUID = 1L;

    public ProjectListCustomizeWindow(AbstractPagedBeanTable table) {
        super(ProjectTypeConstants.PROJECT, table);
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(ProjectTableFieldDef.projectName(), ProjectTableFieldDef.client(),
                ProjectTableFieldDef.lead(), ProjectTableFieldDef.startDate(), ProjectTableFieldDef.status(),
                ProjectTableFieldDef.homePage(), ProjectTableFieldDef.budget(), ProjectTableFieldDef.endDate(),
                ProjectTableFieldDef.createdDate(), ProjectTableFieldDef.rate(), ProjectTableFieldDef.overtimeRate(),
                ProjectTableFieldDef.budget(), ProjectTableFieldDef.actualBudget());
    }
}
