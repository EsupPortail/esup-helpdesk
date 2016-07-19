<%@include file="_include.jsp"%>
<t:dataList value="#{node.evalResult.departments}" var="department" rowIndexVar="index"
	rendered="#{not empty node.evalResult.departments}" >
	<e:bold value=" " rendered="#{index == 0}" />
	<e:bold value="#{msgs['DEPARTMENT_SELECTION.ACTION.DEPARTMENTS_SEPARATOR']}" rendered="#{index != 0}" />
	<e:bold value=" #{department.label}" />
</t:dataList>
