<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="faqs" authorized="#{faqsController.pageAuthorized}" locale="#{sessionController.locale}" >
	   <t:htmlTag id="faqView" value="div" styleClass="faqView">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content">
                    <t:htmlTag value="div" styleClass="content-inner">
                        <e:form
                            freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                            showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                            showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                            id="faqsForm" >
                              <t:htmlTag value="div" styleClass="message">
                                      <e:messages/>
                              </t:htmlTag>

                              <t:htmlTag value="div" styleClass="dashboard-header">
                                <t:htmlTag value="div" styleClass="controlPanel-title">
                                        <t:htmlTag value="h1">
                                            <t:htmlTag value="span" styleClass="title">
                                                  <h:outputText value="#{msgs['FAQS.TITLE.VIEW']}" escape="false" rendered="#{ not faqsController.editInterface }" />
                                                  <h:outputText value="#{msgs['FAQS.TITLE.EDIT']}" escape="false" rendered="#{ preferencesController.userAdmin && faqsController.editInterface}" />
                                            </t:htmlTag>

                                        </t:htmlTag>

                                    <t:htmlTag styleClass="dashboard-toggle" value="div" rendered="#{ preferencesController.userAdmin }">
                                        <h:panelGroup rendered="#{not faqsController.editInterface}">
                                            <h:panelGroup style="cursor: pointer"
                                                onclick="selectChange('faqsForm:interface','true')">
                                                <t:htmlTag value="i"
                                                styleClass="fas fa-toggle-on"/>
                                            </h:panelGroup>
                                        </h:panelGroup>
                                        <h:panelGroup rendered="#{faqsController.editInterface}">
                                            <h:panelGroup style="cursor: pointer"
                                                onclick="selectChange('faqsForm:interface','false')">
                                                <t:htmlTag value="i"
                                                styleClass="fas fa-toggle-off"/>
                                            </h:panelGroup>
                                        </h:panelGroup>
                                        <e:selectOneMenu styleClass="hideme" id="interface"  value="#{faqsController.editInterface}"
                                            onchange="simulateLinkClick('faqsForm:updateInterfaceButton');" >
                                            <f:selectItems value="#{faqsController.interfaceItems}" />
                                        </e:selectOneMenu>
                                        <e:commandButton id="updateInterfaceButton" styleClass="hideme"
                                            value="#{msgs['_.BUTTON.REFRESH']}"
                                            action="#{faqsController.updateInterface}" />
                                    </t:htmlTag>
                                </t:htmlTag>
                              </t:htmlTag>

							<t:htmlTag value="fieldset">
                                <t:htmlTag value="legend">
                                    <t:htmlTag value="span" >
                                        <h:outputText value="#{msgs['FAQS.TITLE.VIEW_GENERAL_FAQS']}"/>
                                    </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
		                            <e:panelGrid columns="3" columnClasses="colLeft,colCenter,colLeftMax" width="100%"  >
		                                <h:panelGroup >
		                                    <t:graphicImage value="/media/images/trans.png" height="1" width="400" />
		                                    <%@include file="_faqsTree.jsp"%>
		                                </h:panelGroup>
		                                <e:text escape="false" value="&nbsp;" style="width: 20px" />
		                                <h:panelGroup style="display:block">
		                                    <%@include file="_faqRoot.jsp"%>
		                                    <%@include file="_faq.jsp"%>
		                                    <%@include file="_faqSubFaqs.jsp"%>
		                                </h:panelGroup>
		                            </e:panelGrid>
	    	                    </t:htmlTag>
                           </t:htmlTag>                            
                            
							<t:htmlTag value="fieldset" rendered="#{not faqsController.editInterface}">
                                <t:htmlTag value="legend">
                                    <t:htmlTag value="span" >
                                        <h:outputText value="#{msgs['FAQS.TITLE.VIEW_DEPARTMENTS_FAQS']}"/>
                                    </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
 	                               <e:panelGrid columns="3" columnClasses="colLeft,colCenter,colLeftMax" width="100%"  >
		                                <h:panelGroup>
		                                    <t:graphicImage value="/media/images/trans.png" height="1" width="400" />
			    	                        <%@include file="_faqsTreeAllDpt.jsp"%>
			    	                    </h:panelGroup>
		    	                 		<e:text escape="false" value="&nbsp;" style="width: 20px" />
			                        	<h:panelGroup style="display:block">
				                        <h:panelGroup styleClass="faq" rendered="#{faqsController.faqAllDpt != null}" >
												<e:subSection styleClass="faq-header" value="#{faqsController.faqAllDpt.label}" />
												<e:text escape="false" value="#{faqsController.faqAllDpt.content}"/>
											</h:panelGroup>
				                        </h:panelGroup>
                                	</e:panelGrid>
	    	                    </t:htmlTag>
                           </t:htmlTag>
                        </e:form>
                   </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>
                <t:htmlTag value="footer" styleClass="footer">
		           	<t:aliasBean alias="#{controller}" value="#{faqsController}" >
					    <%@include file="_footer.jsp"%>
					</t:aliasBean>
                </t:htmlTag>
        </t:htmlTag>
</e:page>
