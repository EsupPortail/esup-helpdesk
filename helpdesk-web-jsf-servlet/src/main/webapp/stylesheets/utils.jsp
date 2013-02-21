<%@include file="_include.jsp"%>
<%@ taglib uri="http://sourceforge.net/projects/jsf-comp" prefix="c" %>
<e:page stringsVar="msgs" menuItem="utils"
	locale="#{sessionController.locale}">
	<%@include file="_navigation.jsp"%>
	
	<h:panelGroup rendered="#{utilsController.currentUser == null}" >
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="utilsForm" rendered="#{utilsController.pageAuthorized}" >
		<e:section value="#{msgs['UTILS.TITLE']}" />
		<e:messages />
		<e:panelGrid columns="2" columnClasses="colRight,colLeft" >
			<e:outputLabel for="utility" value="#{msgs['UTILS.TEXT.UTILITY_PROMPT']} " />
			<h:panelGroup>
				<e:selectOneMenu id="utility" value="#{utilsController.utility}" 
					onchange="simulateLinkClick('utilsForm:changeUtilityButton');" >
					<f:selectItems value="#{utilsController.utilitiesItems}" />
				</e:selectOneMenu>
				<e:commandButton style="display: none"
					value="#{msgs['_.BUTTON.UPDATE']}"
					id="changeUtilityButton"
					action="#{utilsController.enter}" />
			</h:panelGroup>
			<e:outputLabel rendered="#{utilsController.utility != 0}" for="ldapUid"
				value="#{domainService.useLdap ? msgs['UTILS.TEXT.USER_PROMPT'] : msgs['UTILS.TEXT.USER_PROMPT_NO_LDAP']} " />
			<h:panelGroup rendered="#{utilsController.utility != 0}" >
				<e:inputText id="ldapUid" value="#{utilsController.ldapUid}" 
						onkeypress="if (event.keyCode == 13) { if (#{utilsController.utility == 1}) focusElement('utilsForm:computer'); else simulateLinkClick('utilsForm:testButton'); return false; }" />
				<e:message for="ldapUid" />
				<h:panelGroup rendered="#{domainService.useLdap}" >
					<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('utilsForm:ldapSearchButton');" >
						<e:bold value=" #{msgs['_.BUTTON.LDAP']} " />
						<t:graphicImage value="/media/images/search.png"
							alt="#{msgs['_.BUTTON.LDAP']}" 
							title="#{msgs['_.BUTTON.LDAP']}" />
					</h:panelGroup>
					<e:commandButton style="display:none"
						id="ldapSearchButton" action="#{ldapSearchController.firstSearch}" 
						value="#{msgs['_.BUTTON.LDAP']}" >
						<t:updateActionListener value="#{utilsController}"
							property="#{ldapSearchController.caller}" />
						<t:updateActionListener value="userSelectedToUtils"
							property="#{ldapSearchController.successResult}" />
						<t:updateActionListener value="cancelToUtils"
							property="#{ldapSearchController.cancelResult}" />
					</e:commandButton>
				</h:panelGroup>
			</h:panelGroup>
			<h:panelGroup rendered="#{utilsController.utility != 0}" />
			<h:panelGroup rendered="#{utilsController.utility != 0}" >
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('utilsForm:testButton');" >
					<e:bold value=" #{msgs['UTILS.BUTTON.TEST']} " />
					<t:graphicImage value="/media/images/next.png" />
				</h:panelGroup>
				<e:commandButton style="display:none" 
					id="testButton" action="#{utilsController.test}"
					value="#{msgs['UTILS.BUTTON.TEST']}" >
				</e:commandButton>
			</h:panelGroup>
		</e:panelGrid>
		<h:panelGroup rendered="#{utilsController.utility == 1 and utilsController.ldapUser != null}" >
			<e:subSection value="#{msgs['UTILS.TEXT.LDAP.TITLE_NO_ATTRIBUTE']}" 
				rendered="#{empty utilsController.ldapUser.attributeNames}" >
					<f:param value="#{userFormatter[utilsController.testUser]}" />
			</e:subSection>
			<h:panelGroup rendered="#{not empty utilsController.ldapUser.attributeNames}" >
				<e:subSection value="#{msgs['UTILS.TEXT.LDAP.TITLE']}" >
					<f:param value="#{utilsController.ldapUserAttributesNumber}" />
					<f:param value="#{userFormatter[utilsController.testUser]}" />
				</e:subSection>
				<t:dataList value="#{utilsController.ldapUser.attributeNames}" var="name"
					rowCountVar="namesCount" rowIndexVar="nameIndex">
					<t:htmlTag value="br"
						rendered="#{nameIndex != 0}" />
					<e:text value="#{name}=" />
					<t:dataList var="value" value="#{utilsController.ldapUser.attributes[name]}"
						rowCountVar="valuesCount" rowIndexVar="valueIndex">
						<e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.OPEN_BRACE']}"
							rendered="#{(valueIndex==0) and (valuesCount >1)}" />
						<e:bold value="#{value}" />
						<e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.SEPARATOR']}"
							rendered="#{(valueIndex+1 < valuesCount) and (valuesCount >1)}" />
						<e:text
							value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.CLOSE_BRACE']}"
							rendered="#{(valueIndex+1==valuesCount) and (valuesCount >1)}" />
					</t:dataList>
				</t:dataList>
			</h:panelGroup>
		</h:panelGroup>
		<h:panelGroup rendered="#{utilsController.utility == 2 and utilsController.portalUser != null}" >
			<e:subSection value="#{msgs['UTILS.TEXT.PORTAL.ATTRIBUTES_TITLE_NONE']}" 
				rendered="#{empty utilsController.portalUser.attributeNames}" >
					<f:param value="#{userFormatter[utilsController.testUser]}" />
			</e:subSection>
			<h:panelGroup rendered="#{not empty utilsController.portalUser.attributeNames}" >
				<e:subSection value="#{msgs['UTILS.TEXT.PORTAL.ATTRIBUTES_TITLE']}" >
					<f:param value="#{utilsController.portalUserAttributesNumber}" />
					<f:param value="#{userFormatter[utilsController.testUser]}" />
				</e:subSection>
				<t:dataList value="#{utilsController.portalUser.attributeNames}" var="name"
					rowCountVar="namesCount" rowIndexVar="nameIndex">
					<t:htmlTag value="br"
						rendered="#{nameIndex != 0}" />
					<e:text value="#{name}=" />
					<t:dataList var="value" value="#{utilsController.portalUser.attributes[name]}"
						rowCountVar="valuesCount" rowIndexVar="valueIndex">
						<e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.OPEN_BRACE']}"
							rendered="#{(valueIndex==0) and (valuesCount >1)}" />
						<e:bold value="#{value}" />
						<e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.SEPARATOR']}"
							rendered="#{(valueIndex+1 < valuesCount) and (valuesCount >1)}" />
						<e:text
							value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.CLOSE_BRACE']}"
							rendered="#{(valueIndex+1==valuesCount) and (valuesCount >1)}" />
					</t:dataList>
				</t:dataList>
			</h:panelGroup>
			<e:subSection value="#{msgs['UTILS.TEXT.PORTAL.GROUPS_TITLE_NONE']}" 
				rendered="#{empty utilsController.portalGroups}" >
					<f:param value="#{userFormatter[utilsController.testUser]}" />
			</e:subSection>
			<h:panelGroup rendered="#{not empty utilsController.portalGroups}" >
				<e:subSection value="#{msgs['UTILS.TEXT.PORTAL.GROUPS_TITLE']}" >
					<f:param value="#{utilsController.portalGroupsNumber}" />
					<f:param value="#{userFormatter[utilsController.testUser]}" />
				</e:subSection>
				<t:dataList value="#{utilsController.portalGroups}" var="group"
					rowCountVar="groupsCount" rowIndexVar="groupIndex">
					<e:li value="#{msgs['UTILS.TEXT.PORTAL.GROUP']}" >
						<f:param value="#{group.name}" />
						<f:param value="#{group.id}" />
					</e:li>
				</t:dataList>
			</h:panelGroup>
		</h:panelGroup>
		<h:panelGroup rendered="#{utilsController.utility == 3 and utilsController.testUser != null}" >
			<e:subSection value="#{msgs['UTILS.TEXT.USER_INFO.TITLE_NONE']}" 
				rendered="#{utilsController.userInfo == null}" >
					<f:param value="#{userFormatter[utilsController.testUser]}" />
			</e:subSection>
			<h:panelGroup rendered="#{utilsController.userInfo != null}" >
				<e:subSection value="#{msgs['UTILS.TEXT.USER_INFO.TITLE']}" >
					<f:param value="#{userFormatter[utilsController.testUser]}" />
				</e:subSection>
				<h:outputText value="#{utilsController.userInfo}" escape="false" />
			</h:panelGroup>
		</h:panelGroup>
	</e:form>

	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>

</e:page>
