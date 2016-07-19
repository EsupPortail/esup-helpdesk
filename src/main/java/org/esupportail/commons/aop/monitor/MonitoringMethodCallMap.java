/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.aop.monitor;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import org.aspectj.lang.ProceedingJoinPoint;
import org.esupportail.commons.aop.AopUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/**
 * A monitoring call map.
 */
public class MonitoringMethodCallMap extends HashMap<String, MonitoringMethodCall> {

	/** The serialization id. */
	private static final long serialVersionUID = -9061322717388837102L;
	
	/** A logger. */
	private final Logger logger = new LoggerImpl(getClass());
	
	/** Constructor. */
	public MonitoringMethodCallMap() {
		super();
	}
	/**
	 * Add an execution time.
	 * @param joinPoint 
	 * @param startTime
	 */
	public void addCall(
			final ProceedingJoinPoint joinPoint,
			final long startTime) {
		long time = System.currentTimeMillis() - startTime;
		if (logger.isDebugEnabled()) {
			logger.debug("TIME[" + time + "]\t" + AopUtils.getLogSignature(joinPoint));
		}
		String key = AopUtils.getCacheKey(joinPoint);
		if (containsKey(key)) {
			get(key).addTime(time);
		} else {
			put(key, new MonitoringMethodCall(joinPoint, time));
		}
	}
	/**
	 * Print a log report.
	 * @param startTime
	 * @param message 
	 */
	public void printLogReport(
			final long startTime,
			final String message) {
		long time = System.currentTimeMillis() - startTime;
		logger.info(
				"===================================================================== " 
				+ message + "\nTOTAL TIME[" + time + "]");
		if (this.isEmpty()) {
			logger.info("no method call recorded.");
			return;
		}
		Set<MonitoringMethodCall> sortedSet = new TreeSet<MonitoringMethodCall>();
		sortedSet.addAll(values());
		String previousClassName = null;
		for (MonitoringMethodCall call : sortedSet) {
			if (!call.getClassName().equals(previousClassName)) {
				logger.info("---------------- " + call.getClassName());
				previousClassName = call.getClassName();
			}
			logger.info(call.getTimes() + " " + call.getMethodName() + " " + call.getFullString());
		}
	}

}
