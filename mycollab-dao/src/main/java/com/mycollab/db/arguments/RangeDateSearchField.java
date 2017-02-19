/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.arguments;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RangeDateSearchField extends SearchField {
    private static final long serialVersionUID = 1L;

    private Date from;
    private Date to;

    public RangeDateSearchField() {
    }

    public RangeDateSearchField(Date from, Date to) {
        this(SearchField.AND, from, to);
    }

    public RangeDateSearchField(final String oper, final Date from, final Date to) {
        this.operation = oper;
        this.from = from;
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
