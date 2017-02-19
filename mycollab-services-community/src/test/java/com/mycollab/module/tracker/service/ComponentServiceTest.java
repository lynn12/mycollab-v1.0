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
package com.mycollab.module.tracker.service;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
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
public class ComponentServiceTest extends IntegrationServiceTest {
    private static final DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    private ComponentService componentService;

    private ComponentSearchCriteria getCriteria() {
        ComponentSearchCriteria criteria = new ComponentSearchCriteria();
        criteria.setProjectId(new NumberSearchField(1));
        criteria.setSaccountid(new NumberSearchField(1));
        return criteria;
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListComponents() throws ParseException {
        List<SimpleComponent> components = componentService.findPageableListByCriteria(new BasicSearchRequest<>(getCriteria()));

        assertThat(components.size()).isEqualTo(4);
        assertThat(components).extracting("id", "description", "status",
                "name", "numBugs", "numOpenBugs", "userLeadFullName",
                "lastupdatedtime").contains(
                tuple(1, "aaaaaaa", "Open", "com 1", 1, 1, "Nguyen Hai", dateformat.parse("2014-10-02 06:45:22")),
                tuple(2, "bbbbbbb", "Closed", "com 2", 2, 1, "Nghiem Le", dateformat.parse("2014-10-02 07:45:22")),
                tuple(3, "ccccccc", "Closed", "com 3", 1, 1, "Nguyen Hai", dateformat.parse("2014-10-03 06:45:22")),
                tuple(4, "ddddddd", "Open", "com 4", 0, 0, "Nghiem Le", dateformat.parse("2014-10-02 06:32:22")));
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testTotalCount() {
        List<SimpleComponent> components = componentService.findPageableListByCriteria(new BasicSearchRequest<>(getCriteria()));
        assertThat(components.size()).isEqualTo(4);
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testFindComponentById() {
        ComponentSearchCriteria criteria = new ComponentSearchCriteria();
        criteria.setId(new NumberSearchField(1));

        List<SimpleComponent> components = componentService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        assertThat(components.size()).isEqualTo(1);
        assertThat(components).extracting("id", "description", "status",
                "name", "numBugs", "numOpenBugs").contains(
                tuple(1, "aaaaaaa", "Open", "com 1", 1, 1));
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testFindByCriteria() {
        ComponentSearchCriteria criteria = getCriteria();
        criteria.setId(new NumberSearchField(2));
        criteria.setComponentName(StringSearchField.and("com 2"));
        criteria.setStatus(StringSearchField.and("Closed"));
        criteria.setUserlead(StringSearchField.and("nghiemle"));

        List<SimpleComponent> components = componentService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        assertThat(components.size()).isEqualTo(1);
        assertThat(components).extracting("id", "description", "status",
                "name", "numBugs", "numOpenBugs").contains(
                tuple(2, "bbbbbbb", "Closed", "com 2", 2, 1));
    }
}
