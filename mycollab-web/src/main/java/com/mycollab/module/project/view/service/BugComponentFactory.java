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
package com.mycollab.module.project.view.service;

import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.mvp.CacheableComponent;
import com.vaadin.ui.AbstractComponent;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public interface BugComponentFactory {
    AbstractComponent createPriorityPopupField(SimpleBug bug);

    AbstractComponent createAssigneePopupField(SimpleBug bug);

    AbstractComponent createCommentsPopupField(SimpleBug bug);

    AbstractComponent createStatusPopupField(SimpleBug bug);

    AbstractComponent createMilestonePopupField(SimpleBug bug);

    AbstractComponent createDeadlinePopupField(SimpleBug bug);

    AbstractComponent createStartDatePopupField(SimpleBug bug);

    AbstractComponent createEndDatePopupField(SimpleBug bug);

    AbstractComponent createBillableHoursPopupField(SimpleBug bug);

    AbstractComponent createNonbillableHoursPopupField(SimpleBug bug);

    AbstractComponent createFollowersPopupField(SimpleBug bug);
}
