/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlOutputFormat;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.component.html.ext.HtmlSelectOneMenu;
import org.apache.myfaces.custom.div.Div;
import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.web.beans.Paginator;
import org.esupportail.commons.web.beans.PaginatorUtils;
import org.esupportail.commons.web.tags.TagUtils;
import org.esupportail.commons.web.tags.config.TagsConfigurator;
import org.springframework.util.StringUtils;


/**
 * This class makes the different elements of paginator.
 * @author cleprous
 *
 */
public abstract class UIAbstractPaginatorStyle extends UIComponentBase {

	/*
	 ******************* PROPERTIES ******************* */

	/**
	 * The type Component.
	 */
	public static final String COMPONENT_TYPE = "org.esupportail.commons.component.paginatorStyle";

	/**
	 * The component family.
	 */
	protected static final String COMPONENT_FAMILY = "org.esupportail.commons.tag"; 
	
	/**
	 * The block containing the paging system. (name and styleClass for this block)
	 */
	protected static final String NAVIGATION_BLOCK = "navigation";
	
	/**
	 * The separator attribute visibleBlock.
	 */
	private static final String VISIBLE_BLOCKS_SEPARATOR = ",";
	
	/**
	 * Key for the label of items numbers.
	 */
	private static final String ITEMS_NUMBERS_I18N_KEY = "PAGINATION.TEXT.ITEMS_NUMBERS";
	
	/**
	 * Key for the label of items per page.
	 */
	private static final String ITEMS_PER_PAGE_I18N_KEY = "PAGINATION.TEXT.ITEMS_PER_PAGE";
	
	/**
	 * The block containing the total number of items. (name and styleclass for this block)
	 */
	private static final String ITEMS_NUMBERS_BLOCK = "itemsNumbers";
	
	/**
	 * The block can change the number of items per page. (name and styleclass for this block)
	 */
	private static final String ITEMS_PER_PAGE_BLOCK = "itemsPerPage";
	
	/**
	 * The paginator.
	 */
	private Paginator< ? > paginator;
	
	/**
	 * The EL expression of the paginator.
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
	public UIAbstractPaginatorStyle() {
		super();
	}

	/*
	 ******************* METHODS ********************** */
	
	/**
	 * Set all attributes for UIAbstractPaginatorStyle.
	 * @param pagi
	 * @param thePaginatorELExpression
	 * @param theItemsName
	 * @param theVisibleBlocks
	 * @param theStringsVar 
	 * @param onChangeCode 
	 */
	public void setAttributes(final Paginator< ? > pagi,
			final String thePaginatorELExpression,
			final String theItemsName,
			final String theVisibleBlocks,
			final String theStringsVar,
			final String onChangeCode) {
		paginator = pagi;
		paginatorELExpression = thePaginatorELExpression;
		itemsName = theItemsName;
		visibleBlocks = theVisibleBlocks;
		stringsVar = theStringsVar;
		onchange = onChangeCode;
	}
	
