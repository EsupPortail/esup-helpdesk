<%@include file="_include.jsp"%>

<e:panelGrid columns="3" columnClasses="colLeftNowrap,colCenterNowrap,colRightNowrap" width="100%" >
	<h:panelGroup>
		<e:text value="#{msgs['COMMON.DATE_USER']}" style="font-size: 75%;" 
			rendered="#{sessionController.currentUser != null}">
			<f:param value="#{sessionController.now}" />
			<f:param value="#{userFormatter[sessionController.currentUser]}" />
		</e:text>
		<e:text value="#{msgs['COMMON.DATE_GUEST']}" style="font-size: 75%;" 
			rendered="#{sessionController.currentUser == null}">
			<f:param value="#{sessionController.now}" />
		</e:text>
	</h:panelGroup>
	<h:panelGroup>
		<e:text value="#{msgs['COMMON.MEMORY']}" style="font-size: 75%;" 
		rendered="#{sessionController.currentUser != null && sessionController.currentUser.admin}" >
			<f:param value="#{sessionController.freeMemory}" />
			<f:param value="#{sessionController.totalMemory}" />
			<f:param value="#{sessionController.maxMemory}" />
		</e:text>
	</h:panelGroup>
	<h:panelGroup>
		<h:panelGroup rendered="#{controller != null}" >
			<e:text 
				value="#{msgs['COMMON.PERM_LINKS.PROMPT']}" 
				escape="false" style="font-size: 75%;" />
			<e:text 
				value=" #{msgs['COMMON.PERM_LINKS.APPLICATION']}" 
				escape="false" style="font-size: 75%;" 
				rendered="#{userStore.applicationAuthAllowed}" >
				<f:param value="#{controller.applicationPermLink}" />
			</e:text>
			<e:text 
				value=" #{msgs['COMMON.PERM_LINKS.CAS']}" 
				escape="false" style="font-size: 75%;" 
                rendered="#{userStore.casAuthAllowed}" >
				<f:param value="#{controller.casPermLink}" />
			</e:text>
			<e:text 
				value=" #{msgs['COMMON.PERM_LINKS.SHIBBOLETH']}" 
				escape="false" style="font-size: 75%;" 
                rendered="#{userStore.shibbolethAuthAllowed}" >
				<f:param value="#{controller.shibbolethPermLink}" />
			</e:text>
			<e:text 
				value=" #{msgs['COMMON.PERM_LINKS.SPECIFIC']}" 
				escape="false" style="font-size: 75%;" 
                rendered="#{userStore.specificAuthAllowed}" >
				<f:param value="#{controller.specificPermLink}" />
			</e:text>
		</h:panelGroup>
	</h:panelGroup>
</e:panelGrid>
