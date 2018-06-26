/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.logging;

import java.util.List;
import java.util.Set;

/**
 * The interface of logging classes.
 */
public interface Logger {

	/**
	 * This method should be called to prevent from performing expensive operations (such 
	 * as String concatenation) when the logger level is more than trace. 
	 * @return true if the TRACE level of the service is enabled.
	 */
	boolean isTraceEnabled();

	/**
	 * Log a StringBuffer as TRACE.
	 * @param sb a StringBuffer
	 */
	void trace(final StringBuffer sb);

	/**
	 * Log a string as TRACE.
	 * @param str a string
	 */
	void trace(final String str);

	/**
	 * Log an exception as TRACE.
	 * @param t a throwable
	 */
	void trace(final Throwable t);

	/**
	 * Log a string and an exception as TRACE.
	 * @param str a string
	 * @param t a throwable
	 */
	void trace(final String str, final Throwable t);

	/**
	 * Log a string buffer and an exception as TRACE.
	 * @param sb a string buffer
	 * @param t a throwable
	 */
	void trace(final StringBuffer sb, final Throwable t);

	/**
	 * Log a list of strings as TRACE.
	 * @param list
	 */
	void trace(final List<String> list);

	/**
	 * Log a list of strings and an exception as TRACE.
	 * @param list
	 * @param t a throwable
	 */
	void trace(final List<String> list, final Throwable t);
	
	/**
	 * Log a set of strings as TRACE.
	 * @param set
	 */
	void trace(final Set<String> set);

	/**
	 * Log a set of strings and an exception as TRACE.
	 * @param set
	 * @param t a throwable
	 */
	void trace(final Set<String> set, final Throwable t);
	
	/**
	 * This method should be called to prevent from performing expensive operations (such 
	 * as String concatenation) when the logger level is more than debug. 
	 * @return true if the DEBUG level of the service is enabled.
	 */
	boolean isDebugEnabled();

	/**
	 * Log a string and a duration as DEBUG.
	 * @param str a string
	 * @param start the start time
	 */
	void debugTime(final String str, final long start);

	/**
	 * Log a StringBuffer as DEBUG.
	 * @param sb a StringBuffer
	 */
	void debug(final StringBuffer sb);

	/**
	 * Log a string as DEBUG.
	 * @param str a string
	 */
	void debug(final String str);

	/**
	 * Log an exception as DEBUG.
	 * @param t a throwable
	 */
	void debug(final Throwable t);

	/**
	 * Log a string and an exception as DEBUG.
	 * @param str a string
	 * @param t a throwable
	 */
	void debug(final String str, final Throwable t);

	/**
	 * Log a string buffer and an exception as DEBUG.
	 * @param sb a string buffer
	 * @param t a throwable
	 */
	void debug(final StringBuffer sb, final Throwable t);

	/**
	 * Log a list of strings as DEBUG.
	 * @param list
	 */
	void debug(final List<String> list);

	/**
	 * Log a list of strings and an exception as DEBUG.
	 * @param list
	 * @param t a throwable
	 */
	void debug(final List<String> list, final Throwable t);

	/**
	 * Log a set of strings as DEBUG.
	 * @param set
	 */
	void debug(final Set<String> set);

	/**
	 * Log a set of strings and an exception as DEBUG.
	 * @param set
	 * @param t a throwable
	 */
	void debug(final Set<String> set, final Throwable t);
	
	/**
	 * Log a string as INFO.
	 * @param str a string
	 */
	void info(final String str);

	/**
	 * Log a StringBuffer as INFO.
	 * @param sb a StringBuffer
	 */
	void info(final StringBuffer sb);

	/**
	 * Log an exception as INFO.
	 * @param t a throwable
	 */
	void info(final Throwable t);

	/**
	 * Log a string and an exception as INFO.
	 * @param str a string
	 * @param t a throwable
	 */
	void info(final String str, final Throwable t);

	/**
	 * Log a string buffer and an exception as INFO.
	 * @param sb a string buffer
	 * @param t a throwable
	 */
	void info(final StringBuffer sb, final Throwable t);

	/**
	 * Log a list of strings as INFO.
	 * @param list
	 */
	void info(final List<String> list);

