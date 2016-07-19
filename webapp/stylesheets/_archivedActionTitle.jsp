<%@include file="_include.jsp"%>

<e:bold 
	value="#{msgs['ACTION_TITLE.APPROVE']}"
	rendered="#{archivedAction.actionType == 'APPROVE'}" >
	<f:param value="#{archivedAction.date}" />
	<f:param value="#{userFormatter[archivedAction.user]}" />
</e:bold>
<h:panelGroup rendered="#{archivedAction.actionType == 'ASSIGN'}" >
	<h:panelGroup rendered="#{archivedAction.user != null}" >
		<e:bold 
			value="#{msgs['ACTION_TITLE.ASSIGN']}"
			rendered="#{archivedAction.managerBefore != null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.user]}" />
			<f:param value="#{userFormatter[archivedAction.managerAfter]}" />
			<f:param value="#{userFormatter[archivedAction.managerBefore]}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.ASSIGN_FREE_BEFORE']}"
			rendered="#{archivedAction.managerBefore == null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.user]}" />
			<f:param value="#{userFormatter[archivedAction.managerAfter]}" />
		</e:bold>
	</h:panelGroup>
	<h:panelGroup rendered="#{archivedAction.user == null}" >
		<e:bold 
			value="#{msgs['ACTION_TITLE.ASSIGN_APPLICATION']}"
			rendered="#{archivedAction.managerBefore != null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.managerAfter]}" />
			<f:param value="#{userFormatter[archivedAction.managerBefore]}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.ASSIGN_APPLICATION_FREE_BEFORE']}"
			rendered="#{archivedAction.managerBefore == null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.managerAfter]}" />
		</e:bold>
	</h:panelGroup>
</h:panelGroup>
<e:bold 
	value="#{msgs['ACTION_TITLE.CANCEL']}"
	rendered="#{archivedAction.actionType == 'CANCEL'}" >
	<f:param value="#{archivedAction.date}" />
	<f:param value="#{userFormatter[archivedAction.user]}" />
</e:bold>
<h:panelGroup
	rendered="#{archivedAction.actionType == 'CANCEL_POSTPONEMENT'}" >
	<e:bold 
		value="#{msgs['ACTION_TITLE.CANCEL_POSTPONEMENT']}"
		rendered="#{archivedAction.user != null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
	</e:bold>
	<e:bold 
		value="#{msgs['ACTION_TITLE.CANCEL_POSTPONEMENT_APPLICATION']}"
		rendered="#{archivedAction.user == null}" >
		<f:param value="#{archivedAction.date}" />
	</e:bold>
</h:panelGroup>
<h:panelGroup rendered="#{archivedAction.actionType == 'CHANGE_CATEGORY'}" >
	<h:panelGroup rendered="#{archivedAction.user != null}" >
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_CATEGORY']}"
			rendered="#{archivedAction.categoryBeforeLabel != null and archivedAction.categoryAfterLabel != null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.user]}" />
			<f:param value="#{archivedAction.categoryAfterLabel}" />
			<f:param value="#{archivedAction.categoryBeforeLabel}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_CATEGORY_FROM_NONE']}"
			rendered="#{archivedAction.categoryBeforeLabel == null and archivedAction.categoryAfterLabel != null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.user]}" />
			<f:param value="#{archivedAction.categoryAfterLabel}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_CATEGORY_TO_NONE']}"
			rendered="#{archivedAction.categoryAfterLabel == null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.user]}" />
		</e:bold>
	</h:panelGroup >
	<h:panelGroup rendered="#{archivedAction.user == null}" >
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_CATEGORY_APPLICATION']}"
			rendered="#{archivedAction.categoryBeforeLabel != null and archivedAction.categoryAfterLabel != null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{archivedAction.categoryAfterLabel}" />
			<f:param value="#{archivedAction.categoryBeforeLabel}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_CATEGORY_APPLICATION_FROM_NONE']}"
			rendered="#{archivedAction.categoryBeforeLabel == null and archivedAction.categoryAfterLabel != null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{archivedAction.categoryAfterLabel}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_CATEGORY_APPLICATION_TO_NONE']}"
			rendered="#{archivedAction.categoryAfterLabel == null}" >
			<f:param value="#{archivedAction.date}" />
		</e:bold>
	</h:panelGroup>
