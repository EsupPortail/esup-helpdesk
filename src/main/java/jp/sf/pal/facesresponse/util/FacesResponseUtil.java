/*
 * Copyright 2005-2006 Portal Application Laboratory project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.sf.pal.facesresponse.util;

import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;

/**
 * @author shinsuke
 * 
 */
public class FacesResponseUtil {
    public static final String MYFACES_CURRENT_FACES_CONTEXT = "org.apache.myfaces.portlet.MyFacesGenericPortlet.CURRENT_FACES_CONTEXT";

    public static boolean isMyFacesFacesContext(PortletRequest request) {
        if (request.getPortletSession().getAttribute(
                MYFACES_CURRENT_FACES_CONTEXT) != null) {
            return true;
        }
        return false;
    }

    public static boolean isMyFacesFacesContext() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null
                && facesContext.getExternalContext().getSessionMap().get(
                        MYFACES_CURRENT_FACES_CONTEXT) != null) {
            return true;
        }
        return false;
    }

    public static FacesContext getMyFacesFacesContext(PortletRequest request) {
        return (FacesContext) request.getPortletSession().getAttribute(
                MYFACES_CURRENT_FACES_CONTEXT);
    }
}
