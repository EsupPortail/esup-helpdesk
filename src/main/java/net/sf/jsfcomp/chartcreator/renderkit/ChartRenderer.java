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
package net.sf.jsfcomp.chartcreator.renderkit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.esupportail.commons.utils.ContextUtils;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryLabelEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator;

import net.sf.jsfcomp.chartcreator.component.UIChart;
import net.sf.jsfcomp.chartcreator.model.ChartData;
import net.sf.jsfcomp.chartcreator.utils.ChartConstants;
import net.sf.jsfcomp.chartcreator.utils.ChartUtils;

/**
 * @author Cagatay Civici (latest modification by $Author: cagatay_civici $)
 * @version $Revision: 745 $ $Date: 2007-05-08 10:16:19 +0300 (Tue, 08 May 2007) $
 * 
 * Renders the img tag and refers to an image that is generated at serverside
 */
public class ChartRenderer extends Renderer {

	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		UIChart chart = (UIChart) component;
		setChartDataAtSession(context, chart);

		String clientId = chart.getClientId(context);

		writer.startElement("img", chart);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("width", String.valueOf(chart.getWidth()), null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("height", String.valueOf(chart.getHeight()),null);
		
		if(ChartUtils.useServlet(context))
			writer.writeAttribute("src", ChartConstants.CHART_REQUEST + ".chart?ts="+ System.currentTimeMillis() + "&chartId=" + clientId, null);
		else {
			String viewId = context.getViewRoot().getViewId();
			String actionURL = context.getApplication().getViewHandler().getActionURL(context, viewId);
//			writer.writeAttribute("src", actionURL + "?ts="+ System.currentTimeMillis() + "&chartId=" + clientId, null);
			writer.writeAttribute("src", context.getExternalContext().getRequestContextPath() + "/dummy.chart?ts="+ System.currentTimeMillis() + "&chartId=" + clientId, null);
		}
		
		ChartUtils.renderPassThruImgAttributes(writer, chart);
	}

	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		UIChart chart = (UIChart) component;
		
		writer.endElement("img");

		if (chart.getGenerateMap() != null)
			writeImageMap(context, chart);
	}

	// creates and puts the chart data to session for this chart object
	private void setChartDataAtSession(FacesContext facesContext, UIChart chart) {
		Map session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String clientId = chart.getClientId(facesContext);
		ChartData data = new ChartData(chart);
//		session.put(clientId, data);
		ContextUtils.setGlobalSessionAttribute(clientId, data);
	}

	private void renderImageMapSupport(FacesContext context,UIChart uichart,ChartRenderingInfo chartRenderingInfo) {
		ResponseWriter writer = context.getResponseWriter();
		
		try {
			Iterator entities = chartRenderingInfo.getEntityCollection().iterator();
			while (entities.hasNext()) {
				ChartEntity entity = (ChartEntity) entities.next();
				if (entity instanceof CategoryLabelEntity) {
					CategoryLabelEntity categoryEntity = (CategoryLabelEntity) entity;
					if (categoryEntity.getKey() != null) {
						categoryEntity.setToolTipText(categoryEntity.getKey().toString());
						categoryEntity.setURLText("?category="+ categoryEntity.getKey().toString());
					}
				}
			}

			writer.write("<script>");
			writer.write("function chart" + uichart.getId() + "Click(data) {");
			
			if (uichart.getOngeneratedimagemapclick() != null)
				writer.write("  " + uichart.getOngeneratedimagemapclick() + "(data);");
			
			writer.write("}");
			writer.write("</script>");
		} catch (IOException error) {
			error.printStackTrace();
		}
	}

	private void writeImageMap(FacesContext context, UIChart uichart) {
		ResponseWriter writer = context.getResponseWriter();
		ExternalContext externalContext = context.getExternalContext();
		Map sessionMap = externalContext.getSessionMap();
		String clientId = uichart.getClientId(context);
		ChartData data = (ChartData) sessionMap.get(clientId);
		JFreeChart chart = ChartUtils.createChartWithType(data);
		ChartUtils.setGeneralChartProperties(chart, data);

		ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (data.getOutput().equalsIgnoreCase("png"))
				ChartUtilities.writeChartAsPNG(out, chart, data.getWidth(), data.getHeight(), chartRenderingInfo);
			else if (data.getOutput().equalsIgnoreCase("jpeg"))
				ChartUtilities.writeChartAsJPEG(out, chart, data.getWidth(), data.getHeight(), chartRenderingInfo);

			renderImageMapSupport(context, uichart, chartRenderingInfo);

			writer.write(ChartUtilities.getImageMap(uichart.getGenerateMap(), chartRenderingInfo, new StandardToolTipTagFragmentGenerator(), new URLTagFragmentGenerator(uichart.getId())));
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
}

class ToolTipTagFragmentGenerator implements org.jfree.chart.imagemap.ToolTipTagFragmentGenerator {

	public String generateToolTipFragment(String toolTipText) {
		return " title=\"" + toolTipText + "\" alt=\"" + toolTipText + "\"";
	}
}

class URLTagFragmentGenerator implements org.jfree.chart.imagemap.URLTagFragmentGenerator {

	private String chartId = null;
	
	public URLTagFragmentGenerator(String chartId) {
		this.chartId = chartId;
	}
	
	public String generateURLFragment(String urlText) {
		return " href=\"#\" onclick=\"chart" + chartId + "Click('" + urlText + "')\"";
	}
}