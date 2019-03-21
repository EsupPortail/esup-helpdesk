<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanPostpone}">

		   <t:htmlTag id="ticketPostpone" value="div" styleClass="page-wrapper ticketPostpone">
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
                            id="ticketActionForm" enctype="multipart/form-data" >
                                  <t:htmlTag value="div" styleClass="message">
                                      <e:messages/>
                                  </t:htmlTag>
                                  <t:htmlTag value="div" styleClass="ticket-form">
                                      <t:htmlTag value="div" styleClass="form-block form-header">
                                          <t:htmlTag value="h1">
                                              <t:htmlTag value="span" styleClass="title">
                                                    <h:outputText value="#{msgs['TICKET_ACTION.TITLE.POSTPONE']}" escape="false" />
                                              </t:htmlTag>
                                              <t:htmlTag value="span" styleClass="subtitle">
                                                  <h:outputText value=" #{ticketController.ticket.id}" escape="false" />
                                              </t:htmlTag>
                                          </t:htmlTag>
                                      </t:htmlTag>

                                      <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item" rendered="#{ticketController.postponeDate != null}">
                                                    <t:htmlTag value="span" >
                                                             <h:outputText value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.DATE_PROMPT']} " escape="false" />
                                                    </t:htmlTag>
                                                    <e:bold value="{0}"  >
                                                         <f:param value="#{ticketController.postponeDate}" />
                                                         <f:convertDateTime timeZone="#{sessionController.timezone}" dateStyle="short" type="date" locale="#{sessionController.locale}" />
                                                    </e:bold>
                                                    <e:commandButton
                                                    id="noDateNextButton"
                                                    rendered="#{not ticketController.postponeDateSet}"
                                                    value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.DATE.CLEAR']}"
                                                    immediate="true" >
                                                    <t:updateActionListener value="#{null}" property="#{ticketController.postponeDate}" />
                                                    <t:updateActionListener value="true" property="#{ticketController.postponeDateSet}" />
                                                    </e:commandButton>
                                                    <e:commandButton
                                                        styleClass="button--secondary"
                                                        id="dateNextButton"
                                                        rendered="#{not ticketController.postponeDateSet}"
                                                        value="#{msgs['TICKET_ACTION.BUTTON.VALIDATE_POSTPONE_DATE']}"
                                                        immediate="true" >
                                                        <t:updateActionListener value="true" property="#{ticketController.postponeDateSet}" />

                                                    </e:commandButton>
                                                </t:htmlTag>

                                                <t:htmlTag value="div" styleClass="form-item" rendered="#{ticketController.postponeDate == null}">
                                                    <t:htmlTag value="span" >
                                                             <h:outputText value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.DATE_PROMPT']} " escape="false" />
                                                    </t:htmlTag>
                                                    <t:htmlTag value="span" >
                                                               <h:outputText value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.DATE_NONE']}" escape="false" />
                                                    </t:htmlTag>
                                                </t:htmlTag>
                                      </t:htmlTag>


                                      <t:htmlTag value="div" styleClass="form-block form-calendar">
                                               <t:htmlTag value="div" styleClass="form-item">
                                                     <e:text rendered="#{not ticketController.postponeDateSet}" value="#{msgs['TICKET_ACTION.TEXT.POSTPONE.AUTO_HELP']}" />
                                                     <t:inputCalendar rendered="#{not ticketController.postponeDateSet}" styleClass="calendar" value="#{ticketController.postponeDate}" />
                                               </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-block form-body">
                                            <t:htmlTag value="div" styleClass="form-item">
                                                    <fck:editor
                                                    id="actionMessage"
                                                    value="#{ticketController.actionMessage}"
                                                    toolbarSet="actionMessage" />
                                            </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="form-block">
                                            <t:htmlTag value="div" styleClass="form-item display-flex" >
                                                <e:commandButton id="actionButton"
                                                        styleClass="button--primary"
                                                        value="#{msgs['TICKET_ACTION.BUTTON.POSTPONE']}"
                                                        action="#{ticketController.doPostpone}" />
                                                <%@include file="_ticketActionCancel.jsp"%>
                                           </t:htmlTag>
                                      </t:htmlTag>

                                  </t:htmlTag>
                                     <t:htmlTag styleClass="region extended-properties" value="div">
                                                    <t:htmlTag styleClass="tabs" value="ul">
                                                        <t:htmlTag id="properties" styleClass="tab-link current tab-link-info"  value="li" rendered="#{departmentsController.currentUserCanViewDepartments}">
                                                            <h:outputText value="#{msgs['TICKET_ACTION.TAB.GIVE_INFO.PROPERTIES.TEXT']} " />
                                                        </t:htmlTag>
                                                        <t:htmlTag id="history" styleClass="tab-link tab-link-history #{!departmentsController.currentUserCanViewDepartments ? 'current' :''}" value="li">
                                                            <h:outputText value="#{msgs['TICKET_ACTION.TAB.HISTORY.TEXT']} " />
                                                        </t:htmlTag>
                                                        <t:htmlTag id="files" styleClass="tab-link tab-link-files" value="li">
                                                            <h:outputText value="#{msgs['TICKET_ACTION.TAB.FILES.TEXT']} " />
                                                        </t:htmlTag>
                                                        <t:htmlTag id="responses" styleClass="tab-link tab-link-responses" value="li" rendered="#{ticketController.userCanUseCannedResponses and not empty ticketController.responseItems}">
                                                            <h:outputText value="#{msgs['TICKET_ACTION.TAB.RESPONSES.TEXT']} " />
                                                        </t:htmlTag>
                                                    </t:htmlTag>
                                     </t:htmlTag>
                                     <t:htmlTag id="tab-properties" styleClass="tab-content current" value="div" rendered="#{departmentsController.currentUserCanViewDepartments}">
                                                        <t:htmlTag value="div" styleClass="form-block">
                                                            <%@include file="_ticketActionScope.jsp"%>
                                                        </t:htmlTag>
                                     </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="form-item form-checkbox" rendered="#{ticketController.userCanSetNoAlert}" >
		                                 <e:selectBooleanCheckbox id="noAlert"
        		 	                        value="#{ticketController.noAlert}" />
                                         <e:outputLabel for="noAlert" value=" #{msgs['TICKET_ACTION.TEXT.NO_NOTIFICATION']}"/>
                                     </t:htmlTag>                                     
                                     <t:htmlTag id="tab-history" styleClass="tab-content view-ticket_history #{!departmentsController.currentUserCanViewDepartments ? 'current' :''}" value="div">
                                                        <%@include file="_ticketActionHistory.jsp"%>
                                     </t:htmlTag>
                                     <t:htmlTag id="tab-files" styleClass="tab-content" value="div">
                                                        <%@include file="_ticketActionTabUpload.jsp"%>
                                     </t:htmlTag>
                                     <t:htmlTag id="tab-responses" styleClass="tab-content" value="div">
                                                     <%@include file="_ticketActionResponses.jsp"%>
                                      </t:htmlTag>
                        </e:form>
	                    <%@include file="_ticketActionJavascript.jsp"%>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
    <t:htmlTag value="footer" styleClass="footer">
            <%@include file="_footer.jsp"%>
    </t:htmlTag>

</e:page>

