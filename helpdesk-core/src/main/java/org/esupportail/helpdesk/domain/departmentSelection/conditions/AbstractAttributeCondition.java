/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esupportail.commons.utils.strings.WordPattern;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;


/**
 * An abstract condition regarding attributes.
 */
@SuppressWarnings("serial")
public abstract class AbstractAttributeCondition extends AbstractUserCondition {

	/**
	 * The name of the attribute.
	 */
	private String name;

	/**
	 * The value of the attribute.
	 */
	private String value;

	/**
	 * The pattern of the attribute.
	 */
	private String pattern;

	/**
	 * True to ignore case.
	 */
	private boolean ignoreCase;

	/**
	 * Empty constructor (for digester).
	 */
	protected AbstractAttributeCondition() {
		super();
		if (!hasPatternAttribute() && !hasValueAttribute()) {
			throw new UnsupportedOperationException(
                    "methods hasValueAttribute() and "
					+ "hasPatternAttribute() should not both return false");
		}
		if (!isLdapBased() && !isPortalBased() && !isUserIdBased()) {
			throw new UnsupportedOperationException(
                    "methods isLdapBased() and "
					+ "isPortalBased() and "
					+ "isUserIdBased() should not all return false");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "<" + getTagName();
		if (hasNameAttribute()) {
			str += " name=\"" + getName() + "\"";
		}
		if (hasValueAttribute()) {
			str += " value=\"" + getValue() + "\"";
		}
		if (hasPatternAttribute()) {
			str += " pattern=\"" + getPattern() + "\"";
		}
		str += " ignoreCase=\"" + isIgnoreCase() + "\" />";
		return str;
	}

	/**
	 * Check the condition.
	 * @throws DepartmentSelectionCompileError
	 */
	@Override
	protected void checkInternal() throws DepartmentSelectionCompileError {
		if (hasNameAttribute() && getName() == null) {
			throw new DepartmentSelectionCompileError(
					"<" + getTagName() + "> tags should have 'name' attributes");
		}
		if (hasValueAttribute() && getValue() == null) {
			throw new DepartmentSelectionCompileError(
					"<" + getTagName() + "> tags should have 'value' attributes");
		}
		if (hasPatternAttribute() && getPattern() == null) {
			throw new DepartmentSelectionCompileError(
					"<" + getTagName() + "> tags should have 'pattern' attributes");
		}
	}

	/**
	 * @param domainService
	 * @param user
	 * @return the attributes
	 */
	protected Map<String, List<String>> getAttributes(
			final DomainService domainService,
			final User user) {
		if (isLdapBased()) {
			return domainService.getUserStore().getLdapAttributes(user);
		}
		if (isPortalBased()) {
			return domainService.getUserStore().getPortalAttributes(user);
		}
		Map<String, List<String>> attributes = new HashMap<String, List<String>>();
		List<String> attributeValues = new ArrayList<String>();
		attributeValues.add(user.getRealId());
		attributes.put(getName(), attributeValues);
		return attributes;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractUserCondition
	 * #isMatchedInternal(org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	protected boolean isMatchedInternal(
			final DomainService domainService,
			final User user) {
		Map<String, List<String>> attributes = getAttributes(domainService, user);
		if (attributes == null) {
			return false;
		}
		List<String> attributeValues = attributes.get(getName());
		if (attributeValues == null) {
			return false;
		}
		for (String attributeValue : attributeValues) {
			if (hasPatternAttribute()) {
				WordPattern wordPattern = new WordPattern(pattern);
				if (isIgnoreCase()) {
					if (wordPattern.isMatchedByIgnoreCase(attributeValue)) {
						return true;
					}
				} else {
					if (wordPattern.isMatchedBy(attributeValue)) {
						return true;
					}
				}
			} else {
				if (isIgnoreCase()) {
					if (value.equalsIgnoreCase(attributeValue)) {
						return true;
					}
				} else {
					if (value.equals(attributeValue)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * @return true if the tag has a name attribute.
	 */
	protected boolean hasNameAttribute() {
		return false;
	}

	/**
	 * @return true if the tag has a value attribute.
	 */
	protected boolean hasValueAttribute() {
		return false;
	}

	/**
	 * @return true if the tag has a pattern attribute.
	 */
	protected boolean hasPatternAttribute() {
		return false;
	}

	/**
	 * @return true if based on LDAP.
	 */
	protected boolean isLdapBased() {
		return false;
	}

	/**
	 * @return true if based on the portal.
	 */
	protected boolean isPortalBased() {
		return false;
	}

	/**
	 * @return true if based on the the user's id.
	 */
	protected boolean isUserIdBased() {
		return false;
	}

	/**
	 * @return the tag name.
	 */
	protected abstract String getTagName();

	/**
	 * @param theName The name to set.
	 */
	protected void setNameInternal(final String theName) {
		this.name = theName;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		if (!hasNameAttribute()) {
			throw new UnsupportedOperationException(
					"attribute 'name' is not allowed for tag '" + getTagName() + "'");
		}
		setNameInternal(name);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		if (!hasNameAttribute()) {
			return "unused";
		}
		return name;
	}

	/**
	 * @param value The name to set.
	 */
	public void setValue(final String value) {
		if (!hasValueAttribute()) {
			throw new UnsupportedOperationException(
					"attribute 'value' is not allowed for tag '" + getTagName() + "'");
		}
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		if (!hasValueAttribute()) {
			throw new UnsupportedOperationException(
					"attribute 'value' is not allowed for tag '" + getTagName() + "'");
		}
		return value;
	}

	/**
	 * @param pattern The name to set.
	 */
	public void setPattern(final String pattern) {
		if (!hasPatternAttribute()) {
			throw new UnsupportedOperationException(
					"attribute 'pattern' is not allowed for tag '" + getTagName() + "'");
		}
		this.pattern = pattern;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		if (!hasPatternAttribute()) {
			throw new UnsupportedOperationException(
					"attribute 'pattern' is not allowed for tag '" + getTagName() + "'");
		}
		return pattern;
	}

	/**
	 * @return the ignoreCase
	 */
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * @param ignoreCase the ignoreCase to set
	 */
	public void setIgnoreCase(final boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

}
