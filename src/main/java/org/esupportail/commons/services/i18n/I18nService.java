/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.i18n;

import java.io.Serializable;
import java.util.Locale; 
import java.util.Map;

/**
 * The interface of i18n services. An i18n service is a layer to abstract
 * resources bundles, that should never be accessed directly.
 */
public interface I18nService extends Serializable {

	/**
	 * @return The default locale.
	 */
	Locale getDefaultLocale();
	
	/**
	 * @return A map that contains the strings available for a locale.
	 * @param locale
	 */
	Map<String, String> getStrings(Locale locale);

	/**
	 * @return A map that contains the strings available for the default locale.
	 */
	Map<String, String> getStrings();

	/**
	 * @return The string that corresponds to a key and a locale.
	 * @param key
	 * @param locale
	 */
	String getString(String key, Locale locale);

	/**
	 * @return The string that corresponds to a key for the default locale.
	 * @param key
	 */
	String getString(String key);

	/**
	 * @return The string that corresponds to a key and a locale, where {0} ... {n} are replaced by args.
	 * @param key
	 * @param locale
	 * @param args
	 */
	String getString(String key, Locale locale, Object [] args);

	/**
	 * @return The string that corresponds to a key for the default locale, where {0} ... {n} are replaced by args.
	 * @param key
	 * @param args
	 */
	String getString(String key, Object [] args);

	/**
	 * @return The string that corresponds to a key and a locale, where {0} is replaced by arg0.
	 * @param key
	 * @param locale
	 * @param arg0
	 */
	String getString(String key, Locale locale, Object arg0);

	/**
	 * @return The string that corresponds to a key for the default locale, 
	 * where {0} is replaced by arg0.
	 * @param key
	 * @param arg0
	 */
	String getString(String key, Object arg0);

	/**
	 * @return The string that corresponds to a key and a locale, where {0} 
	 * and {1} are replaced by arg0 and arg1.
	 * @param key
	 * @param locale
	 * @param arg0
	 * @param arg1
	 */
	String getString(String key, Locale locale, Object arg0, Object arg1);

	/**
	 * @return The string that corresponds to a key and the default locale, 
	 * where {0} and {1} are replaced by arg0 and arg1.
	 * @param key
	 * @param arg0
	 * @param arg1
	 */
	String getString(String key, Object arg0, Object arg1);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1} 
	 * and {2} are replaced by arg0, arg1 and arg2.
	 * @param key
	 * @param locale
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	String getString(String key, Locale locale, Object arg0, Object arg1, Object arg2);

	/**
	 * @return The string that corresponds to a key and the default locale, 
	 * where {0}, {1} and {2} are replaced by arg0, arg1 and arg2.
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	String getString(String key, Object arg0, Object arg1, Object arg2);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} and {3} are replaced by arg0, arg1, arg2 and arg3.
	 * @param key
	 * @param locale
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	String getString(String key, Locale locale, Object arg0, Object arg1, Object arg2, Object arg3);

	/**
	 * @return The string that corresponds to a key, where {0}, {1}, 
	 * {2} and {3} are replaced by arg0, arg1, arg2 and arg3.
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	String getString(String key, Object arg0, Object arg1, Object arg2, Object arg3);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} and {3} are replaced by arg0, arg1, arg2, arg3 and arg4.
	 * @param key
	 * @param locale
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	String getString(String key, Locale locale, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} and {3} are replaced by arg0, arg1, arg2, arg3 and arg4.
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	String getString(String key, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param locale
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 */
	String getString(String key, Locale locale, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 */
	String getString(String key, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param locale
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 */
	String getString(String key, Locale locale, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, 
			Object arg5, Object arg6);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 */
	String getString(String key, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, 
			Object arg5, Object arg6);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param locale
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 */
	String getString(String key, Locale locale, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, 
			Object arg5, Object arg6, Object arg7);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 */
	String getString(String key, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, 
			Object arg5, Object arg6, Object arg7);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param locale
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 */
	String getString(String key, Locale locale, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, 
			Object arg5, Object arg6, Object arg7, Object arg8);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 */
	String getString(String key, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, 
			Object arg5, Object arg6, Object arg7, Object arg8);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param locale
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 */
	String getString(String key, Locale locale, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, 
			Object arg5, Object arg6, Object arg7, Object arg8, Object arg9);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 */
	String getString(String key, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, 
			Object arg5, Object arg6, Object arg7, Object arg8, Object arg9);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param locale
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 * @param arg10
	 */
	String getString(String key, Locale locale, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, 
			Object arg5, Object arg6, Object arg7, Object arg8, Object arg9,
			Object arg10);

	/**
	 * @return The string that corresponds to a key and a locale, where {0}, {1}, 
	 * {2} {3}, {4} and {5} are replaced by arg0, arg1, arg2, arg3, arg4 and arg5.
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 * @param arg10
	 */
	String getString(String key, 
			Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, 
			Object arg5, Object arg6, Object arg7, Object arg8, Object arg9,
			Object arg10);

	/**
	 * @return The date as a printable string corresponding to a Locale. The format 
	 * used depends on the current date.
	 * @param date
	 * @param locale
	 */
	String printableRelativeDate(long date, Locale locale);

	/**
	 * @return The date as a printable string corresponding to the default Locale. 
	 * The format used depends on the current date.
	 * @param date
	 */
	String printableRelativeDate(long date);

	/**
	 * @return The date as a printable corresponding to a Locale.
	 * @param date
	 * @param locale
	 */
	String printableDate(long date, Locale locale);
	
	/**
	 * @return The date as a printable corresponding to the default Locale.
	 * @param date
	 */
	String printableDate(long date);
	
}
