<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" locale="#{sessionController.locale}" >
	<%@include file="_navigation.jsp"%>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="ldapSearchForm">

		<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colRight">
			<e:section value="#{msgs['LDAP_SEARCH.TITLE']}" />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ldapSearchForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.CANCEL']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.CANCEL']}" 
						title="#{msgs['_.BUTTON.CANCEL']}" />
				</h:panelGroup>
				<e:commandButton style="display: none" id="cancelButton" action="#{ldapSearchController.cancel}"
					value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>

		<e:messages />

		<e:inputText id="searchInput" value="#{ldapSearchController.searchInput}" 
			onkeypress="if (event.keyCode == 13) { simulateLinkClick('ldapSearchForm:searchButton'); return false;}" />
		<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('ldapSearchForm:searchButton');" >
			<e:bold value=" #{msgs['LDAP_SEARCH.BUTTON.SEARCH']} " />
			<t:graphicImage value="/media/images/search.png"
				alt="#{msgs['LDAP_SEARCH.BUTTON.SEARCH']}" 
				title="#{msgs['LDAP_SEARCH.BUTTON.SEARCH']}" />
		</h:panelGroup>
		<e:commandButton style="display: none" id="searchButton" action="#{ldapSearchController.search}"
			value="#{msgs['LDAP_SEARCH.BUTTON.SEARCH']}" />
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{null}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	<script type="text/javascript">
		focusElement("ldapSearchForm:searchInput");
	</script>
</e:page>
