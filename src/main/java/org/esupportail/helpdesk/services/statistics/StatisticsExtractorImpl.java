/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.statistics;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.dao.DaoService;
import org.esupportail.helpdesk.domain.TicketStatus;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.statistics.DayOfWeekTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.DayOfWeekTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.DayTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.DayTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.HourOfDayTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.HourOfDayTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.HourOfWeekTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.HourOfWeekTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.MinAvgMaxNumberStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.MonthTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.MonthTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.SpentTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.StatusStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.StatusStatisticSet;
import org.esupportail.helpdesk.domain.beans.statistics.YearTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.YearTimeStatistic;
import org.esupportail.helpdesk.web.beans.StatisticsTicketEntry;
import org.esupportail.helpdesk.web.beans.UserTicketCreationStatisticEntry;
import org.springframework.beans.factory.InitializingBean;


/**
 * The basic implementation of StatisticsExtractor.
 */
public class StatisticsExtractorImpl implements StatisticsExtractor, InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -1991642155866631700L;

	/**
	 * The DAO service.
	 */
	private DaoService daoService;
	
    /**
	 * Constructor.
	 */
	public StatisticsExtractorImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(daoService, 
				"property daoService of class " + this.getClass().getName() 
				+ " can not be null");
	}

    /**
     * @param start 
     * @param end 
     * @return the yearly timestamps for a period. 
     */
    protected List<Timestamp> getYearTimestamps(
    		final Timestamp start, 
    		final Timestamp end) {
    	List<Timestamp> result = new ArrayList<Timestamp>();
    	Timestamp ts = StatisticsUtils.getYearRoundedDate(start); 
    	while (ts.before(end)) {
        	result.add(ts);
        	ts = StatisticsUtils.getNextYearDate(ts);
    	}
    	return result;
    }
    
    /**
     * @param start 
     * @param end 
     * @return the monthly timestamps for a period. 
     */
    protected List<Timestamp> getMonthTimestamps(
    		final Timestamp start, 
    		final Timestamp end) {
    	List<Timestamp> result = new ArrayList<Timestamp>();
    	Timestamp ts = StatisticsUtils.getMonthRoundedDate(start); 
    	while (ts.before(end)) {
        	result.add(ts);
        	ts = StatisticsUtils.getNextMonthDate(ts);
    	}
    	return result;
    }
    
    /**
     * @param start 
     * @param end 
     * @return the daily timestamps for a period. 
     */
    protected List<Timestamp> getDayTimestamps(
    		final Timestamp start, 
    		final Timestamp end) {
    	List<Timestamp> result = new ArrayList<Timestamp>();
    	Timestamp ts = StatisticsUtils.getDayRoundedDate(start); 
    	while (ts.before(end)) {
        	result.add(ts);
        	ts = StatisticsUtils.getNextDayDate(ts);
    	}
    	return result;
    }
    
    /**
     * @return the days of week. 
     */
    protected List<Integer> getDaysOfWeek() {
    	List<Integer> result = new ArrayList<Integer>();
    	result.add(Calendar.SUNDAY);
    	result.add(Calendar.MONDAY);
    	result.add(Calendar.TUESDAY);
    	result.add(Calendar.WEDNESDAY);
    	result.add(Calendar.THURSDAY);
    	result.add(Calendar.FRIDAY);
    	result.add(Calendar.SATURDAY);
    	return result;
    }
    
    /**
     * @return the hours of day. 
     */
    protected List<Integer> getHoursOfDay() {
    	List<Integer> result = new ArrayList<Integer>();
    	for (int i = 0; i < StatisticsUtils.HOURS_PER_DAY; i++) {
        	result.add(i);
		}
    	return result;
    }
    
    /**
     * @return the hours of week. 
     */
    protected List<Integer> getHoursOfWeek() {
    	List<Integer> result = new ArrayList<Integer>();
    	for (int i = 0; i < StatisticsUtils.HOURS_PER_DAY; i++) {
        	result.add(StatisticsUtils.hourOfWeek(Calendar.SUNDAY, i));
        	result.add(StatisticsUtils.hourOfWeek(Calendar.MONDAY, i));
        	result.add(StatisticsUtils.hourOfWeek(Calendar.TUESDAY, i));
        	result.add(StatisticsUtils.hourOfWeek(Calendar.WEDNESDAY, i));
        	result.add(StatisticsUtils.hourOfWeek(Calendar.THURSDAY, i));
        	result.add(StatisticsUtils.hourOfWeek(Calendar.FRIDAY, i));
        	result.add(StatisticsUtils.hourOfWeek(Calendar.SATURDAY, i));
		}
    	return result;
    }
    
    /**
     * @return an empty map.
     */
    protected Map<Timestamp, Integer> emptyTimestampIntegerMap() {
    	return new TreeMap<Timestamp, Integer>();
    }

    /**
     * Fill missing stats.
     * @param dates 
     * @param map 
     */
    protected void fillMissingTimestampIntegerMap(
    		final List<Timestamp> dates,
    		final Map<Timestamp, Integer> map) {
    	for (Timestamp date : dates) {
    		if (map.get(date) == null) {
    			map.put(date, 0);
    		}
		}
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByYear(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Timestamp, Integer> getTicketCreationsByYear(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Timestamp> dates = getYearTimestamps(start, end);
		Map<Timestamp, Integer> result = emptyTimestampIntegerMap();
    	List<YearTicketCreationStatistic> stats = getDaoService().getTicketCreationsByYear(
    			start, end, StatisticsExtractor.GLOBAL, departments, origins);
    	for (YearTicketCreationStatistic stat : stats) {
    		result.put(StatisticsUtils.getYearDate(stat.getYear()), stat.getNumber());
		}
    	fillMissingTimestampIntegerMap(dates, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByMonth(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Timestamp, Integer> getTicketCreationsByMonth(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Timestamp> dates = getMonthTimestamps(start, end);
		Map<Timestamp, Integer> result = emptyTimestampIntegerMap(); 
    	List<MonthTicketCreationStatistic> stats = getDaoService().getTicketCreationsByMonth(
    			start, end, StatisticsExtractor.GLOBAL, departments, origins);
    	for (MonthTicketCreationStatistic stat : stats) {
    		result.put(StatisticsUtils.getMonthDate(
    				stat.getYear(), stat.getMonth()), stat.getNumber());
		}
    	fillMissingTimestampIntegerMap(dates, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByDay(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Timestamp, Integer> getTicketCreationsByDay(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Timestamp> dates = getDayTimestamps(start, end);
		Map<Timestamp, Integer> result = emptyTimestampIntegerMap(); 
    	List<DayTicketCreationStatistic> stats = getDaoService().getTicketCreationsByDay(
    			start, end, StatisticsExtractor.GLOBAL, departments, origins);
    	for (DayTicketCreationStatistic stat : stats) {
    		result.put(StatisticsUtils.getDayDate(
    				stat.getYear(), stat.getMonth(), stat.getDayOfMonth()), stat.getNumber());
		}
    	fillMissingTimestampIntegerMap(dates, result);
    	return result;
    }

    /**
     * @return an empty map for global statistics results.
     */
    protected Map<Integer, Integer> emptyIntegerIntegerMap() {
    	return new TreeMap<Integer, Integer>();
    }

    /**
     * Fill missing stats.
     * @param integers
     * @param map 
     */
    protected void fillMissingIntegerIntegerMap(
    		final List<Integer> integers,
    		final Map<Integer, Integer> map) {
    	for (Integer integer : integers) {
    		if (map.get(integer) == null) {
    			map.put(integer, 0);
    		}
		}
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByDayOfWeek(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Integer, Integer> getTicketCreationsByDayOfWeek(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Integer> daysOfWeek = getDaysOfWeek();
		Map<Integer, Integer> result = emptyIntegerIntegerMap(); 
    	List<DayOfWeekTicketCreationStatistic> stats = getDaoService().getTicketCreationsByDayOfWeek(
    			start, end, StatisticsExtractor.GLOBAL, departments, origins);
    	for (DayOfWeekTicketCreationStatistic stat : stats) {
    		result.put(stat.getDayOfWeek(), stat.getNumber());
		}
    	fillMissingIntegerIntegerMap(daysOfWeek, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByHourOfDay(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Integer, Integer> getTicketCreationsByHourOfDay(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Integer> hoursOfDay = getHoursOfDay();
		Map<Integer, Integer> result = emptyIntegerIntegerMap(); 
    	List<HourOfDayTicketCreationStatistic> stats = getDaoService().getTicketCreationsByHourOfDay(
    			start, end, StatisticsExtractor.GLOBAL, departments, origins);
    	for (HourOfDayTicketCreationStatistic stat : stats) {
    		result.put(stat.getHourOfDay(), stat.getNumber());
		}
    	fillMissingIntegerIntegerMap(hoursOfDay, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByHourOfWeek(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Integer, Integer> getTicketCreationsByHourOfWeek(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Integer> hoursOfWeek = getHoursOfWeek();
		Map<Integer, Integer> result = emptyIntegerIntegerMap(); 
    	List<HourOfWeekTicketCreationStatistic> stats = getDaoService().getTicketCreationsByHourOfWeek(
    			start, end, StatisticsExtractor.GLOBAL, departments, origins);
    	for (HourOfWeekTicketCreationStatistic stat : stats) {
    		result.put(StatisticsUtils.hourOfWeek(stat.getDayOfWeek(), stat.getHourOfDay()), stat.getNumber());
		}
    	fillMissingIntegerIntegerMap(hoursOfWeek, result);
    	return result;
    }

    /**
     * @param dates 
     * @return an empty map.
     */
    protected Map<Timestamp, Map<Department, Integer>> emptyTimestampDepartmentIntegerMap(
    		final List<Timestamp> dates) {
    	Map<Timestamp, Map<Department, Integer>> result = new TreeMap<Timestamp, Map<Department, Integer>>();
    	for (Timestamp date : dates) {
			result.put(date, new TreeMap<Department, Integer>());
		}
    	return result;
    }

    /**
     * Fill missing stats.
     * @param dates 
     * @param departments 
     * @param map 
     */
    protected void fillMissingTimestampDepartmentIntegerMap(
    		final List<Timestamp> dates,
    		final List<Department> departments,
    		final Map<Timestamp, Map<Department, Integer>> map) {
    	for (Timestamp date : dates) {
    		Map<Department, Integer> subMap = map.get(date);
    		for (Department department : departments) {
				if (subMap.get(department) == null) {
					subMap.put(department, 0);
				}
			}
		}
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByYearPerDepartment(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Timestamp, Map<Department, Integer>> getTicketCreationsByYearPerDepartment(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Timestamp> dates = getYearTimestamps(start, end);
		Map<Timestamp, Map<Department, Integer>> result = emptyTimestampDepartmentIntegerMap(dates); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<YearTicketCreationStatistic> stats = getDaoService().getTicketCreationsByYear(
    			start, end, StatisticsExtractor.PER_DEPARTMENT, theDepartments, theOrigins);
    	for (YearTicketCreationStatistic stat : stats) {
    		Timestamp date = StatisticsUtils.getYearDate(stat.getYear()); 
    		result.get(date).put(stat.getDepartment(), stat.getNumber());
		}
    	fillMissingTimestampDepartmentIntegerMap(dates, theDepartments, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByMonthPerDepartment(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Timestamp, Map<Department, Integer>> getTicketCreationsByMonthPerDepartment(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Timestamp> dates = getMonthTimestamps(start, end);
		Map<Timestamp, Map<Department, Integer>> result = emptyTimestampDepartmentIntegerMap(dates); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<MonthTicketCreationStatistic> stats = getDaoService().getTicketCreationsByMonth(
    			start, end, StatisticsExtractor.PER_DEPARTMENT, theDepartments, theOrigins);
    	for (MonthTicketCreationStatistic stat : stats) {
    		Timestamp date = StatisticsUtils.getMonthDate(stat.getYear(), stat.getMonth()); 
    		result.get(date).put(stat.getDepartment(), stat.getNumber());
		}
    	fillMissingTimestampDepartmentIntegerMap(dates, theDepartments, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByDayPerDepartment(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Timestamp, Map<Department, Integer>> getTicketCreationsByDayPerDepartment(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Timestamp> dates = getDayTimestamps(start, end);
		Map<Timestamp, Map<Department, Integer>> result = emptyTimestampDepartmentIntegerMap(dates); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<DayTicketCreationStatistic> stats = getDaoService().getTicketCreationsByDay(
    			start, end, StatisticsExtractor.PER_DEPARTMENT, theDepartments, theOrigins);
    	for (DayTicketCreationStatistic stat : stats) {
    		Timestamp date = StatisticsUtils.getDayDate(stat.getYear(), stat.getMonth(), stat.getDayOfMonth()); 
    		result.get(date).put(stat.getDepartment(), stat.getNumber());
		}
    	fillMissingTimestampDepartmentIntegerMap(dates, theDepartments, result);
    	return result;
    }

    /**
     * @param integers
     * @return an empty map.
     */
    protected Map<Integer, Map<Department, Integer>> emptyIntegerDepartmentIntegerMap(
    		final List<Integer> integers) {
    	Map<Integer, Map<Department, Integer>> result = new TreeMap<Integer, Map<Department, Integer>>();
    	for (Integer integer : integers) {
			result.put(integer, new TreeMap<Department, Integer>());
		}
    	return result;
    }

    /**
     * Fill missing stats.
     * @param integers
     * @param departments 
     * @param map 
     */
    protected void fillMissingIntegerDepartmentIntegerMap(
    		final List<Integer> integers,
    		final List<Department> departments,
    		final Map<Integer, Map<Department, Integer>> map) {
    	for (Integer integer : integers) {
    		Map<Department, Integer> subMap = map.get(integer);
    		for (Department department : departments) {
				if (subMap.get(department) == null) {
					subMap.put(department, 0);
				}
			}
		}
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByDayOfWeekPerDepartment(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Integer, Map<Department, Integer>> getTicketCreationsByDayOfWeekPerDepartment(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Integer> daysOfWeek = getDaysOfWeek();
		Map<Integer, Map<Department, Integer>> result = emptyIntegerDepartmentIntegerMap(daysOfWeek); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<DayOfWeekTicketCreationStatistic> stats = getDaoService().getTicketCreationsByDayOfWeek(
    			start, end, StatisticsExtractor.PER_DEPARTMENT, theDepartments, theOrigins);
    	for (DayOfWeekTicketCreationStatistic stat : stats) {
    		result.get(stat.getDayOfWeek()).put(stat.getDepartment(), stat.getNumber());
		}
    	fillMissingIntegerDepartmentIntegerMap(daysOfWeek, departments, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByHourOfDayPerDepartment(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Integer, Map<Department, Integer>> getTicketCreationsByHourOfDayPerDepartment(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Integer> hoursOfDay = getHoursOfDay();
		Map<Integer, Map<Department, Integer>> result = emptyIntegerDepartmentIntegerMap(hoursOfDay); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<HourOfDayTicketCreationStatistic> stats = getDaoService().getTicketCreationsByHourOfDay(
    			start, end, StatisticsExtractor.PER_DEPARTMENT, theDepartments, theOrigins);
    	for (HourOfDayTicketCreationStatistic stat : stats) {
    		result.get(stat.getHourOfDay()).put(stat.getDepartment(), stat.getNumber());
		}
    	fillMissingIntegerDepartmentIntegerMap(hoursOfDay, departments, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor
     * #getTicketCreationsByHourOfWeekPerDepartment(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Integer, Map<Department, Integer>> getTicketCreationsByHourOfWeekPerDepartment(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Integer> hoursOfWeek = getHoursOfWeek();
		Map<Integer, Map<Department, Integer>> result = emptyIntegerDepartmentIntegerMap(hoursOfWeek); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}

    	List<HourOfWeekTicketCreationStatistic> stats = getDaoService().getTicketCreationsByHourOfWeek(
    			start, end, StatisticsExtractor.PER_DEPARTMENT, theDepartments, theOrigins);
    	for (HourOfWeekTicketCreationStatistic stat : stats) {
    		result.get(StatisticsUtils.hourOfWeek(stat.getDayOfWeek(), stat.getHourOfDay())).put(
    				stat.getDepartment(), stat.getNumber());
		}
    	fillMissingIntegerDepartmentIntegerMap(hoursOfWeek, departments, result);
    	return result;
    }

    /**
     * @param dates 
     * @return an empty map.
     */
    protected Map<Timestamp, Map<String, Integer>> emptyTimestampStringIntegerMap(
    		final List<Timestamp> dates) {
    	Map<Timestamp, Map<String, Integer>> result = new TreeMap<Timestamp, Map<String, Integer>>();
    	for (Timestamp date : dates) {
			result.put(date, new TreeMap<String, Integer>());
		}
    	return result;
    }

    /**
     * Fill missing stats.
     * @param dates 
     * @param strings
     * @param map 
     */
    protected void fillMissingTimestampStringIntegerMap(
    		final List<Timestamp> dates,
    		final List<String> strings,
    		final Map<Timestamp, Map<String, Integer>> map) {
    	for (Timestamp date : dates) {
    		Map<String, Integer> subMap = map.get(date);
    		for (String string : strings) {
				if (subMap.get(string) == null) {
					subMap.put(string, 0);
				}
			}
		}
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByYearPerOrigin(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Timestamp, Map<String, Integer>> getTicketCreationsByYearPerOrigin(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Timestamp> dates = getYearTimestamps(start, end);
		Map<Timestamp, Map<String, Integer>> result = emptyTimestampStringIntegerMap(dates); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<YearTicketCreationStatistic> stats = getDaoService().getTicketCreationsByYear(
    			start, end, StatisticsExtractor.PER_ORIGIN, theDepartments, theOrigins);
    	for (YearTicketCreationStatistic stat : stats) {
    		Timestamp date = StatisticsUtils.getYearDate(stat.getYear()); 
    		result.get(date).put(stat.getOrigin(), stat.getNumber());
		}
    	fillMissingTimestampStringIntegerMap(dates, theOrigins, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByMonthPerOrigin(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Timestamp, Map<String, Integer>> getTicketCreationsByMonthPerOrigin(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Timestamp> dates = getMonthTimestamps(start, end);
		Map<Timestamp, Map<String, Integer>> result = emptyTimestampStringIntegerMap(dates); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<MonthTicketCreationStatistic> stats = getDaoService().getTicketCreationsByMonth(
    			start, end, StatisticsExtractor.PER_ORIGIN, theDepartments, theOrigins);
    	for (MonthTicketCreationStatistic stat : stats) {
    		Timestamp date = StatisticsUtils.getMonthDate(stat.getYear(), stat.getMonth()); 
    		result.get(date).put(stat.getOrigin(), stat.getNumber());
		}
    	fillMissingTimestampStringIntegerMap(dates, theOrigins, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByDayPerOrigin(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Timestamp, Map<String, Integer>> getTicketCreationsByDayPerOrigin(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Timestamp> dates = getDayTimestamps(start, end);
		Map<Timestamp, Map<String, Integer>> result = emptyTimestampStringIntegerMap(dates); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<DayTicketCreationStatistic> stats = getDaoService().getTicketCreationsByDay(
    			start, end, StatisticsExtractor.PER_ORIGIN, theDepartments, theOrigins);
    	for (DayTicketCreationStatistic stat : stats) {
    		Timestamp date = StatisticsUtils.getDayDate(stat.getYear(), stat.getMonth(), stat.getDayOfMonth()); 
    		result.get(date).put(stat.getOrigin(), stat.getNumber());
		}
    	fillMissingTimestampStringIntegerMap(dates, theOrigins, result);
    	return result;
    }

    /**
     * @param integers
     * @return an empty map for per origin statistics results.
     */
    protected Map<Integer, Map<String, Integer>> emptyIntegerStringIntegerMap(
    		final List<Integer> integers) {
    	Map<Integer, Map<String, Integer>> result = new TreeMap<Integer, Map<String, Integer>>();
    	for (Integer integer : integers) {
			result.put(integer, new TreeMap<String, Integer>());
		}
    	return result;
    }

    /**
     * Fill missing stats.
     * @param integers
     * @param strings
     * @param map 
     */
    protected void fillMissingIntegerStringIntegerMap(
    		final List<Integer> integers,
    		final List<String> strings,
    		final Map<Integer, Map<String, Integer>> map) {
    	for (Integer integer : integers) {
    		Map<String, Integer> subMap = map.get(integer);
    		for (String string : strings) {
				if (subMap.get(string) == null) {
					subMap.put(string, 0);
				}
			}
		}
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByDayOfWeekPerOrigin(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Integer, Map<String, Integer>> getTicketCreationsByDayOfWeekPerOrigin(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Integer> daysOfWeek = getDaysOfWeek();
		Map<Integer, Map<String, Integer>> result = emptyIntegerStringIntegerMap(daysOfWeek); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<DayOfWeekTicketCreationStatistic> stats = getDaoService().getTicketCreationsByDayOfWeek(
    			start, end, StatisticsExtractor.PER_ORIGIN, theDepartments, theOrigins);
    	for (DayOfWeekTicketCreationStatistic stat : stats) {
    		result.get(stat.getDayOfWeek()).put(stat.getOrigin(), stat.getNumber());
		}
    	fillMissingIntegerStringIntegerMap(daysOfWeek, origins, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByHourOfDayPerOrigin(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Integer, Map<String, Integer>> getTicketCreationsByHourOfDayPerOrigin(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Integer> hoursOfDay = getHoursOfDay();
		Map<Integer, Map<String, Integer>> result = emptyIntegerStringIntegerMap(hoursOfDay); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<HourOfDayTicketCreationStatistic> stats = getDaoService().getTicketCreationsByHourOfDay(
    			start, end, StatisticsExtractor.PER_ORIGIN, theDepartments, theOrigins);
    	for (HourOfDayTicketCreationStatistic stat : stats) {
    		result.get(stat.getHourOfDay()).put(stat.getOrigin(), stat.getNumber());
		}
    	fillMissingIntegerStringIntegerMap(hoursOfDay, origins, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketCreationsByHourOfWeekPerOrigin(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, java.util.List)
     */
    @Override
	public Map<Integer, Map<String, Integer>> getTicketCreationsByHourOfWeekPerOrigin(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final List<String> origins) {
    	List<Integer> hoursOfWeek = getHoursOfWeek();
		Map<Integer, Map<String, Integer>> result = emptyIntegerStringIntegerMap(hoursOfWeek); 
    	List<Department> theDepartments = departments;
    	if (theDepartments == null || theDepartments.isEmpty()) {
    		theDepartments = getDaoService().getTicketCreationDepartments(start, end, origins);
    	}
    	List<String> theOrigins = origins;
    	if (theOrigins == null || theOrigins.isEmpty()) {
    		theOrigins = getDaoService().getTicketCreationOrigins(start, end, departments);
    	}
    	List<HourOfWeekTicketCreationStatistic> stats = getDaoService().getTicketCreationsByHourOfWeek(
    			start, end, StatisticsExtractor.PER_ORIGIN, theDepartments, theOrigins);
    	for (HourOfWeekTicketCreationStatistic stat : stats) {
    		result.get(StatisticsUtils.hourOfWeek(stat.getDayOfWeek(), stat.getHourOfDay())).put(
    				stat.getOrigin(), stat.getNumber());
		}
    	fillMissingIntegerStringIntegerMap(hoursOfWeek, origins, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getUserTicketCreations(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, int)
     */
    @Override
	public List<UserTicketCreationStatisticEntry> getUserTicketCreations(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final int maxEntries) {
    	return getDaoService().getUserTicketCreations(start, end, departments, maxEntries);
    }

    /**
     * @return an empty map for statistics results.
     */
    protected Map<Timestamp, MinAvgMaxNumberStatistic> emptyTimestampMinAvgMaxNumberMap() {
    	return new TreeMap<Timestamp, MinAvgMaxNumberStatistic>();
    }

    /**
     * Fill missing stats.
     * @param dates
     * @param map 
     */
    protected void fillMissingTimestampMinAvgMaxNumberMap(
    		final List<Timestamp> dates,
    		final Map<Timestamp, MinAvgMaxNumberStatistic> map) {
    	for (Timestamp date : dates) {
    		if (map.get(date) == null) {
    			map.put(date, new MinAvgMaxNumberStatistic());
    		}
		}
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getChargeOrClosureTimeByYear(
     * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
     */
    @Override
	public Map<Timestamp, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByYear(
    		final boolean charge, 
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments) {
    	List<Timestamp> dates = getYearTimestamps(start, end);
		Map<Timestamp, MinAvgMaxNumberStatistic> result = emptyTimestampMinAvgMaxNumberMap();
    	List<YearTimeStatistic> stats = getDaoService().getChargeOrClosureTimeByYear(
    			charge, start, end, departments);
    	for (YearTimeStatistic stat : stats) {
    		result.put(
    				StatisticsUtils.getYearDate(stat.getYear()), 
    				new MinAvgMaxNumberStatistic(
    						stat.getMin(), stat.getAvg(), stat.getMax(), stat.getNumber()));
		}
    	fillMissingTimestampMinAvgMaxNumberMap(dates, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getChargeOrClosureTimeByMonth(
     * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
     */
    @Override
	public Map<Timestamp, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByMonth(
    		final boolean charge, 
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments) {
    	List<Timestamp> dates = getMonthTimestamps(start, end);
		Map<Timestamp, MinAvgMaxNumberStatistic> result = emptyTimestampMinAvgMaxNumberMap();
    	List<MonthTimeStatistic> stats = getDaoService().getChargeOrClosureTimeByMonth(
    			charge, start, end, departments);
    	for (MonthTimeStatistic stat : stats) {
    		result.put(
    				StatisticsUtils.getMonthDate(stat.getYear(), stat.getMonth()), 
    				new MinAvgMaxNumberStatistic(
    						stat.getMin(), stat.getAvg(), stat.getMax(), stat.getNumber()));
		}
    	fillMissingTimestampMinAvgMaxNumberMap(dates, result);
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getChargeOrClosureTimeByDay(
     * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
     */
    @Override
	public Map<Timestamp, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByDay(
    		final boolean charge, 
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments) {
    	List<Timestamp> dates = getDayTimestamps(start, end);
		Map<Timestamp, MinAvgMaxNumberStatistic> result = emptyTimestampMinAvgMaxNumberMap();
    	List<DayTimeStatistic> stats = getDaoService().getChargeOrClosureTimeByDay(
    			charge, start, end, departments);
    	for (DayTimeStatistic stat : stats) {
    		result.put(
    				StatisticsUtils.getDayDate(stat.getYear(), stat.getMonth(), stat.getDayOfMonth()), 
    				new MinAvgMaxNumberStatistic(
    						stat.getMin(), stat.getAvg(), stat.getMax(), stat.getNumber()));
		}
    	fillMissingTimestampMinAvgMaxNumberMap(dates, result);
    	return result;
    }

    /**
     * @return an empty map for statistics results.
     */
    protected Map<Integer, MinAvgMaxNumberStatistic> emptyIntegerMinAvgMaxNumberMap() {
    	return new TreeMap<Integer, MinAvgMaxNumberStatistic>();
    }

    /**
     * Fill missing stats.
     * @param integers
     * @param map 
     */
    protected void fillMissingIntegerMinAvgMaxNumberMap(
    		final List<Integer> integers,
    		final Map<Integer, MinAvgMaxNumberStatistic> map) {
    	for (Integer integer : integers) {
    		if (map.get(integer) == null) {
    			map.put(integer, new MinAvgMaxNumberStatistic());
    		}
		}
    }

	/**
	 * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getChargeOrClosureTimeByDayOfWeek(
	 * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public Map<Integer, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByDayOfWeek(
			final boolean charge,
    		final Timestamp start, 
			final Timestamp end, 
			final List<Department> departments) {
		Map<Integer, MinAvgMaxNumberStatistic> result = emptyIntegerMinAvgMaxNumberMap(); 
    	List<DayOfWeekTimeStatistic> stats = getDaoService().getChargeOrClosureTimeByDayOfWeek(
    			charge, start, end, departments);
    	for (DayOfWeekTimeStatistic stat : stats) {
    		result.put(stat.getDayOfWeek(), 
    				new MinAvgMaxNumberStatistic(
    						stat.getMin(), stat.getAvg(), stat.getMax(), stat.getNumber()));
		}
    	fillMissingIntegerMinAvgMaxNumberMap(getDaysOfWeek(), result);
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getChargeOrClosureTimeByHourOfDay(
	 * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public Map<Integer, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByHourOfDay(
			final boolean charge,
    		final Timestamp start, 
			final Timestamp end, 
			final List<Department> departments) {
		Map<Integer, MinAvgMaxNumberStatistic> result = emptyIntegerMinAvgMaxNumberMap(); 
    	List<HourOfDayTimeStatistic> stats = getDaoService().getChargeOrClosureTimeByHourOfDay(
    			charge, start, end, departments);
    	for (HourOfDayTimeStatistic stat : stats) {
    		result.put(stat.getHourOfDay(), 
    				new MinAvgMaxNumberStatistic(
    						stat.getMin(), stat.getAvg(), stat.getMax(), stat.getNumber()));
		}
    	fillMissingIntegerMinAvgMaxNumberMap(getHoursOfDay(), result);
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getChargeOrClosureTimeByHourOfWeek(
	 * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public Map<Integer, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByHourOfWeek(
			final boolean charge,
    		final Timestamp start, 
			final Timestamp end, 
			final List<Department> departments) {
		Map<Integer, MinAvgMaxNumberStatistic> result = emptyIntegerMinAvgMaxNumberMap(); 
    	List<HourOfWeekTimeStatistic> stats = getDaoService().getChargeOrClosureTimeByHourOfWeek(
    			charge, start, end, departments);
    	for (HourOfWeekTimeStatistic stat : stats) {
    		result.put(StatisticsUtils.hourOfWeek(stat.getDayOfWeek(), stat.getHourOfDay()),
    				new MinAvgMaxNumberStatistic(
    						stat.getMin(), stat.getAvg(), stat.getMax(), stat.getNumber()));
		}
    	fillMissingIntegerMinAvgMaxNumberMap(getHoursOfWeek(), result);
    	return result;
	}

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getTicketsWithLongChargeOrClosureTime(
     * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List, int, boolean)
     */
    @Override
	public List<StatisticsTicketEntry> getTicketsWithLongChargeOrClosureTime(
    		final boolean charge,
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final int maxEntries,
    		final boolean hideChargedOrClosed) {
    	return daoService.getTicketsWithLongChargeOrClosureTime(
    			charge, start, end, departments, maxEntries, hideChargedOrClosed);
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getStatusStatistics(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, boolean)
     */
    @Override
	public StatusStatisticSet getStatusStatistics(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final boolean ignoreArchivedTickets) {
    	StatusStatisticSet result = new StatusStatisticSet();
    	List<StatusStatistic> stats = daoService.getStatusStatistics(
    			start, end, StatisticsExtractor.GLOBAL, departments, ignoreArchivedTickets);
    	for (StatusStatistic stat : stats) {
			if (TicketStatus.ARCHIVED.equals(stat.getStatus())) {
				result.incArchivedNumber(stat.getNumber());
			} else if (TicketStatus.FREE.equals(stat.getStatus())) {
				result.incFreeNumber(stat.getNumber());
			} else if (TicketStatus.INCOMPLETE.equals(stat.getStatus())) {
				result.incIncompleteNumber(stat.getNumber());
			} else if (TicketStatus.INPROGRESS.equals(stat.getStatus())) {
				result.incInProgressNumber(stat.getNumber());
			} else if (TicketStatus.POSTPONED.equals(stat.getStatus())) {
				result.incPostponedNumber(stat.getNumber());
			} else {
				result.incClosedNumber(stat.getNumber());
			}
		}
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getStatusStatisticsPerDepartment(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List, boolean)
     */
    @Override
	public Map<Long, StatusStatisticSet> getStatusStatisticsPerDepartment(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments,
    		final boolean ignoreArchivedTickets) {
    	Map<Long, StatusStatisticSet> result = new HashMap<Long, StatusStatisticSet>();
    	List<StatusStatistic> stats = daoService.getStatusStatistics(
    			start, end, StatisticsExtractor.PER_DEPARTMENT, departments, ignoreArchivedTickets);
    	for (StatusStatistic stat : stats) {
    		Long departmentId = stat.getDepartmentId();
    		if (result.get(departmentId) == null) {
    			result.put(departmentId, new StatusStatisticSet());
    		}
    		StatusStatisticSet sss = result.get(departmentId);
			if (TicketStatus.ARCHIVED.equals(stat.getStatus())) {
				sss.incArchivedNumber(stat.getNumber());
			} else if (TicketStatus.FREE.equals(stat.getStatus())) {
				sss.incFreeNumber(stat.getNumber());
			} else if (TicketStatus.INCOMPLETE.equals(stat.getStatus())) {
				sss.incIncompleteNumber(stat.getNumber());
			} else if (TicketStatus.INPROGRESS.equals(stat.getStatus())) {
				sss.incInProgressNumber(stat.getNumber());
			} else if (TicketStatus.POSTPONED.equals(stat.getStatus())) {
				sss.incPostponedNumber(stat.getNumber());
			} else {
				sss.incClosedNumber(stat.getNumber());
			}
		}
    	return result;
    }

    /**
     * @see org.esupportail.helpdesk.services.statistics.StatisticsExtractor#getSpentTimeStatistics(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List)
     */
    @Override
	public List<SpentTimeStatistic> getSpentTimeStatistics(
    		final Timestamp start, 
    		final Timestamp end,
    		final List<Department> departments) {
    	return daoService.getSpentTimeStatistics(start, end, departments);
    }

    /**
     * @return the daoService
     */
    protected DaoService getDaoService() {
    	return daoService;
    }

    /**
     * @param daoService the daoService to set
     */
    public void setDaoService(final DaoService daoService) {
    	this.daoService = daoService;
    }

}
