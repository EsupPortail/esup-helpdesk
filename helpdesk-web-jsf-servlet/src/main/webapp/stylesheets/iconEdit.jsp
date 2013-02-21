<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="administrators" locale="#{sessionController.locale}"
	authorized="#{administratorsController.currentUserCanManageIcons}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="iconEditForm" enctype="multipart/form-data" >
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
			<e:section value="#{msgs['ICON_EDIT.TITLE']}">
				<f:param value="#{administratorsController.iconToUpdate.name}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('categoryEditForm:cancelButton');" >
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

		<e:panelGrid columns="2" columnClasses="colLeftNowrap,colLeft" >
			<e:outputLabel for="name" value="#{msgs['ICON_EDIT.TEXT.NAME']}" />
			<e:inputText id="name" value="#{administratorsController.iconToUpdate.name}" size="30" />
			<e:outputLabel for="image" value="#{msgs['ICON_EDIT.TEXT.IMAGE']}" />
			<h:panelGroup>
				<t:graphicImage value="#{iconUrlProvider[administratorsController.iconToUpdate]}" />
				<e:text value=" " />
				<e:inputFileUpload id="uploadedIcon" value="#{administratorsController.uploadedIcon}" 
					storage="memory" />
			</h:panelGroup>
			<h:panelGroup/>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('iconEditForm:updateButton');" >
					<e:bold value="#{msgs['ICON_EDIT.BUTTON.UPDATE']} " />
					<t:graphicImage value="/media/images/save.png"
						alt="#{msgs['ICON_EDIT.BUTTON.UPDATE']}" 
						title="#{msgs['ICON_EDIT.BUTTON.UPDATE']}" />
				</h:panelGroup>
				<e:commandButton 
					id="updateButton" action="#{administratorsController.updateIcon}"
					value="#{msgs['ICON_EDIT.BUTTON.UPDATE']}" style="display: none" />
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
</e:page>
