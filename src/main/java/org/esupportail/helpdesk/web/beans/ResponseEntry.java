/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.Response;

/** 
 * An entry of the responses.
 */ 
public class ResponseEntry implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7961973356544528659L;

	/**
	 * The Response.
	 */
	private Response response;
	
	/**
	 * The formatted message.
	 */
	private String formattedMessage;

	/**
	 * @param response
	 * @param signature 
	 */
	public ResponseEntry(
			final Response response,
			final String signature) {
		super();
		this.response = response;
		this.formattedMessage = formatMessage(response.getMessage(), signature);
	}

	/**
	 * @param message
	 * @param signature
	 * @return a formatted message.
	 */
	private static String formatMessage(
			final String message, 
			final String signature) {
		String input = message;
		if (!org.springframework.util.StringUtils.hasText(input)) {
			return null;
		}
		String result = input;
		result = result .replaceAll("[\\r\\n]+", " ");
		result = result.replaceAll("\\\\", "\\\\\\\\");
		result = result.replaceAll("\"", "\\\\\"");
		result = result.replaceAll(Response.SIGNATURE_TOKEN, signature);
		return result;
	}

	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	protected void setResponse(final Response response) {
		this.response = response;
	}

	/**
	 * @return the formattedMessage
	 */
	public String getFormattedMessage() {
		return formattedMessage;
	}

	/**
	 * @param formattedMessage the formattedMessage to set
	 */
	protected void setFormattedMessage(final String formattedMessage) {
		this.formattedMessage = formattedMessage;
	}

}

