/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.mock;

import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

/**
 * A faces context mock.
 */
public class MockFacesContext extends FacesContext {

	/**
	 * The external context.
	 */
	private ExternalContext externalContext;

	/**
	 * The application.
	 */
	private Application application;

	/**
	 * A view root.
	 */
	private UIViewRoot viewRoot;

	/**
	 * Constructor.
	 */
	public MockFacesContext() {
		super();
	}

	/**
	 * @see javax.faces.context.FacesContext#getApplication()
	 */
	@Override
	public Application getApplication() {
		return application;
	}

	/**
	 * @param application
	 */
	public void setApplication(final Application application) {
		this.application = application;
	}

	/**
	 * @see javax.faces.context.FacesContext#getClientIdsWithMessages()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Iterator getClientIdsWithMessages() {
		return null;
	}

	/**
	 * @see javax.faces.context.FacesContext#getExternalContext()
	 */
	@Override
	public ExternalContext getExternalContext() {
		return externalContext;
	}

	/**
	 * @param externalContext
	 */
	public void setExternalContext(final ExternalContext externalContext) {
		this.externalContext = externalContext;
	}

	/**
	 * @see javax.faces.context.FacesContext#getMaximumSeverity()
	 */
	@Override
	public Severity getMaximumSeverity() {
		return null;
	}

	/**
	 * @see javax.faces.context.FacesContext#getMessages()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Iterator getMessages() {
		return null;
	}

	/**
	 * @see javax.faces.context.FacesContext#getMessages(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Iterator getMessages(
			@SuppressWarnings("unused")
			final String arg0) {
		return null;
	}

	/**
	 * @see javax.faces.context.FacesContext#getRenderKit()
	 */
	@Override
	public RenderKit getRenderKit() {
		return null;
	}

	/**
	 * @see javax.faces.context.FacesContext#getRenderResponse()
	 */
	@Override
	public boolean getRenderResponse() {
		return false;
	}

	/**
	 * @see javax.faces.context.FacesContext#getResponseComplete()
	 */
	@Override
	public boolean getResponseComplete() {
		return false;
	}

	/**
	 * @see javax.faces.context.FacesContext#getResponseStream()
	 */
	@Override
	public ResponseStream getResponseStream() {
		return null;
	}

	/**
	 * @see javax.faces.context.FacesContext#setResponseStream(javax.faces.context.ResponseStream)
	 */
	@Override
	public void setResponseStream(
			@SuppressWarnings("unused")
			final ResponseStream arg0) {
		// do nothing
	}

	/**
	 * @see javax.faces.context.FacesContext#getResponseWriter()
	 */
	@Override
	public ResponseWriter getResponseWriter() {
		return null;
	}

	/**
	 * @see javax.faces.context.FacesContext#setResponseWriter(javax.faces.context.ResponseWriter)
	 */
	@Override
	public void setResponseWriter(
			@SuppressWarnings("unused")
			final ResponseWriter arg0) {
		// do nothing
	}

	/**
	 * @see javax.faces.context.FacesContext#getViewRoot()
	 */
	@Override
	public UIViewRoot getViewRoot() {
		return viewRoot;
	}

	/**
	 * @see javax.faces.context.FacesContext#setViewRoot(javax.faces.component.UIViewRoot)
	 */
	@Override
	public void setViewRoot(final UIViewRoot viewRoot) {
		this.viewRoot = viewRoot;
	}

	/**
	 * @see javax.faces.context.FacesContext#addMessage(java.lang.String, javax.faces.application.FacesMessage)
	 */
	@Override
	public void addMessage(
			@SuppressWarnings("unused")
			final String arg0, 
			@SuppressWarnings("unused")
			final FacesMessage arg1) {
		// do nothing
	}

	/**
	 * @see javax.faces.context.FacesContext#release()
	 */
	@Override
	public void release() {
		// do nothing
	}

	/**
	 * @see javax.faces.context.FacesContext#renderResponse()
	 */
	@Override
	public void renderResponse() {
		// do nothing
	}

	/**
	 * @see javax.faces.context.FacesContext#responseComplete()
	 */
	@Override
	public void responseComplete() {
		// do nothing
	}

}
