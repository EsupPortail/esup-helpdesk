/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.reporting;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.TicketStatus;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.exceptions.DepartmentManagerNotFoundException;
import org.esupportail.helpdesk.services.statistics.StatisticsUtils;
import org.esupportail.helpdesk.web.beans.ElapsedTimeI18nFormatter;
import org.esupportail.helpdesk.web.beans.PriorityI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.PriorityStyleClassProvider;
import org.esupportail.helpdesk.web.beans.TicketStatusI18nKeyProvider;

/**
 * The basic implementation of TicketReporter.
 */
public class TicketReporterImpl extends AbstractSenderFormatter implements TicketReporter {

	/**
	 * A constant for reports results.
	 */
	private static final String REPORT_MANAGED = "REPORT_MANAGED";

	/**
	 * A constant for reports results.
	 */
	private static final String REPORT_FREE = "REPORT_FREE";

	/**
	 * A constant for reports results.
	 */
	private static final String REPORT_CATEGORY_MEMBER = "REPORT_CATEGORY_MEMBER";

	/**
	 * A constant for reports results.
	 */
	private static final String REPORT_OTHER = "REPORT_OTHER";

	/**
	 * Bean constructor.
	 */
	public TicketReporterImpl() {
		super();
	}

	/**
	 * @param user
	 * @param noTicketTitleKey
	 * @param titleKey
	 * @param tickets
	 * @return the HTML content for a list of tickets.
	 */
	protected String getTicketsReportContent(final User user, final String noTicketTitleKey, final String titleKey,
			final List<Ticket> tickets, final Boolean isFree) {
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String result = "";
		if (tickets.isEmpty()) {
			result += getI18nService().getString(noTicketTitleKey, locale);
		} else {
			result += getI18nService().getString(titleKey, locale, new Integer(tickets.size()));
			result += getI18nService().getString("EMAIL.TICKET_REPORT.TICKET_TABLE_HEADER", locale);
			boolean alternateColor = false;
			int day = 24 * 3600 * 1000;
			int week = 7 * 24 * 3600 * 1000;
			long month = 2592000000L;
			Date currentDate = new Date();
			for (Ticket ticket : tickets) {
				String trClass = "table-";

				if (isFree) {
					if (currentDate.getTime() - ticket.getLastActionDate().getTime() > day) {
						trClass += "purple";
					}
				} else {
					if (ticket.getStatus().equals(TicketStatus.POSTPONED)) {
						if (alternateColor) {
							trClass += "odd";
						} else {
							trClass += "even";
						}
						alternateColor = !alternateColor;
					} else {
						if (currentDate.getTime() - ticket.getLastActionDate().getTime() > week) {
							if (currentDate.getTime() - ticket.getLastActionDate().getTime() > month) {
								trClass += "red";
							} else {
								trClass += "yellow";
							}
						}
						else {
							if (alternateColor) {
								trClass += "odd";
							} else {
								trClass += "even";
							}
							alternateColor = !alternateColor;
						}
					}
				}
				String creationDepartmentLabel;
				if (ticket.getCreationDepartment() == null) {
					creationDepartmentLabel = "";
				} else {
					creationDepartmentLabel = StringUtils.escapeHtml(ticket.getCreationDepartment().getLabel());
				}
				String status = getI18nService().getString(TicketStatusI18nKeyProvider.getI18nKey(ticket.getStatus()),
						locale);
				String priority = getI18nService()
						.getString(PriorityI18nKeyProvider.getI18nKey(ticket.getEffectivePriorityLevel()), locale);
				String managerDisplayName;
				if (ticket.getManager() == null) {
					managerDisplayName = "&nbsp;";
				} else {
					managerDisplayName = StringUtils.escapeHtml(ticket.getManager().getDisplayName());
				}
				long idleTime = (System.currentTimeMillis() - ticket.getLastActionDate().getTime()) / 1000;
				result += getI18nService().getString("EMAIL.TICKET_REPORT.TICKET_TABLE_ENTRY", locale,
						new String[] { trClass,
								PriorityStyleClassProvider.getStyleClass(ticket.getEffectivePriorityLevel()),
								String.valueOf(ticket.getId()), creationDepartmentLabel,
								StringUtils.escapeHtml(ticket.getCategory().getLabel()),
								StringUtils.escapeHtml(ticket.getLabel()), status, priority,
								StringUtils.escapeHtml(ticket.getOwner().getDisplayName()), managerDisplayName,
								getUrlBuilder().getTicketViewUrl(user.getAuthType(), ticket.getId()),
								ElapsedTimeI18nFormatter.format(getI18nService(), idleTime, locale), });
				
			}
			result += getI18nService().getString("EMAIL.TICKET_REPORT.TICKET_TABLE_FOOTER", locale);
		}
		return result;
	}

