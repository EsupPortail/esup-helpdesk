/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;

import org.esupportail.commons.utils.strings.StringUtils;


/**
 * A class to store department managers. */
public class DepartmentManager implements Serializable, Comparable<DepartmentManager> {

	/**
	 * a constant for ticket monitoring.
	 */
	public static final int TICKET_MONITORING_ALWAYS = 0;

	/**
	 * a constant for ticket monitoring.
	 */
	public static final int TICKET_MONITORING_CREATION = 1;

	/**
	 * a constant for ticket monitoring.
	 */
	public static final int TICKET_MONITORING_NEVER = 2;

	/**
	 * a constant for reports.
	 */
	public static final String REPORT_NONE = null;

	/**
	 * a constant for reports.
	 */
	public static final String REPORT_M = "M";
	
	/**
	 * a constant for reports.
	 */
	public static final String REPORT_MC = "MC";

	/**
	 * a constant for reports.
	 */
	public static final String REPORT_MF = "MF";

	/**
	 * a constant for reports.
	 */
	public static final String REPORT_MCF = "MCF";
	
	/**
	 * a constant for reports.
	 */
	public static final String REPORT_MFC = "MFC";
	
	/**
	 * a constant for reports.
	 */
	public static final String REPORT_MCFO = "MCFO";

	/**
	 * a constant for reports.
	 */
	public static final String REPORT_MFCO = "MFCO";

	/**
	 * a constant for reports.
	 */
	public static final String REPORT_F = "F";
	

	/**
	 * a constant for reports.
	 */
	public static final String REPORT_FM = "FM";
	
	/**
	 * a constant for reports.
	 */
	public static final String REPORT_FMC = "FMC";
	
	/**
	 * a constant for reports.
	 */
	public static final String REPORT_FMCO = "FMCO";
	
	
	/**
	 * The minimum rate.
	 */
	public static final int MIN_RATE = 0;

	/**
	 * The maximum rate.
	 */
	public static final int MAX_RATE = 100;

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4522276503661909722L;
	
    /**
     * Unique id.
     */
    private long id;

    /**
     * The user.
     */
    private User user;

    /**
     * The department.
     */
    private Department department;

    /**
     * True if the manager is available.
     */
    private Boolean available;

    /**
     * True if the manager can refuse tickets.
     */
    private Boolean refuseTicket;

    /**
     * True if the manager can take already assigned tickets.
     */
    private Boolean takeAlreadyAssignedTicket;

    /**
     * True if the manager can take free tickets.
     */
    private Boolean takeFreeTicket;

    /**
     * True if the manager can assign already assigned tickets.
     */
    private Boolean assignTicket;

    /**
     * True if the manager can manage the properties of the department.
     */
    private Boolean manageProperties;

    /**
     * True if the manager can manage the managers.
     */
    private Boolean manageManagers;

    /**
     * True if the manager can manage the categories.
     */
    private Boolean manageCategories;

    /**
     * True if the manager can manage the FAQs.
     */
    private Boolean manageFaq;

    /**
     * True if the manager can change to change the department of a ticket.
     */
    private Boolean modifyTicketDepartment;

    /**
     * Rate.
     */
    private int rate;

    /**
     * True if the manager can set his own availability.
     */
    private Boolean setOwnAvailability;
    
    /**
     * True if the manager can reopen tickets (s)he does not manage.
     */
    private Boolean reopenAllTickets;

    /**
     * The order of this department manager (in the department).
     */
    private Integer order;

    /** A priority level for ticket monitoring. */
    private Integer ticketMonitoringAny;
    /** A priority level for ticket monitoring. */
    private Integer ticketMonitoringCategory;
    /** A priority level for ticket monitoring. */
    private Integer ticketMonitoringManaged;
    
    /**
     * The report type.
     */
    private String reportType;
    
    /**
     * The time for the first report. 
     */
    private Integer reportTime1;

    /**
     * The time for the second report. 
     */
    private Integer reportTime2;

