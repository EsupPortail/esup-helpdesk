<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="responses" locale="#{sessionController.locale}" >

<script type="text/javascript">
	function toggleContent(data, index) {
		showHideElement('responsesForm:'+data+':'+index+':content'); 
		showHideElement('responsesForm:'+data+':'+index+':showContent'); 
		showHideElement('responsesForm:'+data+':'+index+':hideContent'); 
	}
	function toggleGlobalContent(index) {
		toggleContent('globalData', index);  
	}
	function toggleUserContent(index) {
		toggleContent('userData', index);  
	}
	function toggleDepartmentContent(departmentIndex,index) {
		toggleContent('departmentData:'+departmentIndex+':data', index);  
	}
</script>

	<%@include file="_navigation.jsp"%>

	<h:panelGroup rendered="#{responsesController.currentUser == null}" >
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="responsesForm" rendered="#{responsesController.pageAuthorized}" >
		<e:section value="#{msgs['RESPONSES.TITLE']}" />
		<e:messages />
		<e:panelGrid width="100%" columns="2" columnClasses="colLeft,colLeft">
			<h:panelGroup>
				<e:dataTable id="globalData" var="response" value="#{responsesController.globalResponses}" 
					width="100%" alternateColors="true" columnClasses="colLeft,colLeftMax,colRight,colRight" rowIndexVar="index" 
					rowOnMouseOver="javascript:{previousClass = this.className; this.className = 'portlet-table-selected';}"
					rowOnMouseOut="javascript:{this.className = previousClass;}"
					cellpadding="0" cellspacing="0" >
					<f:facet name="header">
						<h:panelGroup>
							<e:panelGrid width="100%" columns="2" columnClasses="colLeft,colRight">
								<e:subSection value="#{msgs['RESPONSES.HEADER.GLOBAL_RESPONSES']}" >
									<f:param value="#{responsesController.globalResponsesNumber}" />
								</e:subSection>
								<h:panelGroup>
									<h:panelGroup rendered="#{responsesController.userCanManageGlobalResponses}" >
										<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('responsesForm:globalData:addResponseButton');" >
											<e:bold value="#{msgs['RESPONSES.BUTTON.ADD_GLOBAL_RESPONSE']} " />
											<t:graphicImage value="/media/images/add.png" />
										</h:panelGroup>
										<e:commandButton 
											id="addResponseButton" style="display: none"
											value="#{msgs['RESPONSES.BUTTON.ADD_GLOBAL_RESPONSE']}"
											action="#{responsesController.addGlobalResponse}" />
									</h:panelGroup>
								</h:panelGroup>
							</e:panelGrid>
						</h:panelGroup>
					</f:facet>
					<t:column>
						<h:panelGroup style="cursor: pointer" 
							onclick="javascript:{toggleGlobalContent('#{index}'); return false;}" >
							<t:graphicImage id="showContent" value="/media/images/show.png" style="display: block" />
							<t:graphicImage id="hideContent" value="/media/images/hide.png" style="display: none" />
						</h:panelGroup>
					</t:column>
					<t:column>
						<e:bold value="#{msgs['RESPONSES.TEXT.GLOBAL_RESPONSE']} " >
							<f:param value="#{response.label}" />
						</e:bold>
						<h:panelGroup id="content" style="display: none" >
							<t:htmlTag value="hr" />
							<h:outputText 
								value="#{response.message}"
								escape="false" />
						</h:panelGroup>
					</t:column>
					<t:column>
						<h:panelGroup rendered="#{responsesController.userCanManageGlobalResponses}" >
							<t:graphicImage value="/media/images/edit.png" 
								style="cursor: pointer" onclick="javascript:{if (#{responsesController.userCanManageGlobalResponses}) simulateLinkClick('responsesForm:globalData:#{index}:editResponseButton'); return false;}" />
							<e:commandButton 
								id="editResponseButton" style="display: none"
								value="#{msgs['_.BUTTON.UPDATE']}"
								action="#{responsesController.editResponse}" >
								<t:updateActionListener value="#{response}" property="#{responsesController.responseToUpdate}" />
							</e:commandButton>
						</h:panelGroup>
					</t:column>
					<t:column>
						<h:panelGroup rendered="#{responsesController.userCanManageGlobalResponses}" >
							<t:graphicImage value="/media/images/delete.png" 
								style="cursor: pointer" onclick="javascript:{if (#{responsesController.userCanManageGlobalResponses}) simulateLinkClick('responsesForm:globalData:#{index}:deleteResponseButton'); return false;}" />
							<e:commandButton 
								id="deleteResponseButton" style="display: none"
								value="#{msgs['_.BUTTON.DELETE']}"
								action="#{responsesController.doDeleteResponse}" >
								<t:updateActionListener value="#{response}" property="#{responsesController.responseToDelete}" />
							</e:commandButton>
						</h:panelGroup>
					</t:column>
					<f:facet name="footer">
						<t:htmlTag value="hr" />
					</f:facet>
				</e:dataTable>
				<t:htmlTag value="br" />
				<e:dataTable id="userData" var="response" value="#{responsesController.userResponses}" 
					width="100%" alternateColors="true" columnClasses="colLeft,colLeftMax,colRight,colRight" rowIndexVar="index" 
					rowOnMouseOver="javascript:{previousClass = this.className; this.className = 'portlet-table-selected';}"
					rowOnMouseOut="javascript:{this.className = previousClass;}"
					cellpadding="0" cellspacing="0" >
					<f:facet name="header">
						<e:panelGrid width="100%" columns="2" columnClasses="colLeft,colRight">
							<e:subSection value="#{msgs['RESPONSES.HEADER.USER_RESPONSES']}" >
								<f:param value="#{responsesController.userResponsesNumber}" />
							</e:subSection>
							<h:panelGroup>
								<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('responsesForm:userData:addResponseButton');" >
									<e:bold value="#{msgs['RESPONSES.BUTTON.ADD_USER_RESPONSE']} " />
									<t:graphicImage value="/media/images/add.png" />
								</h:panelGroup>
								<e:commandButton 
									id="addResponseButton" style="display: none"
									value="#{msgs['RESPONSES.BUTTON.ADD_USER_RESPONSE']}"
									action="#{responsesController.addUserResponse}" />
							</h:panelGroup>
						</e:panelGrid>
					</f:facet>
					<t:column>
						<h:panelGroup style="cursor: pointer" 
							onclick="javascript:{toggleUserContent('#{index}'); return false;}" >
							<t:graphicImage id="showContent" value="/media/images/show.png" style="display: block" />
							<t:graphicImage id="hideContent" value="/media/images/hide.png" style="display: none" />
						</h:panelGroup>
					</t:column>
					<t:column>
						<e:bold value="#{msgs['RESPONSES.TEXT.USER_RESPONSE']} " >
							<f:param value="#{userFormatter[responsesController.currentUser]}" />
							<f:param value="#{response.label}" />
						</e:bold>
						<h:panelGroup id="content" style="display: none" >
							<t:htmlTag value="hr" />
							<h:outputText 
								value="#{response.message}"
								escape="false" />
						</h:panelGroup>
					</t:column>
					<t:column>
						<h:panelGroup >
							<t:graphicImage value="/media/images/edit.png" 
								style="cursor: pointer" onclick="javascript:{simulateLinkClick('responsesForm:userData:#{index}:editResponseButton'); return false;}" />
							<e:commandButton 
								id="editResponseButton" style="display: none"
								value="#{msgs['_.BUTTON.UPDATE']}"
								action="#{responsesController.editResponse}" >
								<t:updateActionListener value="#{response}" property="#{responsesController.responseToUpdate}" />
							</e:commandButton>
						</h:panelGroup>
					</t:column>
					<t:column>
						<h:panelGroup >
							<t:graphicImage value="/media/images/delete.png" 
								style="cursor: pointer" onclick="javascript:{simulateLinkClick('responsesForm:userData:#{index}:deleteResponseButton'); return false;}" />
							<e:commandButton 
								id="deleteResponseButton" style="display: none"
								value="#{msgs['_.BUTTON.DELETE']}"
								action="#{responsesController.doDeleteResponse}" >
								<t:updateActionListener value="#{response}" property="#{responsesController.responseToDelete}" />
							</e:commandButton>
						</h:panelGroup>
					</t:column>
					<f:facet name="footer">
						<t:htmlTag value="hr" />
					</f:facet>
				</e:dataTable>
			</h:panelGroup>
			<h:panelGroup>
				<t:dataList id="departmentData" 
					value="#{responsesController.departments}" 
					var="department" rowIndexVar="departmentIndex" >
					<h:panelGroup >
						<e:dataTable id="data" var="response" value="#{responsesController.departmentResponses[department]}" 
							width="100%" alternateColors="true" columnClasses="colLeft,colLeftMax,colRight,colRight" rowIndexVar="responseIndex" 
							rowOnMouseOver="javascript:{previousClass = this.className; this.className = 'portlet-table-selected';}"
							rowOnMouseOut="javascript:{this.className = previousClass;}"
							cellpadding="0" cellspacing="0" >
							<f:facet name="header">
								<e:panelGrid width="100%" columns="2" columnClasses="colLeft,colRight">
									<e:subSection value="#{msgs['RESPONSES.HEADER.DEPARTMENT_RESPONSES']}" >
										<f:param value="#{department.label}" />
										<f:param value="#{responsesController.departmentResponsesNumber[department]}" />
									</e:subSection>
									<h:panelGroup>
										<h:panelGroup rendered="#{responsesController.userCanManageDepartmentResponses[department]}" >
											<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('responsesForm:departmentData:#{departmentIndex}:data:addResponseButton');" >
												<e:bold value="#{msgs['RESPONSES.BUTTON.ADD_DEPARTMENT_RESPONSE']} " >
													<f:param value="#{department.label}" />
												</e:bold>
												<t:graphicImage value="/media/images/add.png" />
											</h:panelGroup>
											<e:commandButton 
												id="addResponseButton" style="display: none"
												value="#{msgs['RESPONSES.BUTTON.ADD_DEPARTMENT_RESPONSE']}"
												action="#{responsesController.addDepartmentResponse}" >
												<t:updateActionListener value="#{department}" property="#{responsesController.targetDepartment}" />
											</e:commandButton>
										</h:panelGroup>
									</h:panelGroup>
								</e:panelGrid>
							</f:facet>
							<t:column>
								<h:panelGroup style="cursor: pointer" 
									onclick="javascript:{toggleDepartmentContent('#{departmentIndex}','#{responseIndex}'); return false;}" >
									<t:graphicImage id="showContent" value="/media/images/show.png" style="display: block" />
									<t:graphicImage id="hideContent" value="/media/images/hide.png" style="display: none" />
								</h:panelGroup>
							</t:column>
							<t:column>
								<e:bold value="#{msgs['RESPONSES.TEXT.DEPARTMENT_RESPONSE']} " >
									<f:param value="#{department.label}" />
									<f:param value="#{response.label}" />
								</e:bold>
								<h:panelGroup id="content" style="display: none" >
									<t:htmlTag value="hr" />
									<h:outputText 
										value="#{response.message}"
										escape="false" />
								</h:panelGroup>
							</t:column>
							<t:column>
								<h:panelGroup rendered="#{responsesController.userCanManageDepartmentResponses[department]}" >
									<t:graphicImage value="/media/images/edit.png" 
										style="cursor: pointer" 
										onclick="javascript:{simulateLinkClick('responsesForm:departmentData:#{departmentIndex}:data:#{responseIndex}:editResponseButton'); return false;}" />
									<e:commandButton 
										id="editResponseButton" style="display: none"
										value="#{msgs['_.BUTTON.UPDATE']}"
										action="#{responsesController.editResponse}" >
										<t:updateActionListener value="#{response}" property="#{responsesController.responseToUpdate}" />
									</e:commandButton>
								</h:panelGroup>
							</t:column>
							<t:column>
								<h:panelGroup rendered="#{responsesController.userCanManageDepartmentResponses[department]}" >
									<t:graphicImage value="/media/images/delete.png" 
										style="cursor: pointer" 
										onclick="javascript:{simulateLinkClick('responsesForm:departmentData:#{departmentIndex}:data:#{responseIndex}:deleteResponseButton'); return false;}" />
									<e:commandButton 
										id="deleteResponseButton" style="display: none"
										value="#{msgs['_.BUTTON.DELETE']}"
										action="#{responsesController.doDeleteResponse}" >
										<t:updateActionListener value="#{response}" property="#{responsesController.responseToDelete}" />
									</e:commandButton>
								</h:panelGroup>
							</t:column>
							<f:facet name="footer">
								<t:htmlTag value="hr" />
							</f:facet>
						</e:dataTable>
						<t:htmlTag value="br" />
					</h:panelGroup>
				</t:dataList>
			</h:panelGroup>
		</e:panelGrid>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{responsesController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
	
</e:page>
