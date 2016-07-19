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
import java.util.Enumeration;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jsfcomp.chartcreator.model.ChartData;
import net.sf.jsfcomp.chartcreator.utils.ChartUtils;

import org.esupportail.commons.services.application.VersionException;
import org.esupportail.commons.services.database.DatabaseUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionService;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.web.deepLinking.DeepLinkingRedirector;
import org.hibernate.JDBCException;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Cagatay Civici (latest modification by $Author: cagatay_civici $)
 * @version $Revision: 745 $ $Date: 2007-05-08 10:16:19 +0300 (Tue, 08 May 2007) $
 * 
 * Servlet generating the chart image
 */
public class Chartlet extends HttpServlet{

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3095314543806459757L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("chartId");
		try {
		ChartData chartData = (ChartData) request.getSession().getAttribute(id);
		JFreeChart chart = ChartUtils.createChartWithType(chartData);
		ChartUtils.setGeneralChartProperties(chart, chartData);

//		try {
			writeChart(response, chart, chartData);
		} catch (Throwable t) {
//			e.printStackTrace();
			ExceptionUtils.catchException(t);
		} finally {
			emptySession(request,id);
		}
	}
	
	private void writeChart(HttpServletResponse response, JFreeChart chart, ChartData chartData) throws IOException{
		OutputStream stream = response.getOutputStream();
		response.setContentType(ChartUtils.resolveContentType(chartData.getOutput()));
		
		if(chartData.getOutput().equalsIgnoreCase("png"))
			ChartUtilities.writeChartAsPNG(stream, chart, chartData.getWidth(), chartData.getHeight());
		else if (chartData.getOutput().equalsIgnoreCase("jpeg"))
			ChartUtilities.writeChartAsJPEG(stream, chart, chartData.getWidth(), chartData.getHeight());
		
		stream.flush();
		stream.close();
	}
	
	private void emptySession(HttpServletRequest request, String id) {
		request.getSession().removeAttribute(id);
	}
}
