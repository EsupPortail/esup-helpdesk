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
package net.sf.jsfcomp.chartcreator.taglib;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import net.sf.jsfcomp.chartcreator.component.UIChartAxis;

/**
 * @author Cagatay Civici (latest modification by $Author: cagatay_civici $)
 * @version $Revision: 745 $ $Date: 2007-05-08 10:16:19 +0300 (Tue, 08 May 2007) $
 * 
 * Tag class for the chart axis
 */
public class ChartAxisTag extends UIComponentTag {
	
	private String datasource = null;
	private String label = null;
	private String domain = null;	
	private String type = null;
	private String format = null;
	private String is3d = null;
	private String legend = null;
	private String colors = null;
	private String alt = null;	
	private String onclick = null;
	private String ondblclick = null;
	private String onmousedown = null;
	private String onmouseup = null;
	private String onmouseover = null;
	private String onmousemove = null;
	private String onmouseout = null;
	private String onkeypress = null;
	private String onkeydown = null;
	private String onkeyup = null;
	private String output = null;
	private String usemap = null;
	private String verticalTickLabels = null;
	private String tickLabelFontSize = null;
	private String tickLabels = null;
	private String tickMarks = null;

	public void release() {
		super.release();
		datasource = null;
		domain = null;
		type = null;
		format = null;
		is3d = null;
		legend = null;
		colors = null;
		alt = null;
		onclick = null;
		ondblclick = null;
		onmousedown = null;
		onmouseup = null;
		onmouseover = null;
		onmousemove = null;
		onmouseout = null;
		onkeypress = null;
		onkeydown = null;
		onkeyup = null;
		output = null;
		usemap = null;
		verticalTickLabels = null;
		tickLabelFontSize = null;
		tickLabels = null;
		tickMarks = null;
	}

	protected void setProperties(UIComponent component) {
		super.setProperties(component);

		if (datasource != null) {
			if (isValueReference(datasource)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(datasource);
				component.setValueBinding("datasource", vb);
			} else {
				component.getAttributes().put("datasource", datasource);
			}
		}
		
		if (label != null) {
			if (isValueReference(label)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(label);
				component.setValueBinding("label", vb);
			} else {
				component.getAttributes().put("label", label);
			}
		}

		if (domain != null) {
			if (isValueReference(domain)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(domain);
				component.setValueBinding("domain", vb);
			} else {
				component.getAttributes().put("domain", Boolean.valueOf(domain));
			}
		}
		
		if (type != null) {
			if (isValueReference(type)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(type);
				component.setValueBinding("type", vb);
			} else {
				component.getAttributes().put("type", type);
			}
		}
		
		if (format != null) {
			if (isValueReference(format)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(format);
				component.setValueBinding("format", vb);
			} else {
				component.getAttributes().put("format", format);
			}
		}
		
		if (is3d != null) {
			if (isValueReference(is3d)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(is3d);
				component.setValueBinding("is3d", vb);
			} else {
				component.getAttributes().put("is3d", Boolean.valueOf(is3d));
			}
		}

		if (legend != null) {
			if (isValueReference(legend)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(legend);
				component.setValueBinding("legend", vb);
			} else {
				component.getAttributes().put("legend", Boolean.valueOf(legend));
			}
		}

		if (colors != null) {
			if (isValueReference(colors)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(colors);
				component.setValueBinding("colors", vb);
			} else {
				component.getAttributes().put("colors", colors);
			}
		}

		if (alt != null) {
			if (isValueReference(alt)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(alt);
				component.setValueBinding("alt", vb);
			} else {
				component.getAttributes().put("alt", alt);
			}
		}
		
		if (onclick != null) {
			if (isValueReference(onclick)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(onclick);
				component.setValueBinding("onclick", vb);
			} else {
				component.getAttributes().put("onclick", onclick);
			}
		}
		
		if (ondblclick != null) {
			if (isValueReference(ondblclick)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(ondblclick);
				component.setValueBinding("ondblclick", vb);
			} else {
				component.getAttributes().put("ondblclick", ondblclick);
			}
		}
		
		if (onmousedown != null) {
			if (isValueReference(onmousedown)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(onmousedown);
				component.setValueBinding("onmousedown", vb);
			} else {
				component.getAttributes().put("onmousedown", onmousedown);
			}
		}
		
		if (onmouseup != null) {
			if (isValueReference(onmouseup)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(onmouseup);
				component.setValueBinding("onmouseup", vb);
			} else {
				component.getAttributes().put("onmouseup", onmouseup);
			}
		}
		
		if (onmouseover != null) {
			if (isValueReference(onmouseover)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(onmouseover);
				component.setValueBinding("onmouseover", vb);
			} else {
				component.getAttributes().put("onmouseover", onmouseover);
			}
		}
		
		if (onmousemove != null) {
			if (isValueReference(onmousemove)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(onmousemove);
				component.setValueBinding("onmousemove", vb);
			} else {
				component.getAttributes().put("onmousemove", onmousemove);
			}
		}
		
		if (onmouseout != null) {
			if (isValueReference(onmouseout)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(onmouseout);
				component.setValueBinding("onmouseout", vb);
			} else {
				component.getAttributes().put("onmouseout", onmouseout);
			}
		}
		
		if (onkeypress != null) {
			if (isValueReference(onkeypress)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(onkeypress);
				component.setValueBinding("onkeypress", vb);
			} else {
				component.getAttributes().put("onkeypress", onkeypress);
			}
		}
		
		if (onkeydown != null) {
			if (isValueReference(onkeydown)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(onkeydown);
				component.setValueBinding("onkeydown", vb);
			} else {
				component.getAttributes().put("onkeydown", onkeydown);
			}
		}
		
		if (onkeyup != null) {
			if (isValueReference(onkeyup)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(onkeyup);
				component.setValueBinding("onkeyup", vb);
			} else {
				component.getAttributes().put("onkeyup", onkeyup);
			}
		}
		
		if (output != null) {
			if (isValueReference(output)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(output);
				component.setValueBinding("output", vb);
			} else {
				component.getAttributes().put("output", output);
			}
		}
		
		if (usemap != null) {
			if (isValueReference(usemap)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(usemap);
				component.setValueBinding("usemap", vb);
			} else {
				component.getAttributes().put("usemap", usemap);
			}
		}
		
		if (verticalTickLabels != null) {
			if (isValueReference(verticalTickLabels)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(verticalTickLabels);
				component.setValueBinding("verticalTickLabels", vb);
			} else {
				component.getAttributes().put("verticalTickLabels", Boolean.valueOf(verticalTickLabels));
			}
		}	
		
		if (tickLabelFontSize != null) {
			if (isValueReference(tickLabelFontSize)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(tickLabelFontSize);
				component.setValueBinding("tickLabelFontSize", vb);
			} else {
				component.getAttributes().put("tickLabelFontSize", Float.valueOf(tickLabelFontSize));
			}
		}		

		if (tickLabels != null) {
			if (isValueReference(tickLabels)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(tickLabels);
				component.setValueBinding("tickLabels", vb);
			} else {
				component.getAttributes().put("tickLabels", Boolean.valueOf(tickLabels));
			}
		}
		
		if (tickMarks != null) {
			if (isValueReference(tickMarks)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(tickMarks);
				component.setValueBinding("tickMarks", vb);
			} else {
				component.getAttributes().put("tickMarks", Boolean.valueOf(tickMarks));
			}
		}		
	}

