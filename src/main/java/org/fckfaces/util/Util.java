package org.fckfaces.util;

import javax.faces.context.FacesContext;

public class Util {
	
//	public static final String FCK_FACES_RESOURCE_PREFIX = "/fckfaces";
	public static final String FCK_FACES_RESOURCE_PREFIX = "/media";
	
	public static final String internalPath(String path) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + FCK_FACES_RESOURCE_PREFIX + path;
	}
	
	public static final String externalPath(String path) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + path;
	}
}
