<%@include file="_include.jsp"%>
<script type="text/javascript">	
	// element
	function getElementId(index, name) {
		return "preferencesForm:data:"+index+":"+name;
	}
	function getElement(index, name) {
		var elementId = getElementId(index, name);
		var element = document.getElementById(elementId);
	    if (element == null) {
	      alert("element [" + elementId + "] not found (index="+index+", name="+name+")");
	      return null;
	    }
	    return element;
	}
	function getMonitoringTypeElement(index, tmType) {
		return getElement(index, 'type-'+tmType);
	}
	function setMonitoringTypeValue(index, tmType, tmValue) {
		var element = getMonitoringTypeElement(index, tmType);
	    if (element != null) {
	      element.value = tmValue;
	    }
	}
	function getMonitoringTypeValue(index, tmType) {
		var element = getMonitoringTypeElement(index, tmType);
	    if (element != null) {
	      return element.value;
	    }
	    return null;
	}
	// checkbox
	function getMonitoringCheckboxElement(index, tmType, checkboxValue) {
		return getElement(index, 'monitoring-'+tmType+'-'+checkboxValue);
	}
	function setOneMonitoringCheckboxState(index, tmType, checkboxValue, tmValue) {
		getMonitoringCheckboxElement(index, tmType, checkboxValue).checked = (checkboxValue == tmValue);
	}
	function setMonitoringTypeCheckboxStates(index, tmType, tmValue) {
		setOneMonitoringCheckboxState(index, tmType, 0, tmValue);
		setOneMonitoringCheckboxState(index, tmType, 1, tmValue);
		setOneMonitoringCheckboxState(index, tmType, 2, tmValue);
	}
	function setCheckboxStates(index, anyInitValue, categoryInitValue, managedInitValue) {
		setMonitoringTypeCheckboxStates(index, 'any', anyInitValue);
		setMonitoringTypeCheckboxStates(index, 'category', categoryInitValue);
		setMonitoringTypeCheckboxStates(index, 'managed', managedInitValue);
	}
	function changeMonitoringType(index, tmType, tmValue) {
		setMonitoringTypeValue(index, tmType, tmValue);
		setMonitoringTypeCheckboxStates(index, tmType, getMonitoringTypeValue(index, tmType));
	}
	function changeMonitoringTypes(index, anyInitValue, categoryInitValue, managedInitValue) {
		changeMonitoringType(index, 'any', anyInitValue);
		changeMonitoringType(index, 'category', categoryInitValue);
		changeMonitoringType(index, 'managed', managedInitValue);
	}
