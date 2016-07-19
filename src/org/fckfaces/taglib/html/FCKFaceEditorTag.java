package org.fckfaces.taglib.html;

import javax.faces.component.UIComponent;

import org.apache.myfaces.taglib.html.HtmlInputTextareaTag;
import org.fckfaces.util.Tags;

/**
 * 
 * @author srecinto
 *
 */
public class FCKFaceEditorTag extends HtmlInputTextareaTag {
	public static final String COMPONENT_TYPE = "org.fckfaces.Editor";
	public static final String RENDERER_TYPE = "org.fckfaces.EditorRenderer";
	
	private String toolbarSet;
	private String height;
	private String width;
	
	/**
	 * 
	 */
	public String getComponentType() { 
		return COMPONENT_TYPE; 
	}
	
	/**
	 * 
	 */
	public String getRendererType() { 
		return RENDERER_TYPE;
	}
	
	/**
	 * 
	 */
	protected void setProperties(UIComponent component) {
	    super.setProperties(component);
	    
	    Tags.setString(component, "toolbarSet", toolbarSet);
	    
	}
	
	/**
	 * 
	 */
	public void release() {
	    super.release();
	    toolbarSet = null;
	}

	public String getToolbarSet() {
		return toolbarSet;
	}

	public void setToolbarSet(String toolbarSet) {
		this.toolbarSet = toolbarSet;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
}
