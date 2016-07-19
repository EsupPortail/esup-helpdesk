/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.TicketStatus;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.beans.statistics.MinAvgMaxNumberStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.SpentTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.StatusStatisticSet;
import org.esupportail.helpdesk.services.statistics.StatisticsExtractor;
import org.esupportail.helpdesk.services.statistics.StatisticsUtils;
import org.esupportail.helpdesk.web.beans.OriginI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.SpentTimeStatisticsEntry;
import org.esupportail.helpdesk.web.beans.StatisticsTicketEntry;
import org.esupportail.helpdesk.web.beans.TicketStatusI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.UserTicketCreationStatisticEntry;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 * The journal controller.
 */
public class StatisticsController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6971655261498813555L;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_NONE = 0;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_TICKET_CREATIONS_HISTORY = 1;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_TICKET_CREATIONS_DISTRIBUTION = 2;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_CREATORS = 3;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_CHARGE_HISTORY = 4;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_CHARGE_DISTIBUTION = 5;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_CHARGE_TICKETS = 6;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_CLOSURE_HISTORY = 7;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_CLOSURE_DISTIBUTION = 8;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_CLOSURE_TICKETS = 9;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_STATUS = 10;

	/**
	 * A constant for statistics.
	 */
	private static final Integer STATISTICS_SPENT_TIME = 11;

	/**
	 * A constant for periods.
	 */
//	private static final String PERIOD_ALL = "*";

	/**
	 * A constant for periods.
	 */
	private static final String PERIOD_YEAR = "y";

	/**
	 * A constant for periods.
	 */
	private static final String PERIOD_MONTH = "m";

	/**
	 * A constant for periods.
	 */
	private static final String PERIOD_WEEK = "w";

	/**
	 * A constant for intervals.
	 */
	private static final String INTERVAL_YEAR = "y";

	/**
	 * A constant for intervals.
	 */
//	private static final String INTERVAL_MONTH = "m";

	/**
	 * A constant for intervals.
	 */
//	private static final String INTERVAL_DAY = "d";

	/**
	 * A constant for intervals.
	 */
	private static final String INTERVAL_DOW = "dow";

	/**
	 * A constant for intervals.
	 */
	private static final String INTERVAL_HOD = "hod";

	/**
	 * A constant for intervals.
	 */
