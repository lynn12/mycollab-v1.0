/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.crm.view.setting;

import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.module.crm.view.setting.ICrmCustomView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.view.NotPresentedView;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class CrmCustomViewImpl extends NotPresentedView implements ICrmCustomView {
    private static final long serialVersionUID = 1L;

    @Override
    public void display(String moduleName) {

    }

    @Override
    public void addActiveSection(DynaSection section) {

    }

    @Override
    public DynaSection[] getActiveSections() {
        return null;
    }

    @Override
    public String getCandidateTextFieldName() {
        return null;
    }

    @Override
    public void refreshSectionLayout(DynaSection section) {

    }
}
