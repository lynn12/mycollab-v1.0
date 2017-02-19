/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.domain;

import com.mycollab.core.utils.DateTimeUtils;

import java.util.Date;

public class SimpleItemTimeLogging extends ItemTimeLogging {
    private static final long serialVersionUID = 1L;

    private String logUserAvatarId;

    private String logUserFullName;

    private Double logUserRate;

    private Double logUserOvertimeRate;

    private String projectName;

    private String projectShortName;

    private String name;

    private Double percentageComplete;

    private String status;

    private Date dueDate;

    private Integer extraTypeId;

    public String getLogUserFullName() {
        return logUserFullName;
    }

    public void setLogUserFullName(String logUserFullName) {
        this.logUserFullName = logUserFullName;
    }

    public String getProjectName() {
        return projectName == null ? "" : projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectShortName() {
        return projectShortName == null ? "" : projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public Double getPercentageComplete() {
        return percentageComplete;
    }

    public void setPercentageComplete(Double percentageComplete) {
        this.percentageComplete = percentageComplete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getLogUserAvatarId() {
        return logUserAvatarId;
    }

    public void setLogUserAvatarId(String logUserAvatarId) {
        this.logUserAvatarId = logUserAvatarId;
    }

    public Double getLogUserRate() {
        return logUserRate;
    }

    public void setLogUserRate(Double logUserRate) {
        this.logUserRate = logUserRate;
    }

    public Double getLogUserOvertimeRate() {
        return logUserOvertimeRate;
    }

    public void setLogUserOvertimeRate(Double logUserOvertimeRate) {
        this.logUserOvertimeRate = logUserOvertimeRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOverdue() {
        if (getDueDate() != null) {
            return getDueDate().before(DateTimeUtils.getCurrentDateWithoutMS());
        }

        return false;
    }

    public Integer getExtraTypeId() {
        return extraTypeId;
    }

    public void setExtraTypeId(Integer extraTypeId) {
        this.extraTypeId = extraTypeId;
    }

    public enum Field {
        summary, projectName, logUserFullName;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
