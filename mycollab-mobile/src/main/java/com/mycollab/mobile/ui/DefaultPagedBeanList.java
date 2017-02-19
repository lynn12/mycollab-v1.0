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
import com.mycollab.db.persistence.service.ISearchableService;

import java.util.List;

/**
 * @param <SearchService>
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class DefaultPagedBeanList<SearchService extends ISearchableService<S>, S extends SearchCriteria, B> extends AbstractPagedBeanList<S, B> {
    private static final long serialVersionUID = 1L;

    private final SearchService searchService;

    public DefaultPagedBeanList(final SearchService searchService, RowDisplayHandler<B> rowDisplayHandler) {
        super(rowDisplayHandler);
        this.searchService = searchService;
    }

    @Override
    protected int queryTotalCount() {
        return searchService.getTotalCount(searchRequest.getSearchCriteria());
    }

    @Override
    protected List<B> queryCurrentData() {
        return searchService.findPageableListByCriteria(searchRequest);
    }
}
