/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;


/**
 * The representation of an alert, attached to an action. */
public final class Alert implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8879542269015301696L;

	/**
     * Primary key.
     */
    private Long id;

    /**
     * The action the alert was sent for.
     */
    private Action action;

    /**
     * The user was the alert was sent to (null if sent to an email address).
     */
    private User user;

    /**
     * The email address the alert was sent to (null if sent to a user).
     */
    private String email;

	/**
	 * Default constructor.
	 */
	public Alert() {
		this.action = null;
		this.user = null;
		this.email = null;
	}
	
	/**
	 * Constructor.
	 * @param action the action
	 * @param user the user
	 */
	public Alert(
			final Action action,
			final User user) {
		this();
		this.action = action;
		this.user = user;
	}
	
	/**
	 * Constructor.
	 * @param action the action
	 * @param email the email
	 */
	public Alert(
			final Action action,
			final String email) {
		this();
		this.action = action;
		this.email = email;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Alert)) {
			return false;
		}
		return ((Alert) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		if (getId() == null) {
			return 0;
		}
		return getId().intValue();
	}

    /**
     * @return The id.
     */
    protected Long getId() {
        return this.id;
    }

    /**
     * @param id The id to set.
     */
    protected void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return The action.
     */
    protected Action getAction() {
        return this.action;
    }

    /**
     * @param action The action to set.
     */
    protected void setAction(final Action action) {
        this.action = action;
    }

    /**
     * @return The user.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * @param user The user to set.
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * @return The email.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @param email The email to set.
     */
    public void setEmail(final String email) {
        this.email = email;
    }
    
}
