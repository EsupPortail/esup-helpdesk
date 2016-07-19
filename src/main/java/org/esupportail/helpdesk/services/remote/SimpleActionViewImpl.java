package org.esupportail.helpdesk.services.remote;

import org.esupportail.helpdesk.domain.beans.Action;

/**
 * The actions used by the web service.
 */
public class SimpleActionViewImpl implements SimpleActionView {
	
	/**
	 * The message, only field.
	 */
	private String message;

	/**
	 * @param action
	 */
	public SimpleActionViewImpl(Action action) {
		this.message = action.getMessage();
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleActionView#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	
}
