/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.statistics;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.statistics.MinAvgMaxNumberStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.SpentTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.StatusStatisticSet;
import org.esupportail.helpdesk.web.beans.StatisticsTicketEntry;
import org.esupportail.helpdesk.web.beans.UserTicketCreationStatisticEntry;


/**
 * the interface of statistics computers.
 */
public interface StatisticsExtractor extends Serializable {
	
	/**
	 * A constant for global statistics.
	 */
	int GLOBAL = 0;
	
	/**
	 * A constant for per department statistics.
	 */
	int PER_DEPARTMENT = 1;
	
	/**
	 * A constant for per origin statistics.
	 */
	int PER_ORIGIN = 2;
	
    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each year of a period. 
     */
    Map<Timestamp, Integer> getTicketCreationsByYear(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each month of a period. 
     */
    Map<Timestamp, Integer> getTicketCreationsByMonth(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each day of a period. 
     */
    Map<Timestamp, Integer> getTicketCreationsByDay(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each day of week of a period. 
     */
    Map<Integer, Integer> getTicketCreationsByDayOfWeek(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each hour of day of a period. 
     */
    Map<Integer, Integer> getTicketCreationsByHourOfDay(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each hour of week of a period. 
     */
    Map<Integer, Integer> getTicketCreationsByHourOfWeek(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each year of a period per department. 
     */
    Map<Timestamp, Map<Department, Integer>> getTicketCreationsByYearPerDepartment(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each month of a period per department. 
     */
    Map<Timestamp, Map<Department, Integer>> getTicketCreationsByMonthPerDepartment(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each day of a period for per department. 
     */
    Map<Timestamp, Map<Department, Integer>> getTicketCreationsByDayPerDepartment(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each day of week of a period for per department. 
     */
    Map<Integer, Map<Department, Integer>> getTicketCreationsByDayOfWeekPerDepartment(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each hour of day of a period for per department. 
     */
    Map<Integer, Map<Department, Integer>> getTicketCreationsByHourOfDayPerDepartment(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each hour of week of a period for per department. 
     */
    Map<Integer, Map<Department, Integer>> getTicketCreationsByHourOfWeekPerDepartment(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each year of a period per origin. 
     */
    Map<Timestamp, Map<String, Integer>> getTicketCreationsByYearPerOrigin(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each month of a period per origin. 
     */
    Map<Timestamp, Map<String, Integer>> getTicketCreationsByMonthPerOrigin(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each day of a period for per origin. 
     */
    Map<Timestamp, Map<String, Integer>> getTicketCreationsByDayPerOrigin(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each day of week of a period for per origin. 
     */
    Map<Integer, Map<String, Integer>> getTicketCreationsByDayOfWeekPerOrigin(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each hour of day of a period for per origin. 
     */
    Map<Integer, Map<String, Integer>> getTicketCreationsByHourOfDayPerOrigin(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);

    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param origins 
     * @return the number of ticket creations for each hour of week of a period for per origin. 
     */
    Map<Integer, Map<String, Integer>> getTicketCreationsByHourOfWeekPerOrigin(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		List<String> origins);
    
    /**
     * @param start 
     * @param end 
     * @param departments 
     * @param maxEntries 
     * @return the number of user ticket creations for a period. 
     */
    List<UserTicketCreationStatisticEntry> getUserTicketCreations(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		int maxEntries);

	/**
     * @param charge 
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time by day for a period.
	 */
    Map<Timestamp, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByDay(
    		boolean charge,
    		Timestamp start, 
    		Timestamp end,
			List<Department> departments);

	/**
     * @param charge 
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time by month for a period.
	 */
    Map<Timestamp, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByMonth(
    		boolean charge,
    		Timestamp start,
			Timestamp end, 
			List<Department> departments);

	/**
     * @param charge 
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time by year for a period.
	 */
    Map<Timestamp, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByYear(
    		boolean charge,
    		Timestamp start, 
    		Timestamp end,
			List<Department> departments);

	/**
     * @param charge 
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time by day of week for a period.
	 */
    Map<Integer, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByDayOfWeek(
    		boolean charge,
    		Timestamp start, 
    		Timestamp end,
			List<Department> departments);

	/**
     * @param charge 
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time by hour of day for a period.
	 */
    Map<Integer, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByHourOfDay(
    		boolean charge,
    		Timestamp start, 
    		Timestamp end,
			List<Department> departments);

	/**
     * @param charge 
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time by hour of week for a period.
	 */
    Map<Integer, MinAvgMaxNumberStatistic> getChargeOrClosureTimeByHourOfWeek(
    		boolean charge,
    		Timestamp start, 
    		Timestamp end,
			List<Department> departments);

    /**
     * @param charge 
     * @param start 
     * @param end 
     * @param departments 
     * @param maxEntries 
     * @param hideChargedOrClosed 
     * @return tickets. 
     */
    List<StatisticsTicketEntry> getTicketsWithLongChargeOrClosureTime(
    		boolean charge,
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		int maxEntries,
    		boolean hideChargedOrClosed);

    /**
     * @param start
     * @param end
     * @param departments
     * @param ignoreArchivedTickets
     * @return the status statistics for a list of departments and a period.
     */
    StatusStatisticSet getStatusStatistics(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		boolean ignoreArchivedTickets);

    /**
     * @param start
     * @param end
     * @param departments
     * @param ignoreArchivedTickets
     * @return the status statistics for a list of departments and a period.
     */
    Map<Long, StatusStatisticSet> getStatusStatisticsPerDepartment(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments,
    		boolean ignoreArchivedTickets);

    /**
     * @param start
     * @param end
     * @param departments
     * @return the spent time statistics for a list of departments and a period.
     */
    List<SpentTimeStatistic> getSpentTimeStatistics(
    		Timestamp start, 
    		Timestamp end,
    		List<Department> departments);

}
