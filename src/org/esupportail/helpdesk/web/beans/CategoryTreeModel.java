/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;
import java.util.Map;

import org.esupportail.commons.web.beans.TreeModelBase;
import org.esupportail.helpdesk.domain.beans.Category;

/** 
 * The model of a category tree.
 */ 
public class CategoryTreeModel extends TreeModelBase {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -1109481759380097999L;
	
	/**
	 * The root node.
	 */
	private CategoryNode rootNode;

	/**
	 * A map to store the correspondance between category ids and node ids.
	 */
	private Map<Long, String> ids;

	/**
	 * Bean constructor.
	 * @param root
	 */
	public CategoryTreeModel(final CategoryNode root) {
		super(root);
		rootNode = root;
		ids = new HashMap<Long, String>();
		storeChildrenIds(root, "0");
	}
	
	/**
	 * Store the ids of a hierarchy.
	 * @param node
	 * @param id
	 */
	private void storeChildrenIds(final CategoryNode node, final String id) {
		for (int i = 0; i < node.getChildCount(); i++) {
			CategoryNode child = (CategoryNode) (node.getChildren().get(i));
			String childId = id + SEPARATOR + i;
			ids.put(child.getCategory().getId(), childId);
			storeChildrenIds(child, childId);
		}
	}
	
	/**
	 * @param category
	 * @return the node id that corresponds to category in the tree.
	 */
	public String getCategoryNodeId(final Category category) {
		return ids.get(category.getId());
	}

	/**
	 * @return the rootNode
	 */
	public CategoryNode getRootNode() {
		return rootNode;
	}
	
}

