<%@include file="_include.jsp"%>
<h:panelGroup>
	<h:panelGroup id="upload" >
		<h:panelGroup style="cursor: pointer" 
			onclick="showElement('ticketActionForm:uploadFile');hideElement('ticketActionForm:uploaded');" >
			<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.UPLOADED_FILES']} " >
				<f:param value="#{ticketController.tempUploadedFilesNumber}" />
			</e:bold>
			<t:graphicImage value="/media/images/join.png" />
		</h:panelGroup>
	</h:panelGroup>
	<e:panelGrid id="uploadFile" columns="2" style="display: none" columnClasses="colLeftNowrap,colLeftNowrap" >
		<e:inputFileUpload value="#{ticketController.uploadedFile}" storage="memory" />
		<h:panelGroup>
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:uploadButton');" >
				<e:bold value=" #{msgs['TICKET_ACTION.BUTTON.UPLOAD']} " />
				<t:graphicImage value="/media/images/add.png" alt="" />
			</h:panelGroup>
			<e:commandButton style="display: none"
				value="#{msgs['TICKET_ACTION.BUTTON.UPLOAD']}" 
				id="uploadButton"
				action="#{ticketController.storeTempUploadedFile}" >
				<f:param value="#{ticketController.tempUploadedFilesNumber}" />
			</e:commandButton>
		</h:panelGroup>
	</e:panelGrid>
	<e:dataTable columnClasses="colLeftNowrap,colRight" 
		id="tempUploadedFileData" rowIndexVar="index" 
		value="#{ticketController.tempUploadedFiles}"
		var="tempUploadedFile" border="0" cellspacing="0" cellpadding="0"
		rendered="#{not empty ticketController.tempUploadedFiles}" >
		<t:column>
			<e:text value="#{tempUploadedFile.name}" />
		</t:column>
		<t:column>
			<t:graphicImage value="/media/images/delete.png" 
				style="cursor: pointer" onclick="simulateLinkClick('ticketActionForm:tempUploadedFileData:#{index}:deleteUploadedFileButton');" />
			<e:commandButton style="display: none"
				value="#{msgs['_.BUTTON.DELETE']}" 
				id="deleteUploadedFileButton"
				action="#{ticketController.deleteTempUploadedFile}" >
				<t:updateActionListener value="#{index}" property="#{ticketController.tempUploadedFileToDeleteIndex}" />
			</e:commandButton>
		</t:column>
	</e:dataTable>
</h:panelGroup>
