/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.ArchivedAction;
import org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer;
import org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry;
import org.esupportail.helpdesk.domain.beans.Faq;


/**
 * A bean to clean the code delivered by FCK editor.
 */
public interface FckEditorCodeCleaner {

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	boolean removeMaliciousTags(DeprecatedFaqContainer x);
	
	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	boolean removeMaliciousTags(DeprecatedFaqEntry x);
	
	/**
	 * Remove the malicious tags from a FAQ.
	 * @param faq
	 * @return true if the object was updated.
	 */
	boolean removeMaliciousTags(Faq faq);
	
	/**
	 * Remove the malicious tags from an action.
	 * @param action
	 * @return true if the object was updated.
	 */
	boolean removeMaliciousTags(Action action);
	
	/**
	 * Remove the malicious tags from an archived action.
	 * @param archivedAction
	 * @return true if the object was updated.
	 */
	boolean removeMaliciousTags(ArchivedAction archivedAction);
	
}
