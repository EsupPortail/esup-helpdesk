/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.jsf; 

//see http://learnjsf.com/wp/2006/08/06/a-prg-phase-listener-for-jsf/

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.ContextUtils;

/**
 * A phase listener to implement the PRG pattern.
 */
public class RedirectPhaseListener extends AbstractPhaseListener {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4528533350927362247L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * Constructor.
	 */
	public RedirectPhaseListener() {
		super();
	}

	/**
	 * @see org.esupportail.commons.jsf.AbstractPhaseListener#beforePhaseInternal(javax.faces.event.PhaseEvent)
	 */
	@Override
	public void beforePhaseInternal(final PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		if (event.getPhaseId() != PhaseId.RENDER_RESPONSE) {
			return;
		}
		// Implement POST-REDIRECT-GET pattern
		if (!"POST".equals(getMethod(externalContext))) {
			return;
		}
		if (!ContextUtils.isServlet()) {
			return;
		}
		String nextViewID = facesContext.getViewRoot().getViewId();
		String nextViewURL = facesContext.getApplication().getViewHandler()
		.getActionURL(facesContext, nextViewID);
		if (logger.isDebugEnabled()) {
			logger.debug("Redirecting to " + nextViewURL);
		}
		try {
			event.getFacesContext().getExternalContext().redirect(nextViewURL);
		} catch (Throwable t) {
			logger.error("EXCEPTION: " + t.getMessage());
		}
	}

}
