package org.esupportail.helpdesk.domain.beans;

/**
 * The interface of ticket containers.
 *
 */
public interface TicketContainer {

	/** A constant for the monitoring level. */
	int MONITORING_NEVER = 0; 

	/** A constant for the monitoring level. */
	int MONITORING_CREATION = 1; 

	/** A constant for the monitoring level. */
	int MONITORING_CREATION_OR_RELEASE = 2; 

	/** A constant for the monitoring level. */
	int MONITORING_ALWAYS = 3; 

	/** A constant for the monitoring level. */
	int MONITORING_CREATION_OR_FREE = 4; 

	/**
	 * @return  Returns the id.
	 */
	long getId();

	/**
	 * @return  Returns the label.
	 */
	String getLabel();

	/**
	 * @return the xlabel
	 */
	String getXlabel();

	/**
	 * @return the autoExpire
	 */
	Integer getAutoExpire();

	/**
	 * @return the defaultTicketScope
	 */
	String getDefaultTicketScope();

	/**
	 * @return the order
	 */
	Integer getOrder();

	/**
	 * @return the defaultTicketLabel
	 */
	String getDefaultTicketLabel();

	/**
	 * @return the defaultTicketMessage
	 */
	String getDefaultTicketMessage();

	/**
	 * @return the defaultTicketPriority
	 */
	int getDefaultTicketPriority();

	/**
	 * @return the url
	 */
	String getUrl();

	/**
	 * @return the effectiveDefaultTicketScope
	 */
	String getEffectiveDefaultTicketScope();

	/**
	 * @return the assignmentAlgorithmName
	 */
	String getAssignmentAlgorithmName();

	/**
	 * @return the hideToExternalUsers
	 */
	Boolean getHideToExternalUsers();

	/**
	 * @return the inheritMonitoring
	 */
	Boolean getInheritMonitoring();

	/**
	 * @return the monitoringEmail
	 */
	String getMonitoringEmail();

	/**
	 * @return the monitoringLocalEmailAuthType
	 */
	String getMonitoringEmailAuthType();

	/**
	 * @return the monitoringLevel
	 */
	Integer getMonitoringLevel();

	/**
	 * @return the icon
	 */
	Icon getIcon();

	/**
	 * @return the effective icon (inherited for categories)
	 */
	Icon getEffectiveIcon();

}