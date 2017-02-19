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
package com.mycollab.module.project.view.milestone;

import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.db.arguments.DateSearchField;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.RiskI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.mycollab.module.project.view.ticket.ToggleTicketSummaryField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.ui.field.DateViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.mycollab.vaadin.web.ui.*;
import com.mycollab.vaadin.web.ui.field.ContainerViewField;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class MilestonePreviewForm extends AdvancedPreviewBeanForm<SimpleMilestone> {
    @Override
    public void setBean(SimpleMilestone bean) {
        this.setFormLayoutFactory(new DefaultDynaFormLayout(ProjectTypeConstants.MILESTONE, MilestoneDefaultFormLayoutFactory.getForm(),
                Milestone.Field.name.name()));
        this.setBeanFormFieldFactory(new MilestoneFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class MilestoneFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> {
        private static final long serialVersionUID = 1L;

        MilestoneFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            SimpleMilestone beanItem = attachForm.getBean();
            SimpleMilestone milestone = attachForm.getBean();
            if (Milestone.Field.startdate.equalTo(propertyId)) {
                return new DateViewField(milestone.getStartdate());
            } else if (Milestone.Field.enddate.equalTo(propertyId)) {
                return new DateViewField(milestone.getEnddate());
            } else if (Milestone.Field.assignuser.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(milestone.getAssignuser(), milestone.getOwnerAvatarId(), milestone.getOwnerFullName());
            } else if (Milestone.Field.description.equalTo(propertyId)) {
                return new RichTextViewField(milestone.getDescription());
            } else if (Milestone.Field.status.equalTo(propertyId)) {
                String milestoneStatus = UserUIContext.getMessage(MilestoneStatus.class, beanItem.getStatus());
                FontAwesome statusIcon = ProjectAssetsUtil.getPhaseIcon(beanItem.getStatus());
                return new DefaultViewField(statusIcon.getHtml() + " " + milestoneStatus, ContentMode.HTML)
                        .withStyleName(UIConstants.FIELD_NOTE);
            } else if (Milestone.Field.id.equalTo(propertyId)) {
                ContainerViewField containerField = new ContainerViewField();
                containerField.addComponentField(new AssignmentsComp(beanItem));
                return containerField;
            } else if (Milestone.Field.saccountid.equalTo(propertyId)) {
                return new ProjectFormAttachmentDisplayField(beanItem.getProjectid(), ProjectTypeConstants.MILESTONE,
                        beanItem.getId());
            }
            return null;
        }
    }

    private static class AssignmentsComp extends MVerticalLayout {
        private ProjectTicketSearchCriteria searchCriteria;
        private SimpleMilestone beanItem;
        private DefaultBeanPagedList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> assignmentsLayout;

        AssignmentsComp(SimpleMilestone milestone) {
            this.beanItem = milestone;
            withMargin(false).withFullWidth();
            MHorizontalLayout header = new MHorizontalLayout().withFullWidth();

            final CheckBox openSelection = new CheckBox(UserUIContext.getMessage(StatusI18nEnum.Open), true);
            openSelection.addValueChangeListener(valueChangeEvent -> {
                if (openSelection.getValue()) {
                    searchCriteria.setIsOpenned(new SearchField());
                } else {
                    searchCriteria.setIsOpenned(null);
                }
                updateSearchStatus();
            });

            final CheckBox overdueSelection = new CheckBox(UserUIContext.getMessage(StatusI18nEnum.Overdue), false);
            overdueSelection.addValueChangeListener(valueChangeEvent -> {
                if (overdueSelection.getValue()) {
                    searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS()));
                } else {
                    searchCriteria.setDueDate(null);
                }
                updateSearchStatus();
            });

            Label spacingLbl1 = new Label("");
            Label spacingLbl2 = new Label("");

            final CheckBox taskSelection = new CheckBox(UserUIContext.getMessage(TaskI18nEnum.LIST), true);
            taskSelection.addValueChangeListener(valueChangeEvent -> updateTypeSearchStatus(taskSelection.getValue(),
                    ProjectTypeConstants.TASK));

            final CheckBox bugSelection = new CheckBox(UserUIContext.getMessage(BugI18nEnum.LIST), true);
            bugSelection.addValueChangeListener(valueChangeEvent -> updateTypeSearchStatus(bugSelection.getValue(),
                    ProjectTypeConstants.BUG));

            final CheckBox riskSelection = new CheckBox(UserUIContext.getMessage(RiskI18nEnum.LIST), true);
            riskSelection.addValueChangeListener(valueChangeEvent -> updateTypeSearchStatus(riskSelection.getValue(),
                    ProjectTypeConstants.RISK));

            header.with(openSelection, overdueSelection, spacingLbl1, taskSelection, bugSelection, riskSelection, spacingLbl2)
                    .withAlign(openSelection, Alignment.MIDDLE_LEFT).withAlign(overdueSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(taskSelection, Alignment.MIDDLE_LEFT).withAlign(bugSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(riskSelection, Alignment.MIDDLE_LEFT).expand(spacingLbl1, spacingLbl2);

            assignmentsLayout = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(ProjectTicketService.class), new GenericTaskRowRenderer());
            assignmentsLayout.setMargin(new MarginInfo(true, true, true, false));
            this.with(header, assignmentsLayout);
            searchCriteria = new ProjectTicketSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            searchCriteria.setIsOpenned(new SearchField());
            searchCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK, ProjectTypeConstants.RISK));
            searchCriteria.setMilestoneId(new NumberSearchField(beanItem.getId()));
            updateSearchStatus();
        }

        private void updateTypeSearchStatus(boolean selection, String type) {
            SetSearchField<String> types = searchCriteria.getTypes();
            if (types == null) {
                types = new SetSearchField<>();
            }
            if (selection) {
                types.addValue(type);
            } else {
                types.removeValue(type);
            }
            searchCriteria.setTypes(types);
            updateSearchStatus();
        }

        private void updateSearchStatus() {
            assignmentsLayout.setSearchCriteria(searchCriteria);
        }
    }

    private static class GenericTaskRowRenderer implements IBeanList.RowDisplayHandler<ProjectTicket> {
        @Override
        public Component generateRow(IBeanList<ProjectTicket> host, ProjectTicket genericTask, int rowIndex) {
            MHorizontalLayout rowComp = new MHorizontalLayout().withStyleName(WebThemes.HOVER_EFFECT_NOT_BOX, "margin-bottom");
            rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
            rowComp.with(ELabel.fontIcon(ProjectAssetsManager.getAsset(genericTask.getType())).withWidthUndefined());
            String status = "";
            if (genericTask.isBug()) {
                status = UserUIContext.getMessage(BugStatus.class, genericTask.getStatus());
            } else if (genericTask.isMilestone()) {
                status = UserUIContext.getMessage(MilestoneStatus.class, genericTask.getStatus());
            } else if (genericTask.isRisk()) {
                status = UserUIContext.getMessage(StatusI18nEnum.class, genericTask.getStatus());
            } else if (genericTask.isTask()) {
                status = UserUIContext.getMessage(StatusI18nEnum.class, genericTask.getStatus());
            }
            rowComp.with(new ELabel(status).withStyleName(UIConstants.BLOCK).withWidthUndefined());
            String avatarLink = StorageFactory.getAvatarPath(genericTask.getAssignUserAvatarId(), 16);
            Img img = new Img(genericTask.getAssignUserFullName(), avatarLink).setCSSClass(UIConstants.CIRCLE_BOX)
                    .setTitle(genericTask.getAssignUserFullName());

            ToggleTicketSummaryField toggleTicketSummaryField = new ToggleTicketSummaryField(genericTask);
            rowComp.with(ELabel.html(img.write()).withWidthUndefined(), toggleTicketSummaryField).expand(toggleTicketSummaryField);
            return rowComp;
        }
    }
}
