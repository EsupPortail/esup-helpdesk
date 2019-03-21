<%@include file="_include.jsp"%>

<t:htmlTag value="div" styleClass="block">
    <t:htmlTag  value="div" styleClass="form-block ">
	    <e:paragraph value="#{msgs['TICKET_VIEW.FILES.NO_FILE']}" rendered="#{empty ticketController.fileInfoEntries}" />
        <e:dataTable
            id="fileData" rowIndexVar="variable"
            value="#{ticketController.fileInfoEntries}"
            var="fie" border="0" cellspacing="0" cellpadding="0"
            rendered="#{not empty ticketController.fileInfoEntries}" styleClass="files_array" rowClasses="oddRow,evenRow">
            <t:column  styleClass="column file-name">
            	<f:facet name="header">
                         <h:outputText value="#{msgs['TICKET_VIEW.FILES.FILE.LIB']}" />
                </f:facet>
                <t:htmlTag value="div" rendered="#{fie.canView}">
                    <h:panelGroup styleClass="cursor file-name link"
                        onclick="buttonClick('viewTicketForm:fileData:#{variable}:downloadButton');" >
                        <h:outputText  value="#{fie.fileInfo.filename} " />
                    </h:panelGroup>
                    <e:commandButton style="display: none"
                        value="#{msgs['TICKET_VIEW.FILES.BUTTON.DOWNLOAD']}"
                        id="downloadButton"
                        action="#{ticketController.download}"
                        immediate="true" >
                        <t:updateActionListener value="#{fie.fileInfo}"
                            property="#{ticketController.fileInfoToDownload}" />
                    </e:commandButton>
                </t:htmlTag>
                <t:htmlTag value="div" rendered="#{not fie.canView}">
                    <h:outputText  value="#{fie.fileInfo.filename} " />
                </t:htmlTag>
            </t:column>

            <t:column  styleClass="column file-info file-info--date">
                <f:facet name="header">
                             <h:outputText value="#{msgs['TICKET_VIEW.FILES.DATE.LIB']}"/>
                </f:facet>
                 <e:text value="#{msgs['TICKET_VIEW.FILES.DATE.VALUE']}" >
                       <f:param value="#{fie.fileInfo.date}" />
                 </e:text>
            </t:column>

            <t:column  styleClass="column file-info file-info--size">
                <f:facet name="header">
                             <h:outputText value="#{msgs['TICKET_VIEW.FILES.SIZE.LIB']}"/>
                </f:facet>
                 <e:text value="#{msgs['TICKET_VIEW.FILES.SIZE.VALUE']}" >
                       <f:param value="#{fie.fileInfo.filesize}" />
                 </e:text>
            </t:column>

            <t:column  styleClass="column file-info file-info--user">
                <f:facet name="header">
                             <h:outputText value="#{msgs['TICKET_VIEW.FILES.USER.LIB']}"/>
                </f:facet>
                 <e:text rendered="#{fie.fileInfo.user != null}" value="#{msgs['TICKET_VIEW.FILES.USER.VALUE']}" >
                      <f:param value="#{userFormatter[fie.fileInfo.user]}" />
                 </e:text>
                 <e:text rendered="#{fie.fileInfo.user == null}" value="#{msgs['TICKET_VIEW.FILES.APPLICATION']}" />
            </t:column>

            <t:column  styleClass="column file-scope">
            	<f:facet name="header">
                         <h:outputText value="#{msgs['DOMAIN.ACTION_SCOPE.COL.HEADER']}" />
                </f:facet>
                <t:htmlTag rendered="#{fie.fileInfo.scope == 'DEFAULT'}" value="div">
                    <h:panelGroup rendered="#{not fie.canChangeScope}">
                         <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED']}"/>
                    </h:panelGroup>
                    <h:panelGroup  styleClass="action-scope-edit default link" onclick="javascript:{showHideElement('viewTicketForm:fileData:'+#{variable}+':editScope');}" rendered="#{fie.canChangeScope}">
                        <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED']}"/>
                    </h:panelGroup>
                </t:htmlTag>

                <t:htmlTag  rendered="#{fie.fileInfo.scope == 'INVITED'}"  value="div">
                    <h:panelGroup rendered="#{not fie.canChangeScope}">
                         <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED']}"/>
                    </h:panelGroup>
                    <h:panelGroup  styleClass="action-scope-edit invited link" onclick="javascript:{showHideElement('viewTicketForm:fileData:'+#{variable}+':editScope');}" rendered="#{fie.canChangeScope}">
                        <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED']}"/>
                    </h:panelGroup>
                </t:htmlTag>

                <t:htmlTag rendered="#{fie.fileInfo.scope == 'INVITED_MANAGER'}"  value="div">
                    <h:panelGroup rendered="#{not fie.canChangeScope}">
                        <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED_MANAGER']}"/>
                    </h:panelGroup>
                    <h:panelGroup  styleClass="action-scope-edit invited-manager link" onclick="javascript:{showHideElement('viewTicketForm:fileData:'+#{variable}+':editScope');}" rendered="#{fie.canChangeScope}">
                        <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED_MANAGER']}"/>
                    </h:panelGroup>
                </t:htmlTag>

                <t:htmlTag rendered="#{fie.fileInfo.scope == 'OWNER'}"  value="div">
                    <h:panelGroup rendered="#{not fie.canChangeScope}">
                         <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.OWNER']}"/>
                    </h:panelGroup>
                    <h:panelGroup  styleClass="action-scope-edit owner link" onclick="javascript:{showHideElement('viewTicketForm:fileData:'+#{variable}+':editScope');}" rendered="#{fie.canChangeScope}">
                        <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.OWNER']}"/>
                    </h:panelGroup>
                </t:htmlTag>

                <t:htmlTag rendered="#{fie.fileInfo.scope == 'MANAGER'}"  value="div">
                    <h:panelGroup rendered="#{not fie.canChangeScope}">
                         <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.MANAGER']}"/>
                    </h:panelGroup>
                    <h:panelGroup  styleClass="action-scope-edit manager link" onclick="javascript:{showHideElement('viewTicketForm:fileData:'+#{variable}+':editScope');}" rendered="#{fie.canChangeScope}">
                        <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.MANAGER']}"/>
                    </h:panelGroup>
                </t:htmlTag>

                <h:panelGroup id="editScope" rendered="#{fie.canChangeScope}" style="display: none" styleClass="form-item">
                    <e:selectOneMenu id="fileInfoScope"
                        value="#{fie.newScope}"
                        onchange="buttonClick('viewTicketForm:fileData:'+#{variable}+':changeFileInfoScopeButton');return false;">
                        <f:selectItems value="#{ticketController.actionScopeItems}" />
                    </e:selectOneMenu>
                    <e:commandButton
                        id="changeFileInfoScopeButton" style="display: none"
                        value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_SCOPE']}"
                        action="#{ticketController.doUpdateFileInfoScope}" >
                        <t:updateActionListener value="#{fie.newScope}" property="#{ticketController.fileInfoScopeToSet}" />
                        <t:updateActionListener value="#{fie.fileInfo}" property="#{ticketController.fileInfoToUpdate}" />
                    </e:commandButton>

                </h:panelGroup>
            </t:column>

            <t:column styleClass="column" rendered="#{ticketController.userCanDeleteFileInfo}">
            	<f:facet name="header">
                         <h:outputText value="" />
                </f:facet>
                <t:htmlTag  value="div" styleClass="form-item"  rendered="#{ticketController.userCanDeleteFileInfo}">
                    <e:commandButton
                        id="deleteFileInfoButton"
                        value="#{msgs['TICKET_VIEW.FILES.DELETE.TEXT']}"
                        action="#{ticketController.deleteFileInfo}" >
                        <t:updateActionListener value="#{fie.fileInfo}" property="#{ticketController.fileInfoToDelete}" />
                    </e:commandButton>
                </t:htmlTag>
            </t:column>
        </e:dataTable>
    </t:htmlTag>

