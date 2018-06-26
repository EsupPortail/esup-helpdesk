/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userInfo;

import java.io.Serializable;
import java.util.Locale;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * the interface of user info providers.
 */
public interface UserInfoProvider extends Serializable {

    /**
     * @return information about the user.
     * @param user
     * @param locale
     */
    String getInfo(
    		User user,
    		Locale locale);

    /**
     * Test the class.
     */
    void test();

	/**
	 * @param domainService the domainService to set
	 */
	void setDomainService(final DomainService domainService);

}
