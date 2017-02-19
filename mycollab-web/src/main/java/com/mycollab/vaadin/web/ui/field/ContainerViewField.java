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
package com.mycollab.vaadin.web.ui.field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class ContainerViewField extends CustomField<Object> {
    private static final long serialVersionUID = 1L;
    private CssLayout layout;

    public ContainerViewField() {
        layout = new CssLayout();
        layout.setWidth("100%");
    }

    public void addComponentField(Component component) {
        layout.addComponent(component);
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    @Override
    protected Component initContent() {
        return layout;
    }
}
