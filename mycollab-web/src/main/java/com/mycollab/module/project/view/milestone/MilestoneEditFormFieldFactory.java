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

import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class MilestoneEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleMilestone> {
    private static final long serialVersionUID = 1L;

    private AttachmentUploadField attachmentUploadField;

    MilestoneEditFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (Milestone.Field.assignuser.equalTo(propertyId)) {
            ProjectMemberSelectionField memberSelectionField = new ProjectMemberSelectionField();
            memberSelectionField.setRequired(true);
            memberSelectionField.setRequiredError("Please select an assignee");
            return memberSelectionField;
        } else if (propertyId.equals("status")) {
            return new ProgressStatusComboBox();
        } else if (propertyId.equals("name")) {
            final TextField tf = new TextField();
            if (isValidateForm) {
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError("Please enter name");
            }
            return tf;
        } else if (propertyId.equals("description")) {
            RichTextArea descArea = new RichTextArea();
            descArea.setNullRepresentation("");
            return descArea;
        } else if (Milestone.Field.saccountid.equalTo(propertyId)) {
            Milestone beanItem = attachForm.getBean();
            if (beanItem.getId() != null) {
                String attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(MyCollabUI.getAccountId(),
                        beanItem.getProjectid(), ProjectTypeConstants.MILESTONE, "" + beanItem.getId());
                attachmentUploadField = new AttachmentUploadField(attachmentPath);
            } else {
                attachmentUploadField = new AttachmentUploadField();
            }
            return attachmentUploadField;
        }

        return null;
    }

    public AttachmentUploadField getAttachmentUploadField() {
        return attachmentUploadField;
    }

    private static class ProgressStatusComboBox extends I18nValueComboBox {
        private static final long serialVersionUID = 1L;

        ProgressStatusComboBox() {
            super();
            setCaption(null);
            this.setNullSelectionAllowed(false);
            this.loadData(Arrays.asList(MilestoneStatus.InProgress, MilestoneStatus.Future, MilestoneStatus.Closed));

            this.setItemIcon(MilestoneStatus.InProgress.name(), FontAwesome.SPINNER);
            this.setItemIcon(MilestoneStatus.Future.name(), FontAwesome.CLOCK_O);
            this.setItemIcon(MilestoneStatus.Closed.name(), FontAwesome.MINUS_CIRCLE);
        }

        @Override
        public void setPropertyDataSource(Property newDataSource) {
            Object value = newDataSource.getValue();
            if (value == null) {
                newDataSource.setValue(MilestoneStatus.InProgress.name());
            }
            super.setPropertyDataSource(newDataSource);
        }
    }

}
