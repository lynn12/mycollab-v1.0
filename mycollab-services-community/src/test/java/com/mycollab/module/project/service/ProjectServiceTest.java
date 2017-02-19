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

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectServiceTest extends IntegrationServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectActivityStreamService projectActivityStreamService;

    @DataSet
    @Test
    public void testSaveProject() {
        Project project = new Project();
        project.setSaccountid(1);
        project.setName("Example");
        project.setProjectstatus("Open");
        project.setShortname("abc");
        int projectId = projectService.saveWithSession(project, "admin");
        assertThat(projectId).isGreaterThan(0);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @DataSet
    @Test
    public void testGetListProjects() {
        List projects = projectService.findPageableListByCriteria(new BasicSearchRequest<>(null));
        Assert.assertEquals(projects.size(), 4);
        assertThat(projects).extracting("id", "name").contains(tuple(1, "A"),
                tuple(2, "B"), tuple(3, "C"), tuple(4, "D"));
    }

    @DataSet
    @Test
    public void testGetListProjectsByCriteria() {
        ProjectSearchCriteria criteria = new ProjectSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));

        List projects = projectService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        assertThat(projects.size()).isEqualTo(4);
        assertThat(projects).extracting("id", "name").contains(tuple(1, "A"),
                tuple(2, "B"), tuple(3, "C"), tuple(4, "D"));
    }

    @DataSet
    @Test
    public void testGetListProjectsByUsername() {
        ProjectSearchCriteria criteria = new ProjectSearchCriteria();
        criteria.setInvolvedMember(StringSearchField.and("admin"));
        criteria.setSaccountid(new NumberSearchField(1));

        List projects = projectService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));

        assertThat(projects.size()).isEqualTo(2);
        assertThat(projects).extracting("id", "name").contains(tuple(1, "A"), tuple(2, "B"));
    }

    @DataSet
    @Test
    public void testGetProjectsUserInvolved() {
        List<SimpleProject> projects = projectService.getProjectsUserInvolved("admin", 1);
        Assert.assertEquals(2, projects.size());
        assertThat(projects.size()).isEqualTo(2);
        assertThat(projects).extracting("id", "name").contains(tuple(1, "A"), tuple(2, "B"));
    }

    @DataSet
    @Test
    public void testGetActivityStreams() {
        ActivityStreamSearchCriteria criteria = new ActivityStreamSearchCriteria();
        criteria.setModuleSet(new SetSearchField<>(ModuleNameConstants.PRJ));
        criteria.setExtraTypeIds(new SetSearchField<>(4));
        criteria.setSaccountid(new NumberSearchField(1));
        List<ProjectActivityStream> streams = projectActivityStreamService.getProjectActivityStreams(new BasicSearchRequest<>(criteria));

        assertThat(streams.size()).isEqualTo(3);
        assertThat(streams).extracting("type", "typeid", "itemKey").contains(
                tuple("Project-Bug", "1", 20), tuple("Project-Task", "1", 10),
                tuple("Project-Risk", "1", null));
    }
}
