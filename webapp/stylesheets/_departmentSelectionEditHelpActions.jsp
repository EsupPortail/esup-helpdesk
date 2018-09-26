<%@include file="_include.jsp"%>

<e:subSection value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.HEADER.ACTIONS']}" />
<e:panelGrid columns="2" columnClasses="colfLeftNowrap,colLeft" >
	<t:htmlTag value="pre"><f:verbatim escape="true"><add-all /></f:verbatim></t:htmlTag>
	<e:text value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_ADD_ALL']}" />
	<t:htmlTag value="pre"><f:verbatim escape="true"><add-by-label label="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_LABEL_PARAM']}" /><f:verbatim escape="true">" /></f:verbatim></t:htmlTag>
	<e:text value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_ADD_BY_LABEL']}" />
	<t:htmlTag value="pre"><f:verbatim escape="true"><add-by-cate label="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_ADD_BY_LABEL']}" /><f:verbatim escape="true">" cateIds="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_CATE_PARAM']}" /><f:verbatim escape="true">"/></f:verbatim></t:htmlTag>
	<e:text value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_ADD_BY_CATE']}" />
	<t:htmlTag value="pre"><f:verbatim escape="true"><add-by-filter filter="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_FILTER_PARAM']}" /><f:verbatim escape="true">" /></f:verbatim></t:htmlTag>
	<e:text value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_ADD_BY_FILTER']}" />
	<t:htmlTag value="pre"><f:verbatim escape="true"><stop /></f:verbatim></t:htmlTag>
	<e:text value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_STOP']}" />
	<t:htmlTag value="pre"><f:verbatim escape="true"><do-nothing /></f:verbatim></t:htmlTag>
	<e:text value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_DO_NOTHING']}" />
</e:panelGrid>
<e:paragraph value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.ACTION_NOTE']}" />
