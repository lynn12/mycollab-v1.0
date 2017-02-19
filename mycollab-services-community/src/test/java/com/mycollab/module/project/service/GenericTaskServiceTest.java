/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.service;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.DateSearchField;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class GenericTaskServiceTest extends IntegrationServiceTest {
    @Autowired
    private ProjectTicketService genericTaskService;

    @DataSet
    @Test
    public void testGenericTaskListFindPageable() {
        List<ProjectTicket> tasks = genericTaskService.findPageableListByCriteria(new BasicSearchRequest<>(null));
        assertThat(tasks.size()).isEqualTo(2);
        assertThat(tasks).extracting("type", "name").contains(tuple("Project-Risk", "b"), tuple("Project-Bug", "name 1"));
    }

    @DataSet
    @Test
    public void testCountTaskOverDue() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = df.parse("2014-01-23 10:49:49");
        ProjectTicketSearchCriteria criteria = new ProjectTicketSearchCriteria();
        criteria.setDueDate(new DateSearchField(d));
        criteria.setProjectIds(new SetSearchField<>(1));
        criteria.setSaccountid(new NumberSearchField(1));
        List<ProjectTicket> tasks = genericTaskService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        assertThat(tasks.size()).isEqualTo(1);
        assertThat(tasks).extracting("type", "name").contains(tuple("Project-Risk", "b"));
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testListTaskOverDue() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = df.parse("2014-01-23 10:49:49");

        ProjectTicketSearchCriteria criteria = new ProjectTicketSearchCriteria();
        criteria.setDueDate(new DateSearchField(d));
        criteria.setProjectIds(new SetSearchField<>(1));
        criteria.setSaccountid(new NumberSearchField(1));
        List<ProjectTicket> taskList = genericTaskService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));

        assertThat(taskList.size()).isEqualTo(1);
        assertThat(taskList).extracting("type", "name").contains(tuple("Project-Risk", "b"));
    }
}
