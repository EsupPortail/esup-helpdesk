/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * AOP Utilities.
 */
public abstract class AopUtils {

	/**
	 * Constructor.
	 */
	private AopUtils() {
		super();
	}

	/**
	 * @param joinPoint
	 * @return The key to cache the method call
	 */
	public static String getCacheKey(
			final ProceedingJoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		String key = joinPoint.getSignature().toLongString() + "(";
		String separator = "";
		for (int i = 0; i < args.length; i++) {
			key += separator;
			Object o = args[i];
			if (o == null) {
				key += "null";
			} else {
				key += o.getClass().getSimpleName();
				if (o instanceof String || o instanceof Number || o instanceof Boolean) {
					key += "[" + o + "]";
				} else {
					key += "#" + o.hashCode();
				}
			}
			separator = ", ";
		}
		key += ")";
		return key;
	}

	/**
	 * @param joinPoint
	 * @return The log signature
	 */
	public static String getLogSignature(
			final ProceedingJoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		String signature = joinPoint.getSignature().toLongString();
		String[] signatureParts = signature.split("\\(");
		String[] pathParts = signatureParts[0].split("\\.");
		String methodName = pathParts[pathParts.length - 1];
		String className = pathParts[pathParts.length - 2];
		String str = className + "." + methodName + "(";
		String separator = "";
		for (int i = 0; i < args.length; i++) {
			str += separator;
			Object o = args[i];
			if (o == null) {
				str += "null";
			} else {
				String[] nameParts = o.getClass().getSimpleName().split("\\.");
				String argClassName = nameParts[nameParts.length - 1];
				if (o instanceof String || o instanceof Number || o instanceof Boolean) {
					str += argClassName + "[" + o + "]";
				} else {
					str += argClassName + "#" + o.hashCode();
				}
			}
			separator = ", ";
		}
		str += ")";
		return str;
	}

}