//	private static final String INTERVAL_HOW = "how";

	/**
	 * The width step.
	 */
	private static final int WIDTH_STEP = 100;

	/**
	 * The width height.
	 */
	private static final int HEIGHT_STEP = 50;

	/**
	 * The minimum width for bar charts.
	 */
	private static final int BAR_CHART_MIN_WIDTH = 400;

	/**
	 * The maximum width for bar charts.
	 */
	private static final int BAR_CHART_MAX_WIDTH = 1800;

	/**
	 * The maximum width for bar charts.
	 */
	private static final int BAR_CHART_DEFAULT_WIDTH = 800;

	/**
	 * The minimum width for bar charts.
	 */
	private static final int BAR_CHART_MIN_HEIGHT = 200;

	/**
	 * The maximum width for bar charts.
	 */
	private static final int BAR_CHART_MAX_HEIGHT = 1000;

	/**
	 * The maximum width for bar charts.
	 */
	private static final int BAR_CHART_DEFAULT_HEIGHT = 300;

    /**
     * The max numbers for ticket creators.
     */
    private static final Integer [] TICKET_CREATORS_MAX_NUMBERS = {10, 20, 50, 100, };

    /**
     * The max numbers for tickets.
     */
    private static final Integer [] TICKETS_MAX_NUMBERS = {10, 20, 30, };

	/**
	 * Magic number.
	 */
	private static final int SECONDS_PER_HOUR = 3600;

    /**
     * The statistics extractor.
     */
    private StatisticsExtractor statisticsExtrator;

    /**
     * The origin i18n key provider.
     */
    private OriginI18nKeyProvider originI18nKeyProvider;

    /**
     * The selected creation departments.
     */
    private List<Department> creationDepartments;

    /**
     * The selected origins.
     */
    private List<String> origins;

    /**
     * The current statistic.
     */
    private Integer statistics;

    /**
     * The chart data.
     */
    private DefaultCategoryDataset chartData;

    /**
     * The pie data.
     */
    private DefaultPieDataset pieData;

    /**
     * The chart values.
     */
    private Map<String, MinAvgMaxNumberStatistic> chartValues;

    /**
     * The chart title.
     */
    private String chartTitle;

    /**
     * The chart X title.
     */
    private String chartXtitle;

    /**
     * The chart Y title.
     */
    private String chartYtitle;

    /**
     * The width for ticket creations.
     */
    private int chartWidth;

    /**
     * The height for ticket creations.
     */
    private int chartHeight;

    /**
     * The period for ticket creations.
     */
    private String ticketCreationsPeriodSpec;

    /**
     * The interval for ticket creations history.
     */
    private String ticketCreationsHistoryIntervalSpec;

    /**
     * The ticket creations distribution.
     */
    private String ticketCreationsDistributionSpec;

    /**
     * The ticket creations sort.
     */
    private String ticketCreationsSort;

    /**
     * The data for ticket creators.
     */
    private List<UserTicketCreationStatisticEntry> ticketCreatorsData;

    /**
     * The max number for ticket creators.
     */
    private int ticketCreatorsMaxNumber;

    /**
     * The data for tickets.
     */
    private List<StatisticsTicketEntry> ticketsData;

    /**
     * The data for the spent time.
     */
    private List<SpentTimeStatisticsEntry> spentTimeData;

    /**
     * The max number for tickets.
     */
    private int ticketsMaxNumber;

    /**
     * True to show min/max.
     */
    private boolean showMinMax;

    /**
     * True to hide the ticket already taken in charge.
     */
    private boolean hideCharged;

    /**
     * True to hide the ticket already closed.
     */
    private boolean hideClosed;

    /**
     * The status sort.
     */
    private String statusSort;

    /**
     * True to hide the ticket already closed.
     */
    private boolean ignoreArchivedTickets;

	/**
	 * Bean constructor.
	 */
	public StatisticsController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(originI18nKeyProvider,
				"property originI18nKeyProvider of class " + getClass().getName()
				+ " can not be null");
		Assert.notNull(statisticsExtrator,
				"property statisticsExtrator of class " + getClass().getName()
				+ " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		statistics = STATISTICS_NONE;
		chartData = null;
		ticketCreatorsData = null;
		chartTitle = null;
		chartXtitle = null;
		chartYtitle = null;
		creationDepartments = null;
		ticketCreationsPeriodSpec = null;
		ticketCreationsHistoryIntervalSpec = null;
		ticketCreationsDistributionSpec = null;
		chartWidth = getChartDefaultWidth();
		chartHeight = getChartDefaultHeight();
		ticketCreationsSort = null;
		creationDepartments = new ArrayList<Department>();
		origins = new ArrayList<String>();
		ticketCreatorsMaxNumber = TICKET_CREATORS_MAX_NUMBERS[0];
		ticketsMaxNumber = TICKETS_MAX_NUMBERS[0];
		showMinMax = false;
		statusSort = null;
		ignoreArchivedTickets = false;
		ticketsData = null;
		spentTimeData = null;
	}

	/**
	 * @return true if the current user is allowed to access the view.
	 */
	@RequestCache
	public boolean isPageAuthorized() {
		if (getCurrentUser() == null) {
			return false;
		}
		if (getCurrentUser().getAdmin()) {
			return true;
		}
		if (getDomainService().isDepartmentManager(getCurrentUser())) {
			return true;
		}
		return false;
	}

	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			return null;
		}
		getSessionController().setShowShortMenu(false);
		computeStatistics();
		return "navigationStatistics";
	}

	/**
	 * Set the current statistics.
	 * @param statistics
	 */
	public void setStatistics(final Integer statistics) {
		this.statistics = statistics;
	}

	/**
	 * @return the statisticsItems
	 */
	@RequestCache
	public List<SelectItem> getStatisticsItems() {
		List<SelectItem> statisticsItems = new ArrayList<SelectItem>();
		statisticsItems.add(new SelectItem(
				STATISTICS_NONE,
				getString("STATISTICS.STATISTICS_FILTER.NONE")));
		statisticsItems.add(new SelectItem(
				STATISTICS_TICKET_CREATIONS_HISTORY,
				getString("STATISTICS.STATISTICS_FILTER.TICKET_CREATIONS_HISTORY")));
		statisticsItems.add(new SelectItem(
				STATISTICS_TICKET_CREATIONS_DISTRIBUTION,
				getString("STATISTICS.STATISTICS_FILTER.TICKET_CREATIONS_DISTRIBUTION")));
		statisticsItems.add(new SelectItem(
				STATISTICS_CREATORS,
				getString("STATISTICS.STATISTICS_FILTER.TICKET_CREATORS")));
		statisticsItems.add(new SelectItem(
				STATISTICS_CHARGE_HISTORY,
				getString("STATISTICS.STATISTICS_FILTER.CHARGE_HISTORY")));
		statisticsItems.add(new SelectItem(
				STATISTICS_CHARGE_DISTIBUTION,
				getString("STATISTICS.STATISTICS_FILTER.CHARGE_DISTRIBUTION")));
		statisticsItems.add(new SelectItem(
				STATISTICS_CHARGE_TICKETS,
				getString("STATISTICS.STATISTICS_FILTER.CHARGE_TICKETS")));
		statisticsItems.add(new SelectItem(
				STATISTICS_CLOSURE_HISTORY,
				getString("STATISTICS.STATISTICS_FILTER.CLOSURE_HISTORY")));
		statisticsItems.add(new SelectItem(
				STATISTICS_CLOSURE_DISTIBUTION,
				getString("STATISTICS.STATISTICS_FILTER.CLOSURE_DISTRIBUTION")));
		statisticsItems.add(new SelectItem(
				STATISTICS_CLOSURE_TICKETS,
				getString("STATISTICS.STATISTICS_FILTER.CLOSURE_TICKETS")));
		statisticsItems.add(new SelectItem(
				STATISTICS_STATUS,
				getString("STATISTICS.STATISTICS_FILTER.STATUS")));
		statisticsItems.add(new SelectItem(
				STATISTICS_SPENT_TIME,
				getString("STATISTICS.STATISTICS_FILTER.SPENT_TIME")));
		return statisticsItems;
	}

	/**
	 * @return the ticketCreatorsMaxNumberItems
	 */
	@RequestCache
	public List<SelectItem> getTicketCreatorsMaxNumberItems() {
		List<SelectItem> ticketCreatorsNumberItems = new ArrayList<SelectItem>();
		for (Integer value : TICKET_CREATORS_MAX_NUMBERS) {
			String itemString = getString(
					"STATISTICS.TICKET_CREATORS_MAX_NUMBER_FILTER.VALUE",
					value);
			ticketCreatorsNumberItems.add(
					new SelectItem(value, itemString));
		}
		return ticketCreatorsNumberItems;
	}

	/**
	 * @return the ticketsMaxNumberItems
	 */
	@RequestCache
	public List<SelectItem> getTicketsMaxNumberItems() {
		List<SelectItem> ticketsNumberItems = new ArrayList<SelectItem>();
		for (Integer value : TICKETS_MAX_NUMBERS) {
			String itemString = getString(
					"STATISTICS.TICKETS_MAX_NUMBER_FILTER.VALUE",
					value);
			ticketsNumberItems.add(
					new SelectItem(value, itemString));
		}
		return ticketsNumberItems;
	}

	/**
	 * @return the creationDepartmentSpecItems
	 */
	@RequestCache
	public List<SelectItem> getCreationDepartmentItems() {
		List<SelectItem> departmentItems = new ArrayList<SelectItem>();
		List<Department> departments;
		if (getCurrentUser().getAdmin()) {
			departments = getDomainService().getDepartments();
		} else {
			departments = getDomainService().getManagedDepartments(getCurrentUser());
		}
		for (Department dep : departments) {
			if (!dep.isVirtual()) {
				departmentItems.add(new SelectItem(dep, dep.getLabel()));
				List<Department> virtualDepartments =
					getDomainService().getVirtualDepartments(dep);
				if (!virtualDepartments.isEmpty()) {
					for (Department vdep : virtualDepartments) {
						departmentItems.add(new SelectItem(
								vdep, "- " + vdep.getLabel()));
					}
				}
			}
		}
		return departmentItems;
	}

	/**
	 * @return the creationDepartmentSpecItems
	 */
	@RequestCache
	public List<SelectItem> getCreationPeriodSpecItems() {
		List<SelectItem> creationPeriodSpecItems = new ArrayList<SelectItem>();
		if (!STATISTICS_NONE.equals(statistics)) {
			creationPeriodSpecItems.add(new SelectItem(
					"*", getString("STATISTICS.PERIOD_FILTER.ALL")));
			creationPeriodSpecItems.add(new SelectItem(
					"y", getString("STATISTICS.PERIOD_FILTER.YEAR")));
			if (!STATISTICS_SPENT_TIME.equals(statistics)) {
				creationPeriodSpecItems.add(new SelectItem(
						"m", getString("STATISTICS.PERIOD_FILTER.MONTH")));
				creationPeriodSpecItems.add(new SelectItem(
						"w", getString("STATISTICS.PERIOD_FILTER.WEEK")));
			}
		}
		return creationPeriodSpecItems;
	}

	/**
	 * @return the originItems
	 */
	@RequestCache
	public List<SelectItem> getOriginItems() {
		List<SelectItem> originItems = new ArrayList<SelectItem>();
		for (String origin : getDomainService().getOrigins()) {
			originItems.add(new SelectItem(origin, getString(originI18nKeyProvider.get(origin))));
		}
		return originItems;
	}

	/**
	 * @return the creationSortItems
	 */
	@RequestCache
	public List<SelectItem> getCreationSortItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("", getString("STATISTICS.SORT_FILTER.NONE")));
		if (origins.size() != 1) {
			items.add(new SelectItem("byOrigin", getString("STATISTICS.SORT_FILTER.BY_ORIGIN")));
		}
		if (creationDepartments.size() != 1) {
			items.add(new SelectItem("byDepartment", getString("STATISTICS.SORT_FILTER.BY_DEPARTMENT")));
		}
		return items;
	}

	/**
	 * @return the statusSortItems
	 */
	@RequestCache
	public List<SelectItem> getStatusSortItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("", getString("STATISTICS.SORT_FILTER.NONE")));
		if (creationDepartments.size() != 1) {
			items.add(new SelectItem("byDepartment", getString("STATISTICS.SORT_FILTER.BY_DEPARTMENT")));
		}
		return items;
	}

	/**
	 * @param ts
	 * @return the day
	 */
	protected int getDay(final Timestamp ts) {
		return StatisticsUtils.getDay(ts);
	}

	/**
	 * @param ts
	 * @return the name of the day of week
	 */
	protected String getDayOfWeekShortName(
			final Timestamp ts) {
		return StatisticsUtils.getDayOfWeekShortName(ts, getLocale());
	}

	/**
	 * @param dow
	 * @return the name of the day of week
	 */
	protected String getDayOfWeekShortName(
			final Integer dow) {
		return StatisticsUtils.getDayOfWeekShortName(dow, getLocale());
	}

	/**
	 * @param ts
	 * @return the name of the month
	 */
	protected String getMonthName(
			final Timestamp ts) {
		return StatisticsUtils.getMonthName(ts, getLocale());
	}

	/**
	 * @param ts
	 * @return the short name of the month
	 */
	protected String getMonthShortName(
			final Timestamp ts) {
		return StatisticsUtils.getMonthShortName(ts, getLocale());
	}

	/**
	 * @param ts
	 * @return the month
	 */
	protected int getMonth(final Timestamp ts) {
		return StatisticsUtils.getMonth(ts);
	}

	/**
	 * @param ts
	 * @return the year
	 */
	protected int getYear(final Timestamp ts) {
		return StatisticsUtils.getYear(ts);
	}

	/**
	 * Set the ticket creations history chart title.
	 * @param start
	 */
	protected void setTicketCreationChartXYtitle(
			final Timestamp start) {
		if (start != null) {
			chartXtitle = getString(
					"STATISTICS.CHART.TICKET_CREATIONS.XTITLE",
					String.valueOf(getDay(start)),
					getMonth(start) + 1,
					String.valueOf(getYear(start)));
			chartYtitle = getString(
					"STATISTICS.CHART.TICKET_CREATIONS.YTITLE");
		} else {
			chartXtitle = "";
			chartYtitle = "";
		}
	}

	/**
	 * @param date
	 * @param intervalMonth
	 * @param periodWeek
	 * @param periodMonth
	 * @param periodYear
	 * @return the Xlabel string that corresponds to a date.
	 */
	protected String getTicketCreationHistoryDateXlabel(
			final Timestamp date,
			final boolean intervalMonth,
			final boolean periodWeek,
			final boolean periodMonth,
			final boolean periodYear) {
		String xlabel;
		if (periodWeek) {
			xlabel = getString(
					"STATISTICS.CHART.XLABEL.DOW",
					getDayOfWeekShortName(date));
		} else if (periodMonth) {
			xlabel = getString(
					"STATISTICS.CHART.XLABEL.DAY_MONTH",
					String.valueOf(getDay(date)), getMonthShortName(date));
		} else if (periodYear) {
			xlabel = getString(
					"STATISTICS.CHART.XLABEL.MONTH",
					getMonthShortName(date));
		} else {
			if (intervalMonth) {
				xlabel = getString(
						"STATISTICS.CHART.XLABEL.MONTH_YEAR",
						getMonthShortName(date), String.valueOf(getYear(date)));
			} else {
				xlabel = getString(
						"STATISTICS.CHART.XLABEL.YEAR",
						String.valueOf(getYear(date)));
			}
		}
		return xlabel;
	}

	/**
	 * @return the selected creation departments.
	 */
	protected List<Department> getSelectedCreationDepartments() {
		List<Department> departments = creationDepartments;
		if (departments.isEmpty()) {
			if (getCurrentUser().getAdmin()) {
				departments = getDomainService().getDepartments();
			} else {
				departments = getDomainService().getManagedDepartments(getCurrentUser());
			}
		}
		return departments;
	}

	/**
	 * @return the selected creation origins.
	 */
	protected List<String> getSelectedCreationOrigins() {
		List<String> selectedOrigins = origins;
		if (selectedOrigins.isEmpty()) {
			selectedOrigins = getDomainService().getOrigins();
		}
		return selectedOrigins;
	}

	/**
	 * Fill the statistics for ticket creations.
	 * @param start
	 * @param now
	 * @param intervalDay
	 * @param intervalMonth
	 * @param periodWeek
	 * @param periodMonth
	 * @param periodYear
	 */
	protected void fillTicketCreationsHistoryStats(
			final Timestamp start,
			final Timestamp now,
			final boolean intervalDay,
			final boolean intervalMonth,
			final boolean periodWeek,
			final boolean periodMonth,
			final boolean periodYear) {
		List<Department> departments = getSelectedCreationDepartments();
		List<String> selectedOrigins = getSelectedCreationOrigins();
		if ("byDepartment".equals(ticketCreationsSort)) {
			Map<Timestamp, Map<Department, Integer>> stats;
			if (intervalDay) {
				stats = statisticsExtrator.getTicketCreationsByDayPerDepartment(
						start, now, departments, selectedOrigins);
			} else if (intervalMonth) {
				stats = statisticsExtrator.getTicketCreationsByMonthPerDepartment(
						start, now, departments, selectedOrigins);
			} else {
				stats = statisticsExtrator.getTicketCreationsByYearPerDepartment(
						start, now, departments, selectedOrigins);
			}
			for (Timestamp date : stats.keySet()) {
				String xlabel = getTicketCreationHistoryDateXlabel(
						date, intervalMonth, periodWeek, periodMonth, periodYear);
				Map<Department, Integer> dateMap = stats.get(date);
				for (Department department : dateMap.keySet()) {
					chartData.addValue(
							dateMap.get(department), department.getLabel(), xlabel);
				}
			}
		} else if ("byOrigin".equals(ticketCreationsSort)) {
			Map<Timestamp, Map<String, Integer>> stats;
			if (intervalDay) {
				stats = statisticsExtrator.getTicketCreationsByDayPerOrigin(
						start, now, departments, selectedOrigins);
			} else if (intervalMonth) {
				stats = statisticsExtrator.getTicketCreationsByMonthPerOrigin(
						start, now, departments, selectedOrigins);
			} else {
				stats = statisticsExtrator.getTicketCreationsByYearPerOrigin(
						start, now, departments, selectedOrigins);
			}
			for (Timestamp date : stats.keySet()) {
				String xlabel = getTicketCreationHistoryDateXlabel(
						date, intervalMonth, periodWeek, periodMonth, periodYear);
				Map<String, Integer> dateMap = stats.get(date);
				for (String origin : dateMap.keySet()) {
					chartData.addValue(
							dateMap.get(origin),
							getString(originI18nKeyProvider.get(origin)), xlabel);
				}
			}
		} else {
			Map<Timestamp, Integer> stats;
			if (intervalDay) {
				stats = statisticsExtrator.getTicketCreationsByDay(
						start, now, departments, selectedOrigins);
			} else if (intervalMonth) {
				stats = statisticsExtrator.getTicketCreationsByMonth(
						start, now, departments, selectedOrigins);
			} else {
				stats = statisticsExtrator.getTicketCreationsByYear(
						start, now, departments, selectedOrigins);
			}
			for (Timestamp date : stats.keySet()) {
				String xlabel = getTicketCreationHistoryDateXlabel(
						date, intervalMonth, periodWeek, periodMonth, periodYear);
				chartData.addValue(stats.get(date), "", xlabel);
			}
		}
	}

	/**
	 * Compute the ticket creations bar graph.
	 */
	protected void computeTicketCreationsHistory() {
		Timestamp start = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		boolean periodWeek = false;
		boolean periodMonth = false;
		boolean periodYear = false;
		@SuppressWarnings("unused")
		boolean periodAll = false;
		boolean intervalDay = false;
		boolean intervalMonth = false;
		@SuppressWarnings("unused")
		boolean intervalYear = false;
		if (PERIOD_WEEK.equals(ticketCreationsPeriodSpec)) {
			periodWeek = true;
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousWeekDate(now));
			intervalDay = true;
		} else if (PERIOD_MONTH.equals(ticketCreationsPeriodSpec)) {
			periodMonth = true;
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousMonthDate(now));
			intervalDay = true;
		} else if (PERIOD_YEAR.equals(ticketCreationsPeriodSpec)) {
			periodYear = true;
			start = StatisticsUtils.getMonthUpperRoundedDate(
							StatisticsUtils.getPreviousYearDate(now));
			intervalMonth = true;
		} else {
			periodAll = true;
			start = getDomainService().getOldestTicketDate();
			if (INTERVAL_YEAR.equals(ticketCreationsHistoryIntervalSpec)) {
				intervalYear = true;
			} else {
				intervalMonth = true;
			}
		}
		setTicketCreationChartXYtitle(start);
		chartData = new DefaultCategoryDataset();
		if (start != null) {
			fillTicketCreationsHistoryStats(
					start, now,
					intervalDay, intervalMonth,
					periodWeek, periodMonth, periodYear);
			String chartTitlePrefix = "STATISTICS.CHART.TICKET_CREATIONS.HISTORY.TITLE_";
			if (periodWeek) {
				chartTitle = getString(chartTitlePrefix + "WEEK");
			} else if (periodMonth) {
				chartTitle = getString(chartTitlePrefix + "MONTH");
			} else if (periodYear) {
				chartTitle = getString(chartTitlePrefix + "YEAR");
			} else {
				chartTitle = getString(
						chartTitlePrefix + "ALL",
						getMonthName(start), String.valueOf(getYear(start)));
			}
		}
	}

	/**
	 * @param value
	 * @param intervalDayOfWeek
	 * @param intervalHourOfDay
	 * @return the Xlabel string that corresponds to an integer.
	 */
	protected String getTicketCreationDistributionIntegerXlabel(
			final int value,
			final boolean intervalDayOfWeek,
			final boolean intervalHourOfDay) {
		String xlabel;
		if (intervalDayOfWeek) {
			xlabel = getString(
					"STATISTICS.CHART.XLABEL.DOW",
					getDayOfWeekShortName(value));
		} else if (intervalHourOfDay) {
			xlabel = getString(
					"STATISTICS.CHART.XLABEL.HOD",
					String.valueOf(value));
		} else {
			xlabel = getString(
					"STATISTICS.CHART.XLABEL.HOW",
					getDayOfWeekShortName(
							StatisticsUtils.getDayFromHourOfWeek(value)),
							String.valueOf(StatisticsUtils.getHourFromHourOfWeek(value)));
		}
		return xlabel;
	}

	/**
	 * Fill the ticket creations distribution stats.
	 * @param start
	 * @param now
	 * @param intervalDayOfWeek
	 * @param intervalHourOfDay
	 */
	protected void fillTicketCreationsDistributionStats(
			final Timestamp start,
			final Timestamp now,
			final boolean intervalDayOfWeek,
			final boolean intervalHourOfDay) {
		List<Department> departments = getSelectedCreationDepartments();
		List<String> selectedOrigins = getSelectedCreationOrigins();
		if ("byDepartment".equals(ticketCreationsSort)) {
			Map<Integer, Map<Department, Integer>> stats;
			if (intervalDayOfWeek) {
				stats = statisticsExtrator.getTicketCreationsByDayOfWeekPerDepartment(
					start, now, departments, selectedOrigins);
			} else if (intervalHourOfDay) {
				stats = statisticsExtrator.getTicketCreationsByHourOfDayPerDepartment(
						start, now, departments, selectedOrigins);
			} else {
				stats = statisticsExtrator.getTicketCreationsByHourOfWeekPerDepartment(
						start, now, departments, selectedOrigins);
			}
			for (Integer integer : stats.keySet()) {
				String xlabel = getTicketCreationDistributionIntegerXlabel(
						integer, intervalDayOfWeek, intervalHourOfDay);
				Map<Department, Integer> dateMap = stats.get(integer);
				for (Department department : dateMap.keySet()) {
					chartData.addValue(
							dateMap.get(department), department.getLabel(), xlabel);
				}
			}
		} else if ("byOrigin".equals(ticketCreationsSort)) {
			Map<Integer, Map<String, Integer>> stats;
			if (intervalDayOfWeek) {
				stats = statisticsExtrator.getTicketCreationsByDayOfWeekPerOrigin(
						start, now, departments, selectedOrigins);
			} else if (intervalHourOfDay) {
				stats = statisticsExtrator.getTicketCreationsByHourOfDayPerOrigin(
						start, now, departments, selectedOrigins);
			} else {
				stats = statisticsExtrator.getTicketCreationsByHourOfWeekPerOrigin(
						start, now, departments, selectedOrigins);
			}
			for (Integer integer : stats.keySet()) {
				String xlabel = getTicketCreationDistributionIntegerXlabel(
						integer, intervalDayOfWeek, intervalHourOfDay);
				Map<String, Integer> dateMap = stats.get(integer);
				for (String origin : dateMap.keySet()) {
					chartData.addValue(
							dateMap.get(origin),
							getString(originI18nKeyProvider.get(origin)),
							xlabel);
				}
			}
		} else {
			Map<Integer, Integer> stats;
			if (intervalDayOfWeek) {
				stats = statisticsExtrator.getTicketCreationsByDayOfWeek(
						start, now, departments, selectedOrigins);
			} else if (intervalHourOfDay) {
				stats = statisticsExtrator.getTicketCreationsByHourOfDay(
						start, now, departments, selectedOrigins);
			} else {
				stats = statisticsExtrator.getTicketCreationsByHourOfWeek(
						start, now, departments, selectedOrigins);
			}
			for (Integer integer : stats.keySet()) {
				String xlabel = getTicketCreationDistributionIntegerXlabel(
						integer, intervalDayOfWeek, intervalHourOfDay);
				chartData.addValue(stats.get(integer), "", xlabel);
			}
		}
	}

	/**
	 * Compute the ticket creations bar graph.
	 */
	protected void computeTicketCreationsDistribution() {
		Timestamp start = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		boolean periodWeek = false;
		boolean periodMonth = false;
		boolean periodYear = false;
		@SuppressWarnings("unused")
		boolean periodAll = false;
		boolean intervalDayOfWeek = false;
		boolean intervalHourOfDay = false;
		@SuppressWarnings("unused")
		boolean intervalHourOfWeek = false;
		if (PERIOD_WEEK.equals(ticketCreationsPeriodSpec)) {
			periodWeek = true;
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousWeekDate(now));
		} else if (PERIOD_MONTH.equals(ticketCreationsPeriodSpec)) {
			periodMonth = true;
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousMonthDate(now));
		} else if (PERIOD_YEAR.equals(ticketCreationsPeriodSpec)) {
			periodYear = true;
			start = StatisticsUtils.getMonthUpperRoundedDate(
							StatisticsUtils.getPreviousYearDate(now));
		} else {
			periodAll = true;
			start = getDomainService().getOldestTicketDate();
		}
		if (INTERVAL_DOW.equals(ticketCreationsDistributionSpec)) {
			intervalDayOfWeek = true;
		} else if (INTERVAL_HOD.equals(ticketCreationsDistributionSpec)) {
			intervalHourOfDay = true;
		} else {
			intervalHourOfWeek = true;
		}
		chartData = new DefaultCategoryDataset();
		setTicketCreationChartXYtitle(start);
		if (start != null) {
			fillTicketCreationsDistributionStats(start, now, intervalDayOfWeek, intervalHourOfDay);
			String chartTitlePrefix = "STATISTICS.CHART.TICKET_CREATIONS.DISTRIBUTION.";
			if (intervalDayOfWeek) {
				chartTitlePrefix += "DOW";
			} else if (intervalHourOfDay) {
				chartTitlePrefix += "HOD";
			} else {
				chartTitlePrefix += "HOW";
			}
			chartTitlePrefix += ".TITLE_";
			if (periodWeek) {
				chartTitle = getString(chartTitlePrefix + "WEEK");
			} else if (periodMonth) {
				chartTitle = getString(chartTitlePrefix + "MONTH");
			} else if (periodYear) {
				chartTitle = getString(chartTitlePrefix + "YEAR");
			} else {
				chartTitle = getString(
						chartTitlePrefix + "ALL",
						getMonthName(start), String.valueOf(getYear(start)));
			}
		}
	}

	/**
	 * Compute the ticket creators bar graph.
	 */
	protected void computeTicketCreators() {
		Timestamp start = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		boolean periodWeek = false;
		boolean periodMonth = false;
		boolean periodYear = false;
		@SuppressWarnings("unused")
		boolean periodAll = false;
		if (PERIOD_WEEK.equals(ticketCreationsPeriodSpec)) {
			periodWeek = true;
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousWeekDate(now));
		} else if (PERIOD_MONTH.equals(ticketCreationsPeriodSpec)) {
			periodMonth = true;
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousMonthDate(now));
		} else if (PERIOD_YEAR.equals(ticketCreationsPeriodSpec)) {
			periodYear = true;
			start = StatisticsUtils.getMonthUpperRoundedDate(
							StatisticsUtils.getPreviousYearDate(now));
		} else {
			periodAll = true;
			start = getDomainService().getOldestTicketDate();
		}
		ticketCreatorsData = statisticsExtrator.getUserTicketCreations(
				start, now, creationDepartments, ticketCreatorsMaxNumber);
		String chartTitlePrefix = "STATISTICS.CHART.TICKET_CREATORS.TITLE_";
		if (periodWeek) {
			chartTitle = getString(chartTitlePrefix + "WEEK", ticketCreatorsMaxNumber);
		} else if (periodMonth) {
			chartTitle = getString(chartTitlePrefix + "MONTH", ticketCreatorsMaxNumber);
		} else if (periodYear) {
			chartTitle = getString(chartTitlePrefix + "YEAR", ticketCreatorsMaxNumber);
		} else {
			chartTitle = getString(
					chartTitlePrefix + "ALL", ticketCreatorsMaxNumber,
					getMonthName(start), String.valueOf(getYear(start)));
		}
	}

	/**
	 * Set the charge time chart title.
	 * @param charge
	 * @param start
	 */
	protected void setChargeOrClosureChartXYtitle(
			final boolean charge,
			final Timestamp start) {
		String prefix;
		if (charge) {
			prefix = "STATISTICS.CHART.CHARGE.";
		} else {
			prefix = "STATISTICS.CHART.CLOSURE.";
		}
		if (start != null) {
			chartXtitle = getString(
					prefix + "XTITLE",
					String.valueOf(getDay(start)),
					getMonth(start) + 1,
					String.valueOf(getYear(start)));
			chartYtitle = getString(prefix + "YTITLE");
		} else {
			chartXtitle = "";
			chartYtitle = "";
		}
	}

	/**
	 * Fill the statistics for charge/closure time history.
	 * @param charge
	 * @param start
	 * @param now
	 * @param intervalDay
	 * @param intervalMonth
	 * @param periodWeek
	 * @param periodMonth
	 * @param periodYear
	 */
	protected void fillChargeOrClosureHistoryStats(
			final boolean charge,
			final Timestamp start,
			final Timestamp now,
			final boolean intervalDay,
			final boolean intervalMonth,
			final boolean periodWeek,
			final boolean periodMonth,
			final boolean periodYear) {
		List<Department> departments = getSelectedCreationDepartments();
		Map<Timestamp, MinAvgMaxNumberStatistic> stats;
		if (intervalDay) {
			stats = statisticsExtrator.getChargeOrClosureTimeByDay(
					charge, start, now, departments);
		} else if (intervalMonth) {
			stats = statisticsExtrator.getChargeOrClosureTimeByMonth(
					charge, start, now, departments);
		} else {
			stats = statisticsExtrator.getChargeOrClosureTimeByYear(
					charge, start, now, departments);
		}
		chartValues = new LinkedHashMap<String, MinAvgMaxNumberStatistic>();
		for (Timestamp date : stats.keySet()) {
			String xlabel = getTicketCreationHistoryDateXlabel(
					date, intervalMonth, periodWeek, periodMonth, periodYear);
			MinAvgMaxNumberStatistic data = stats.get(date);
			chartData.addValue(data.getAvg() / SECONDS_PER_HOUR, getString("STATISTICS.AVG"), xlabel);
			if (showMinMax) {
				chartData.addValue(
						data.getMin() / SECONDS_PER_HOUR, getString("STATISTICS.MIN"), xlabel);
				chartData.addValue(
						data.getMax() / SECONDS_PER_HOUR, getString("STATISTICS.MAX"), xlabel);
			}
			chartValues.put(xlabel, data);
		}
	}

	/**
	 * Compute the charge/closure time history bar graph.
	 * @param charge
	 */
	protected void computeChargeOrClosureHistory(
			final boolean charge) {
		Timestamp start = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		boolean periodWeek = false;
		boolean periodMonth = false;
		boolean periodYear = false;
		@SuppressWarnings("unused")
		boolean periodAll = false;
		boolean intervalDay = false;
		boolean intervalMonth = false;
		@SuppressWarnings("unused")
		boolean intervalYear = false;
		if (PERIOD_WEEK.equals(ticketCreationsPeriodSpec)) {
			periodWeek = true;
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousWeekDate(now));
			intervalDay = true;
		} else if (PERIOD_MONTH.equals(ticketCreationsPeriodSpec)) {
			periodMonth = true;
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousMonthDate(now));
			intervalDay = true;
		} else if (PERIOD_YEAR.equals(ticketCreationsPeriodSpec)) {
			periodYear = true;
			start = StatisticsUtils.getMonthUpperRoundedDate(
							StatisticsUtils.getPreviousYearDate(now));
			intervalMonth = true;
		} else {
			periodAll = true;
			start = getDomainService().getOldestTicketDate();
			if (INTERVAL_YEAR.equals(ticketCreationsHistoryIntervalSpec)) {
				intervalYear = true;
			} else {
				intervalMonth = true;
			}
		}
		setChargeOrClosureChartXYtitle(charge, start);
		chartData = new DefaultCategoryDataset();
		if (start != null) {
			fillChargeOrClosureHistoryStats(
					charge, start, now,
					intervalDay, intervalMonth,
					periodWeek, periodMonth, periodYear);
			String chartTitlePrefix;
			if (charge) {
				chartTitlePrefix = "STATISTICS.CHART.CHARGE.HISTORY.TITLE_";
			} else {
				chartTitlePrefix = "STATISTICS.CHART.CLOSURE.HISTORY.TITLE_";
			}
			if (periodWeek) {
				chartTitle = getString(chartTitlePrefix + "WEEK");
			} else if (periodMonth) {
				chartTitle = getString(chartTitlePrefix + "MONTH");
			} else if (periodYear) {
				chartTitle = getString(chartTitlePrefix + "YEAR");
			} else {
				chartTitle = getString(
						chartTitlePrefix + "ALL",
						getMonthName(start), String.valueOf(getYear(start)));
			}
		}
	}

	/**
	 * Fill the charge/closure time distribution stats.
	 * @param charge
	 * @param start
	 * @param now
	 * @param intervalDayOfWeek
	 * @param intervalHourOfDay
	 */
	protected void fillChargeOrClosureDistributionStats(
			final boolean charge,
			final Timestamp start,
			final Timestamp now,
			final boolean intervalDayOfWeek,
			final boolean intervalHourOfDay) {
		List<Department> departments = getSelectedCreationDepartments();
		Map<Integer, MinAvgMaxNumberStatistic> stats;
		if (intervalDayOfWeek) {
			stats = statisticsExtrator.getChargeOrClosureTimeByDayOfWeek(
					charge, start, now, departments);
		} else if (intervalHourOfDay) {
			stats = statisticsExtrator.getChargeOrClosureTimeByHourOfDay(
					charge, start, now, departments);
		} else {
			stats = statisticsExtrator.getChargeOrClosureTimeByHourOfWeek(
					charge, start, now, departments);
		}
		chartValues = new LinkedHashMap<String, MinAvgMaxNumberStatistic>();
		for (Integer integer : stats.keySet()) {
			String xlabel = getTicketCreationDistributionIntegerXlabel(
					integer, intervalDayOfWeek, intervalHourOfDay);
			MinAvgMaxNumberStatistic data = stats.get(integer);
			chartData.addValue(data.getAvg() / SECONDS_PER_HOUR, getString("STATISTICS.AVG"), xlabel);
			if (showMinMax) {
				chartData.addValue(
						data.getMin() / SECONDS_PER_HOUR, getString("STATISTICS.MIN"), xlabel);
				chartData.addValue(
						data.getMax() / SECONDS_PER_HOUR, getString("STATISTICS.MAX"), xlabel);
			}
			chartValues.put(xlabel, data);
		}
	}

	/**
	 * Compute the charge/closure time distribution bar graph.
	 * @param charge
	 */
	protected void computeChargeOrClosureDistribution(
			final boolean charge) {
		Timestamp start = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		boolean periodWeek = false;
		boolean periodMonth = false;
		boolean periodYear = false;
		@SuppressWarnings("unused")
		boolean periodAll = false;
		boolean intervalDayOfWeek = false;
		boolean intervalHourOfDay = false;
		@SuppressWarnings("unused")
		boolean intervalHourOfWeek = false;
		if (PERIOD_WEEK.equals(ticketCreationsPeriodSpec)) {
			periodWeek = true;
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousWeekDate(now));
		} else if (PERIOD_MONTH.equals(ticketCreationsPeriodSpec)) {
			periodMonth = true;
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousMonthDate(now));
		} else if (PERIOD_YEAR.equals(ticketCreationsPeriodSpec)) {
			periodYear = true;
			start = StatisticsUtils.getMonthUpperRoundedDate(
							StatisticsUtils.getPreviousYearDate(now));
		} else {
			periodAll = true;
			start = getDomainService().getOldestTicketDate();
		}
		if (INTERVAL_DOW.equals(ticketCreationsDistributionSpec)) {
			intervalDayOfWeek = true;
		} else if (INTERVAL_HOD.equals(ticketCreationsDistributionSpec)) {
			intervalHourOfDay = true;
		} else {
			intervalHourOfWeek = true;
		}
		chartData = new DefaultCategoryDataset();
		setChargeOrClosureChartXYtitle(charge, start);
		if (start != null) {
			fillChargeOrClosureDistributionStats(charge, start, now, intervalDayOfWeek, intervalHourOfDay);
			String chartTitlePrefix;
			if (charge) {
				chartTitlePrefix = "STATISTICS.CHART.CHARGE.DISTRIBUTION.";
			} else {
				chartTitlePrefix = "STATISTICS.CHART.CLOSURE.DISTRIBUTION.";
			}
			if (intervalDayOfWeek) {
				chartTitlePrefix += "DOW";
			} else if (intervalHourOfDay) {
				chartTitlePrefix += "HOD";
			} else {
				chartTitlePrefix += "HOW";
			}
			chartTitlePrefix += ".TITLE_";
			if (periodWeek) {
				chartTitle = getString(chartTitlePrefix + "WEEK");
			} else if (periodMonth) {
				chartTitle = getString(chartTitlePrefix + "MONTH");
			} else if (periodYear) {
				chartTitle = getString(chartTitlePrefix + "YEAR");
			} else {
				chartTitle = getString(
						chartTitlePrefix + "ALL",
						getMonthName(start), String.valueOf(getYear(start)));
			}
		}
	}

	/**
	 * Compute the charge/closure time tickets.
	 * @param charge
	 */
	protected void computeChargeOrClosureTickets(
			final boolean charge) {
		List<Department> departments = getSelectedCreationDepartments();
		Timestamp start = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String chartTitlePrefix;
		if (charge) {
			chartTitlePrefix = "STATISTICS.CHART.CHARGE.TICKETS.TITLE_";
		} else {
			chartTitlePrefix = "STATISTICS.CHART.CLOSURE.TICKETS.TITLE_";
		}
		if (PERIOD_WEEK.equals(ticketCreationsPeriodSpec)) {
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousWeekDate(now));
			chartTitle = getString(chartTitlePrefix + "WEEK", ticketsMaxNumber);
		} else if (PERIOD_MONTH.equals(ticketCreationsPeriodSpec)) {
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousMonthDate(now));
			chartTitle = getString(chartTitlePrefix + "MONTH", ticketsMaxNumber);
		} else if (PERIOD_YEAR.equals(ticketCreationsPeriodSpec)) {
			start = StatisticsUtils.getMonthUpperRoundedDate(
							StatisticsUtils.getPreviousYearDate(now));
			chartTitle = getString(chartTitlePrefix + "YEAR", ticketsMaxNumber);
		} else {
			start = getDomainService().getOldestTicketDate();
			chartTitle = getString(
					chartTitlePrefix + "ALL", ticketsMaxNumber,
					getMonthName(start), String.valueOf(getYear(start)));
		}
		if (start != null) {
			boolean hideChargedOrClosed;
			if (charge) {
				hideChargedOrClosed = hideCharged;
			} else {
				hideChargedOrClosed = hideClosed;
			}
			ticketsData = statisticsExtrator.getTicketsWithLongChargeOrClosureTime(
					charge, start, now, departments, ticketsMaxNumber, hideChargedOrClosed);
			if (!ticketsData.isEmpty()) {
				User user = getCurrentUser();
				List<Department> visibleDepartments =
					getDomainService().getTicketViewDepartments(
						user, getClient());
				for (StatisticsTicketEntry ticketEntry : ticketsData) {
					boolean canView;
					if (ticketEntry.isArchived()) {
						canView = getDomainService().userCanViewArchivedTicket(
								user, ticketEntry.getArchivedTicket(),
								visibleDepartments);
					} else {
						canView = getDomainService().userCanViewTicket(
								user, ticketEntry.getTicket(), visibleDepartments);
					}
					ticketEntry.setCanView(canView);
				}
			}
		} else {
			ticketsData = new ArrayList<StatisticsTicketEntry>();
		}
	}

	/**
	 * @param value
	 * @param status
	 * @param label
	 */
	protected void addStatusChartValue(
			final Integer value,
			final String status,
			final String label) {
		chartData.addValue(
				value,
				getString(TicketStatusI18nKeyProvider.getI18nKey(status)),
				label);
	}

	/**
	 * @param label
	 * @param sss
	 */
	protected void addStatusChartData(
			final String label,
			final StatusStatisticSet sss) {
		if (!ignoreArchivedTickets) {
			addStatusChartValue(sss.getArchivedNumber(), TicketStatus.ARCHIVED, label);
		}
		addStatusChartValue(sss.getClosedNumber(), TicketStatus.CLOSED, label);
		addStatusChartValue(sss.getFreeNumber(), TicketStatus.FREE, label);
		addStatusChartValue(sss.getIncompleteNumber(), TicketStatus.INCOMPLETE, label);
		addStatusChartValue(sss.getInProgressNumber(), TicketStatus.INPROGRESS, label);
		addStatusChartValue(sss.getPostponedNumber(), TicketStatus.POSTPONED, label);
	}

	/**
	 * @param status
	 * @param value
	 */
	protected void addStatusPieValue(
			final String status,
			final Integer value) {
		pieData.setValue(
				getString(
						"STATISTICS.CHART.STATUS.PIE_LABEL",
						getString(TicketStatusI18nKeyProvider.getI18nKey(status)),
						value),
				value);
	}

	/**
	 * @param sss
	 */
	protected void addStatusPieData(
			final StatusStatisticSet sss) {
		if (!ignoreArchivedTickets) {
			addStatusPieValue(TicketStatus.ARCHIVED, sss.getArchivedNumber());
		}
		addStatusPieValue(TicketStatus.CLOSED, sss.getClosedNumber());
		addStatusPieValue(TicketStatus.FREE, sss.getFreeNumber());
		addStatusPieValue(TicketStatus.INCOMPLETE, sss.getIncompleteNumber());
		addStatusPieValue(TicketStatus.INPROGRESS, sss.getInProgressNumber());
		addStatusPieValue(TicketStatus.POSTPONED, sss.getPostponedNumber());
	}

	/**
	 * Compute the status statistics.
	 */
	protected void computeStatus() {
		List<Department> departments = getSelectedCreationDepartments();
		Timestamp start = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String chartTitlePrefix = "STATISTICS.CHART.STATUS.TITLE_";
		if (PERIOD_WEEK.equals(ticketCreationsPeriodSpec)) {
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousWeekDate(now));
			chartTitle = getString(chartTitlePrefix + "WEEK");
		} else if (PERIOD_MONTH.equals(ticketCreationsPeriodSpec)) {
			start = StatisticsUtils.getDayUpperRoundedDate(
					StatisticsUtils.getPreviousMonthDate(now));
			chartTitle = getString(chartTitlePrefix + "MONTH");
		} else if (PERIOD_YEAR.equals(ticketCreationsPeriodSpec)) {
			start = StatisticsUtils.getMonthUpperRoundedDate(
							StatisticsUtils.getPreviousYearDate(now));
			chartTitle = getString(chartTitlePrefix + "YEAR");
		} else {
			start = getDomainService().getOldestTicketDate();
			chartTitle = getString(
					chartTitlePrefix + "ALL",
					getMonthName(start), String.valueOf(getYear(start)));
		}
		if (start != null) {
			if ("byDepartment".equals(statusSort)) {
				chartData = new DefaultCategoryDataset();
				Map<Long, StatusStatisticSet> stats;
				stats = statisticsExtrator.getStatusStatisticsPerDepartment(
						start, now, departments, ignoreArchivedTickets);
				for (Long departmentId : stats.keySet()) {
					Department department = getDomainService().getDepartment(departmentId);
					addStatusChartData(department.getLabel(), stats.get(departmentId));
				}
			} else {
				pieData = new DefaultPieDataset();
				StatusStatisticSet sss = statisticsExtrator.getStatusStatistics(
						start, now, departments, ignoreArchivedTickets);
				addStatusPieData(sss);
			}
			String prefix = "STATISTICS.CHART.STATUS.";
			chartXtitle = getString(
					prefix + "XTITLE",
					String.valueOf(getDay(start)),
					getMonth(start) + 1,
					String.valueOf(getYear(start)));
			chartYtitle = getString(prefix + "YTITLE");
		} else {
			chartXtitle = "";
			chartYtitle = "";
		}
	}

	/**
	 * Compute the spent time.
	 */
	protected void computeSpentTime() {
		List<Department> departments = getSelectedCreationDepartments();
		Timestamp start = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String chartTitlePrefix = "STATISTICS.SPENT_TIME.TITLE_";
		if (PERIOD_YEAR.equals(ticketCreationsPeriodSpec)) {
			start = StatisticsUtils.getMonthUpperRoundedDate(
							StatisticsUtils.getPreviousYearDate(now));
			chartTitle = getString(chartTitlePrefix + "YEAR");
		} else {
			start = getDomainService().getOldestTicketDate();
			chartTitle = getString(
					chartTitlePrefix + "ALL",
					getMonthName(start), String.valueOf(getYear(start)));
		}
		spentTimeData = new ArrayList<SpentTimeStatisticsEntry>();
		if (start != null) {
			long oldFinalDepartmentId = 0;
			Department finalDepartment = null;
			int subNumber = 0;
			long subSpentTime = 0;
//			int totalNumber = 0;
//			long totalSpentTime = 0;
			for (SpentTimeStatistic stat : statisticsExtrator.getSpentTimeStatistics(
					start, now, departments)) {
				long finalDepartmentId = stat.getFinalDepartmentId();
				if (finalDepartmentId != oldFinalDepartmentId) {
					if (finalDepartment != null) {
						spentTimeData.add(new SpentTimeStatisticsEntry(
								null, finalDepartment,
								subNumber, subSpentTime, true));
					}
					finalDepartment = getDomainService().getDepartment(finalDepartmentId);
//					totalNumber += subNumber;
//					totalSpentTime += subSpentTime;
					subNumber = 0;
					subSpentTime = 0;
				}
				Department creationDepartment = null;
				if (stat.getCreationDepartmentId() != null) {
					creationDepartment = getDomainService().getDepartment(
							stat.getCreationDepartmentId());
				}
				int number = stat.getNumber();
				long spentTime;
				if (finalDepartment != null && finalDepartment.isSpentTimeNeeded()) {
					spentTime = stat.getSpentTime();
				} else {
					spentTime = 0;
				}
				spentTimeData.add(new SpentTimeStatisticsEntry(
						creationDepartment, null,
						number, spentTime, false));
				subNumber += number;
				subSpentTime += spentTime;
				oldFinalDepartmentId = finalDepartmentId;
			}
			if (finalDepartment != null) {
				spentTimeData.add(new SpentTimeStatisticsEntry(
						null, finalDepartment,
						subNumber, subSpentTime, true));
//				totalNumber += subNumber;
//				totalSpentTime += subSpentTime;
			}
//			spentTimeData.add(new SpentTimeStatisticsEntry(
//					null, null,
//					totalNumber, totalSpentTime));
			Collections.reverse(spentTimeData);
		}
	}

	/**
	 * Compute the current statistics.
	 */
	public void computeStatistics() {
		if (STATISTICS_TICKET_CREATIONS_HISTORY.equals(statistics)) {
			computeTicketCreationsHistory();
		} else if (STATISTICS_TICKET_CREATIONS_DISTRIBUTION.equals(statistics)) {
			computeTicketCreationsDistribution();
		} else if (STATISTICS_CREATORS.equals(statistics)) {
			computeTicketCreators();
		} else if (STATISTICS_CHARGE_HISTORY.equals(statistics)) {
			computeChargeOrClosureHistory(true);
		} else if (STATISTICS_CHARGE_DISTIBUTION.equals(statistics)) {
			computeChargeOrClosureDistribution(true);
		} else if (STATISTICS_CHARGE_TICKETS.equals(statistics)) {
			computeChargeOrClosureTickets(true);
		} else if (STATISTICS_CLOSURE_HISTORY.equals(statistics)) {
			computeChargeOrClosureHistory(false);
		} else if (STATISTICS_CLOSURE_DISTIBUTION.equals(statistics)) {
			computeChargeOrClosureDistribution(false);
		} else if (STATISTICS_CLOSURE_TICKETS.equals(statistics)) {
			computeChargeOrClosureTickets(false);
		} else if (STATISTICS_STATUS.equals(statistics)) {
			computeStatus();
		} else if (STATISTICS_SPENT_TIME.equals(statistics)) {
			computeSpentTime();
		}
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getUrlBuilder().getStatisticsUrl(AuthUtils.APPLICATION);
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getUrlBuilder().getStatisticsUrl(AuthUtils.CAS);
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getUrlBuilder().getStatisticsUrl(AuthUtils.SHIBBOLETH);
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getUrlBuilder().getStatisticsUrl(AuthUtils.SPECIFIC);
	}

	/**
	 * @return the chart width.
	 */
	public int getChartWidth() {
		return chartWidth;
	}

	/**
	 * Increase the chart width.
	 */
	public void incChartWidth() {
		chartWidth = Math.min(
				chartWidth + getWidthStep(), getChartMaxWidth());
	}

	/**
	 * @return true if the user can increase the chart width.
	 */
	public boolean isCanIncChartWidth() {
		return chartWidth < getChartMaxWidth();
	}

	/**
	 * Decrease the chart width.
	 */
	public void decChartWidth() {
		chartWidth = Math.max(
				chartWidth - getWidthStep(), getChartMinWidth());
	}

	/**
	 * @return true if the user can increase the chart width.
	 */
	public boolean isCanDecChartWidth() {
		return chartWidth > getChartMinWidth();
	}

	/**
	 * @return the chart height.
	 */
	public int getChartHeight() {
		return chartHeight;
	}

	/**
	 * Increase the chart height.
	 */
	public void incChartHeight() {
		chartHeight = Math.min(
				chartHeight + getHeigthStep(), getChartMaxHeight());
	}

	/**
	 * @return true if the user can increase the chart height.
	 */
	public boolean isCanIncChartHeight() {
		return chartHeight < getChartMaxHeight();
	}

	/**
	 * Decrease the chart height.
	 */
	public void decChartHeight() {
		chartHeight = Math.max(
				chartHeight - getHeigthStep(), getChartMinHeight());
	}

	/**
	 * @return true if the user can decrease the chart height.
	 */
	public boolean isCanDecChartHeight() {
		return chartHeight > getChartMinHeight();
	}

	/**
	 * Set the default chart size.
	 */
	public void setDefaultChartSize() {
		chartHeight = getChartDefaultHeight();
		chartWidth = getChartDefaultWidth();
	}

	/**
	 * @return the chartData
	 */
	public DefaultCategoryDataset getChartData() {
		return chartData;
	}

	/**
	 * @return true to print the legend of the bar chart.
	 */
	@RequestCache
	public boolean isChartLegend() {
		if (STATISTICS_STATUS.equals(statistics)) {
			return true;
		}
		if (STATISTICS_CHARGE_HISTORY.equals(statistics)
				|| STATISTICS_CHARGE_DISTIBUTION.equals(statistics)) {
			return showMinMax;
		}
		return getTicketCreationsSort() != null;
	}

	/**
	 * @return the style of the bar chart.
	 */
	@RequestCache
	public String getChartType() {
		if (STATISTICS_STATUS.equals(statistics) && !"byDepartment".equals(statusSort)) {
			return "pie";
		}
		if (STATISTICS_CHARGE_HISTORY.equals(statistics)
				|| STATISTICS_CHARGE_DISTIBUTION.equals(statistics)) {
			return "line";
		}
		return "stackedbar";
	}

	/**
	 * @return the orientation of the bar chart.
	 */
	@RequestCache
	public String getChartOrientation() {
		if (STATISTICS_STATUS.equals(statistics)) {
			return "horizontal";
		}
		return "vertical";
	}

	/**
	 * @param chartData the chartData to set
	 */
	protected void setChartData(final DefaultCategoryDataset chartData) {
		this.chartData = chartData;
	}

	/**
	 * @return the widthStep
	 */
	public static int getWidthStep() {
		return WIDTH_STEP;
	}

	/**
	 * @return the heightStep
	 */
	public static int getHeigthStep() {
		return HEIGHT_STEP;
	}

	/**
	 * @return the chartDefaultHeight
	 */
	public static int getChartDefaultHeight() {
		return BAR_CHART_DEFAULT_HEIGHT;
	}

	/**
	 * @return the chartMinHeight
	 */
	public static int getChartMinHeight() {
		return BAR_CHART_MIN_HEIGHT;
	}

	/**
	 * @return the chartMaxHeight
	 */
	public static int getChartMaxHeight() {
		return BAR_CHART_MAX_HEIGHT;
	}

	/**
	 * @return the chartDefaultWidth
	 */
	public static int getChartDefaultWidth() {
		return BAR_CHART_DEFAULT_WIDTH;
	}

	/**
	 * @return the chartMinWidth
	 */
	public static int getChartMinWidth() {
		return BAR_CHART_MIN_WIDTH;
	}

	/**
	 * @return the chartMaxWidth
	 */
	public static int getChartMaxWidth() {
		return BAR_CHART_MAX_WIDTH;
	}

	/**
	 * @return the chartTitle
	 */
	public String getChartTitle() {
		return chartTitle;
	}

	/**
	 * @param chartTitle the chartTitle to set
	 */
	protected void setChartTitle(final String chartTitle) {
		this.chartTitle = chartTitle;
	}

	/**
	 * @return the chartXtitle
	 */
	public String getChartXtitle() {
		return chartXtitle;
	}

	/**
	 * @param chartXtitle the chartXtitle to set
	 */
	protected void setChartXtitle(final String chartXtitle) {
		this.chartXtitle = chartXtitle;
	}

	/**
	 * @return the chartYtitle
	 */
	public String getChartYtitle() {
		return chartYtitle;
	}

	/**
	 * @param chartYtitle the chartYtitle to set
	 */
	protected void setChartYtitle(final String chartYtitle) {
		this.chartYtitle = chartYtitle;
	}

	/**
	 * @return the ticketCreationsPeriodSpec
	 */
	public String getTicketCreationsPeriodSpec() {
		return ticketCreationsPeriodSpec;
	}

	/**
	 * @return the statistics
	 */
	public Integer getStatistics() {
		return statistics;
	}

	/**
	 * @param chartWidth the chartWidth to set
	 */
	public void setChartWidth(final int chartWidth) {
		this.chartWidth = chartWidth;
	}

	/**
	 * @param chartHeight the chartHeight to set
	 */
	public void setChartHeight(final int chartHeight) {
		this.chartHeight = chartHeight;
	}

	/**
	 * @return the ticketCreationsHistoryIntervalSpec
	 */
	public String getTicketCreationsHistoryIntervalSpec() {
		return ticketCreationsHistoryIntervalSpec;
	}

	/**
	 * @param ticketCreationsHistoryIntervalSpec the ticketCreationsHistoryIntervalSpec to set
	 */
	public void setTicketCreationsHistoryIntervalSpec(
			final String ticketCreationsHistoryIntervalSpec) {
		this.ticketCreationsHistoryIntervalSpec = ticketCreationsHistoryIntervalSpec;
	}

	/**
	 * @return the ticketCreationsDistributionSpec
	 */
	public String getTicketCreationsDistributionSpec() {
		return ticketCreationsDistributionSpec;
	}

	/**
	 * @param ticketCreationsDistributionSpec the ticketCreationsDistributionSpec to set
	 */
	public void setTicketCreationsDistributionSpec(
			final String ticketCreationsDistributionSpec) {
		this.ticketCreationsDistributionSpec = ticketCreationsDistributionSpec;
	}

	/**
	 * @param ticketCreationsPeriodSpec the ticketCreationsPeriodSpec to set
	 */
	public void setTicketCreationsPeriodSpec(final String ticketCreationsPeriodSpec) {
		this.ticketCreationsPeriodSpec = ticketCreationsPeriodSpec;
	}

	/**
	 * @return the ticketCreationsSort
	 */
	public String getTicketCreationsSort() {
		return ticketCreationsSort;
	}

	/**
	 * @param ticketCreationsSort the ticketCreationsSort to set
	 */
	public void setTicketCreationsSort(final String ticketCreationsSort) {
		this.ticketCreationsSort =
			org.esupportail.commons.utils.strings.StringUtils.nullIfEmpty(ticketCreationsSort);
	}

	/**
	 * @return the originI18nKeyProvider
	 */
	protected OriginI18nKeyProvider getOriginI18nKeyProvider() {
		return originI18nKeyProvider;
	}

	/**
	 * @param originI18nKeyProvider the originI18nKeyProvider to set
	 */
	public void setOriginI18nKeyProvider(final OriginI18nKeyProvider originI18nKeyProvider) {
		this.originI18nKeyProvider = originI18nKeyProvider;
	}

	/**
	 * @return the creationDepartments
	 */
	public List<Department> getCreationDepartments() {
		return creationDepartments;
	}

	/**
	 * @param creationDepartments the creationDepartments to set
	 */
	public void setCreationDepartments(final List<Department> creationDepartments) {
		this.creationDepartments = creationDepartments;
	}

	/**
	 * @return the origins
	 */
	public List<String> getOrigins() {
		return origins;
	}

	/**
	 * @param origins the origins to set
	 */
	public void setOrigins(final List<String> origins) {
		this.origins = origins;
	}

	/**
	 * @return the ticketCreatorsData
	 */
	public List<UserTicketCreationStatisticEntry> getTicketCreatorsData() {
		return ticketCreatorsData;
	}

	/**
	 * @param ticketCreatorsData the ticketCreatorsData to set
	 */
	protected void setTicketCreatorsData(
			final List<UserTicketCreationStatisticEntry> ticketCreatorsData) {
		this.ticketCreatorsData = ticketCreatorsData;
	}

	/**
	 * @return the ticketCreatorsMaxNumber
	 */
	public int getTicketCreatorsMaxNumber() {
		return ticketCreatorsMaxNumber;
	}

	/**
	 * @param ticketCreatorsMaxNumber the ticketCreatorsMaxNumber to set
	 */
	public void setTicketCreatorsMaxNumber(final int ticketCreatorsMaxNumber) {
		this.ticketCreatorsMaxNumber = ticketCreatorsMaxNumber;
	}

	/**
	 * @return the statisticsExtrator
	 */
	protected StatisticsExtractor getStatisticsExtrator() {
		return statisticsExtrator;
	}

	/**
	 * @param statisticsExtrator the statisticsExtrator to set
	 */
	public void setStatisticsExtrator(final StatisticsExtractor statisticsExtrator) {
		this.statisticsExtrator = statisticsExtrator;
	}

	/**
	 * @return the showMinMax
	 */
	public boolean isShowMinMax() {
		return showMinMax;
	}

	/**
	 * @param showMinMax the showMinMax to set
	 */
	public void setShowMinMax(final boolean showMinMax) {
		this.showMinMax = showMinMax;
	}

	/**
	 * @return the chartValues
	 */
	public Map<String, MinAvgMaxNumberStatistic> getChartValues() {
		return chartValues;
	}

	/**
	 * @return the chartValues
	 */
	public Set<String> getChartValuesLabels() {
		return chartValues.keySet();
	}

	/**
	 * @param chartValues the chartValues to set
	 */
	protected void setChartValues(final Map<String, MinAvgMaxNumberStatistic> chartValues) {
		this.chartValues = chartValues;
	}

	/**
	 * @return the ticketsData
	 */
	public List<StatisticsTicketEntry> getTicketsData() {
		return ticketsData;
	}

	/**
	 * @param ticketsData the ticketsData to set
	 */
	protected void setTicketsData(final List<StatisticsTicketEntry> ticketsData) {
		this.ticketsData = ticketsData;
	}

	/**
	 * @return the ticketsMaxNumber
	 */
	public int getTicketsMaxNumber() {
		return ticketsMaxNumber;
	}

	/**
	 * @param ticketsMaxNumber the ticketsMaxNumber to set
	 */
	public void setTicketsMaxNumber(final int ticketsMaxNumber) {
		this.ticketsMaxNumber = ticketsMaxNumber;
	}

	/**
	 * @return the hideCharged
	 */
	public boolean isHideCharged() {
		return hideCharged;
	}

	/**
	 * @param hideCharged the hideCharged to set
	 */
	public void setHideCharged(final boolean hideCharged) {
		this.hideCharged = hideCharged;
	}

	/**
	 * @return the hideClosed
	 */
	public boolean isHideClosed() {
		return hideClosed;
	}

	/**
	 * @param hideClosed the hideClosed to set
	 */
	public void setHideClosed(final boolean hideClosed) {
		this.hideClosed = hideClosed;
	}

	/**
	 * @return the statusSort
	 */
	public String getStatusSort() {
		return statusSort;
	}

	/**
	 * @param statusSort the statusSort to set
	 */
	public void setStatusSort(final String statusSort) {
		this.statusSort = statusSort;
	}

	/**
	 * @return the ignoreArchivedTickets
	 */
	public boolean isIgnoreArchivedTickets() {
		return ignoreArchivedTickets;
	}

	/**
	 * @param ignoreArchivedTickets the ignoreArchivedTickets to set
	 */
	public void setIgnoreArchivedTickets(final boolean ignoreArchivedTickets) {
		this.ignoreArchivedTickets = ignoreArchivedTickets;
	}

	/**
	 * @return the pieData
	 */
	public DefaultPieDataset getPieData() {
		return pieData;
	}

	/**
	 * @param pieData the pieData to set
	 */
	protected void setPieData(final DefaultPieDataset pieData) {
		this.pieData = pieData;
	}

	/**
	 * @return the spentTimeData
	 */
	public List<SpentTimeStatisticsEntry> getSpentTimeData() {
		return spentTimeData;
	}

	/**
	 * @param spentTimeData the spentTimeData to set
	 */
	protected void setSpentTimeData(final List<SpentTimeStatisticsEntry> spentTimeData) {
		this.spentTimeData = spentTimeData;
	}

}
