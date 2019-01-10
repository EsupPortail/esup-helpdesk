<%@include file="_include.jsp"%>
<%@ taglib prefix="fck" uri="http://www.fck-faces.org/fck-faces"%>
<e:page stringsVar="msgs" menuItem="responses" locale="#{sessionController.locale}" >
    <t:htmlTag id="responseEdit" value="div" styleClass="page-wrapper responseEdit">
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
                            id="responseEditForm">
                                <t:htmlTag value="div" styleClass="message">
                                                <e:messages/>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="ticket-form">
                                    <t:htmlTag value="div" styleClass="form-block form-header">
                                        <t:htmlTag value="h1">
                                            <t:htmlTag value="span" styleClass="title">
                                                  <h:outputText value="#{msgs['RESPONSE_EDIT.TITLE']}" escape="false" />
                                            </t:htmlTag>
                                        </t:htmlTag>
                                    </t:htmlTag>
                                    <t:htmlTag value="div" styleClass="form-block form-body">
                                        <t:htmlTag value="div" styleClass="form-item">
                                            <e:outputLabel for="actionMessage" value=" #{msgs['RESPONSE_EDIT.TEXT.LABEL']}"/>
                                            <e:inputText size="30" id="label" value="#{responsesController.responseToUpdate.label}"/>
                                        </t:htmlTag>

                                        <t:htmlTag value="div" styleClass="form-item">
                                            <e:outputLabel for="actionMessage" value=" #{msgs['RESPONSE_ADD.TEXT.MESSAGE']}"/>
                                            <fck:editor
                                                id="actionMessage"
                                                value="#{responsesController.responseToUpdate.message}"
                                                toolbarSet="actionMessage" />
                                        </t:htmlTag>
                                    </t:htmlTag>
                                    <t:htmlTag value="div" styleClass="form-block">
                                        <t:htmlTag value="div" styleClass="form-item display-flex" >
                                            <e:commandButton styleClass="button--primary" id="addButton"
                                            action="#{responsesController.doEditResponse}"
                                            value="#{msgs['RESPONSE_EDIT.BUTTON.EDIT_RESPONSE']}" />
                                            <e:commandButton  id="cancelButton" action="cancel" value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                       </t:htmlTag>
                                    </t:htmlTag>
                                </t:htmlTag>

                            </e:form>
                    </t:htmlTag>
                    </t:htmlTag>
                </t:htmlTag>
             <t:htmlTag value="footer" styleClass="footer">
                     <%@include file="_footer.jsp"%>
             </t:htmlTag>
        </t:htmlTag>

</e:page>
