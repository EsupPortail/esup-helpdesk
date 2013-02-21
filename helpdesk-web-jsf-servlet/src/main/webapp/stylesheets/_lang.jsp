<%@include file="_include.jsp"%>
<e:form id="langForm"
	rendered="#{sessionController.currentUser == null}">
	<e:panelGrid columns="2">
		<e:outputLabel for="locale"
			value="#{msgs['PREFERENCES.TEXT.LANGUAGE']}" />
		<h:panelGroup>
			<e:selectOneMenu id="locale" onchange="submit();"
				value="#{preferencesController.locale}"
				converter="#{localeConverter}">
				<f:selectItems value="#{preferencesController.localeItems}" />
			</e:selectOneMenu>
			<e:commandButton style="display: none" value="#{msgs['_.BUTTON.CHANGE']}"
				id="localeChangeButton" />
		</h:panelGroup>
	</e:panelGrid>
</e:form>
