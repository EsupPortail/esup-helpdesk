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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jsfcomp.chartcreator.component.UIChart;
import net.sf.jsfcomp.chartcreator.component.UIChartAxis;

/**
 * @author Cagatay Civici (latest modification by $Author: cagatay_civici $)
 * @version $Revision: 745 $ $Date: 2007-05-08 10:16:19 +0300 (Tue, 08 May 2007) $
 * 
 * Holds the chart information to be rendered, this class lives in session when the chart rendering begins 
 * and removes when the request is completed
 */
public class ChartData implements Serializable {

	private Object datasource;
	private int width;
	private int height;
	private int alpha;
	private int depth;
	private int startAngle;
	private String title;
	private String type;
	private String background;
	private String foreground;
	private String xlabel;
	private String ylabel;
	private String orientation;
	private String colors;
	private boolean chart3d;
	private boolean legend;
	private boolean antialias;
	private boolean outline;
	private String output;
	private List extensions;
	private float legendFontSize;
	private String generateMap;
	private boolean domainGridLines;
	private boolean rangeGridLines;
	private boolean legendBorder;
	private float lineStokeWidth;

	public ChartData() {
		//NoOp
	}

	public ChartData(UIChart chart) {
		this.datasource = chart.getDatasource();
		this.width = chart.getWidth();
		this.height = chart.getHeight();
		this.alpha = chart.getAlpha();
		this.depth = chart.getDepth();
		this.startAngle = chart.getStartAngle();
		this.title = chart.getTitle();
		this.type = chart.getType();
		this.background = chart.getBackground();
		this.foreground = chart.getForeground();
		this.xlabel = chart.getXlabel();
		this.ylabel = chart.getYlabel();
		this.orientation = chart.getOrientation();
		this.colors = chart.getColors();
		this.chart3d = chart.getIs3d();
		this.legend = chart.getLegend();
		this.antialias = chart.getAntialias();
		this.outline = chart.getOutline();
		this.output = chart.getOutput();
		this.legendFontSize = chart.getLegendFontSize();
		this.generateMap = chart.getGenerateMap();
		this.domainGridLines = chart.getDomainGridLines();
		this.rangeGridLines = chart.getRangeGridLines();
		this.legendBorder = chart.getLegendBorder();
		this.lineStokeWidth = chart.getLineStrokeWidth();
		
		createExtensions(chart);
	}
	
	//for now we only have chartaxis as extension
	private void createExtensions(UIChart chart) {
		extensions = new ArrayList();
		for (Iterator iterator = chart.getChildren().iterator(); iterator.hasNext();)
			extensions.add(new ChartAxisData((UIChartAxis) iterator.next()));
	}

	public int getAlpha() {
		return alpha;
	}
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public boolean isAntialias() {
		return antialias;
	}
	public void setAntialias(boolean antialias) {
		this.antialias = antialias;
	}

	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	
	public String getForeground() {
		return foreground;
	}
	public void setForeground(String foreground) {
		this.foreground = foreground;
	}

	public boolean isChart3d() {
		return chart3d;
	}
	public void setChart3d(boolean chart3d) {
		this.chart3d = chart3d;
	}

	public String getColors() {
		return colors;
	}
	public void setColors(String colors) {
		this.colors = colors;
	}

	public Object getDatasource() {
		return datasource;
	}
	public void setDatasource(Object datasource) {
		this.datasource = datasource;
	}

	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isLegend() {
		return legend;
	}
	public void setLegend(boolean legend) {
		this.legend = legend;
	}

	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public boolean isOutline() {
		return outline;
	}
	public void setOutline(boolean outline) {
		this.outline = outline;
	}

	public int getStartAngle() {
		return startAngle;
	}
	public void setStartAngle(int startAngle) {
		this.startAngle = startAngle;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public String getXlabel() {
		return xlabel;
	}
	public void setXlabel(String xlabel) {
		this.xlabel = xlabel;
	}

	public String getYlabel() {
		return ylabel;
	}
	public void setYlabel(String ylabel) {
		this.ylabel = ylabel;
	}

	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}

	public List getExtensions() {
		return extensions;
	}
	public void setExtensions(List extensions) {
		this.extensions = extensions;
	}

	public float getLegendFontSize() {
		return legendFontSize;
	}
	public void setLegendFontSize(float legendFontSize) {
		this.legendFontSize = legendFontSize;
	}

	public String getGenerateMap() {
		return generateMap;
	}
	public void setGenerateMap(String generateMap) {
		this.generateMap = generateMap;
	}

	public boolean isDomainGridLines() {
		return domainGridLines;
	}
	public void setDomainGridLines(boolean domainGridLines) {
		this.domainGridLines = domainGridLines;
	}

	public boolean isRangeGridLines() {
		return rangeGridLines;
	}
	public void setRangeGridLines(boolean rangeGridLines) {
		this.rangeGridLines = rangeGridLines;
	}
	
	public boolean isLegendBorder() {
		return legendBorder;
	}

	public void setLegendBorder(boolean legendBorder) {
		this.legendBorder = legendBorder;
	}
	
	public float getLineStokeWidth() {
		return lineStokeWidth;
	}

	public void setLineStokeWidth(float lineStokeWidth) {
		this.lineStokeWidth = lineStokeWidth;
	}
}