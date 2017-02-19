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
package com.mycollab.vaadin.ui;

import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.PopupDateField;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.5.4
 */
public class PopupDateFieldExt extends PopupDateField {
    private static final long serialVersionUID = 1L;

    public PopupDateFieldExt() {
        this(null);
    }

    public PopupDateFieldExt(Date value) {
        this(null, value);
    }

    public PopupDateFieldExt(String caption, Date value) {
        super(null, value);
        this.setTimeZone(UserUIContext.getUserTimeZone());
        this.setDateFormat(MyCollabUI.getDateFormat());
    }

    public PopupDateFieldExt withWidth(String width) {
        this.setWidth(width);
        return this;
    }
}
