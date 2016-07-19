/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.categoryConfiguration;

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.Category;


/**
 * the interface of category configurators.
 */
public interface CategoryConfigurator extends Serializable {
	
    /**
     * Configure a category (just after its creation).
     * @param category
     */
    void configure(final Category category);

}
