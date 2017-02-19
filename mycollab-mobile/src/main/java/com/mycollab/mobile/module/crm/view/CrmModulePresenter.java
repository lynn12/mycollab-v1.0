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
package com.mycollab.mobile.module.crm.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.2
 */
public class CrmModulePresenter extends AbstractCrmPresenter<CrmModule> {
    private static final long serialVersionUID = -3370467477599009160L;

    public CrmModulePresenter() {
        super(CrmModule.class);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        MyCollabUI.addFragment("crm/", UserUIContext.getMessage(GenericI18Enum.MODULE_CRM));
    }
}
