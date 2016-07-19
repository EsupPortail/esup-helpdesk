/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.component;


import javax.faces.application.Application;

import javax.faces.el.MethodBinding;

import org.apache.myfaces.component.html.ext.HtmlCommandButton;
import org.apache.myfaces.component.html.ext.HtmlOutputText;
import org.apache.myfaces.custom.div.Div;
import org.apache.myfaces.custom.htmlTag.HtmlTag;
import org.apache.myfaces.custom.updateactionlistener.UpdateActionListener;
import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.shared_impl.renderkit.html.HTML;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.web.beans.Paginator;
import org.esupportail.commons.web.beans.PaginatorUtils;
import org.esupportail.commons.web.tags.TagUtils;
import org.esupportail.commons.web.tags.config.TagsConfigurator;
import org.springframework.util.StringUtils;


/**
 * This class make the block navigation.
 * @author cleprous
 */
public class UIPaginatorStyle extends UIAbstractPaginatorStyle {

	/*
	 ******************* PROPERTIES ******************* */

	/**
	 * Key for the name of next button.
	 */
	private static final String NEXT_BUTTON_I18N_KEY = "_.BUTTON.NEXT";
	
	/**
	 * Key for the name of previous button.
	 */
	private static final String PREVIOUS_BUTTON_I18N_KEY = "_.BUTTON.PREVIOUS";
	
	/**
	 * Key for the text who separating the different lists of pages.
	 */
	private static final String TEXT_SEPARATOR_I18N_KEY = "PAGINATION.TEXT.SEPARATOR";
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/*
	 ******************* INIT ************************* */

	/**
	 * Constructor.
	 */
	public UIPaginatorStyle() {
		super();
	}

	/*
	 ******************* METHODS ********************** */