	/**
	 * Log a list of strings and an exception as INFO.
	 * @param list
	 * @param t a throwable
	 */
	void info(final List<String> list, final Throwable t);

	/**
	 * Log a set of strings as INFO.
	 * @param set
	 */
	void info(final Set<String> set);

	/**
	 * Log a set of strings and an exception as INFO.
	 * @param set
	 * @param t a throwable
	 */
	void info(final Set<String> set, final Throwable t);
	
	/**
	 * Log a string as WARN.
	 * @param str a string
	 */
	void warn(final String str);

	/**
	 * Log a StringBuffer as WARN.
	 * @param sb a StringBuffer
	 */
	void warn(final StringBuffer sb);

	/**
	 * Log an exception as WARN.
	 * @param t a throwable
	 */
	void warn(final Throwable t);

	/**
	 * Log a string and an exception as WARN.
	 * @param str a string
	 * @param t a throwable
	 */
	void warn(final String str, final Throwable t);

	/**
	 * Log a string buffer and an exception as WARN.
	 * @param sb a string buffer
	 * @param t a throwable
	 */
	void warn(final StringBuffer sb, final Throwable t);

	/**
	 * Log a list of strings as WARN.
	 * @param list
	 */
	void warn(final List<String> list);

	/**
	 * Log a list of strings and an exception as WARN.
	 * @param list
	 * @param t a throwable
	 */
	void warn(final List<String> list, final Throwable t);

	/**
	 * Log a set of strings as WARN.
	 * @param set
	 */
	void warn(final Set<String> set);

	/**
	 * Log a set of strings and an exception as WARN.
	 * @param set
	 * @param t a throwable
	 */
	void warn(final Set<String> set, final Throwable t);
	
	/**
	 * Log a string as ERROR.
	 * @param str a string
	 */
	void error(final String str);

	/**
	 * Log a StringBuffer as ERROR.
	 * @param sb a StringBuffer
	 */
	void error(final StringBuffer sb);

	/**
	 * Log an exception as ERROR.
	 * @param t a throwable
	 */
	void error(final Throwable t);

	/**
	 * Log a string and an exception as ERROR.
	 * @param str a string
	 * @param t a throwable
	 */
	void error(final String str, final Throwable t);

	/**
	 * Log a string buffer and an exception as ERROR.
	 * @param sb a string buffer
	 * @param t a throwable
	 */
	void error(final StringBuffer sb, final Throwable t);

	/**
	 * Log a list of strings as ERROR.
	 * @param list
	 */
	void error(final List<String> list);

	/**
	 * Log a list of strings and an exception as ERROR.
	 * @param list
	 * @param t a throwable
	 */
	void error(final List<String> list, final Throwable t);

	/**
	 * Log a set of strings as ERROR.
	 * @param set
	 */
	void error(final Set<String> set);

	/**
	 * Log a set of strings and an exception as ERROR.
	 * @param set
	 * @param t a throwable
	 */
	void error(final Set<String> set, final Throwable t);
	
	/**
	 * Log a string as FATAL .
	 * @param str a string
	 */
	void fatal(final String str);

	/**
	 * Log a StringBuffer as FATAL.
	 * @param sb a StringBuffer
	 */
	void fatal(final StringBuffer sb);

	/**
	 * Log an exception as FATAL.
	 * @param t a throwable
	 */
	void fatal(final Throwable t);

	/**
	 * Log a string and an exception as FATAL.
	 * @param str a string
	 * @param t a throwable
	 */
	void fatal(final String str, final Throwable t);

	/**
	 * Log a string buffer and an exception as FATAL.
	 * @param sb a string buffer
	 * @param t a throwable
	 */
	void fatal(final StringBuffer sb, final Throwable t);

	/**
	 * Log a list of strings as FATAL.
	 * @param list
	 */
	void fatal(final List<String> list);

	/**
	 * Log a list of strings and an exception as FATAL.
	 * @param list
	 * @param t a throwable
	 */
	void fatal(final List<String> list, final Throwable t);

	/**
	 * Log a set of strings as FATAL.
	 * @param set
	 */
	void fatal(final Set<String> set);

	/**
	 * Log a set of strings and an exception as FATAL.
	 * @param set
	 * @param t a throwable
	 */
	void fatal(final Set<String> set, final Throwable t);
	
}