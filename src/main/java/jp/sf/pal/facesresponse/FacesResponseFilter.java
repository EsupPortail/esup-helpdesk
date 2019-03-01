/*
 * Copyright 2004-2006 The Apache Software Foundation.
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
package jp.sf.pal.facesresponse;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import jp.sf.pal.facesresponse.io.BufferedResponseStreamFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.portals.bridges.portletfilter.PortletFilter;
import org.apache.portals.bridges.portletfilter.PortletFilterChain;
import org.apache.portals.bridges.portletfilter.PortletFilterConfig;

/**
 * 
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 */
public class FacesResponseFilter implements PortletFilter {
    private static final Log log = LogFactory.getLog(FacesResponseFilter.class);

    private PortletConfig portletConfig;

    /**
     * Called by init method to initialize this portlet filter.
     * 
     * @param filterConfig
     * @throws PortletException
     */
    public void init(PortletFilterConfig filterConfig) throws PortletException {
        if (log.isDebugEnabled())
            log.debug("Initializing FacesResponseFilter.");

        setPortletConfig(filterConfig.getPortletConfig());

    }

    /**
     * 
     * @param request
     * @param response
     * @param chain
     *            PortletFilterChain instance
     * @throws PortletException
     */
    public void renderFilter(RenderRequest request, RenderResponse response,
            PortletFilterChain chain) throws PortletException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("called renderFilter.");
        }

        try {
            BufferedResponseStreamFactory.init(request, response);

            // call next rednerFilter
            chain.renderFilter(request, response);

            BufferedResponseStreamFactory.render(request, response);
        } catch (Exception e) {
            log.error(e);
            throw new PortletException(e);
        }

    }

    /**
     * 
     * @param request
     * @param response
     * @param chain
     *            PortletFilterChain instance
     * @throws PortletException
     */
    public void processActionFilter(ActionRequest request,
            ActionResponse response, PortletFilterChain chain)
            throws PortletException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("called processActionFilter.");
        }

        // call next processActionFilter
        chain.processActionFilter(request, response);
    }

    /**
     * Called by destroy method to destroy this portlet filter.
     */
    public void destroy() {
    }

    /**
     * @return Returns the portletConfig.
     */
    public PortletConfig getPortletConfig() {
        return portletConfig;
    }

    /**
     * @param portletConfig
     *            The portletConfig to set.
     */
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

}
