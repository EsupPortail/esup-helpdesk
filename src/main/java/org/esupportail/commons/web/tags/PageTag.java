/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import java.io.IOException;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentBodyTag;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import org.apache.myfaces.application.MyfacesStateManager;
import org.apache.myfaces.application.jsp.JspViewHandlerImpl;
import org.apache.myfaces.portlet.PortletUtil;
import org.apache.myfaces.shared_impl.renderkit.html.HTML;
import org.apache.myfaces.shared_impl.renderkit.html.HtmlLinkRendererBase;
import org.apache.myfaces.shared_impl.util.LocaleUtils;
import org.esupportail.commons.exceptions.WebFlowException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.commons.web.renderers.FooterRenderer;
import org.esupportail.commons.web.tags.config.TagsConfigurator;
import org.springframework.util.StringUtils;

/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * ESUP-Portail implementation of the page tag.
 * 
 * This class is a very few-modified copy of class org.apache.myfaces.taglib.core.ViewTag.java.
 * It would be much simpler if it could inherit from it, but it is impossible:
 * - ViewTag.doEndTag() calls responseWriter.endDocument(), which throws an exception since
 *   custom tags are not allowed to flush.
 * - One could think of overriding doEndTag(), but it is impossible since we must call
 *   private method UIComponentBase.release(), and this only possible by calling ViewTag.doEndTag()
 *   (since super.super.doEndTag() is not possible in Java).
 * So the only things changed is method doEndTag(), as well as doStartTag().
 */
public class PageTag extends UIComponentBodyTag {

	/**
	 * The name of the attribute to store the current menu item.
	 */
	private static final String CURRENT_MENU_ITEM_ATTRIBUTE = PageTag.class.getName() + ".menuItem";

	/**
	 * The 'html' tag.
	 */
	private static final String HTML_TAG = "html";

	/**
	 * The 'head' tag.
	 */
	private static final String HEAD_TAG = "head";

	/**
	 * The 'title' tag.
	 */
	private static final String TITLE_TAG = "title";

	/**
	 * The 'body' tag.
	 */
	private static final String BODY_TAG = "body";

	/**
	 * The default value of the 'stringsVar' attribute.
	 */
	private static final String DEFAULT_STRINGS_VAR = "msgs";

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The locale, passed as a string.
	 */
	private String localeString;

	/**
	 * The locale.
	 */
	private Locale locale;

	/**
	 * The name of the JSF variable that will hold the strings of the application.
	 */
	private String stringsVar;

	/**
	 * The string to display at the end of the page.
	 */
	private String footer;

	/**
	 * False to print a message that tells the user that
	 * he is not allowed to view the page.
	 */
	private String authorizedString;

	/**
	 * The download id, passed as a string.
	 */
	private String downloadIdString;

	/**
	 * The download id.
	 */
	private Long downloadId;

	/**
	 * Bean constructor.
	 */
	public PageTag() {
		super();
	}

	/**
	 * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {
		if (!isAuthorized()) {
			throw new WebFlowException("Page not allowed");
		}
		super.setProperties(component);
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		if (localeString != null) {
			if (UIComponentTag.isValueReference(localeString)) {
				ValueBinding vb = application.createValueBinding(localeString);
				Object localeValue = vb.getValue(context);
				if (localeValue instanceof Locale) {
					locale = (Locale) localeValue;
				} else if (localeValue instanceof String) {
					locale = LocaleUtils.toLocale((String) localeValue);
				} else {
					if (localeValue != null) {
						throw new IllegalArgumentException(
								"Locale or String class expected. Expression: " + localeString
								+ ". Class: " + localeValue.getClass().getName());
					}
					locale = getDefaultLocale();
				}
			} else {
				locale = LocaleUtils.toLocale(localeString);
			}
		} else {
			locale = getDefaultLocale();
		}
		if (footer != null) {
			if (UIComponentTag.isValueReference(footer)) {
				ValueBinding vb = application.createValueBinding(footer);
				Object footerValue = vb.getValue(context);
				footer = (String) footerValue;
			}
		}
		// set the locale of the view
		UIViewRoot view = (UIViewRoot) component;
		view.setLocale(locale);
		Config.set((ServletRequest) getFacesContext().getExternalContext().getRequest(),
				Config.FMT_LOCALE, locale);

		component.setRendered(true);

		// set the strings variable
		if (!StringUtils.hasText(stringsVar)) {
			stringsVar = DEFAULT_STRINGS_VAR;
			logger.warn(getClass() + ": no stringsVar set, using default ["
					+ stringsVar + "]");
		}

		downloadId = null;
		if (downloadIdString != null) {
			if (UIComponentTag.isValueReference(downloadIdString)) {
				ValueBinding vb = application.createValueBinding(downloadIdString);
				Object downloadIdValue = vb.getValue(context);
				if (downloadIdValue != null) {
					if (downloadIdValue instanceof Long) {
						downloadId = (Long) downloadIdValue;
					} else if (downloadIdValue instanceof String) {
						downloadId = Long.valueOf(downloadIdValue.toString());
					} else {
						throw new IllegalArgumentException(
								"Long or String class expected. Expression: " + downloadIdString
								+ ". Class: " + downloadIdValue.getClass().getName());
					}
					locale = getDefaultLocale();
				}
			}
		}
	}

	/**
	 * @return The current menu item.
	 */
	public static String getCurrentMenuItem() {
		return (String) ContextUtils.getRequestAttribute(CURRENT_MENU_ITEM_ATTRIBUTE);
	}

