<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="welcome"
	locale="#{sessionController.locale}">
	   <t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper welcome">
	   <t:htmlTag id="header" value="header" styleClass="header">
		    <%@include file="_header.jsp"%>
		</t:htmlTag>
    	<t:htmlTag value="div" styleClass="columns">
            <t:htmlTag value="aside" styleClass="navigation">
                <%@include file="_navigation.jsp"%>
            </t:htmlTag>

            <t:htmlTag value="main" styleClass="content">
	            <t:htmlTag value="div" styleClass="content-inner">

	                <t:htmlTag value="div" styleClass="region" rendered="#{welcomeController.currentUser != null}">
                        <e:form
                        freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                        showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                        showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                        id="welcomeForm" >

                         <t:htmlTag value="div" styleClass="region-inner">
                            <h:panelGroup  styleClass="block block--card cursor--pointer block--actif"  onclick="buttonClick('welcomeForm:addTicketButton');" >
                              <t:htmlTag value="i" styleClass="fas fa-edit"/>
                              <h:outputText  value="#{msgs['TICKET_ACTION.TITLE.ADD']}"/>
                             <e:commandButton id="addTicketButton" action="#{ticketController.add}" style="display: none"
                                                    value="#{msgs['WELCOME.BUTTON.ADD_TICKET']}"  />
                            </h:panelGroup>

                            <h:panelGroup id="faqLink"  styleClass="block block--card cursor--pointer" onclick="buttonClick('welcomeForm:faqsButton');"
                                rendered="#{welcomeController.showFaqLink}" >
                              <t:htmlTag value="i" styleClass="fas fa-question-circle"/>
                              <e:text value="#{msgs['WELCOME.BUTTON.FAQS']}"/>
                              <e:commandButton id="faqsButton" action="#{faqsController.enter}" style="display: none"
                                        value="#{msgs['WELCOME.BUTTON.FAQS']}" immediate="true" />
                            </h:panelGroup>

                            <h:panelGroup id="dashboardLink" styleClass="block block--card cursor--pointer" onclick="buttonClick('welcomeForm:controlPanelButton');" >
                              <t:htmlTag value="i" styleClass="fas fa-tasks"/>
                              <e:text value="#{msgs['WELCOME.BUTTON.CONTROL_PANEL']}"/>
                            <e:commandButton id="controlPanelButton" action="#{controlPanelController.enter}" style="display: none"
                                        value="#{msgs['WELCOME.BUTTON.CONTROL_PANEL']}" immediate="true" />
                            </h:panelGroup>

                            <h:panelGroup id="searchLink" styleClass="block block--card cursor--pointer"  onclick="buttonClick('welcomeForm:searchButton');" >
                              <t:htmlTag value="i" styleClass="fas fa-search"/>
                              <e:text value="#{msgs['WELCOME.BUTTON.SEARCH']}"/>
                              <e:commandButton id="searchButton" action="#{searchController.enter}" style="display: none"
                                        value="#{msgs['WELCOME.BUTTON.SEARCH']}" immediate="true" />
                            </h:panelGroup>
                          </t:htmlTag>
                        </e:form>
                    </t:htmlTag>
                    <t:htmlTag value="div" styleClass="region" rendered="#{welcomeController.currentUser == null}">
                        <%@include file="_auth.jsp"%>
                    </t:htmlTag>

	            </t:htmlTag>
            </t:htmlTag>

	</t:htmlTag>
	<t:htmlTag value="footer" styleClass="footer">
	    <t:aliasBean alias="#{controller}" value="#{welcomeController}" >
		    <%@include file="_footer.jsp"%>
		</t:aliasBean>
	</t:htmlTag>
	</t:htmlTag>
</e:page>