</h:panelGroup>
<h:panelGroup rendered="#{archivedAction.actionType == 'CHANGE_COMPUTER'}" >
	<e:bold 
		value="#{msgs['ACTION_TITLE.CHANGE_COMPUTER']}"
		rendered="#{archivedAction.computerAfter != null and archivedAction.computerBefore != null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
		<f:param value="#{archivedAction.computerAfter}" />
		<f:param value="#{archivedAction.computerBefore}" />
	</e:bold>
	<e:bold 
		value="#{msgs['ACTION_TITLE.CHANGE_COMPUTER_NULL_AFTER']}"
		rendered="#{archivedAction.computerAfter == null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
		<f:param value="#{archivedAction.computerBefore}" />
	</e:bold>
	<e:bold 
		value="#{msgs['ACTION_TITLE.CHANGE_COMPUTER_NULL_BEFORE']}"
		rendered="#{archivedAction.computerBefore == null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
		<f:param value="#{archivedAction.computerAfter}" />
	</e:bold>
</h:panelGroup>
<h:panelGroup rendered="#{archivedAction.actionType == 'CHANGE_DEPARTMENT'}" >
	<h:panelGroup rendered="#{archivedAction.user != null}" >
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_DEPARTMENT']}"
			rendered="#{archivedAction.departmentBefore != null and archivedAction.departmentAfter != null}" >
			<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
			<f:param value="#{archivedAction.departmentAfter.label}" />
			<f:param value="#{archivedAction.departmentBefore.label}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_DEPARTMENT_FROM_NONE']}"
			rendered="#{archivedAction.departmentBefore == null and archivedAction.departmentAfter != null}" >
			<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
			<f:param value="#{archivedAction.departmentAfter.label}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_DEPARTMENT_TO_NONE']}"
			rendered="#{archivedAction.departmentAfter == null}" >
			<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
		</e:bold>
	</h:panelGroup >
	<h:panelGroup rendered="#{archivedAction.user == null}" >
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_DEPARTMENT_APPLICATION']}"
			rendered="#{archivedAction.departmentBefore != null and archivedAction.departmentAfter != null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{archivedAction.departmentAfter.label}" />
			<f:param value="#{archivedAction.departmentBefore.label}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_DEPARTMENT_APPLICATION_FROM_NONE']}"
			rendered="#{archivedAction.departmentBefore == null and archivedAction.departmentAfter != null}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{archivedAction.departmentAfter.label}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.CHANGE_DEPARTMENT_APPLICATION_FROM_NONE']}"
			rendered="#{archivedAction.departmentAfter == null}" >
			<f:param value="#{archivedAction.date}" />
		</e:bold>
	</h:panelGroup >
</h:panelGroup>
<e:bold 
	value="#{msgs['ACTION_TITLE.CHANGE_LABEL']}"
	rendered="#{archivedAction.actionType == 'CHANGE_LABEL'}" >
	<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
	<f:param value="#{archivedAction.labelAfter}" />
	<f:param value="#{archivedAction.labelBefore}" />
</e:bold>
<h:panelGroup
	rendered="#{archivedAction.actionType == 'CHANGE_OWNER'}" >
	<e:bold 
		value="#{msgs['ACTION_TITLE.CHANGE_OWNER']}"
		rendered="#{archivedAction.user != null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
		<f:param value="#{userFormatter[archivedAction.ticketOwnerAfter]}" />
		<f:param value="#{userFormatter[archivedAction.ticketOwnerBefore]}" />
	</e:bold>
	<e:bold 
		value="#{msgs['ACTION_TITLE.CHANGE_OWNER_APPLICATION']}"
		rendered="#{archivedAction.user == null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.ticketOwnerAfter]}" />
		<f:param value="#{userFormatter[archivedAction.ticketOwnerBefore]}" />
	</e:bold>
</h:panelGroup>
<e:bold 
	value="#{msgs['ACTION_TITLE.CHANGE_PRIORITY']}"
	rendered="#{archivedAction.actionType == 'CHANGE_PRIORITY'}" >
	<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
	<f:param value="#{msgs[priorityI18nKeyProvider[archivedAction.priorityLevelAfter]]}" />
	<f:param value="#{msgs[priorityI18nKeyProvider[archivedAction.priorityLevelBefore]]}" />
</e:bold>
<e:bold 
	value="#{msgs['ACTION_TITLE.CHANGE_SCOPE']}"
	rendered="#{archivedAction.actionType == 'CHANGE_SCOPE'}" >
	<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
	<f:param value="#{msgs[ticketScopeI18nKeyProvider[archivedAction.scopeAfter]]}" />
	<f:param value="#{msgs[ticketScopeI18nKeyProvider[archivedAction.scopeBefore]]}" />
