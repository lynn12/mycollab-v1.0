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

import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectMemberServiceTest extends IntegrationServiceTest {
    @Autowired
    private ProjectMemberService projectMemberService;

    @DataSet
    @Test
    public void testGetActiveMembersInproject() {
        List<SimpleUser> activeUsers = projectMemberService.getActiveUsersInProject(1, 1);
        assertThat(activeUsers.size()).isEqualTo(1);
        assertThat(activeUsers).extracting("username").contains("user1");
    }

    @DataSet
    @Test
    public void testGetMembersNotInProject() {
        List<SimpleUser> users = projectMemberService.getUsersNotInProject(1, 1);

        assertThat(users.size()).isEqualTo(2);
        assertThat(users).extracting("username").contains("user2", "user3");
    }

    @DataSet
    @Test
    public void testGetProjectMembersInProjects() {
        List<SimpleUser> users = projectMemberService.getActiveUsersInProjects(Arrays.asList(1, 2), 1);

        assertThat(users.size()).isEqualTo(3);
        assertThat(users).extracting("username").contains("user1", "user2", "user3");
    }

    @DataSet
    @Test
    public void testGetUsersNotInProjects() {
        List<SimpleUser> users = projectMemberService.getUsersNotInProject(1, 1);
        assertThat(users.size()).isEqualTo(2);
        assertThat(users).extracting("username").contains("user2", "user3");
    }

    @DataSet
    @Test
    public void testFindMemberByUsername() {
        SimpleProjectMember member = projectMemberService.findMemberByUsername("user1", 1, 1);
        assertThat(member.getProjectid()).isEqualTo(1);
        assertThat(member.getStatus()).isEqualTo("Active");
        assertThat(member.getUsername()).isEqualTo("user1");
    }
}
