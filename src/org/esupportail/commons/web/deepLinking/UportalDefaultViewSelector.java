/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.deepLinking;

import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;

import org.apache.myfaces.portlet.DefaultViewSelector;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.urlGeneration.AbstractUrlGenerator;
import org.esupportail.commons.services.urlGeneration.UportalUrlGeneratorImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * The default view selector, used for deep linking.
 */
public class UportalDefaultViewSelector implements DefaultViewSelector {
	
	/**
	 * The default name of the redirector.
	 */
	private static final String DEFAULT_REDIRECTOR_NAME = "deepLinkingRedirector";

	/**
	 * The name of the portlet parameter that gives the name of the redirector.
	 */
	private static final String REDIRECTOR_PARAM = "deep-linking-redirector";

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The portlet context.
	 */
	private PortletContext portletContext;
	
	/**
	 * The name of the redirector.
	 */
	private String redirectorName;

	/**
	 * Bean constructor.
	 */
	public UportalDefaultViewSelector() {
		super();
	}

	/**
	 * @param object
	 * @return The original ServletRequest of an object.
	 */
	private static ServletRequest getOriginalServletRequest(final Object object) {
		if (!(object instanceof ServletRequestWrapper)) {
			return null;
		}
		ServletRequestWrapper requestWrapper = (ServletRequestWrapper) object;
		ServletRequest wrappedRequest = requestWrapper.getRequest();
		ServletRequest wrappedWrappedRequest = getOriginalServletRequest(wrappedRequest);
		if (wrappedWrappedRequest != null) {
			return wrappedWrappedRequest;
		}
		return wrappedRequest;
	}
	
	/**
	 * @return the redirector
	 */
	private DeepLinkingRedirector getRedirector() {
		if (!StringUtils.hasText(redirectorName)) {
			redirectorName = portletContext.getInitParameter(REDIRECTOR_PARAM);
			if (!StringUtils.hasText(redirectorName)) {
				redirectorName = DEFAULT_REDIRECTOR_NAME;
				logger.info("property " + REDIRECTOR_PARAM 
						+ " is not set, using default value [" + redirectorName + "]");
			}
		}
        return (DeepLinkingRedirector) BeanUtils.getBean(redirectorName);
	}

	/**
	 * @param renderRequest 
	 * @return the parameters of the current request.
	 */
	private Map<String, String> getParams(
			final RenderRequest renderRequest) {
		ServletRequest servletRequest = getOriginalServletRequest(renderRequest);
		if (servletRequest == null) {
			return null;
		}
		String arg = servletRequest.getParameter(UportalUrlGeneratorImpl.ARGS_PARAM);
		if (logger.isDebugEnabled()) {
			logger.debug("getParams(): arg=[" + arg + "]");
		}
		Map<String, String> params = AbstractUrlGenerator.decodeArgToParams(arg);
		if (logger.isDebugEnabled()) {
			logger.debug("getParams(): params=" + params);
		}
		return params;
	}

	/**
	 * @see org.apache.myfaces.portlet.DefaultViewSelector#selectViewId(
	 * javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	public String selectViewId(
			final RenderRequest renderRequest, 
			@SuppressWarnings("unused")
			final RenderResponse renderResponse) {
		return getRedirector().redirect(getParams(renderRequest));
	}

	/**
	 * @see org.apache.myfaces.portlet.DefaultViewSelector#setPortletContext(javax.portlet.PortletContext)
	 */
	@Override
	public void setPortletContext(final PortletContext portletContext) {
		this.portletContext = portletContext;
	}

	/**
	 * @return the portletContext
	 */
	protected PortletContext getPortletContext() {
		return portletContext;
	}

}
