/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.servlet;

import java.net.SocketException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.DownloadUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The servlet to download files.
 */
public class DownloadServlet extends HttpServlet {

	/**
	 * The attribute to retrieve the data from.
	 */
	public static final String ID_ATTRIBUTE = "downloadId"; 

	/**
	 * The attribute to retrieve the data from.
	 */
	public static final String DATA_ATTRIBUTE = "downloadData-"; 

	/**
	 * The attribute to retrieve the content-type from.
	 */
	public static final String CONTENT_TYPE_ATTRIBUTE = "downloadContentType-"; 

	/**
	 * The atribute to retrieve the content-disposition from.
	 */
	public static final String CONTENT_DISPOSITION_ATTRIBUTE = "downloadContentDisposition"; 

	/**
	 * The attribute to retrieve the filename from.
	 */
	public static final String FILENAME_ATTRIBUTE = "downloadFilename-"; 

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -7231367075834134378L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The encoding.
	 */
	private String encoding = "UTF-8";

	/**
	 * Constructor.
	 */
	public DownloadServlet() {
		super();
	}

	/**
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);
		String configEncoding = config.getInitParameter("encoding");
		if (configEncoding != null) {
			encoding = configEncoding;
		}
	}



	/**
	 * @throws ServletException 
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	public void service(
			final ServletRequest servletRequest, 
			final ServletResponse servletResponse) 
	throws ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String id = request.getParameter(ID_ATTRIBUTE);
		if (id == null) {
			throw new DownloadException(
					"attribute " + ID_ATTRIBUTE + " not found, can not download");
		}
		ServletRequestAttributes previousRequestAttributes = null;
		try {
			previousRequestAttributes = ContextUtils.bindRequestAndContext(
					request, getServletContext());
			BeanUtils.initBeanFactory(getServletContext());
			String contentType = (String) DownloadUtils.getDownloadAttribute(
					CONTENT_TYPE_ATTRIBUTE + id);
			if (contentType != null) {
				response.setContentType(contentType);
			}
			String filename = (String) DownloadUtils.getDownloadAttribute(
					FILENAME_ATTRIBUTE + id);
			String disposition = (String) ContextUtils.getGlobalSessionAttribute(
					CONTENT_DISPOSITION_ATTRIBUTE);

			if (filename != null) {
				if (disposition != null) {
					response.setHeader(
							"Content-disposition", disposition 
							+ "; filename=\"" + filename + "\"");
				} else {
					response.setHeader(
							"Content-disposition", "inline; filename=\"" 
							+ filename + "\"");
				}
			}
			byte [] data = (byte []) DownloadUtils.getDownloadAttribute(
					DATA_ATTRIBUTE + id);
			if (data == null) {
				throw new DownloadException("data is null, can not download");
			}
			response.setContentLength(data.length);
			response.setCharacterEncoding(encoding);
			ServletOutputStream out = response.getOutputStream();
			try {
				out.write(data);
			} catch (SocketException e) {
				logger.warn(
						"SocketException was raides while downloading, " 
						+ "probably because the client cancelled");
			}
		} catch (Throwable t) {
			Exception de = new DownloadException(t);
			ExceptionUtils.catchException(de);
			ContextUtils.unbindRequest(previousRequestAttributes);
			throw new ServletException(de);
		}
	}

}
