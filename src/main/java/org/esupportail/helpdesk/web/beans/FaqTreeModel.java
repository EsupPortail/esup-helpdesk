/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.List;

import org.esupportail.commons.web.beans.TransientTreeModelBase;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Faq;

/** 
 * The model of a FAQ tree.
 */ 
public class FaqTreeModel extends TransientTreeModelBase {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7415449259686193649L;

	/**
	 * The root node.
	 */
	private FaqNode rootNode;

	/**
	 * Bean constructor.
	 * @param root
	 */
	public FaqTreeModel(final FaqNode root) {
		super(root);
		rootNode = root;
	}
	
	/**
	 * @param department 
	 * @return the node for the given department.
	 */
	@SuppressWarnings("unchecked")
	public FaqNode findNode(final Department department) {
		for (FaqNode node : (List<FaqNode>) rootNode.getChildren()) {
			if ("department".equals(node.getType()) && department.equals(node.getDepartment())) {
				return node;
			}
		}
		return rootNode;
	}
	
	/**
	 * @param node 
	 * @param faq
	 * @return the node for the given FAQ.
	 */
	@SuppressWarnings("unchecked")
	protected FaqNode findNode(
			final FaqNode node,
			final Faq faq) {
		if ("faq".equals(node.getType()) && faq.equals(node.getFaq())) {
			return node;
		}
		for (FaqNode subNode : (List<FaqNode>) node.getChildren()) {
			FaqNode resultNode = findNode(subNode, faq);
			if (resultNode != null) {
				return resultNode;
			}
		}
		return null;
	}
	
	/**
	 * @param faq
	 * @return the node for the given FAQ.
	 */
	public FaqNode findNode(final Faq faq) {
		return findNode(rootNode, faq);
	}
	
	/**
	 * @return the rootNode
	 */
	public FaqNode getRootNode() {
		return rootNode;
	}
	
}

