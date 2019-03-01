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
package jp.sf.pal.facesresponse.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import jp.sf.pal.facesresponse.FacesResponseConstants;
import jp.sf.pal.facesresponse.util.FacesResponseUtil;

/**
 * @author shinsuke
 * 
 */
public class BufferedResponseStreamFactory {
    public static void init(RenderRequest request, RenderResponse response) {
        String outputEncoding = response.getCharacterEncoding();
//        if (FacesResponseUtil.isMyFacesFacesContext(request)) {
            // for MyFaces
            // MyFaces needs this workaround code because of the implementation
            // issue..
            PortletSession session = request.getPortletSession(true);
            // FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesContext facesContext = FacesResponseUtil
                    .getMyFacesFacesContext(request);
            boolean existResonseWriter = true;
            if (facesContext == null) {
                existResonseWriter = false;
            } else {
                if (facesContext.getResponseWriter() == null) {
                    existResonseWriter = false;
                }
            }

            Object oldBufferedResonseStream = session
                    .getAttribute(FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
            if (existResonseWriter && oldBufferedResonseStream != null) {
                ((BufferedResponseStream) oldBufferedResonseStream).recycle();
            } else {
                BufferedResponseStream bufferedResponseStream = new BufferedResponseStream(
                        outputEncoding);
                session.setAttribute(
                        FacesResponseConstants.BUFFERED_RESPONSE_STREAM,
                        bufferedResponseStream);
            }
//        } else {
//            // for JSF implementation whice does not pass FacesContext from
//            // processAction to render
//            BufferedResponseStream bufferedResponseStream = new BufferedResponseStream(
//                    outputEncoding);
//            request.setAttribute(
//                    FacesResponseConstants.BUFFERED_RESPONSE_STREAM,
//                    bufferedResponseStream);
//        }
    }

    public static BufferedResponseStream getBufferedResponseStream() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            throw new IllegalStateException("FacesContext is null.");
        }

        BufferedResponseStream s = null;
//        if (FacesResponseUtil.isMyFacesFacesContext()) {
            // for MyFaces
            // MyFaces needs this workaround code because of the implementation
            // issue..
            s = (BufferedResponseStream) context.getExternalContext()
            .getSessionMap().get(
                    FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
//            return (BufferedResponseStream) context.getExternalContext()
//            .getSessionMap().get(
//                    FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
//        } else {
//            // for JSF implementation whice does not pass FacesContext from
//            // processAction to render        	
//            s = (BufferedResponseStream) context.getExternalContext()
//            .getRequestMap().get(
//                    FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
////            return (BufferedResponseStream) context.getExternalContext()
////            .getRequestMap().get(
////                    FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
//        }
    	return s;
    }

    public static BufferedResponseStream getBufferedResponseStream(
            PortletRequest request) {
        BufferedResponseStream s = null;
//        if (FacesResponseUtil.isMyFacesFacesContext(request)) {
            // for MyFaces
            // MyFaces needs this workaround code because of the implementation
            // issue..
        	s = (BufferedResponseStream) request.getPortletSession()
            .getAttribute(
                    FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
//            return (BufferedResponseStream) request.getPortletSession()
//            .getAttribute(
//                    FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
//        } else {
//            // for JSF implementation whice does not pass FacesContext from
//            // processAction to render
//        	s = (BufferedResponseStream) request
//                .getAttribute(FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
////            return (BufferedResponseStream) request
////            .getAttribute(FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
//        }
    	return s;
    }

    public static void render(RenderRequest request, RenderResponse response)
            throws IOException {
        BufferedResponseStream bufferedResponseStream = getBufferedResponseStream(request);
        if (bufferedResponseStream == null) {
            throw new IllegalStateException("BufferedResponseStream is null.");
        }
        synchronized (bufferedResponseStream) {
            // flush stream
            bufferedResponseStream.commit();

            if (request.getWindowState() != WindowState.MINIMIZED) {
                Reader r = bufferedResponseStream.getReader();
                Writer w = response.getWriter();
                char[] bytes = new char[FacesResponseConstants.BLOCK_SIZE];
                try {
                    int length = r.read(bytes);
                    while (length != -1) {
                        if (length != 0) {
                            w.write(bytes, 0, length);
                        }
                        length = r.read(bytes);
                    }
                } finally {
                    bytes = null;
                }
                w.flush();
            }
        }

        // cleanup
//        if (FacesResponseUtil.isMyFacesFacesContext(request)) {
            // for MyFaces
            // MyFaces needs this workaround code because of the implementation
            // issue..
            // request.getPortletSession().removeAttribute(
            // FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
//        } else {
//            // for JSF implementation whice does not pass FacesContext from
//            // processAction to render
//            request
//                    .removeAttribute(FacesResponseConstants.BUFFERED_RESPONSE_STREAM);
//        }
    }

}
