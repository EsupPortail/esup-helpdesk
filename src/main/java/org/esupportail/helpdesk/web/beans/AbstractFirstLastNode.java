/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;

/** 
 * The node of a category.
 */ 
@SuppressWarnings("serial")
public abstract class AbstractFirstLastNode extends TreeNodeBase {
	
	/**
	 * True when the first node of its parent.
	 */
	private boolean first;
	
	/**
	 * True when the last node of its parent.
	 */
	private boolean last;
	
	/**
	 * Bean constructor.
	 */
	public AbstractFirstLastNode() {
		super();
	}

	/**
	 * Bean constructor.
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public AbstractFirstLastNode(final String arg0, final String arg1, final boolean arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * Bean constructor.
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public AbstractFirstLastNode(final String arg0, final String arg1, final String arg2, final boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * @return the first
	 */
	public boolean isFirst() {
		return first;
	}

	/**
	 * @param first the first to set
	 */
	public void setFirst(final boolean first) {
		this.first = first;
	}

	/**
	 * @return the last
	 */
	public boolean isLast() {
		return last;
	}

	/**
	 * @param last the last to set
	 */
	public void setLast(final boolean last) {
		this.last = last;
	}

	/**
	 * Mark the first and last child nodes of a node.
	 * @param node 
	 */
	public static void markFirstAndLastChildNodes(final TreeNode node) {
    	if (node.getChildCount() == 0) {
    		return;
    	}
    	TreeNode firstNode = (TreeNode) node.getChildren().get(0);
    	if (firstNode instanceof AbstractFirstLastNode) {
    		((AbstractFirstLastNode) firstNode).setFirst(true);
    	}
    	TreeNode lastNode = (TreeNode) node.getChildren().get(node.getChildCount() - 1);
    	if (lastNode instanceof AbstractFirstLastNode) {
    		((AbstractFirstLastNode) lastNode).setLast(true);
    	}
	}

	/**
	 * Mark the first and last child nodes.
	 */
	public void markFirstAndLastChildNodes() {
		markFirstAndLastChildNodes(this);
	}

}