	/**
	 * @see org.apache.myfaces.component.html.ext.HtmlDataTable#encodeBegin(javax.faces.context.FacesContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void encodeBegin(final FacesContext arg0)  {
		if (logger.isDebugEnabled()) {
			logger.debug("Entered encodeBegin for client-Id: " + getId());
		}
		List<UIComponent> allchildren = makePaginator(arg0.getApplication());
		this.getChildren().addAll(allchildren);
		if (logger.isDebugEnabled()) {
			logger.debug("Exited encodeBegin");
		}
	}

	/**
	 * Make a paginator component. 
	 * @param a
	 * @return List< UIComponent>
	 */
	private List<UIComponent> makePaginator(final Application a) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entered makePaginator");
		}
		List<UIComponent> allchildren = new ArrayList<UIComponent>();
		//The order of creation of div is important.
		//empty = default value then displays all the blocks
		if (!StringUtils.hasText(visibleBlocks)) {
			allchildren.add(makeDivNbItems(a));
			allchildren.add(makeDivPages(a));
			allchildren.add(makeDivNbItemsByPage(a));
		} else {
			String[] blocks = visibleBlocks.split(Pattern.quote(VISIBLE_BLOCKS_SEPARATOR));
			for (int i = 0; i < blocks.length; ++i) {
				String block = blocks[i];
				if (ITEMS_NUMBERS_BLOCK.equals(block)) {
					allchildren.add(makeDivNbItems(a));
				} else if (NAVIGATION_BLOCK.equals(block)) {
					allchildren.add(makeDivPages(a));
				} else if (ITEMS_PER_PAGE_BLOCK.equals(block)) {
					allchildren.add(makeDivNbItemsByPage(a));
				} else {
					if (block.contains(ITEMS_NUMBERS_BLOCK) || block.contains(NAVIGATION_BLOCK)
									|| block.contains(ITEMS_PER_PAGE_BLOCK)) {
						throw new IllegalArgumentException(
								"The values of the attribute visibleBlocks" 
								+ " must be separated by a comma with no space");
					}
					throw new IllegalArgumentException(
							"The attribute visibleBlocks can only contain" 
							+ " the following values: " + ITEMS_NUMBERS_BLOCK + ", " 
							+ NAVIGATION_BLOCK + ", " + ITEMS_PER_PAGE_BLOCK);
				}
			}
				
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exited makePaginator");
		}
		return allchildren;
	}

	/**
	 * Make div displaying the total number Item.
	 * @param a
	 * @return Div
	 */
	@SuppressWarnings("unchecked")
	protected Div makeDivNbItems(final Application a) {
		Div divNbItems = (Div) a.createComponent(Div.COMPONENT_TYPE);
		divNbItems.setParent(this);
		divNbItems.setStyleClass(ITEMS_NUMBERS_BLOCK);
		
		HtmlOutputFormat span = (HtmlOutputFormat) a.createComponent(HtmlOutputFormat.COMPONENT_TYPE);
		span.setValueBinding(JSFAttr.VALUE_ATTR, a.createValueBinding(
				TagUtils.makeELExpression(getStringsVar() + "['" + ITEMS_NUMBERS_I18N_KEY + "']")));
		span.setParent(divNbItems);
		span.setStyleClass(TagsConfigurator.getInstance().getTextStyleClass());
		UIParameter param = (UIParameter) a.createComponent(UIParameter.COMPONENT_TYPE);		
		if (UIComponentTag.isValueReference(getItemsName())) {
			param.setValueBinding(JSFAttr.VALUE_ATTR, a.createValueBinding(getItemsName()));
		} else {
			param.setValue(getItemsName());
		}
		param.setParent(span);
		
		UIParameter param1 = (UIParameter) a.createComponent(UIParameter.COMPONENT_TYPE);
		int firstPage = getPaginator().getFirstVisibleNumber() + 1;
		param1.setValue(firstPage);
		param1.setParent(span);

		UIParameter param2 = (UIParameter) a.createComponent(UIParameter.COMPONENT_TYPE);
		int lastPage = getPaginator().getLastVisibleNumber() + 1;
		param2.setValue(lastPage);
		param2.setParent(span);
		
		UIParameter param3 = (UIParameter) a.createComponent(UIParameter.COMPONENT_TYPE);
		param3.setValue(getPaginator().getTotalItemsCount());
		param3.setParent(span);
		
		span.getChildren().add(param);
		span.getChildren().add(param1);
		span.getChildren().add(param2);
		span.getChildren().add(param3);
		
		divNbItems.getChildren().add(span);
		
		return divNbItems;
	}
	
	/**
	 * Make Div which allows to navigate between different pages.
	 * @param a
	 * @return Div
	 */
	protected Div makeDivPages(
			@SuppressWarnings("unused")
			final Application a) {
		return null;
	}
	
	/**
	 * Make div which contains the menu to choose the number of items displayed per page.
	 * @param a
	 * @return Div
	 */
	@SuppressWarnings("unchecked")
	protected Div makeDivNbItemsByPage(final Application a) {
		Div divNbItemsByPage = (Div) a.createComponent(Div.COMPONENT_TYPE);
		divNbItemsByPage.setParent(this);
		divNbItemsByPage.setStyleClass(ITEMS_PER_PAGE_BLOCK);
		
		HtmlOutputFormat span = (HtmlOutputFormat) a.createComponent(HtmlOutputFormat.COMPONENT_TYPE);
		span.setValueBinding(JSFAttr.VALUE_ATTR, a.createValueBinding(
				TagUtils.makeELExpression(getStringsVar() + "['" + ITEMS_PER_PAGE_I18N_KEY + "']")));
		span.setParent(divNbItemsByPage);
		span.setStyleClass(TagsConfigurator.getInstance().getTextStyleClass());
		
		UIParameter param = (UIParameter) a.createComponent(UIParameter.COMPONENT_TYPE);
		if (UIComponentTag.isValueReference(getItemsName())) {
			param.setValueBinding(JSFAttr.VALUE_ATTR, a.createValueBinding(getItemsName()));
		} else {
			param.setValue(getItemsName());
		}
		param.setParent(span);
		
		span.getChildren().add(param);
		
		HtmlSelectOneMenu selectMenu = (HtmlSelectOneMenu) a.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
		selectMenu.setValueBinding(JSFAttr.VALUE_ATTR, a.createValueBinding(
				TagUtils.makeELExpression(getPaginatorELExpression() 
								+ "." + PaginatorUtils.PAGE_SIZE)));
		selectMenu.setParent(divNbItemsByPage);
		if (StringUtils.hasText(getOnchange())) {
			selectMenu.setOnchange(getOnchange());
		}
		
		
		for (SelectItem item : getPaginator().getPageSizeItems()) {
			UISelectItem selectItem = (UISelectItem) a.createComponent(UISelectItem.COMPONENT_TYPE); 
			selectItem.setItemLabel(item.getLabel());
			selectItem.setItemValue(item.getValue());
			
			selectMenu.getChildren().add(selectItem);
		}
		
		divNbItemsByPage.getChildren().add(span);
		divNbItemsByPage.getChildren().add(selectMenu);
		
		return divNbItemsByPage;
	}
	
	/**
	 * @see javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context.FacesContext)
	 */
	@Override
	public void encodeChildren(final FacesContext context) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entered encodeChildren for client-Id: ");
		}
		for (Object o : getChildren()) {
			UIComponentBase c = (UIComponentBase) o;
			c.encodeBegin(context);
			TagUtils.recursiveEncodechildren(c, context);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exited encodeChildren");
		}
	}
	
	/**
	 * @see org.apache.myfaces.component.html.ext.HtmlDataTable#encodeEnd(javax.faces.context.FacesContext)
	 */
	@Override
	public void encodeEnd(final FacesContext arg0) throws IOException {
		super.encodeEnd(arg0);
	}

	/**
	 * @see javax.faces.component.UIComponent#getFamily()
	 */
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	/*
	 ******************* ACCESSORS ******************** */

	/**
	 * @return the paginator
	 */
	public Paginator< ? > getPaginator() {
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
	 * @return the stringsVar
	 */
	protected String getStringsVar() {
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