	/** 
	 * @see org.esupportail.commons.web.component.UIAbstractPaginatorStyle#makeDivPages(
	 * javax.faces.application.Application)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Div makeDivPages(final Application a) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering makeDivPagination(" + a + ")");
		}
		Div divPages = (Div) a.createComponent(Div.COMPONENT_TYPE);
		divPages.setParent(this);
		divPages.setStyleClass(NAVIGATION_BLOCK);
		//if there are more than one page on display pagination
		if (getPaginator().getFirstPageNumber() != getPaginator().getLastPageNumber()) {
			HtmlTag ul1 = (HtmlTag) a.createComponent(HtmlTag.COMPONENT_TYPE);
			ul1.setValue(HTML.UL_ELEM);
			ul1.setParent(divPages);
			//PREVIOUS BUTTON
			if (getPaginator().getCurrentPage() 
					!= getPaginator().getFirstPageNumber()) {
				ul1.getChildren().add(makeLiGroup(a, 
								getStringsVar() + "['" 
								+ PREVIOUS_BUTTON_I18N_KEY + "']", 
								null, true,
								PaginatorUtils.GOTO_PREVIOUS,
								null, null));
			}
			//BODY of Paginator
			divPages = makePages(divPages, getPaginator(), ul1, a);
		}
		return divPages;
	}
	
	/**
	 * @param divPagination
	 * @param pagi
	 * @param ul1
	 * @param a
	 * @return Div
	 */
	@SuppressWarnings("unchecked")
	private Div makePages(final Div divPagination,
					@SuppressWarnings("rawtypes") final Paginator pagi, 
					final HtmlTag ul1, final Application a) {
		Div divTemp = divPagination;
		if (!pagi.getFirstPagesNumber().isEmpty()) {
			for (Object o : pagi.getFirstPagesNumber()) {
				Integer page = (Integer) o;
				if (page != pagi.getCurrentPage()) {
					ul1.getChildren().add(
							makeLiGroup(a, "" + (page + 1), "" + page,
									false,
									PaginatorUtils.RELOAD_DATA,
									PaginatorUtils.CURRENT_PAGE, null));
				} else { 

					ul1.getChildren().add(
							makeLiGroup(a, "" + (page + 1), "" + page,
									false,
									PaginatorUtils.RELOAD_DATA,
									PaginatorUtils.CURRENT_PAGE,
									PaginatorUtils.CURRENT_PAGE));
				}
			}
			ul1.getChildren().add(makeLiText(a));
			String nameValueButton = getPaginatorELExpression() 
					+ "." + PaginatorUtils.LAST_PAGE_NUMBER + "+1";
			ul1.getChildren().add(
					makeLiGroup(a, nameValueButton, PaginatorUtils.LAST_PAGE_NUMBER,
							true,
							PaginatorUtils.RELOAD_DATA,
							PaginatorUtils.CURRENT_PAGE, null));
		} else if (!pagi.getLastPagesNumber().isEmpty()) {
			String nameValueButton = getPaginatorELExpression() 
					+ "." + PaginatorUtils.FIRST_PAGE_NUMBER + "+1";
			ul1.getChildren().add(
					makeLiGroup(a, nameValueButton, PaginatorUtils.FIRST_PAGE_NUMBER,
							true,
							PaginatorUtils.RELOAD_DATA,
							PaginatorUtils.CURRENT_PAGE, null));
			ul1.getChildren().add(makeLiText(a));
			for (Object o : pagi.getLastPagesNumber()) {
				Integer page = (Integer) o;
				if (page != pagi.getCurrentPage()) {
					ul1.getChildren().add(
							makeLiGroup(a, "" + (page + 1), "" + page,
									false,
									PaginatorUtils.RELOAD_DATA,
									PaginatorUtils.CURRENT_PAGE, null));
				} else { 
					ul1.getChildren().add(
							makeLiGroup(a, "" + (page + 1), "" + page,
									false,
									PaginatorUtils.RELOAD_DATA,
									PaginatorUtils.CURRENT_PAGE,
									PaginatorUtils.CURRENT_PAGE));
				}
			}
		} else if (!pagi.getMiddlePagesNumber().isEmpty()) {
			String nameValueButton = getPaginatorELExpression() 
					+ "." + PaginatorUtils.FIRST_PAGE_NUMBER + "+1";
			ul1.getChildren().add(
					makeLiGroup(a, nameValueButton, PaginatorUtils.FIRST_PAGE_NUMBER,
							true,
							PaginatorUtils.RELOAD_DATA,
							PaginatorUtils.CURRENT_PAGE, null));
			ul1.getChildren().add(makeLiText(a));
			for (Object o : pagi.getMiddlePagesNumber()) {
				Integer page = (Integer) o;
				if (page != pagi.getCurrentPage()) {
					ul1.getChildren().add(
							makeLiGroup(a, "" + (page + 1), "" + page,
									false,
									PaginatorUtils.RELOAD_DATA,
									PaginatorUtils.CURRENT_PAGE, null));
				} else { 

					ul1.getChildren().add(
							makeLiGroup(a, "" + (page + 1), "" + page,
									false,
									PaginatorUtils.RELOAD_DATA,
									PaginatorUtils.CURRENT_PAGE,
									PaginatorUtils.CURRENT_PAGE));
				}
			}
			ul1.getChildren().add(makeLiText(a));
			nameValueButton = getPaginatorELExpression() 
					+ "." + PaginatorUtils.LAST_PAGE_NUMBER + "+1";
			ul1.getChildren().add(
					makeLiGroup(a, nameValueButton, PaginatorUtils.LAST_PAGE_NUMBER,
							true,
							PaginatorUtils.RELOAD_DATA,
							PaginatorUtils.CURRENT_PAGE, null));
		} else {
			//in the case of a paginator with very little page
			for (Object o : pagi.getNearPages()) {
				Integer page = (Integer) o;
				if (page != pagi.getCurrentPage()) {
					ul1.getChildren().add(
							makeLiGroup(a, "" + (page + 1), "" + page,
									false,
									PaginatorUtils.RELOAD_DATA,
									PaginatorUtils.CURRENT_PAGE, null));
				} else { 

					ul1.getChildren().add(
							makeLiGroup(a, "" + (page + 1), "" + page,
									false,
									PaginatorUtils.RELOAD_DATA,
									PaginatorUtils.CURRENT_PAGE,
									PaginatorUtils.CURRENT_PAGE));
				}
			}
		}
		if (getPaginator().getCurrentPage() != getPaginator().getLastPageNumber()) {
			ul1.getChildren().add(
					makeLiGroup(a, getStringsVar() + "['" + NEXT_BUTTON_I18N_KEY + "']",
							null,
							true,
							PaginatorUtils.GOTO_NEXT,
							null, null));
		}
		divTemp.getChildren().add(ul1);
		return divTemp;
	}

