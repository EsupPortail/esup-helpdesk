/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.esupportail.helpdesk.domain.departmentSelection.actions.AddAllAction;
import org.esupportail.helpdesk.domain.departmentSelection.actions.AddByFilterAction;
import org.esupportail.helpdesk.domain.departmentSelection.actions.AddByLabelAction;
import org.esupportail.helpdesk.domain.departmentSelection.actions.DoNothingAction;
import org.esupportail.helpdesk.domain.departmentSelection.actions.StopAction;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.AdministratorCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.AndCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.ApplicationUserCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.CasUserCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.ClientIpCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.DepartmentManagerCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.ExternalDbCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.FalseCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.FqdnEndsWithCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.FqdnEqCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.FqdnStartsWithCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.LdapAttributeEqCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.LdapAttributeLikeCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.NamedCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.NotCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.OrCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.PortalAttributeEqCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.PortalAttributeLikeCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.PortalGroupMemberCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.ShibbolethUserCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.SpecificUserCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.TrueCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.UidLikeCondition;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

/**
 * A utility class for Digester.
 */
public class DigesterUtils {

	/**
	 * The addCondition method name.
	 */
	private static final String ADD_CONDITION_METHOD = "addCondition";

	/**
	 * The addAction method name.
	 */
	private static final String ADD_ACTION_METHOD = "addAction";

	/**
	 * Private constructor.
	 */
	private DigesterUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Parse a complete configuration.
	 * @param config
	 * @param configReader
	 * @throws DepartmentSelectionCompileError
	 */
	public static void parseConfigReader(
			final String config,
			final DepartmentSelectionConfigReader configReader)
	throws DepartmentSelectionCompileError {
		parse(config, configReader, true, true, true);
	}

	/**
	 * Parse a user-defined condition.
	 * @param config
	 * @param userDefinedCondition
	 * @throws DepartmentSelectionCompileError
	 */
	public static void parseUserDefinedCondition(
			final String config,
			final UserDefinedCondition userDefinedCondition)
	throws DepartmentSelectionCompileError {
		parse(config, userDefinedCondition, false, false, false);
	}

	/**
	 * Parse a rule.
	 * @param config
	 * @param rule
	 * @throws DepartmentSelectionCompileError
	 */
	public static void parseRule(
			final String config,
			final Rule rule)
	throws DepartmentSelectionCompileError {
		parse(config, rule, false, false, true);
	}

