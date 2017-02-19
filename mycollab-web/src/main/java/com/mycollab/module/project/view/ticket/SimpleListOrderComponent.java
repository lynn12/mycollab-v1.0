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
package com.mycollab.module.project.view.ticket;

import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.vaadin.web.ui.WebThemes;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
class SimpleListOrderComponent extends TicketGroupOrderComponent {
    SimpleListOrderComponent() {
        this.addStyleName(WebThemes.BORDER_LIST);
    }

    @Override
    public void insertTickets(List<ProjectTicket> tickets) {
        for (ProjectTicket ticket : tickets) {
            this.addComponent(new TicketRowRenderer(ticket));
        }
    }
}