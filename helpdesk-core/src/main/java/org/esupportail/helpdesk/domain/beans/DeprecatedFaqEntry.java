/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import org.esupportail.helpdesk.domain.FaqScope;


/**
 * A class that represents a FAQ entry.
 * @deprecated
 */
@SuppressWarnings("deprecation")
@Deprecated
public class DeprecatedFaqEntry extends AbstractDeprecatedFaqEntity {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7705270525641103132L;

	/**
	 * Bean constructor.
	 */
	public DeprecatedFaqEntry() {
		super();
	}
	
	/**
	 * Bean constructor.
	 * @param fe 
	 */
	public DeprecatedFaqEntry(final DeprecatedFaqEntry fe) {
		super(fe);
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DeprecatedFaqEntry)) {
			return false;
		}
		return ((DeprecatedFaqEntry) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[" 
		+ toStringInternal()
		+ "]";
	}

	/**
	 * Compute the effective scope (using default policy if needed).
	 * @return true if the object needs to be updated.
	 */
	public boolean computeEffectiveScope() {
		String oldEffectiveScope = getEffectiveScope();
		String oldScope = getScope();
		if (!getScope().equals(FaqScope.DEFAULT)) {
			setEffectiveScope(getScope());
		} else {
			String parentEffectiveScope = getParent().getEffectiveScope();
			if (FaqScope.MANAGER.equals(parentEffectiveScope)) {
				setScope(FaqScope.MANAGER);
			} else if (FaqScope.DEPARTMENT.equals(parentEffectiveScope)) {
				if (!FaqScope.MANAGER.equals(getScope())) {
					setScope(FaqScope.DEPARTMENT);
				}
			} else if (FaqScope.AUTHENTICATED.equals(parentEffectiveScope)) {
				if (FaqScope.ALL.equals(getScope())) {
					setScope(FaqScope.AUTHENTICATED);
				}
			} else if (!FaqScope.ALL.equals(parentEffectiveScope)) {
				throw new IllegalArgumentException(
						"unexpected FAQ scope [" + parentEffectiveScope + "]");
			}
			setEffectiveScope(parentEffectiveScope);
		}
		return !(getEffectiveScope().equals(oldEffectiveScope)) || !(getScope().equals(oldScope));
	}
	
}