package jp.sf.pal.tomahawk.multipart;

import javax.portlet.ActionRequest;

/**
 * A utility class to retrieve the original ActionRequest of a MultipartPortletRequestWrapper.
 *
 */
public abstract class MultipartUtils {
	
	/**
	 * Constructor.
	 */
	private MultipartUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param wrapper 
	 * @return the original ActionRequest of a MultipartPortletRequestWrapper.
	 */
	public static ActionRequest getActionRequest(final MultipartPortletRequestWrapper wrapper) {
		return wrapper.request;
	}

}
