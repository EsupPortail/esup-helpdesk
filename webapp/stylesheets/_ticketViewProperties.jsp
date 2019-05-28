<%@include file="_include.jsp"%>

<t:htmlTag styleClass="ticket-property ticket-status" value="div">
    <t:htmlTag styleClass="ticket-property-inner" value="div">
        <t:htmlTag styleClass="ticket-property_label" value="div">
              <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.STATUS']} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_lib" value="div">
            	<e:text  value="#{msgs[ticketStatusI18nKeyProvider[ticketController.ticket.status]]} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_actions" value="div">
        <t:htmlTag id="ticket-manager_actionsBis" styleClass="ticket-property_actions form-item form-select actions" value="div">
        
            <t:htmlTag styleClass="actions-header" value="div">
                            <t:htmlTag styleClass="actions-header-inner" value="div">
                                <t:htmlTag value="span"><h:outputText value="#{msgs['TICKET_VIEW.ACTIONS.HEADER']}"/></t:htmlTag>
                                <t:htmlTag value="i" styleClass="fas fa-chevron-down"/>
                             </t:htmlTag>
            </t:htmlTag>
            <t:htmlTag styleClass="actions-list hideme" value="div">
                <t:htmlTag styleClass="actions-list-inner" value="div">
                    <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.managerInfo != null}" >
                        <h:panelGroup id="showManagerInfoBis"
                            onclick="javascript:{showHideElement('viewTicketForm:managerInfoBis');showHideElement('viewTicketForm:hideManagerInfoBis');showHideElement('viewTicketForm:showManagerInfoBis')}" >
                            <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.MANAGER.PROPERTIES.SHOW']}"/>
                        </h:panelGroup>
                        <h:panelGroup id="hideManagerInfoBis"
                            style="display: none"
                            onclick="javascript:{showHideElement('viewTicketForm:managerInfoBis');showHideElement('viewTicketForm:showManagerInfoBis');showHideElement('viewTicketForm:hideManagerInfoBis')}" >
                            <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.MANAGER.PROPERTIES.HIDE']}"/>
                        </h:panelGroup>
                    </t:htmlTag>


                  <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeManager && ticketController.userCanTake}">
                      <h:panelGroup
                          onclick="buttonClick('viewTicketForm:takeButtonBis');"  >
                          <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.TAKE']}"/>
                      </h:panelGroup>
                          <e:commandButton id="takeButtonBis" style="display: none"
                              value="#{msgs['TICKET_VIEW.BUTTON.TAKE']}"
                              action="#{ticketController.take}" />
                  </t:htmlTag>

                  <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeManager && ticketController.userCanTakeAndClose}">
                      <h:panelGroup
                          onclick="buttonClick('viewTicketForm:takeAndCloseButtonBis');"  >
                          <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_CLOSE']}"/>
                      </h:panelGroup>
                          <e:commandButton id="takeAndCloseButtonBis" style="display: none"
                              value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_CLOSE']}"
                              action="#{ticketController.takeAndClose}"/>
                  </t:htmlTag>

                  <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeManager && ticketController.userCanTakeAndRequestInformation}">
                      <h:panelGroup
                          onclick="buttonClick('viewTicketForm:takeAndRequestInformationButtonBis');"  >
                          <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_REQUEST_INFORMATION']}"/>
                      </h:panelGroup>
                          <e:commandButton id="takeAndRequestInformationButtonBis" style="display: none"
                              value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_REQUEST_INFORMATION']}"
                              action="#{ticketController.takeAndRequestInformation}" />
                  </t:htmlTag>

                  <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeManager && ticketController.userCanFree}">
                      <h:panelGroup
                          onclick="buttonClick('viewTicketForm:freeButtonBis');"  >
                          <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.FREE']}"/>
                      </h:panelGroup>
                          <e:commandButton id="freeButtonBis" style="display: none"
                              value="#{msgs['TICKET_VIEW.BUTTON.FREE']}"
                              action="#{ticketController.free}" />
                  </t:htmlTag>

                  <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeManager && ticketController.userCanAssign}">
                      <h:panelGroup
                          onclick="buttonClick('viewTicketForm:assignButtonBis');"  >
                          <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.ASSIGN']}"/>
                      </h:panelGroup>
                          <e:commandButton id="assignButtonBis" style="display: none"
                              value="#{msgs['TICKET_VIEW.BUTTON.ASSIGN']}"
                              action="#{ticketController.assign}" />
                  </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_info" value="div">
            <h:panelGroup id="managerInfoBis"
                rendered="#{ticketController.managerInfo != null}"
                style="display: none">
                <e:text
                    escape="false"
                    value="#{ticketController.managerInfo}" />
            </h:panelGroup>
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_info" value="div">
            <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.RECALL_DATE']}"
                rendered="#{ticketController.ticket.status == 'POSTPONED' and ticketController.ticket.recallDate != null}"/>
            <h:panelGroup
                rendered="#{ticketController.ticket.status == 'POSTPONED' and ticketController.ticket.recallDate != null}">
                <e:bold value="{0}" >
                    <f:param value="#{ticketController.ticket.recallDate}" />
                </e:bold>
			</h:panelGroup>
        </t:htmlTag>
    </t:htmlTag>
  </t:htmlTag>
