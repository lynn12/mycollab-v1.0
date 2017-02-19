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
package com.mycollab.mobile.mvp.view;

import com.mycollab.core.MyCollabException;
import com.mycollab.mobile.module.project.view.UserProjectListPresenter;
import com.mycollab.mobile.mvp.AbstractPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.2.6
 */
public class NotPresentPresenter extends AbstractPresenter<NotPresenterView> {
    public NotPresentPresenter() {
        super(NotPresenterView.class);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        if (navigator instanceof NavigationManager) {
            NavigationManager navManager = ((NavigationManager) navigator);
            navManager.navigateTo(view);
            view.display();
            if (navManager.getPreviousComponent() == null) {
                UserProjectListPresenter projectListPresenter = PresenterResolver.getPresenter(UserProjectListPresenter.class);
                navManager.setPreviousComponent(projectListPresenter.getView());
            }
        } else {
            throw new MyCollabException("Invalid flow");
        }
    }
}