//	function isCheckboxChecked(index, tmType, checkboxValue) {
//		return getMonitoringCheckboxElement(index, tmType, checkboxValue).checked;
//	}
	function enableMonitoringCheckbox(index, tmType, checkboxValue) {
		getMonitoringCheckboxElement(index, tmType, checkboxValue).disabled = false;
	}
	function enableMonitoringType(index, tmType) {
		enableMonitoringCheckbox(index, tmType, 0);
		enableMonitoringCheckbox(index, tmType, 1);
		enableMonitoringCheckbox(index, tmType, 2);
	}
	function enableMonitoringAll(index) {
		enableMonitoringType(index, "any");
		enableMonitoringType(index, "category");
		enableMonitoringType(index, "managed");
	}
	function disableMonitoringCheckbox(index, tmType, checkboxValue) {
		getMonitoringCheckboxElement(index, tmType, checkboxValue).disabled = true;
	}
	// profile
	function getMonitoringProfileElement(index) {
		return getElement(index, "monitoringProfile");
	}
	function setMonitoringProfileValue(index, value) {
		var element = getMonitoringProfileElement(index);
	    if (element != null) {
	      element.value = value;
	    }
	}
	function getMonitoringProfileValue(index) {
		var element = getMonitoringProfileElement(index);
	    if (element != null) {
	      return element.value;
	    }
	    return null;
	}
	function isMonitoringProfile(index, anyValue, categoryValue, managedValue) {
		return getMonitoringTypeValue(index,'any') == anyValue 
			&& getMonitoringTypeValue(index,'category') == categoryValue 
			&& getMonitoringTypeValue(index,'managed') == managedValue;
	}
	function updateMonitoringProfileFromValues(index) {
		var profile;
		if (isMonitoringProfile(index, 2, 2, 2)) {
			profile = "none";
		} else if (isMonitoringProfile(index, 2, 2, 0)) {
			profile = "minimal";
		} else if (isMonitoringProfile(index, 2, 1, 0)) {
			profile = "medium";
		} else if (isMonitoringProfile(index, 1, 1, 0)) {
			profile = "assiduous";
		} else if (isMonitoringProfile(index, 0, 0, 0)) {
			profile = "complete";
		} else {
			profile = "";
		}
		setMonitoringProfileValue(index, profile);
	}
	function updateValuesFromMonitoringProfile(index) {
		var profile = getElement(index,"monitoringProfile").value;
		if (profile == "none") {
			changeMonitoringTypes(index, 2, 2, 2);
		} else if (profile == "minimal") {
			changeMonitoringTypes(index, 2, 2, 0);
		} else if (profile == "medium") {
			changeMonitoringTypes(index, 2, 1, 0);
		} else if (profile == "assiduous") {
			changeMonitoringTypes(index, 1, 1, 0);
		} else if (profile == "complete") {
			changeMonitoringTypes(index, 0, 0, 0);
		}
	}
	// callbacks
	function enableMonitoringCheckboxes(index, updateProfile) {
		enableMonitoringAll(index);
		if (getMonitoringTypeValue(index, "category") > getMonitoringTypeValue(index, "any")) {
			changeMonitoringType(index, "category", getMonitoringTypeValue(index, "any"));
		}
		if (getMonitoringTypeValue(index, "managed") > getMonitoringTypeValue(index, "category")) {
			changeMonitoringType(index, "managed", getMonitoringTypeValue(index, "category"));
		}
		if (updateProfile) {
			updateMonitoringProfileFromValues(index);
		}
		if (getMonitoringTypeValue(index, "any") == 0) {
			disableMonitoringCheckbox(index, "category", 1);
			disableMonitoringCheckbox(index, "managed", 1);
			disableMonitoringCheckbox(index, "category", 2);
			disableMonitoringCheckbox(index, "managed", 2);
		} else if (getMonitoringTypeValue(index, "any") == 1) {
			disableMonitoringCheckbox(index, "category", 2);
			disableMonitoringCheckbox(index, "managed", 2);
		}
		if (getMonitoringTypeValue(index, "category") == 0) {
			disableMonitoringCheckbox(index, "managed", 1);
			disableMonitoringCheckbox(index, "managed", 2);
		} else if (getMonitoringTypeValue(index, "category") == 1) {
			disableMonitoringCheckbox(index, "managed", 2);
		}
	}
	function monitoringTypeOnchange(index, tmType) {
		setMonitoringTypeCheckboxStates(index, tmType, getMonitoringTypeValue(index, tmType));
		updateMonitoringProfileFromValues(index);
		enableMonitoringCheckboxes(index, true);
	}
	function monitoringCheckboxOnclick(index, tmType, tmValue) {
		changeMonitoringType(index, tmType, tmValue);
		updateMonitoringProfileFromValues(index);
		enableMonitoringCheckboxes(index, true);
	}
	function monitoringProfileOnchange(index) {
		updateValuesFromMonitoringProfile(index);
		enableMonitoringCheckboxes(index, false);
		if (getMonitoringProfileValue(index) == "") {
			showElement(getElementId(index, "monitoringCheckboxes"));
			hideElement(getElementId(index, "monitoringProfileHelp"));
		}
	}
	// initMonitoring
	function initMonitoring(index, anyInitValue, categoryInitValue, managedInitValue) {
		changeMonitoringTypes(index, anyInitValue, categoryInitValue, managedInitValue);
		updateMonitoringProfileFromValues(index);
		enableMonitoringCheckboxes(index, true);
		if (getMonitoringProfileValue(index) != "") {
			hideElement(getElementId(index, "monitoringCheckboxes"));
		}
	}
	// Report
	function getReportProfileElement(index) {
		return getElement(index, "reportProfile");
	}
	function getReportProfileValue(index) {
		var element = getReportProfileElement(index);
	    if (element != null) {
	      return element.value;
	    }
	    return null;
	}
	function getReportTime1Element(index) {
		return getElement(index, "reportTime1");
	}
	function getReportTime1Value(index) {
		var element = getReportTime1Element(index);
	    if (element != null) {
	      return element.value;
	    }
	    return null;
	}
	// updateReport
	function updateReport(index, showProfile) {
		hideElement(getElementId(index, "reportProfilePanel"));
		hideElement(getElementId(index, "reportTimePanel"));
		hideElement(getElementId(index, "reportTime2"));
		hideElement(getElementId(index, "reportWeekendPanel"));
		if (showProfile) {
			showElement(getElementId(index, "reportProfilePanel"));
			if (getReportProfileValue(index) != "") {
				showElement(getElementId(index, "reportTimePanel"));
				if (getReportTime1Value(index) >= 0) {
					showElement(getElementId(index, "reportTime2"));
					showElement(getElementId(index, "reportWeekendPanel"));
				}
			}
		}
	}