<t:htmlTag value="div" styleClass="form-block" rendered="#{ticketController.userCanGiveInformation}">
        <t:htmlTag value="fieldset" styleClass="active">
              <t:htmlTag value="legend">
                  <t:htmlTag value="span" >
                            <h:outputText value="#{msgs['TICKET_ACTION.TAB.FILES.TEXT']}"/>
                  </t:htmlTag>
              </t:htmlTag>

	        <t:htmlTag id="uploadedFile" value="div" styleClass="form-item">
                <e:inputFileUpload value="#{ticketController.uploadedFile}" storage="memory" />
                <e:commandButton value="#{msgs['TICKET_VIEW.FILES.BUTTON.UPLOAD']}"
                            id="uploadButton"
                            action="#{ticketController.doUpload}"/>
		    </t:htmlTag>
            <t:htmlTag  value="div" styleClass="form-item" rendered="#{ticketController.actionScopeItems != null}">
                <e:outputLabel for="scope" value="#{msgs['TICKET_ACTION.TEXT.UPLOAD_FILE.SCOPE_PROMPT']} " />
                <e:selectOneMenu id="scope"
                    value="#{ticketController.actionScope}" >
                    <f:selectItems value="#{ticketController.actionScopeItems}" />
                </e:selectOneMenu>
            </t:htmlTag>
             <t:htmlTag  value="div" styleClass="form-item form-checkbox" rendered="#{ticketController.userCanSetNoAlert}" >
                    <e:selectBooleanCheckbox id="uploadNoAlert" value="#{ticketController.uploadNoAlert}" />
                    <e:outputLabel for="uploadNoAlert" value=" #{msgs['TICKET_ACTION.TEXT.UPLOAD_FILE.NO_NOTIFICATION']} " />
             </t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>

</t:htmlTag>
	
	