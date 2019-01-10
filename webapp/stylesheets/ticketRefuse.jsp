<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem=""
	locale="#{sessionController.locale}"
	authorized="#{ticketController.userCanRefuse}">
		   <t:htmlTag id="ticketRefuse" value="div" styleClass="page-wrapper ticketRefuse">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>

                    <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
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
                                                  <h:outputText value="#{msgs['TICKET_ACTION.TITLE.REFUSE']}" escape="false" />
                                            </t:htmlTag>
                                            <t:htmlTag value="span" styleClass="subtitle">
                                                <h:outputText value=" #{ticketController.ticket.id}" escape="false" />
                                            </t:htmlTag>
                                        </t:htmlTag>
                                    </t:htmlTag>
                                    <t:htmlTag value="div" styleClass="form-block form-body">
                                        <t:htmlTag value="div" styleClass="form-item">
                                            <e:outputLabel for="actionMessage" value=" #{msgs['TICKET_ACTION.TEXT.REFUSE']}"/>
                                            <fck:editor
                                                id="actionMessage"
                                                value="#{ticketController.actionMessage}"
                                                toolbarSet="actionMessage" />
                                        </t:htmlTag>
                                    </t:htmlTag>
                                    <t:htmlTag value="div" styleClass="form-block">
                                        <t:htmlTag value="div" styleClass="form-item display-flex" >
                                            <e:commandButton
                                                id="actionButton" styleClass="button--primary"
                                                value="#{msgs['TICKET_ACTION.BUTTON.REFUSE']}"
                                                action="#{ticketController.doRefuse}" />
                                            <%@include file="_ticketActionCancel.jsp"%>
                                       </t:htmlTag>
                                    </t:htmlTag>
                            </t:htmlTag>
                            <t:htmlTag styleClass="region extended-properties" value="div">
                                            <t:htmlTag styleClass="tabs" value="ul">
                                                <t:htmlTag id="spent-time" styleClass="tab-link tab-link-time #{ticketController.userCanChangeSpentTime ? 'current' :''}" value="li" rendered="#{ticketController.userCanChangeSpentTime}">
                                                    <h:outputText value="#{msgs['TICKET_ACTION.TAB.SPENT_TIME_PROMPT.TEXT']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="properties" styleClass="tab-link tab-link-info #{!ticketController.userCanChangeSpentTime ? 'current' :''}"  value="li">
                                                    <h:outputText value="#{msgs['TICKET_ACTION.TAB.GIVE_INFO.PROPERTIES.TEXT']} " />
                                                </t:htmlTag>
                                                <t:htmlTag id="history" styleClass="tab-link tab-link-history" value="li">
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
                            <t:htmlTag id="tab-spent-time" styleClass="tab-content  edit-spent-time #{ticketController.userCanChangeSpentTime ? 'current' :''}" value="div" rendered="#{ticketController.userCanChangeSpentTime}">
                                        <t:htmlTag value="div" styleClass="form-block">
                                            <%@include file="_ticketEditSpentTime.jsp"%>
                                        </t:htmlTag>
                            </t:htmlTag>
                            <t:htmlTag id="tab-properties" styleClass="tab-content #{!ticketController.userCanChangeSpentTime ? 'current' :''}" value="div">
                                        <t:htmlTag value="div" styleClass="form-block">
                                            <%@include file="_ticketActionScope.jsp"%>
                                        </t:htmlTag>
                            </t:htmlTag>
                            <t:htmlTag id="tab-history" styleClass="tab-content view-ticket_history" value="div">
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