</script>
<e:page stringsVar="msgs" menuItem="preferences" locale="#{sessionController.locale}" >
    <t:htmlTag id="managerPreferences" value="div" styleClass="page-wrapper preferences">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>

                    <t:htmlTag value="main" styleClass="content">
                        <t:htmlTag value="div" styleClass="content-inner">
                            <h:panelGroup rendered="#{not preferencesController.pageAuthorized}" >
                                <%@include file="_auth.jsp"%>
                            </h:panelGroup>
                            <e:form
                                freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="preferencesForm" rendered="#{preferencesController.userManagerOrAdmin}" >
                                     <t:htmlTag value="div" styleClass="message">
                                             <e:messages/>
                                     </t:htmlTag>
                                     <t:htmlTag value="div" styleClass="dashboard-header">
                                        <t:htmlTag value="div" styleClass="controlPanel-title">
                                            <t:htmlTag value="h1">
                                                <t:htmlTag value="span">
                                                    <h:outputText value="#{msgs['PREFERENCES.TITLE']}"/>
                                                </t:htmlTag>
                                                <h:panelGroup rendered="#{preferencesController.userManagerOrAdmin}">
                                                     <t:htmlTag value="span">
                                                        <h:outputText value="#{msgs['CONTROL_PANEL.TITLE.MANAGER']}"/>
                                                     </t:htmlTag>
                                                </h:panelGroup>
                                            </t:htmlTag>

                                            <t:htmlTag styleClass="dashboard-toggle" value="div">
                                                <h:panelGroup
                                                    rendered="#{preferencesController.userManagerOrAdmin}">
                                                    <h:panelGroup style="cursor: pointer"
                                                        onclick="simulateLinkClick('preferencesForm:userPreferencesButton');">
                                                        <t:htmlTag value="i"
                                                        styleClass="fas fa-toggle-on"/>
                                                    </h:panelGroup>
                                                    <e:commandButton style="display: none" id="userPreferencesButton" action="userPreferences"
                                                        value="#{msgs['_.BUTTON.BACK']}" immediate="true" />
                                                </h:panelGroup>
                                            </t:htmlTag>
                                        </t:htmlTag>
                                     </t:htmlTag>
                                     <t:htmlTag value="fieldset">
                                          <t:htmlTag value="legend">
                                              <t:htmlTag value="span" >
                                                        <h:outputText value="#{msgs['MANAGER_PREFERENCES.TEXT.CONTROL_PANEL_REFRESH_DELAY']}"/>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item">
                                                <e:selectOneMenu styleClass="selectOneMenu" id="controlPanelRefreshDelay"
                                                     value="#{preferencesController.currentUser.controlPanelRefreshDelay}" >
                                                     <f:selectItems value="#{preferencesController.controlPanelRefreshDelayItems}" />
                                                </e:selectOneMenu>
                                          </t:htmlTag>
                                           <t:htmlTag value="div" styleClass="form-item form-submit">
                                                <e:commandButton value="#{msgs['_.BUTTON.UPDATE']}"
                                                       id="updateRefreshDelay" action="#{preferencesController.updateUser}" />
                                           </t:htmlTag>
                                     </t:htmlTag>



                                     <t:htmlTag value="fieldset" rendered="#{preferencesController.userManager}">
                                          <t:htmlTag value="legend">
                                              <t:htmlTag value="span" >
                                                        <h:outputText value="#{msgs['MANAGER_PREFERENCES.MONITORING.TITLE']}"/>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-submit">
                                            <e:commandButton id="toggleMonitoringButton" action="#{preferencesController.toggleMonitoring}"
                                                value="#{msgs[preferencesController.currentUser.receiveManagerMonitoring ? 'MANAGER_PREFERENCES.BUTTON.DISABLE_MONITORING' : 'MANAGER_PREFERENCES.BUTTON.ENABLE_MONITORING']}" />
                                          </t:htmlTag>
                                     </t:htmlTag>

                                     <t:htmlTag value="fieldset" rendered="#{preferencesController.userManager}">
                                          <t:htmlTag value="legend">
                                              <t:htmlTag value="span" >
                                                        <h:outputText value="#{msgs['MANAGER_PREFERENCES.TICKET_REPORTS.TITLE']}"/>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-submit">
                                            <e:commandButton id="toggleTicketReportsButton" action="#{preferencesController.toggleTicketReports}"
                                                value="#{msgs[preferencesController.currentUser.receiveTicketReports ? 'MANAGER_PREFERENCES.BUTTON.DISABLE_TICKET_REPORTS' : 'MANAGER_PREFERENCES.BUTTON.ENABLE_TICKET_REPORTS']}" />
                                          </t:htmlTag>
                                     </t:htmlTag>

                                     <t:htmlTag value="fieldset" rendered="#{preferencesController.userManager && preferencesController.currentUser.receiveTicketReports}">
                                          <t:htmlTag value="legend">
                                              <t:htmlTag value="span" >
                                                        <h:outputText value="#{msgs['MANAGER_PREFERENCES.GROUP_REPORTS.TITLE']}"/>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="reportsAllInOne"
                                                value="#{preferencesController.currentUser.receiveTicketReportsAllInOne}"/>
                                            <e:outputLabel for="reportsAllInOne" value=" #{msgs['MANAGER_PREFERENCES.TEXT.GROUP_REPORTS']} " />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-submit">
                                                <e:commandButton value="#{msgs['_.BUTTON.UPDATE']}"
                                                       id="updateReceiveTicketReportsAllInOne" action="#{preferencesController.updateUser}" />
                                          </t:htmlTag>
                                     </t:htmlTag>

                                     <t:htmlTag value="fieldset" rendered="#{preferencesController.userManager}">
                                          <t:htmlTag value="legend">
                                              <t:htmlTag value="span" >
                                                        <h:outputText value="#{msgs['MANAGER_PREFERENCES.SHOW_TICKET_AFTER_CLOSURE.TITLE']}"/>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                            <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="showTicketAfterClosure"
                                                value="#{preferencesController.currentUser.showTicketAfterClosure}"/>
                                            <e:outputLabel for="showTicketAfterClosure" value="#{msgs['MANAGER_PREFERENCES.TEXT.SHOW_TICKET_AFTER_CLOSURE']} " />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-submit">
                                                <e:commandButton value="#{msgs['_.BUTTON.UPDATE']}"
                                                       id="updateShowTicketAfterClosure" action="#{preferencesController.updateUser}" />
                                          </t:htmlTag>
                                     </t:htmlTag>

                                     <t:htmlTag value="fieldset">
                                          <t:htmlTag value="legend">
                                              <t:htmlTag value="span" >
                                                        <h:outputText value="#{msgs['MANAGER_PREFERENCES.FAQ_REPORTS.TITLE']}"/>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-item form-submit">
                                            <e:commandButton id="toggleFaqReportsButton" action="#{preferencesController.toggleFaqReports}"
                                                value="#{msgs[preferencesController.currentUser.receiveFaqReports ? 'MANAGER_PREFERENCES.BUTTON.DISABLE_FAQ_REPORTS' : 'MANAGER_PREFERENCES.BUTTON.ENABLE_FAQ_REPORTS']}" />
                                          </t:htmlTag>
                                     </t:htmlTag>

                                <t:htmlTag value="div" rendered="#{preferencesController.userManager}">

                                    <t:dataList id="data" value="#{preferencesController.departmentManagers}"
                                        var="departmentManager" rowIndexVar="index"
                                        rendered="#{preferencesController.currentUser.receiveManagerMonitoring or preferencesController.currentUser.receiveTicketReports}">

                                        <t:htmlTag value="fieldset" styleClass="collapsible collapsed service-preference" rendered="#{preferencesController.userManager}">
                                          <t:htmlTag value="legend">
                                              <t:htmlTag value="span" >
                                                <e:text value="#{msgs['MANAGER_PREFERENCES.DEPARTMENT_SUBTITLE']}" >
                                                    <f:param value="#{departmentManager.department.label}" />
                                                </e:text>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                          <t:htmlTag value="div">


                                          <t:htmlTag value="div" styleClass="form-block" rendered="#{preferencesController.currentUser.receiveManagerMonitoring}">
                                            <t:htmlTag value="h2">
                                                    <h:outputText value="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.HEADER']} " />
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-item form-checkbox">

                                                <e:selectOneMenu styleClass="selectOneMenu" id="monitoringProfile" onchange="javascript:{monitoringProfileOnchange(#{index}); return false;}" >
                                                    <f:selectItem itemValue="none" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.NONE']}" />
                                                    <f:selectItem itemValue="minimal" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.MINIMAL']}" />
                                                    <f:selectItem itemValue="medium" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.MEDIUM']}" />
                                                    <f:selectItem itemValue="assiduous" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.ASSIDUOUS']}" />
                                                    <f:selectItem itemValue="complete" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.COMPLETE']}" />
                                                    <f:selectItem itemValue="" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.CUSTOM']}" />
                                                </e:selectOneMenu>
                                                <e:italic id="monitoringProfileHelp" value="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.HELP']} " />
                                            </t:htmlTag>
                                          </t:htmlTag>

                                          <t:htmlTag value="div" id="monitoringCheckboxes" styleClass="form-block monitoringCheckboxes" rendered="#{preferencesController.currentUser.receiveManagerMonitoring}">
                                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                    <e:text value="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROMPT.ANY']}" />
                                                    <h:panelGroup>
                                                        <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="monitoring-any-2" onclick="javascript:{monitoringCheckboxOnclick(#{index},'any',2);}" />
                                                        <e:text id="text-any-2" value="#{msgs[ticketMonitoringI18nKeyProvider[2]]} " />
                                                        <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="monitoring-any-1" onclick="javascript:{monitoringCheckboxOnclick(#{index},'any',1);}" />
                                                        <e:text id="text-any-1" value="#{msgs[ticketMonitoringI18nKeyProvider[1]]} " />
                                                        <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="monitoring-any-0" onclick="javascript:{monitoringCheckboxOnclick(#{index},'any',0);}" />
                                                        <e:text id="text-any-0" value="#{msgs[ticketMonitoringI18nKeyProvider[0]]} " />
                                                        <e:selectOneMenu styleClass="selectOneMenu" id="type-any" onclick="javascript:{monitoringTypeOnchange(#{index},'any');}"
                                                            value="#{departmentManager.ticketMonitoringAny}" style="display: none" >
                                                            <f:selectItems value="#{preferencesController.ticketMonitoringItems}" />
                                                        </e:selectOneMenu>
                                                    </h:panelGroup>
                                                </t:htmlTag>

                                                <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                    <e:text value="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROMPT.CATEGORY']}" />
                                                    <h:panelGroup>
                                                        <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="monitoring-category-2" onclick="javascript:{monitoringCheckboxOnclick(#{index},'category',2);}" />
                                                        <e:text id="text-category-2" value="#{msgs[ticketMonitoringI18nKeyProvider[2]]} " />
                                                        <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="monitoring-category-1" onclick="javascript:{monitoringCheckboxOnclick(#{index},'category',1);}" />
                                                        <e:text id="text-category-1" value="#{msgs[ticketMonitoringI18nKeyProvider[1]]} " />
                                                        <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="monitoring-category-0" onclick="javascript:{monitoringCheckboxOnclick(#{index},'category',0);}" />
                                                        <e:text id="text-category-0" value="#{msgs[ticketMonitoringI18nKeyProvider[0]]} " />
                                                        <e:selectOneMenu styleClass="selectOneMenu" id="type-category" onclick="javascript:{monitoringTypeOnchange(#{index},'category');}"
                                                            value="#{departmentManager.ticketMonitoringCategory}" style="display: none" >
                                                            <f:selectItems value="#{preferencesController.ticketMonitoringItems}" />
                                                        </e:selectOneMenu>
                                                    </h:panelGroup>
                                                </t:htmlTag>

                                                 <t:htmlTag value="div" styleClass="form-item form-checkbox">
                                                    <e:text value="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROMPT.MANAGED']}" />
                                                    <h:panelGroup>
                                                        <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="monitoring-managed-2" onclick="javascript:{monitoringCheckboxOnclick(#{index},'managed',2);}" />
                                                        <e:text id="text-managed-2" value="#{msgs[ticketMonitoringI18nKeyProvider[2]]} " />
                                                        <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="monitoring-managed-1" onclick="javascript:{monitoringCheckboxOnclick(#{index},'managed',1);}" />
                                                        <e:text id="text-managed-1" value="#{msgs[ticketMonitoringI18nKeyProvider[1]]} " />
                                                        <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="monitoring-managed-0" onclick="javascript:{monitoringCheckboxOnclick(#{index},'managed',0);}" />
                                                        <e:text id="text-managed-0" value="#{msgs[ticketMonitoringI18nKeyProvider[0]]} " />
                                                        <e:selectOneMenu styleClass="selectOneMenu" id="type-managed" onclick="javascript:{monitoringTypeOnchange(#{index},'managed');}"
                                                            value="#{departmentManager.ticketMonitoringManaged}" style="display: none" >
                                                            <f:selectItems value="#{preferencesController.ticketMonitoringItems}" />
                                                        </e:selectOneMenu>
                                                    </h:panelGroup>
                                                </t:htmlTag>

                                          </t:htmlTag>

                                          <t:htmlTag value="div" styleClass="form-block" rendered="#{preferencesController.currentUser.receiveTicketReports}">
                                            <t:htmlTag value="h2">
                                                    <h:outputText value="#{msgs['MANAGER_PREFERENCES.REPORT.HEADER']} " />
                                            </t:htmlTag>
                                             <t:htmlTag value="div" styleClass="form-item" id="reportProfilePanel">
                                                     <e:selectOneMenu styleClass="selectOneMenu" id="reportProfile" value="#{departmentManager.reportType}"
                                                         onchange="javascript:{updateReport(#{index}, #{preferencesController.currentUser.receiveTicketReports});return false;}" >
                                                         <f:selectItem itemValue="" itemLabel="#{msgs['MANAGER_PREFERENCES.REPORT.PROFILE.NONE']}" />
                                                         <f:selectItem itemValue="M" itemLabel="#{msgs['MANAGER_PREFERENCES.REPORT.PROFILE.M']}" />
                                                         <f:selectItem itemValue="MC" itemLabel="#{msgs['MANAGER_PREFERENCES.REPORT.PROFILE.MC']}" />
                                                         <f:selectItem itemValue="MF" itemLabel="#{msgs['MANAGER_PREFERENCES.REPORT.PROFILE.MF']}" />
                                                         <f:selectItem itemValue="MCF" itemLabel="#{msgs['MANAGER_PREFERENCES.REPORT.PROFILE.MCF']}" />
                                                         <f:selectItem itemValue="MFC" itemLabel="#{msgs['MANAGER_PREFERENCES.REPORT.PROFILE.MFC']}" />
                                                         <f:selectItem itemValue="MCFO" itemLabel="#{msgs['MANAGER_PREFERENCES.REPORT.PROFILE.MCFO']}" />
                                                         <f:selectItem itemValue="MFCO" itemLabel="#{msgs['MANAGER_PREFERENCES.REPORT.PROFILE.MFCO']}" />
                                                     </e:selectOneMenu>
                                             </t:htmlTag>
                                             <t:htmlTag value="div" styleClass="form-item " id="reportTimePanel">
                                                     <e:outputLabel for="reportTime1" value=" #{msgs['MANAGER_PREFERENCES.REPORT.TIME']} "/>
                                                     <e:selectOneMenu styleClass="selectOneMenu" id="reportTime1" value="#{departmentManager.reportTime1}"
                                                         onchange="javascript:{updateReport(#{index}, #{preferencesController.currentUser.receiveTicketReports});return false;}" >
                                                         <f:selectItems value="#{preferencesController.reportTimeItems}" />
                                                     </e:selectOneMenu>
                                                      <e:selectOneMenu  styleClass="selectOneMenu" id="reportTime2" value="#{departmentManager.reportTime2}" >
                                                          <f:selectItems value="#{preferencesController.reportTimeItems}" />
                                                      </e:selectOneMenu>
                                             </t:htmlTag>

                                             <t:htmlTag value="div" styleClass="form-item" id="reportWeekendPanel">
                                                    <e:selectBooleanCheckbox styleClass="selectBooleanCheckbox" id="reportWeekend" value="#{departmentManager.reportWeekend}" />
                                                    <e:text value=" #{msgs['MANAGER_PREFERENCES.REPORT.WEEKEND']}" />
                                                     <e:commandButton id="testReportButton" styleClass="button--tertiary" action="#{preferencesController.updateManagerAndTestReport}"
                                                         value="#{msgs['MANAGER_PREFERENCES.BUTTON.TEST_REPORT']}" >
                                                         <f:param value="#{departmentManager.department.label}" />
                                                         <t:updateActionListener value="#{departmentManager}"
                                                             property="#{preferencesController.departmentManagerToUpdate}" />
                                                     </e:commandButton>
                                              </t:htmlTag>
                                          </t:htmlTag>
                                             <t:htmlTag value="div" styleClass="form-block">
                                                        <t:htmlTag value="div" styleClass="form-item form-submit" >
                                                            <e:commandButton id="updateButton" action="#{preferencesController.updateManager}"
                                                                value="#{msgs['MANAGER_PREFERENCES.BUTTON.UPDATE']}" >
                                                                <f:param value="#{departmentManager.department.label}" />
                                                                <t:updateActionListener value="#{departmentManager}"
                                                                    property="#{preferencesController.departmentManagerToUpdate}" />
                                                            </e:commandButton>
                                                       </t:htmlTag>
                                             </t:htmlTag>
                                        </t:htmlTag>
                                        </t:htmlTag>
                                    </t:dataList>


                                </t:htmlTag>
                            </e:form>

                            <h:outputText value="<script type=&quot;text/javascript&quot;>" escape="false" />
                            <t:dataList
                                id="initData" value="#{preferencesController.departmentManagers}"
                                var="departmentManager" rowIndexVar="index">
                                <h:outputText
                                    value="initMonitoring(#{index}, #{departmentManager.ticketMonitoringAny}, #{departmentManager.ticketMonitoringCategory}, #{departmentManager.ticketMonitoringManaged});"
                                    escape="false"
                                    rendered="#{preferencesController.currentUser.receiveManagerMonitoring}" />
                                <h:outputText
                                    value="updateReport(#{index}, #{preferencesController.currentUser.receiveTicketReports});"
                                    escape="false"
                                    rendered="#{preferencesController.currentUser.receiveTicketReports}" />
                            </t:dataList>
                            <h:outputText value="</script>" escape="false" />
                    </t:htmlTag>
                </t:htmlTag>
            </t:htmlTag>
            <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
            </t:htmlTag>
        </t:htmlTag>

</e:page>
