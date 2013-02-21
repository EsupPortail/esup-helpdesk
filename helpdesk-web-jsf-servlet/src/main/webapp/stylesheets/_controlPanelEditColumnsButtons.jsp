<%@include file="_include.jsp"%>
<e:commandButton id="editColumnsQuitButton" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.EDIT_COLUMNS_QUIT']}"
	action="#{controlPanelController.toggleEditColumns}" />
<e:commandButton id="moveColumn0RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{0}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn1LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{1}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn1RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{1}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn2LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{2}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn2RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{2}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn3LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{3}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn3RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{3}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn4LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{4}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn4RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{4}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn5LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{5}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn5RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{5}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn6LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{6}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn6RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{6}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn7LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{7}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn7RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{7}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn8LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{8}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn8RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{8}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn9LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{9}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn9RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{9}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn10LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{10}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn10RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{10}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn11LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{11}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="moveColumn11RightButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{11}"
		property="#{controlPanelController.columnToMoveRight}" />
</e:commandButton>
<e:commandButton id="moveColumn12LeftButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{12}"
		property="#{controlPanelController.columnToMoveLeft}" />
</e:commandButton>
<e:commandButton id="removeColumn0Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{0}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn1Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{1}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn2Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{2}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn3Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{3}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn4Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{4}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn5Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{5}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn6Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{6}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn7Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{7}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn8Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{8}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn9Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{9}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn10Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{10}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn11Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{11}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="removeColumn12Button" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}"
	action="#{controlPanelController.updateColumns}">
	<t:updateActionListener value="#{12}"
		property="#{controlPanelController.columnToRemove}" />
</e:commandButton>
<e:commandButton id="resetColumnsButton" immediate="true" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.RESET_COLUMNS']}"
	action="#{controlPanelController.resetColumns}" />
<e:commandButton id="addColumnButton"
	rendered="#{not empty controlPanelController.addColumnItems}" style="display: none"
	value="#{msgs['CONTROL_PANEL.BUTTON.ADD_COLUMN']}"
	action="#{controlPanelController.addColumn}" />
