/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.tagext.Tag;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.util.Assert;

/**
 * Utility class for tags.
 */
public class TagUtils {
	
	/**
	 * A logger.
	 */
	private static final Logger LOG = new LoggerImpl(TagUtils.class);

	/**
	 * Bean constructor.
	 */
	private TagUtils() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This method can be used to 'decode' the attributes of tags 
	 * ('#{xxx} are replaced by their real value).
	 * @param attrValue
	 * @return the 'real' value.
	 */
	public static Object getAttributeValue(
			final String attrValue) {
		if (attrValue == null) {
			return null;
		}
		if (!UIComponentTag.isValueReference(attrValue)) {
			return attrValue;
		}
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Assert.notNull(facesContext, "facesContext should not be null");
        ValueBinding vb = facesContext.getApplication().createValueBinding(attrValue);
        return vb.getValue(facesContext);
	}

	/**
	 * @param attrName
	 * @param attrValue
	 * @return the 'real' boolean value of a tag attribute.
	 */
	public static Boolean getBooleanAttributeValue(
			final String attrName,
			final String attrValue) {
		Object value = getAttributeValue(attrValue);
		if (value == null) {
			return null;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof String) {
			return Boolean.valueOf((String) value);
		}
		throw new TagException("attribute '" + attrName + "' is not a boolean ([" + value + "])");
	}

	/**
	 * @param attrName
	 * @param attrValue
	 * @return the 'real' string value of a tag attribute.
	 */
	public static String getStringAttributeValue(
			final String attrName,
			final String attrValue) {
		Object value = getAttributeValue(attrValue);
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}
		LOG.error("attribute '" + attrName + "' is not a string");
		return value.toString();
	}
	
	/**
	 * Recursive method that manage the opening and closure components.
	 * Ex. : <li><ul><button/></ul></li>
	 * Stop condition: cNew hasn't more children --> close Tag
	 * @param cNew
	 * @param context
	 * @throws IOException
	 */
	public static void recursiveEncodechildren(
			final UIComponentBase cNew, 
			final FacesContext context) throws IOException {
		if (cNew.getChildren() != null && !cNew.getChildren().isEmpty()) {
			for (Object o : cNew.getChildren()) {
				UIComponentBase c2 = (UIComponentBase) o;
				c2.encodeBegin(context);
				recursiveEncodechildren(c2, context);
			}
			//we travelled all children must therefore close the parent
			cNew.encodeEnd(context);
		} else {
			cNew.encodeEnd(context);
		}
	}
	
	/**
	 * Make an EL Expression.
	 * EX. : #{param}
	 * @param param 
	 * @return String
	 */
	public static String makeELExpression(final String param) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("entering makeELExpression(" + param + " )");
		}
		return "#{" + param + "}";
	}
	
	/**
	 * Recursive method that returns the StringVar attribute of the upper PageTag.
	 * Stop condition: cNew is a PageTage or cNew.getParent == null.
	 * @param cNew
	 * @return String
	 */
	public static String getStringsVarInPageTag(
			final Tag cNew) {
		if (cNew instanceof PageTag) {
			PageTag page = (PageTag) cNew;
			return page.getStringsVar();
		} else if (cNew.getParent() == null) {
			return null;
		}
		return getStringsVarInPageTag(cNew.getParent());
	}

}
