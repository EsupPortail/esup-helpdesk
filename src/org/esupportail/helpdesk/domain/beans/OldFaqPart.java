/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

/**
 * A class that handles the old FAQ parts, for upgrade only.
 * @deprecated
 */
@SuppressWarnings("deprecation")
@Deprecated
public class OldFaqPart extends AbstractDeprecatedFaqEntity {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4222740752271334927L;

	/**
	 * Bean constructor.
	 */
	public OldFaqPart() {
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
		if (!(obj instanceof OldFaqPart)) {
			return false;
		}
		return ((OldFaqPart) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

}
