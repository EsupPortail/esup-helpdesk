<%@include file="_include.jsp"%>

<e:commandButton id="idAscSortButton" value="v" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="+id" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="idDescSortButton" value="^" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="-id" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="creationDepartmentAscSortButton" value="v" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="+creationDepartment" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="creationDepartmentDescSortButton" value="^" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="-creationDepartment" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="departmentAscSortButton" value="v" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="+department" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="departmentDescSortButton" value="^" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="-department" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="categoryAscSortButton" value="v" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="+category" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="categoryDescSortButton" value="^" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="-category" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="labelAscSortButton" value="v" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="+label" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="labelDescSortButton" value="^" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="-label" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="statusAscSortButton" value="v" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="+status" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="statusDescSortButton" value="^" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="-status" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="priorityAscSortButton" value="v" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="+priority" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="priorityDescSortButton" value="^" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="-priority" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="lastActionDateAscSortButton" value="v" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="+lastActionDate" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="lastActionDateDescSortButton" value="^" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="-lastActionDate" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="ownerAscSortButton" value="v" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="+owner" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="ownerDescSortButton" value="^" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="-owner" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="managerAscSortButton" value="v" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="+manager" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
<e:commandButton id="managerDescSortButton" value="^" action="changeSortOrder" style="display: none" >
	<t:updateActionListener value="-manager" property="#{controlPanelController.firstOrderPartSpec}" />
</e:commandButton>
