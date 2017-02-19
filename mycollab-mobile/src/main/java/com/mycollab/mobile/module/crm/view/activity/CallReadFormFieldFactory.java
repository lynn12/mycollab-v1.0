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
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.module.crm.ui.RelatedReadItemField;
import com.mycollab.module.crm.domain.SimpleCall;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.field.DateTimeViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class CallReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleCall> {
    private static final long serialVersionUID = 1L;

    public CallReadFormFieldFactory(GenericBeanForm<SimpleCall> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("assignuser")) {
            return new DefaultViewField(attachForm.getBean().getAssignUserFullName());
        } else if (propertyId.equals("typeid")) {
            return new RelatedReadItemField(attachForm.getBean());
        } else if (propertyId.equals("status")) {
            String value = "";
            value += attachForm.getBean().getStatus() != null ? attachForm.getBean().getStatus() + " " : "";
            value += attachForm.getBean().getCalltype() != null ? attachForm.getBean().getCalltype() : "";
            return new DefaultViewField(value);
        } else if (propertyId.equals("durationinseconds")) {
            final Integer duration = attachForm.getBean().getDurationinseconds();
            if (duration != null && duration != 0) {
                final int hours = duration / 3600;
                final int minutes = (duration % 3600) / 60;
                final StringBuffer value = new StringBuffer();
                if (hours == 1) {
                    value.append("1 hour ");
                } else if (hours >= 2) {
                    value.append(hours + " hours ");
                }

                if (minutes > 0) {
                    value.append(minutes + " minutes");
                }

                return new DefaultViewField(value.toString());
            } else {
                return new DefaultViewField("");
            }
        } else if (propertyId.equals("startdate")) {
            if (attachForm.getBean().getStartdate() == null) {
                return new DefaultViewField("");
            } else {
                return new DateTimeViewField(attachForm.getBean().getStartdate());
            }
        }

        return null;
    }

}
