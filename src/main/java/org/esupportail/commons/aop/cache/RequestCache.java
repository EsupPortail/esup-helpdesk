/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.aop.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The RequestCache annotation.
 */
@Documented
//available at runtime
@Retention(RetentionPolicy.RUNTIME)
// applies to methods
@Target(ElementType.METHOD) 
public @interface RequestCache {
	// no parameter
}
