<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="responses" locale="#{sessionController.locale}" >
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="responseEditForm">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['RESPONSE_EDIT.TITLE']}" />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('responseEditForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="cancelButton" action="cancel"
					value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:panelGrid id="responseEditPanel" columns="2" columnClasses="colLeftNowrap,colLeftMax">
			<e:outputLabel for="label"
				value="#{msgs['RESPONSE_EDIT.TEXT.LABEL']}" />
			<e:inputText id="label"
				value="#{responsesController.responseToUpdate.label}" 
				onkeypress="if (event.keyCode == 13) { FCKeditorAPI.GetInstance('responseEditForm:message').Focus(); return false;}" />
			<e:outputLabel for="message"
				value="#{msgs['RESPONSE_EDIT.TEXT.MESSAGE']}" />
			<fck:editor  
				id="message" 
				value="#{responsesController.responseToUpdate.message}" 
				toolbarSet="actionMessage" />
			<h:panelGroup />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('responseEditForm:editButton');" >
					<e:bold value="#{msgs['RESPONSE_EDIT.BUTTON.EDIT_RESPONSE']} " />
					<t:graphicImage value="/media/images/save.png" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="editButton" action="#{responsesController.doEditResponse}"
					value="#{msgs['RESPONSE_EDIT.BUTTON.EDIT_RESPONSE']}" />
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		focusElement("responseEditForm:label");
</script>
</e:page>
