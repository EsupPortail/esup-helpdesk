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
package net.sf.jsfcomp.chartcreator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;

import net.sf.jsfcomp.chartcreator.model.ChartData;
import net.sf.jsfcomp.chartcreator.utils.ChartUtils;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * @author Cagatay Civici (latest modification by $Author: cagatay_civici $)
 * @version $Revision: 745 $ $Date: 2007-05-08 10:16:19 +0300 (Tue, 08 May 2007) $
 * 
 * PhaseListener generating the chart image
 */
public class ChartListener implements PhaseListener {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5471196494131856482L;

	public void afterPhase(PhaseEvent phaseEvent) {
		String chartId = (String)phaseEvent.getFacesContext().getExternalContext().getRequestParameterMap().get("chartId");
		if (chartId != null) {
			handleChartRequest(phaseEvent, chartId);
		}
	}
	
	private void handleChartRequest(PhaseEvent phaseEvent, String id) {
		FacesContext facesContext = phaseEvent.getFacesContext();
		ExternalContext externalContext = facesContext.getExternalContext();		
		Map sessionMap = externalContext.getSessionMap();

		ChartData chartData = (ChartData) sessionMap.get(id);
		JFreeChart chart = ChartUtils.createChartWithType(chartData);
		ChartUtils.setGeneralChartProperties(chart, chartData);

		try {
			if(externalContext.getResponse() instanceof HttpServletResponse)
				writeChartWithServletResponse((HttpServletResponse)externalContext.getResponse(),chart, chartData);
			else if(externalContext.getResponse() instanceof RenderResponse)
				writeChartWithPortletResponse((RenderResponse)externalContext.getResponse(), chart, chartData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emptySession(sessionMap, id);
			facesContext.responseComplete();
		}
	}

	public void beforePhase(PhaseEvent phaseEvent) {

	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
	
	private void writeChartWithServletResponse(HttpServletResponse response, JFreeChart chart, ChartData chartData) throws IOException{
		OutputStream stream = response.getOutputStream();
		response.setContentType(ChartUtils.resolveContentType(chartData.getOutput()));
		writeChart(stream, chart, chartData);
	}
	
	private void writeChartWithPortletResponse(RenderResponse response, JFreeChart chart, ChartData chartData) throws IOException{
		OutputStream stream = response.getPortletOutputStream();
		response.setContentType(ChartUtils.resolveContentType(chartData.getOutput()));
		writeChart(stream, chart, chartData);
	}
	
	private void writeChart(OutputStream stream, JFreeChart chart, ChartData chartData) throws IOException{
		if(chartData.getOutput().equalsIgnoreCase("png"))
			ChartUtilities.writeChartAsPNG(stream, chart, chartData.getWidth(), chartData.getHeight());
		else if (chartData.getOutput().equalsIgnoreCase("jpeg"))
			ChartUtilities.writeChartAsJPEG(stream, chart, chartData.getWidth(), chartData.getHeight());
		
		stream.flush();
		stream.close();
	}
	
	private void emptySession(Map sessionMap, String id) {
		sessionMap.remove(id);
	}
}