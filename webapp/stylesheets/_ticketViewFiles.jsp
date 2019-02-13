<%@include file="_include.jsp"%>
<e:panelGrid columns="3" columnClasses="colLeftNowrap,colRightNowrapMid,colRightMaxNowrap">
	<e:subSection value="#{msgs['TICKET_VIEW.FILES.HEADER']}" >
		<f:param value="#{ticketController.fileInfoEntriesNumber}" />
	</e:subSection>
	<h:panelGroup style="cursor: pointer" onclick="javascript:{showHideElement('viewTicketForm:files');showHideElement('viewTicketForm:showFiles');showHideElement('viewTicketForm:hideFiles');if (isIe7()) showHideElement('viewTicketForm:ie7icon');return false;}" >
		<h:panelGroup id="showFiles" >
			<t:graphicImage value="/media/images/show.png" 
				alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
		</h:panelGroup>
		<h:panelGroup id="hideFiles" style="display: none" >
			<t:graphicImage value="/media/images/hide.png"
				alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
		</h:panelGroup>
	</h:panelGroup>
	<h:panelGroup id="ie7icon" style="display: none; cursor: pointer" onclick="javascript:{alert('#{msgs['_.IE7_NOTE']}');}" >
		<t:graphicImage value="/media/images/ie7.png" />
		<e:bold value="#{msgs['_.IE7']}" />
	</h:panelGroup>
