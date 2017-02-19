/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view

import com.google.common.eventbus.Subscribe
import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.BeanUtility
import com.mycollab.db.arguments.{NumberSearchField, SearchField, SetSearchField, StringSearchField}
import com.mycollab.eventmanager.ApplicationEventListener
import com.mycollab.mobile.module.project.events._
import com.mycollab.mobile.module.project.view.message.MessagePresenter
import com.mycollab.mobile.module.project.view.milestone.MilestonePresenter
import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData.{Add, ProjectActivities}
import com.mycollab.mobile.module.project.view.parameters._
import com.mycollab.mobile.module.project.view.settings.ProjectUserPresenter
import com.mycollab.mobile.module.project.view.ticket.TicketPresenter
import com.mycollab.mobile.mvp.view.PresenterOptionUtil
import com.mycollab.module.project.domain._
import com.mycollab.module.project.domain.criteria._
import com.mycollab.module.project.service.ProjectService
import com.mycollab.module.project.{CurrentProjectVariables, ProjectMemberStatusConstants}
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.mvp.{AbstractController, PageActionChain, PresenterResolver, ScreenData}
import com.mycollab.vaadin.{MyCollabUI, UserUIContext}
import com.vaadin.addon.touchkit.ui.NavigationManager

/**
  * @author MyCollab Ltd
  * @since 5.2.5
  */
class ProjectModuleController(val navManager: NavigationManager) extends AbstractController {
  bindProjectEvents()
  bindTicketEvents()
  bindBugEvents()
  bindMessageEvents()
  bindMilestoneEvents()
  bindTaskEvents()
  bindRiskEvents()
  bindMemberEvents()
  
