<%@include file="_include.jsp"%>
<t:htmlTag value="div" styleClass="region">
    <t:htmlTag value="div" styleClass="logo">
        <t:graphicImage value="/media/images/amu-helpdesk.png" alt="ACCUEIL" title="ACCUEIL" />
    </t:htmlTag>
    <t:htmlTag value="div">
        <t:htmlTag value="div" styleClass="current-user">
                <e:text  rendered="#{not sessionController.applicationUser}" value="#{preferencesController.currentUser.displayName}" />
                <t:graphicImage rendered="#{preferencesController.currentUser!=null}" value="/media/images/user-profil.png"/>
        </t:htmlTag>
        <t:htmlTag value="div" styleClass="ent-link">
            <h:outputText value="<a href=&quot;https://ent.univ-amu.fr&quot;>ENT</a>" escape="false" />
        </t:htmlTag>
    </t:htmlTag>
</t:htmlTag>