	/**
	 * @see javax.faces.webapp.UIComponentTag#getComponentType()
	 */
	@Override
	public String getComponentType() {
		return UIViewRoot.COMPONENT_TYPE;
	}

	/**
	 * @see javax.faces.webapp.UIComponentTag#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return null;
	}

	/**
	 * @return true if the page is authorized.
	 */
	private boolean isAuthorized() {
		Boolean authorized = TagUtils.getBooleanAttributeValue("authorized", authorizedString);
		if (authorized == null) {
			return true;
		}
		return authorized.booleanValue();
	}

	/**
	 * @see javax.faces.webapp.UIComponentTag#doStartTag()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
		if (logger.isDebugEnabled()) {
			logger.debug("entering PageTag.doStartTag");
		}
		super.doStartTag();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		TagsConfigurator tagsConfigurator = TagsConfigurator.getInstance();
		facesContext.getExternalContext().getRequestMap().put(stringsVar, tagsConfigurator.getStrings(locale));
		ResponseWriter responseWriter = facesContext.getResponseWriter();
		if (logger.isDebugEnabled()) {
			logger.debug("doStartTag().facesContext=" + facesContext);
			logger.debug("doStartTag().responseWriter=" + responseWriter.getClass());
		}
		boolean isPortlet = PortletUtil.isPortletRequest(facesContext);
		if (logger.isDebugEnabled()) {
			logger.debug("isPortlet=" + isPortlet);
		}
		try {
			responseWriter.startDocument();
			UIViewRoot view = (UIViewRoot) getComponentInstance();
			if (!isPortlet) {
				responseWriter.write(tagsConfigurator.getDoctype());
				responseWriter.startElement(HTML_TAG, view);
				responseWriter.writeAttribute(HTML.LANG_ATTR, view.getLocale().getLanguage(), null);
				responseWriter.startElement(HEAD_TAG, view);
				responseWriter.startElement(TITLE_TAG, null);
				String title;
				if (tagsConfigurator.getDocumentTitleI18nKey() != null) {
					Application application = facesContext.getApplication();
					String exp = TagUtils.makeELExpression(
							getStringsVar() + "['"
							                    + tagsConfigurator.getDocumentTitleI18nKey() + "']");
					ValueBinding valueBinding = application.createValueBinding(exp);
					title = (String) valueBinding.getValue(facesContext);
				} else {
					title = tagsConfigurator.getDocumentTitle();
				}
				responseWriter.write(title);
				responseWriter.endElement(TITLE_TAG);
				for (String stylesheet : tagsConfigurator.getStylesheets()) {
					responseWriter.startElement(HTML.LINK_ELEM, null);
					responseWriter.writeAttribute(HTML.REL_ATTR, "stylesheet", null);
					responseWriter.writeAttribute(HTML.TYPE_ATTR, "text/css", null);
					String stylesheetUrl;
					if (stylesheet.startsWith("http://")
							|| stylesheet.startsWith(".")
							|| stylesheet.startsWith("/")) {
						stylesheetUrl = stylesheet;
					} else {
						stylesheetUrl = tagsConfigurator.getMediaPath()
						+ "/" + stylesheet;
					}
					responseWriter.writeAttribute(HTML.HREF_ATTR, stylesheetUrl, null);
					responseWriter.endElement(HTML.LINK_ELEM);
				}
				responseWriter.startElement("meta", null);
				responseWriter.writeAttribute("http-equiv", "X-UA-Compatible", null);
				responseWriter.writeAttribute("content", "IE=7", null);
				responseWriter.endElement("meta");
			}
			if (logger.isDebugEnabled()) {
				logger.debug("writing scripts...");
			}
			for (String script : tagsConfigurator.getScripts()) {
				if (logger.isDebugEnabled()) {
					logger.debug("script=" + script);
				}
				responseWriter.startElement(HTML.SCRIPT_ELEM, null);
				responseWriter.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
				String scriptUrl;
				if (script.startsWith("http://") || script.startsWith(".") || script.startsWith("/")) {
					scriptUrl = script;
				} else {
					scriptUrl = tagsConfigurator.getMediaPath() + "/" + script;
				}
				responseWriter.writeAttribute(HTML.SRC_ATTR, scriptUrl, null);
				responseWriter.endElement(HTML.SCRIPT_ELEM);
			}
			if (isPortlet) {
				responseWriter.startElement(HTML.DIV_ELEM, view);
			} else {
				responseWriter.endElement(HEAD_TAG);
				responseWriter.startElement(BODY_TAG, view);
			}
			addBodyStyleInformation(tagsConfigurator, responseWriter);
			responseWriter.flush();
		} catch (IOException e) {
			throw new JspException("Error writing startDocument", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("leaving PageTag.doStartTag");
		}
		return BodyTag.EVAL_BODY_BUFFERED;
	}

	/**
	 * @param tagsConfigurator
	 * @param responseWriter
	 * @throws IOException
	 */
	protected void addBodyStyleInformation(TagsConfigurator tagsConfigurator, ResponseWriter responseWriter)
	throws IOException {
		responseWriter.writeAttribute(HTML.CLASS_ATTR,
				tagsConfigurator.getDocumentBodyStyleClass(), null);
	}

