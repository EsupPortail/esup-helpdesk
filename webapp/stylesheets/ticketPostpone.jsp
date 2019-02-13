<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanPostpone}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ticketActionForm" enctype="multipart/form-data" >
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['TICKET_ACTION.TITLE.POSTPONE']}" >
				<f:param value="#{ticketController.ticket.id}" />
			</e:section>
			<%@include file="_ticketActionCancel.jsp"%>
		</e:panelGrid>
		<e:messages />
		<h:panelGroup rendered="#{not ticketController.postponeDateSet}" >
			<e:subSection value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.CHOOSE']}" />
			<e:panelGrid columns="2" columnClasses="colLeft,colLeft" width="100%">
				<e:subSection value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.MANUAL_TITLE']}" />
				<e:subSection value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.AUTO_TITLE']}" />
				<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.MANUAL_HELP']}" />
				<h:panelGroup>
					<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.AUTO_HELP']}" />
					<t:inputCalendar 
						value="#{ticketController.postponeDate}" />
				</h:panelGroup>
				<h:panelGroup>
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:noDateNextButton');" >
						<e:bold value="#{msgs['_.BUTTON.NEXT']} " />
						<t:graphicImage value="/media/images/next.png" />
					</h:panelGroup>
					<e:commandButton 
						id="noDateNextButton" style="display: none"
						value="#{msgs['_.BUTTON.NEXT']}" 
						immediate="true" >
						<t:updateActionListener value="#{null}" property="#{ticketController.postponeDate}" />
						<t:updateActionListener value="true" property="#{ticketController.postponeDateSet}" />
					</e:commandButton>
				</h:panelGroup>
				<h:panelGroup>
					<h:panelGroup rendered="#{ticketController.postponeDate != null}" >
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:dateNextButton');" >
							<e:bold value="#{msgs['_.BUTTON.NEXT']}" >
								<f:convertDateTime timeZone="#{sessionController.timezone}" type="date" dateStyle="short" locale="#{sessionController.locale}"/>
							</e:bold>
							<t:graphicImage value="/media/images/next.png" />
						</h:panelGroup>
						<e:commandButton 
							id="dateNextButton" style="display: none"
							value="#{msgs['_.BUTTON.NEXT']}" 
							immediate="true" >
							<t:updateActionListener value="true" property="#{ticketController.postponeDateSet}" />
						</e:commandButton>
					</h:panelGroup>
				</h:panelGroup>
			</e:panelGrid>
		</h:panelGroup>
		<h:panelGroup rendered="#{ticketController.postponeDateSet}" >
			<e:text value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.DATE_PROMPT']} " />
			<h:panelGroup style="cursor: pointer" 
				onclick="simulateLinkClick('ticketActionForm:changePostponeDateButton');" >
				<e:bold 
					value="{0}" 
					rendered="#{ticketController.postponeDate != null}" >
					<f:param
						value="#{ticketController.postponeDate}" />
					<f:convertDateTime timeZone="#{sessionController.timezone}" dateStyle="short" type="date" locale="#{sessionController.locale}" />
				</e:bold>
				<e:bold 
					value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.DATE_NONE']}" 
					rendered="#{ticketController.postponeDate == null}" />
			</h:panelGroup>
			<e:italic value=" #{msgs['TICKET_ACTION.TEXT.POSTPONE.DATE_HELP']}" />
			<e:commandButton 
				id="changePostponeDateButton" style="display: none"
				value="#{msgs['TICKET_ACTION.BUTTON.CHANGE_POSTPONE_DATE']}">
				<t:updateActionListener value="false" property="#{ticketController.postponeDateSet}" />
			</e:commandButton>
			<e:panelGrid columns="3" width="100%" columnClasses="colLeftMax,colRightNowrap,colRightNowrap">
				<e:paragraph value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.MESSAGE']}" />
				<%@include file="_ticketActionResponses.jsp"%>
				<%@include file="_ticketActionUpload.jsp"%>
			</e:panelGrid>
			<fck:editor  
				id="actionMessage" 
				value="#{ticketController.actionMessage}" 
				toolbarSet="actionMessage" />
			<e:panelGrid columns="2" columnClasses="colLeftNowrap,colRightMaxNowrap" width="100%" >
				<h:panelGroup id="mainButtonGroup" style="position: absolute; white-space: nowrap;" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:actionButton');" >
						<e:bold value="#{msgs['TICKET_ACTION.BUTTON.POSTPONE']} " />
						<t:graphicImage value="/media/images/postpone.png" />
					</h:panelGroup>
					<e:commandButton 
						id="actionButton" style="display: none"
						value="#{msgs['TICKET_ACTION.BUTTON.POSTPONE']}"
						action="#{ticketController.doPostpone}" />
				</h:panelGroup>
				<h:panelGroup>
					<%@include file="_ticketActionSpentTime.jsp"%>
					<%@include file="_ticketActionScope.jsp"%>
               <h:panelGroup rendered="#{ticketController.userCanSetNoAlert}" >
                  <e:selectBooleanCheckbox id="noAlert"
                     value="#{ticketController.noAlert}" />
                  <e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
               </h:panelGroup>
               <%@include file="_ticketActionPreviewButton.jsp"%>
					<%@include file="_ticketActionPreviewButton.jsp"%>
				</h:panelGroup>
			</e:panelGrid>
			<%@include file="_ticketActionPreview.jsp"%>
			<%@include file="_ticketActionHistory.jsp"%>
		</h:panelGroup>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<%@include file="_ticketActionJavascript.jsp"%>
</e:page>

