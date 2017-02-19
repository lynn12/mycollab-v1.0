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
package com.mycollab.module.crm.view.activity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.DateDynaFieldBuilder;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextAreaDynaFieldBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.crm.i18n.TaskI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AssignmentDefaultFormLayoutFactory {
    public static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection taskSection = new DynaSectionBuilder().orderIndex(0)
                .layoutType(LayoutType.TWO_COLUMN).header(TaskI18nEnum.SECTION_TASK_INFORMATION)
                .build();

        taskSection.fields(new TextDynaFieldBuilder().fieldName("subject")
                .displayName(TaskI18nEnum.FORM_SUBJECT).fieldIndex(0).mandatory(true).build());

        taskSection.fields(new TextDynaFieldBuilder().fieldName("status")
                .displayName(GenericI18Enum.FORM_STATUS).fieldIndex(1).build());

        taskSection.fields(new DateDynaFieldBuilder().fieldName("startdate")
                .displayName(GenericI18Enum.FORM_START_DATE).fieldIndex(2).build());

        taskSection.fields(new TextDynaFieldBuilder().fieldName("type")
                .displayName(TaskI18nEnum.FORM_RELATED_TO).fieldIndex(3).build());

        taskSection.fields(new DateDynaFieldBuilder().fieldName("duedate")
                .displayName(GenericI18Enum.FORM_DUE_DATE).fieldIndex(4).build());

        taskSection.fields(new TextDynaFieldBuilder()
                .fieldName("contactid").displayName(TaskI18nEnum.FORM_CONTACT).fieldIndex(5)
                .build());

        taskSection.fields(new TextDynaFieldBuilder().fieldName("priority")
                .displayName(TaskI18nEnum.FORM_PRIORITY).fieldIndex(6).build());

        taskSection.fields(new TextDynaFieldBuilder().fieldName("assignuser")
                .displayName(GenericI18Enum.FORM_ASSIGNEE)
                .fieldIndex(7).build());

        defaultForm.sections(taskSection);

        DynaSection descSection = new DynaSectionBuilder()
                .layoutType(LayoutType.ONE_COLUMN).orderIndex(1)
                .header(GenericI18Enum.FORM_DESCRIPTION).build();

        descSection.fields(new TextAreaDynaFieldBuilder()
                .fieldName("description").displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(0).build());

        defaultForm.sections(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}
