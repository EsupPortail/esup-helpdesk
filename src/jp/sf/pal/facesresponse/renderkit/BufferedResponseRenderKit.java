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

import java.io.OutputStream;
import java.io.Writer;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import javax.servlet.ServletContext;

import jp.sf.pal.facesresponse.io.BufferedResponseStream;
import jp.sf.pal.facesresponse.io.BufferedResponseStreamFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BufferedResponseRenderKit extends RenderKit {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory
            .getLog(BufferedResponseRenderKit.class);

    private RenderKit parent;

    public BufferedResponseRenderKit(RenderKit parent) {
        this.parent = parent;
    }

    /**
     * @param family
     * @param rendererType
     * @param renderer
     * @see javax.faces.render.RenderKit#addRenderer(java.lang.String,
     *      java.lang.String, javax.faces.render.Renderer)
     */
    public void addRenderer(String family, String rendererType,
            Renderer renderer) {
        parent.addRenderer(family, rendererType, renderer);
    }

    /**
     * @param out
     * @return .
     * @see javax.faces.render.RenderKit#createResponseStream(java.io.OutputStream)
     */
    public ResponseStream createResponseStream(OutputStream out) {
        return parent.createResponseStream(out);
    }

    /**
     * @param writer
     * @param contentTypeList
     * @param characterEncoding
     * @return .
     * @see javax.faces.render.RenderKit#createResponseWriter(java.io.Writer,
     *      java.lang.String, java.lang.String)
     */
    public ResponseWriter createResponseWriter(Writer writer,
            String contentTypeList, String characterEncoding) {
        if (FacesContext.getCurrentInstance().getExternalContext().getContext() instanceof ServletContext) {
            // Servlet
            if (log.isDebugEnabled()) {
                log
                        .debug("createResponseWriter(Writer, String, String) - Servlet environment: context="
                                + FacesContext.getCurrentInstance()
                                        .getExternalContext().getContext());
            }

            return parent.createResponseWriter(writer, contentTypeList,
                    characterEncoding);
        } else {
            // Portlet
            if (log.isDebugEnabled()) {
                log
                        .debug("createResponseWriter(Writer, String, String) - Portlet environment: context="
                                + FacesContext.getCurrentInstance()
                                        .getExternalContext().getContext());
            }

            BufferedResponseStream bufferedResponseStream = BufferedResponseStreamFactory
                    .getBufferedResponseStream();
            if (bufferedResponseStream == null) {
                if (log.isDebugEnabled()) {
                    log
                            .debug("createResponseWriter(Writer, String, String) - BufferedResponseStream is null.");
                }
                return parent.createResponseWriter(writer, contentTypeList,
                        characterEncoding);
            }

            Writer bufferedWriter = bufferedResponseStream.getFacesWriter();
            if (bufferedWriter == null) {
                if (log.isDebugEnabled()) {
                    log
                            .debug("createResponseWriter(Writer, String, String) - Writer of the  BufferedResponseStream is null.");
                }
                return parent.createResponseWriter(writer, contentTypeList,
                        characterEncoding);
            }

            if (log.isDebugEnabled()) {
                log
                        .debug("createResponseWriter(Writer, String, String) - ResponseWriter is replaced.");
            }
            return parent.createResponseWriter(writer, contentTypeList,
                    characterEncoding).cloneWithWriter(bufferedWriter);
        }
    }

    /**
     * @param obj
     * @return .
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return parent.equals(obj);
    }

    /**
     * @param family
     * @param rendererType
     * @return .
     * @see javax.faces.render.RenderKit#getRenderer(java.lang.String,
     *      java.lang.String)
     */
    public Renderer getRenderer(String family, String rendererType) {
        return parent.getRenderer(family, rendererType);
    }

    /**
     * @return .
     * @see javax.faces.render.RenderKit#getResponseStateManager()
     */
    public ResponseStateManager getResponseStateManager() {
        return parent.getResponseStateManager();
    }

    /**
     * @return .
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return parent.hashCode();
    }

    /**
     * @return .
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return parent.toString();
    }
}
