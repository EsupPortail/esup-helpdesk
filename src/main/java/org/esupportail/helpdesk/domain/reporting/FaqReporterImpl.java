/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.FaqEvent;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.exceptions.FaqNotFoundException;

/**
 * The basic implementation of FaqReporter.
 */
public class FaqReporterImpl extends AbstractSenderFormatter implements FaqReporter {

	/**
	 * Bean constructor.
	 */
	public FaqReporterImpl() {
		super();
	}

	/**
	 * @param events
	 * @return a map that contains all the FAQ events to print on reports sorted by department.
	 */
	protected Map<Department, List<FaqEvent>> getEventsByDepartment(final List<FaqEvent> events) {
		Map<Department, List<FaqEvent>> result = new HashMap<Department, List<FaqEvent>>();
		for (FaqEvent event : events) {
			Department department = event.getDepartment();
			if (department != null) {
				List<FaqEvent> departmentEvents = result.get(department);
				if (departmentEvents == null) {
					departmentEvents = new ArrayList<FaqEvent>();
					result.put(department, departmentEvents);
				}
				boolean found = false;
				long faqId = event.getFaqId();
				for (FaqEvent e : departmentEvents) {
					if (faqId == e.getFaqId()) {
						found = true;
						break;
					}
				}
				if (!found) {
					departmentEvents.add(event);
				}
			}
		}
		return result;
	}

	/**
	 * @param events
	 * @return the events to print on reports.
	 */
	protected List<FaqEvent> getRootEvents(final List<FaqEvent> events) {
		List<FaqEvent> result = new ArrayList<FaqEvent>();
		for (FaqEvent event : events) {
			Department department = event.getDepartment();
			if (department == null) {
				boolean found = false;
				long faqId = event.getFaqId();
				for (FaqEvent e : result) {
					if (faqId == e.getFaqId()) {
						found = true;
						break;
					}
				}
				if (!found) {
					result.add(event);
				}
			}
		}
		return result;
	}

	/**
	 * @return a map that contains all the managers.
	 */
	protected Map<User, List<Department>> getUserMap() {
		Map<User, List<Department>> map = new HashMap<User, List<Department>>();
		for (User user : getDomainService().getAdmins()) {
			if (user.getReceiveFaqReports()) {
				List<Department> departments = new ArrayList<Department>();
				map.put(user, departments);
				departments.add(null);
			}
		}
		for (Department department : getDomainService().getEnabledDepartments()) {
			for (DepartmentManager departmentManager
					: getDomainService().getDepartmentManagers(department)) {
				User user = departmentManager.getUser();
				if (user.getReceiveFaqReports()) {
					List<Department> departments = map.get(user);
					if (departments == null) {
						departments = new ArrayList<Department>();
						map.put(user, departments);
					}
					departments.add(departmentManager.getDepartment());
				}
			}
		}
		return map;
	}

	/**
	 * @param user
	 * @param department
	 * @param departmentEvents
	 * @return the report content that corresponds to a department.
	 */
	protected String getDepartmentReportContent(
			final User user,
			final Department department,
			final List<FaqEvent> departmentEvents) {
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String content;
		if (department == null) {
			content = getI18nService().getString(
				"EMAIL.FAQ_REPORT.ROOT_HEADER", locale);
		} else {
			content = getI18nService().getString(
					"EMAIL.FAQ_REPORT.DEPARTMENT_HEADER", locale, department.getLabel());
		}
		for (FaqEvent event : departmentEvents) {
			Faq faq = null;
			String faqLabel = null;
			try {
				faq = getDomainService().getFaq(event.getFaqId());
				faqLabel = faq.getLabel();
			} catch (FaqNotFoundException e) {
				faqLabel = event.getLabel();
			}
			String i18nKey = "EMAIL.FAQ_REPORT." + event.getAction();
			String departmentLabel = null;
			if (FaqEvent.MOVE_FROM.equals(event.getAction())) {
				if (event.getFromDepartment() == null) {
					i18nKey += "_NULL";
				} else {
					departmentLabel = event.getFromDepartment().getLabel();
				}
			} else if (FaqEvent.MOVE_TO.equals(event.getAction())) {
				if (event.getToDepartment() == null) {
					i18nKey += "_ROOT";
				} else {
					departmentLabel = event.getToDepartment().getLabel();
				}
			}
			if (faq == null) {
				i18nKey += "_NO_LINK";
			}
			String author = null;
			if (event.getAuthor() != null) {
				author = getUserFormattingService().format(null, null, event.getAuthor(), false, locale, null);
			}
			content += "<li>" + getI18nService().getString(
					i18nKey, locale,
					getUrlBuilder().getFaqsUrl(user.getAuthType(), faq),
					StringUtils.escapeHtml(faqLabel),
					StringUtils.escapeHtml(author),
					StringUtils.escapeHtml(departmentLabel)) + "</li>";
		}
		return content;
	}

	/**
	 * Send a FAQ report to a user.
	 * @param user
	 * @param reportContent
	 */
	protected void sendFaqReport(
			final User user,
			final String reportContent) {
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String subject = getI18nService().getString(
				"EMAIL.FAQ_REPORT.SUBJECT", locale);
		String htmlContent = getI18nService().getString(
				"EMAIL.FAQ_REPORT.TITLE", locale);
		htmlContent += "<table width=\"100%\"><tr><td valign=\"top\">";
		htmlContent += reportContent;
		htmlContent += "</td><td valign=\"top\">";
		htmlContent += getEmailQuickLinks(AuthUtils.NONE, user, null);
		htmlContent += "</td></tr></table>";
		htmlContent += getI18nService().getString(
				"EMAIL.FAQ_REPORT.FOOTER", locale,
				getUrlBuilder().getManagerPreferencesUrl(user.getAuthType()),
				getUrlBuilder().getToggleFaqReportsUrl(user.getAuthType()));
		send(user, genMessageId(), subject, htmlContent);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.FaqReporter#sendFaqReports()
	 */
	@Override
	public void sendFaqReports() {
		List<FaqEvent> events = getDomainService().getFaqEvents();
		List<FaqEvent> rootEvents = getRootEvents(events);
		Map<Department, List<FaqEvent>> eventsByDepartment = getEventsByDepartment(events);
		Map<User, List<Department>> userMap = getUserMap();
		for (User user : userMap.keySet()) {
			String reportContent = "";
			boolean send = false;
			for (Department department : userMap.get(user)) {
				List<FaqEvent> departmentEvents;
				if (department == null) {
					departmentEvents = rootEvents;
				} else {
					departmentEvents = eventsByDepartment.get(department);
				}
				if (departmentEvents != null && !departmentEvents.isEmpty()) {
					send = true;
					reportContent += getDepartmentReportContent(
							user, department, departmentEvents);
				}
			}
			if (send) {
				sendFaqReport(user, reportContent);
			}
		}
		for (FaqEvent event : events) {
			getDomainService().deleteFaqEvent(event);
		}
	}

}
