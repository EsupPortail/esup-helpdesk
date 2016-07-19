/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.aop.monitor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Monitor annotation.
 */
@Documented
//available at runtime
@Retention(RetentionPolicy.RUNTIME)
// applies to methods
@Target({ElementType.TYPE, ElementType.METHOD })
@Inherited
public @interface Monitor {
	// no parameter
}