	/**
	 * @see javax.faces.webapp.UIComponentTag#isSuppressed()
	 */
	@Override
	protected boolean isSuppressed() {
		return true;
	}

	/**
	 * @see javax.faces.webapp.UIComponentTag#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		if (logger.isDebugEnabled()) {
			logger.debug("entering PageTag.doEndTag");
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResponseWriter responseWriter = facesContext.getResponseWriter();
		boolean isPortlet = PortletUtil.isPortletRequest(facesContext);
		UIComponent uiComponent = getComponentInstance();
		if (logger.isDebugEnabled()) {
			logger.debug("doStartTag().facesContext=" + facesContext);
		}

		// this was added
		try {
			addFooter(facesContext, uiComponent);
			if (downloadId != null) {
				responseWriter.startElement(HTML.SCRIPT_ELEM, null);
				responseWriter.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
				Application application = facesContext.getApplication();
				String popupBlockerMessage = application.createValueBinding(
						"#{" + stringsVar + "['POPUP_BLOCKER_MESSAGE']}"
				).getValue(facesContext).toString();
				responseWriter.write("openDownload('"
						+ DownloadUtils.getDownloadUrl(downloadId) + "', '"
						+ popupBlockerMessage + "');");
				responseWriter.endElement(HTML.SCRIPT_ELEM);
			}
			if (isPortlet) {
				responseWriter.endElement(HTML.DIV_ELEM);
			} else {
				responseWriter.endElement(BODY_TAG);
				responseWriter.endElement(HTML_TAG);
			}
		} catch (IOException e) {
			throw new JspException("Error writing endDocument", e);
		}

		try {
			responseWriter.endDocument();
		} catch (IOException e) {
			logger.error("Error writing endDocument", e);
			throw new JspException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("leaving PageTag.doEndTag");
		}
		return super.doEndTag();
	}

	/**
	 * @param facesContext
	 * @param uiComponent
	 * @throws IOException
	 */
	protected void addFooter(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		if (footer != null) {
			FooterRenderer.encodeFooter(facesContext, uiComponent, footer);
		} else {
			FooterRenderer.encodeFooter(
					facesContext, getComponentInstance(),
					TagsConfigurator.getInstance().getFooterText());
		}
	}

