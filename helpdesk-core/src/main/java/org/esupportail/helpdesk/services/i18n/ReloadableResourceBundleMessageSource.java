package org.esupportail.helpdesk.services.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class ReloadableResourceBundleMessageSource extends
	org.springframework.context.support.ReloadableResourceBundleMessageSource {
	
	public Map<String, String> getStrings(Locale locale) {
		Map<String, String> ret = new HashMap<String, String>();
		PropertiesHolder propertiesHolder = getMergedProperties(locale);
		Properties properties = propertiesHolder.getProperties();
		for (Object key : properties.keySet()) {
			ret.put((String)key, (String)properties.get(key));
		} 
		return ret;
	}
	
}