</e:bold>
<e:bold 
	value="#{msgs['ACTION_TITLE.CHANGE_SPENT_TIME']}"
	rendered="#{archivedAction.actionType == 'CHANGE_SPENT_TIME'}" >
	<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
</e:bold>
<e:bold 
	value="#{msgs['ACTION_TITLE.CLOSE']}"
	rendered="#{archivedAction.actionType == 'CLOSE' or archivedAction.actionType == 'CLOSE_APPROVE'}" >
	<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
</e:bold>
<e:bold 
	value="#{msgs['ACTION_TITLE.CREATE']}"
	rendered="#{archivedAction.actionType == 'CREATE'}" >
	<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
</e:bold>
<e:bold 
	value="#{msgs['ACTION_TITLE.EXPIRE']}"
	rendered="#{archivedAction.actionType == 'EXPIRE'}" >
	<f:param value="#{archivedAction.date}" />
</e:bold>
<h:panelGroup rendered="#{archivedAction.actionType == 'FREE'}" >
	<e:bold 
		value="#{msgs['ACTION_TITLE.FREE']}"
		rendered="#{archivedAction.user != null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
		<f:param value="#{userFormatter[archivedAction.managerBefore]}" />
	</e:bold>
	<e:bold 
		value="#{msgs['ACTION_TITLE.FREE_APPLICATION']}"
		rendered="#{archivedAction.user == null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.managerBefore]}" />
	</e:bold>
</h:panelGroup>
<h:panelGroup rendered="#{archivedAction.actionType == 'GIVE_INFORMATION'}" >
	<e:bold 
		value="#{msgs['ACTION_TITLE.GIVE_INFORMATION']}"
		rendered="#{archivedAction.user != null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
	</e:bold>
	<e:bold 
		value="#{msgs['ACTION_TITLE.GIVE_INFORMATION_APPLICATION']}"
		rendered="#{archivedAction.user == null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
	</e:bold>
</h:panelGroup>
<e:bold 
	value="#{msgs['ACTION_TITLE.MONITORING_INVITE_V2']}"
	rendered="#{archivedAction.actionType == 'MONITORING_INVITE'}" >
	<f:param value="#{archivedAction.date}" />
	<f:param value="#{userFormatter[archivedAction.user]}" />
	<f:param value="#{userFormatter[archivedAction.invitedUser]}" />
</e:bold>
<h:panelGroup rendered="#{archivedAction.actionType == 'INVITE'}" >
	<h:panelGroup rendered="#{archivedAction.user != null}" >
		<e:bold 
			value="#{msgs['ACTION_TITLE.INVITE']}"
			rendered="#{archivedAction.user.id != archivedAction.invitedUser.id}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.user]}" />
			<f:param value="#{userFormatter[archivedAction.invitedUser]}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.INVITE_SELF']}"
			rendered="#{archivedAction.user.id == archivedAction.invitedUser.id}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.user]}" />
		</e:bold>
	</h:panelGroup>
	<e:bold 
		value="#{msgs['ACTION_TITLE.INVITE_APPLICATION']}"
		rendered="#{archivedAction.user == null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.invitedUser]}" />
	</e:bold>
</h:panelGroup>
<h:panelGroup rendered="#{archivedAction.actionType == 'REMOVE_INVITATION'}" >
	<h:panelGroup rendered="#{archivedAction.user != null}" >
		<e:bold 
			value="#{msgs['ACTION_TITLE.REMOVE_INVITATION']}"
			rendered="#{archivedAction.user.id != archivedAction.invitedUser.id}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.user]}" />
			<f:param value="#{userFormatter[archivedAction.invitedUser]}" />
		</e:bold>
		<e:bold 
			value="#{msgs['ACTION_TITLE.REMOVE_INVITATION_SELF']}"
			rendered="#{archivedAction.user.id == archivedAction.invitedUser.id}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.user]}" />
		</e:bold>
	</h:panelGroup>
	<h:panelGroup rendered="#{archivedAction.user == null}" >
		<e:bold 
			value="#{msgs['ACTION_TITLE.REMOVE_INVITATION_APPLICATION']}" >
			<f:param value="#{archivedAction.date}" />
			<f:param value="#{userFormatter[archivedAction.invitedUser]}" />
		</e:bold>
	</h:panelGroup>
