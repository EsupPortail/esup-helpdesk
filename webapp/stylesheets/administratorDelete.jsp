<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="administrators" locale="#{sessionController.locale}" authorized="#{administratorsController.currentUserCanDeleteAdmin}" >
		   <t:htmlTag id="administratorDelete" value="div" styleClass="page-wrapper administratorDelete">
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
                            id="administratorDeleteForm" >
                             <t:htmlTag value="div" styleClass="message">
                                     <e:messages/>
                             </t:htmlTag>
                            <t:htmlTag value="div" styleClass="dashboard-header">
                                        <t:htmlTag value="div" styleClass="controlPanel-title">
                                            <t:htmlTag value="h1">
                                                <t:htmlTag value="span">
                                                    <h:outputText value="#{msgs['ADMINISTRATOR_DELETE.TITLE']}"/>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:htmlTag>
                            </t:htmlTag>
                            <t:htmlTag value="div" styleClass="form-block form-body">
                                <e:text value="#{msgs['ADMINISTRATOR_DELETE.TEXT.TOP']}">
                                    <f:param value="#{userFormatter[administratorsController.userToDelete]}" />
                                </e:text>
                                <t:htmlTag value="div" styleClass="form-block">
                                        <t:htmlTag value="div" styleClass="form-item display-flex" >
                                            <e:commandButton id="confirmButton" action="#{administratorsController.confirmDeleteAdmin}"
                                                value="#{msgs['_.BUTTON.CONFIRM']}" styleClass="button--primary" />
                                            <e:commandButton id="cancelButton" action="cancel"
                                                value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
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
