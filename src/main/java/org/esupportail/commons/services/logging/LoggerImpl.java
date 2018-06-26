/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.logging; 

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;

/**
 * A class based on Jakarta commons-logging that provides logging features.
 */
public class LoggerImpl implements Logger, Serializable {

	/**
	 * For serialize.
	 */
	private static final long serialVersionUID = 1256964268439590331L;

	/**
	 * The null string.
	 */
	private static final String NULL = "null";
	
	/**
	 * a static physical logger.
	 */
	private org.apache.commons.logging.Log logger;

	/**
	 * Constructor.
	 * @param logClass the class the logger will be used by.
	 */
	public LoggerImpl(@SuppressWarnings("rawtypes") final Class logClass) {
		logger = LogFactory.getLog(logClass);
	}

	/**
	 * Constructor.
	 * @param category
	 */
	public LoggerImpl(final String category) {
		logger = LogFactory.getLog(category);
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#isTraceEnabled()
	 */
	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#trace(java.lang.StringBuffer)
	 */
	@Override
	public void trace(final StringBuffer sb) {
		trace(sb.toString());
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#trace(java.lang.String)
	 */
	@Override
	public void trace(final String str) {
		if (str == null) {
			logger.trace(NULL);
		} else {
			String [] lines = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				logger.trace(lines[i]);
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#trace(java.lang.Throwable)
	 */
	@Override
	public void trace(final Throwable t) {
		trace(ExceptionUtils.getPrintableStackTrace(t));
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#trace(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void trace(final String str, final Throwable t) {
		trace(str);
		trace(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#trace(java.lang.StringBuffer, java.lang.Throwable)
	 */
	@Override
	public void trace(final StringBuffer sb, final Throwable t) {
		trace(sb.toString(), t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#trace(java.util.List)
	 */
	@Override
	public void trace(final List<String> list) {
		for (String string : list) {
			trace(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#trace(java.util.List, java.lang.Throwable)
	 */
	@Override
	public void trace(final List<String> list, final Throwable t) {
		trace(list);
		trace(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#trace(java.util.Set)
	 */
	@Override
	public void trace(final Set<String> set) {
		for (String string : set) {
			trace(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#trace(java.util.Set, java.lang.Throwable)
	 */
	@Override
	public void trace(final Set<String> set, final Throwable t) {
		trace(set);
		trace(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#isDebugEnabled()
	 */
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#debugTime(java.lang.String, long)
	 */
	@Override
	public void debugTime(final String str, final long start) {
		long duration = System.currentTimeMillis() - start;
		StringBuffer sb = new StringBuffer();
		sb.append("duration: ").append(duration).append(" -> ").append(str);
		debug(sb);
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#debug(java.lang.StringBuffer)
	 */
	@Override
	public void debug(final StringBuffer sb) {
		debug(sb.toString());
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#debug(java.lang.String)
	 */
	@Override
	public void debug(final String str) {
		if (str == null) {
			logger.debug(NULL);
		} else {
			String [] lines = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				logger.debug(lines[i]);
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#debug(java.lang.Throwable)
	 */
	@Override
	public void debug(final Throwable t) {
		debug(ExceptionUtils.getPrintableStackTrace(t));
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#debug(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void debug(final String str, final Throwable t) {
		debug(str);
		debug(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#debug(java.lang.StringBuffer, java.lang.Throwable)
	 */
	@Override
	public void debug(final StringBuffer sb, final Throwable t) {
		debug(sb.toString(), t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#debug(java.util.List)
	 */
	@Override
	public void debug(final List<String> list) {
		for (String string : list) {
			debug(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#debug(java.util.List, java.lang.Throwable)
	 */
	@Override
	public void debug(final List<String> list, final Throwable t) {
		debug(list);
		debug(t);
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#debug(java.util.Set)
	 */
	@Override
	public void debug(final Set<String> set) {
		for (String string : set) {
			debug(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#debug(java.util.Set, java.lang.Throwable)
	 */
	@Override
	public void debug(final Set<String> set, final Throwable t) {
		debug(set);
		debug(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#info(java.lang.String)
	 */
	@Override
	public void info(final String str) {
		if (str == null) {
			logger.info(NULL);
		} else {
			String [] lines = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				logger.info(lines[i]);
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#info(java.lang.StringBuffer)
	 */
	@Override
	public void info(final StringBuffer sb) {
		info(sb.toString());
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#info(java.lang.Throwable)
	 */
	@Override
	public void info(final Throwable t) {
		info(ExceptionUtils.getPrintableStackTrace(t));
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#info(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void info(final String str, final Throwable t) {
		info(str);
		info(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#info(java.lang.StringBuffer, java.lang.Throwable)
	 */
	@Override
	public void info(final StringBuffer sb, final Throwable t) {
		info(sb.toString(), t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#info(java.util.List)
	 */
	@Override
	public void info(final List<String> list) {
		for (String string : list) {
			info(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#info(java.util.List, java.lang.Throwable)
	 */
	@Override
	public void info(final List<String> list, final Throwable t) {
		info(list);
		info(t);
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#info(java.util.Set)
	 */
	@Override
	public void info(final Set<String> set) {
		for (String string : set) {
			info(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#info(java.util.Set, java.lang.Throwable)
	 */
	@Override
	public void info(final Set<String> set, final Throwable t) {
		info(set);
		info(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#warn(java.lang.String)
	 */
	@Override
	public void warn(final String str) {
		if (str == null) {
			logger.warn(NULL);
		} else {
			String [] lines = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				logger.warn(lines[i]);
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#warn(java.lang.StringBuffer)
	 */
	@Override
	public void warn(final StringBuffer sb) {
		warn(sb.toString());
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#warn(java.lang.Throwable)
	 */
	@Override
	public void warn(final Throwable t) {
		warn(ExceptionUtils.getPrintableStackTrace(t));
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#warn(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void warn(final String str, final Throwable t) {
		warn(str);
		warn(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#warn(java.lang.StringBuffer, java.lang.Throwable)
	 */
	@Override
	public void warn(final StringBuffer sb, final Throwable t) {
		warn(sb.toString(), t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#warn(java.util.List)
	 */
	@Override
	public void warn(final List<String> list) {
		for (String string : list) {
			warn(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#warn(java.util.List, java.lang.Throwable)
	 */
	@Override
	public void warn(final List<String> list, final Throwable t) {
		warn(list);
		warn(t);
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#warn(java.util.Set)
	 */
	@Override
	public void warn(final Set<String> set) {
		for (String string : set) {
			warn(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#warn(java.util.Set, java.lang.Throwable)
	 */
	@Override
	public void warn(final Set<String> set, final Throwable t) {
		warn(set);
		warn(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#error(java.lang.String)
	 */
	@Override
	public void error(final String str) {
		if (str == null) {
			logger.error(NULL);
		} else {
			String [] lines = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				logger.error(lines[i]);
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#error(java.lang.StringBuffer)
	 */
	@Override
	public void error(final StringBuffer sb) {
		error(sb.toString());
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#error(java.lang.Throwable)
	 */
	@Override
	public void error(final Throwable t) {
		error(ExceptionUtils.getPrintableStackTrace(t));
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#error(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void error(final String str, final Throwable t) {
		error(str);
		error(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#error(java.lang.StringBuffer, java.lang.Throwable)
	 */
	@Override
	public void error(final StringBuffer sb, final Throwable t) {
		error(sb.toString(), t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#error(java.util.List)
	 */
	@Override
	public void error(final List<String> list) {
		for (String string : list) {
			error(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#error(java.util.List, java.lang.Throwable)
	 */
	@Override
	public void error(final List<String> list, final Throwable t) {
		error(list);
		error(t);
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#error(java.util.Set)
	 */
	@Override
	public void error(final Set<String> set) {
		for (String string : set) {
			error(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#error(java.util.Set, java.lang.Throwable)
	 */
	@Override
	public void error(final Set<String> set, final Throwable t) {
		error(set);
		error(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#fatal(java.lang.String)
	 */
	@Override
	public void fatal(final String str) {
		if (str == null) {
			logger.fatal(NULL);
		} else {
			String [] lines = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				logger.fatal(lines[i]);
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#fatal(java.lang.StringBuffer)
	 */
	@Override
	public void fatal(final StringBuffer sb) {
		fatal(sb.toString());
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#fatal(java.lang.Throwable)
	 */
	@Override
	public void fatal(final Throwable t) {
		fatal(ExceptionUtils.getPrintableStackTrace(t));
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#fatal(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void fatal(final String str, final Throwable t) {
		fatal(str);
		fatal(t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#fatal(java.lang.StringBuffer, java.lang.Throwable)
	 */
	@Override
	public void fatal(final StringBuffer sb, final Throwable t) {
		fatal(sb.toString(), t);
	}
	
	/**
	 * @see org.esupportail.commons.services.logging.Logger#fatal(java.util.List)
	 */
	@Override
	public void fatal(final List<String> list) {
		for (String string : list) {
			fatal(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#fatal(java.util.List, java.lang.Throwable)
	 */
	@Override
	public void fatal(final List<String> list, final Throwable t) {
		fatal(list);
		fatal(t);
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#fatal(java.util.Set)
	 */
	@Override
	public void fatal(final Set<String> set) {
		for (String string : set) {
			fatal(string);
		}
	}

	/**
	 * @see org.esupportail.commons.services.logging.Logger#fatal(java.util.Set, java.lang.Throwable)
	 */
	@Override
	public void fatal(final Set<String> set, final Throwable t) {
		fatal(set);
		fatal(t);
	}
	
}

