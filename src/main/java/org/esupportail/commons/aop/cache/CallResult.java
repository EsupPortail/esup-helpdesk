/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.aop.cache;

/**
 * A call result.
 */
public class CallResult {

	/** The result itself. */
	private Object result;
	/** The exception throw if any. */
	private Throwable throwable;
	/** The execution time. */
	private long time;
	
	/** Constructor. */
	public CallResult() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param result
	 * @param throwable
	 * @param time 
	 */
	CallResult(
			final Object result, 
			final Throwable throwable,
			final long time) {
		super();
		this.result = result;
		this.throwable = throwable;
		this.time = time;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (throwable == null) {
			return " returns " + result;
		}
		return " throws " + throwable;
	}

	/** 
	 * @return the result 
	 */
	public Object getResult() {
		return result;
	}
	
	/** 
	 * @return the throwable 
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
}
	
