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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import jp.sf.pal.facesresponse.util.FacesResponseUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author shinsuke
 * 
 */
public class BufferedResponseStream {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory
            .getLog(BufferedResponseStream.class);

    private ByteArrayOutputStream byteArrayOutputStream;

    private ByteArrayInputStream byteArrayInputStream;

    private Writer writer;

    private String encoding;

    private ByteArrayOutputStream facesOutputStream;

    public BufferedResponseStream(String encoding) {
        this.encoding = encoding;
        byteArrayOutputStream = null;
        byteArrayInputStream = null;
        writer = null;
        facesOutputStream = null;
    }

    public void recycle() {
        byteArrayInputStream = null;
        if (facesOutputStream != null) {
            facesOutputStream.reset();
            byteArrayOutputStream = facesOutputStream;
            try {
                writer = new OutputStreamWriter(byteArrayOutputStream,
                        getEncoding());
            } catch (UnsupportedEncodingException e) {
                log.warn("Unsupported encoding: " + getEncoding(), e);
                writer = new OutputStreamWriter(byteArrayOutputStream);
            }
        } else {
            byteArrayOutputStream = null;
            writer = null;
            facesOutputStream = null;
        }
    }

    public OutputStream getOutputStream() {
        if (log.isDebugEnabled()) {
            log.debug("getOutputStream()");
        }

        if (writer != null) {
            throw new IllegalStateException(
                    "getOutputStream() cannot be called after getWriter().");
        }

        if (byteArrayOutputStream == null) {
            byteArrayOutputStream = new ByteArrayOutputStream();
        }
        return byteArrayOutputStream;
    }

    public Writer getWriter() {
        if (log.isDebugEnabled()) {
            log.debug("getWriter()");
        }
        if (byteArrayOutputStream != null && writer == null) {
            throw new IllegalStateException(
                    "getWriter() cannot be called after getOutputStream().");
        }

        if (writer == null) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                writer = new OutputStreamWriter(byteArrayOutputStream,
                        getEncoding());
            } catch (UnsupportedEncodingException e) {
                log.warn("Unsupported encoding: " + getEncoding(), e);
                writer = new OutputStreamWriter(byteArrayOutputStream);
            }
        }
        return writer;
    }

    public Writer getFacesWriter() {
        Writer writer = getWriter();
        // for MyFaces
        // MyFaces needs this workaround code because of the implementation
        // issue..
        facesOutputStream = byteArrayOutputStream;
        return writer;
    }

    public InputStream getInputStream() {
        if (log.isDebugEnabled()) {
            log.debug("getInputStream()");
        }
        if (byteArrayInputStream == null) {
            byte[] emptyBytes = new byte[0];
            byteArrayInputStream = new ByteArrayInputStream(emptyBytes);
        }
        return byteArrayInputStream;
    }

    public Reader getReader() {
        if (log.isDebugEnabled()) {
            log.debug("getReader()");
        }

        try {
            return new InputStreamReader(getInputStream(), getEncoding());
        } catch (UnsupportedEncodingException e) {
            log.warn("Unsupported encoding: " + getEncoding(), e);
            return new InputStreamReader(getInputStream());
        }
    }

    public boolean commit() {
        if (log.isDebugEnabled()) {
            log.debug("commit()- byteArrayOutputStream="
                    + byteArrayOutputStream);
        }

        if (byteArrayOutputStream != null) {
            try {
                if (writer != null) {
                    writer.flush();
                }
                byteArrayOutputStream.flush();
            } catch (IOException e) {
                log.error("Could not flush the output stream.", e);
                return false;
            }

            byteArrayInputStream = new ByteArrayInputStream(
                    byteArrayOutputStream.toByteArray());
            if (byteArrayOutputStream != facesOutputStream
                    || !FacesResponseUtil.isMyFacesFacesContext()) {
                // non-MyFaces always calls close()
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        log.warn("Could not close the writer.", e);
                    }
                }
                // ByteArrayOutputStream#close() is no effect.
            }
            byteArrayOutputStream = null;
            writer = null;
            return true;
        }
        return false;
    }

    /**
     * @return Returns the encoding.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding
     *            The encoding to set.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
