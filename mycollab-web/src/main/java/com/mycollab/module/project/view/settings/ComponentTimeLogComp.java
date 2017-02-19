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

import com.mycollab.module.project.service.ItemTimeLoggingService;
import com.mycollab.module.project.ui.components.TimeLogComp;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
public class ComponentTimeLogComp extends TimeLogComp<SimpleComponent> {
    private ItemTimeLoggingService itemTimeLoggingService = AppContextUtil.getSpringBean(ItemTimeLoggingService.class);

    @Override
    protected Double getTotalBillableHours(SimpleComponent bean) {
        return itemTimeLoggingService.getTotalBillableHoursByComponent(bean.getId(), MyCollabUI.getAccountId());
    }

    @Override
    protected Double getTotalNonBillableHours(SimpleComponent bean) {
        return itemTimeLoggingService.getTotalNonBillableHoursByComponent(bean.getId(), MyCollabUI.getAccountId());
    }

    @Override
    protected Double getRemainedHours(SimpleComponent bean) {
        return itemTimeLoggingService.getRemainHoursByComponent(bean.getId(), MyCollabUI.getAccountId());
    }

    @Override
    protected boolean hasEditPermission() {
        return false;
    }

    @Override
    protected void showEditTimeWindow(SimpleComponent bean) {

    }
}
