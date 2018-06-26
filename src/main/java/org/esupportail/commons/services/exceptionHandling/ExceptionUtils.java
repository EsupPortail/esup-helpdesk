/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.exceptionHandling; 

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.esupportail.commons.exceptions.ExceptionHandlingException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.ContextUtils;

/**
 * A class that provides static utilities for exception handling.
 */
public final class ExceptionUtils {
	
	/**
	 * The name of the exception service bean.
	 */
	private static final String EXCEPTION_SERVICE_FACTORY_BEAN = "exceptionServiceFactory";

	/**
	 * The name of the session attribute set to prevent from infite redirections.
	 */
	private static final String EXCEPTION_MARKER_NAME = "exception.marker";

	/**
	 * The text separator for the stack trace.
	 */
	private static final String STACK_TRACE_SEPARATOR = 
		"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ";

	/**
	 * The "caused by" element.
	 */
	private static final String STACK_TRACE_CAUSED_BY = "caused by: "; 
	
	/**
	 * A logger.
	 */
	private static final Logger LOGGER = new LoggerImpl(ExceptionUtils.class);

	/**
	 * Private constructor.
	 */
	private ExceptionUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return A list of strings that correspond to the stack trace of a throwable.
	 * @param t
	 */
	private static List<String> internalGetStackTraceStrings(final Throwable t) {
		List<String> result = new ArrayList<String>();
		result.add(t.toString());
		for (StackTraceElement element : t.getStackTrace()) {
			result.add(element.toString());
		}
		Throwable cause;
		if (t instanceof ServletException) {
			cause = ((ServletException) t).getRootCause();
		} else {
			cause = t.getCause();
		}
		if (cause != null) {
			result.add(STACK_TRACE_SEPARATOR + STACK_TRACE_CAUSED_BY);
			result.addAll(internalGetStackTraceStrings(cause));
		}
		return result;
	}

	/**
	 * @return A list of strings that correspond to the stack trace of an exception.
	 * @param t
	 */
	public static List<String> getStackTraceStrings(final Throwable t) {
		return internalGetStackTraceStrings(t);
	}

	/**
	 * @return A printable form of the stack trace of an exception.
	 * @param t
	 */
	public static String getPrintableStackTrace(final Throwable t) {
		StringBuffer sb = new StringBuffer();
		List<String> strings = internalGetStackTraceStrings(t);
		String separator = "";
		for (String string : strings) {
			sb.append(separator).append(string);
			separator = "\n";
		}
		return sb.toString();
	}

	/**
	 * @return A list of strings that correspond to the short stack trace of a throwable.
	 * @param t
	 * @param addPrefix true to add the "caused by" prefix
	 */
	private static List<String> internalGetShortStackTraceStrings(final Throwable t, final boolean addPrefix) {
		List<String> result = new ArrayList<String>();
		if (addPrefix) {
			result.add(STACK_TRACE_CAUSED_BY + t.toString());
		} else {
			result.add(t.toString());
		}
		Throwable cause;
		if (t instanceof ServletException) {
			cause = ((ServletException) t).getRootCause();
		} else {
			cause = t.getCause();
		}
		if (cause != null) {
			result.addAll(internalGetShortStackTraceStrings(cause, true));
		}
		return result;
	}

	/**
	 * @return A list of strings that correspond to the short stack trace of an exception.
	 * @param t
	 */
	public static List<String> getShortStackTraceStrings(final Throwable t) {
		return internalGetShortStackTraceStrings(t, false);
	}

	/**
	 * @return A short printable form of the stack trace of an exception.
	 * @param t
	 */
	public static String getShortPrintableStackTrace(final Throwable t) {
		StringBuffer sb = new StringBuffer();
		List<String> strings = getShortStackTraceStrings(t);
		String separator = "";
		for (String string : strings) {
			sb.append(separator).append(string);
			separator = "\n";
		}
		return sb.toString();
	}

	/**
	 * @return The real cause of an exception.
	 * @param t
	 */
	public static Throwable getCause(final Throwable t) {
		Throwable cause;
		if (t instanceof ServletException) {
			cause = ((ServletException) t).getRootCause();
		} else if (t instanceof SQLException) {
			cause = t.getCause();
			if (cause == null) {
				cause = ((SQLException) t).getNextException();
			}
		} else {
			cause = t.getCause();
		}
		if (cause == null) {
			return null;
		}
		return cause;
	}
	
	/**
	 * @return The real cause of an exception.
	 * @param t
	 */
	public static Throwable getRealCause(final Throwable t) {
		Throwable cause = getCause(t);
		if (cause == null) {
			return t;
		}
		return getRealCause(cause);
	}
	