</t:htmlTag>

<t:htmlTag styleClass="ticket-property ticket-also" value="div"
        rendered="#{ticketController.userCanViewConnectionTicket or ticketController.userCanViewConnectionArchivedTicket or ticketController.userCanViewConnectionFaq}">
    <t:htmlTag styleClass="ticket-property-inner" value="div">
        <t:htmlTag styleClass="ticket-property_label" value="div">
              <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.SEE_ALSO']} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_lib" value="div"/>
        <t:htmlTag styleClass="ticket-property_actions" value="div"/>
        <t:htmlTag styleClass="ticket-property_info" value="div">
            <h:panelGroup styleClass="block " rendered="#{ticketController.userCanViewConnectionTicket}" >
                <h:panelGroup style="cursor: pointer" onclick="buttonClick('viewTicketForm:viewConnectedTicketButton');" >
                    <e:text styleClass="link" value=" #{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}" >
                        <f:param value="#{ticketController.ticket.connectionTicket.id}" />
                    </e:text>
                </h:panelGroup>
                <e:commandButton id="viewConnectedTicketButton" style="display: none"
                    value="#{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}"
                    action="viewTicket" >
                    <f:param value="#{ticketController.ticket.connectionTicket.id}" />
                    <t:updateActionListener
                        value="#{ticketController.ticket.connectionTicket}"
                        property="#{ticketController.ticket}" />
                </e:commandButton>
		    </h:panelGroup>
		    <h:panelGroup styleClass="block" rendered="#{ticketController.userCanViewConnectionArchivedTicket}" >
                <h:panelGroup style="cursor: pointer" onclick="buttonClick('viewTicketForm:viewConnectedArchivedTicketButton');" >
                    <e:text styleClass="link" value=" #{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}" >
                        <f:param value="#{ticketController.ticket.connectionArchivedTicket.ticketId}" />
                    </e:text>
                </h:panelGroup>
                <e:commandButton id="viewConnectedArchivedTicketButton" style="display: none"
                    value="#{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_TICKET']}"
                    action="viewArchivedTicket" >
                    <f:param value="#{ticketController.ticket.connectionArchivedTicket.ticketId}" />
                    <t:updateActionListener
                        value="#{ticketController.ticket.connectionArchivedTicket}"
                        property="#{archivedTicketController.archivedTicket}" />
                </e:commandButton>
		    </h:panelGroup>
		    <h:panelGroup styleClass="block" rendered="#{ticketController.userCanViewConnectionFaq}" >
                <h:panelGroup  style="cursor: pointer" onclick="buttonClick('viewTicketForm:viewConnectedFaqButton');" >
                    <e:text styleClass="link" value=" #{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_FAQ']}" >
                        <f:param value="#{ticketController.ticket.connectionFaq.label}" />
                    </e:text>
                </h:panelGroup>
                <e:commandButton id="viewConnectedFaqButton" style="display: none"
                    value="#{msgs['TICKET_VIEW.BUTTON.VIEW_CONNECTED_FAQ']}"
                    action="#{faqsController.enter}" >
                    <f:param value="#{ticketController.ticket.connectionFaq.label}" />
                    <t:updateActionListener
                        value="#{ticketController.ticket.connectionFaq}"
                        property="#{faqsController.faq}" />
                </e:commandButton>
		    </h:panelGroup>
         </t:htmlTag>
    </t:htmlTag>
