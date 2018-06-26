<%@include file="_include.jsp"%>

<h:panelGroup id="files" >
	<e:paragraph value="#{msgs['TICKET_VIEW.FILES.NO_FILE']}" rendered="#{empty archivedTicketController.archivedFileInfoEntries}" />

	<e:dataTable width="100%" columnClasses="colCenter,colLeftMaxNowrap,colRight"
		id="fileData" rowIndexVar="variable" 
		value="#{archivedTicketController.archivedFileInfoEntries}"
		var="afie" border="0" cellspacing="0" cellpadding="0"
		rendered="#{not empty archivedTicketController.archivedFileInfoEntries}" >
		<f:facet name="header">
			<t:htmlTag value="hr" />
		</f:facet>
		<f:facet name="footer">
			<t:htmlTag value="hr" />
		</f:facet>
		<t:column>
			<t:graphicImage value="/media/images/public.png" rendered="#{afie.archivedFileInfo.effectiveScope == 'DEFAULT'}" />
			<t:graphicImage value="/media/images/protected.png" rendered="#{afie.archivedFileInfo.effectiveScope == 'OWNER'}" />
			<t:graphicImage value="/media/images/private.png" rendered="#{afie.archivedFileInfo.effectiveScope == 'MANAGER'}" />
		</t:column>
		<t:column>
			<h:panelGroup rendered="#{afie.canView}" >
				<h:panelGroup style="cursor: pointer"  
					onclick="simulateLinkClick('viewArchivedTicketForm:fileData:#{variable}:downloadButton');" >
					<e:text value="#{afie.archivedFileInfo.filename} " />
					<t:graphicImage value="/media/images/save.png" 
						alt="#{msgs['TICKET_VIEW.FILES.BUTTON.DOWNLOAD']} " />
				</h:panelGroup>
				<e:commandButton 
					value="#{msgs['TICKET_VIEW.FILES.BUTTON.DOWNLOAD']}" 
					id="downloadButton"
					action="#{archivedTicketController.download}" 
					immediate="true" >
					<t:updateActionListener value="#{afie.archivedFileInfo}"
						property="#{archivedTicketController.archivedFileInfoToDownload}" />
				</e:commandButton>
			</h:panelGroup>
			<e:bold value="#{afie.archivedFileInfo.filename}" rendered="#{not afie.canView}" />
			<h:panelGroup id="info" style="display: none" >
				<e:text value="#{msgs['TICKET_VIEW.FILES.DATE']}" >
					<f:param value="#{afie.archivedFileInfo.date}" />
				</e:text>
				<t:htmlTag value="br" />
				<e:text value="#{msgs['TICKET_VIEW.FILES.SIZE']}" >
					<f:param value="#{fileSizeI18nFormatter[afie.archivedFileInfo.filesize]}" />
				</e:text>
				<t:htmlTag value="br" />
				<e:text 
					rendered="#{afie.archivedFileInfo.user != null}" 
					value="#{msgs['TICKET_VIEW.FILES.USER']}" >
					<f:param value="#{userFormatter[afie.archivedFileInfo.user]}" />
				</e:text>
				<e:text 
					rendered="#{afie.archivedFileInfo.user == null}" 
					value="#{msgs['TICKET_VIEW.FILES.APPLICATION']}" />
			</h:panelGroup>
		</t:column>
		<t:column>
			<h:panelGroup style="cursor: pointer"  
				onclick="javascript:{showHideElement('viewArchivedTicketForm:fileData:#{variable}:info');showHideElement('viewArchivedTicketForm:fileData:#{variable}:showFileInfo');showHideElement('viewArchivedTicketForm:fileData:#{variable}:hideFileInfo');}" >
				<h:panelGroup id="showFileInfo" >
					<t:graphicImage value="/media/images/show.png" 
						alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
				</h:panelGroup>
				<h:panelGroup id="hideFileInfo" style="display: none" >
					<t:graphicImage value="/media/images/hide.png"
						alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
				</h:panelGroup>
			</h:panelGroup>
		</t:column>
	</e:dataTable>				
</h:panelGroup>
	