package org.esupportail.helpdesk.support.spring.context;

/*
 * Copyright 2011- Agilord, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.*;

import org.esupportail.commons.utils.Assert;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;

/**
 * Before Spring 3.1 The imports defined in the application Context will be
 * applied before the PropertyPlaceholderConfigurer So, use the configuration
 * settings through property placeholding for imports isn't possible.
 * 
 * 
 * Spring context bootstrapper that reads the configuration locations from the
 * actual context and refreshes it.
 * 
 * @author Istvan Soos
 * @since 1.0
 */
public class SpringWebContextBootstrapper implements InitializingBean,
		ApplicationContextAware {

	protected String[] configLocations;
	protected AbstractRefreshableWebApplicationContext context;

    public void init() throws Exception {
		Set<String> configs = new LinkedHashSet<String>();
		if (configLocations != null)
			for (String s : configLocations)
				configs.add(s);
		for (ConfigLocationProvider p : (Collection<ConfigLocationProvider>) context
				.getBeansOfType(ConfigLocationProvider.class).values()) {
			String[] sp = p.getConfigLocations();
			if (sp != null)
				for (String s : sp)
					configs.add(s);
		}
		context.setConfigLocations(new ArrayList<String>(configs)
				.toArray(new String[configs.size()]));
		context.refresh();
	}

	public void setConfigLocation(String configLocations) {
		String[] parts = configLocations.split("\\,");
		for (int i = 0; i < parts.length; i++)
			parts[i] = parts[i].trim();
		this.configLocations = parts;
	}

	public void setConfigLocations(String[] configLocations) {
		this.configLocations = configLocations;
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = (AbstractRefreshableWebApplicationContext) context;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(configLocations, "property configLocations of class "
				+ this.getClass().getName() + " can not be null");
		init();
	}
}