</t:htmlTag>

<t:htmlTag styleClass="ticket-property ticket-category" value="div">
    <t:htmlTag styleClass="ticket-property-inner" value="div">
        <t:htmlTag styleClass="ticket-property_label" value="div">
              <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CATEGORY']} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_lib" value="div">
            <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CATEGORY_VALUE']} " title="#{ticketController.labelCategories}">
                <f:param value="#{ticketController.ticket.department.label}" />
               <f:param value="#{ticketController.ticket.category.label}" />
            </e:text>
        </t:htmlTag>
        <t:htmlTag id="ticket-category_actions" styleClass="ticket-property_actions form-item form-select actions" value="div">
            <t:htmlTag styleClass="actions-header" value="div">
                            <t:htmlTag styleClass="actions-header-inner" value="div">
                                <t:htmlTag value="span"><h:outputText value="#{msgs['TICKET_VIEW.ACTIONS.HEADER']}"/></t:htmlTag>
                                <t:htmlTag value="i" styleClass="fas fa-chevron-down"/>
                             </t:htmlTag>
            </t:htmlTag>
            <t:htmlTag styleClass="actions-list hideme" value="div">
                    <t:htmlTag styleClass="actions-list-inner" value="div">
                     <t:htmlTag styleClass="action-item" value="div"  rendered="#{ticketController.userCanMove}">
                             <h:panelGroup
                                 onclick="buttonClick('viewTicketForm:moveButton');"  >
                                 <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.MOVE']}"/>
                             </h:panelGroup>
                                <e:commandButton id="moveButton" style="display: none"
                                    value="#{msgs['TICKET_VIEW.BUTTON.MOVE']}"
                                    action="#{ticketController.move}"
                                    rendered="#{ticketController.userCanMove}" />
                     </t:htmlTag>
                     <t:htmlTag styleClass="action-item" value="div"  rendered="#{ticketController.userCanMoveBack}">
                             <h:panelGroup style="cursor: pointer"
                                 onclick="buttonClick('viewTicketForm:giveInformationMoveBackButton');"  >
                                 <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.MOVE.BACK']}"/>
                             </h:panelGroup>
                                <e:commandButton id="giveInformationMoveBackButton" style="display: none"
                                    value="#{msgs['TICKET_VIEW.BUTTON.MOVE']}"
                                    action="#{ticketController.giveInformationMoveBack}"
                                    rendered="#{ticketController.userCanMoveBack}" />
                    </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_info" value="div">
            <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.CREATION_DEPARTMENT']}"
                rendered="#{ticketController.ticket.creationDepartment != ticketController.ticket.department}" />
            <e:bold rendered="#{ticketController.ticket.creationDepartment != ticketController.ticket.department}"
                value="#{ticketController.ticket.creationDepartment.label}" />
         </t:htmlTag>
    </t:htmlTag>
</t:htmlTag>

