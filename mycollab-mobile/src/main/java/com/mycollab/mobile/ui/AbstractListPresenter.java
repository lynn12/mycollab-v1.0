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
package com.mycollab.mobile.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.mobile.mvp.AbstractPresenter;

/**
 * @param <V>
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public abstract class AbstractListPresenter<V extends IListView<S, B>, S extends SearchCriteria, B extends ValuedBean> extends AbstractPresenter<V> {
    private static final long serialVersionUID = -2202567598255893303L;

    protected S searchCriteria;

    public AbstractListPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    public void doSearch(S searchCriteria) {
        this.searchCriteria = searchCriteria;
        view.getPagedBeanTable().search(searchCriteria);
    }
}
