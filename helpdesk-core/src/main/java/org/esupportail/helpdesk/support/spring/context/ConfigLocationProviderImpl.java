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

	/**
	 * Simple {@link ConfigLocationProvider} implementation
	 * 
	 * @author Istvan Soos
	 * @since 1.0
	 */
	public class ConfigLocationProviderImpl implements ConfigLocationProvider {

	    protected String[] configLocations;

	    public String[] getConfigLocations() {
	        return configLocations;
	    }

	    public void setConfigLocation(String configLocation) {
	        String[] parts = configLocation.split("\\,");
	        for (int i = 0; i < parts.length; i++)
	            parts[i] = parts[i].trim();
	        this.configLocations = parts;
	    }

	    public void setConfigLocations(String[] configLocations) {
	        this.configLocations = configLocations;
	    }
	    
}
