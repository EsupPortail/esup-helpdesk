/*
 * Copyright 2004 The Apache Software Foundation.
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
package net.sf.jsfcomp.chartcreator.component;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Cagatay Civici (latest modification by $Author: cagatay_civici $)
 * @version $Revision: 744 $ $Date: 2007-05-08 10:10:00 +0300 (Tue, 08 May 2007) $
 * 
 * Core component representing a chart axis
 */
public class UIChartAxis extends UIComponentBase {
	
	public static final String COMPONENT_TYPE = "net.sf.jsfcomp.chartcreator.UIChartAxis";
	public static final String COMPONENT_FAMILY = "net.sf.jsfcomp.chartcreator";
	
	private Object datasource;
	private String label;
	private Boolean domain;
	private String type;
	private String format;
	private String colors;
	private Boolean verticalTickLabels;
	private Float tickLabelFontSize;
	private Boolean tickLabels;
	private Boolean tickMarks;

	public UIChartAxis() {
		super();
		setRendererType(null);
	}

	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	/**
	 * Colors attributes for bar charts
	 */
	public String getColors() {
		if(colors != null)
			return colors;
		
		ValueBinding vb = getValueBinding("colors");
		String v = vb != null ? (String)vb.getValue(getFacesContext()) : null;
		return v != null ? v: null;
	}

	public void setColors(String colors) {
		this.colors = colors;
	}

	/**
	 * DataSource attribute
	 */
	public Object getDatasource() {
		if(datasource != null)
			return datasource;
		
		ValueBinding vb = getValueBinding("datasource");
		Object v = vb != null ? vb.getValue(getFacesContext()) : null;
		return v != null ? v: null;
	}

	public void setDatasource(Object datasource) {
		this.datasource = datasource;
	}

	/**
	 * Y-axis attribute
	 */
	public String getLabel() {
		if(label != null)
			return label;
		
		ValueBinding vb = getValueBinding("label");
		String v = vb != null ? (String)vb.getValue(getFacesContext()) : null;
		return v != null ? v: null;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean getDomain() {
		if(domain != null)
			return domain.booleanValue();
		
		ValueBinding vb = getValueBinding("domain");
		Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue(): false;
	}

	public void setDomain(boolean domain) {
		this.domain = Boolean.valueOf(domain);
	}

	public String getFormat() {
		if(format != null)
			return format;
		
		ValueBinding vb = getValueBinding("format");
		String v = vb != null ? (String)vb.getValue(getFacesContext()) : null;
		return v != null ? v: null;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getType() {
		if(type != null)
			return type;
		
		ValueBinding vb = getValueBinding("type");
		String v = vb != null ? (String)vb.getValue(getFacesContext()) : null;
		return v != null ? v: "number";
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean getVerticalTickLabels() {
		if(verticalTickLabels != null)
			return verticalTickLabels.booleanValue();
		
		ValueBinding vb = getValueBinding("verticalTickLabels");
		Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue(): false;
	}

	public void setVerticalTickLabels(boolean verticalTickLabels) {
		this.verticalTickLabels = Boolean.valueOf(verticalTickLabels);
	}

	/**
	 * @return the tickLabelFontSize
	 */
	public float getTickLabelFontSize() {
		if(tickLabelFontSize != null)
			return tickLabelFontSize.floatValue();
		
		ValueBinding vb = getValueBinding("tickLabelFontSize");
		Float v = vb != null ? (Float)vb.getValue(getFacesContext()) : null;
		return v != null ? v.floatValue(): 0;
	}

	/**
	 * @param tickLabelFontSize the tickLabelFontSize to set
	 */
	public void setTickLabelFontSize(float tickLabelFontSize) {
		this.tickLabelFontSize = new Float(tickLabelFontSize);
	}
	
	public boolean getTickLabels() {
		if(tickLabels != null)
			return tickLabels.booleanValue();
		
		ValueBinding vb = getValueBinding("tickLabels");
		Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue(): true;
	}

	public void setTickLabels(boolean tickLabels) {
		this.tickLabels = Boolean.valueOf(tickLabels);
	}

	public boolean getTickMarks() {
		if(tickMarks != null)
			return tickMarks.booleanValue();
		
		ValueBinding vb = getValueBinding("tickMarks");
		Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue(): true;
	}

	public void setTickMarks(boolean tickMarks) {
		this.tickMarks = Boolean.valueOf(tickMarks);
	}

	public Object saveState(FacesContext context) {
	    Object values[] = new Object[26];
	    values[0] = super.saveState(context);
	    values[1] = datasource;
	    values[2] = label;
	    values[3] = colors;
	    values[4] = domain;
	    values[5] = type;	    
	    values[6] = format;	    
	    values[7] = tickLabelFontSize;	    
	    values[8] = verticalTickLabels;	    
	    values[9] = tickLabels;	    
	    values[10] = tickMarks;	    
	    return values;
	  }

	  public void restoreState(FacesContext context, Object state) {
	    Object values[] = (Object[]) state;
	    super.restoreState(context, values[0]);
	    this.datasource = values [1];
	    this.label = (String) values[2];
	    this.colors = (String) values[3];
	    this.domain = (Boolean) values[4];	    
	    this.type = (String) values[5];
	    this.format = (String) values[6];
	    this.tickLabelFontSize = (Float) values[7];
	    this.verticalTickLabels = (Boolean) values[8];
	    this.tickLabels = (Boolean) values[9];
	    this.tickMarks = (Boolean) values[10];
	}
}


