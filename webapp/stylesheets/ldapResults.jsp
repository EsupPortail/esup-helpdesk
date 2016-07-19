<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" locale="#{sessionController.locale}" >

<script type="text/javascript">
	function selectUser(index) {
		simulateLinkClick('ldapResultsForm:data:'+index+':selectButton');
	}
</script>

	<%@include file="_navigation.jsp"%>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ldapResultsForm">

		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['LDAP_RESULTS.TITLE']}" />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ldapResultsForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton id="cancelButton" action="#{ldapSearchController.cancel}"
					value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:paragraph value="#{msgs['LDAP_RESULTS.TEXT.TOP']}" />

		<e:dataTable value="#{ldapSearchController.ldapUsers}" var="ldapUser"
			border="0" id="data" rowIndexVar="variable" 
			cellspacing="0" cellpadding="0" width="100%">
			<t:column style="cursor: pointer" onclick="selectUser(#{variable});" >
				<f:facet name="header">
					<e:text value="#{msgs['LDAP_RESULTS.HEADER.ID']}" />
				</f:facet>
				<e:text value="#{ldapUser.id}" />
			</t:column>
			<t:column style="cursor: pointer" onclick="selectUser(#{variable});" >
				<f:facet name="header">
					<e:text value="#{msgs['LDAP_RESULTS.HEADER.ATTRIBUTES']}" />
				</f:facet>
				<t:dataList value="#{ldapUser.attributeNames}" var="keyName"
					rowCountVar="rowCountAttrs" rowIndexVar="rowIndexAttrs">
					<e:text value="#{keyName}=" />
					<t:dataList var="valueName" value="#{ldapUser.attributes[keyName]}"
						rowCountVar="rowCount" rowIndexVar="rowIndex">
						<e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.OPEN_BRACE']}"
							rendered="#{(rowIndex==0) and (rowCount >1)}" />
						<e:bold value="#{valueName}" />
						<e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.SEPARATOR']}"
							rendered="#{(rowIndex+1 < rowCount) and (rowCount >1)}" />
						<e:text
							value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.CLOSE_BRACE']}"
							rendered="#{(rowIndex+1==rowCount) and (rowCount >1)}" />
					</t:dataList>
				</t:dataList>
			</t:column>
			<t:column >
				<e:commandButton id="selectButton" style="display: none" 
					action="#{ldapSearchController.selectUser}" immediate="true">
					<t:updateActionListener value="#{ldapUser}"
						property="#{ldapSearchController.selectedUser}" />
				</e:commandButton>
			</t:column>
			<f:facet name="footer">
				<t:htmlTag value="hr" />
			</f:facet>
		</e:dataTable>
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ldapResultsForm:changeButton');" >
			<e:bold value="#{msgs['LDAP_RESULTS.BUTTON.SEARCH_AGAIN']} " />
			<t:graphicImage value="/media/images/previous.png"
				alt="#{msgs['LDAP_RESULTS.BUTTON.SEARCH_AGAIN']}" 
				title="#{msgs['LDAP_RESULTS.BUTTON.SEARCH_AGAIN']}" />
		</h:panelGroup>
		<e:commandButton id="changeButton" action="back"
			value="#{msgs['LDAP_RESULTS.BUTTON.SEARCH_AGAIN']}" immediate="true" />
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
highlightTableRows("ldapResultsForm:data");
hideTableButtons("ldapResultsForm:data","selectButton");
hideButton("ldapResultsForm:cancelButton");
hideButton("ldapResultsForm:changeButton");
</script>
</e:page>
