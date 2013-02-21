/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;


/**
 * A class to store old ticket templates, used to upgrade only.
 * @deprecated
 */
@Deprecated
public class OldTicketTemplate implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5334555472406578396L;

	/**
	 * Primary key.
	 */
	private long id;
	
	/**
	 * Short description.
	 */
	private String label;
	
	/**
	 * Extended description.
	 */
	private String xlabel;
	
	/**
	 * The category.
	 */
	private Category category;
	
	/**
     * The order of the member in the category.
     */
    private Integer order;
    
    /**
     * Defines if the ticket template uses the default priority of the category.
     */
	private Boolean useCategoryPriority;
   
	/**
     * Defines if the ticket template uses the default message of the category.
     */
	private Boolean useCategoryMessage;

	/**
	 * The label of the future ticket.
	 */
	private String ticketTemplateLabel;

	/**
	 * The priority level of the future ticket.
	 */	
	private Integer ticketTemplatePriorityLevel;

	/**
	 * The message of the future ticket.
	 */	
	private String ticketTemplateMessage;

	/**
	 * Default constructor.
	 */
	public OldTicketTemplate() {
		super();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof OldTicketTemplate)) {
			return false;
		}
		return ((OldTicketTemplate) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * @return the ticketTemplateLabel
	 */
	public String getTicketTemplateLabel() {
		return ticketTemplateLabel;
	}

	/**
	 * @return the ticketTemplateMessage
	 */
	public String getTicketTemplateMessage() {
		return ticketTemplateMessage;
	}

	/**
	 * @return the ticketTemplatePriorityLevel
	 */
	public Integer getTicketTemplatePriorityLevel() {
		return ticketTemplatePriorityLevel;
	}

	/**
	 * @return the useCategoryMessage
	 */
	public Boolean getUseCategoryMessage() {
		return useCategoryMessage;
	}

	/**
	 * @return the useCategoryPriority
	 */
	public Boolean getUseCategoryPriority() {
		return useCategoryPriority;
	}

	/**
	 * @return the xlabel
	 */
	public String getXlabel() {
		return xlabel;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(final Category category) {
		this.category = category;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(final Integer order) {
		this.order = order;
	}

	/**
	 * @param ticketTemplateLabel the ticketTemplateLabel to set
	 */
	public void setTicketTemplateLabel(final String ticketTemplateLabel) {
		this.ticketTemplateLabel = ticketTemplateLabel;
	}

	/**
	 * @param ticketTemplateMessage the ticketTemplateMessage to set
	 */
	public void setTicketTemplateMessage(final String ticketTemplateMessage) {
		this.ticketTemplateMessage = ticketTemplateMessage;
	}

	/**
	 * @param ticketTemplatePriorityLevel the ticketTemplatePriorityLevel to set
	 */
	public void setTicketTemplatePriorityLevel(final Integer ticketTemplatePriorityLevel) {
		this.ticketTemplatePriorityLevel = ticketTemplatePriorityLevel;
	}

	/**
	 * @param useCategoryMessage the useCategoryMessage to set
	 */
	public void setUseCategoryMessage(final Boolean useCategoryMessage) {
		this.useCategoryMessage = useCategoryMessage;
	}

	/**
	 * @param useCategoryPriority the useCategoryPriority to set
	 */
	public void setUseCategoryPriority(final Boolean useCategoryPriority) {
		this.useCategoryPriority = useCategoryPriority;
	}

	/**
	 * @param xlabel the xlabel to set
	 */
	public void setXlabel(final String xlabel) {
		this.xlabel = xlabel;
	}
	
}
