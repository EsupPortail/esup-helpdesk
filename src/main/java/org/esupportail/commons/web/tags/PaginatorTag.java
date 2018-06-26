/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.custom.div.DivTag;
import org.apache.myfaces.shared_impl.renderkit.html.HTML;
import org.esupportail.commons.web.component.UIPaginator;
import org.springframework.util.StringUtils;

/**
 * This class is a tag for paginator element.
 * @author cleprous
 */
public class PaginatorTag extends DivTag {

	/*
	 ******************* PROPERTIES ******************* */

	/**
	 * The Paginator.
	 */
	private String paginator;
	
	/**
	 * name of the items displayed.
	 */
	private String itemsName;
	
	/**
	 * The names of the blocks to show separated by a comma(,).
	 */
	private String visibleBlocks;
	
	/**
	 * Same as h:selectOneMenu. Used in itemsPerPage block to update the pageSize.
	 */
	private String onchange;
	
	/*
	 ******************* INIT ************************* */
	
	/**
	 * Constructor.
	 */
	public PaginatorTag() {
		super();
		paginator = "";
		itemsName = "";
		visibleBlocks = "";
	}

	/*
	 ******************* METHODS ********************** */
	
	/**
	 * @see org.apache.myfaces.taglib.html.ext.HtmlDataTableTag#getComponentType()
	 */
	@Override
	public String getComponentType() {
		return UIPaginator.COMPONENT_TYPE;
	}
	
	/**
	 * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void setProperties(final UIComponent component) {
		super.setProperties(component);
		if (StringUtils.hasText(getPaginator())) {
			component.setValueBinding(UIPaginator.PAGINATOR_ATTR, 
					getFacesContext().getApplication().createValueBinding(getPaginator()));
		} else {
			throw new TagException("The attribute paginator don't have to be NULL or empty");
		}
		component.getAttributes().put(
				UIPaginator.PAGINATOR_EL_EXPRESSION_ATTR, getValueInELExpression(getPaginator()));
		component.getAttributes().put(UIPaginator.ITEM_NAMES_ATTR, getItemsName());
		component.getAttributes().put(UIPaginator.VISIBLE_BLOCKS_ATTR, getVisibleBlocks());
		String stringVar = TagUtils.getStringsVarInPageTag(this);
		if (stringVar != null) {
			component.getAttributes().put(UIPaginator.STRINGS_VAR_ATTR, stringVar);
		}
		if (StringUtils.hasText(getOnchange())) {
			component.getAttributes().put(HTML.ONCHANGE_ATTR, getOnchange());
		}
	}
	
	/**
	 * Returns the value contained in an EL expression.
	 * EX.: #{value} return value.
	 * @param eLExpression
	 * @return String 
	 */
	private String getValueInELExpression(final String eLExpression) {
		String value = eLExpression.substring(2, eLExpression.length() - 1);
		return value;
	}
	
	/**
	 * @see javax.faces.webapp.UIComponentTag#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return null;
	}

	/*
	 ******************* ACCESSORS ******************** */
	/**
	 * @return the paginator
	 */
	public String getPaginator() {
		return paginator;
	}

	/**
	 * @param paginator the paginator to set
	 */
	public void setPaginator(final String paginator) {
		this.paginator = paginator;
	}

	/**
	 * @return the itemsName
	 */
	public String getItemsName() {
		return itemsName;
	}

	/**
	 * @param itemsName the itemsName to set
	 */
	public void setItemsName(final String itemsName) {
		this.itemsName = itemsName;
	}

	/**
	 * @return the visibleBlocks
	 */
	public String getVisibleBlocks() {
		return visibleBlocks;
	}

	/**
	 * @param visibleBlocks the visibleBlocks to set
	 */
	public void setVisibleBlocks(final String visibleBlocks) {
		this.visibleBlocks = visibleBlocks;
	}

	/**
	 * @return the onchange
	 */
	public String getOnchange() {
		return onchange;
	}

	/**
	 * @param onchange the onchange to set
	 */
	public void setOnchange(final String onchange) {
		this.onchange = onchange;
	}
	
}
