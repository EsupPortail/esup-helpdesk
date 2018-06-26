<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="bookmarks"
	locale="#{sessionController.locale}">

<script type="text/javascript">
	function selectBookmarkTicket(index) {
		buttonClick('bookmarksForm:bookmarkData:'+index+':viewTicketButton');
		buttonClick('bookmarksForm:bookmarkData:'+index+':viewArchivedTicketButton');
	}
	function selectHistoryTicket(index) {
		buttonClick('bookmarksForm:historyData:'+index+':viewTicketButton');
		buttonClick('bookmarksForm:historyData:'+index+':viewArchivedTicketButton');
	}
</script>
	   <t:htmlTag id="bookmarks" value="div" styleClass="page-wrapper bookmarks">
           <t:htmlTag id="header" value="header" styleClass="header">
                <%@include file="_header.jsp"%>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="columns">
                <t:htmlTag value="aside" styleClass="navigation">
                    <%@include file="_navigation.jsp"%>
                </t:htmlTag>

                <t:htmlTag value="main" styleClass="content">
                    <t:htmlTag value="div" styleClass="content-inner">

                    <h:panelGroup rendered="#{not bookmarksController.pageAuthorized}">
                        <%@include file="_auth.jsp"%>
                    </h:panelGroup>

                    <e:form
                        freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                        showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                        showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                        id="bookmarksForm"
                        rendered="#{bookmarksController.pageAuthorized}">
                            <t:htmlTag value="div" styleClass="message">
                                    <e:messages/>
                            </t:htmlTag>
                            <t:htmlTag styleClass="region" value="div">
                                <t:htmlTag styleClass="tabs" value="ul">
                                    <t:htmlTag id="bookmarks" styleClass="tab-link current" value="li">
                                        <h:outputText value="#{msgs['BOOKMARKS.BOOKMARKS.TITLE']} " />
                                    </t:htmlTag>
                                    <t:htmlTag id="tickets-history" styleClass="tab-link history" value="li">
                                        <t:htmlTag value="span"><h:outputText value="#{msgs['BOOKMARKS.HISTORY.TITLE']}" /></t:htmlTag>
                                        <t:htmlTag styleClass="actions-wrapper" value="div" rendered="#{not empty bookmarksController.historyItemEntries}">
                                            <t:htmlTag styleClass="actions-header-wrapper" value="div">
                                                <t:htmlTag value="i" styleClass="fas fa-cog"/>
                                            </t:htmlTag>
                                            <t:htmlTag styleClass="actions-list-wrapper hideme" value="div">
                                                <h:panelGroup>
                                                     <e:commandButton
                                                         id="clearButton"
                                                         value=" #{msgs['BOOKMARKS.HISTORY.BUTTON.CLEAR']}"
                                                         action="#{bookmarksController.clearHistory}" />
                                                </h:panelGroup>
                                             </t:htmlTag>
                                        </t:htmlTag>
                                    </t:htmlTag>

                                </t:htmlTag>
                            </t:htmlTag>
                            <t:htmlTag id="tab-bookmarks" styleClass="tab-content current" value="div">
                                <e:dataTable
                                    id="bookmarkData" rowIndexVar="variable"
                                    value="#{bookmarksController.bookmarkEntries}" var="be"
                                    border="0" style="width:100%" cellspacing="0" cellpadding="0"
                                    rendered="#{not empty bookmarksController.bookmarkEntries}">


                                    <t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" onclick="selectBookmarkTicket(#{variable});">
                                        <f:facet name="header">
                                            <e:text value="#{msgs['BOOKMARKS.BOOKMARKS.ENTRY.NUMBER.LIB']}" />
                                        </f:facet>
                                        <h:panelGroup rendered="#{be.bookmark.ticketBookmark}">
                                                <h:outputText value="#{be.bookmark.ticket.id} " />
                                        </h:panelGroup>
                                        <h:panelGroup rendered="#{be.bookmark.archivedTicketBookmark}">
                                                <h:outputText value="#{be.bookmark.archivedTicket.ticketId} " />
                                        </h:panelGroup>
                                    </t:column>

                                     <t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" onclick="selectBookmarkTicket(#{variable});">
                                         <h:panelGroup rendered="#{be.bookmark.ticketBookmark}">
                                                 <h:outputText value="#{be.bookmark.ticket.label} " />
                                         </h:panelGroup>
                                         <h:panelGroup rendered="#{be.bookmark.archivedTicketBookmark}">
                                                 <h:outputText value="#{be.bookmark.archivedTicket.label} " />
                                         </h:panelGroup>
                                     </t:column>

                                    <t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" onclick="selectBookmarkTicket(#{variable});">
                                        <f:facet name="header">
                                            <e:bold value="#{msgs['BOOKMARKS.BOOKMARKS.HEADER.DEPARTMENT']}" />
                                        </f:facet>
                                        <e:text rendered="#{be.bookmark.ticketBookmark}"
                                            value="#{be.bookmark.ticket.department.label}" />
                                        <e:text rendered="#{be.bookmark.archivedTicketBookmark}"
                                            value="#{be.bookmark.archivedTicket.department.label}" />
                                    </t:column>
                                    <t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" onclick="selectBookmarkTicket(#{variable});">
                                        <f:facet name="header">
                                            <e:bold value="#{msgs['BOOKMARKS.BOOKMARKS.HEADER.OWNER']}" />
                                        </f:facet>
                                        <h:panelGroup>
                                            <e:text rendered="#{be.bookmark.ticketBookmark}"
                                                value="#{userFormatter[be.bookmark.ticket.owner]}" />
                                        </h:panelGroup>
                                        <h:panelGroup>
                                            <e:text rendered="#{be.bookmark.archivedTicketBookmark}"
                                                value="#{userFormatter[be.bookmark.archivedTicket.owner]}" />
                                        </h:panelGroup>
                                    </t:column>
                                    <t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" onclick="selectBookmarkTicket(#{variable});">
                                        <f:facet name="header">
                                            <e:bold value="#{msgs['BOOKMARKS.BOOKMARKS.HEADER.MANAGER']}" />
                                        </f:facet>
                                        <h:panelGroup>
                                            <e:text rendered="#{be.bookmark.ticketBookmark and be.bookmark.ticket.manager != null}"
                                                value="#{userFormatter[be.bookmark.ticket.manager]}" />
                                        </h:panelGroup>
                                        <h:panelGroup>
                                            <e:text rendered="#{be.bookmark.archivedTicketBookmark and be.bookmark.archivedTicket.manager != null}"
                                                value="#{userFormatter[be.bookmark.archivedTicket.manager]}" />
                                        </h:panelGroup>
                                    </t:column>
                                    <t:column style="cursor: pointer" >
                                        <h:panelGroup onclick="buttonClick('bookmarksForm:bookmarkData:#{variable}:deleteBookmarkButton');" >
                                            <t:htmlTag value="i" styleClass="fas fa-trash-alt fa-2x"/>
                                        </h:panelGroup>
                                        <e:commandButton style="display: none" id="deleteBookmarkButton"
                                            action="#{bookmarksController.deleteBookmark}" >
                                            <t:updateActionListener value="#{be.bookmark}" property="#{bookmarksController.bookmarkToDelete}" />
                                        </e:commandButton>
                                    </t:column>
                                    <t:column style="cursor: #{be.canRead ? 'pointer' : 'default'}" >
                                        <h:panelGroup style="display: none" rendered="#{be.canRead}" >
                                            <e:commandButton id="viewTicketButton"
                                                rendered="#{be.bookmark.ticketBookmark}"
                                                action="viewTicket" immediate="true">
                                                <t:updateActionListener value="#{be.bookmark.ticket}"
                                                    property="#{ticketController.updatedTicket}" />
                                                <t:updateActionListener value="bookmarks"
                                                    property="#{ticketController.backPage}" />
                                            </e:commandButton>
                                            <e:commandButton id="viewArchivedTicketButton"
                                                rendered="#{be.bookmark.archivedTicketBookmark}"
                                                action="viewArchivedTicket" immediate="true">
                                                <t:updateActionListener value="#{be.bookmark.archivedTicket}"
                                                    property="#{archivedTicketController.archivedTicket}" />
                                                <t:updateActionListener value="bookmarks"
                                                    property="#{archivedTicketController.backPage}" />
                                            </e:commandButton>
                                        </h:panelGroup>
                                    </t:column>
                                </e:dataTable>
                                <e:paragraph value="#{msgs['BOOKMARKS.BOOKMARKS.TEXT.NO_BOOKMARK']}" rendered="#{empty bookmarksController.bookmarkEntries}" />
                            </t:htmlTag>

                            <t:htmlTag id="tab-tickets-history" styleClass="tab-content" value="div">

                                <e:dataTable
                                    id="historyData" rowIndexVar="variable"
                                    value="#{bookmarksController.historyItemEntries}" var="hie"
                                    border="0" style="width:100%" cellspacing="0" cellpadding="0"
                                    rendered="#{not empty bookmarksController.historyItemEntries}"
                                    columnClasses="colLeftNowrap,colLeft,colLeft,colLeft">

                                    <t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
                                        <f:facet name="header">
                                            <e:text value="#{msgs['BOOKMARKS.BOOKMARKS.ENTRY.NUMBER.LIB']}" />
                                        </f:facet>
                                        <h:panelGroup rendered="#{hie.historyItem.ticketHistoryItem}">
                                                <h:outputText value="#{hie.historyItem.ticket.id} " />
                                        </h:panelGroup>
                                        <h:panelGroup rendered="#{hie.historyItem.archivedTicketHistoryItem}">
                                                <h:outputText value="#{hie.historyItem.archivedTicket.ticketId} " />
                                        </h:panelGroup>
                                    </t:column>

                                     <t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
                                         <h:panelGroup rendered="#{hie.historyItem.ticketHistoryItem}">
                                                 <h:outputText value="#{hie.historyItem.ticket.label}" />
                                         </h:panelGroup>
                                         <h:panelGroup rendered="#{hie.historyItem.archivedTicketHistoryItem}">
                                                 <h:outputText value="#{hie.historyItem.archivedTicket.label}" />
                                         </h:panelGroup>
                                     </t:column>


                                    <t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">

                                        <h:panelGroup rendered="#{hie.historyItem.ticketHistoryItem}" >
                                            <e:text value="#{msgs['BOOKMARKS.HISTORY.ENTRY.TICKET']}" >
                                                <f:param value="#{hie.historyItem.ticket.id}" />
                                                <f:param value="#{hie.historyItem.ticket.label}" />
                                            </e:text>
                                        </h:panelGroup>
                                        <h:panelGroup rendered="#{hie.historyItem.archivedTicketHistoryItem}" >
                                            <e:text value="#{msgs['BOOKMARKS.HISTORY.ENTRY.ARCHIVED_TICKET']}" >
                                                <f:param value="#{hie.historyItem.archivedTicket.ticketId}" />
                                                <f:param value="#{hie.historyItem.archivedTicket.label}" />
                                            </e:text>
                                        </h:panelGroup>
                                    </t:column>
                                    <t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
                                        <f:facet name="header">
                                            <e:bold value="#{msgs['BOOKMARKS.HISTORY.HEADER.DEPARTMENT']}" />
                                        </f:facet>
                                        <e:text rendered="#{hie.historyItem.ticketHistoryItem}"
                                            value="#{hie.historyItem.ticket.department.label}" />
                                        <e:text rendered="#{hie.historyItem.archivedTicketHistoryItem}"
                                            value="#{hie.historyItem.archivedTicket.department.label}" />
                                    </t:column>
                                    <t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
                                        <f:facet name="header">
                                            <e:bold value="#{msgs['BOOKMARKS.HISTORY.HEADER.OWNER']}" />
                                        </f:facet>
                                        <h:panelGroup>
                                            <e:text rendered="#{hie.historyItem.ticketHistoryItem}"
                                                value="#{userFormatter[hie.historyItem.ticket.owner]}" />
                                        </h:panelGroup>
                                        <h:panelGroup>
                                            <e:text rendered="#{hie.historyItem.archivedTicketHistoryItem}"
                                                value="#{userFormatter[hie.historyItem.archivedTicket.owner]}" />
                                        </h:panelGroup>
                                    </t:column>
                                    <t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" onclick="selectHistoryTicket(#{variable});">
                                        <f:facet name="header">
                                            <e:bold value="#{msgs['BOOKMARKS.HISTORY.HEADER.MANAGER']}" />
                                        </f:facet>
                                        <h:panelGroup>
                                            <e:text rendered="#{hie.historyItem.ticketHistoryItem and hie.historyItem.ticket.manager != null}"
                                                value="#{userFormatter[hie.historyItem.ticket.manager]}" />
                                        </h:panelGroup>
                                        <h:panelGroup>
                                            <e:text rendered="#{hie.historyItem.archivedTicketHistoryItem and hie.historyItem.archivedTicket.manager != null}"
                                                value="#{userFormatter[hie.historyItem.archivedTicket.manager]}" />
                                        </h:panelGroup>
                                    </t:column>
                                    <t:column style="cursor: #{hie.canRead ? 'pointer' : 'default'}" >
                                        <h:panelGroup style="display: none" rendered="#{hie.canRead}" >
                                            <e:commandButton id="viewTicketButton"
                                                rendered="#{hie.historyItem.ticketHistoryItem}"
                                                action="viewTicket" immediate="true">
                                                <t:updateActionListener value="#{hie.historyItem.ticket}"
                                                    property="#{ticketController.updatedTicket}" />
                                                <t:updateActionListener value="bookmarks"
                                                    property="#{ticketController.backPage}" />
                                            </e:commandButton>
                                            <e:commandButton id="viewArchivedTicketButton"
                                                rendered="#{hie.historyItem.archivedTicketHistoryItem}"
                                                action="viewArchivedTicket" immediate="true">
                                                <t:updateActionListener value="#{hie.historyItem.archivedTicket}"
                                                    property="#{archivedTicketController.archivedTicket}" />
                                                <t:updateActionListener value="bookmarks"
                                                    property="#{archivedTicketController.backPage}" />
                                            </e:commandButton>
                                        </h:panelGroup>
                                    </t:column>
                                    <f:facet name="footer">
                                        <t:htmlTag value="hr" />
                                    </f:facet>
                                </e:dataTable>
                                <e:paragraph value="#{msgs['BOOKMARKS.HISTORY.TEXT.NO_HISTORY']}" rendered="#{empty bookmarksController.historyItemEntries}" />
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
	