	/**
	 * @param a
	 * @param valueButton
	 * @param valueListener if a null or empty -> the listener is not created
	 * @param isValueBinding a true if valueButton and / or valueListener are valueBinding
	 * @param methodForButton 
	 * @param methodForListener if a null or empty -> the listener is not created
	 * @param styleClassForLi 
	 * @return HtmlTag : LI including (or not) a command Button with his Listener.
	 */
	@SuppressWarnings("unchecked")
	private HtmlTag makeLiGroup(final Application a, 
			final String valueButton,
			final String valueListener,
			final boolean isValueBinding,
			final String methodForButton, 
			final String methodForListener,
			final String styleClassForLi) {
		HtmlTag li = (HtmlTag) a.createComponent(HtmlTag.COMPONENT_TYPE);
		li.setValue(HTML.LI_ELEM);
		if (StringUtils.hasText(styleClassForLi)) {
			li.setStyleClass(styleClassForLi);
		}
		HtmlCommandButton command = (HtmlCommandButton)
		a.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		command.setImmediate(true);
		command.setParent(li);
		MethodBinding m = a.createMethodBinding(
				TagUtils.makeELExpression(
						getPaginatorELExpression() + "." + methodForButton)
						, null);
		command.setAction(m);
		if (isValueBinding) {
			command.setValueBinding(JSFAttr.VALUE_ATTR, 
					a.createValueBinding(
							TagUtils.makeELExpression(valueButton)));
		} else {
			command.setValue(valueButton);
		}
		if (StringUtils.hasText(methodForListener) && StringUtils.hasText(valueListener)) {
			UpdateActionListener listener = new UpdateActionListener();
			if (isValueBinding) {
				listener.setValueBinding(a.createValueBinding(
						TagUtils.makeELExpression(
								getPaginatorELExpression() 
								+ "." + valueListener)));
			} else {
				listener.setValue(valueListener);
			}
			listener.setPropertyBinding(a.createValueBinding(
					TagUtils.makeELExpression(
							getPaginatorELExpression() + "." + methodForListener )));
			command.addActionListener(listener);
		}
		li.getChildren().add(command);
		return li;
	}

	/**
	 * @param a
	 * @return HtmlTag : LI tag containing the text separator (3 small points).
	 */
	@SuppressWarnings("unchecked")
	private HtmlTag makeLiText(final Application a) {
		HtmlTag liText = (HtmlTag) a.createComponent(HtmlTag.COMPONENT_TYPE);
		liText.setValue(HTML.LI_ELEM);
		liText.setStyle("border:none;background-color:transparent;");
		
		HtmlOutputText text = (HtmlOutputText)
		a.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setValueBinding(
				JSFAttr.VALUE_ATTR,
				a.createValueBinding(
						TagUtils.makeELExpression(
								getStringsVar() + "['" 
								+ TEXT_SEPARATOR_I18N_KEY + "']")));
		text.setParent(liText);
		text.setStyleClass(TagsConfigurator.getInstance().getTextStyleClass());
		
		liText.getChildren().add(text);
		
		return liText;
	}
	/*
	 ******************* ACCESSORS ******************** */

}