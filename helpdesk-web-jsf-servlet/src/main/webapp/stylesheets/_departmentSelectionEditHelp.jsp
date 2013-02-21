<%@include file="_include.jsp"%>

<e:subSection value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.HEADER.MAIN']}" />
<t:htmlTag value="pre">
<f:verbatim escape="true">
<department-selection>
    <description></f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.MAIN_DESCRIPTION']}" /><f:verbatim escape="true"></description>
    <defined-conditions>
    	</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DEFINED_CONDITIONS']}" /><f:verbatim escape="true">
    </defined-conditions>
    <rules>
    	</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.RULES']}" /><f:verbatim escape="true">
    </rules>
    <actions>
    	</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DEFAULT_ACTIONS']}" /><f:verbatim escape="true">
    </actions>
</department-selection></f:verbatim>
</t:htmlTag>
<e:subSection value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.HEADER.DEFINED_CONDITIONS']}" />
<t:htmlTag value="pre">
<f:verbatim escape="true">
<define-condition name="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DEFINED_CONDITION_NAME']}" /><f:verbatim escape="true">">
    <description></f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DEFINED_CONDITION_DESCRIPTION']}" /><f:verbatim escape="true"></description>
    </f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DEFINED_CONDITION_CONDITION']}" /><f:verbatim escape="true">
</define-condition></f:verbatim>
</t:htmlTag>
<e:paragraph value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DEFINED_CONDITION_NOTE']}" />
<%@include file="_departmentSelectionEditHelpRules.jsp"%>
<%@include file="_departmentSelectionEditHelpActions.jsp"%>
<%@include file="_departmentSelectionEditHelpConditions.jsp"%>