<t:htmlTag styleClass="ticket-property ticket-owner" value="div">
    <t:htmlTag styleClass="ticket-property-inner" value="div">
        <t:htmlTag styleClass="ticket-property_label" value="div">
              	<e:text value="#{msgs['TICKET_VIEW.PROPERTIES.OWNER']} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_lib" value="div">
            <e:text value=" #{msgs['TICKET_VIEW.PROPERTIES.USER']} " >
                <f:param value="#{userFormatter[ticketController.ticket.owner]}" />
            </e:text>
        </t:htmlTag>
        <t:htmlTag id="ticket-owner_actions" styleClass="ticket-property_actions form-item form-select actions" value="div">
            <t:htmlTag styleClass="actions-header" value="div">
                            <t:htmlTag styleClass="actions-header-inner" value="div">
                                <t:htmlTag value="span"><h:outputText value="#{msgs['TICKET_VIEW.ACTIONS.HEADER']}"/></t:htmlTag>
                                <t:htmlTag value="i" styleClass="fas fa-chevron-down"/>
                             </t:htmlTag>
            </t:htmlTag>
            <t:htmlTag styleClass="actions-list hideme" value="div">
                <t:htmlTag styleClass="actions-list-inner" value="div">
                        <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.ownerInfo != null}" >
                            <h:panelGroup id="showOwnerInfo"
                                onclick="javascript:{showHideElement('viewTicketForm:ownerInfo');showHideElement('viewTicketForm:hideOwnerInfo');showHideElement('viewTicketForm:showOwnerInfo')}" >
                                <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.OWNER.PROPERTIES.SHOW']}"/>
                            </h:panelGroup>
                            <h:panelGroup id="hideOwnerInfo"
                                style="display: none"
                                onclick="javascript:{showHideElement('viewTicketForm:ownerInfo');showHideElement('viewTicketForm:showOwnerInfo');showHideElement('viewTicketForm:hideOwnerInfo')}" >
                                <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.OWNER.PROPERTIES.HIDE']}"/>
                            </h:panelGroup>
                        </t:htmlTag>
                     <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeOwner}">
                        <h:panelGroup
                            onclick="buttonClick('viewTicketForm:changeOwnerButton');"
                            rendered="#{ticketController.userCanChangeOwner}">
                            <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_OWNER']}"/>
                        </h:panelGroup>
                        <e:commandButton id="changeOwnerButton"
                            rendered="#{ticketController.userCanChangeOwner}"
                            value="#{msgs['TICKET_VIEW.BUTTON.CHANGE_OWNER']}"
                            action="#{ticketController.changeOwner}"
                            style="display:none" />
                     </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_info" value="div">
                <h:panelGroup id="ownerInfo"
                    rendered="#{ticketController.ownerInfo != null}"
                    style="display: none">
                    <e:text
                        escape="false"
                        value="#{ticketController.ownerInfo}" />
                </h:panelGroup>
        </t:htmlTag>
    </t:htmlTag>
</t:htmlTag>