</h:panelGroup>
<e:bold 
	value="#{msgs['ACTION_TITLE.POSTPONE']}"
	rendered="#{archivedAction.actionType == 'POSTPONE'}" >
	<f:param value="#{archivedAction.date}" />
	<f:param value="#{userFormatter[archivedAction.user]}" />
</e:bold>
<e:bold 
	value="#{msgs['ACTION_TITLE.REFUSE']}"
	rendered="#{archivedAction.actionType == 'REFUSE'}" >
	<f:param value="#{archivedAction.date}" />
	<f:param value="#{userFormatter[archivedAction.user]}" />
</e:bold>
<e:bold 
	value="#{msgs['ACTION_TITLE.REOPEN']}"
	rendered="#{archivedAction.actionType == 'REOPEN'}" >
	<f:param value="#{archivedAction.date}" />
	<f:param value="#{userFormatter[archivedAction.user]}" />
</e:bold>
<e:bold 
	value="#{msgs['ACTION_TITLE.REQUEST_INFORMATION']}"
	rendered="#{archivedAction.actionType == 'REQUEST_INFORMATION'}" >
	<f:param value="#{archivedAction.date}" />
	<f:param value="#{userFormatter[archivedAction.user]}" />
</e:bold>
<h:panelGroup 
	rendered="#{archivedAction.actionType == 'TAKE'}" >
	<e:bold 
		value="#{msgs['ACTION_TITLE.TAKE']}"
		rendered="#{archivedAction.managerBefore != null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
		<f:param value="#{userFormatter[archivedAction.managerBefore]}" />
	</e:bold>
	<e:bold 
		value="#{msgs['ACTION_TITLE.TAKE_FREE_BEFORE']}"
		rendered="#{archivedAction.managerBefore == null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
	</e:bold>
</h:panelGroup>
<h:panelGroup rendered="#{archivedAction.actionType == 'UPLOAD'}" >
	<e:bold 
		value="#{msgs['ACTION_TITLE.UPLOAD']}"
		rendered="#{archivedAction.user != null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{userFormatter[archivedAction.user]}" />
		<f:param value="#{archivedAction.filename}" />
	</e:bold>
	<e:bold 
		value="#{msgs['ACTION_TITLE.UPLOAD_APPLICATION']}"
		rendered="#{archivedAction.user == null}" >
		<f:param value="#{archivedAction.date}" />
		<f:param value="#{archivedAction.filename}" />
	</e:bold>
</h:panelGroup>
<e:bold 
	value="??? #{archivedAction.actionType} ???" 
	rendered="#{archivedAction.actionType != 'CREATE' 
	and archivedAction.actionType != 'APPROVE' 
	and archivedAction.actionType != 'ASSIGN'
	and archivedAction.actionType != 'CANCEL'
	and archivedAction.actionType != 'CANCEL_POSTPONEMENT'
	and archivedAction.actionType != 'CHANGE_CATEGORY'
	and archivedAction.actionType != 'CHANGE_COMPUTER'
	and archivedAction.actionType != 'CHANGE_DEPARTMENT'
	and archivedAction.actionType != 'CHANGE_LABEL'
	and archivedAction.actionType != 'CHANGE_OWNER'
	and archivedAction.actionType != 'CHANGE_PRIORITY'
	and archivedAction.actionType != 'CHANGE_SCOPE'
	and archivedAction.actionType != 'CHANGE_SPENT_TIME'
	and archivedAction.actionType != 'CLOSE_APPROVE'
	and archivedAction.actionType != 'CLOSE'
	and archivedAction.actionType != 'CREATE'
	and archivedAction.actionType != 'EXPIRE'
	and archivedAction.actionType != 'FREE'
	and archivedAction.actionType != 'GIVE_INFORMATION'
	and archivedAction.actionType != 'MONITORING_INVITE'
	and archivedAction.actionType != 'INVITE'
	and archivedAction.actionType != 'REMOVE_INVITATION'
	and archivedAction.actionType != 'POSTPONE'
	and archivedAction.actionType != 'REFUSE'
	and archivedAction.actionType != 'REFUSE_CLOSURE'
	and archivedAction.actionType != 'REOPEN'
	and archivedAction.actionType != 'REQUEST_INFORMATION'
	and archivedAction.actionType != 'TAKE'
	and archivedAction.actionType != 'UPLOAD'}" 
	/>
