/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.billing.esb

import java.util
import java.util.{Timer, TimerTask}

import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import com.mycollab.common.NotificationType
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.common.i18n.WikiI18nEnum
import com.mycollab.common.service.OptionValService
import com.mycollab.core.utils.{BeanUtility, StringUtils}
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.file.PathUtils
import com.mycollab.module.page.domain.{Folder, Page}
import com.mycollab.module.page.service.PageService
import com.mycollab.module.project.domain._
import com.mycollab.module.project.i18n.OptionI18nEnum._
import com.mycollab.module.project.service._
import com.mycollab.module.tracker.domain.{BugWithBLOBs, Version}
import com.mycollab.module.tracker.service._
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 5.1.1
  */
@Component class AccountCreatedCommand extends GenericCommand {

  @Autowired private val optionValService: OptionValService = null
  @Autowired private val projectService: ProjectService = null
  @Autowired private val messageService: MessageService = null
  @Autowired private val milestoneService: MilestoneService = null
  @Autowired private val taskService: ProjectTaskService = null
  @Autowired private val bugService: BugService = null
  @Autowired private val bugRelatedService: BugRelatedItemService = null
  @Autowired private val componentService: ComponentService = null
  @Autowired private val versionService: VersionService = null
  @Autowired private val pageService: PageService = null
  @Autowired private val resourceService: ResourceService = null
  @Autowired val projectNotificationSettingService: ProjectNotificationSettingService = null

  @AllowConcurrentEvents
  @Subscribe
  def execute(event: AccountCreatedEvent): Unit = {
    createDefaultOptionVals(event.accountId)
    if (event.createSampleData) {
      createSampleProjectData(event.initialUser, event.accountId)
    }
  }

  private def createDefaultOptionVals(accountId: Integer): Unit = {
    optionValService.createDefaultOptions(accountId)
  }