	/**
	 * @see javax.faces.webapp.UIComponentBodyTag#doAfterBody()
	 */
	@Override
	public int doAfterBody() throws JspException {
		if (logger.isDebugEnabled()) {
			logger.debug("entering PageTag.doAfterBody");
		}
		try {
			BodyContent myBodyContent = getBodyContent();
			if (myBodyContent != null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				StateManager stateManager = facesContext.getApplication().getStateManager();
				StateManager.SerializedView serializedView
				= stateManager.saveSerializedView(facesContext);
				if (serializedView != null) {
					//until now we have written to a buffer
					ResponseWriter bufferWriter = facesContext.getResponseWriter();
					bufferWriter.flush();
					//now we switch to real output
					ResponseWriter realWriter = bufferWriter.cloneWithWriter(getPreviousOut());
					facesContext.setResponseWriter(realWriter);

					String bodyStr = myBodyContent.getString();
					if (stateManager.isSavingStateInClient(facesContext)) {
						int formMarker = bodyStr.indexOf(JspViewHandlerImpl.FORM_STATE_MARKER);
						int urlMarker = bodyStr.indexOf(HtmlLinkRendererBase.URL_STATE_MARKER);
						int lastMarkerEnd = 0;
						while ((formMarker != -1) || (urlMarker != -1)) {
							if ((urlMarker == -1) || ((formMarker != -1) && (formMarker < urlMarker))) {
								//replace form_marker
								realWriter.write(bodyStr, lastMarkerEnd, formMarker - lastMarkerEnd);
								stateManager.writeState(facesContext, serializedView);
								lastMarkerEnd = formMarker + JspViewHandlerImpl.FORM_STATE_MARKER_LEN;
								formMarker = bodyStr.indexOf(JspViewHandlerImpl.FORM_STATE_MARKER, lastMarkerEnd);
							} else {
								//replace url_marker
								realWriter.write(bodyStr, lastMarkerEnd, urlMarker - lastMarkerEnd);
								if (stateManager instanceof MyfacesStateManager) {
									((MyfacesStateManager) stateManager).writeStateAsUrlParams(facesContext,
											serializedView);
								} else {
									logger.error(
											"Current StateManager is no MyfacesStateManager and does not support saving state in url parameters.");
								}
								lastMarkerEnd = urlMarker + HtmlLinkRendererBase.URL_STATE_MARKER_LEN;
								urlMarker = bodyStr.indexOf(HtmlLinkRendererBase.URL_STATE_MARKER, lastMarkerEnd);
							}
						}
						realWriter.write(bodyStr, lastMarkerEnd, bodyStr.length() - lastMarkerEnd);
					} else {
						realWriter.write(bodyStr);
					}
				} else {
					myBodyContent.writeOut(getPreviousOut());
				}
			}
		} catch (IOException e) {
			throw new JspException("Error writing body content", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("leaving PageTag.doAfterBody");
		}
		return super.doAfterBody();
	}

	/**
	 * @return the default locale to apply.
	 */
	private Locale getDefaultLocale() {
		TagsConfigurator tagsConfigurator = TagsConfigurator.getInstance();
		return tagsConfigurator.getLocale();
	}

	/**
	 * @param authorized the authorized to set
	 */
	public void setAuthorized(final String authorized) {
		authorizedString = authorized;
	}

	/**
	 * @param locale
	 */
	public void setLocale(final String locale) {
		localeString = locale;
	}

	/**
	 * @param menuItem
	 */
	public void setMenuItem(final String menuItem) {
		ContextUtils.setRequestAttribute(CURRENT_MENU_ITEM_ATTRIBUTE, menuItem);
	}

	/**
	 * @param stringsVar the stringsVar to set
	 */
	public void setStringsVar(final String stringsVar) {
		this.stringsVar = stringsVar;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(final String footer) {
		this.footer = footer;
	}

	/**
	 * @return the stringsVar
	 */
	public String getStringsVar() {
		return stringsVar;
	}

	/**
	 * @param downloadId the downloadId to set
	 */
	public void setDownloadId(final String downloadId) {
		downloadIdString = downloadId;
	}

}
