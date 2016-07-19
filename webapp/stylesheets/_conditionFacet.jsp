<%@include file="_include.jsp"%>
<f:facet name="and">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.AND']}" />
	</h:panelGroup>
</f:facet>
<f:facet name="or">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.OR']}" />
	</h:panelGroup>
</f:facet>
<f:facet name="not">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.NOT']}" />
	</h:panelGroup>
</f:facet>
<f:facet name="ip">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.IP']}" >
			<f:param value="#{node.condition.pattern}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="departmentManager">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.DEPARTMENT_MANAGER']}" 
			rendered="#{node.condition.label == null}" />
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.DEPARTMENT_MANAGER_LABEL']}" 
			rendered="#{node.condition.label != null}" >
			<f:param value="#{node.condition.label}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="applicationUser">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.APPLICATION_USER']}" />
	</h:panelGroup>
</f:facet>
<f:facet name="casUser">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.CAS_USER']}" />
	</h:panelGroup>
</f:facet>
<f:facet name="shibbolethUser">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.SHIBBOLETH_USER']}" />
	</h:panelGroup>
</f:facet>
<f:facet name="false">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.FALSE']}" />
	</h:panelGroup>
</f:facet>
<f:facet name="true">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.TRUE']}" />
	</h:panelGroup>
</f:facet>
<f:facet name="fqdnEq">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.FQND_EQ']}" >
			<f:param value="#{node.condition.value}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="fqdnEndsWith">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.FQND_ENDS_WITH']}" >
			<f:param value="#{node.condition.suffix}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="fqdnStartsWith">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.FQND_STARTS_WITH']}" >
			<f:param value="#{node.condition.prefix}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="ldapAttributeEq">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.LDAP_ATTRIBUTE_EQ']}" >
			<f:param value="#{node.condition.name}" />
			<f:param value="#{node.condition.value}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="ldapAttributeLike">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.LDAP_ATTRIBUTE_LIKE']}" >
			<f:param value="#{node.condition.name}" />
			<f:param value="#{node.condition.pattern}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="portalAttributeEq">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.PORTAL_ATTRIBUTE_EQ']}" >
			<f:param value="#{node.condition.name}" />
			<f:param value="#{node.condition.value}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="portalAttributeLike">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.PORTAL_ATTRIBUTE_LIKE']}" >
			<f:param value="#{node.condition.name}" />
			<f:param value="#{node.condition.pattern}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="portalGroupMember">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.PORTAL_GROUP_MEMBER_ID']}" 
			rendered="#{node.condition.id != null}" >
			<f:param value="#{node.condition.id}" />
		</e:text>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.PORTAL_GROUP_MEMBER_NAME']}" 
			rendered="#{node.condition.name != null}" >
			<f:param value="#{node.condition.name}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="named">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.NAMED']}" >
			<f:param value="#{node.condition.name}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="uidLike">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.UID_LIKE']}" >
			<f:param value="#{node.condition.pattern}" />
		</e:text>
	</h:panelGroup>
</f:facet>
<f:facet name="administrator">
	<h:panelGroup>
		<%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.ADMINISTRATOR']}" />
	</h:panelGroup>
</f:facet>
<f:facet name="externalDb">
    <h:panelGroup>
        <%@include file="_conditionResult.jsp"%>
		<e:text value=" #{msgs['DEPARTMENT_SELECTION.CONDITION.EXTERNAL_DB']}" >
			<f:param value="#{node.condition.jndi}" />
			<f:param value="#{node.condition.sql}" />
		</e:text>
    </h:panelGroup>
</f:facet>