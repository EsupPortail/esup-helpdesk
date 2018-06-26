<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" locale="#{sessionController.locale}" >
<t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper ldap-search">
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
                            id="ldapSearchForm">
                            <t:htmlTag value="div" styleClass="message">
                                    <e:messages/>
                            </t:htmlTag>
                            <t:htmlTag value="div" styleClass="ldap-search-form">
                                <t:htmlTag value="div" styleClass="form-block form-header">
                                    <t:htmlTag value="h1">
                                        <t:htmlTag value="span" styleClass="title">
                                            <h:outputText value="#{msgs['LDAP_SEARCH.TITLE']}" escape="false" />
                                        </t:htmlTag>
                                    </t:htmlTag>
                                </t:htmlTag>
                                <t:htmlTag value="div" styleClass="form-block">
                                    <t:htmlTag value="div" styleClass="form-item">
                                            <e:inputText id="searchInput" value="#{ldapSearchController.searchInput}"
                                                             onkeypress="if (event.keyCode == 13) { simulateLinkClick('ldapSearchForm:searchButton'); return false;}" />
                                            <e:commandButton styleClass="button--secondary" id="searchButton" action="#{ldapSearchController.search}"
                                                                            value="#{msgs['LDAP_SEARCH.BUTTON.SEARCH']}" />
                                            <e:commandButton id="cancelButton" action="#{ldapSearchController.cancel}"
                                                value="#{msgs['_.BUTTON.CANCEL']}" immediate="true" />
                                            </t:htmlTag>
                                    </t:htmlTag>
                            </t:htmlTag>
                        </e:form>
                        <script type="text/javascript">
                            focusElement("ldapSearchForm:searchInput");
                        </script>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
    <t:htmlTag value="footer" styleClass="footer">
            <%@include file="_footer.jsp"%>
    </t:htmlTag>

</e:page>
