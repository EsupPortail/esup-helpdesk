<%@include file="_include.jsp"%>
<e:subSection value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.HEADER.CONDITIONS']}" />
<e:panelGrid columns="1" width="100%" columnClasses="colLeft" >
	<e:bold value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.SUB_HEADER.USER_CONDITIONS']}" />
	<t:htmlTag value="pre">
<f:verbatim escape="true">
<application-user />
<cas-user />
<shibboleth-user />
<specific-user />
<administrator />
<department-manager />
<department-manager label="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DEPARTMENT_MANAGER_LABEL_PARAM']}" /><f:verbatim escape="true">" />
<uid-like pattern="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.UID_LIKE_PATTERN_PARAM']}" /><f:verbatim escape="true">" />
<ldap-attribute-eq name="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.LDAP_NAME_PARAM']}" /><f:verbatim escape="true">" value="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.LDAP_VALUE_PARAM']}" /><f:verbatim escape="true">" />
<ldap-attribute-like name="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.LDAP_NAME_PARAM']}" /><f:verbatim escape="true">" pattern="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.LDAP_PATTERN_PARAM']}" /><f:verbatim escape="true">" />
<portal-attribute-eq name="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.PORTAL_NAME_PARAM']}" /><f:verbatim escape="true">" value="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.PORTAL_VALUE_PARAM']}" /><f:verbatim escape="true">" />
<portal-attribute-like name="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.PORTAL_NAME_PARAM']}" /><f:verbatim escape="true">" pattern="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.PORTAL_PATTERN_PARAM']}" /><f:verbatim escape="true">" />
<portal-group-member name="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.PORTAL_GROUP_NAME_PARAM']}" /><f:verbatim escape="true">" />
<portal-group-member name="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.PORTAL_GROUP_ID_PARAM']}" /><f:verbatim escape="true">" /></f:verbatim>
	</t:htmlTag>
	<e:bold value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.SUB_HEADER.CLIENT_CONDITIONS']}" />
	<t:htmlTag value="pre">
<f:verbatim escape="true">
<ip pattern="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.IP_PATTERN_PARAM']}" /><f:verbatim escape="true">" />
<fqdn-eq value="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.FQDN_VALUE_PARAM']}" /><f:verbatim escape="true">" />
<fqdn-starts-with pattern="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.FQDN_PREFIX_PARAM']}" /><f:verbatim escape="true">" />
<fqdn-ends-with pattern="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.FQDN_SUFFIX_PARAM']}" /><f:verbatim escape="true">" /></f:verbatim>
	</t:htmlTag>
	<e:bold value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.SUB_HEADER.DB_CONDITIONS']}" />
	<t:htmlTag value="pre">
<f:verbatim escape="true">
<external-db jndi="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DB_JNDI']}" /><f:verbatim escape="true">" sql="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DB_SQL']}" /><f:verbatim escape="true">" />
<external-db ctx="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DB_CTX']}" /><f:verbatim escape="true">" jndi="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DB_JNDI']}" /><f:verbatim escape="true">" sql="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DB_SQL']}" /><f:verbatim escape="true">" /></f:verbatim>
	</t:htmlTag>
<e:paragraph value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DB_HELP']}" escape="false" />
	<t:htmlTag value="pre">
<e:text value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.DB_EXAMPLE']}" />
<f:verbatim escape="true">
<external-db ctx="java:comp/env"
      jndi="jdbc/user-db"
      sql="SELECT COUNT(*) FROM users
           WHERE username=%USER% AND (ip=%IP% OR hostname=%HOSTNAME%)" /></f:verbatim>
	</t:htmlTag>
	<e:bold value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.SUB_HEADER.COMPOSED_CONDITIONS']}" />
</e:panelGrid>
<e:panelGrid columns="3" width="100%" columnClasses="colLeft,colLeft,colLeft" >
	<t:htmlTag value="pre">
<f:verbatim escape="true">
<and>
    </f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.COMPOSED_CONDITIONS']}" /><f:verbatim escape="true">
</and></f:verbatim>
	</t:htmlTag>
	<t:htmlTag value="pre">
<f:verbatim escape="true">
<or>
    </f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.COMPOSED_CONDITIONS']}" /><f:verbatim escape="true">
</or></f:verbatim>
	</t:htmlTag>
	<t:htmlTag value="pre">
<f:verbatim escape="true">
<not>
    </f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.NOT_CONDITION']}" /><f:verbatim escape="true">
</not></f:verbatim>
	</t:htmlTag>
</e:panelGrid>
<e:panelGrid columns="1" width="100%" columnClasses="colLeft" >
	<e:bold value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.SUB_HEADER.NAMED_CONDITIONS']}" />
	<t:htmlTag value="pre">
<f:verbatim escape="true">
<named-condition name="</f:verbatim><e:italic value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.TEXT.NAMED_CONDITION_NAME_PARAM']}" /><f:verbatim escape="true">" /></f:verbatim>
	</t:htmlTag>
	<e:bold value="#{msgs['DEPARTMENT_SELECTION_EDIT_HELP.SUB_HEADER.CONSTANT_CONDITIONS']}" />
</e:panelGrid>
<e:panelGrid columns="2" width="100%" columnClasses="colLeft,colLeft" >
	<t:htmlTag value="pre">
<f:verbatim escape="true">
<true /></f:verbatim>
	</t:htmlTag>
	<t:htmlTag value="pre">
<f:verbatim escape="true">
<false /></f:verbatim>
	</t:htmlTag>
</e:panelGrid>