    /**
     * True to report on the week-end.
     */
    private Boolean reportWeekend;
    
    /**
	 * Bean constructor.
	 */
	public DepartmentManager() {
		super();
	}

	/**
	 * Bean constructor.
	 * @param user
	 * @param department
	 */
	public DepartmentManager(
			final User user, 
			final Department department) {
		super();
		this.user = user;
		this.department = department;
		this.available = false;
		this.refuseTicket = false;
		this.takeAlreadyAssignedTicket = false;
		this.takeFreeTicket = false;
		this.assignTicket = false;
		this.manageProperties = false;
		this.manageManagers = false;
		this.manageCategories = false;
		this.manageFaq = false;
		this.modifyTicketDepartment = false;
		this.rate = MAX_RATE;
		this.setOwnAvailability = false;
		this.reopenAllTickets = false;
		this.order = -1;
		this.reportType = null;
}

	/**
	 * Copy.
	 * @param dm the department manager to copy.
	 */
	public DepartmentManager(
			final DepartmentManager dm) {
		super();
		this.id = dm.id;
		this.user = dm.user;
		this.department = dm.department;
		this.available = dm.available;
		this.refuseTicket = dm.refuseTicket;
		this.takeAlreadyAssignedTicket = dm.takeAlreadyAssignedTicket;
		this.takeFreeTicket = dm.takeFreeTicket;
		this.assignTicket = dm.assignTicket;
		this.manageProperties = dm.manageProperties;
		this.manageManagers = dm.manageManagers;
		this.manageCategories = dm.manageCategories;
		this.manageFaq = dm.manageFaq;
		this.modifyTicketDepartment = dm.modifyTicketDepartment;
		this.rate = dm.rate;
		this.setOwnAvailability = dm.setOwnAvailability;
		this.reopenAllTickets = dm.reopenAllTickets;
		this.order = dm.order;
		this.ticketMonitoringAny = dm.ticketMonitoringAny;
		this.ticketMonitoringCategory = dm.ticketMonitoringCategory;
		this.ticketMonitoringManaged = dm.ticketMonitoringManaged;
		this.reportType = dm.reportType;
		this.reportTime1 = dm.reportTime1;
		this.reportTime2 = dm.reportTime2;
		this.reportWeekend = dm.reportWeekend;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DepartmentManager)) {
			return false;
		}
		return ((DepartmentManager) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = getClass().getSimpleName() + "#" + hashCode() + "["
		+ "id=" + id + ""
		+ ", order=[" + order + "]";
		if (department != null) {
			result += ", department=[" + department.getLabel() + "]";
		} else {
			result += ", department=null";
		}
		if (user != null) {
			result += ", user=[" + user.getId() + "]";
		} else {
			result += ", user=null";
		}
		result += ", manageCategories=" + manageCategories + ""
		+ ", manageFaq=" + manageFaq + ""
		+ ", manageManagers=" + manageManagers + ""
		+ ", manageProperties=" + manageProperties + ""
		+ ", ticketMonitoringAny=" + ticketMonitoringAny + ""
		+ ", ticketMonitoringCategory=" + ticketMonitoringCategory + ""
		+ ", ticketMonitoringManaged=" + ticketMonitoringManaged + ""
		+ ", reportType=[" + reportType + "]"
		+ ", reportTime1=[" + reportTime1 + "]"
		+ ", reportTime2=[" + reportTime2 + "]"
		+ ", reportWeekend=[" + reportWeekend + "]"
		+ "]";
		return result;
	}
	
	/**
	 * Check the coherence of ticket monitoring values.
	 */
	public void checkTicketMonitoringValues() {
		switch (ticketMonitoringAny) {
		case TICKET_MONITORING_ALWAYS:
		case TICKET_MONITORING_CREATION:
		case TICKET_MONITORING_NEVER:
			break;
		default:
			throw new UnsupportedOperationException();
		}
		switch (ticketMonitoringCategory) {
		case TICKET_MONITORING_ALWAYS:
		case TICKET_MONITORING_CREATION:
		case TICKET_MONITORING_NEVER:
			break;
		default:
			throw new UnsupportedOperationException();
		}
		switch (ticketMonitoringCategory) {
		case TICKET_MONITORING_ALWAYS:
		case TICKET_MONITORING_CREATION:
		case TICKET_MONITORING_NEVER:
			break;
		default:
			throw new UnsupportedOperationException();
		}
		if (ticketMonitoringCategory > ticketMonitoringAny) {
			ticketMonitoringCategory = ticketMonitoringAny;
		}
		if (ticketMonitoringManaged > ticketMonitoringCategory) {
			ticketMonitoringManaged = ticketMonitoringCategory;
		}
	}

	/**
	 * @return the assignTicket
	 */
	public Boolean getAssignTicket() {
		return assignTicket;
	}

	/**
	 * @param assignTicket the assignTicket to set
	 */
	public void setAssignTicket(final Boolean assignTicket) {
		this.assignTicket = assignTicket;
	}

	/**
	 * @return the available
	 */
	public Boolean getAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(final Boolean available) {
		this.available = available;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(final Department department) {
		this.department = department;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @return the manageCategories
	 */
	public Boolean getManageCategories() {
		return manageCategories;
	}

	/**
	 * @param manageCategories the manageCategories to set
	 */
	public void setManageCategories(final Boolean manageCategories) {
		this.manageCategories = manageCategories;
	}

	/**
	 * @return the manageFaq
	 */
	public Boolean getManageFaq() {
		return manageFaq;
	}

	/**
	 * @param manageFaq the manageFaq to set
	 */
	public void setManageFaq(final Boolean manageFaq) {
		this.manageFaq = manageFaq;
	}

	/**
	 * @return the manageManagers
	 */
	public Boolean getManageManagers() {
		return manageManagers;
	}

	/**
	 * @param manageManagers the manageManagers to set
	 */
	public void setManageManagers(final Boolean manageManagers) {
		this.manageManagers = manageManagers;
	}

	/**
	 * @return the manageProperties
	 */
	public Boolean getManageProperties() {
		return manageProperties;
	}

	/**
	 * @param manageProperties the manageProperties to set
	 */
	public void setManageProperties(final Boolean manageProperties) {
		this.manageProperties = manageProperties;
	}

	/**
	 * @return the manageResponses
	 */
	public boolean getManageResponses() {
		return getManageProperties();
	}

	/**
	 * @return the modifyTicketDepartment
	 */
	public Boolean getModifyTicketDepartment() {
		return modifyTicketDepartment;
	}

	/**
	 * @param modifyTicketDepartment the modifyTicketDepartment to set
	 */
	public void setModifyTicketDepartment(final Boolean modifyTicketDepartment) {
		this.modifyTicketDepartment = modifyTicketDepartment;
	}

	/**
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(final Integer order) {
		this.order = order;
	}

	/**
	 * @return the rate
	 */
	public int getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(final int rate) {
		this.rate = rate;
	}

	/**
	 * @return the refuseTicket
	 */
	public Boolean getRefuseTicket() {
		return refuseTicket;
	}

	/**
	 * @param refuseTicket the refuseTicket to set
	 */
	public void setRefuseTicket(final Boolean refuseTicket) {
		this.refuseTicket = refuseTicket;
	}

	/**
	 * @return the reopenAllTickets
	 */
	public Boolean getReopenAllTickets() {
		return reopenAllTickets;
	}

	/**
	 * @param reopenAllTickets the reopenAllTickets to set
	 */
	public void setReopenAllTickets(final Boolean reopenAllTickets) {
		this.reopenAllTickets = reopenAllTickets;
	}

	/**
	 * @return the setOwnAvailability
	 */
	public Boolean getSetOwnAvailability() {
		return setOwnAvailability;
	}

	/**
	 * @param setOwnAvailability the setOwnAvailability to set
	 */
	public void setSetOwnAvailability(final Boolean setOwnAvailability) {
		this.setOwnAvailability = setOwnAvailability;
	}

	/**
	 * @return the takeAlreadyAssignedTicket
	 */
	public Boolean getTakeAlreadyAssignedTicket() {
		return takeAlreadyAssignedTicket;
	}

	/**
	 * @param takeAlreadyAssignedTicket the takeAlreadyAssignedTicket to set
	 */
	public void setTakeAlreadyAssignedTicket(final Boolean takeAlreadyAssignedTicket) {
		this.takeAlreadyAssignedTicket = takeAlreadyAssignedTicket;
	}

	/**
	 * @return the takeFreeTicket
	 */
	public Boolean getTakeFreeTicket() {
		return takeFreeTicket;
	}

	/**
	 * @param takeFreeTicket the takeFreeTicket to set
	 */
	public void setTakeFreeTicket(final Boolean takeFreeTicket) {
		this.takeFreeTicket = takeFreeTicket;
	}

	/**
	 * @return the ticketMonitoringAny
	 */
	public Integer getTicketMonitoringAny() {
		return ticketMonitoringAny;
	}

	/**
	 * @param ticketMonitoringAny the ticketMonitoringAny to set
	 */
	public void setTicketMonitoringAny(final Integer ticketMonitoringAny) {
		this.ticketMonitoringAny = ticketMonitoringAny;
	}

	/**
	 * @return the ticketMonitoringCategory
	 */
	public Integer getTicketMonitoringCategory() {
		return ticketMonitoringCategory;
	}

	/**
	 * @param ticketMonitoringCategory the ticketMonitoringCategory to set
	 */
	public void setTicketMonitoringCategory(final Integer ticketMonitoringCategory) {
		this.ticketMonitoringCategory = ticketMonitoringCategory;
	}

	/**
	 * @return the ticketMonitoringManaged
	 */
	public Integer getTicketMonitoringManaged() {
		return ticketMonitoringManaged;
	}

	/**
	 * @param ticketMonitoringManaged the ticketMonitoringManaged to set
	 */
	public void setTicketMonitoringManaged(final Integer ticketMonitoringManaged) {
		this.ticketMonitoringManaged = ticketMonitoringManaged;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
	}

	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(final String reportType) {
		this.reportType = StringUtils.nullIfEmpty(reportType);
	}

	/**
	 * @return the reportTime1
	 */
	public Integer getReportTime1() {
		if (reportTime1 == null) {
			return -1;
		}
		return reportTime1;
	}

	/**
	 * @param reportTime1 the reportTime1 to set
	 */
	public void setReportTime1(final Integer reportTime1) {
		if (reportTime1 == null || reportTime1 < 0) {
			this.reportTime1 = null;
		} else {
			this.reportTime1 = reportTime1;
		}
	}

	/**
	 * @return the reportTime2
	 */
	public Integer getReportTime2() {
		if (reportTime2 == null) {
			return -1;
		}
		return reportTime2;
	}

	/**
	 * @param reportTime2 the reportTime2 to set
	 */
	public void setReportTime2(final Integer reportTime2) {
		if (reportTime2 == null || reportTime2 < 0) {
			this.reportTime2 = null;
		} else {
			this.reportTime2 = reportTime2;
		}
	}

	/**
	 * @return the reportWeekend
	 */
	public Boolean getReportWeekend() {
		if (reportWeekend == null) {
			return false;
		}
		return reportWeekend;
	}

	/**
	 * @param reportWeekend the reportWeekend to set
	 */
	public void setReportWeekend(final Boolean reportWeekend) {
		this.reportWeekend = reportWeekend;
	}

	@Override
	public int compareTo(DepartmentManager departmentManager) {
		// TODO Auto-generated method stub
		return this.user.getDisplayName().compareTo(departmentManager.getUser().getDisplayName());
	}

}
