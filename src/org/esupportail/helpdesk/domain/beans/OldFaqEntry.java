/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

/**
 * A class that handles the old FAQ entries, for upgrade only.
 * @deprecated
 */
@SuppressWarnings("deprecation")
@Deprecated
public class OldFaqEntry extends AbstractDeprecatedFaqEntity {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5553053576086302531L;
	/**
	 * The old FAQ part.
	 */
	private OldFaqPart oldFaqPart;
	
	/**
	 * Bean constructor.
	 */
	public OldFaqEntry() {
		super();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof OldFaqEntry)) {
			return false;
		}
		return ((OldFaqEntry) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * @return the oldFaqPart
	 */
	public OldFaqPart getOldFaqPart() {
		return oldFaqPart;
	}

	/**
	 * @param oldFaqPart the oldFaqPart to set
	 */
	public void setOldFaqPart(final OldFaqPart oldFaqPart) {
		this.oldFaqPart = oldFaqPart;
	}
	
}
