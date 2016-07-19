/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.beans; 

import org.apache.myfaces.custom.tree2.TreeNode;

/** 
 * The model of a tree able to set the node identifiers.
 */ 
public class TreeModelBase extends org.apache.myfaces.custom.tree2.TreeModelBase {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6839340913523565161L;

	/**
	 * The root node.
	 */
	private TreeNode rootNode;

	/**
	 * Bean constructor.
	 * @param root
	 */
	public TreeModelBase(final TreeNode root) {
		super(root);
		rootNode = root;
		setNodeIdentifiers();
	}
	
	/**
	 * Set the identifiers of the nodes of a hierarchy.
	 * @param node
	 * @param id
	 */
	private void setChildrenIdentifiers(final TreeNode node, final String id) {
		for (int i = 0; i < node.getChildCount(); i++) {
			TreeNode child = (TreeNode) (node.getChildren().get(i));
			String childId = id + SEPARATOR + i;
			child.setIdentifier(childId);
			setChildrenIdentifiers(child, childId);
		}
	}
	
	/**
	 * Set the identifiers of all the nodes of the tree.
	 */
	protected void setNodeIdentifiers() {
		rootNode.setIdentifier("0");
		setChildrenIdentifiers(rootNode, "0");
	}
	
}

