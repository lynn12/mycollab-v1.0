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
package com.mycollab.reporting;

import com.mycollab.vaadin.UserUIContext;

import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.OutputStream;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

/**
 * @author MyCollab Ltd
 * @since 5.4.7
 */
public class PdfReportBuilder extends AbstractReportBuilder {

    PdfReportBuilder(String reportTitle, RpFieldsBuilder fieldBuilder, Class classType, Map parameters) {
        super(reportTitle, fieldBuilder, classType, parameters);
    }

    @Override
    protected void initReport() {
        reportBuilder
                .setPageFormat(PageType.A3, PageOrientation.LANDSCAPE)
                .setHighlightDetailEvenRows(true)
                .setColumnStyle(stl.style(stl.style().setLeftPadding(4)))
                .setColumnTitleStyle(ReportStyles.instance().getColumnTitleStyle())
                .pageFooter(cmp.pageXofY())
                .setLocale(UserUIContext.getUserLocale());
    }

    @Override
    public void toStream(OutputStream outputStream) throws DRException {
        reportBuilder.toPdf(outputStream);
    }
}
