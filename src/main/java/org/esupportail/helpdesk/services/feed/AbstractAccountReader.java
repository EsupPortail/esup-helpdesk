/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed;


/**
 * An abstract email reader.
 */
@SuppressWarnings("serial")
public abstract class AbstractAccountReader implements AccountReader {
	
	/**
	 * True if enabled.
	 */
	private boolean enabled;

	/**
	 * Constructor.
	 */
	public AbstractAccountReader() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.AccountReader#isEnabled()
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(final boolean disabled) {
		setEnabled(!disabled);
	}

	public void afterPropertiesSet() {
		// TODO Auto-generated method stub
		
	}

	public boolean read(ErrorHolder errorHolder) {
		// TODO Auto-generated method stub
		return false;
	}
	
}