	/**
	 * Compute tickets and manager to prepare reports.
	 * 
	 * @param manager
	 * @param openedTickets
	 * @return a map with reporting results.
	 */
	protected Map<String, List<Ticket>> computeReporting(final DepartmentManager manager,
			final List<Ticket> openedTickets) {
		User user = manager.getUser();
		String reportType = manager.getReportType();
		if (reportType == null) {
			return null;
		}
		boolean reportFreeTickets = !(reportType.equals(DepartmentManager.REPORT_M)
				|| reportType.equals(DepartmentManager.REPORT_MC));
		boolean reportCategoryMemberTickets = !(reportType.equals(DepartmentManager.REPORT_M)
				|| reportType.equals(DepartmentManager.REPORT_MF) || reportType.equals(DepartmentManager.REPORT_F)
				|| reportType.equals(DepartmentManager.REPORT_FM));
		boolean reportOtherTickets = reportType.equals(DepartmentManager.REPORT_MCFO)
				|| reportType.equals(DepartmentManager.REPORT_MFCO) || reportType.equals(DepartmentManager.REPORT_FMCO);
		boolean reportFreeBeforeCategoryMember = reportType.equals(DepartmentManager.REPORT_MF)
				|| reportType.equals(DepartmentManager.REPORT_MFC) || reportType.equals(DepartmentManager.REPORT_MFCO)
				|| reportType.equals(DepartmentManager.REPORT_FMC) || reportType.equals(DepartmentManager.REPORT_FMCO);

		List<Ticket> managedTickets = new ArrayList<Ticket>();
		List<Ticket> freeTickets = new ArrayList<Ticket>();
		List<Ticket> categoryMemberTickets = new ArrayList<Ticket>();
		List<Ticket> otherTickets = new ArrayList<Ticket>();
		for (Ticket ticket : openedTickets) {
			if (user.equals(ticket.getManager())) {
				managedTickets.add(ticket);
			} else if (reportFreeTickets || reportCategoryMemberTickets || reportOtherTickets) {
				if (reportFreeBeforeCategoryMember) {
					if (ticket.isFree()) {
						freeTickets.add(ticket);
					} else if (reportCategoryMemberTickets || reportOtherTickets) {
						if (getDomainService().isCategoryMember(ticket.getCategory(), user)) {
							categoryMemberTickets.add(ticket);
						} else if (reportOtherTickets) {
							otherTickets.add(ticket);
						}
					}
				} else {
					if (getDomainService().isCategoryMember(ticket.getCategory(), user)) {
						categoryMemberTickets.add(ticket);
					} else if (reportFreeTickets || reportOtherTickets) {
						if (ticket.isFree()) {
							freeTickets.add(ticket);
						} else if (reportOtherTickets) {
							otherTickets.add(ticket);
						}
					}
				}
			}
		}
		Map<String, List<Ticket>> computeReportingResults = new HashMap<String, List<Ticket>>();
		computeReportingResults.put(REPORT_MANAGED, managedTickets);
		if (reportFreeTickets) {
			computeReportingResults.put(REPORT_FREE, freeTickets);
		}
		if (reportCategoryMemberTickets) {
			computeReportingResults.put(REPORT_CATEGORY_MEMBER, categoryMemberTickets);
		}
		if (reportOtherTickets) {
			computeReportingResults.put(REPORT_OTHER, otherTickets);
		}
		return computeReportingResults;
	}

