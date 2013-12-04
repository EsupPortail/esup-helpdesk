package org.esupportail.helpdesk.support.spring.context;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.esupportail.commons.utils.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Before Spring 3.1 The imports defined in the application Context will be
 * applied before the PropertyPlaceholderConfigurer So, use the configuration
 * settings through property placeholding for imports isn't possible.
 * 
 * Create a new Spring context bootstrapper that reads the configuration
 * locations from the actual context and push this new context in the local
 * Thread used by esup-commons's BeanUtils
 */
public class SpringXmlContextBootstrapper implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Set<String> configs = new LinkedHashSet<String>();
		configs.add("classpath*:properties/config/properties.xml");
		for (ConfigLocationProvider p : (Collection<ConfigLocationProvider>) applicationContext
				.getBeansOfType(ConfigLocationProvider.class).values()) {
			String[] sp = p.getConfigLocations();
			if (sp != null)
				for (String s : sp)
					configs.add(s);
		}

		ApplicationContext context = new FileSystemXmlApplicationContext(
				configs.toArray(new String[configs.size()]), applicationContext);
		BeanUtils.initBeanFactory(context);
	}

}