</e:panelGrid>
<h:panelGroup 
	id="files" 
	style="display: none" >
	<h:panelGroup rendered="#{ticketController.userCanGiveInformation}" >
		<h:panelGroup id="uploadedFile" >
			<e:inputFileUpload value="#{ticketController.uploadedFile}" storage="memory" />
			<t:htmlTag value="br" />
		</h:panelGroup>
		<h:panelGroup rendered="#{ticketController.actionScopeItems != null}" >
			<e:text value="#{msgs['TICKET_ACTION.TEXT.SCOPE_PROMPT']} " />
			<e:selectOneMenu id="scope" 
				value="#{ticketController.actionScope}" >
				<f:selectItems value="#{ticketController.actionScopeItems}" />
			</e:selectOneMenu>
			<t:htmlTag value="br" />
		</h:panelGroup>
		<h:panelGroup rendered="#{ticketController.userCanSetNoAlert}" >
			<e:selectBooleanCheckbox id="uploadNoAlert"
				value="#{ticketController.uploadNoAlert}" />
			<e:text value=" #{msgs['TICKET_ACTION.TEXT.NO_ALERT']} " />
		</h:panelGroup>
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:uploadButton');" >
			<e:bold value=" #{msgs['TICKET_VIEW.FILES.BUTTON.UPLOAD']} " />
			<t:graphicImage value="/media/images/join.png" />
		</h:panelGroup>
		<e:commandButton style="display: none"
			value="#{msgs['TICKET_VIEW.FILES.BUTTON.UPLOAD']}" 
			id="uploadButton"
			action="#{ticketController.doUpload}" 
			/>
		<t:htmlTag value="br" />
	</h:panelGroup >
	<e:paragraph value="#{msgs['TICKET_VIEW.FILES.NO_FILE']}"
		rendered="#{empty ticketController.fileInfoEntries}" />
	<e:dataTable width="100%" columnClasses="colCenter,colLeftMaxNowrap,colRight" 
		id="fileData" rowIndexVar="variable" 
		value="#{ticketController.fileInfoEntries}"
		var="fie" border="0" cellspacing="0" cellpadding="0"
		rendered="#{not empty ticketController.fileInfoEntries}" >
		<f:facet name="header">
			<t:htmlTag value="hr" />
		</f:facet>
		<f:facet name="footer">
			<t:htmlTag value="hr" />
		</f:facet>
		<t:column>
			<t:graphicImage value="/media/images/public.png" style="#{fie.canChangeScope ? 'cursor: pointer' : ''}" rendered="#{fie.fileInfo.scope == 'DEFAULT'}" onclick="javascript:{showHideElement('viewTicketForm:fileData:'+#{variable}+':editScope');}" />
			<t:graphicImage value="/media/images/invited.png" style="#{fie.canChangeScope ? 'cursor: pointer' : ''}" rendered="#{fie.fileInfo.scope == 'INVITED'}" onclick="javascript:{showHideElement('viewTicketForm:fileData:'+#{variable}+':editScope');}" />
			<t:graphicImage value="/media/images/protected.png" style="#{fie.canChangeScope ? 'cursor: pointer' : ''}" rendered="#{fie.fileInfo.scope == 'OWNER'}" onclick="javascript:{showHideElement('viewTicketForm:fileData:'+#{variable}+':editScope');}" />
			<t:graphicImage value="/media/images/private.png" style="#{fie.canChangeScope ? 'cursor: pointer' : ''}" rendered="#{fie.fileInfo.scope == 'MANAGER'}" onclick="javascript:{showHideElement('viewTicketForm:fileData:'+#{variable}+':editScope');}" />
		</t:column>
		<t:column>
			<h:panelGroup id="editScope" rendered="#{fie.canChangeScope}" style="display: none" >
				<e:selectOneMenu id="fileInfoScope"
					value="#{fie.newScope}" 
					onchange="javascript:{simulateLinkClick('viewTicketForm:fileData:'+#{variable}+':changeFileInfoScopeButton');return false;}"
					>
					<f:selectItems value="#{ticketController.actionScopeItems}" />
				</e:selectOneMenu>
				<e:commandButton 
					id="changeFileInfoScopeButton" style="display: none"
					value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_SCOPE']}"
					action="#{ticketController.doUpdateFileInfoScope}" >
					<t:updateActionListener value="#{fie.newScope}" property="#{ticketController.fileInfoScopeToSet}" />
					<t:updateActionListener value="#{fie.fileInfo}" property="#{ticketController.fileInfoToUpdate}" />
				</e:commandButton>
				<t:htmlTag value="br" />
			</h:panelGroup>
			<h:panelGroup rendered="#{fie.canView}" >
				<h:panelGroup style="cursor: pointer"  
					onclick="simulateLinkClick('viewTicketForm:fileData:#{variable}:downloadButton');" >
					<e:text value="#{fie.fileInfo.filename} " />
					<t:graphicImage value="/media/images/save.png" 
						alt="#{msgs['TICKET_VIEW.FILES.BUTTON.DOWNLOAD']} " />
				</h:panelGroup>
				<e:commandButton style="display: none"
					value="#{msgs['TICKET_VIEW.FILES.BUTTON.DOWNLOAD']}" 
					id="downloadButton"
					action="#{ticketController.download}" 
					immediate="true" >
					<t:updateActionListener value="#{fie.fileInfo}"
						property="#{ticketController.fileInfoToDownload}" />
				</e:commandButton>
			</h:panelGroup>
			<e:bold value="#{fie.fileInfo.filename}" rendered="#{not fie.canView}" />
			<h:panelGroup id="info" style="display: none" >
				<e:text value="#{msgs['TICKET_VIEW.FILES.DATE']}" >
					<f:param value="#{fie.fileInfo.date}" />
				</e:text>
				<t:htmlTag value="br" />
				<e:text value="#{msgs['TICKET_VIEW.FILES.SIZE']}" >
					<f:param value="#{fileSizeI18nFormatter[fie.fileInfo.filesize]}" />
				</e:text>
				<t:htmlTag value="br" />
				<e:text 
					rendered="#{fie.fileInfo.user != null}" 
					value="#{msgs['TICKET_VIEW.FILES.USER']}" >
					<f:param value="#{userFormatter[fie.fileInfo.user]}" />
				</e:text>
				<e:text 
					rendered="#{fie.fileInfo.user == null}" 
					value="#{msgs['TICKET_VIEW.FILES.APPLICATION']}" />
			</h:panelGroup>
		</t:column>
		<t:column>
			<h:panelGroup style="cursor: pointer"  
				onclick="javascript:{showHideElement('viewTicketForm:fileData:#{variable}:info');showHideElement('viewTicketForm:fileData:#{variable}:showFileInfo');showHideElement('viewTicketForm:fileData:#{variable}:hideFileInfo');}" >
				<h:panelGroup id="showFileInfo" >
					<t:graphicImage value="/media/images/show.png" 
						alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
				</h:panelGroup>
				<h:panelGroup id="hideFileInfo" style="display: none" >
					<t:graphicImage value="/media/images/hide.png"
						alt="#{msgs['TICKET_VIEW.TEXT.SHOW']} " />
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup rendered="#{ticketController.userCanDeleteFileInfo}" >
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('viewTicketForm:fileData:#{variable}:deleteFileInfoButton');" >
					<t:graphicImage value="/media/images/delete.png" />
				</h:panelGroup>
				<e:commandButton 
					id="deleteFileInfoButton" style="display: none"
					value="x" action="#{ticketController.deleteFileInfo}" >
					<t:updateActionListener value="#{fie.fileInfo}" property="#{ticketController.fileInfoToDelete}" />
				</e:commandButton>
			</h:panelGroup>
		</t:column>
	</e:dataTable>				
</h:panelGroup>
	
	