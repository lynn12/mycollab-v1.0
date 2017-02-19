/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui.field;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.3.3
 */
public class DateTimeOptionViewField extends CustomField<String> {
    private Date date;

    public DateTimeOptionViewField(Date dateVal) {
        this.date = dateVal;
    }

    @Override
    protected Component initContent() {
        String dateValue = (date == null) ? "" : UserUIContext.formatDateTime(date);
        return new Label(dateValue);
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }
}
