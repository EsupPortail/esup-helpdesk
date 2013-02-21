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
package net.sf.jsfcomp.chartcreator.model;

import java.io.Serializable;

import net.sf.jsfcomp.chartcreator.component.UIChartAxis;

/**
 * @author Cagatay Civici (latest modification by $Author: cagatay_civici $)
 * @version $Revision: 745 $ $Date: 2007-05-08 10:16:19 +0300 (Tue, 08 May 2007) $
 * 
 * Represents a chart axis
 */
public class ChartAxisData implements Serializable{

	private Object datasource;
	private String label;
	private boolean domain;
	private String type;
	private String format;
	private String colors;
	private boolean verticalTickLabels;
	private float tickLabelFontSize;
	private boolean tickLabels;
	private boolean tickMarks;
	
	public ChartAxisData() {
		//NoOp
	}
	
	public ChartAxisData(UIChartAxis chartAxis) {
		this.datasource = chartAxis.getDatasource();
		this.label = chartAxis.getLabel();
		this.domain = chartAxis.getDomain();
		this.type = chartAxis.getType();
		this.format = chartAxis.getFormat();
		this.colors = chartAxis.getColors();
		this.verticalTickLabels = chartAxis.getVerticalTickLabels();
		this.tickLabelFontSize = chartAxis.getTickLabelFontSize();
		this.tickLabels = chartAxis.getTickLabels();
		this.tickMarks = chartAxis.getTickMarks();
	}
	
	public Object getDatasource() {
		return datasource;
	}
	public void setDatasource(Object datasource) {
		this.datasource = datasource;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isDomain() {
		return domain;
	}
	public void setDomain(boolean domain) {
		this.domain = domain;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getColors() {
		return colors;
	}
	public void setColors(String colors) {
		this.colors = colors;
	}
	public boolean isVerticalTickLabels() {
		return verticalTickLabels;
	}
	public void setVerticalTickLabels(boolean verticalTickLabels) {
		this.verticalTickLabels = verticalTickLabels;
	}
	public float getTickLabelFontSize() {
		return tickLabelFontSize;
	}
	public void setTickLabelFontSize(float tickLabelFontSize) {
		this.tickLabelFontSize = tickLabelFontSize;
	}
	public boolean isTickLabels() {
		return tickLabels;
	}
	public void setTickLabels(boolean tickLabels) {
		this.tickLabels = tickLabels;
	}
	public boolean isTickMarks() {
		return tickMarks;
	}
	public void setTickMarks(boolean tickMarks) {
		this.tickMarks = tickMarks;
	}
}
