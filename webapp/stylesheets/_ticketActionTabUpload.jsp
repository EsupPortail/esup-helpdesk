<%@include file="_include.jsp"%>


<t:htmlTag id="ticketFiles" value="div" styleClass=" block">


    <t:htmlTag value="div" styleClass="content">
        <e:panelGrid id="uploadFile" columns="2" styleClass="uploadFiles">
            <e:inputFileUpload value="#{ticketController.uploadedFile}" storage="memory" />
             <t:htmlTag value="div" styleClass="form-item">
                <e:commandButton
                    value="#{msgs['TICKET_ACTION.BUTTON.UPLOAD']}"
                    id="uploadButton"
                    action="#{ticketController.storeTempUploadedFile}" >
                    <f:param value="#{ticketController.tempUploadedFilesNumber}" />
                </e:commandButton>
            </t:htmlTag>
        </e:panelGrid>

        <e:dataTable
            id="tempUploadedFileData" rowIndexVar="index"
            styleClass="uploadedFiles"
            value="#{ticketController.tempUploadedFiles}"
            var="tempUploadedFile" border="0" cellspacing="0" cellpadding="0"
            rendered="#{not empty ticketController.tempUploadedFiles}" >
            <t:column>
                    <e:text value="#{tempUploadedFile.name}" />
            </t:column>
            <t:column>
                <h:panelGroup styleClass="cursor--pointer"
                            onclick="buttonClick('ticketActionForm:tempUploadedFileData:#{index}:deleteUploadedFileButton');">
                            <t:htmlTag value="i" styleClass="fas fa-trash-alt"/>
                </h:panelGroup>
                <e:commandButton style="display: none"
                    value="#{msgs['_.BUTTON.DELETE']}"
                    id="deleteUploadedFileButton"
                    action="#{ticketController.deleteTempUploadedFile}" >
                    <t:updateActionListener value="#{index}" property="#{ticketController.tempUploadedFileToDeleteIndex}" />
                </e:commandButton>
            </t:column>
        </e:dataTable>
    </t:htmlTag>
</t:htmlTag>





