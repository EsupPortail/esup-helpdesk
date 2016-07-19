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
	<%@include file="_navigation.jsp"%>

	<h:panelGroup rendered="#{not preferencesController.pageAuthorized}" >
		<%@include file="_auth.jsp"%>
	</h:panelGroup>
 	<e:form 
		freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}" 
		showSubmitPopupText="#{sessionController.showSubmitPopupText}" 
		showSubmitPopupImage="#{sessionController.showSubmitPopupImage}" 
		id="preferencesForm" rendered="#{preferencesController.userManagerOrAdmin}" >
		<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%" >
			<e:section value="#{msgs['MANAGER_PREFERENCES.TITLE']}" />
			<h:panelGroup>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('preferencesForm:cancelButton');" >
					<e:bold value="#{msgs['_.BUTTON.BACK']} " />
					<t:graphicImage value="/media/images/back.png"
						alt="#{msgs['_.BUTTON.BACK']}" 
						title="#{msgs['_.BUTTON.BACK']}" />
				</h:panelGroup>
				<e:commandButton id="cancelButton" action="userPreferences" style="display: none"
					value="#{msgs['_.BUTTON.BACK']}" immediate="true" />
			</h:panelGroup>
		</e:panelGrid>
		<e:messages />
		<e:paragraph value="#{msgs['MANAGER_PREFERENCES.TEXT.TOP']}" />
		<e:outputLabel for="controlPanelRefreshDelay" value="#{msgs['MANAGER_PREFERENCES.TEXT.CONTROL_PANEL_REFRESH_DELAY']} " />
		<e:selectOneMenu id="controlPanelRefreshDelay" onchange="javascript:{simulateLinkClick('preferencesForm:updateUserButton');}"
			value="#{preferencesController.currentUser.controlPanelRefreshDelay}" >
			<f:selectItems value="#{preferencesController.controlPanelRefreshDelayItems}" />
		</e:selectOneMenu>
		<e:commandButton style="display: none" value="#{msgs['_.BUTTON.CHANGE']}" 
			id="updateUserButton" action="#{preferencesController.updateUser}" />
		<t:htmlTag value="br" />
        <e:commandButton id="toggleFaqReportsButton" action="#{preferencesController.toggleFaqReports}"
            style="display: none"
            value="#{msgs[preferencesController.currentUser.receiveFaqReports ? 'MANAGER_PREFERENCES.BUTTON.DISABLE_FAQ_REPORTS' : 'MANAGER_PREFERENCES.BUTTON.ENABLE_FAQ_REPORTS']}" >
        </e:commandButton>
        <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('preferencesForm:toggleFaqReportsButton');" >
            <t:graphicImage value="/media/images/enable-faq-reports.png"
                alt="#{msgs['MANAGER_PREFERENCES.BUTTON.ENABLE_FAQ_REPORTS']}" 
                title="#{msgs['MANAGER_PREFERENCES.BUTTON.ENABLE_FAQ_REPORTS']}" 
                rendered="#{not preferencesController.currentUser.receiveFaqReports}" />
            <t:graphicImage value="/media/images/disable-faq-reports.png"
                alt="#{msgs['MANAGER_PREFERENCES.BUTTON.DISABLE_FAQ_REPORTS']}" 
                title="#{msgs['MANAGER_PREFERENCES.BUTTON.DISABLE_FAQ_REPORTS']}" 
                rendered="#{preferencesController.currentUser.receiveFaqReports}" />
            <e:bold value=" #{msgs[preferencesController.currentUser.receiveFaqReports ? 'MANAGER_PREFERENCES.BUTTON.DISABLE_FAQ_REPORTS' : 'MANAGER_PREFERENCES.BUTTON.ENABLE_FAQ_REPORTS']}" />
        </h:panelGroup>
        <h:panelGroup rendered="#{preferencesController.userManager}">
	        <t:htmlTag value="br" />
	        <e:commandButton id="toggleMonitoringButton" action="#{preferencesController.toggleMonitoring}"
	            style="display: none"
	            value="#{msgs[preferencesController.currentUser.receiveManagerMonitoring ? 'MANAGER_PREFERENCES.BUTTON.DISABLE_MONITORING' : 'MANAGER_PREFERENCES.BUTTON.ENABLE_MONITORING']}" >
	        </e:commandButton>
	        <h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('preferencesForm:toggleMonitoringButton');" >
	            <t:graphicImage value="/media/images/enable-monitoring.png"
	                alt="#{msgs['MANAGER_PREFERENCES.BUTTON.ENABLE_MONITORING']}" 
	                title="#{msgs['MANAGER_PREFERENCES.BUTTON.ENABLE_MONITORING']}" 
	                rendered="#{not preferencesController.currentUser.receiveManagerMonitoring}" />
	            <t:graphicImage value="/media/images/disable-monitoring.png"
	                alt="#{msgs['MANAGER_PREFERENCES.BUTTON.DISABLE_MONITORING']}" 
	                title="#{msgs['MANAGER_PREFERENCES.BUTTON.DISABLE_MONITORING']}" 
	                rendered="#{preferencesController.currentUser.receiveManagerMonitoring}" />
	            <e:bold value=" #{msgs[preferencesController.currentUser.receiveManagerMonitoring ? 'MANAGER_PREFERENCES.BUTTON.DISABLE_MONITORING' : 'MANAGER_PREFERENCES.BUTTON.ENABLE_MONITORING']}" />
	        </h:panelGroup>
	        <t:htmlTag value="br" />
			<e:commandButton id="toggleTicketReportsButton" action="#{preferencesController.toggleTicketReports}"
				style="display: none"
				value="#{msgs[preferencesController.currentUser.receiveTicketReports ? 'MANAGER_PREFERENCES.BUTTON.DISABLE_TICKET_REPORTS' : 'MANAGER_PREFERENCES.BUTTON.ENABLE_TICKET_REPORTS']}" >
			</e:commandButton>
			<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('preferencesForm:toggleTicketReportsButton');" >
				<t:graphicImage value="/media/images/enable-ticket-reports.png"
					alt="#{msgs['MANAGER_PREFERENCES.BUTTON.ENABLE_TICKET_REPORTS']}" 
					title="#{msgs['MANAGER_PREFERENCES.BUTTON.ENABLE_TICKET_REPORTS']}" 
					rendered="#{not preferencesController.currentUser.receiveTicketReports}" />
				<t:graphicImage value="/media/images/disable-ticket-reports.png"
					alt="#{msgs['MANAGER_PREFERENCES.BUTTON.DISABLE_TICKET_REPORTS']}" 
					title="#{msgs['MANAGER_PREFERENCES.BUTTON.DISABLE_TICKET_REPORTS']}" 
					rendered="#{preferencesController.currentUser.receiveTicketReports}" />
				<e:bold value=" #{msgs[preferencesController.currentUser.receiveTicketReports ? 'MANAGER_PREFERENCES.BUTTON.DISABLE_TICKET_REPORTS' : 'MANAGER_PREFERENCES.BUTTON.ENABLE_TICKET_REPORTS']}" />
			</h:panelGroup>
			<h:panelGroup rendered="#{preferencesController.currentUser.receiveTicketReports}" >
				<t:htmlTag value="br" />
				<e:selectBooleanCheckbox id="reportsAllInOne"
					value="#{preferencesController.currentUser.receiveTicketReportsAllInOne}"
					onchange="simulateLinkClick('preferencesForm:updateUserButton');" />
				<e:outputLabel for="reportsAllInOne" value=" #{msgs['MANAGER_PREFERENCES.TEXT.GROUP_REPORTS']} " />
				<t:graphicImage value="/media/images/group-reports.png" />
			</h:panelGroup>
			<h:panelGroup >
				<t:htmlTag value="br" />
				<e:selectBooleanCheckbox id="showTicketAfterClosure"
					value="#{preferencesController.currentUser.showTicketAfterClosure}"
					onchange="simulateLinkClick('preferencesForm:updateUserButton');" />
				<e:outputLabel for="showTicketAfterClosure" value=" #{msgs['MANAGER_PREFERENCES.TEXT.SHOW_TICKET_AFTER_CLOSURE']} " />
			</h:panelGroup>
			<t:dataList id="data" value="#{preferencesController.departmentManagers}"
				var="departmentManager" rowIndexVar="index"
				rendered="#{preferencesController.currentUser.receiveManagerMonitoring or preferencesController.currentUser.receiveTicketReports}">
				<t:htmlTag value="hr" />
				<e:subSection value="#{msgs['MANAGER_PREFERENCES.DEPARTMENT_SUBTITLE']}" >
					<f:param value="#{departmentManager.department.label}" />
				</e:subSection>
				<e:panelGrid columns="3" 
					rendered="#{preferencesController.currentUser.receiveManagerMonitoring}" >
					<e:text value="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.HEADER']} " />
					<e:selectOneMenu id="monitoringProfile" onchange="javascript:{monitoringProfileOnchange(#{index}); return false;}" >
						<f:selectItem itemValue="none" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.NONE']}" />
						<f:selectItem itemValue="minimal" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.MINIMAL']}" />
						<f:selectItem itemValue="medium" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.MEDIUM']}" />
						<f:selectItem itemValue="assiduous" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.ASSIDUOUS']}" />
						<f:selectItem itemValue="complete" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.COMPLETE']}" />
						<f:selectItem itemValue="" itemLabel="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROFILE.CUSTOM']}" />
					</e:selectOneMenu>
					<e:italic id="monitoringProfileHelp" value="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.HELP']} " />
				</e:panelGrid>
				<e:panelGrid columns="2" id="monitoringCheckboxes" 
					rendered="#{preferencesController.currentUser.receiveManagerMonitoring}" >
					<e:text value="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROMPT.ANY']}" />
					<h:panelGroup> 
						<e:selectBooleanCheckbox id="monitoring-any-2" onclick="javascript:{monitoringCheckboxOnclick(#{index},'any',2);}" />
						<e:text id="text-any-2" value="#{msgs[ticketMonitoringI18nKeyProvider[2]]} " /> 
						<e:selectBooleanCheckbox id="monitoring-any-1" onclick="javascript:{monitoringCheckboxOnclick(#{index},'any',1);}" />
						<e:text id="text-any-1" value="#{msgs[ticketMonitoringI18nKeyProvider[1]]} " /> 
						<e:selectBooleanCheckbox id="monitoring-any-0" onclick="javascript:{monitoringCheckboxOnclick(#{index},'any',0);}" />
						<e:text id="text-any-0" value="#{msgs[ticketMonitoringI18nKeyProvider[0]]} " /> 
						<e:selectOneMenu id="type-any" onclick="javascript:{monitoringTypeOnchange(#{index},'any');}" 
							value="#{departmentManager.ticketMonitoringAny}" style="display: none" >
							<f:selectItems value="#{preferencesController.ticketMonitoringItems}" />
						</e:selectOneMenu>
					</h:panelGroup>
					<e:text value="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROMPT.CATEGORY']}" />
					<h:panelGroup>
						<e:selectBooleanCheckbox id="monitoring-category-2" onclick="javascript:{monitoringCheckboxOnclick(#{index},'category',2);}" />
						<e:text id="text-category-2" value="#{msgs[ticketMonitoringI18nKeyProvider[2]]} " /> 
						<e:selectBooleanCheckbox id="monitoring-category-1" onclick="javascript:{monitoringCheckboxOnclick(#{index},'category',1);}" />
						<e:text id="text-category-1" value="#{msgs[ticketMonitoringI18nKeyProvider[1]]} " /> 
						<e:selectBooleanCheckbox id="monitoring-category-0" onclick="javascript:{monitoringCheckboxOnclick(#{index},'category',0);}" />
						<e:text id="text-category-0" value="#{msgs[ticketMonitoringI18nKeyProvider[0]]} " /> 
						<e:selectOneMenu id="type-category" onclick="javascript:{monitoringTypeOnchange(#{index},'category');}" 
							value="#{departmentManager.ticketMonitoringCategory}" style="display: none" >
							<f:selectItems value="#{preferencesController.ticketMonitoringItems}" />
						</e:selectOneMenu>
					</h:panelGroup>
					<e:text value="#{msgs['MANAGER_PREFERENCES.TICKET_MONITORING.PROMPT.MANAGED']}" />
					<h:panelGroup>
						<e:selectBooleanCheckbox id="monitoring-managed-2" onclick="javascript:{monitoringCheckboxOnclick(#{index},'managed',2);}" />
						<e:text id="text-managed-2" value="#{msgs[ticketMonitoringI18nKeyProvider[2]]} " /> 
						<e:selectBooleanCheckbox id="monitoring-managed-1" onclick="javascript:{monitoringCheckboxOnclick(#{index},'managed',1);}" />
						<e:text id="text-managed-1" value="#{msgs[ticketMonitoringI18nKeyProvider[1]]} " /> 
						<e:selectBooleanCheckbox id="monitoring-managed-0" onclick="javascript:{monitoringCheckboxOnclick(#{index},'managed',0);}" />
						<e:text id="text-managed-0" value="#{msgs[ticketMonitoringI18nKeyProvider[0]]} " /> 
						<e:selectOneMenu id="type-managed" onclick="javascript:{monitoringTypeOnchange(#{index},'managed');}" 
							value="#{departmentManager.ticketMonitoringManaged}" style="display: none" >
							<f:selectItems value="#{preferencesController.ticketMonitoringItems}" />
						</e:selectOneMenu>
					</h:panelGroup>
				</e:panelGrid>
				<e:panelGrid columns="4" columnClasses="colLeftNowrap,colLeftNowrap,colLeftNowrap,colLeftNowrap" 
					rendered="#{preferencesController.currentUser.receiveTicketReports}" >
					<h:panelGroup id="reportProfilePanel" >
						<e:text value=" #{msgs['MANAGER_PREFERENCES.REPORT.HEADER']} " />
						<e:selectOneMenu id="reportProfile" value="#{departmentManager.reportType}" 
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
					</h:panelGroup>
					<h:panelGroup id="reportTimePanel" >
						<e:bold value=" #{msgs['MANAGER_PREFERENCES.REPORT.TIME']} " />
						<e:selectOneMenu id="reportTime1" value="#{departmentManager.reportTime1}" 
							onchange="javascript:{updateReport(#{index}, #{preferencesController.currentUser.receiveTicketReports});return false;}" >
							<f:selectItems value="#{preferencesController.reportTimeItems}" />
						</e:selectOneMenu>
					</h:panelGroup>
					<e:selectOneMenu id="reportTime2" value="#{departmentManager.reportTime2}" >
						<f:selectItems value="#{preferencesController.reportTimeItems}" />
					</e:selectOneMenu>
					<h:panelGroup id="reportWeekendPanel" >
						<e:selectBooleanCheckbox id="reportWeekend" value="#{departmentManager.reportWeekend}" />
						<e:text value=" #{msgs['MANAGER_PREFERENCES.REPORT.WEEKEND']}" />
						<e:commandButton id="testReportButton" action="#{preferencesController.updateManagerAndTestReport}"
							style="display: none"
							value="#{msgs['MANAGER_PREFERENCES.BUTTON.TEST_REPORT']}" >
							<f:param value="#{departmentManager.department.label}" />
							<t:updateActionListener value="#{departmentManager}" 
								property="#{preferencesController.departmentManagerToUpdate}" />
						</e:commandButton>
						<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('preferencesForm:data:#{index}:testReportButton');" >
							<e:bold value=" " />
							<t:graphicImage value="/media/images/report.png" />
							<e:bold value=" #{msgs['MANAGER_PREFERENCES.BUTTON.TEST_REPORT']} " />
						</h:panelGroup>
					</h:panelGroup>
				</e:panelGrid>
				<t:htmlTag value="br" /> 
				<e:commandButton id="updateButton" action="#{preferencesController.updateManager}"
					style="display: none"
					value="#{msgs['MANAGER_PREFERENCES.BUTTON.UPDATE']}" >
					<f:param value="#{departmentManager.department.label}" />
					<t:updateActionListener value="#{departmentManager}" 
						property="#{preferencesController.departmentManagerToUpdate}" />
				</e:commandButton>
				<h:panelGroup style="cursor: pointer" onclick="simulateLinkClick('preferencesForm:data:#{index}:updateButton');" >
					<t:graphicImage value="/media/images/save.png"
						alt="#{msgs['MANAGER_PREFERENCES.ALT.UPDATE']}" 
						title="#{msgs['MANAGER_PREFERENCES.ALT.UPDATE']}" />
					<e:bold value=" #{msgs['MANAGER_PREFERENCES.BUTTON.UPDATE']}" >
						<f:param value="#{departmentManager.department.label}" />
					</e:bold>
				</h:panelGroup>
			</t:dataList>
		</h:panelGroup>
	</e:form>
	<t:aliasBean alias="#{controller}" value="#{preferencesController}" >
		<%@include file="_signature.jsp"%>
	</t:aliasBean>
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
</e:page>
