<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="controlPanel"
	locale="#{sessionController.locale}">
	   <t:htmlTag id="page-wrapper" value="div" styleClass="page-wrapper">
	   <t:htmlTag id="header" value="header" styleClass="header">
		    <%@include file="_header.jsp"%>
		</t:htmlTag>
    	<t:htmlTag value="div" styleClass="columns">
            <t:htmlTag value="aside" styleClass="navigation">
                <%@include file="_navigation.jsp"%>
            </t:htmlTag>

            <t:htmlTag value="main" styleClass="content">
	            <t:htmlTag value="div" styleClass="content-inner">

            <script type="text/javascript">
            function formatMessage() {
              var num = arguments.length;
              var oStr = arguments[0];
              for (var i = 1; i < num; i++) {
                var pattern = "\\{" + (i-1) + "\\}";
                var re = new RegExp(pattern, "g");
                oStr = oStr.replace(re, arguments[i]);
              }
              return oStr;
            }
            </script>

            <h:outputText
                value="<script type=&quot;text/javascript&quot;>function getRefreshMessage(delay) { if (delay <= 0) return '#{msgs['CONTROL_PANEL.TEXT.REFRESHING']}'; if (delay <= 60000) return formatMessage('#{msgs['CONTROL_PANEL.TEXT.REFRESH_IN_SECONDS']}',delay/1000); return formatMessage('#{msgs['CONTROL_PANEL.TEXT.REFRESH_IN_MINUTES']}',Math.round((delay+1)/60000));}</script>"
                escape="false" />

            <script type="text/javascript">
                function selectTicket(index) {
                    simulateLinkClick('controlPanelForm:data:' + index + ':selectTicketButton');
                }
                function refreshWindow(delay) {
                    var marker = document.getElementById('controlPanelForm:data:refreshDelay');
                    if (!marker) {
                        marker = document.getElementById('controlPanelForm:refreshDelay');
                    }
                    marker.innerHTML = getRefreshMessage(delay);
                    if (delay <= 0) {
                        simulateLinkClick('controlPanelForm:filterChangeButton');
                    } else {
                        setTimeout("refreshWindow("+(delay - 10000)+")", 10000);
                    }
                }
            </script>

                <h:panelGroup rendered="#{not controlPanelController.pageAuthorized}">
                    <%@include file="_auth.jsp"%>
                </h:panelGroup>
                <e:form
                    freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                    showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                    showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                    id="controlPanelForm"
                    rendered="#{controlPanelController.pageAuthorized}">

                    <t:htmlTag value="div" styleClass="message"><e:messages/></t:htmlTag>

                    <t:htmlTag value="div" styleClass="dashboard-header">
                        <t:htmlTag value="div" styleClass="controlPanel-title">
                            <t:htmlTag value="h1">
                                <t:htmlTag value="span">
                                    <h:outputText value="#{msgs['CONTROL_PANEL.TITLE.SIMPLE']}"/>
                                </t:htmlTag>
                                <h:panelGroup rendered="#{controlPanelController.currentUserDepartmentManager}">
                                     <t:htmlTag value="span" rendered="#{controlPanelController.currentUser.controlPanelUserInterface}">
                                        <h:outputText value="#{msgs['CONTROL_PANEL.TITLE.USER']}"/>
                                     </t:htmlTag>
                                     <t:htmlTag value="span" rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}">
                                        <h:outputText value="#{msgs['CONTROL_PANEL.TITLE.MANAGER']}"/>
                                     </t:htmlTag>
                                </h:panelGroup>
                            </t:htmlTag>

                            <t:htmlTag styleClass="dashboard-toggle" value="div">
                                <h:panelGroup
                                    rendered="#{controlPanelController.currentUserDepartmentManager}">
                                    <h:panelGroup style="cursor: pointer"
                                        onclick="buttonClick('controlPanelForm:toggleInterfaceButton');"
                                        rendered="#{controlPanelController.currentUserDepartmentManager}">
                                        <t:htmlTag value="i"
                                        styleClass="#{controlPanelController.currentUser.controlPanelUserInterface ? ' fas fa-toggle-off' : ' fas fa-toggle-on'}"/>
                                    </h:panelGroup>
                                    <e:commandButton style="display: none"
                                        value="#{controlPanelController.currentUser.controlPanelUserInterface ? msgs['CONTROL_PANEL.BUTTON.SET_MANAGER_INTERFACE'] : msgs['CONTROL_PANEL.BUTTON.SET_USER_INTERFACE'] }"
                                        id="toggleInterfaceButton"
                                        action="#{controlPanelController.enter}">
                                        <t:updateActionListener
                                            value="#{not controlPanelController.currentUser.controlPanelUserInterface}"
                                            property="#{controlPanelController.currentUser.controlPanelUserInterface}" />
                                    </e:commandButton>
                                </h:panelGroup>
                            </t:htmlTag>
                        </t:htmlTag>
                         <%@include file="_ticketViewGotoTicket.jsp"%>

                    </t:htmlTag>

                    <t:htmlTag value="div" styleClass="dashboard-filter form-block">
                        <t:htmlTag value="div" styleClass="form-item" rendered="#{controlPanelController.currentUser.controlPanelUserInterface}">
                             <e:outputLabel for="controlPanelForm:userStatusFilter"
                                 value="#{msgs['CONTROL_PANEL.STATUS_FILTER.PROMPT']}"/>
                            <e:selectOneMenu id="userStatusFilter"
                                value="#{controlPanelController.currentUser.controlPanelUserStatusFilter}">
                                <f:selectItems value="#{controlPanelController.statusItems}" />
                            </e:selectOneMenu>
                        </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-item" rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}">
                            <e:outputLabel for="controlPanelForm:managerStatusFilter"
                                value="#{msgs['CONTROL_PANEL.STATUS_FILTER.PROMPT']}"/>
                            <e:selectOneMenu id="managerStatusFilter"
                                value="#{controlPanelController.currentUser.controlPanelManagerStatusFilter}">
                                <f:selectItems value="#{controlPanelController.statusItems}" />
                            </e:selectOneMenu>
                        </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-item" rendered="#{controlPanelController.currentUser.controlPanelUserInterface}">
                            <e:outputLabel for="controlPanelForm:userDepartmentFilter"
                                value="#{msgs['CONTROL_PANEL.DEPARTMENT_FILTER.PROMPT']}"/>
                             <e:selectOneMenu id="userDepartmentFilter"
                                 value="#{controlPanelController.currentUser.controlPanelUserDepartmentFilter}"
                                 converter="#{departmentConverter}">
                                 <f:selectItems
                                     value="#{controlPanelController.userDepartmentItems}" />
                             </e:selectOneMenu>
                        </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-item" rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}" >
                                <e:outputLabel for="controlPanelForm:managerDepartmentFilter"
                                    value="#{msgs['CONTROL_PANEL.DEPARTMENT_FILTER.PROMPT']}" />
                                 <e:selectOneMenu id="managerDepartmentFilter"
                                     value="#{controlPanelController.currentUser.controlPanelManagerDepartmentFilter}"
                                     converter="#{departmentConverter}" >
                                     <f:selectItems
                                         value="#{controlPanelController.managerDepartmentItems}" />
                                 </e:selectOneMenu>
                                <e:selectOneMenu id="managerCategoryFilterSpec"
                                    value="#{controlPanelController.managerCategoryFilterSpec}"
                                    rendered="#{not empty controlPanelController.managerCategorySpecItems}" >
                                    <f:selectItems
                                        value="#{controlPanelController.managerCategorySpecItems}" />
                                </e:selectOneMenu>
                        </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-item" rendered="#{controlPanelController.currentUser.controlPanelUserInterface}">
                            <e:outputLabel for="controlPanelForm:userInvolvementFilter"
                                value="#{msgs['CONTROL_PANEL.INVOLVEMENT_FILTER.PROMPT']}"/>
                             <e:selectOneMenu id="userInvolvementFilter"
                                 value="#{controlPanelController.currentUser.controlPanelUserInvolvementFilter}">
                                 <f:selectItems
                                     value="#{controlPanelController.userInvolvementItems}" />
                             </e:selectOneMenu>
                        </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-item"  rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}">
                            <e:outputLabel for="controlPanelForm:managerInvolvementFilter"
                                value="#{msgs['CONTROL_PANEL.INVOLVEMENT_FILTER.PROMPT']}"/>
                             <e:selectOneMenu id="managerInvolvementFilter"
                                 value="#{controlPanelController.currentUser.controlPanelManagerInvolvementFilter}">
                                 <f:selectItems
                                     value="#{controlPanelController.managerInvolvementItems}" />
                             </e:selectOneMenu>
                        </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-item" rendered="#{not controlPanelController.currentUser.controlPanelUserInterface}">
                                <e:outputLabel for="controlPanelForm:managerManagerFilter"
                                    value="#{msgs['CONTROL_PANEL.MANAGER_FILTER.PROMPT']}"/>
                                  <e:selectOneMenu id="managerManagerFilter"
                                      value="#{controlPanelController.paginator.selectedManager}"
                                      converter="#{userConverter}">
                                      <f:selectItems value="#{controlPanelController.managerManagerItems}" />
                                  </e:selectOneMenu>
                        </t:htmlTag>
                         <t:htmlTag value="div" styleClass="form-item" >
                                  <e:outputLabel for="controlPanelForm:pageSizeFilter"
                                                                     value="#{msgs['CONTROL_PANEL.TEXT.TICKETS_PER_PAGE']}"/>
                                   <e:selectOneMenu id="pageSizeFilter"
                                             onchange="buttonClick('controlPanelForm:filterChangeButton');"
                                             value="#{controlPanelController.currentUser.controlPanelPageSize}">
                                             <f:selectItems
                                                 value="#{controlPanelController.paginator.pageSizeItems}" />
                                   </e:selectOneMenu>
                          </t:htmlTag>
                        <t:htmlTag value="div" styleClass="form-item" >
                                  <e:commandButton  id="filter"
                                  styleClass="button--secondary"
                                      onclick="buttonClick('controlPanelForm:filterChangeButton');"
                                      value="#{msgs['CONTROL_PANEL.BUTTON.FILTER']}" >
                                  </e:commandButton>
                         </t:htmlTag>

                    </t:htmlTag>


                    <e:dataTable
                        id="data" rowIndexVar="variable"
                        value="#{controlPanelController.paginator.visibleItems}" var="cpe"
                        border="0"  cellspacing="0" cellpadding="0"
                        rendered="#{not empty controlPanelController.paginator.visibleItems}"
                        rowClasses="oddRow,evenRow"
                        styleClass="dashboard  #{controlPanelController.editColumns ? 'edit-columns':''}">

                        <f:facet name="header">

                        <t:htmlTag styleClass="dashboard-header-wrapper header-wrapper" value="div">
                             <t:htmlTag value="span" style="display:none" id="refreshDelay" />
                            <t:htmlTag styleClass="items-count" value="div" rendered="#{not empty controlPanelController.paginator.visibleItems}">
                                    <e:text value="#{msgs['CONTROL_PANEL.TEXT.TICKETS']}">
                                             <f:param value="#{controlPanelController.paginator.firstVisibleNumber + 1}" />
                                             <f:param value="#{controlPanelController.paginator.lastVisibleNumber + 1}" />
                                             <f:param value="#{controlPanelController.paginator.totalItemsCount}" />
                                    </e:text>
                            </t:htmlTag>

                            <t:htmlTag styleClass="manage-columns" value="div">
                                <t:htmlTag value="div" styleClass="actions" rendered="#{controlPanelController.editColumns}">
                                    <t:htmlTag styleClass="reset-columns" value="div" >
                                        <h:panelGroup style="cursor: pointer"
                                                    onclick="buttonClick('controlPanelForm:data:resetColumnsButton');">
                                                    <h:outputText value=" #{msgs['CONTROL_PANEL.BUTTON.RESET_COLUMNS']}" />
                                        </h:panelGroup>
                                    </t:htmlTag>
                                    <t:htmlTag styleClass="add-column" value="div" >
                                         <h:panelGroup id="addColumnPanel"
                                                rendered="#{not empty controlPanelController.addColumnItems}">
                                                <e:selectOneMenu style="cursor:pointer" value="#{controlPanelController.columnToAdd}"
                                                    onchange="buttonClick('controlPanelForm:data:addColumnButton');">
                                                    <f:selectItems value="#{controlPanelController.addColumnItems}" />
                                                </e:selectOneMenu>
                                          </h:panelGroup>
                                    </t:htmlTag>
                                </t:htmlTag>

                                <t:htmlTag styleClass="edit-columns" value="div" rendered="#{not controlPanelController.editColumns}">
                                         <e:commandButton id="editColumnsButton"
                                             value="#{msgs['CONTROL_PANEL.BUTTON.EDIT_COLUMNS']}"
                                             action="#{controlPanelController.toggleEditColumns}" />
                                         <%@include file="_controlPanelSortColumnsButtons.jsp"%>
                                </t:htmlTag>
                                <t:htmlTag styleClass="quit-edit-columns" value="div" rendered="#{controlPanelController.editColumns}">
                                         <e:commandButton id="quitEditColumnsButton"
                                             value="#{msgs['CONTROL_PANEL.BUTTON.EDIT_COLUMNS_QUIT']}"
                                             action="#{controlPanelController.toggleEditColumns}" />
                                         <%@include file="_controlPanelEditColumnsButtons.jsp"%>
                                </t:htmlTag>
                            </t:htmlTag>
                        </t:htmlTag>

                        </f:facet>
                        <t:column>
                            <f:facet name="header">
                                <t:htmlTag styleClass="actions-wrapper" value="div">
                                    <t:htmlTag styleClass="actions-header-wrapper" value="div">
                                        <t:htmlTag value="i" styleClass="fas fa-cog"/>
                                    </t:htmlTag>
                                    <t:htmlTag styleClass="actions-list-wrapper hideme" value="div">
                                        <h:panelGroup rendered="#{not empty controlPanelController.paginator.visibleItems}" >
                                             <e:commandButton
                                                 value="#{msgs['CONTROL_PANEL.BUTTON.MARK_ALL_READ']}" id="markAllReadButton"
                                                 action="#{controlPanelController.markAllRead}" />
                                        </h:panelGroup>

                                        <h:panelGroup>
                                             <e:commandButton
                                                 value="#{msgs['_.BUTTON.REFRESH']}" id="filterChangeButton"
                                                 action="#{controlPanelController.enter}" />
                                        </h:panelGroup>
                                     </t:htmlTag>
                                </t:htmlTag>

                            </f:facet>
                            <t:htmlTag styleClass="ticketControlPanel-item-actions" value="div">
                                <t:htmlTag style="cursor:pointer;" styleClass="ticketControlPanel-item-actions-header-wrapper" value="div">
                                    <t:htmlTag value="i" styleClass="fas fa-cog #{cpe.bookmarked? 'bookmarked' : ''}"/>
                                </t:htmlTag>
                                <t:htmlTag styleClass="ticketControlPanel-item-actions-wrapper hideme" value="div">
                                    <h:panelGroup rendered="#{not cpe.bookmarked}" >
                                        <h:panelGroup style="cursor: pointer" onclick="buttonClick('controlPanelForm:data:#{variable}:bookmarkTicketButton');" >
                                         	<e:text value="#{msgs['CONTROL_PANEL.BUTTON.BOOKMARK']} " />
                                         </h:panelGroup>
                                         <e:commandButton id="bookmarkTicketButton" style="display: none"
                                             value="#{msgs['CONTROL_PANEL.BUTTON.BOOKMARK']}"
                                             action="#{controlPanelController.bookmarkTicket}" immediate="true" >
                                             <t:updateActionListener value="#{cpe.ticket}"
                                                 property="#{controlPanelController.ticketToBookmark}" />
                                         </e:commandButton>
                                     </h:panelGroup>
                                     <h:panelGroup rendered="#{cpe.bookmarked}" >
                                         <h:panelGroup style="cursor: pointer" onclick="buttonClick('controlPanelForm:data:#{variable}:unbookmarkTicketButton')" >
                                          	<e:text value="#{msgs['CONTROL_PANEL.BUTTON.UNBOOKMARK']} " />
                                          </h:panelGroup>
                                         <e:commandButton id="unbookmarkTicketButton" style="display: none"
                                             value="#{msgs['CONTROL_PANEL.BUTTON.UNBOOKMARK']}"
                                             action="#{controlPanelController.unbookmarkTicket}" immediate="true" >
                                             <t:updateActionListener value="#{cpe.ticket}"
                                                 property="#{controlPanelController.ticketToBookmark}" />
                                         </e:commandButton>
                                     </h:panelGroup>
                                     <h:panelGroup rendered="#{cpe.viewed}" >
                                         <h:panelGroup style="cursor: pointer" onclick="buttonClick('controlPanelForm:data:#{variable}:markTicketUnreadButton');" >
                                          	<e:text value="#{msgs['CONTROL_PANEL.BUTTON.MARK_UNREAD']} " />
                                          </h:panelGroup>
                                         <e:commandButton id="markTicketUnreadButton" style="display: none"
                                             value="#{msgs['CONTROL_PANEL.BUTTON.MARK_UNREAD']}"
                                             action="#{controlPanelController.markTicketUnread}" immediate="true" >
                                             <t:updateActionListener value="#{cpe.ticket}"
                                                 property="#{controlPanelController.ticketToMarkReadUnread}" />
                                         </e:commandButton>
                                     </h:panelGroup>
                                     <h:panelGroup rendered="#{not cpe.viewed}" >
                                          <h:panelGroup style="cursor: pointer" onclick="buttonClick('controlPanelForm:data:#{variable}:markTicketReadButton')" >
                                           	<e:text value="#{msgs['CONTROL_PANEL.BUTTON.MARK_READ']} " />
                                           </h:panelGroup>
                                         <e:commandButton id="markTicketReadButton" style="display: none"
                                             value="#{msgs['CONTROL_PANEL.BUTTON.MARK_READ']}"
                                             action="#{controlPanelController.markTicketRead}" immediate="true" >
                                             <t:updateActionListener value="#{cpe.ticket}"
                                                 property="#{controlPanelController.ticketToMarkReadUnread}" />
                                         </e:commandButton>
                                     </h:panelGroup>
                                </t:htmlTag>
                            </t:htmlTag>
                        </t:column>

                        <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                            <f:facet name="header">
                                <t:aliasBean alias="#{columnIndex}" value="#{0}">
                                    <%@include file="_controlPanelColumnFacet.jsp"%>
                                </t:aliasBean>
                            </f:facet>
                            <t:aliasBean alias="#{columnIndex}" value="#{0}">
                                <%@include file="_controlPanelColumnContent.jsp"%>
                            </t:aliasBean>
                        </t:column>

                        <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                            <f:facet name="header">
                                <t:aliasBean alias="#{columnIndex}" value="#{1}">
                                    <%@include file="_controlPanelColumnFacet.jsp"%>
                                </t:aliasBean>
                            </f:facet>
                            <t:aliasBean alias="#{columnIndex}" value="#{1}">
                                <%@include file="_controlPanelColumnContent.jsp"%>
                            </t:aliasBean>
                        </t:column>

                        <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                            <f:facet name="header">
                                <t:aliasBean alias="#{columnIndex}" value="#{2}">
                                    <%@include file="_controlPanelColumnFacet.jsp"%>
                                </t:aliasBean>
                            </f:facet>
                            <t:aliasBean alias="#{columnIndex}" value="#{2}">
                                <%@include file="_controlPanelColumnContent.jsp"%>
                            </t:aliasBean>
                        </t:column>

                         <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                             <f:facet name="header">
                                 <t:aliasBean alias="#{columnIndex}" value="#{3}">
                                     <%@include file="_controlPanelColumnFacet.jsp"%>
                                 </t:aliasBean>
                             </f:facet>
                             <t:aliasBean alias="#{columnIndex}" value="#{3}">
                                 <%@include file="_controlPanelColumnContent.jsp"%>
                             </t:aliasBean>
                         </t:column>

                         <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                             <f:facet name="header">
                                 <t:aliasBean alias="#{columnIndex}" value="#{4}">
                                     <%@include file="_controlPanelColumnFacet.jsp"%>
                                 </t:aliasBean>
                             </f:facet>
                             <t:aliasBean alias="#{columnIndex}" value="#{4}">
                                 <%@include file="_controlPanelColumnContent.jsp"%>
                             </t:aliasBean>
                         </t:column>

                         <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                             <f:facet name="header">
                                 <t:aliasBean alias="#{columnIndex}" value="#{5}">
                                     <%@include file="_controlPanelColumnFacet.jsp"%>
                                 </t:aliasBean>
                             </f:facet>
                             <t:aliasBean alias="#{columnIndex}" value="#{5}">
                                 <%@include file="_controlPanelColumnContent.jsp"%>
                             </t:aliasBean>
                         </t:column>

                        <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                            <f:facet name="header">
                                <t:aliasBean alias="#{columnIndex}" value="#{6}">
                                    <%@include file="_controlPanelColumnFacet.jsp"%>
                                </t:aliasBean>
                            </f:facet>
                            <t:aliasBean alias="#{columnIndex}" value="#{6}">
                                <%@include file="_controlPanelColumnContent.jsp"%>
                            </t:aliasBean>
                        </t:column>

                        <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                            <f:facet name="header">
                                <t:aliasBean alias="#{columnIndex}" value="#{7}">
                                    <%@include file="_controlPanelColumnFacet.jsp"%>
                                </t:aliasBean>
                            </f:facet>
                            <t:aliasBean alias="#{columnIndex}" value="#{7}">
                                <%@include file="_controlPanelColumnContent.jsp"%>
                            </t:aliasBean>
                        </t:column>

                        <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                            <f:facet name="header">
                                <t:aliasBean alias="#{columnIndex}" value="#{8}">
                                    <%@include file="_controlPanelColumnFacet.jsp"%>
                                </t:aliasBean>
                            </f:facet>
                            <t:aliasBean alias="#{columnIndex}" value="#{8}">
                                <%@include file="_controlPanelColumnContent.jsp"%>
                            </t:aliasBean>
                        </t:column>

                         <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                             <f:facet name="header">
                                 <t:aliasBean alias="#{columnIndex}" value="#{9}">
                                     <%@include file="_controlPanelColumnFacet.jsp"%>
                                 </t:aliasBean>
                             </f:facet>
                             <t:aliasBean alias="#{columnIndex}" value="#{9}">
                                 <%@include file="_controlPanelColumnContent.jsp"%>
                             </t:aliasBean>
                         </t:column>

                         <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                             <f:facet name="header">
                                 <t:aliasBean alias="#{columnIndex}" value="#{10}">
                                     <%@include file="_controlPanelColumnFacet.jsp"%>
                                 </t:aliasBean>
                             </f:facet>
                             <t:aliasBean alias="#{columnIndex}" value="#{10}">
                                 <%@include file="_controlPanelColumnContent.jsp"%>
                             </t:aliasBean>
                         </t:column>

                          <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                              <f:facet name="header">
                                  <t:aliasBean alias="#{columnIndex}" value="#{11}">
                                      <%@include file="_controlPanelColumnFacet.jsp"%>
                                  </t:aliasBean>
                              </f:facet>
                              <t:aliasBean alias="#{columnIndex}" value="#{11}">
                                  <%@include file="_controlPanelColumnContent.jsp"%>
                              </t:aliasBean>
                          </t:column>

                        <t:column styleClass="cursor" onclick="if (#{cpe.canRead}){selectTicket('#{variable}');}">
                            <f:facet name="header">
                                <t:aliasBean alias="#{columnIndex}" value="#{12}">
                                    <%@include file="_controlPanelColumnFacet.jsp"%>
                                </t:aliasBean>
                            </f:facet>
                            <t:aliasBean alias="#{columnIndex}" value="#{12}">
                                <%@include file="_controlPanelColumnContent.jsp"%>
                            </t:aliasBean>
                        </t:column>

                        <t:column
                            styleClass="cursor"
                            headerstyle="" >
                            <e:commandButton value="#{msgs['_.BUTTON.VIEW_EDIT']}"
                                id="selectTicketButton" style="display: none"
                                action="#{controlPanelController.viewTicket}" immediate="true"
                                rendered="#{cpe.canRead}">
                                <t:updateActionListener value="#{cpe.ticket}"
                                    property="#{controlPanelController.ticketToView}" />
                            </e:commandButton>
                        </t:column>

                        <f:facet name="footer">
                                <h:panelGroup>
                                    <h:panelGroup style="display: none" rendered="#{controlPanelController.paginator.lastPageNumber != 0}">
                                             <h:panelGroup
                                                 rendered="#{not controlPanelController.paginator.firstPage}">
                                                 <e:commandButton id="pageFirst"
                                                     value="#{msgs['PAGINATION.BUTTON.FIRST']}"
                                                     action="#{controlPanelController.paginator.gotoFirstPage}" />
                                                 <e:commandButton id="pagePrevious"
                                                     value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
                                                     action="#{controlPanelController.paginator.gotoPreviousPage}" />
                                             </h:panelGroup>
                                             <h:panelGroup rendered="#{not controlPanelController.paginator.lastPage}">
                                                 <e:commandButton id="pageNext"
                                                     value="#{msgs['PAGINATION.BUTTON.NEXT']}"
                                                     action="#{controlPanelController.paginator.gotoNextPage}" />
                                                 <e:commandButton id="pageLast"
                                                     value="#{msgs['PAGINATION.BUTTON.LAST']}"
                                                     action="#{controlPanelController.paginator.gotoLastPage}" />
                                             </h:panelGroup>
                                    </h:panelGroup>
                                    <h:panelGroup styleClass="dashboard-paginator" rendered="#{not empty controlPanelController.paginator.visibleItems}">
                                        <%@include file="_controlPanelPages.jsp"%>
                                    </h:panelGroup>
                            </h:panelGroup>
                        </f:facet>
                    </e:dataTable>
                    <e:panelGrid rendered="#{empty controlPanelController.paginator.visibleItems}"
                        columnClasses="colLeft,colRight" columns="2" width="100%" >
                        <e:text value="#{msgs['CONTROL_PANEL.TEXT.NO_TICKET']}" />
                        <e:italic id="refreshDelay" />
                    </e:panelGrid>
                </e:form>

                <script type="text/javascript">
                    var previousClass = null;
            </script>

                <h:outputText
                    rendered="#{not controlPanelController.currentUser.controlPanelUserInterface and not controlPanelController.editColumns and controlPanelController.currentUser.controlPanelRefreshDelay ge 1}"
                    value="<script type=&quot;text/javascript&quot;>refreshWindow(#{controlPanelController.currentUser.controlPanelRefreshDelay}*60000);</script>"
                    escape="false" />

               </t:htmlTag>
            </t:htmlTag>

	    </t:htmlTag>
        <t:htmlTag value="footer" styleClass="footer">
            <%@include file="_footer.jsp"%>
        </t:htmlTag>
	</t:htmlTag>
</e:page>
