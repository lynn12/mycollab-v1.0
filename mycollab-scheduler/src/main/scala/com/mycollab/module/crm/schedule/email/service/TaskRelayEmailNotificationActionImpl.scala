/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.schedule.email.service

import com.hp.gagawa.java.elements.{Img, Span, Text}
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.{FormatUtils, LinkUtils}
import com.mycollab.module.crm.domain.{SimpleCrmTask, CrmTask}
import com.mycollab.module.crm.i18n.TaskI18nEnum
import com.mycollab.module.crm.service.{ContactService, TaskService}
import com.mycollab.module.crm.{CrmLinkGenerator, CrmResources, CrmTypeConstants}
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.crm.TaskRelayEmailNotificationAction
import com.mycollab.schedule.email.format.{DateFieldFormat, FieldFormat}
import com.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.mycollab.spring.AppContextUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 4.6.0
  */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE) class TaskRelayEmailNotificationActionImpl extends CrmDefaultSendingRelayEmailAction[SimpleCrmTask] with TaskRelayEmailNotificationAction {
  private val LOG = LoggerFactory.getLogger(classOf[TaskRelayEmailNotificationActionImpl])
  
  @Autowired var taskService: TaskService = _
  private val mapper = new TaskFieldNameMapper
  
  protected def buildExtraTemplateVariables(context: MailContext[SimpleCrmTask]) {
    val summary = bean.getSubject
    val summaryLink = CrmLinkGenerator.generateTaskPreviewFullLink(siteUrl, bean.getId)
    val emailNotification = context.getEmailNotification
    
    val avatarId = if (changeUser != null) changeUser.getAvatarid else ""
    val userAvatar: Img = LinkUtils.newAvatar(avatarId)
    
    val makeChangeUser = userAvatar.toString + " " + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => TaskI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => TaskI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => TaskI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }
    
    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("name", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }
  
  protected def getCreateSubjectKey: Enum[_] = TaskI18nEnum.MAIL_CREATE_ITEM_SUBJECT
  
  protected def getUpdateSubjectKey: Enum[_] = TaskI18nEnum.MAIL_UPDATE_ITEM_SUBJECT
  
  protected def getCommentSubjectKey: Enum[_] = TaskI18nEnum.MAIL_COMMENT_ITEM_SUBJECT
  
  protected def getItemName: String = StringUtils.trim(bean.getSubject, 100)
  
  protected def getItemFieldMapper: ItemFieldMapper = mapper
  
  protected def getBeanInContext(notification: SimpleRelayEmailNotification): SimpleCrmTask =
    taskService.findById(notification.getTypeid.toInt, notification.getSaccountid)
  
  class TaskFieldNameMapper extends ItemFieldMapper {
    put(CrmTask.Field.subject, TaskI18nEnum.FORM_SUBJECT, isColSpan = true)
    put(CrmTask.Field.status, GenericI18Enum.FORM_STATUS)
    put(CrmTask.Field.startdate, new DateFieldFormat(CrmTask.Field.startdate.name, GenericI18Enum.FORM_START_DATE))
    put(CrmTask.Field.assignuser, new AssigneeFieldFormat(CrmTask.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(CrmTask.Field.duedate, new DateFieldFormat(CrmTask.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE))
    put(CrmTask.Field.contactid, new ContactFieldFormat(CrmTask.Field.contactid.name, TaskI18nEnum.FORM_CONTACT))
    put(CrmTask.Field.priority, TaskI18nEnum.FORM_PRIORITY)
    put(CrmTask.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
  }
  
  class ContactFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    
    def formatField(context: MailContext[_]): String = {
      val task = context.getWrappedBean.asInstanceOf[SimpleCrmTask]
      if (task.getContactid != null) {
        val img = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.CONTACT))
        val contactLink = CrmLinkGenerator.generateContactPreviewFullLink(context.siteUrl, task.getContactid)
        val link = FormatUtils.newA(contactLink, task.getContactName)
        FormatUtils.newLink(img, link).write
      }
      else {
        new Span().write
      }
    }
    
    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value)) {
        new Span().write
      }
      try {
        val contactId = value.toInt
        val contactService = AppContextUtil.getSpringBean(classOf[ContactService])
        val contact = contactService.findById(contactId, context.getUser.getAccountId)
        if (contact != null) {
          val img = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.CONTACT))
          val contactLink = CrmLinkGenerator.generateContactPreviewFullLink(context.siteUrl, contact.getId)
          val link = FormatUtils.newA(contactLink, contact.getDisplayName)
          return FormatUtils.newLink(img, link).write
        }
      }
      catch {
        case e: Exception => LOG.error("Error", e)
      }
      value
    }
  }
  
  class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    def formatField(context: MailContext[_]): String = {
      val task = context.getWrappedBean.asInstanceOf[SimpleCrmTask]
      if (task.getAssignuser != null) {
        val userAvatarLink = MailUtils.getAvatarLink(task.getAssignUserAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(task.getSaccountid),
          task.getAssignuser)
        val link = FormatUtils.newA(userLink, task.getAssignUserFullName)
        FormatUtils.newLink(img, link).write
      }
      else {
        new Span().write
      }
    }
    
    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value)) {
        new Span().write
      } else {
        val userService = AppContextUtil.getSpringBean(classOf[UserService])
        val user = userService.findUserByUserNameInAccount(value, context.getUser.getAccountId)
        if (user != null) {
          val userAvatarLink = MailUtils.getAvatarLink(user.getAvatarid, 16)
          val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.getAccountId),
            user.getUsername)
          val img = FormatUtils.newImg("avatar", userAvatarLink)
          val link = FormatUtils.newA(userLink, user.getDisplayName)
          FormatUtils.newLink(img, link).write
        } else
          value
      }
    }
  }
  
}
