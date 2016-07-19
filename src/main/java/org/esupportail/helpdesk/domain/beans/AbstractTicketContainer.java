/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;

import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.TicketScope;

/**
 * The abstract class that represents ticket containers (departments and categories).
 */
@SuppressWarnings("serial")
public abstract class AbstractTicketContainer implements Serializable, TicketContainer {
	
    /**
	 * The unique id.
	 */
    private long id;
	
    /**
	 * The label.
	 */
	private String label;

	/**
	 * The long label.
	 */
	private String xlabel;
	
    /**
     * The number of days before a ticket is automatically approved when closed
     * (no approbation when 0, inherit when null).
     */
    private Integer autoExpire;
    
    /**
     * the default scope for the tickets of the container (inherit when null).
     */
    private String defaultTicketScope;

    /**
     * the default label for the tickets of the container (inherit when null).
     */
    private String defaultTicketLabel;

    /**
     * the default message for the tickets of the container (inherit when null).
     */
    private String defaultTicketMessage;

    /**
     * the default priority for the tickets of the container (inherit when null).
     */
    private int defaultTicketPriority;

    /**
     * The order of this container.
     */
    private Integer order;

    /**
     * the URL of the department.
     */
    private String url;
    
    /**
     * The effective scope for the new tickets.
     */
    private String effectiveDefaultTicketScope;
    
    /**
     * The name of the default assignment algorithm. 
     */
    private String assignmentAlgorithmName;

    /**
     * True to hide to application users. 
     */
    private Boolean hideToExternalUsers;
    
    /**
     * True to inherit email monitoring properties.
     */
    private Boolean inheritMonitoring;
    
    /**
     * The email address to send an email.
     */
    private String monitoringEmail;
    
    /**
     * True to send email to local users.
     */
    private Boolean monitoringLocalEmails;
    
    /**
     * The auth type for sent emails.
     */
    private String monitoringEmailAuthType;
    
    /**
     * The monitoring level.
     */
    private Integer monitoringLevel;

    /**
     * The icon.
     */
    private Icon icon;

    /**
     * Bean constructor.
     */
    public AbstractTicketContainer() {
    	super();
    	this.defaultTicketScope = TicketScope.DEFAULT;
    	this.defaultTicketPriority = DomainService.DEFAULT_PRIORITY_VALUE;
    	this.hideToExternalUsers = Boolean.FALSE;
    	this.inheritMonitoring = Boolean.TRUE;
    	this.monitoringEmail = null;
    	this.monitoringLocalEmails = Boolean.TRUE;
    	this.monitoringEmailAuthType = null;
    	this.monitoringLevel = MONITORING_NEVER;
    	this.icon = null;
    }

    /**
     * Copy.
     * @param container the container to copy
     */
    public AbstractTicketContainer(final AbstractTicketContainer container) {
    	super();
    	this.id = container.id;
    	this.label = container.label;
    	this.xlabel = container.xlabel;
    	this.autoExpire = container.autoExpire;
    	this.defaultTicketScope = container.defaultTicketScope;
    	this.effectiveDefaultTicketScope = container.effectiveDefaultTicketScope;
    	this.defaultTicketLabel = container.defaultTicketLabel;
    	this.defaultTicketMessage = container.defaultTicketMessage;
    	this.defaultTicketPriority = container.defaultTicketPriority;
    	this.assignmentAlgorithmName = container.assignmentAlgorithmName;
    	this.order = container.order;
    	this.url = container.url;
    	this.hideToExternalUsers = container.hideToExternalUsers;
    	this.inheritMonitoring = container.inheritMonitoring;
    	this.monitoringEmail = container.monitoringEmail;
    	this.monitoringLevel = container.monitoringLevel;
    	this.monitoringLocalEmails = container.monitoringLocalEmails;
    	this.monitoringEmailAuthType = container.monitoringEmailAuthType;
    	this.icon = container.icon;
    }

