/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans.departmentSelection; 

import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.departmentSelection.Actions;

/** 
 * The node of a rule.
 */ 
public class ActionsNode extends AbstractActionsNode {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -501202324426281184L;
	
	/**
	 * Bean constructor.
	 * @param actions
	 */
	public ActionsNode(
			final Actions actions) {
		super("actions", actions);
		addActionNodes();
	}
	
	/**
	 * @return the sub nodes
	 */
	@Override
	@SuppressWarnings({ "unchecked", "cast" })
	public List<ActionNode> getActionNodes() {
		return (List<ActionNode>) getChildren();
	}

	/**
	 * Evaluate the associated condition.
	 * @param domainService 
	 * @param type 
	 */
	public void eval(
			final DomainService domainService,
			final int type) {
		super.eval(domainService, type, true);
	}
	
}

