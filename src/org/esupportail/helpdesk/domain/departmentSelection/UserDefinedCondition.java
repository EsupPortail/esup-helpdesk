/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.io.Serializable;
import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition;

/**
 * A user-defined condition.
 */
public class UserDefinedCondition implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7796614013051371391L;

	/**
	 * The name of the condition.
	 */
	private String name;
	
	/**
	 * The description of the condition.
	 */
	private String description;
	
	/**
	 * The condition (its definition).
	 */
	private Condition condition;
	
	/**
	 * True when used.
	 */
	private boolean used;
	
	/**
	 * Empty constructor (for Digester).
	 */
	public UserDefinedCondition() {
		super();
		used = false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "";
		str += "<define-condition name=\"" + name + "\" >";
		str += contentToString();
		str += "</define-condition>";
		return str;
	}

	/**
	 * @return the content as a string.
	 */
	public String contentToString() {
		String str = "";
		str += "<description>";
		if (description != null) {
			str += description;
		}
		str += "</description>";
		str += getCondition();
		return str;
	}

	/**
	 * Set the condition.
	 * @param cond the condition to add
	 * @throws DepartmentSelectionCompileError 
	 */
	public void addCondition(final Condition cond) throws DepartmentSelectionCompileError {
		if (condition != null) {
			throw new DepartmentSelectionCompileError(
					"<define-condition> tags should be used with one nested condition only");
		}
		this.condition = cond;
	}

	/**
	 * Is the condition matched?
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @return a boolean.
	 */
	public boolean isMatched(
			final DomainService domainService, 
			final User user, 
			final InetAddress client) {
		return condition.isMatched(domainService, user, client);
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Returns the condition.
	 */
	public Condition getCondition() {
		return this.condition;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the used
	 */
	public boolean isUsed() {
		return used;
	}

	/**
	 * Set used.
	 */
	public void setUsed() {
		this.used = true;
	}
}
