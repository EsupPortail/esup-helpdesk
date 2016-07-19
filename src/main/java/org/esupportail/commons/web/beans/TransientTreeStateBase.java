/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.beans;

import java.util.HashSet;
import java.util.Set;

import org.apache.myfaces.custom.tree2.TreeStateBase;

/**
 * 
 * @author Benjamin
 *
 */
public class TransientTreeStateBase extends TreeStateBase {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8930136920743543157L;

	/**
	 * A set to store the expanded nodes.
	 */
    private Set<String> expandedNodes = new HashSet<String>();

    /**
     * The id of the active node.
     */
    private String activeNodeId;

    /**
	 * Bean constructor.
	 */
	public TransientTreeStateBase() {
		super();
	}

	/**
	 * @see org.apache.myfaces.custom.tree2.TreeStateBase#isNodeExpanded(java.lang.String)
	 */
	@Override
	public boolean isNodeExpanded(final String nodeId) {
        return expandedNodes.contains(nodeId);
	}

	/**
	 * @see org.apache.myfaces.custom.tree2.TreeStateBase#toggleExpanded(java.lang.String)
	 */
	@Override
	public void toggleExpanded(final String nodeId) {
        activeNodeId = nodeId;
        if (expandedNodes.contains(nodeId)) {
            expandedNodes.remove(nodeId);
        } else {
        	Set<String> newExpandedNodes = new HashSet<String>();
        	for (String id : expandedNodes) {
				if (nodeId.startsWith(id) || id.startsWith(nodeId)) {
					newExpandedNodes.add(id);
				}
			}
            newExpandedNodes.add(nodeId);
            expandedNodes = newExpandedNodes;
        }
	}

	/**
	 * @see org.apache.myfaces.custom.tree2.TreeStateBase#isTransient()
	 */
	@Override
	public boolean isTransient() {
		return true;
	}

	/**
	 * @see org.apache.myfaces.custom.tree2.TreeStateBase#setTransient(boolean)
	 */
	@Override
	public void setTransient(
			@SuppressWarnings("unused")
			final boolean trans) {
		// must always be transient
	}

	/**
	 * @see org.apache.myfaces.custom.tree2.TreeStateBase#expandPath(java.lang.String[])
	 */
	@Override
	public void expandPath(final String[] nodePath) {
        for (int i = 0; i < nodePath.length; i++) {
            String nodeId = nodePath[i];
            expandedNodes.add(nodeId);
        }
        activeNodeId = nodePath[nodePath.length - 1];
	}

	/**
	 * @see org.apache.myfaces.custom.tree2.TreeStateBase#collapsePath(java.lang.String[])
	 */
	@Override
	public void collapsePath(final String[] nodePath) {
        for (int i = 0; i < nodePath.length; i++) {
            String nodeId = nodePath[i];
            expandedNodes.remove(nodeId);
        }
        activeNodeId = "0";
	}

	/**
	 * @return the activeNodeId
	 */
	public String getActiveNodeId() {
		return activeNodeId;
	}

}