	/**
	 * @param manager
	 * @param computeReportingResults
	 * @return the HTML content for a department.
	 */
	protected String getDepartmentReportContent(final DepartmentManager manager,
			final Map<String, List<Ticket>> computeReportingResults) {
		User user = manager.getUser();
		String htmlContent = "";
		String reportType = manager.getReportType();
		boolean reportFreeBeforeCategoryMember = reportType.equals(DepartmentManager.REPORT_MF)
				|| reportType.equals(DepartmentManager.REPORT_MFC) || reportType.equals(DepartmentManager.REPORT_MFCO);

		boolean reportFreeOnly = reportType.equals(DepartmentManager.REPORT_F);

		boolean reportFreeBeforeManaged = reportType.equals(DepartmentManager.REPORT_FM)
				|| reportType.equals(DepartmentManager.REPORT_FMC) || reportType.equals(DepartmentManager.REPORT_FMCO);

		List<Ticket> managedTickets = computeReportingResults.get(REPORT_MANAGED);
		List<Ticket> freeTickets = computeReportingResults.get(REPORT_FREE);
		List<Ticket> categoryMemberTickets = computeReportingResults.get(REPORT_CATEGORY_MEMBER);
		List<Ticket> otherTickets = computeReportingResults.get(REPORT_OTHER);

		if (reportFreeOnly) {
			htmlContent += getTicketsReportContent(user, "EMAIL.TICKET_REPORT.NO_FREE_TICKET_SUBTITLE",
					"EMAIL.TICKET_REPORT.FREE_TICKETS_SUBTITLE", freeTickets, true);

		} else {
			if (reportFreeBeforeManaged) {
				htmlContent += getTicketsReportContent(user, "EMAIL.TICKET_REPORT.NO_FREE_TICKET_SUBTITLE",
						"EMAIL.TICKET_REPORT.FREE_TICKETS_SUBTITLE", freeTickets, true);
			}

			htmlContent += getTicketsReportContent(user, "EMAIL.TICKET_REPORT.NO_MANAGED_TICKET_SUBTITLE",
					"EMAIL.TICKET_REPORT.MANAGED_TICKETS_SUBTITLE", managedTickets, false);
			if (freeTickets != null || categoryMemberTickets != null) {
				if (reportFreeBeforeCategoryMember) {
					if (freeTickets != null) {
						htmlContent += getTicketsReportContent(user, "EMAIL.TICKET_REPORT.NO_FREE_TICKET_SUBTITLE",
								"EMAIL.TICKET_REPORT.FREE_TICKETS_SUBTITLE", freeTickets, true);
					}
					if (categoryMemberTickets != null) {
						htmlContent += getTicketsReportContent(user,
								"EMAIL.TICKET_REPORT.NO_CATEGORY_MEMBER_TICKET_SUBTITLE",
								"EMAIL.TICKET_REPORT.CATEGORY_MEMBER_TICKETS_SUBTITLE", categoryMemberTickets, false);
					}
				} else {
					if (categoryMemberTickets != null) {
						htmlContent += getTicketsReportContent(user,
								"EMAIL.TICKET_REPORT.NO_CATEGORY_MEMBER_TICKET_SUBTITLE",
								"EMAIL.TICKET_REPORT.CATEGORY_MEMBER_TICKETS_SUBTITLE", categoryMemberTickets, false);
					}
					if (freeTickets != null && !reportFreeBeforeManaged) {
						htmlContent += getTicketsReportContent(user, "EMAIL.TICKET_REPORT.NO_FREE_TICKET_SUBTITLE",
								"EMAIL.TICKET_REPORT.FREE_TICKETS_SUBTITLE", freeTickets, true);
					}
				}
			}
			if (otherTickets != null) {
				htmlContent += getTicketsReportContent(user, "EMAIL.TICKET_REPORT.NO_OTHER_TICKET_SUBTITLE",
						"EMAIL.TICKET_REPORT.OTHER_TICKETS_SUBTITLE", otherTickets, false);
			}
		}
		return htmlContent;
	}

