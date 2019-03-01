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
package jp.sf.pal.facesresponse.renderkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

public class BufferedResponseRenderKitFactoryImpl extends RenderKitFactory {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory
            .getLog(BufferedResponseRenderKitFactoryImpl.class);

    private RenderKitFactory parent;

    public BufferedResponseRenderKitFactoryImpl(RenderKitFactory parent) {
        if (log.isDebugEnabled()) {
            log
                    .debug("ExtensionsRenderKitFactoryImpl(RenderKitFactory) parent="
                            + parent);
        }
        this.parent = parent;
    }

    public void addRenderKit(String renderKitId, RenderKit renderKit) {
        parent.addRenderKit(renderKitId, renderKit);
    }

    public RenderKit getRenderKit(FacesContext context, String renderKitId) {
        return new BufferedResponseRenderKit(parent.getRenderKit(context,
                renderKitId));
    }

    public Iterator getRenderKitIds() {
        return parent.getRenderKitIds();
    }

}
