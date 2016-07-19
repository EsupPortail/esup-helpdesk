/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.beans;

import org.apache.myfaces.custom.tree2.TreeNode;

/**
 * 
 * @author Benjamin
 *
 */
public class TransientTreeModelBase extends TreeModelBase {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7342900258702215016L;

    /**
     * Bean constructor.
     * @param root
     */
    public TransientTreeModelBase(final TreeNode root) {
        super(root);
        setTreeState(new TransientTreeStateBase());
    }
    
    /**
     * @return the active node of the tree
     */
    public TreeNode getActiveNode() {
    	TransientTreeStateBase myTreeState = (TransientTreeStateBase) getTreeState();
    	String nodeId = myTreeState.getActiveNodeId();
    	return getNodeById(nodeId);
    }

    /**
     * Set the active node.
     * @param nodeId
     */
    public void setActiveNode(final String nodeId) {
    	TransientTreeStateBase myTreeState = (TransientTreeStateBase) getTreeState();
    	String[] nodePath = getPathInformation(nodeId);
    	String id = "";
    	for (String string : nodePath) {
			id += " " + string;
		}
    	myTreeState.expandPath(nodePath);
    }

}
