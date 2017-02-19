/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.arguments.NotBindable;
import com.mycollab.core.utils.ClassUtils;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.validator.constraints.DateComparison;
import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerState;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public abstract class AbstractBeanFieldGroupFieldFactory<B> implements IBeanFieldGroupFieldFactory<B>, FieldGroup.CommitHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBeanFieldGroupFieldFactory.class);

    protected GenericBeanForm<B> attachForm;
    protected FieldGroup fieldGroup;
    protected boolean isValidateForm;
    protected boolean isReadOnlyGroup;
    protected Validator validation;

    public AbstractBeanFieldGroupFieldFactory(GenericBeanForm<B> form, boolean isValidateForm, boolean isReadOnlyGroup) {
        this.attachForm = form;
        this.fieldGroup = new FieldGroup();
        this.fieldGroup.setBuffered(true);
        this.isValidateForm = isValidateForm;
        this.isReadOnlyGroup = isReadOnlyGroup;

        if (isValidateForm) {
            fieldGroup.addCommitHandler(this);
            validation = AppContextUtil.getValidator();
        }
    }

    @Override
    public void setBean(B bean) {
        fieldGroup.setItemDataSource(new BeanItem<>(bean));
        IFormLayoutFactory layoutFactory = attachForm.getLayoutFactory();
        if (layoutFactory instanceof WrappedFormLayoutFactory) {
            layoutFactory = ((WrappedFormLayoutFactory) layoutFactory).getWrappedFactory();
        }
        if (layoutFactory instanceof IDynaFormLayout) {
            IDynaFormLayout dynaFormLayout = (IDynaFormLayout) layoutFactory;
            Set<String> bindFields = dynaFormLayout.bindFields();
            for (String bindField : bindFields) {
                Field<?> formField = onCreateField(bindField);
                if (formField == null) {
                    if (isReadOnlyGroup) {
                        try {
                            String propertyValue = BeanUtils.getProperty(attachForm.getBean(), bindField);
                            formField = new DefaultViewField(propertyValue);
                        } catch (Exception e) {
                            LOG.error("Error while get field value", e);
                            formField = new DefaultViewField("Error");
                        }
                    } else {
                        formField = fieldGroup.buildAndBind(bindField);
                    }
                } else {
                    if (formField instanceof DummyCustomField) {
                        continue;
                    } else if (!(formField instanceof CompoundCustomField)) {
                        fieldGroup.bind(formField, bindField);
                    }
                }

                if (formField instanceof AbstractTextField) {
                    ((AbstractTextField) formField).setNullRepresentation("");
                } else if (formField instanceof RichTextArea) {
                    ((RichTextArea) formField).setNullRepresentation("");
                } else if (formField instanceof DateField && !(formField instanceof PopupDateFieldExt)) {
                    ((DateField) formField).setTimeZone(UserUIContext.getUserTimeZone());
                    ((DateField) formField).setDateFormat(MyCollabUI.getDateFormat());
                }
                postCreateField(bindField, formField);
                attachForm.attachField(bindField, formField);
            }
        } else {
            Class<?> beanClass = bean.getClass();
            java.lang.reflect.Field[] fields = ClassUtils.getAllFields(beanClass);
            for (java.lang.reflect.Field field : fields) {
                Field<?> formField = onCreateField(field.getName());
                if (formField == null) {
                    if (field.getAnnotation(NotBindable.class) != null) {
                        continue;
                    } else {
                        if (isReadOnlyGroup) {
                            try {
                                String propertyValue = BeanUtils.getProperty(attachForm.getBean(), field.getName());
                                formField = new DefaultViewField(propertyValue);
                            } catch (Exception e) {
                                LOG.error("Error while get field value", e);
                                formField = new DefaultViewField("Error");
                            }
                        } else {
                            formField = fieldGroup.buildAndBind(field.getName());
                        }
                    }
                } else {
                    if (formField instanceof DummyCustomField) {
                        continue;
                    } else if (!(formField instanceof CompoundCustomField)) {
                        if (!isReadOnlyGroup) {
                            fieldGroup.bind(formField, field.getName());
                        }
                    }
                }

                if (formField instanceof AbstractTextField) {
                    ((AbstractTextField) formField).setNullRepresentation("");
                } else if (formField instanceof RichTextArea) {
                    ((RichTextArea) formField).setNullRepresentation("");
                } else if (formField instanceof DateField) {
                    ((DateField) formField).setTimeZone(UserUIContext.getUserTimeZone());
                    ((DateField) formField).setDateFormat(MyCollabUI.getDateFormat());
                } else if (formField instanceof DatePicker) {
                    ((DatePicker) formField).setResolution(DatePickerState.Resolution.DAY);
                    formField.setWidth("100px");
                }
                postCreateField(field.getName(), formField);
                attachForm.attachField(field.getName(), formField);
            }
        }
    }

    @Override
    public boolean commit() {
        try {
            fieldGroup.commit();
            attachForm.setValid(true);
            return true;
        } catch (FieldGroup.CommitException e) {
            attachForm.setValid(false);
            Map<Field<?>, com.vaadin.data.Validator.InvalidValueException> invalidFields = e.getInvalidFields();
            if (invalidFields != null && invalidFields.size() > 0) {
                StringBuilder errorMsg = new StringBuilder();
                for (Map.Entry<Field<?>, com.vaadin.data.Validator.InvalidValueException> entry : invalidFields.entrySet()) {
                    com.vaadin.data.Validator.InvalidValueException invalidEx = entry.getValue();
                    errorMsg.append(invalidEx.getHtmlMessage()).append("<br/>");
                    entry.getKey().addStyleName("errorField");
                }
                NotificationUtil.showErrorNotification(errorMsg.toString());
                return false;
            } else {
                NotificationUtil.showErrorNotification(e.getCause().getMessage());
                return false;
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
        for (Object propertyId : fieldGroup.getBoundPropertyIds()) {
            fieldGroup.getField(propertyId).removeStyleName("errorField");
        }
        StringBuilder errorMsg = new StringBuilder();
        int violationCount = 0;
        for (Field<?> f : commitEvent.getFieldBinder().getFields()) {
            try {
                if (f instanceof CustomField) {
                    continue;
                }
                f.validate();
            } catch (com.vaadin.data.Validator.InvalidValueException e) {
                violationCount++;
                errorMsg.append(e.getHtmlMessage()).append("<br/>");
                f.addStyleName("errorField");
            }
        }
        if (violationCount > 0) {
            throw new FieldGroup.CommitException(errorMsg.toString());
        }
    }

    @Override
    public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
        Set<ConstraintViolation<B>> violations = validation.validate(attachForm.getBean());
        if (violations.size() > 0) {
            StringBuilder errorMsg = new StringBuilder();

            for (ConstraintViolation violation : violations) {
                errorMsg.append(violation.getMessage()).append("<br/>");
                Path propertyPath = violation.getPropertyPath();
                if (propertyPath != null && !propertyPath.toString().equals("")) {
                    fieldGroup.getField(propertyPath.toString()).addStyleName("errorField");
                } else {
                    Annotation validateAnno = violation.getConstraintDescriptor().getAnnotation();
                    if (validateAnno instanceof DateComparison) {
                        String firstDateField = ((DateComparison) validateAnno).firstDateField();
                        String lastDateField = ((DateComparison) validateAnno).lastDateField();

                        fieldGroup.getField(firstDateField).addStyleName("errorField");
                        fieldGroup.getField(lastDateField).addStyleName("errorField");
                    }
                }

            }
            throw new FieldGroup.CommitException(errorMsg.toString());
        }
    }

    @Override
    public void setBuffered(boolean isBuffered) {
        if (fieldGroup != null) {
            fieldGroup.setBuffered(isBuffered);
        }
    }

    abstract protected Field<?> onCreateField(Object propertyId);

    protected void postCreateField(Object propertyId, Field<?> field) {
    }
}
