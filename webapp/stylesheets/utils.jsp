<%@include file="_include.jsp"%>
<%@ taglib uri="http://sourceforge.net/projects/jsf-comp" prefix="c" %>
<e:page stringsVar="msgs" menuItem="utils"
	locale="#{sessionController.locale}">
		   <t:htmlTag id="utils" value="div" styleClass="page-wrapper utils">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>
                    <t:htmlTag value="main" styleClass="content">
                        <t:htmlTag value="div" styleClass="content-inner">
	
                            <h:panelGroup rendered="#{utilsController.currentUser == null}" >
                                <%@include file="_auth.jsp"%>
                            </h:panelGroup>
                            <e:form
                                freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="utilsForm" rendered="#{utilsController.pageAuthorized}" >
                                 <t:htmlTag value="div" styleClass="message">
                                     <e:messages/>
                                 </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="dashboard-header">
                                        <t:htmlTag value="div" styleClass="controlPanel-title">
                                            <t:htmlTag value="h1">
                                                <t:htmlTag value="span">
                                                    <h:outputText value="#{msgs['UTILS.TITLE']}"/>
                                                </t:htmlTag>
                                            </t:htmlTag>
                                        </t:htmlTag>
                                     </t:htmlTag>
                                    <t:htmlTag value="div" styleClass="form-block utils-select">
                                        <t:htmlTag value="div" styleClass="form-item">
                                            <e:selectOneMenu id="utility" value="#{utilsController.utility}">
                                                <f:selectItems value="#{utilsController.utilitiesItems}" />
                                            </e:selectOneMenu>
                                        </t:htmlTag>
                                         <t:htmlTag value="div" styleClass="form-item" >
                                                   <e:commandButton  id="changeUtilityButton" styleClass="button--secondary"
                                                       action="#{utilsController.enter}"
                                                       value="#{msgs['_.BUTTON_OK']}" >
                                                   </e:commandButton>
                                          </t:htmlTag>
                                    </t:htmlTag>

                                    <t:htmlTag value="div" styleClass="form-block utils-ldapuid" rendered="#{utilsController.utility != 0}">
                                            <t:htmlTag value="div" styleClass="form-item">
                                                <e:outputLabel  for="ldapUid" value="#{domainService.useLdap ? msgs['UTILS.TEXT.USER_PROMPT'] : msgs['UTILS.TEXT.USER_PROMPT_NO_LDAP']} " />
                                                <e:inputText id="ldapUid" value="#{utilsController.ldapUid}"/>
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-item " >
                                                <e:commandButton rendered="#{domainService.useLdap}"
                                                    id="ldapSearchButton" action="#{ldapSearchController.firstSearch}"
                                                    value="#{msgs['_.BUTTON.LDAP']}" >
                                                    <t:updateActionListener value="#{utilsController}"
                                                        property="#{ldapSearchController.caller}" />
                                                    <t:updateActionListener value="userSelectedToUtils"
                                                        property="#{ldapSearchController.successResult}" />
                                                    <t:updateActionListener value="cancelToUtils"
                                                        property="#{ldapSearchController.cancelResult}" />
                                                </e:commandButton>
                                            </t:htmlTag>
                                             <t:htmlTag value="div" styleClass="form-item form-submit" >
                                                <e:commandButton styleClass="button--secondary"
                                                    id="testButton" action="#{utilsController.test}"
                                                    value="#{msgs['UTILS.BUTTON.TEST']}" >
                                                </e:commandButton>
                                             </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-item utils-ldapuid--info" >
                                                <e:message for="ldapUid" />
                                            </t:htmlTag>
                                    </t:htmlTag>
                                    <t:htmlTag value="div" styleClass="form-block" rendered="#{utilsController.utility == 1 and utilsController.ldapUser != null}">
                                                      <e:subSection value="#{msgs['UTILS.TEXT.LDAP.TITLE_NO_ATTRIBUTE']}"
                                                          rendered="#{empty utilsController.ldapUser.attributeNames}" >
                                                              <f:param value="#{userFormatter[utilsController.testUser]}" />
                                                      </e:subSection>
                                                      <h:panelGroup rendered="#{not empty utilsController.ldapUser.attributeNames}" >
                                                          <e:subSection value="#{msgs['UTILS.TEXT.LDAP.TITLE']}" >
                                                              <f:param value="#{utilsController.ldapUserAttributesNumber}" />
                                                              <f:param value="#{userFormatter[utilsController.testUser]}" />
                                                          </e:subSection>
                                                          <t:dataList value="#{utilsController.ldapUser.attributeNames}" var="name"
                                                              rowCountVar="namesCount" rowIndexVar="nameIndex">
                                                              <t:htmlTag value="br"
                                                                  rendered="#{nameIndex != 0}" />
                                                              <e:text value="#{name}=" />
                                                              <t:dataList var="value" value="#{utilsController.ldapUser.attributes[name]}"
                                                                  rowCountVar="valuesCount" rowIndexVar="valueIndex">
                                                                  <e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.OPEN_BRACE']}"
                                                                      rendered="#{(valueIndex==0) and (valuesCount >1)}" />
                                                                  <e:bold value="#{value}" />
                                                                  <e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.SEPARATOR']}"
                                                                      rendered="#{(valueIndex+1 < valuesCount) and (valuesCount >1)}" />
                                                                  <e:text
                                                                      value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.CLOSE_BRACE']}"
                                                                      rendered="#{(valueIndex+1==valuesCount) and (valuesCount >1)}" />
                                                              </t:dataList>
                                                          </t:dataList>
                                                      </h:panelGroup>
                                    </t:htmlTag>
                                    <t:htmlTag value="div" styleClass="form-block" rendered="#{utilsController.utility == 2 and utilsController.portalUser != null}">
                                        <e:subSection value="#{msgs['UTILS.TEXT.PORTAL.ATTRIBUTES_TITLE_NONE']}"
                                        rendered="#{empty utilsController.portalUser.attributeNames}" >
                                            <f:param value="#{userFormatter[utilsController.testUser]}" />
                                        </e:subSection>
                                        <h:panelGroup rendered="#{not empty utilsController.portalUser.attributeNames}" >
                                            <e:subSection value="#{msgs['UTILS.TEXT.PORTAL.ATTRIBUTES_TITLE']}" >
                                                <f:param value="#{utilsController.portalUserAttributesNumber}" />
                                                <f:param value="#{userFormatter[utilsController.testUser]}" />
                                            </e:subSection>
                                            <t:dataList value="#{utilsController.portalUser.attributeNames}" var="name"
                                                rowCountVar="namesCount" rowIndexVar="nameIndex">
                                                <t:htmlTag value="br"
                                                    rendered="#{nameIndex != 0}" />
                                                <e:text value="#{name}=" />
                                                <t:dataList var="value" value="#{utilsController.portalUser.attributes[name]}"
                                                    rowCountVar="valuesCount" rowIndexVar="valueIndex">
                                                    <e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.OPEN_BRACE']}"
                                                        rendered="#{(valueIndex==0) and (valuesCount >1)}" />
                                                    <e:bold value="#{value}" />
                                                    <e:text value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.SEPARATOR']}"
                                                        rendered="#{(valueIndex+1 < valuesCount) and (valuesCount >1)}" />
                                                    <e:text
                                                        value="#{msgs['LDAP_RESULTS.TEXT.ATTRIBUTES.CLOSE_BRACE']}"
                                                        rendered="#{(valueIndex+1==valuesCount) and (valuesCount >1)}" />
                                                </t:dataList>
                                            </t:dataList>
                                        </h:panelGroup>
                                        <e:subSection value="#{msgs['UTILS.TEXT.PORTAL.GROUPS_TITLE_NONE']}"
                                            rendered="#{empty utilsController.portalGroups}" >
                                                <f:param value="#{userFormatter[utilsController.testUser]}" />
                                        </e:subSection>
                                        <h:panelGroup rendered="#{not empty utilsController.portalGroups}" >
                                            <e:subSection value="#{msgs['UTILS.TEXT.PORTAL.GROUPS_TITLE']}" >
                                                <f:param value="#{utilsController.portalGroupsNumber}" />
                                                <f:param value="#{userFormatter[utilsController.testUser]}" />
                                            </e:subSection>
                                            <t:dataList value="#{utilsController.portalGroups}" var="group"
                                                rowCountVar="groupsCount" rowIndexVar="groupIndex">
                                                <e:li value="#{msgs['UTILS.TEXT.PORTAL.GROUP']}" >
                                                    <f:param value="#{group.name}" />
                                                    <f:param value="#{group.id}" />
                                                </e:li>
                                            </t:dataList>
                                        </h:panelGroup>
                                    </t:htmlTag>

                                    <t:htmlTag value="div" styleClass="form-block" rendered="#{utilsController.utility == 3 and utilsController.testUser != null}">
                                        <e:subSection value="#{msgs['UTILS.TEXT.USER_INFO.TITLE_NONE']}"
                                            rendered="#{utilsController.userInfo == null}" >
                                            <f:param value="#{userFormatter[utilsController.testUser]}" />
                                        </e:subSection>
                                        <h:panelGroup rendered="#{utilsController.userInfo != null}" >
                                            <e:subSection value="#{msgs['UTILS.TEXT.USER_INFO.TITLE']}" >
                                                <f:param value="#{userFormatter[utilsController.testUser]}" />
                                            </e:subSection>
                                            <h:outputText value="#{utilsController.userInfo}" escape="false" />
                                        </h:panelGroup>
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
