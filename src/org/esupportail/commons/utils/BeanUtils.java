/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils; 

import java.util.Map;

import javax.portlet.PortletContext;
import javax.servlet.ServletContext;

import org.esupportail.commons.exceptions.ConfigException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;

/**
 * A class to bind a beanFocatry to the current thread and retrieve beans.
 */
public final class BeanUtils {   
	
	/**
	 * The configuration file where Spring beans are defined.
	 */
	private static final String SPRING_CONFIG_FILE = "/properties/applicationContext.xml"; 

	/**
	 * The bean factory used for this thread.
	 */
	private static final ThreadLocal<ApplicationContext> CONTEXT_HOLDER = new ThreadLocal<ApplicationContext>();

	/**
	 * Private constructor.
	 */
	private BeanUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Set the bean factory from a servlet context.
	 * @param servletContext
	 */
	public static void initBeanFactory(final ServletContext servletContext) {
		CONTEXT_HOLDER.set(
				WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext));
	}

	/**
	 * Set the bean factory from a portlet context.
	 * @param portletContext
	 */
	public static void initBeanFactory(final PortletContext portletContext) {
		CONTEXT_HOLDER.set(
				PortletApplicationContextUtils.getRequiredWebApplicationContext(portletContext));
	}
	
	/**
	 * Initialize the bean factory using a given config file.
	 * @param configFile
	 */
	public static void initBeanFactory(final String configFile) {
		CONTEXT_HOLDER.set(new FileSystemXmlApplicationContext("classpath:" + configFile));
	}

	/**
	 * @return the bean factory for the thread.
	 */
	private static BeanFactory getBeanFactory() {
		if (CONTEXT_HOLDER.get() == null) {
			initBeanFactory(SPRING_CONFIG_FILE);
		}
		return CONTEXT_HOLDER.get();
	}

	/**
	 * Get a bean.
	 * @param name the name of the bean
	 * @return a bean.
	 * @throws ConfigException
	 */
	public static Object getBean(
			final String name) throws ConfigException {
		try {
			return getBeanFactory().getBean(name);
		} catch (BeansException e) {
			throw new ConfigException(e);
		}
	}

	/**
	 * @param type 
	 * @return the beans of a given type.
	 * @throws ConfigException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getBeansOfClass(
			@SuppressWarnings("rawtypes") final Class type) throws ConfigException {
		BeanFactory beanFactory = getBeanFactory();
		if (!(beanFactory instanceof ListableBeanFactory)) {
			throw new ConfigException(
					"bean factory is not an instance of ListableBeanFactory (" 
					+ beanFactory.getClass() + ")");
		}
		return BeanFactoryUtils.beansOfTypeIncludingAncestors(
				(ListableBeanFactory) beanFactory, type);
	}

}