	/**
	 * Parse a complete configuration.
	 * @param config
	 * @param object
	 * @param addUserDefinedConditions
	 * @param addRules
	 * @param addActions
	 * @throws DepartmentSelectionCompileError
	 */
	protected static void parse(
			final String config,
			final Object object,
			final boolean addUserDefinedConditions,
			final boolean addRules,
			final boolean addActions)
	throws DepartmentSelectionCompileError {
		if (!StringUtils.hasText(config)) {
			throw new DepartmentSelectionCompileError("null config");
		}
		try {
			Digester dig = new Digester();
			dig.setValidating(false);
			dig.push(object);
			if (addUserDefinedConditions) {
				dig.addObjectCreate("*/define-condition", UserDefinedCondition.class);
				dig.addSetProperties("*/define-condition");
				dig.addSetNext("*/define-condition", "addUserDefinedCondition");
			}
			if (addRules) {
				dig.addObjectCreate("*/rule", Rule.class);
				dig.addSetNext("*/rule", "addRule");
				dig.addSetProperties("*/rule");
			}
			if (addActions) {
				dig.addObjectCreate("*/add-by-label", AddByLabelAction.class);
				dig.addSetProperties("*/add-by-label");
				dig.addSetNext("*/add-by-label", ADD_ACTION_METHOD);
				dig.addObjectCreate("*/add-by-filter", AddByFilterAction.class);
				dig.addSetProperties("*/add-by-filter");
				dig.addSetNext("*/add-by-filter", ADD_ACTION_METHOD);
				dig.addObjectCreate("*/add-all", AddAllAction.class);
				dig.addSetNext("*/add-all", ADD_ACTION_METHOD);
				dig.addObjectCreate("*/stop", StopAction.class);
				dig.addSetNext("*/stop", ADD_ACTION_METHOD);
				dig.addObjectCreate("*/do-nothing", DoNothingAction.class);
				dig.addSetNext("*/do-nothing", ADD_ACTION_METHOD);
			}
			// object descriptions
			dig.addBeanPropertySetter("*/description", "description");
			// conditions
			dig.addObjectCreate("*/true", TrueCondition.class);
			dig.addSetNext("*/true", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/false", FalseCondition.class);
			dig.addSetNext("*/false", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/uid-like", UidLikeCondition.class);
			dig.addSetProperties("*/uid-like");
			dig.addSetNext("*/uid-like", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/portal-attribute-eq", PortalAttributeEqCondition.class);
			dig.addSetProperties("*/portal-attribute-eq");
			dig.addSetNext("*/portal-attribute-eq", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/portal-attribute-like", PortalAttributeLikeCondition.class);
			dig.addSetProperties("*/portal-attribute-like");
			dig.addSetNext("*/portal-attribute-like", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/ldap-attribute-eq", LdapAttributeEqCondition.class);
			dig.addSetProperties("*/ldap-attribute-eq");
			dig.addSetNext("*/ldap-attribute-eq", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/attribute-eq", LdapAttributeEqCondition.class);
			dig.addSetProperties("*/attribute-eq");
			dig.addSetNext("*/attribute-eq", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/ldap-attribute-like", LdapAttributeLikeCondition.class);
			dig.addSetProperties("*/ldap-attribute-like");
			dig.addSetNext("*/ldap-attribute-like", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/attribute-like", LdapAttributeLikeCondition.class);
			dig.addSetProperties("*/attribute-like");
			dig.addSetNext("*/attribute-like", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/administrator", AdministratorCondition.class);
			dig.addSetNext("*/administrator", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/application-user", ApplicationUserCondition.class);
			dig.addSetNext("*/application-user", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/external-user", ApplicationUserCondition.class);
			dig.addSetNext("*/external-user", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/cas-user", CasUserCondition.class);
			dig.addSetNext("*/cas-user", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/local-user", CasUserCondition.class);
			dig.addSetNext("*/local-user", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/shibboleth-user", ShibbolethUserCondition.class);
			dig.addSetNext("*/shibboleth-user", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/specific-user", SpecificUserCondition.class);
			dig.addSetNext("*/specific-user", ADD_CONDITION_METHOD);
			dig.addObjectCreate(
					"*/department-manager", DepartmentManagerCondition.class);
			dig.addSetProperties("*/department-manager");
			dig.addSetNext("*/department-manager", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/or", OrCondition.class);
			dig.addSetNext("*/or", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/and", AndCondition.class);
			dig.addSetNext("*/and", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/not", NotCondition.class);
			dig.addSetNext("*/not", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/named-condition", NamedCondition.class);
			dig.addSetProperties("*/named-condition");
			dig.addSetNext("*/named-condition", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/fqdn-eq", FqdnEqCondition.class);
			dig.addSetProperties("*/fqdn-eq");
			dig.addSetNext("*/fqdn-eq", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/fqdn-starts-with", FqdnStartsWithCondition.class);
			dig.addSetProperties("*/fqdn-starts-with");
			dig.addSetNext("*/fqdn-starts-with", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/fqdn-ends-with", FqdnEndsWithCondition.class);
			dig.addSetProperties("*/fqdn-ends-with");
			dig.addSetNext("*/fqdn-ends-with", ADD_CONDITION_METHOD);
			dig.addObjectCreate("*/ip", ClientIpCondition.class);
			dig.addSetProperties("*/ip");
			dig.addSetNext("*/ip", ADD_CONDITION_METHOD);
			dig.addObjectCreate(
					"*/portal-group-member",
					PortalGroupMemberCondition.class);
			dig.addSetProperties("*/portal-group-member");
			dig.addSetNext("*/portal-group-member", ADD_CONDITION_METHOD);
            dig.addObjectCreate("*/external-db", ExternalDbCondition.class);
            dig.addSetProperties("*/external-db");
            dig.addSetNext("*/external-db", ADD_CONDITION_METHOD);

			dig.parse(new ByteArrayInputStream(config.getBytes()));
		} catch (IOException e) {
			throw new DepartmentSelectionCompileError(
					"exception while reading configuration: " + e.getMessage(), e);
		} catch (SAXException e) {
			throw new DepartmentSelectionCompileError(
					"exception while reading configuration: " + e.getMessage(), e);
		}
	}

}