	/**
	 * @param manager
	 * @param computeReportingResults
	 * @return the HTML content for a department.
	 */
	protected String getStatsDepartmentReportContent(final DepartmentManager manager,
			final Map<String, List<Ticket>> computeReportingResults) {
		User user = manager.getUser();
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String htmlContent = "";
		String reportType = manager.getReportType();
		boolean reportFreeBeforeCategoryMember = reportType.equals(DepartmentManager.REPORT_MF)
				|| reportType.equals(DepartmentManager.REPORT_MFC) || reportType.equals(DepartmentManager.REPORT_MFCO);

		boolean reportFreeOnly = reportType.equals(DepartmentManager.REPORT_F);

		boolean reportFreeBeforeManaged = reportType.equals(DepartmentManager.REPORT_FM)
				|| reportType.equals(DepartmentManager.REPORT_FMC) || reportType.equals(DepartmentManager.REPORT_FMCO);

		List<Ticket> managedTickets = computeReportingResults.get(REPORT_MANAGED);
		List<Ticket> freeTickets = computeReportingResults.get(REPORT_FREE);
		List<Ticket> categoryMemberTickets = computeReportingResults.get(REPORT_CATEGORY_MEMBER);
		List<Ticket> otherTickets = computeReportingResults.get(REPORT_OTHER);

		htmlContent += getI18nService().getString("EMAIL.TICKET_REPORT.STATS.SUBTITLE", locale);
		if (reportFreeOnly) {
			htmlContent += getI18nService().getString("EMAIL.TICKET_REPORT.STATS.FREE_TICKETS_NUMBER", locale,
					freeTickets.size());

		} else {
			if (reportFreeBeforeManaged) {
				htmlContent += getI18nService().getString("EMAIL.TICKET_REPORT.STATS.FREE_TICKETS_NUMBER", locale,
						freeTickets.size());
			}
			htmlContent += getI18nService().getString("EMAIL.TICKET_REPORT.STATS.MANAGED_TICKETS_NUMBER", locale,
					managedTickets.size());
			if (freeTickets != null || categoryMemberTickets != null) {
				if (reportFreeBeforeCategoryMember) {
					if (freeTickets != null) {
						htmlContent += getI18nService().getString("EMAIL.TICKET_REPORT.STATS.FREE_TICKETS_NUMBER",
								locale, freeTickets.size());
					}
					if (categoryMemberTickets != null) {
						htmlContent += getI18nService().getString(
								"EMAIL.TICKET_REPORT.STATS.CATEGORY_MEMBER_TICKETS_NUMBER", locale,
								categoryMemberTickets.size());
					}
				} else {
					if (categoryMemberTickets != null) {
						htmlContent += getI18nService().getString(
								"EMAIL.TICKET_REPORT.STATS.CATEGORY_MEMBER_TICKETS_NUMBER", locale,
								categoryMemberTickets.size());
					}
					if (freeTickets != null) {
						htmlContent += getI18nService().getString("EMAIL.TICKET_REPORT.STATS.FREE_TICKETS_NUMBER",
								locale, freeTickets.size());
					}
				}
			}
			if (otherTickets != null) {
				htmlContent += getI18nService().getString("EMAIL.TICKET_REPORT.STATS.OTHER_TICKETS_NUMBER", locale,
						otherTickets.size());
			}
		}
		return htmlContent;
	}

	/**
	 * Send a report to a department manager (for a department).
	 * 
	 * @param manager
	 * @param openedTickets
	 */
	protected void sendTicketReport(final DepartmentManager manager, final List<Ticket> openedTickets) {
		User user = manager.getUser();
		Department department = manager.getDepartment();
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String subject = getI18nService().getString("EMAIL.TICKET_REPORT.DEPARTMENT_SUBJECT", locale,
				department.getLabel());
		String htmlContent = getI18nService().getString("EMAIL.TICKET_REPORT.DEPARTMENT_TITLE", locale,
				department.getLabel());
		Map<String, List<Ticket>> computeReportingResults = computeReporting(manager, openedTickets);
		htmlContent += "<table width=\"100%\"><tr><td valign=\"top\">";
		htmlContent += getStatsDepartmentReportContent(manager, computeReportingResults);
		htmlContent += getDepartmentReportContent(manager, computeReportingResults);
		htmlContent += "</td><td valign=\"top\">";
		htmlContent += getEmailQuickLinks(AuthUtils.NONE, user, null);
		htmlContent += "</td></tr></table>";
		htmlContent += getI18nService().getString("EMAIL.TICKET_REPORT.FOOTER", locale,
				getUrlBuilder().getManagerPreferencesUrl(user.getAuthType()),
				getUrlBuilder().getToggleTicketReportsUrl(user.getAuthType()));
		send(user, genMessageId(), subject, htmlContent);
	}