  private def bindProjectEvents() {
    this.register(new ApplicationEventListener[ProjectEvent.GotoAdd]() {
      @Subscribe def handle(event: ProjectEvent.GotoAdd): Unit = {
        val presenter = PresenterOptionUtil.getPresenter(classOf[IProjectAddPresenter])
        presenter.go(navManager, new Add(new SimpleProject))
      }
    })
    this.register(new ApplicationEventListener[ProjectEvent.GotoProjectList]() {
      @Subscribe def handle(event: ProjectEvent.GotoProjectList) {
        val presenter = PresenterResolver.getPresenter(classOf[UserProjectListPresenter])
        val criteria = new ProjectSearchCriteria
        criteria.setInvolvedMember(StringSearchField.and(UserUIContext.getUsername))
        criteria.setProjectStatuses(new SetSearchField[String](StatusI18nEnum.Open.name))
        presenter.go(navManager, new ScreenData.Search[ProjectSearchCriteria](criteria))
      }
    })
    this.register(new ApplicationEventListener[ProjectEvent.GotoMyProject]() {
      @Subscribe def handle(event: ProjectEvent.GotoMyProject) {
        val presenter = PresenterResolver.getPresenter(classOf[ProjectViewPresenter])
        presenter.handleChain(navManager, event.getData.asInstanceOf[PageActionChain])
      }
    })
    this.register(new ApplicationEventListener[ProjectEvent.AllActivities]() {
      @Subscribe def handle(event: ProjectEvent.AllActivities) {
        val presenter = PresenterResolver.getPresenter(classOf[AllActivityStreamPresenter])
        val prjService = AppContextUtil.getSpringBean(classOf[ProjectService])
        val prjKeys = prjService.getProjectKeysUserInvolved(UserUIContext.getUsername, MyCollabUI.getAccountId)
        val searchCriteria = new ActivityStreamSearchCriteria()
        searchCriteria.setModuleSet(new SetSearchField(ModuleNameConstants.PRJ))
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId))
        searchCriteria.setExtraTypeIds(new SetSearchField(prjKeys))
        presenter.go(navManager, new ProjectScreenData.AllActivities(searchCriteria))
      }
    })
    this.register(new ApplicationEventListener[ProjectEvent.MyProjectActivities]() {
      @Subscribe def handle(event: ProjectEvent.MyProjectActivities) {
        val presenter = PresenterResolver.getPresenter(classOf[ProjectActivityStreamPresenter])
        val searchCriteria = new ActivityStreamSearchCriteria()
        searchCriteria.setModuleSet(new SetSearchField(ModuleNameConstants.PRJ))
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId))
        searchCriteria.setExtraTypeIds(new SetSearchField(event.getData.asInstanceOf[Integer]))
        presenter.go(navManager, new ProjectActivities(searchCriteria))
      }
    })
  }
  
  private def bindTicketEvents(): Unit = {
    this.register(new ApplicationEventListener[TicketEvent.GotoDashboard]() {
      @Subscribe def handle(event: TicketEvent.GotoDashboard) {
        val searchCriteria = new ProjectTicketSearchCriteria
        searchCriteria.setProjectIds(new SetSearchField[Integer](CurrentProjectVariables.getProjectId))
        searchCriteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes)
        searchCriteria.setIsOpenned(new SearchField())
        val data = new TicketScreenData.GotoDashboard(searchCriteria)
        val presenter = PresenterResolver.getPresenter(classOf[TicketPresenter])
        presenter.go(navManager, data)
      }
    })
  }
  
  private def bindBugEvents() {
    this.register(new ApplicationEventListener[BugEvent.GotoRead]() {
      @Subscribe def handle(event: BugEvent.GotoRead) {
        val data = new BugScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[TicketPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[BugEvent.GotoAdd]() {
      @Subscribe def handle(event: BugEvent.GotoAdd) {
        val data = new BugScreenData.Add(new SimpleBug)
        val presenter = PresenterResolver.getPresenter(classOf[TicketPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[BugEvent.GotoEdit]() {
      @Subscribe def handle(event: BugEvent.GotoEdit) {
        val data = new BugScreenData.Edit(event.getData.asInstanceOf[SimpleBug])
        val presenter = PresenterResolver.getPresenter(classOf[TicketPresenter])
        presenter.go(navManager, data)
      }
    })
  }
  
  private def bindMessageEvents() {
    this.register(new ApplicationEventListener[MessageEvent.GotoAdd]() {
      @Subscribe def handle(event: MessageEvent.GotoAdd) {
        val data = new MessageScreenData.Add
        val presenter = PresenterResolver.getPresenter(classOf[MessagePresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[MessageEvent.GotoList]() {
      @Subscribe def handle(event: MessageEvent.GotoList) {
        val searchCriteria = new MessageSearchCriteria
        searchCriteria.setProjectids(new SetSearchField[Integer](CurrentProjectVariables.getProjectId))
        val data = new MessageScreenData.Search(searchCriteria)
        val presenter = PresenterResolver.getPresenter(classOf[MessagePresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[MessageEvent.GotoRead]() {
      @Subscribe def handle(event: MessageEvent.GotoRead) {
        val data = new MessageScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[MessagePresenter])
        presenter.go(navManager, data)
      }
    })
  }
  
  private def bindMilestoneEvents() {
    this.register(new ApplicationEventListener[MilestoneEvent.GotoList]() {
      @Subscribe def handle(event: MilestoneEvent.GotoList) {
        val params: Any = event.getData
        val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        if (params == null) {
          val criteria = new MilestoneSearchCriteria
          criteria.setProjectIds(new SetSearchField[Integer](CurrentProjectVariables.getProjectId))
          presenter.go(navManager, new MilestoneScreenData.Search(criteria))
        }
        else if (params.isInstanceOf[MilestoneScreenData.Search]) {
          presenter.go(navManager, params.asInstanceOf[MilestoneScreenData.Search])
        }
        else {
          throw new MyCollabException("Invalid search parameter: " + BeanUtility.printBeanObj(params))
        }
      }
    })
    this.register(new ApplicationEventListener[MilestoneEvent.GotoRead]() {
      @Subscribe def handle(event: MilestoneEvent.GotoRead) {
        val data = new MilestoneScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[MilestoneEvent.GotoAdd]() {
      @Subscribe def handle(event: MilestoneEvent.GotoAdd) {
        val data = new MilestoneScreenData.Add(new SimpleMilestone)
        val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[MilestoneEvent.GotoEdit]() {
      @Subscribe def handle(event: MilestoneEvent.GotoEdit) {
        val data = new MilestoneScreenData.Edit(event.getData.asInstanceOf[SimpleMilestone])
        val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(navManager, data)
      }
    })
  }
  
  private def bindTaskEvents() {
    this.register(new ApplicationEventListener[TaskEvent.GotoRead]() {
      @Subscribe def handle(event: TaskEvent.GotoRead) {
        val data = new TaskScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[TicketPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[TaskEvent.GotoEdit]() {
      @Subscribe def handle(event: TaskEvent.GotoEdit) {
        val data = new TaskScreenData.Edit(event.getData.asInstanceOf[SimpleTask])
        val presenter = PresenterResolver.getPresenter(classOf[TicketPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[TaskEvent.GotoAdd]() {
      @Subscribe def handle(event: TaskEvent.GotoAdd) {
        val data = new TaskScreenData.Add(new SimpleTask)
        val presenter = PresenterResolver.getPresenter(classOf[TicketPresenter])
        presenter.go(navManager, data)
      }
    })
  }
  
  private def bindRiskEvents() {
    this.register(new ApplicationEventListener[RiskEvent.GotoRead]() {
      @Subscribe def handle(event: RiskEvent.GotoRead) {
        val data = new RiskScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterOptionUtil.getPresenter(classOf[TicketPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[RiskEvent.GotoAdd]() {
      @Subscribe def handle(event: RiskEvent.GotoAdd) {
        val data = new RiskScreenData.Add(new SimpleRisk)
        val presenter = PresenterOptionUtil.getPresenter(classOf[TicketPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[RiskEvent.GotoEdit]() {
      @Subscribe def handle(event: RiskEvent.GotoEdit) {
        val data = new RiskScreenData.Edit(event.getData.asInstanceOf[SimpleRisk])
        val presenter = PresenterOptionUtil.getPresenter(classOf[TicketPresenter])
        presenter.go(navManager, data)
      }
    })
  }
  
  private def bindMemberEvents() {
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoList]() {
      @Subscribe def handle(event: ProjectMemberEvent.GotoList) {
        val criteria = new ProjectMemberSearchCriteria
        criteria.setProjectId(NumberSearchField.equal(CurrentProjectVariables.getProjectId))
        criteria.setSaccountid(NumberSearchField.equal(MyCollabUI.getAccountId))
        criteria.setStatuses(new SetSearchField(ProjectMemberStatusConstants.ACTIVE, ProjectMemberStatusConstants.NOT_ACCESS_YET))
        val presenter = PresenterResolver.getPresenter(classOf[ProjectUserPresenter])
        presenter.go(navManager, new ProjectMemberScreenData.Search(criteria))
      }
    })
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoRead]() {
      @Subscribe def handle(event: ProjectMemberEvent.GotoRead) {
        val data = new ProjectMemberScreenData.Read(event.getData)
        val presenter = PresenterResolver.getPresenter(classOf[ProjectUserPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoEdit]() {
      @Subscribe def handle(event: ProjectMemberEvent.GotoEdit) {
        val data = new ProjectMemberScreenData.Edit(event.getData.asInstanceOf[SimpleProjectMember])
        val presenter = PresenterResolver.getPresenter(classOf[ProjectUserPresenter])
        presenter.go(navManager, data)
      }
    })
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoInviteMembers]() {
      @Subscribe def handle(event: ProjectMemberEvent.GotoInviteMembers) {
        val data = new ProjectMemberScreenData.InviteProjectMembers
        val presenter = PresenterResolver.getPresenter(classOf[ProjectUserPresenter])
        presenter.go(navManager, data)
      }
    })
  }
}
