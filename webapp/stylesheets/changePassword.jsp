<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="preferences" locale="#{sessionController.locale}" >
	<%@include file="_navigation.jsp"%>

	<h:panelGroup rendered="#{sessionController.currentUser == null}" >
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
 	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="changePasswordForm" 
		rendered="#{sessionController.currentUser != null and sessionController.applicationUser}" >
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['CHANGE_PASSWORD.TITLE']}" />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('changePasswordForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton id="cancelButton" action="cancel" style="display: none"
					value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>
		<e:messages />
		<e:panelGrid columns="2">
			<e:outputLabel for="oldPassword" value="#{msgs['CHANGE_PASSWORD.TEXT.OLD_PASSWORD']}" rendered="#{preferencesController.oldPassword == null}" />
			<e:inputSecret id="oldPassword" value="#{preferencesController.oldPassword}" rendered="#{preferencesController.oldPassword == null}" />
			<e:outputLabel for="newPassword1" value="#{msgs['CHANGE_PASSWORD.TEXT.NEW_PASSWORD_1']}" />
			<e:inputSecret id="newPassword1" value="#{preferencesController.newPassword1}" />
			<e:outputLabel for="newPassword2" value="#{msgs['CHANGE_PASSWORD.TEXT.NEW_PASSWORD_2']}" />
			<e:inputSecret id="newPassword2" value="#{preferencesController.newPassword2}" />
			<h:panelGroup />
			<h:panelGroup >
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('changePasswordForm:updateButton');" >
					<e:bold value="#{msgs['_.BUTTON.UPDATE']} " />
					<t:graphicImage value="/media/images/save.png"
						alt="#{msgs['_.BUTTON.UPDATE']}" 
						title="#{msgs['_.BUTTON.UPDATE']}" />
				</h:panelGroup>
				<e:commandButton id="updateButton" style="display: none" 
					action="#{preferencesController.doChangePassword}"
					value="#{msgs['_.BUTTON.UPDATE']}" />
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