	/**
	 * @return a String.
	 * @see java.lang.Object#toString()
	 */
	protected String toStringInternal() {
		return "id=[" + id + "]"
		+ ", label=[" + label + "]"
		+ ", xlabel=[" + xlabel + "]"
		+ ", order=[" + order + "]"
		+ ", autoExpire=[" + autoExpire + "]" 
		+ ", defaultTicketScope=[" + defaultTicketScope + "]" 
		+ ", defaultTicketLabel=[" + defaultTicketLabel + "]"
		+ ", defaultTicketMessage=[" + defaultTicketMessage + "]"
		+ ", defaultTicketPriority=[" + defaultTicketPriority + "]"
		+ ", url=[" + url + "]";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getId()
	 */
    @Override
	public long getId() {
        return id;
    }

    /**
	 * @param id  The id to set.
	 */
    public void setId(final long id) {
        this.id = id;
    }

    /**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getLabel()
	 */
    @Override
	public String getLabel() {
        return label;
    }

    /**
	 * @param label  The label to set.
	 */
    public void setLabel(final String label) {
        this.label = StringUtils.nullIfEmpty(label);
    }

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getXlabel()
	 */
	@Override
	public String getXlabel() {
		return xlabel;
	}

	/**
	 * @param xlabel the xlabel to set
	 */
	public void setXlabel(final String xlabel) {
		this.xlabel = StringUtils.nullIfEmpty(xlabel);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getAutoExpire()
	 */
	@Override
	public Integer getAutoExpire() {
		return autoExpire;
	}

	/**
	 * @param autoExpire the autoExpire to set
	 */
	public void setAutoExpire(final Integer autoExpire) {
		this.autoExpire = autoExpire;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getDefaultTicketScope()
	 */
	@Override
	public String getDefaultTicketScope() {
		return defaultTicketScope;
	}

	/**
	 * @param defaultTicketScope the defaultTicketScope to set
	 */
	public void setDefaultTicketScope(final String defaultTicketScope) {
		this.defaultTicketScope = defaultTicketScope;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getOrder()
	 */
	@Override
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
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getDefaultTicketLabel()
	 */
	@Override
	public String getDefaultTicketLabel() {
		return defaultTicketLabel;
	}

	/**
	 * @param defaultTicketLabel the defaultTicketLabel to set
	 */
	public void setDefaultTicketLabel(final String defaultTicketLabel) {
		this.defaultTicketLabel = StringUtils.nullIfEmpty(defaultTicketLabel);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getDefaultTicketMessage()
	 */
	@Override
	public String getDefaultTicketMessage() {
		return defaultTicketMessage;
	}

	/**
	 * @param defaultTicketMessage the defaultTicketMessage to set
	 */
	public void setDefaultTicketMessage(final String defaultTicketMessage) {
		this.defaultTicketMessage = StringUtils.filterFckInput(defaultTicketMessage);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getDefaultTicketPriority()
	 */
	@Override
	public int getDefaultTicketPriority() {
		return defaultTicketPriority;
	}

	/**
	 * @param defaultTicketPriority the defaultTicketPriority to set
	 */
	public void setDefaultTicketPriority(final int defaultTicketPriority) {
		this.defaultTicketPriority = defaultTicketPriority;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getUrl()
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	/**
	 * @param effectiveDefaultTicketScope the effectiveDefaultTicketScope to set
	 */
	public void setEffectiveDefaultTicketScope(final String effectiveDefaultTicketScope) {
		this.effectiveDefaultTicketScope = effectiveDefaultTicketScope;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getEffectiveDefaultTicketScope()
	 */
	@Override
	public String getEffectiveDefaultTicketScope() {
		return effectiveDefaultTicketScope;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getAssignmentAlgorithmName()
	 */
	@Override
	public String getAssignmentAlgorithmName() {
		return assignmentAlgorithmName;
	}

	/**
	 * @param assignmentAlgorithmName the assignmentAlgorithmName to set
	 */
	public void setAssignmentAlgorithmName(
			final String assignmentAlgorithmName) {
		this.assignmentAlgorithmName = StringUtils.nullIfEmpty(assignmentAlgorithmName);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getHideToExternalUsers()
	 */
	@Override
	public Boolean getHideToExternalUsers() {
		if (hideToExternalUsers == null) {
			return false;
		}
		return hideToExternalUsers;
	}

	/**
	 * @param hideToExternalUsers the hideToExternalUsers to set
	 */
	public void setHideToExternalUsers(final Boolean hideToExternalUsers) {
		this.hideToExternalUsers = hideToExternalUsers;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getInheritMonitoring()
	 */
	@Override
	public Boolean getInheritMonitoring() {
		if (inheritMonitoring == null) {
			return true;
		}
		return inheritMonitoring;
	}

	/**
	 * @param inheritMonitoring the inheritMonitoring to set
	 */
	public void setInheritMonitoring(final Boolean inheritMonitoring) {
		this.inheritMonitoring = inheritMonitoring;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getMonitoringEmail()
	 */
	@Override
	public String getMonitoringEmail() {
		return monitoringEmail;
	}

	/**
	 * @param monitoringEmail the monitoringEmail to set
	 */
	public void setMonitoringEmail(final String monitoringEmail) {
		this.monitoringEmail = StringUtils.nullIfEmpty(monitoringEmail);
	}

	/**
	 * @return the monitoringLocalEmails
	 */
	@Deprecated
	public Boolean getMonitoringLocalEmails() {
		if (monitoringLocalEmails == null) {
			return true;
		}
		return monitoringLocalEmails;
	}

	/**
	 * @param monitoringLocalEmails the monitoringLocalEmails to set
	 */
	@Deprecated
	public void setMonitoringLocalEmails(final Boolean monitoringLocalEmails) {
		this.monitoringLocalEmails = monitoringLocalEmails;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getMonitoringEmailAuthType()
	 */
	@Override
	public String getMonitoringEmailAuthType() {
		return monitoringEmailAuthType;
	}

	/**
	 * @param monitoringEmailAuthType the monitoringEmailAuthType to set
	 */
	public void setMonitoringEmailAuthType(final String monitoringEmailAuthType) {
		this.monitoringEmailAuthType = monitoringEmailAuthType;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getMonitoringLevel()
	 */
	@Override
	public Integer getMonitoringLevel() {
		if (monitoringLevel == null) {
			return MONITORING_NEVER;
		}
		return monitoringLevel;
	}

	/**
	 * @param monitoringLevel the monitoringLevel to set
	 */
	public void setMonitoringLevel(final Integer monitoringLevel) {
		this.monitoringLevel = monitoringLevel;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getIcon()
	 */
	@Override
	public Icon getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(final Icon icon) {
		this.icon = icon;
	}

}
