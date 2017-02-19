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
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.domain.FollowingTicket;
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectFollowingTicketServiceTest extends IntegrationServiceTest {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    private ProjectFollowingTicketService projectFollowingTicketService;

    private FollowingTicketSearchCriteria getCriteria() {
        FollowingTicketSearchCriteria criteria = new FollowingTicketSearchCriteria();
        criteria.setExtraTypeIds(new SetSearchField<>(1, 2));
        criteria.setSaccountid(new NumberSearchField(1));
        criteria.setUser(StringSearchField.and("hainguyen@esofthead.com"));
        return criteria;
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListProjectFollowingTicket() throws ParseException {
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(new BasicSearchRequest<>(getCriteria()));
        assertThat(projectFollowingTickets).extracting("type", "name",
                "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Task", "task 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Bug", "bug 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Task", "task 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Task", "task 4", DATE_FORMAT.parse("2014-09-22 00:00:00")),
                tuple("Project-Bug", "bug 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Bug", "bug 4", DATE_FORMAT.parse("2014-09-24 00:00:00")),
                tuple("Project-Risk", "risk 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Risk", "risk 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Risk", "risk 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Risk", "risk 4", DATE_FORMAT.parse("2014-09-24 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(12);
    }

    @DataSet
    @Test
    public void testGetListProjectFollowingTicketBySummary() throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setName(StringSearchField.and("1"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        assertThat(projectFollowingTickets).extracting("type", "name", "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Risk", "risk 1", DATE_FORMAT.parse("2014-10-23 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(3);
    }

    @DataSet
    @Test
    public void testGetListProjectFollowingTicketOfTaskAndBug() throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setTypes(new SetSearchField<>("Project-Task", "Project-Bug"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        assertThat(projectFollowingTickets).extracting("type", "name",
                "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Task", "task 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Task", "task 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Task", "task 4", DATE_FORMAT.parse("2014-09-22 00:00:00")),
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Bug", "bug 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Bug", "bug 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Bug", "bug 4", DATE_FORMAT.parse("2014-09-24 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(8);
    }

    @DataSet
    @Test
    public void testGetListProjectFollowingTicketOfTask() throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setType(StringSearchField.and("Project-Task"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        assertThat(projectFollowingTickets).extracting("type", "name",
                "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Task", "task 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Task", "task 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Task", "task 4", DATE_FORMAT.parse("2014-09-22 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(4);
    }

    @DataSet
    @Test
    public void testGetListProjectFollowingTicketOfRisk() throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setType(StringSearchField.and("Project-Risk"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));

        assertThat(projectFollowingTickets).extracting("type", "name",
                "monitorDate").contains(
                tuple("Project-Risk", "risk 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Risk", "risk 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Risk", "risk 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Risk", "risk 4", DATE_FORMAT.parse("2014-09-24 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(4);
    }

    @DataSet
    @Test
    public void testGetListProjectFollowingTicketOfBug() throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setType(StringSearchField.and("Project-Bug"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));

        assertThat(projectFollowingTickets).extracting("type", "name",
                "monitorDate").contains(
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Bug", "bug 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Bug", "bug 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Bug", "bug 4", DATE_FORMAT.parse("2014-09-24 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(4);
    }

    @DataSet
    @Test
    public void testGetTotalCount() throws ParseException {
        assertThat(projectFollowingTicketService.getTotalCount(getCriteria())).isEqualTo(12);
    }
}
