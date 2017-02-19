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
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.view.parameters.RoleScreenData;
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RolePresenter extends AbstractPresenter<RoleContainer> {
    private static final long serialVersionUID = 1L;

    public RolePresenter() {
        super(RoleContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        UserPermissionManagementView groupContainer = (UserPermissionManagementView) container;
        groupContainer.gotoSubView(UserUIContext.getMessage(RoleI18nEnum.LIST));

        if (data == null) {
            RoleListPresenter listPresenter = PresenterResolver.getPresenter(RoleListPresenter.class);
            RoleSearchCriteria criteria = new RoleSearchCriteria();
            listPresenter.go(view, new ScreenData.Search<>(criteria));
        } else if (data instanceof RoleScreenData.Add || data instanceof RoleScreenData.Edit) {
            RoleAddPresenter presenter = PresenterResolver.getPresenter(RoleAddPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof RoleScreenData.Read) {
            RoleReadPresenter presenter = PresenterResolver.getPresenter(RoleReadPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof RoleScreenData.Search) {
            RoleListPresenter presenter = PresenterResolver.getPresenter(RoleListPresenter.class);
            presenter.go(view, data);
        }
    }
}
