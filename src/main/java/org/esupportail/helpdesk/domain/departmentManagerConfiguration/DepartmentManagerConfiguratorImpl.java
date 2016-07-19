/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentManagerConfiguration;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.springframework.beans.factory.InitializingBean;


/**
 * A basic implementation of DepartmentManagerConfigurator.
 */
public class DepartmentManagerConfiguratorImpl 
implements DepartmentManagerConfigurator, InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6317690649012607580L;

	/**
	 * The default rate.
	 */
	private static final int DEFAULT_RATE = 100;

    /**
     * True if new managers are available.
     */
    private boolean available;

    /**
     * True if new managers can manage the FAQs.
     */
    private boolean manageFaq;

    /**
     * True if new managers can refuse tickets.
     */
    private boolean refuseTicket;

    /**
     * True if new managers can take already assigned tickets.
     */
    private boolean takeAlreadyAssignedTicket;

    /**
     * True if new managers can take free tickets.
     */
    private boolean takeFreeTicket;

    /**
     * True if new managers can assign already assigned tickets.
     */
    private boolean assignTicket;

    /**
     * True if new managers can manage the managers.
     */
    private boolean manageProperties;

    /**
     * True if new managers can manage the managers.
     */
    private boolean manageManagers;

    /**
     * True if new managers can manage the categories.
     */
    private boolean manageCategories;

    /**
     * True if new managers can change to change the department of a ticket.
     */
    private boolean modifyTicketDepartment;

    /**
     * The rate for new managers.
     */
    private int rate;

    /**
     * True if new managers can set their own availability.
     */
    private Boolean setOwnAvailability;
    
    /**
     * True if new managers can reopen tickets they do not manage.
     */
    private Boolean reopenAllTickets;

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
     * The time of the first report sent.
     */
    private Integer reportTime1; 

    /**
     * The time of the second report sent.
     */
    private Integer reportTime2;

    /**
     * True to send reports even on the week-end.
     */
    private Boolean reportWeekend;

    /**
     * Bean constructor.
     */
    public DepartmentManagerConfiguratorImpl() {
		super();
		this.available = true;
		this.manageFaq = true;
		this.refuseTicket = true;
		this.takeAlreadyAssignedTicket = true;
		this.takeFreeTicket = true;
		this.assignTicket = true;
		this.manageProperties = true;
		this.manageManagers = true;
		this.manageCategories = true;
		this.modifyTicketDepartment = true;
		this.rate = DEFAULT_RATE;
		this.setOwnAvailability = true;
		this.reopenAllTickets = true;
		this.ticketMonitoringAny = null;
		this.ticketMonitoringCategory = null;
		this.ticketMonitoringManaged = null;
		reportType = null;
		reportTime1 = null;
		reportTime2 = null;
	}
    
    /**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Integer [] allowedValues = new Integer[] {
				DepartmentManager.TICKET_MONITORING_ALWAYS, 
				DepartmentManager.TICKET_MONITORING_CREATION,
				DepartmentManager.TICKET_MONITORING_NEVER,
		};
		Assert.contains(allowedValues, "ticketMonitoringAny", ticketMonitoringAny);
		Assert.contains(allowedValues, "ticketMonitoringCategory", ticketMonitoringCategory);
		Assert.contains(allowedValues, "ticketMonitoringManaged", ticketMonitoringManaged);
		if (ticketMonitoringCategory > ticketMonitoringAny) {
			ticketMonitoringCategory = ticketMonitoringAny;
		}
		if (ticketMonitoringManaged > ticketMonitoringCategory) {
			ticketMonitoringManaged = ticketMonitoringCategory;
		}
		if (reportType != null
				&& !DepartmentManager.REPORT_M.equals(reportType)
				&& !DepartmentManager.REPORT_MC.equals(reportType)
				&& !DepartmentManager.REPORT_MF.equals(reportType)
				&& !DepartmentManager.REPORT_MCF.equals(reportType)
				&& !DepartmentManager.REPORT_MFC.equals(reportType)
				&& !DepartmentManager.REPORT_MCFO.equals(reportType)
				&& !DepartmentManager.REPORT_MFCO.equals(reportType)) {
			throw new ConfigException(
					getClass() + ": invalid value for property reportType (" + reportType + ")");
		}
	}

	/**
     * @see org.esupportail.helpdesk.domain.departmentManagerConfiguration.DepartmentManagerConfigurator
     * #configureTicketMonitoring(
     * org.esupportail.helpdesk.domain.beans.DepartmentManager)
     */
    @Override
	public void configureTicketMonitoring(final DepartmentManager departmentManager) {
    	departmentManager.setTicketMonitoringAny(ticketMonitoringAny);
    	departmentManager.setTicketMonitoringCategory(ticketMonitoringCategory);
    	departmentManager.setTicketMonitoringManaged(ticketMonitoringManaged);
    }

	/**
     * @see org.esupportail.helpdesk.domain.departmentManagerConfiguration.DepartmentManagerConfigurator#configure(
     * org.esupportail.helpdesk.domain.beans.DepartmentManager)
     */
    @Override
	public void configure(final DepartmentManager departmentManager) {
    	departmentManager.setAvailable(available);
    	departmentManager.setManageFaq(manageFaq);
    	departmentManager.setRefuseTicket(refuseTicket);
    	departmentManager.setTakeAlreadyAssignedTicket(takeAlreadyAssignedTicket);
    	departmentManager.setTakeFreeTicket(takeFreeTicket);
    	departmentManager.setAssignTicket(assignTicket);
    	departmentManager.setManageProperties(manageProperties);
    	departmentManager.setManageManagers(manageManagers);
    	departmentManager.setManageCategories(manageCategories);
    	departmentManager.setModifyTicketDepartment(modifyTicketDepartment);
    	departmentManager.setRate(rate);
    	departmentManager.setSetOwnAvailability(setOwnAvailability);
    	departmentManager.setReopenAllTickets(reopenAllTickets);
    	departmentManager.setReportType(reportType);
    	departmentManager.setReportTime1(reportTime1);
    	departmentManager.setReportTime2(reportTime2);
    	departmentManager.setReportWeekend(reportWeekend);
    	configureTicketMonitoring(departmentManager);
    }

	/**
	 * @param assignTicket the assignTicket to set
	 */
	public void setAssignTicket(final boolean assignTicket) {
		this.assignTicket = assignTicket;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(final boolean available) {
		this.available = available;
	}

	/**
	 * @param manageCategories the manageCategories to set
	 */
	public void setManageCategories(final boolean manageCategories) {
		this.manageCategories = manageCategories;
	}

	/**
	 * @param manageFaq the manageFaq to set
	 */
	public void setManageFaq(final boolean manageFaq) {
		this.manageFaq = manageFaq;
	}

	/**
	 * @param manageManagers the manageManagers to set
	 */
	public void setManageManagers(final boolean manageManagers) {
		this.manageManagers = manageManagers;
	}

	/**
	 * @param modifyTicketDepartment the modifyTicketDepartment to set
	 */
	public void setModifyTicketDepartment(final boolean modifyTicketDepartment) {
		this.modifyTicketDepartment = modifyTicketDepartment;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(final int rate) {
		this.rate = rate;
	}

	/**
	 * @param refuseTicket the refuseTicket to set
	 */
	public void setRefuseTicket(final boolean refuseTicket) {
		this.refuseTicket = refuseTicket;
	}

	/**
	 * @param reopenAllTickets the reopenAllTickets to set
	 */
	public void setReopenAllTickets(final Boolean reopenAllTickets) {
		this.reopenAllTickets = reopenAllTickets;
	}

	/**
	 * @param setOwnAvailability the setOwnAvailability to set
	 */
	public void setSetOwnAvailability(final Boolean setOwnAvailability) {
		this.setOwnAvailability = setOwnAvailability;
	}

	/**
	 * @param takeAlreadyAssignedTicket the takeAlreadyAssignedTicket to set
	 */
	public void setTakeAlreadyAssignedTicket(final boolean takeAlreadyAssignedTicket) {
		this.takeAlreadyAssignedTicket = takeAlreadyAssignedTicket;
	}

	/**
	 * @param takeFreeTicket the takeFreeTicket to set
	 */
	public void setTakeFreeTicket(final boolean takeFreeTicket) {
		this.takeFreeTicket = takeFreeTicket;
	}
	
	/**
	 * @param stringValue
	 * @param propertyName
	 * @return the int value that corresponds to a string.
	 */
	protected int getTicketMonitoringIntValue(
			final String stringValue,
			final String propertyName) {
		String [] allowedValues = new String [] {
				"always", 
				"creation",
				"never",
		};
		Assert.contains(allowedValues, propertyName, stringValue.toLowerCase());
		if ("always".equalsIgnoreCase(stringValue.trim())) {
			return DepartmentManager.TICKET_MONITORING_ALWAYS;
		}
		if ("creation".equalsIgnoreCase(stringValue.trim())) {
			return DepartmentManager.TICKET_MONITORING_CREATION;
		}
		return DepartmentManager.TICKET_MONITORING_NEVER;
	}

	/**
	 * @param ticketMonitoringAny the ticketMonitoringAny to set
	 */
	public void setTicketMonitoringAny(final String ticketMonitoringAny) {
		this.ticketMonitoringAny = getTicketMonitoringIntValue(
				ticketMonitoringAny, "ticketMonitoringAny");
	}

	/**
	 * @param ticketMonitoringCategory the ticketMonitoringCategory to set
	 */
	public void setTicketMonitoringCategory(final String ticketMonitoringCategory) {
		this.ticketMonitoringCategory = getTicketMonitoringIntValue(
				ticketMonitoringCategory, "ticketMonitoringCategory");
	}

	/**
	 * @param ticketMonitoringManaged the ticketMonitoringManaged to set
	 */
	public void setTicketMonitoringManaged(final String ticketMonitoringManaged) {
		this.ticketMonitoringManaged = getTicketMonitoringIntValue(
				ticketMonitoringManaged, "ticketMonitoringManaged");
	}

	/**
	 * @param manageProperties the manageProperties to set
	 */
	public void setManageProperties(final boolean manageProperties) {
		this.manageProperties = manageProperties;
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
		return reportTime1;
	}

	/**
	 * @param reportTime1 the reportTime1 to set
	 */
	public void setReportTime1(final Integer reportTime1) {
		if (reportTime1 == null || reportTime1 == -1) {
			this.reportTime1 = null;
		} else {
			this.reportTime1 = reportTime1;
		}
	}

	/**
	 * @return the reportTime2
	 */
	public Integer getReportTime2() {
		return reportTime2;
	}

	/**
	 * @param reportTime2 the reportTime2 to set
	 */
	public void setReportTime2(final Integer reportTime2) {
		if (reportTime2 == null || reportTime2 == -1) {
			this.reportTime2 = null;
		} else {
			this.reportTime2 = reportTime2;
		}
	}

	/**
	 * @return the reportWeekend
	 */
	public Boolean getReportWeekend() {
		return reportWeekend;
	}

	/**
	 * @param reportWeekend the reportWeekend to set
	 */
	public void setReportWeekend(final Boolean reportWeekend) {
		this.reportWeekend = reportWeekend;
	}

}
