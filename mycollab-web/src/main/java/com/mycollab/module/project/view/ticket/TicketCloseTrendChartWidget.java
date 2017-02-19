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
package com.mycollab.module.project.view.ticket;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.common.domain.OptionVal;
import com.mycollab.common.domain.criteria.TimelineTrackingSearchCriteria;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.common.service.OptionValService;
import com.mycollab.common.service.TimelineTrackingService;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.ui.chart.GenericChartWrapper;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.Depot;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class TicketCloseTrendChartWidget extends Depot {
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd").withZone(DateTimeZone.UTC);

    public TicketCloseTrendChartWidget() {
        super(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_RESOLVING_TREND_IN_DURATION, 30), new MVerticalLayout().withFullWidth());
        setContentBorder(true);
    }

    public void display(TimelineTrackingSearchCriteria searchCriteria) {
        MVerticalLayout content = (MVerticalLayout) getContent();
        content.removeAllComponents();
//        TicketStatusChartWrapper chartWrapper = new TicketStatusChartWrapper();
//        content.addComponent(chartWrapper);
//        chartWrapper.display(searchCriteria);
    }

    private static class TicketStatusChartWrapper extends GenericChartWrapper {
        private TimelineTrackingService timelineTrackingService;
        private Map<String, List<GroupItem>> groupItems;
        private TimeSeriesCollection dataSet;

        private TicketStatusChartWrapper() {
            super(350, 280);
            timelineTrackingService = AppContextUtil.getSpringBean(TimelineTrackingService.class);
        }

        @Override
        protected JFreeChart createChart() {
            dataSet = new TimeSeriesCollection();
            if (groupItems != null) {
                Set<Map.Entry<String, List<GroupItem>>> entries = groupItems.entrySet();
                Map<Date, Double> openMap = new HashMap<>(30);
                for (Map.Entry<String, List<GroupItem>> entry : entries) {
                    if (StatusI18nEnum.Closed.name().equals(entry.getKey())) {
                        TimeSeries series = new TimeSeries(entry.getKey());
                        for (GroupItem item : entry.getValue()) {
                            series.add(new Day(formatter.parseDateTime(item.getGroupname()).toDate()), item.getValue());
                        }
                        dataSet.addSeries(series);
                    } else {
                        for (GroupItem item : entry.getValue()) {
                            Date date = formatter.parseDateTime(item.getGroupname()).toDate();
                            Double val = openMap.get(date);
                            if (val == null) {
                                openMap.put(date, item.getValue());
                            } else {
                                openMap.put(date, val + item.getValue());
                            }
                        }
                    }
                }

                TimeSeries series = new TimeSeries(UserUIContext.getMessage(StatusI18nEnum.Unresolved));
                for (Map.Entry<Date, Double> entry : openMap.entrySet()) {
                    series.add(new Day(entry.getKey()), entry.getValue());
                }
                dataSet.addSeries(series);

                JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", dataSet, false, true, false);
                chart.setBackgroundPaint(Color.white);

                XYPlot plot = (XYPlot) chart.getPlot();
                plot.setBackgroundPaint(Color.white);
                plot.setDomainGridlinePaint(Color.lightGray);
                plot.setRangeGridlinePaint(Color.cyan);
                plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
                plot.setDomainCrosshairVisible(true);
                plot.setRangeCrosshairVisible(true);
                XYItemRenderer r = plot.getRenderer();
                if (r instanceof XYLineAndShapeRenderer) {
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
                    renderer.setBaseShapesVisible(true);
                    renderer.setBaseShapesFilled(true);
                    renderer.setDrawSeriesLineAsPath(true);
                    for (int i = 0; i < entries.size(); i++) {
                        int colorIndex = i % CHART_COLOR_STR.size();
                        renderer.setSeriesPaint(i, Color.decode("0x" + CHART_COLOR_STR.get(colorIndex)));
                    }
                }
                NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
                valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                return chart;
            } else {
                return ChartFactory.createTimeSeriesChart("", "", "", new TimeSeriesCollection(), false, true, false);
            }
        }

        @Override
        protected final ComponentContainer createLegendBox() {
            final CssLayout mainLayout = new CssLayout();
            mainLayout.setWidth("100%");
            mainLayout.addStyleName("legendBoxContent");
            final List series = dataSet.getSeries();

            for (int i = 0; i < series.size(); i++) {
                final MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(false, false, false, true));
                layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

                final TimeSeries key = (TimeSeries) series.get(i);
                int colorIndex = i % CHART_COLOR_STR.size();
                final String color = "<div style = \" width:13px;height:13px;background: #" + CHART_COLOR_STR.get(colorIndex) + "\" />";
                final ELabel lblCircle = ELabel.html(color);

                String captionBtn = UserUIContext.getMessage(StatusI18nEnum.class, (String) key.getKey());
                final MButton btnLink = new MButton(StringUtils.trim(captionBtn, 30, true)).withDescription
                        (captionBtn).withStyleName(WebThemes.BUTTON_LINK);
                layout.with(lblCircle, btnLink);
                mainLayout.addComponent(layout);
            }

            return mainLayout;
        }

        void display(TimelineTrackingSearchCriteria searchCriteria) {
            searchCriteria.setTypes(new SetSearchField(ProjectTypeConstants.TASK, ProjectTypeConstants.BUG, ProjectTypeConstants.TASK));
            searchCriteria.setFieldgroup(StringSearchField.and("status"));
            LocalDate endDate = new LocalDate(new GregorianCalendar().getTime());
            LocalDate startDate = endDate.minusDays(30);
            OptionValService optionValService = AppContextUtil.getSpringBean(OptionValService.class);
            List<OptionVal> optionVals = optionValService.findOptionVals(ProjectTypeConstants.TASK,
                    CurrentProjectVariables.getProjectId(), MyCollabUI.getAccountId());
            List<String> options = optionVals.stream().map(OptionVal::getTypeval).collect(Collectors.toList());
            groupItems = timelineTrackingService.findTimelineItems("status", options, startDate.toDate(), endDate.toDate(), searchCriteria);
            displayChart();
        }
    }
}