	/**
	 * @return The causes of an exception as a list.
	 * @param t
	 */
	public static List<Throwable> getCauses(final Throwable t) {
		Throwable cause = getCause(t);
		if (cause == null) {
			List<Throwable> result = new ArrayList<Throwable>();
			result.add(t);
			return result;
		}
		List<Throwable> causeResult = getCauses(cause); 
		causeResult.add(t);
		return causeResult;
	}
	
	/**
	 * @return true if the exception has a cause of the given class, false otherwise.
	 * @param t
	 * @param exceptionClass
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasCause(final Throwable t, @SuppressWarnings("rawtypes") final Class exceptionClass) {
		for (Throwable cause : getCauses(t)) {
			if (exceptionClass.isAssignableFrom(cause.getClass())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return true if the exception has a cause of the given classes, false otherwise.
	 * @param t
	 * @param exceptionClasses
	 */
	public static boolean hasCause(final Throwable t, @SuppressWarnings("rawtypes") final Class [] exceptionClasses) {
		for (@SuppressWarnings("rawtypes") Class clazz : exceptionClasses) {
			if (hasCause(t, clazz)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return a safe exception service.
	 */
	private static ExceptionService createSafeExceptionService() {
		try {
			return new SafeExceptionServiceImpl();
		} catch (Throwable t) {
			LOGGER.error("An exception was thhrown while getting a safe implementation, giving up", t);
			throw new ExceptionHandlingException(t);
		}
	}

	/**
	 * @param t
	 * @throws ExceptionHandlingException 
	 */
	private static void safeCatchException(
			final Throwable t) throws ExceptionHandlingException {
		ExceptionService exceptionService = createSafeExceptionService();
		exceptionService.setParameters(t);
		exceptionService.handleException();
	}

	/**
	 * @return an exception service.
	 */
	private static ExceptionService getExceptionService() {
		ExceptionServiceFactory exceptionServiceFactory  = 
			(ExceptionServiceFactory) BeanUtils.getBean(EXCEPTION_SERVICE_FACTORY_BEAN);
		return exceptionServiceFactory.getExceptionService();
	}
	
	/**
	 * @param t
	 * @return an ExceptionService instance, that can be used later to set a HTTP response.
	 * @throws ExceptionHandlingException 
	 */
	public static ExceptionService catchException(
			final Throwable t) throws ExceptionHandlingException {
		ExceptionService exceptionService = null;
		try {
			exceptionService = getExceptionService();
		} catch (Throwable t1) {
			LOGGER.error(
"An exception was thrown while retrieving the exception service, falling back to a safe implementation", t1);
			safeCatchException(t);
			// never reached
			return null; 
		}
		try {
			exceptionService.setParameters(t);
		} catch (ExceptionHandlingException e1) {
			LOGGER.error(
					"An exception was thrown while setting the parameters of the exception " 
					+ "service, falling back to a safe implementation", e1);
			safeCatchException(t);
			// never reached
			return null; 
		}
		try {
			exceptionService.handleException();
		} catch (ExceptionHandlingException e1) {
			LOGGER.error(
"An exception was thrown while handling the exception, falling back to a safe implementation", e1);
			safeCatchException(t);
			// never reached
			return null; 
		}
		return exceptionService;
	}

	/**
	 * @return true if an exception has already been caught.
	 */
	public static boolean exceptionAlreadyCaught() {
		return ContextUtils.getSessionAttribute(EXCEPTION_MARKER_NAME) != null;
	}

	/**
	 * @return The exception service created when the exception had been thrown.
	 */
	public static ExceptionService getMarkedExceptionService() {
		Object marker = ContextUtils.getSessionAttribute(EXCEPTION_MARKER_NAME); 
		if (marker != null && marker instanceof ExceptionService) {
			return (ExceptionService) marker;
		}
		return null;
	}

	/**
	 * Mark that an exception has been caught.
	 */
	public static void markExceptionCaught() {
		ContextUtils.setSessionAttribute(EXCEPTION_MARKER_NAME, Boolean.TRUE); 
	}

	/**
	 * Mark that an exception has been caught.
	 * @param exceptionService 
	 */
	public static void markExceptionCaught(
			final ExceptionService exceptionService) {
		ContextUtils.setSessionAttribute(EXCEPTION_MARKER_NAME, exceptionService); 
	}

	/**
	 * Unmark that an exception has been caught.
	 */
	public static void unmarkExceptionCaught() {
		ContextUtils.setSessionAttribute(EXCEPTION_MARKER_NAME, null); 
	}

}

