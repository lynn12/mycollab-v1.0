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
package com.mycollab.module.crm.view.parameters;

import com.mycollab.module.crm.domain.CrmTask;
import com.mycollab.vaadin.mvp.ScreenData;

public class AssignmentScreenData {
    public static class Add extends ScreenData<CrmTask> {

        public Add(CrmTask task) {
            super(task);
        }
    }

    public static class Edit extends ScreenData<CrmTask> {

        public Edit(CrmTask task) {
            super(task);
        }
    }

    public static class Read extends ScreenData<Integer> {

        public Read(Integer params) {
            super(params);
        }
    }
}
