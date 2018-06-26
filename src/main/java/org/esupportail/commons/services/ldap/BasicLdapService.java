/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * The interface of LDAP services for any entity.
 */
interface BasicLdapService extends Serializable {

	/**
	 * @param filterExpr
	 * @return null if the filter is correct, an error message otherwise.
	 * @throws LdapException 
	 */
	String testLdapFilter(String filterExpr) throws LdapException;
	
	/**
	 * @return true if the class supports the retrieval of statictics. If false,
	 * calls to methods getStatistics() and resetStatistics() should throw an 
	 * exception.
	 */
	boolean supportStatistics();
	
	/**
	 * Reset the statistics.
	 */
	void resetStatistics();
	
	/**
	 * @return the statistics, as a list of Strings.
	 * @param locale
	 */
	List<String> getStatistics(Locale locale);
	
	/**
	 * @return true if the class supports testing. If false, calls to method 
	 * test() should throw an exception.
	 */
	boolean supportsTest();
	
	/**
	 * Test the LDAP connection.
	 */
	void test();
	
}
