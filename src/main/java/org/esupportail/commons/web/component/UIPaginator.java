/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.component;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.custom.div.Div;
import org.apache.myfaces.custom.htmlTag.HtmlTag;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.web.beans.AbstractPaginator;
import org.esupportail.commons.web.beans.Paginator;
import org.esupportail.commons.web.tags.TagException;
import org.esupportail.commons.web.tags.config.TagsConfigurator;
import org.springframework.util.StringUtils;


/**
 * This class is a div element. This div is the parent of the group paginator. 
 * @author cleprous
 */
public class UIPaginator extends Div {

	/*
	 ******************* PROPERTIES ******************* */

	/**
	 * The type Component.
	 */
	public static final String COMPONENT_TYPE = "org.esupportail.commons.component.paginator";
	
	/**
	 * The attribute paginator.
	 */
	public static final String PAGINATOR_ATTR = "paginator";
	
	/**
	 * The attribute itemsName.
	 */
	public static final String ITEM_NAMES_ATTR = "itemsName";
	
	/**
	 * The attribute visibleBlocks.
	 */
	public static final String VISIBLE_BLOCKS_ATTR = "visibleBlocks";
	
	/**
	 * The attribute stringsVar.
	 */
	public static final String STRINGS_VAR_ATTR = "stringsVar";
	
	/**
	 * The attribute paginatorELExpression.
	 */
	public static final String PAGINATOR_EL_EXPRESSION_ATTR = "paginatorELExpression";
	
	/**
	 * The paginator.
	 */
	private Paginator< ? > paginator;
	
	/**
	 * The name of paginator.
	 * Ex.: myController.paginator
	 */
	private String paginatorELExpression;
	
	/**
	 * name of the items displayed.
	 */
	private String itemsName;
	
	/**
	 * The names of the blocks to show separated by a comma(,).
	 */
	private String visibleBlocks;
	
	/**
     * The name of the JSF variable that will hold the strings of the application.
     */
    private String stringsVar;
    
    /**
	 * Same as h:selectOneMenu. Used in itemsPerPage block to update the pageSize.
	 */
	private String onchange;
    
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/*
	 ******************* INIT ************************* */
	
	/**
	 * Constructor.
	 */
	public UIPaginator() {
		super();
	}

	/*
	 ******************* METHODS ********************** */
	
	/**
	 * @see javax.faces.component.UIComponentBase#saveState(javax.faces.context.FacesContext)
	 */
	@Override
	public Object saveState(final FacesContext context) {
		Object[] values = new Object[7];
		values[0] = super.saveState(context);
		values[1] = paginatorELExpression;
		values[2] = paginator;
		values[3] = itemsName;
		values[4] = visibleBlocks;
		values[5] = stringsVar;
		values[6] = onchange;
		return values;
	}

	/**
	 * @see javax.faces.component.UIComponentBase#restoreState(javax.faces.context.FacesContext, java.lang.Object)
	 */
	@Override
	public void restoreState(final FacesContext context, final Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		paginatorELExpression = (String) values[1];
		paginator = (AbstractPaginator< ? >) values[2];
		itemsName = (String) values[3];
		visibleBlocks = (String) values[4];
		stringsVar = (String) values[5];
		onchange = (String) values[6];
	}
	
	/**
	 * @throws IOException 
	 * @see org.apache.myfaces.component.html.ext.HtmlDataTable#encodeBegin(javax.faces.context.FacesContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void encodeBegin(final FacesContext arg0) throws IOException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entered encodeBegin for client-Id: " + getId());
		}
		if (!StringUtils.hasText(getStyleClass())) {
			setStyleClass(TagsConfigurator.getInstance().getPaginatorStyleClass());
		}		
		Paginator< ? > pagi = getPaginator();
		UIAbstractPaginatorStyle paginatorStyle = null;
		if (getChildren() != null && !getChildren().isEmpty()) {
			////this paginator can have only one son.
			getChildren().remove(0);
		}
		paginatorStyle = new UIPaginatorStyle();
		paginatorStyle.setParent(this);
		paginatorStyle.setAttributes(pagi, 
				getPaginatorELExpression(), 
				getItemsName(), 
				getVisibleBlocks(),
				getStringsVar(),
				getOnchange());
		getChildren().add(paginatorStyle);		
		if (logger.isDebugEnabled()) {
			logger.debug("Exited encodeBegin");
		}
		super.encodeBegin(arg0);
	}
	
	/**
	 * @see javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context.FacesContext)
	 */
	@Override
	public void encodeChildren(final FacesContext context) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entered encodeChildren for class: " + this.getClass() );
		}
		for (Object o : getChildren()) {
			UIComponentBase c = (UIComponentBase) o;
			c.encodeBegin(context); 
			c.encodeChildren(context);
			c.encodeEnd(context);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exited encodeChildren for class: " + this.getClass());
		}
	}
	
	/**
	 * @see javax.faces.component.UIComponent#getFamily()
	 */
	@Override
	public String getFamily() {
		return HtmlTag.COMPONENT_FAMILY;
	}
	
	/**
	 * @see javax.faces.component.UIComponentBase#getRendersChildren()
	 */
	@Override
	public boolean getRendersChildren() {
		return true;
	}
	
	/*
	 ******************* ACCESSORS ******************** */
	
	/**
	 * @return the paginator
	 */
	public Paginator< ? > getPaginator() {
		ValueBinding vb = getValueBinding(PAGINATOR_ATTR);
		if (vb != null) {
			try {
				return (Paginator< ? >) vb.getValue(getFacesContext());
			} catch (ClassCastException e) {
				throw new TagException("The attribute paginator must be a Paginator class.", e);
			}
		}
		return paginator;
	}

	/**
	 * @param paginator the paginator to set
	 */
	public void setPaginator(final Paginator< ? > paginator) {
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
	 * @param stringsVar the stringsVar to set
	 */
	public void setStringsVar(final String stringsVar) {
		this.stringsVar = stringsVar;
	}

	/**
	 * @return the stringsVar
	 */
	public String getStringsVar() {
		return stringsVar;
	}

	/**
	 * @return the paginatorELExpression
	 */
	public String getPaginatorELExpression() {
		return paginatorELExpression;
	}

	/**
	 * @param paginatorELExpression the paginatorELExpression to set
	 */
	public void setPaginatorELExpression(final String paginatorELExpression) {
		this.paginatorELExpression = paginatorELExpression;
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