	/**
	 * Send a ticket report to a user (for one or more departments).
	 * 
	 * @param hour
	 * @param weekend
	 * @param user
	 * @param openedTicketsMap
	 */
	protected void sendTicketReport(final int hour, final boolean weekend, final User user,
			final Map<Department, List<Ticket>> openedTicketsMap) {
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String subject = getI18nService().getString("EMAIL.TICKET_REPORT.SUBJECT", locale);
		String htmlContent = getI18nService().getString("EMAIL.TICKET_REPORT.ALL_TITLE", locale);
		htmlContent += "<table width=\"100%\"><tr><td valign=\"top\">";
		for (Department department : getDomainService().getDepartments()) {
			try {
				DepartmentManager manager = getDomainService().getDepartmentManager(department, user);
				if (isReportingManager(hour, weekend, manager)) {
					htmlContent += getI18nService().getString("EMAIL.TICKET_REPORT.DEPARTMENT_HEADER", locale,
							department.getLabel());
					Map<String, List<Ticket>> computeReportingResults = computeReporting(manager,
							openedTicketsMap.get(department));
					htmlContent += getStatsDepartmentReportContent(manager, computeReportingResults);
					htmlContent += getDepartmentReportContent(manager, computeReportingResults);
				}
			} catch (DepartmentManagerNotFoundException e) {
				// not managing this department
			}
		}
		htmlContent += "</td><td valign=\"top\">";
		htmlContent += getEmailQuickLinks(AuthUtils.NONE, user, null);
		htmlContent += "</td></tr></table>";
		htmlContent += getI18nService().getString("EMAIL.TICKET_REPORT.FOOTER", locale,
				getUrlBuilder().getManagerPreferencesUrl(user.getAuthType()),
				getUrlBuilder().getToggleTicketReportsUrl(user.getAuthType()));
		send(user, genMessageId(), subject, htmlContent);
	}

	/**
	 * @param hour
	 * @param weekend
	 * @param manager
	 * @return true if the manager receives reports for the given hour.
	 */
	protected boolean isReportingManager(final int hour, final boolean weekend, final DepartmentManager manager) {
		if (!manager.getUser().getReceiveTicketReports()) {
			return false;
		}
		if (weekend && !manager.getReportWeekend()) {
			return false;
		}
		if (manager.getReportType() == null) {
			return false;
		}
		return hour == manager.getReportTime1() || hour == manager.getReportTime2();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.TicketReporter#sendTicketReport(org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public void sendTicketReport(final DepartmentManager manager) {
		sendTicketReport(manager, getDomainService().getOpenedTicketsByLastActionDate(manager.getDepartment()));
	}

	/**
	 * Send ticket reports for an hour.
	 * 
	 * @param hour
	 * @param weekend
	 */
	protected void sendTicketReports(final int hour, final boolean weekend) {
		List<User> users = new ArrayList<User>();
		Map<Department, List<Ticket>> openedTicketsMap = new HashMap<Department, List<Ticket>>();
		for (Department department : getDomainService().getDepartments()) {
			for (DepartmentManager manager : getDomainService().getDepartmentManagers(department)) {
				boolean userFound = false;
				if (isReportingManager(hour, weekend, manager)) {
					userFound = true;
					if (!users.contains(manager.getUser())) {
						users.add(manager.getUser());
					}
				}
				if (userFound) {
					openedTicketsMap.put(department, getDomainService().getOpenedTicketsByLastActionDate(department));
				}
			}
		}
		for (User user : users) {
			if (user.getReceiveTicketReportsAllInOne()) {
				sendTicketReport(hour, weekend, user, openedTicketsMap);
			} else {
				for (Department department : getDomainService().getDepartments()) {
					try {
						DepartmentManager manager = getDomainService().getDepartmentManager(department, user);
						if (isReportingManager(hour, weekend, manager)) {
							sendTicketReport(manager, openedTicketsMap.get(department));
						}
					} catch (DepartmentManagerNotFoundException e) {
						// not managing this department
					}
				}
			}
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.TicketReporter#sendTicketReports()
	 */
	@Override
	public void sendTicketReports() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		int hour = StatisticsUtils.getHour(now);
		int dow = StatisticsUtils.getDayOfWeek(now);
		boolean weekend = dow == Calendar.SATURDAY || dow == Calendar.SUNDAY;
		sendTicketReports(hour, weekend);
	}

}
