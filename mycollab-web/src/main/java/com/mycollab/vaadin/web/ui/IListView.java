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
package com.mycollab.vaadin.web.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.events.HasMassItemActionHandler;
import com.mycollab.vaadin.events.HasSearchHandlers;
import com.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.web.ui.table.IPagedBeanTable;

/**
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface IListView<S extends SearchCriteria, B> extends PageView {
    void enableActionControls(int numOfSelectedItem);

    void disableActionControls();

    HasSearchHandlers<S> getSearchHandlers();

    HasSelectionOptionHandlers getOptionSelectionHandlers();

    HasMassItemActionHandler getPopupActionHandlers();

    HasSelectableItemHandlers<B> getSelectableItemHandlers();

    IPagedBeanTable<S, B> getPagedBeanTable();

    void showNoItemView();
}