	public String getComponentType() {
		return UIChartAxis.COMPONENT_TYPE;
	}

	public String getRendererType() {
		return null;
	}

	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	
	public String getColors() {
		return colors;
	}
	public void setColors(String colors) {
		this.colors = colors;
	}

	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}

	public String getIs3d() {
		return is3d;
	}
	public void setIs3d(String is3d) {
		this.is3d = is3d;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}

	public String getOnclick() {
		return onclick;
	}
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getOndblclick() {
		return ondblclick;
	}
	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	public String getOnkeydown() {
		return onkeydown;
	}
	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}

	public String getOnkeypress() {
		return onkeypress;
	}
	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}

	public String getOnkeyup() {
		return onkeyup;
	}
	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}

	public String getOnmousedown() {
		return onmousedown;
	}
	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}

	public String getOnmousemove() {
		return onmousemove;
	}
	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}

	public String getOnmouseout() {
		return onmouseout;
	}
	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}

	public String getOnmouseover() {
		return onmouseover;
	}
	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}

	public String getOnmouseup() {
		return onmouseup;
	}
	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}

	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}

	public String getTickLabelFontSize() {
		return tickLabelFontSize;
	}
	public void setTickLabelFontSize(String tickLabelFontSize) {
		this.tickLabelFontSize = tickLabelFontSize;
	}

	public String getTickLabels() {
		return tickLabels;
	}
	public void setTickLabels(String tickLabels) {
		this.tickLabels = tickLabels;
	}

	public String getTickMarks() {
		return tickMarks;
	}
	public void setTickMarks(String tickMarks) {
		this.tickMarks = tickMarks;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getUsemap() {
		return usemap;
	}
	public void setUsemap(String usemap) {
		this.usemap = usemap;
	}

	public String getVerticalTickLabels() {
		return verticalTickLabels;
	}
	public void setVerticalTickLabels(String verticalTickLabels) {
		this.verticalTickLabels = verticalTickLabels;
	}
}