<t:htmlTag styleClass="ticket-property ticket-manager" value="div">
    <t:htmlTag styleClass="ticket-property-inner" value="div">
        <t:htmlTag styleClass="ticket-property_label" value="div">
              <e:text value="#{msgs['TICKET_VIEW.PROPERTIES.MANAGER']} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_lib" value="div">
                <h:panelGroup rendered="#{ticketController.ticket.manager != null}" >
					<e:text value=" #{msgs['TICKET_VIEW.PROPERTIES.USER']} " rendered="#{!ticketController.ticket.anonymous}">
						<f:param value="#{userFormatter[ticketController.ticket.manager]}" />
					</e:text>
					<e:text value=" #{msgs['TICKET_VIEW.PROPERTIES.USER']} " rendered="#{ticketController.ticket.anonymous && controlPanelController.managerDpt}">
						<f:param value="#{userFormatter[ticketController.ticket.manager]}" />
					</e:text>
					<e:text value=" #{msgs['TICKET_VIEW.PROPERTIES.USER']} " rendered="#{ticketController.ticket.anonymous && !controlPanelController.managerDpt}">
						<f:param value="#{msgs['USER.ANONYMOUS']}" />
					</e:text>
				</h:panelGroup>
				<e:italic
					rendered="#{ticketController.ticket.manager == null}"
					value=" #{msgs['TICKET_VIEW.PROPERTIES.NO_MANAGER']} " />
        </t:htmlTag>
        <t:htmlTag id="ticket-manager_actions" styleClass="ticket-property_actions form-item form-select actions" value="div">
            <t:htmlTag styleClass="actions-header" value="div">
                            <t:htmlTag styleClass="actions-header-inner" value="div">
                                <t:htmlTag value="span"><h:outputText value="#{msgs['TICKET_VIEW.ACTIONS.HEADER']}"/></t:htmlTag>
                                <t:htmlTag value="i" styleClass="fas fa-chevron-down"/>
                             </t:htmlTag>
            </t:htmlTag>
            <t:htmlTag styleClass="actions-list hideme" value="div">
                <t:htmlTag styleClass="actions-list-inner" value="div">



                          <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.managerInfo != null}" >
                              <h:panelGroup id="showManagerInfo"
                                  onclick="javascript:{showHideElement('viewTicketForm:managerInfo');showHideElement('viewTicketForm:hideManagerInfo');showHideElement('viewTicketForm:showManagerInfo')}" >
                                  <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.MANAGER.PROPERTIES.SHOW']}"/>
                              </h:panelGroup>
                              <h:panelGroup id="hideManagerInfo"
                                  style="display: none"
                                  onclick="javascript:{showHideElement('viewTicketForm:managerInfo');showHideElement('viewTicketForm:showManagerInfo');showHideElement('viewTicketForm:hideManagerInfo')}" >
                                  <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.MANAGER.PROPERTIES.HIDE']}"/>
                              </h:panelGroup>
                          </t:htmlTag>


                        <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeManager && ticketController.userCanTake}">
                            <h:panelGroup
                                onclick="buttonClick('viewTicketForm:takeButton');"  >
                                <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.TAKE']}"/>
                            </h:panelGroup>
                                <e:commandButton id="takeButton" style="display: none"
                                    value="#{msgs['TICKET_VIEW.BUTTON.TAKE']}"
                                    action="#{ticketController.take}" />
                        </t:htmlTag>

                        <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeManager && ticketController.userCanTakeAndClose}">
                            <h:panelGroup
                                onclick="buttonClick('viewTicketForm:takeAndCloseButton');"  >
                                <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_CLOSE']}"/>
                            </h:panelGroup>
                                <e:commandButton id="takeAndCloseButton" style="display: none"
                                    value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_CLOSE']}"
                                    action="#{ticketController.takeAndClose}"/>
                        </t:htmlTag>

                        <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeManager && ticketController.userCanTakeAndRequestInformation}">
                            <h:panelGroup
                                onclick="buttonClick('viewTicketForm:takeAndRequestInformationButton');"  >
                                <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_REQUEST_INFORMATION']}"/>
                            </h:panelGroup>
                                <e:commandButton id="takeAndRequestInformationButton" style="display: none"
                                    value="#{msgs['TICKET_VIEW.BUTTON.TAKE_AND_REQUEST_INFORMATION']}"
                                    action="#{ticketController.takeAndRequestInformation}" />
                        </t:htmlTag>

                        <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeManager && ticketController.userCanFree}">
                            <h:panelGroup
                                onclick="buttonClick('viewTicketForm:freeButton');"  >
                                <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.FREE']}"/>
                            </h:panelGroup>
                                <e:commandButton id="freeButton" style="display: none"
                                    value="#{msgs['TICKET_VIEW.BUTTON.FREE']}"
                                    action="#{ticketController.free}" />
                        </t:htmlTag>

                        <t:htmlTag styleClass="action-item" value="div" rendered="#{ticketController.userCanChangeManager && ticketController.userCanAssign}">
                            <h:panelGroup
                                onclick="buttonClick('viewTicketForm:assignButton');"  >
                                <h:outputText value="#{msgs['TICKET_VIEW.BUTTON.ASSIGN']}"/>
                            </h:panelGroup>
                                <e:commandButton id="assignButton" style="display: none"
                                    value="#{msgs['TICKET_VIEW.BUTTON.ASSIGN']}"
                                    action="#{ticketController.assign}" />
                        </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_info" value="div">
            <h:panelGroup id="managerInfo"
                rendered="#{ticketController.managerInfo != null}"
                style="display: none">
                <e:text
                    escape="false"
                    value="#{ticketController.managerInfo}" />
            </h:panelGroup>
         </t:htmlTag>
    </t:htmlTag>
</t:htmlTag>
<t:htmlTag styleClass="ticket-property ticket-status" value="div" rendered="#{ticketController.ticket.scope == 'PUBLIC'}">
    <t:htmlTag styleClass="ticket-property-inner" value="div">
        <t:htmlTag styleClass="ticket-property_label" value="div">
              <e:text value="#{msgs['TICKET_ACTION.TEXT.SCOPE_PROMPT']} " />
        </t:htmlTag>
        <t:htmlTag styleClass="ticket-property_lib" value="div">
            	<e:text  value="#{msgs[ticketScopeI18nKeyProvider[ticketController.ticket.scope]]} " />
        </t:htmlTag>
    </t:htmlTag>
</t:htmlTag>
