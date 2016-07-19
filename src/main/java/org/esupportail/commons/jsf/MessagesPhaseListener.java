/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.jsf; 

//see http://learnjsf.com/wp/2006/08/06/a-prg-phase-listener-for-jsf/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.portlet.PortletRequest;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.ContextUtils;

/**
 * A phase listener to pass messages through the PRG pattern.
 */
public class MessagesPhaseListener extends AbstractPhaseListener {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2560498754887415967L;

	/**
	 * A name under which to save messages between the redirect and 
	 * the subsequent get.
	 */
	private final String sessionToken = getClass().getName() + ".messages";

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * Constructor.
	 */
	public MessagesPhaseListener() {
		super();
	}

	/**
	 * Debug an event.
	 * @param event
	 * @param string
	 */
	@Override
	protected void debugEvent(
			final PhaseEvent event, 
			final String string) {
		if (logger.isDebugEnabled()) {
			super.debugEvent(event, string);
			debugMessages(event);
		}
	}

	/**
	 * Debug an event.
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	protected void debugMessages(
			final PhaseEvent event) {
		if (logger.isDebugEnabled()) {
			FacesContext facesContext = event.getFacesContext();
			for (Iterator<String> i = facesContext.getClientIdsWithMessages(); i.hasNext();) {
				String clientId = i.next();
				for (Iterator<FacesMessage> j = facesContext.getMessages(clientId); j.hasNext();) {
					FacesMessage facesMessage = j.next();
					logger.debug("CONTEXT: [" + clientId + "]=>[" + facesMessage.getDetail() + "]");
				}
			}
			ExternalContext externalContext = facesContext.getExternalContext();
			Map<Object, Object>  sessionMap = externalContext.getSessionMap();
			Map<String, List<FacesMessage>> allMessages = 
				(Map<String, List<FacesMessage>>) sessionMap.get(sessionToken);
			if (allMessages == null) {
				return;
			}
			for (String clientId : allMessages.keySet()) {
				List<FacesMessage> clientMessages = allMessages.get(clientId);
				for (FacesMessage facesMessage : clientMessages) {
					logger.debug("SESSION: [" + clientId + "]=>[" + facesMessage.getDetail() + "]");
				}
			}
		}
	}

	/**
	 * @see org.esupportail.commons.jsf.AbstractPhaseListener#afterPhaseInternal(javax.faces.event.PhaseEvent)
	 */
	@Override
	protected void afterPhaseInternal(final PhaseEvent event) {
		// Save messages in session so they'll be available on the
		// subsequent GET request
		if (event.getPhaseId() == PhaseId.APPLY_REQUEST_VALUES
				|| event.getPhaseId() == PhaseId.PROCESS_VALIDATIONS
				|| event.getPhaseId() == PhaseId.INVOKE_APPLICATION) {
			if (logger.isDebugEnabled()) {
				logger.debug("SAVING MESSAGES TO SESSION...");
			}
			saveMessages(event.getFacesContext());
			debugMessages(event);
		}
	}

	/**
	 * @see org.esupportail.commons.jsf.AbstractPhaseListener#beforePhaseInternal(javax.faces.event.PhaseEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void beforePhaseInternal(final PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		UIViewRoot viewRoot = facesContext.getViewRoot();
		ExternalContext externalContext = facesContext.getExternalContext();
		if (event.getPhaseId() != PhaseId.RENDER_RESPONSE) {
			return;
		}
		if ("POST".equals(getMethod(externalContext))) {
			return;
		}
		// Implement POST-REDIRECT-GET pattern
		boolean restoreMessages;
		if (ContextUtils.isPortlet()) {
			Map<String, Object> userInfo = 
				(Map<String, Object>) ContextUtils.getRequestAttribute(
						PortletRequest.USER_INFO);
			restoreMessages = userInfo == null || userInfo.isEmpty();
		} else {
			restoreMessages = true;
		}
		if (restoreMessages) {
			if (logger.isDebugEnabled()) {
				logger.debug("RESTORING MESSAGES TO CONTEXT...");
			}
			// Move saved messages from session back to request queue
			restoreMessages(facesContext);
			/*
			 * JSF normally clears input component values in the UpdateModel
			 * phase. However, this phase does not run for a GET request, so we
			 * must do it ourselves. Otherwise, the view will retain values from
			 * the first time it was loaded.
			 */
			resetComponentValues(viewRoot.getChildren());
			debugMessages(event);
		}
	}

	/**
	 * Remove the messages that are not associated with any particular component
	 * from the user's session and add them to the faces context.
	 * @param facesContext 
	 *
	 * @return the number of removed messages.
	 */
	@SuppressWarnings("unchecked")
	protected int restoreMessages(final FacesContext facesContext) {
		// remove messages from the session
		int numRestoredMessages = 0;
		Map<Object, Object>  sessionMap = facesContext.getExternalContext().getSessionMap();
		Map<String, List<FacesMessage>> allMessages = 
			(Map<String, List<FacesMessage>>) sessionMap.remove(sessionToken);
		if (allMessages == null) {
			return 0;
		}
		// Move messages from session back to facesContext
		for (String clientId : allMessages.keySet()) {
			List<FacesMessage> clientMessages = allMessages.get(clientId);
			for (FacesMessage facesMessage : clientMessages) {
				facesContext.addMessage(clientId, facesMessage);
				numRestoredMessages++;
			}
		}
		return numRestoredMessages;
	}

	/**
	 * Remove the messages that are not associated with any particular component
	 * from the faces context and store them to the user's session.
	 * @param facesContext 
	 *
	 * @return the number of removed messages.
	 */
	@SuppressWarnings("unchecked")
	private int saveMessages(final FacesContext facesContext) {
		// Remove messages from the context
		// Save as a map of lists so we can continue to messages with components
		Map<Object, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
		int numMessages = 0;

		if (!sessionMap.containsKey(sessionToken)) {
			sessionMap.put(sessionToken, new HashMap<Object, Object>());
		}
		Map<String, List<FacesMessage>> allMessages = 
			(Map<String, List<FacesMessage>>) sessionMap.get(sessionToken);

		for (Iterator<String> i = facesContext.getClientIdsWithMessages(); i.hasNext();) {
			String clientId = i.next();
			// For each component (client ID), retrieve the messages to a list
			List<FacesMessage> messages = new ArrayList<FacesMessage>();
			for (Iterator<FacesMessage> j = facesContext.getMessages(clientId); j.hasNext();) {
				messages.add(j.next());
				j.remove();
				numMessages++;
			}
			List<FacesMessage> clientMessages = allMessages.get(clientId);
			if (clientMessages != null) {
				// There are already messages for this component
				clientMessages.addAll(messages);
			} else {
				// Not yet messages for this component
				allMessages.put(clientId, messages);
			}
		}
		return numMessages;
	}

	/**
	 * Resets UIInput component values.
	 * From http://forum.java.sun.com/thread.jspa?threadID=495087&messageID=3704164
	 * @param children 
	 */
	@SuppressWarnings("unchecked")
	private void resetComponentValues(final List<UIComponent> children) {
		for (UIComponent component : children) {
			if (component instanceof UIInput) {
				UIInput input = (UIInput) component;
				input.setSubmittedValue(null);
			}
			resetComponentValues(component.getChildren());
		}
	}
}
