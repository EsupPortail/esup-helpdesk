/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esupportail.commons.utils.HttpUtils;

/** 
 * A portal authenticator.
 */
public class PortalAuthenticationService extends AbstractTypedAuthenticationService {

	/**
	 * The default value for uidPortalAttribute.
	 */
	public static final String DEFAULT_UID_PORTAL_ATTRIBUTE = "uid";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6775192478532264210L;

	/**
	 * The portal attribute that contains the uid. 
	 */
	private String uidPortalAttribute = DEFAULT_UID_PORTAL_ATTRIBUTE;
	
	/**
	 * The portal attributes. 
	 */
	private List<String> portalAttributes;
	
	/**
	 * Bean constructor.
	 */
	public PortalAuthenticationService() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractRealAuthenticationService#getAuthId()
	 */
	@Override
	public String getAuthId() {
		return HttpUtils.getPortalPref(uidPortalAttribute);
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractRealAuthenticationService#getAuthAttributes()
	 */
	@Override
	protected Map<String, List<String>> getAuthAttributes() {
		if (portalAttributes == null) {
			return null;
		}
		Map<String, List<String>> attributes = new HashMap<String, List<String>>();
		for (String portalAttribute : portalAttributes) {
			String value = HttpUtils.getPortalPref(portalAttribute);
			if (value != null) {
				List<String> values = new ArrayList<String>();
				values.add(value);
				attributes.put(portalAttribute, values);
			}
		}
		return attributes;
	}

	/**
	 * @param uidPortalAttribute the uidPortalAttribute to set
	 */
	public void setUidPortalAttribute(final String uidPortalAttribute) {
		this.uidPortalAttribute = uidPortalAttribute;
	}

	/**
	 * @return the uidPortalAttribute
	 */
	protected String getUidPortalAttribute() {
		return uidPortalAttribute;
	}

	/**
	 * @param portalAttributes the portalAttributes to set
	 */
	public void setPortalAttributes(final String portalAttributes) {
		if (portalAttributes == null 
				|| !org.springframework.util.StringUtils.hasLength(portalAttributes)) {
			return;
		}
		this.portalAttributes = new ArrayList<String>();
		for (String portalAttribute : portalAttributes.split(",")) {
			if (!this.portalAttributes.contains(portalAttribute)) {
				this.portalAttributes.add(portalAttribute);
			}
		}
		Collections.sort(this.portalAttributes);
	}

}
