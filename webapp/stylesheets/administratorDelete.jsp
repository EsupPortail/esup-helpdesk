<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="administrators" locale="#{sessionController.locale}" authorized="#{administratorsController.currentUserCanDeleteAdmin}" >
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="administratorDeleteForm" >
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['ADMINISTRATOR_DELETE.TITLE']}">
				<f:param value="#{userFormatter[administratorsController.userToDelete]}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('administratorDeleteForm:cancelButton');" >
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

		<e:paragraph value="#{msgs['ADMINISTRATOR_DELETE.TEXT.TOP']}">
				<f:param value="#{userFormatter[administratorsController.userToDelete]}" />
		</e:paragraph>

		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('administratorDeleteForm:confirmButton');" >
			<e:bold value="#{msgs['_.BUTTON.CONFIRM']} " />
			<t:graphicImage value="/media/images/delete.png"
				alt="#{msgs['_.BUTTON.CONFIRM']}" 
				title="#{msgs['_.BUTTON.CONFIRM']}" />
		</h:panelGroup>
		<e:commandButton id="confirmButton" action="#{administratorsController.confirmDeleteAdmin}"
			value="#{msgs['_.BUTTON.CONFIRM']}" style="display: none" />

	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
