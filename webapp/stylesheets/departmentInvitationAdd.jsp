<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments"
	locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentManagers}">
	<%@include file="_navigation.jsp"%>

	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="departmentInvitationAddForm">
		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['DEPARTMENT_INVITATION_ADD.TITLE']}">
				<f:param value="#{departmentsController.department.label}" />
			</e:section>
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentInvitationAddForm:cancelButton');" >
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

		<e:panelGrid columns="2" >
			<e:outputLabel for="ldapUid" 
				value="#{domainService.useLdap ? msgs['DEPARTMENT_INVITATION_ADD.TEXT.PROMPT_LDAP'] : msgs['DEPARTMENT_INVITATION_ADD.TEXT.PROMPT_NO_LDAP']}" />
			<h:panelGroup>
				<e:inputText id="ldapUid" value="#{departmentsController.ldapUid}" 
					onkeypress="if (event.keyCode == 13) { simulateLinkClick('departmentInvitationAddForm:addButton'); return false;}" />
				<e:message for="ldapUid" />
				<h:panelGroup rendered="#{domainService.useLdap}" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentInvitationAddForm:ldapSearchButton');" >
						<e:bold value=" #{msgs['_.BUTTON.LDAP']} " />
						<t:graphicImage value="/media/images/search.png"
							alt="#{msgs['_.BUTTON.LDAP']}" 
							title="#{msgs['_.BUTTON.LDAP']}" />
					</h:panelGroup>
					<e:commandButton style="display:none"
						id="ldapSearchButton" action="#{ldapSearchController.firstSearch}"
						value="#{msgs['_.BUTTON.LDAP']}" >
						<t:updateActionListener value="#{departmentsController}"
							property="#{ldapSearchController.caller}" />
						<t:updateActionListener value="userSelectedToDepartmentInvitationAdd"
							property="#{ldapSearchController.successResult}" />
						<t:updateActionListener value="cancelToDepartmentInvitationAdd"
							property="#{ldapSearchController.cancelResult}" />
					</e:commandButton>
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('departmentInvitationAddForm:addButton');" >
					<e:bold value=" #{msgs['DEPARTMENT_INVITATION_ADD.BUTTON.ADD_DEPARTMENT_INVITATION']} " />
					<t:graphicImage value="/media/images/add.png"
						alt="#{msgs['DEPARTMENT_INVITATION_ADD.BUTTON.ADD_DEPARTMENT_INVITATION']}" 
						title="#{msgs['DEPARTMENT_INVITATION_ADD.BUTTON.ADD_DEPARTMENT_INVITATION']}" />
				</h:panelGroup>
				<e:commandButton tabindex="0" id="addButton" action="#{departmentsController.addDepartmentInvitation}" 
					value="#{msgs['DEPARTMENT_INVITATION_ADD.BUTTON.ADD_DEPARTMENT_INVITATION']}" style="display: none" />
			</h:panelGroup>		
		</e:panelGrid>

	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		focusElement("departmentManagerAddForm:ldapUid");
	</script>
</e:page>
