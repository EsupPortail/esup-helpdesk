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
package net.sf.jsfcomp.chartcreator.utils;

import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import net.sf.jsfcomp.chartcreator.model.ChartAxisData;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;

/**
 * @author Rogerio Araujo (latest modification by $Author: cagatay_civici $)
 * @version $Revision: 745 $ $Date: 2007-05-08 10:16:19 +0300 (Tue, 08 May 2007) $
 */
public class ChartAxisUtils {

	//Generic axis creation methods
	public static ValueAxis createDateAxis(JFreeChart chart, ChartAxisData chartAxisData)
	{
		DateAxis dateAxis = new DateAxis(chartAxisData.getLabel());
		if(chartAxisData.getFormat() != null)
			dateAxis.setDateFormatOverride(new SimpleDateFormat(chartAxisData.getFormat()));
		
		dateAxis.setTickUnit(DateAxis.DEFAULT_DATE_TICK_UNIT);
		
		return dateAxis;
	}
	
	public static ValueAxis createNumberAxis(JFreeChart chart, ChartAxisData chartAxisData)
	{
		NumberAxis numberAxis = new NumberAxis(chartAxisData.getLabel());
		if(chartAxisData.getFormat() != null)
			numberAxis.setNumberFormatOverride(new DecimalFormat(chartAxisData.getFormat()));
		
		return numberAxis;
	}
	
	//XYSeries axis methods
	public static void createXYSeriesAxis(JFreeChart chart, ChartAxisData chartAxisData, int axisIndex)
	{
		ValueAxis axis = null;
		if(chartAxisData.getType() != null)
		{
			if(chartAxisData.getType().equals("number"))
				axis = createNumberAxis(chart, chartAxisData);
			else if(chartAxisData.getType().equals("date"))
				axis = createDateAxis(chart, chartAxisData);
			
			if(chartAxisData.getTickLabelFontSize() > 0) 
			{
				Font tickFont = CategoryAxis.DEFAULT_TICK_LABEL_FONT.deriveFont(chartAxisData.getTickLabelFontSize());
				axis.setTickLabelFont(tickFont);
			}
			
			axis.setTickLabelsVisible(chartAxisData.isTickLabels());
			axis.setTickMarksVisible(chartAxisData.isTickMarks());	
			axis.setVerticalTickLabels(chartAxisData.isVerticalTickLabels());
		}
		
		XYPlot plot = chart.getXYPlot();
		if(chartAxisData.isDomain())
		{
			plot.setDomainAxis(plot.getDomainAxisCount() - 1, axis);
		}
		else 
		{
			plot.setRangeAxis(axisIndex, axis);
			XYDataset dataset = (XYDataset) chartAxisData.getDatasource();
			plot.setRenderer(axisIndex, new StandardXYItemRenderer());
			plot.setDataset(axisIndex, dataset);
			plot.mapDatasetToRangeAxis(axisIndex, axisIndex);
		}
		
		setXYSeriesAxisColors(chartAxisData, plot.getRenderer(axisIndex));
	}
	
	public static void setXYSeriesAxisColors(ChartAxisData chartAxisData, XYItemRenderer axisRenderer)
	{
		if(chartAxisData.getColors() != null) {
			String[] colors = chartAxisData.getColors().split(",");
			for (int i = 0; i < colors.length; i++) {
				axisRenderer.setSeriesPaint(i, ChartUtils.getColor(colors[i].trim()));
			}
		}
	}
	
	//CategorySeries axis methods
	public static void createCategorySeriesAxis(JFreeChart chart, ChartAxisData chartAxisData, int axisIndex)
	{
		CategoryPlot plot = chart.getCategoryPlot();
		if(chartAxisData.isDomain())
		{
			CategoryAxis axis = new CategoryAxis(chartAxisData.getLabel());
			axis.setTickLabelsVisible(chartAxisData.isTickLabels());
			axis.setTickMarksVisible(chartAxisData.isTickMarks());	
			
			if(chartAxisData.getTickLabelFontSize() > 0) 
			{
				Font tickFont = CategoryAxis.DEFAULT_TICK_LABEL_FONT.deriveFont(chartAxisData.getTickLabelFontSize());
				axis.setTickLabelFont(tickFont);
			}
			if(chartAxisData.isVerticalTickLabels())
			{
				axis.setCategoryLabelPositions(
						CategoryLabelPositions.UP_90
				);
			}
			plot.setDomainAxis(plot.getDomainAxisCount() - 1, axis);
		}
		else {
			ValueAxis axis = createNumberAxis(chart, chartAxisData);
			axis.setTickLabelsVisible(chartAxisData.isTickLabels());
			axis.setTickMarksVisible(chartAxisData.isTickMarks());	
			
			if(chartAxisData.getTickLabelFontSize() > 0) 
			{
				Font tickFont = CategoryAxis.DEFAULT_TICK_LABEL_FONT.deriveFont(chartAxisData.getTickLabelFontSize());
				axis.setTickLabelFont(tickFont);
			}
			plot.setRangeAxis(axis);
		}
	}
	
}
