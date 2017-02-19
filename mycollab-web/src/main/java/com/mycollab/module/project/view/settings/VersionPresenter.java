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
package com.mycollab.module.project.view.settings;

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.view.parameters.VersionScreenData;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class VersionPresenter extends AbstractPresenter<VersionContainer> {
    private static final long serialVersionUID = 1L;

    public VersionPresenter() {
        super(VersionContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        view.removeAllComponents();
        AbstractPresenter<?> presenter;

        if (data instanceof VersionScreenData.Add) {
            presenter = PresenterResolver.getPresenter(VersionAddPresenter.class);
        } else if (data instanceof VersionScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(VersionAddPresenter.class);
        } else if (data instanceof VersionScreenData.Search) {
            presenter = PresenterResolver.getPresenter(VersionListPresenter.class);
        } else if (data instanceof VersionScreenData.Read) {
            presenter = PresenterResolver.getPresenter(VersionReadPresenter.class);
        } else if (data == null) {
            VersionSearchCriteria criteria = new VersionSearchCriteria();
            criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            data = new VersionScreenData.Search(criteria);
            presenter = PresenterResolver.getPresenter(VersionListPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data");
        }

        presenter.go(view, data);

    }

}