  private def createSampleProjectData(initialUser: String, accountId: Integer): Unit = {
    val now: DateTime = new DateTime()

    val project = new Project()
    project.setSaccountid(accountId)
    project.setDescription("Sample project")
    project.setHomepage("https://www.mycollab.com")
    project.setName("Sample project")
    project.setProjectstatus(StatusI18nEnum.Open.name())
    project.setShortname("SP1")
    val projectId = projectService.saveWithSession(project, initialUser)

    val projectNotificationSetting = new ProjectNotificationSetting()
    projectNotificationSetting.setLevel(NotificationType.None.name())
    projectNotificationSetting.setProjectid(projectId)
    projectNotificationSetting.setSaccountid(accountId)
    projectNotificationSetting.setUsername(initialUser)
    projectNotificationSettingService.saveWithSession(projectNotificationSetting, initialUser)

    val message = new Message()
    message.setIsstick(true)
    message.setPosteduser(initialUser)
    message.setMessage("Welcome to MyCollab workspace. I hope you enjoy it!")
    message.setSaccountid(accountId)
    message.setProjectid(projectId)
    message.setTitle("Thank you for using MyCollab!")
    message.setPosteddate(now.toLocalDate.toDate)
    messageService.saveWithSession(message, initialUser)

    val milestone: Milestone = new Milestone()
    milestone.setCreateduser(initialUser)
    milestone.setDuedate(now.plusDays(14).toLocalDate.toDate)
    milestone.setStartdate(now.toLocalDate.toDate)
    milestone.setEnddate(now.plusDays(14).toLocalDate.toDate)
    milestone.setName("Sample milestone")
    milestone.setAssignuser(initialUser)
    milestone.setProjectid(projectId)
    milestone.setSaccountid(accountId)
    milestone.setStatus(MilestoneStatus.InProgress.name())
    val sampleMilestoneId = milestoneService.saveWithSession(milestone, initialUser)

    val taskA = new Task()
    taskA.setName("Task A")
    taskA.setProjectid(projectId)
    taskA.setCreateduser(initialUser)
    taskA.setPercentagecomplete(0d)
    taskA.setPriority(Priority.Medium.name())
    taskA.setSaccountid(accountId)
    taskA.setStatus(StatusI18nEnum.Open.name())
    taskA.setStartdate(now.toLocalDate.toDate)
    taskA.setEnddate(now.plusDays(3).toLocalDate.toDate)
    val taskAId = taskService.saveWithSession(taskA, initialUser)

    val taskB = BeanUtility.deepClone(taskA)
    taskB.setName("Task B")
    taskB.setId(null)
    taskB.setMilestoneid(sampleMilestoneId)
    taskB.setStartdate(now.plusDays(2).toLocalDate.toDate)
    taskB.setEnddate(now.plusDays(4).toLocalDate.toDate)
    taskService.saveWithSession(taskB, initialUser)

    val taskC = BeanUtility.deepClone(taskA)
    taskC.setId(null)
    taskC.setName("Task C")
    taskC.setStartdate(now.plusDays(3).toLocalDate.toDate)
    taskC.setEnddate(now.plusDays(5).toLocalDate.toDate)
    taskC.setParenttaskid(taskAId)
    taskService.saveWithSession(taskC, initialUser)

    val taskD = BeanUtility.deepClone(taskA)
    taskD.setId(null)
    taskD.setName("Task D")
    taskD.setStartdate(now.toLocalDate.toDate)
    taskD.setEnddate(now.plusDays(2).toLocalDate.toDate)
    taskService.saveWithSession(taskD, initialUser)

    val component = new com.mycollab.module.tracker.domain.Component()
    component.setName("Component 1")
    component.setCreateduser(initialUser)
    component.setDescription("Sample Component 1")
    component.setStatus(StatusI18nEnum.Open.name())
    component.setProjectid(projectId)
    component.setSaccountid(accountId)
    component.setUserlead(initialUser)
    componentService.saveWithSession(component, initialUser)

    val version = new Version()
    version.setCreateduser(initialUser)
    version.setName("Version 1")
    version.setDescription("Sample version")
    version.setDuedate(now.plusDays(21).toLocalDate.toDate)
    version.setProjectid(projectId)
    version.setSaccountid(accountId)
    version.setStatus(StatusI18nEnum.Open.name())
    versionService.saveWithSession(version, initialUser)

    val bugA = new BugWithBLOBs()
    bugA.setDescription("Sample bug")
    bugA.setEnvironment("All platforms")
    bugA.setAssignuser(initialUser)
    bugA.setDuedate(now.plusDays(2).toLocalDate.toDate)
    bugA.setCreateduser(initialUser)
    bugA.setMilestoneid(sampleMilestoneId)
    bugA.setName("Bug A")
    bugA.setStatus(BugStatus.Open.name())
    bugA.setPriority(Priority.Medium.name())
    bugA.setProjectid(projectId)
    bugA.setSaccountid(accountId)
    val bugAId = bugService.saveWithSession(bugA, initialUser)

    val bugB = BeanUtility.deepClone(bugA)
    bugB.setId(null)
    bugB.setName("Bug B")
    bugB.setStatus(BugStatus.Resolved.name())
    bugB.setResolution(BugResolution.CannotReproduce.name())
    bugB.setPriority(Priority.Low.name())
    bugService.saveWithSession(bugB, initialUser)

    bugRelatedService.saveAffectedVersionsOfBug(bugAId, util.Arrays.asList(version))
    bugRelatedService.saveComponentsOfBug(bugAId, util.Arrays.asList(component))

    val page = new Page()
    page.setSubject("Welcome to sample workspace")
    page.setContent("I hope you enjoy MyCollab!")
    page.setPath(PathUtils.getProjectDocumentPath(accountId, projectId) + "/" + StringUtils.generateSoftUniqueId())
    page.setStatus(WikiI18nEnum.status_public.name())
    pageService.savePage(page, initialUser)

    val folder = new Folder()
    folder.setName("Requirements")
    folder.setDescription("Sample folder")
    folder.setPath(PathUtils.getProjectDocumentPath(accountId, projectId) + "/" + StringUtils.generateSoftUniqueId())
    pageService.createFolder(folder, initialUser)

    val timer = new Timer("Set member notification")
    timer.schedule(new TimerTask {
      override def run(): Unit = {
        projectNotificationSetting.setLevel(NotificationType.Default.name())
        projectNotificationSettingService.updateWithSession(projectNotificationSetting, initialUser)
      }
    }, 90000)
  }
}
