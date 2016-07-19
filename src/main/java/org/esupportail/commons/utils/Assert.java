/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils; 

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.esupportail.commons.exceptions.ConfigException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


/**
 * An Assert class that throws ConfigException.
 */
public abstract class Assert {

	/**
	 * Constructor.
	 */
	private Assert() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Assert a boolean expression, throwing <code>IllegalArgumentException</code>
	 * if the test result is <code>false</code>.
	 * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
	 * @param expression a boolean expression
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if expression is <code>false</code>
	 */
	public static void isTrue(final boolean expression, final String message) {
		if (!expression) {
			throw new ConfigException(message);
		}
	}

	/**
	 * Assert that an object is <code>null</code> .
	 * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is not <code>null</code>
	 */
	public static void isNull(final Object object, final String message) {
		if (object != null) {
			throw new ConfigException(message);
		}
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is <code>null</code>
	 */
	public static void notNull(final Object object, final String message) {
		if (object == null) {
			throw new ConfigException(message);
		}
	}

	/**
	 * Assert that a string is not empty; that is, it must not be <code>null</code> and not empty.
	 * <pre class="code">Assert.hasLength(name, "Name must not be empty");</pre>
	 * @param text the string to check
	 * @param message the exception message to use if the assertion fails
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(final String text, final String message) {
		if (!StringUtils.hasLength(text)) {
			throw new ConfigException(message);
		}
	}

	/**
	 * Assert that a string has valid text content; that is, it must not be <code>null</code>
	 * and must contain at least one non-whitespace character.
	 * <pre class="code">Assert.hasText(name, "Name must not be empty");</pre>
	 * @param text the string to check
	 * @param message the exception message to use if the assertion fails
	 * @see StringUtils#hasText
	 */
	public static void hasText(final String text, final String message) {
		if (!StringUtils.hasText(text)) {
			throw new ConfigException(message);
		}
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * <pre class="code">Assert.doesNotContain(name, "rod", "Name must not contain 'rod'");</pre>
	 * @param textToSearch the text to search
	 * @param substring the substring to find within the text
	 * @param message the exception message to use if the assertion fails
	 */
	public static void doesNotContain(final String textToSearch, final String substring, final String message) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) 
				&& textToSearch.indexOf(substring) != -1) {
			throw new ConfigException(message);
		}
	}

	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array, "The array must have elements");</pre>
	 * @param array the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object array is <code>null</code> or has no elements
	 */
	public static void notEmpty(final Object[] array, final String message) {
		if (ObjectUtils.isEmpty(array)) {
			throw new ConfigException(message);
		}
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @param message the exception message to use if the assertion fails
	 */
	public static void notEmpty(@SuppressWarnings("rawtypes") final Collection collection, final String message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new ConfigException(message);
		}
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">Assert.notEmpty(map, "Map must have entries");</pre>
	 * @param map the map to check
	 * @param message the exception message to use if the assertion fails
	 */
	public static void notEmpty(@SuppressWarnings("rawtypes") final Map map, final String message) {
		if (CollectionUtils.isEmpty(map)) {
			throw new ConfigException(message);
		}
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
	 * @param clazz the required class
	 * @param obj the object to check
	 * @param message a message which will be prepended to the message produced by
	 * the function itself, and which may be used to provide context. It should
	 * normally end in a ": " or ". " so that the function generate message looks
	 * ok when prepended to it.
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(@SuppressWarnings("rawtypes") final Class clazz, final Object obj, final String message) {
		Assert.notNull(clazz, "The clazz to perform the instanceof assertion cannot be null");
		if (obj == null) {
			Assert.isTrue(clazz.isInstance(obj), message 
					+ "Object of class '" + "[null]" 
					+ "' must be an instance of '" + clazz.getName() + "'");
		} else {
			Assert.isTrue(clazz.isInstance(obj), message 
					+ "Object of class '" + obj.getClass().getName() 
					+ "' must be an instance of '" + clazz.getName() + "'");
		}
	}

	/**
	 * Assert that <code>superType.isAssignableFrom(subType)</code> is <code>true</code>.
	 * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
	 * @param superType the super type to check
	 * @param subType the sub type to check
	 * @param message a message which will be prepended to the message produced by
	 * the function itself, and which may be used to provide context. It should
	 * normally end in a ": " or ". " so that the function generate message looks
	 * ok when prepended to it.
	 */
	@SuppressWarnings("unchecked")
	public static void isAssignable(@SuppressWarnings("rawtypes") final Class superType, @SuppressWarnings("rawtypes") final Class subType, final String message) {
		Assert.notNull(superType, "superType cannot be null");
		Assert.notNull(subType, "subType cannot be null");
		Assert.isTrue(superType.isAssignableFrom(subType), message + "Type [" + subType.getName()
						+ "] is not assignable to type [" + superType.getName() + "]");
	}


	/**
	 * Assert a boolean expression, throwing <code>IllegalStateException</code>
	 * if the test result is <code>false</code>. Call isTrue if you wish to
	 * throw IllegalArgumentException on an assertion failure.
	 * <pre class="code">Assert.state(id == null, "The id property must not already be initialized");</pre>
	 * @param expression a boolean expression
	 * @param message the exception message to use if the assertion fails
	 */
	public static void state(final boolean expression, final String message) {
		if (!expression) {
			throw new ConfigException(message);
		}
	}

	/**
	 * Assert that a list of strings contains a value.
	 * @param values
	 * @param listName 
	 * @param value 
	 */
	public static void contains(
			final List<String> values,
			final String listName,
			final String value) {
		if (values == null) {
			throw new ConfigException("list " + listName + " should not be null");
		}
		if (!values.contains(value)) {
			String msg = "allowed values for " + listName + " are ";
			String separator = "";
			for (String string : values) {
				msg += separator + "'" + string + "'";
				separator = ", ";
			}
			throw new ConfigException(msg);
		}
	}

	/**
	 * Assert that an array of strings contains a value.
	 * @param values
	 * @param listName 
	 * @param value 
	 */
	public static void contains(
			final String [] values,
			final String listName,
			final String value) {
		contains(Arrays.asList(values), listName, value);
	}

	/**
	 * Assert that a list of strings contains a value.
	 * @param values
	 * @param listName 
	 * @param value 
	 */
	public static void contains(
			final List<Integer> values,
			final String listName,
			final Integer value) {
		if (values == null) {
			throw new ConfigException("list " + listName + " should not be null");
		}
		if (!values.contains(value)) {
			String msg = "allowed values for " + listName + " are ";
			String separator = "";
			for (Integer integer : values) {
				separator = ", ";
				msg += separator + "'" + integer + "'";
			}
			throw new ConfigException(msg);
		}
	}

	/**
	 * Assert that an array of strings contains a value.
	 * @param values
	 * @param listName 
	 * @param value 
	 */
	public static void contains(
			final Integer [] values,
			final String listName,
			final Integer value) {
		contains(Arrays.asList(values), listName, value);
	}

}
