<%@include file="_include.jsp"%>



<e:dataTable  id="actionData" styleClass="history-container" width="100%"
	value="#{ticketController.historyEntries}" rowIndexVar="variable" 
	var="he" border="0" cellspacing="0" cellpadding="0" rowClasses="oddRow,evenRow">

	<t:column styleClass="col-main">
        <f:facet name="header">
             <h:outputText value="" />
        </f:facet>
        <t:htmlTag styleClass="action--header" value="div">
            		<h:outputText value="#{actionI18nTitleProvider[he.action]}" />
        </t:htmlTag>
        <t:htmlTag rendered="#{he.action.message != null && he.canView}" styleClass="action--content #{he.action.user == null and he.canView ? 'hideme' : ''}" value="div">
            <t:htmlTag styleClass="action-message" value="div">
                <h:outputText escape="false"  value="#{he.action.message}"/>
            </t:htmlTag>
        </t:htmlTag>
	</t:column>
	<t:column styleClass="col-main action--scope">
		   <f:facet name="header">
		        <t:htmlTag value="div" >
                    <h:outputText value="#{msgs['DOMAIN.ACTION_SCOPE.COL.HEADER']}"/>
                 </t:htmlTag>
            </f:facet>
        <t:htmlTag styleClass="action-inner" value="div">
         
                                <t:htmlTag  rendered="#{he.action.scope == 'DEFAULT' 
                                && ticketController.ticket.scope == 'DEFAULT' 
                                && ( ticketController.departmentDefaultTicketScope == 'PUBLIC' || ticketController.departmentDefaultTicketScope == 'CAS') }"  value="div" styleClass="action-scope--default">
                                      <h:panelGroup  rendered="#{not he.canChangeScope}">
                                             <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.PUBLIC']}"/>
                                      </h:panelGroup>
                                      <h:panelGroup  styleClass="action-scope-edit default link" rendered="#{he.canChangeScope}">
                                            <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.PUBLIC']}"/>
                                      </h:panelGroup>
                                </t:htmlTag>
                                <t:htmlTag  rendered="#{he.action.scope == 'DEFAULT' 
                                && ( ticketController.ticket.scope == 'PUBLIC' || ticketController.ticket.scope == 'CAS') }"  value="div" styleClass="action-scope--default">
                                      <h:panelGroup  rendered="#{not he.canChangeScope}">
                                             <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.PUBLIC']}"/>
                                      </h:panelGroup>
                                      <h:panelGroup  styleClass="action-scope-edit default link" rendered="#{he.canChangeScope}">
                                            <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.PUBLIC']}"/>
                                      </h:panelGroup>
                                </t:htmlTag>
                                <t:htmlTag  rendered="#{he.action.scope == 'DEFAULT' 
                                 && ( ticketController.ticket.scope == 'SUBJECT_ONLY' || ticketController.ticket.scope == 'PRIVATE') }"  value="div" styleClass="action-scope--default">
                                      <h:panelGroup  rendered="#{not he.canChangeScope}">
                                             <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED']}"/>
                                      </h:panelGroup>
                                      <h:panelGroup  styleClass="action-scope-edit default link" rendered="#{he.canChangeScope}">
                                            <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED']}"/>
                                      </h:panelGroup>
                                </t:htmlTag>                                
                                <t:htmlTag  rendered="#{he.action.scope == 'INVITED'}"  value="div" styleClass="action-scope--invited">
                                      <h:panelGroup   rendered="#{not he.canChangeScope}">
                                              <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED']}"/>
                                      </h:panelGroup>
                                      <h:panelGroup  styleClass="action-scope-edit invited link" rendered="#{he.canChangeScope}">
                                            <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED']}"/>
                                      </h:panelGroup>
                                </t:htmlTag>
                                <t:htmlTag  rendered="#{he.action.scope == 'PUBLIC'}"  value="div" styleClass="action-scope--invited">
                                      <h:panelGroup   rendered="#{not he.canChangeScope}">
                                              <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.PUBLIC']}"/>
                                      </h:panelGroup>
                                      <h:panelGroup  styleClass="action-scope-edit invited link" rendered="#{he.canChangeScope}">
                                            <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.PUBLIC']}"/>
                                      </h:panelGroup>
                                </t:htmlTag>                                
                                <t:htmlTag  rendered="#{he.action.scope == 'INVITED_MANAGER'}"  value="div" styleClass="action-scope--invited-manager">
                                      <h:panelGroup   rendered="#{not he.canChangeScope}">
                                          <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED_MANAGER']}"/>
                                      </h:panelGroup>
                                      <h:panelGroup  styleClass="action-scope-edit invited-manager link"  rendered="#{he.canChangeScope}">
                                          <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.INVITED_MANAGER']}"/>
                                      </h:panelGroup>
                                </t:htmlTag>
                                 <t:htmlTag rendered="#{he.action.scope == 'OWNER'}"  value="div" styleClass="action-scope--owner">
                                      <h:panelGroup   rendered="#{not he.canChangeScope}">
                                             <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.OWNER']}"/>
                                      </h:panelGroup>
                                       <h:panelGroup  styleClass="action-scope-edit owner link"  rendered="#{he.canChangeScope}">
                                             <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.OWNER']}"/>
                                       </h:panelGroup>
                                 </t:htmlTag>
                                <t:htmlTag  rendered="#{he.action.scope == 'MANAGER'}"  value="div" styleClass="action-scope--manager">
                                      <h:panelGroup   rendered="#{not he.canChangeScope}">
                                            <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.MANAGER']}"/>
                                      </h:panelGroup>
                                      <h:panelGroup  styleClass="action-scope-edit manager link"  rendered="#{he.canChangeScope}">
                                            <h:outputText escape="false" value="#{msgs['DOMAIN.ACTION_SCOPE.MANAGER']}"/>
                                      </h:panelGroup>
                                </t:htmlTag>

                                <h:panelGroup id="editScope" rendered="#{he.canChangeScope}" style="display: none" styleClass="form-item form-action ">
                                    <e:selectOneMenu id="actionScope" value="#{he.newScope}" >
                                        <f:selectItems value="#{ticketController.actionScopeItems}" />
                                    </e:selectOneMenu>
                                     <t:htmlTag value="div" >
                                        <e:commandButton
                                            id="changeActionScopeButton"
                                            value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_SCOPE']}"
                                            action="#{ticketController.doUpdateActionScope}" styleClass="button--primary">
                                            <t:updateActionListener value="#{he.action}" property="#{ticketController.actionToUpdate}" />
                                            <t:updateActionListener value="#{he.newScope}" property="#{ticketController.actionScopeToSet}" />
                                        </e:commandButton>
                                        <h:panelGroup styleClass="cancel" onclick="showHideElementById('viewTicketForm:actionData:'+#{variable}+':editScope'); ">
                                            <h:outputText value=" #{msgs['_.BUTTON.CANCEL']} "/>
                                         </h:panelGroup>
                                      </t:htmlTag>
                                </h:panelGroup>
                    </t:htmlTag>

	</t:column>
	<t:column styleClass="col-option action--edit hideme">
		   <f:facet name="header">
                 <h:outputText value=""/>
            </f:facet>
             <t:htmlTag value="div" rendered="#{he.canUpdateInformation}">
                <h:panelGroup onclick="buttonClick('viewTicketForm:actionData:'+#{variable}+':updateInformation');" title="#{msgs['CHANGE_COMMENT']}">
                    <t:htmlTag style="cursor:pointer" value="i" styleClass="far fa-edit fa-2x" />
                 </h:panelGroup>
            </t:htmlTag>
            <e:commandButton
                id="updateInformation"
                style="display: none"
                value="#{msgs['_.BUTTON.VIEW_EDIT']}"
                action="#{ticketController.updateInformation}">
                <t:updateActionListener value="#{he.action}" property="#{ticketController.actionToUpdate}" />
                <t:updateActionListener value="#{he.action.message}" property="#{ticketController.actionMessage}" />
            </e:commandButton>
    </t:column>


    <t:column styleClass="col-option action--alert hideme">

            <h:panelGroup styleClass="action-show-alert" rendered="#{not empty he.alerts}" >
                <h:panelGroup rendered="#{not ticketController.showAlerts and not empty he.alerts}" title="#{msgs['ALERT_SENDING']}">
                    <t:htmlTag style="cursor:pointer" value="i" styleClass="far fa-envelope fa-2x"/>
                 </h:panelGroup>
            </h:panelGroup>
            <h:panelGroup styleClass="action-alert-recipient hideme" rendered="#{not empty he.alerts}" >
                <t:htmlTag value="div">
                    <e:bold value=" #{msgs['TICKET_VIEW.HISTORY.ALERTS_PREFIX']}" />
                </t:htmlTag>
                <t:dataList value="#{he.alerts}" var="alert" rowIndexVar="index">
                    <e:text value=" " rendered="#{index == 0}" />
                    <t:htmlTag value="div">
                        <e:text rendered="#{alert.user != null}" value=" #{userFormatter[alert.user]}" />
                        <e:text rendered="#{alert.user == null}" value=" #{alert.email}" />
                    </t:htmlTag>
                </t:dataList>

            </h:panelGroup>
	</t:column>
</e:dataTable>				
