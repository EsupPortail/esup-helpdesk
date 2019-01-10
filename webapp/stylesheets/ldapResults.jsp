<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" locale="#{sessionController.locale}" >

<t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper ldap-search-result">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation #{(sessionController.showShortMenu) ? 'close' : ''}">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content #{(sessionController.showShortMenu) ? 'fullSize' : ''}">
                    <t:htmlTag value="div" styleClass="content-inner">

                    <script type="text/javascript">
                        function selectUser(index) {
                            //simulateLinkClick('ldapResultsForm:data:'+index+':selectButton');
                        }
                    </script>

                    <e:form
                        freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                        showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                        showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                        id="ldapResultsForm">
                            <t:htmlTag value="div" styleClass="message">
                                    <e:messages/>
                            </t:htmlTag>
                            <t:htmlTag value="div" styleClass="ldap-search-form">
                                <t:htmlTag value="div" styleClass="form-block form-header">
                                    <t:htmlTag value="h1">
                                        <t:htmlTag value="span" styleClass="title">
                                            <h:outputText value="#{msgs['LDAP_RESULTS.TITLE']}" escape="false" />
                                        </t:htmlTag>
                                    </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-block">
                                    <e:dataTable value="#{ldapSearchController.ldapUsers}" var="ldapUser"
                                        border="0" id="data" rowIndexVar="variable" styleClass="search-result-data"
                                        rowClasses="oddRow,evenRow"
                                        cellspacing="0" cellpadding="0" width="100%">
                                        <t:column>
                                            <t:htmlTag value="div" styleClass="user-data hideme">
                                                <t:outputText value="id={#{ldapUser.id}}|" escape="false"/>
                                                <t:dataList value="#{ldapUser.attributeNames}" var="keyName"
                                                    rowCountVar="rowCountAttrs" rowIndexVar="rowIndexAttrs">
                                                    <t:outputText value="#{keyName}=" escape="true"/>
                                                    <t:dataList var="valueName" value="#{ldapUser.attributes[keyName]}"
                                                        rowCountVar="rowCount" rowIndexVar="rowIndex">
                                                        <t:outputText value="{" rendered="#{(rowIndex==0) and (rowCount >0)}" escape="false"/>
                                                        <t:outputText value="#{valueName}" escape="false"/>
                                                        <t:outputText value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.SEPARATOR']}" rendered="#{(rowIndex+1 < rowCount) and (rowCount >1)}" escape="false"/>
                                                        <t:outputText value="}" rendered="#{(rowIndex+1==rowCount) and (rowCount >0)}" escape="false"/>
                                                    </t:dataList>
                                                    <t:outputText value="|"/>
                                                </t:dataList>
                                            </t:htmlTag>

                                            <t:htmlTag value="div" styleClass="formated-user-data">
                                                <t:htmlTag value="div">
                                                    <t:htmlTag value="div" styleClass="user--display-name"/>
                                                    <t:htmlTag value="div" styleClass="user-properties">
                                                        <t:htmlTag value="div" styleClass="user--status"/>
                                                        <t:htmlTag value="div" styleClass="user--composante"/>
                                                    </t:htmlTag>
                                                </t:htmlTag>
                                                <t:htmlTag value="div" styleClass="form-item">
                                                    <e:commandButton  onclick="storeInSession('invite-user-select','true')" value="#{msgs['LDAP_SEARCH.BUTTON.USER.SELECT']}" id="selectButton" styleClass="user-select" action="#{ldapSearchController.selectUser}" >
                                                          <t:updateActionListener value="#{ldapUser}" property="#{ldapSearchController.selectedUser}" />
                                                    </e:commandButton>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:column>
                                    </e:dataTable>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-block">
                                    <t:htmlTag value="div" styleClass="form-item display-flex">
                                    <e:commandButton id="changeButton" styleClass="button--secondary" action="back" value="#{msgs['LDAP_RESULTS.BUTTON.SEARCH_AGAIN']}" immediate="true" />
                                     <e:commandButton id="cancelButton" action="#{ldapSearchController.cancel}" value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                    </t:htmlTag>
                                </t:htmlTag>



                            </t:htmlTag>








                    </e:form>

                    <script type="text/javascript">


                    </script>

                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
    <t:htmlTag value="footer" styleClass="footer">
            <%@include file="_footer.jsp"%>
    </t:htmlTag>

</e:page>
