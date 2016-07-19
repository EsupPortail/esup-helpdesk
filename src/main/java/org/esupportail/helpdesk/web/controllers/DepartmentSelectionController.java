/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.apache.myfaces.custom.tree2.TreeState;
import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.utils.strings.XmlUtils;
import org.esupportail.commons.web.beans.TreeModelBase;
import org.esupportail.commons.web.controllers.LdapSearchCaller;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReaderImpl;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelector;
import org.esupportail.helpdesk.domain.departmentSelection.DigesterUtils;
import org.esupportail.helpdesk.domain.departmentSelection.Rule;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedCondition;
import org.esupportail.helpdesk.web.beans.departmentSelection.ActionsNode;
import org.esupportail.helpdesk.web.beans.departmentSelection.RulesNode;
import org.esupportail.helpdesk.web.beans.departmentSelection.UserDefinedConditionsNode;

/**
 * A bean to manage department visibility.
 */
public class DepartmentSelectionController extends AbstractContextAwareController implements LdapSearchCaller {

	/**
	 * A direction.
	 */
	public static final String FIRST = "FIRST";
	/**
	 * A direction.
	 */
	public static final String UP = "UP";
	/**
	 * A direction.
	 */
	public static final String DOWN = "DOWN";
	/**
	 * A direction.
	 */
	public static final String LAST = "LAST";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7399807037061814125L;

	/**
	 * The department selector.
	 */
	private DepartmentSelector departmentSelector;

	/**
	 * The testUser id.
	 */
	private String ldapUid;

	/**
	 * The computer.
	 */
	private String computer;

	/**
	 * The index of the object to update/delete.
	 */
	private int indexToUpdate;

	/**
	 * The name of the object to update.
	 */
	private String nameToUpdate;

	/**
	 * The previous name of the object to update.
	 */
	private String previousName;

	/**
	 * The direction in which objects should be moved.
	 */
	private String direction;

	/**
	 * The (resolved) testClient.
	 */
	private InetAddress testClient;

	/**
	 * The (resolved) testUser.
	 */
	private User testUser;

	/**
	 * The visibility type.
	 */
	private int type;

	/**
	 * The root node for user-defined conditions.
	 */
	private UserDefinedConditionsNode userDefinedConditionsNode;

	/**
	 * The tree for user-defined conditions.
	 */
	private TreeModelBase userDefinedConditionsTree;

	/**
	 * The root node for rules.
	 */
	private RulesNode rulesNode;

	/**
	 * The tree for rules.
	 */
	private TreeModelBase rulesTree;

	/**
	 * The tree for when empty actions.
	 */
	private TreeModelBase whenEmptyActionsTree;

	/**
	 * The root node for when empty actions.
	 */
	private ActionsNode whenEmptyActionsNode;

	/**
	 * The config reader.
	 */
	private DepartmentSelectionConfigReader configReader;

	/**
	 * The download id.
	 */
	private Long downloadId;

	/**
	 * The uploaded file.
	 */
    private UploadedFile uploadedFile;

    /**
     * The config to edit.
     */
    private String config;

    /**
     * True to show the advanced features.
     */
    private boolean advanced;

	/**
	 * Bean constructor.
	 */
	public DepartmentSelectionController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(departmentSelector,
				"property departmentSelector of class "
				+ this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		setConfigReader(null);
		ldapUid = null;
		computer = null;
		type = 0;
		uploadedFile = null;
		advanced = false;
	}

	/**
	 * Set the config reader.
	 * @param configReader
	 */
	protected void setConfigReader(final DepartmentSelectionConfigReader configReader) {
		this.configReader = configReader;
		if (configReader == null) {
			userDefinedConditionsNode = null;
			userDefinedConditionsTree = null;
			rulesNode = null;
			rulesTree = null;
			whenEmptyActionsNode = null;
			whenEmptyActionsTree = null;
		} else {
			TreeState state = null;
			if (userDefinedConditionsTree != null) {
				state = userDefinedConditionsTree.getTreeState();
			}
			userDefinedConditionsNode = new UserDefinedConditionsNode(
					configReader.getUserDefinedConditions());
			userDefinedConditionsTree = new TreeModelBase(userDefinedConditionsNode);
			if (state != null) {
				userDefinedConditionsTree.setTreeState(state);
			}
			state = null;
			if (rulesTree != null) {
				state = rulesTree.getTreeState();
			}
			rulesNode = new RulesNode(configReader.getRules());
			rulesTree = new TreeModelBase(rulesNode);
			if (state != null) {
				rulesTree.setTreeState(state);
			}
			state = null;
			if (whenEmptyActionsTree != null) {
				state = whenEmptyActionsTree.getTreeState();
			}
			whenEmptyActionsNode = new ActionsNode(configReader.getWhenEmptyActions());
			whenEmptyActionsTree = new TreeModelBase(whenEmptyActionsNode);
			if (state != null) {
				whenEmptyActionsTree.setTreeState(state);
			}
			clear();
		}
	}

	/**
	 * Reset the results.
	 */
	public void clear() {
		testUser = null;
		testClient = null;
		userDefinedConditionsNode.resetEval();
		rulesNode.resetEval();
		whenEmptyActionsNode.resetEval();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[ldapUid=[" + ldapUid + "]"
		+ ", computer=[" + computer + "]"
		+ ", type=[" + type + "]"
		+ "]";
	}

	/**
	 * @return true if the current testUser is allowed to access the view.
	 */
	@RequestCache
	public boolean isPageAuthorized() {
		if (getCurrentUser() == null) {
			return false;
		}
		if (getCurrentUser().getAdmin()) {
			return true;
		}
		if (!getDomainService().isDepartmentManager(getCurrentUser())) {
			return false;
		}
		return true;
	}

	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			return null;
		}
		getSessionController().setShowShortMenu(false);
		if (configReader == null) {
			loadConfig();
		}
		return "navigationDepartmentSelection";
	}

	/**
	 * @return the utilitiesItems
	 */
	@RequestCache
	public List<SelectItem> getTypeItems() {
		List<SelectItem> typesItems = new ArrayList<SelectItem>();
		typesItems.add(new SelectItem(
				0,
				getString("DEPARTMENT_SELECTION.TEXT.TYPE.NONE")));
		typesItems.add(new SelectItem(
				DepartmentSelector.TICKET_CREATION_SELECTION,
				getString("DEPARTMENT_SELECTION.TEXT.TYPE.TICKET_CREATION")));
		typesItems.add(new SelectItem(
				DepartmentSelector.TICKET_VIEW_SELECTION,
				getString("DEPARTMENT_SELECTION.TEXT.TYPE.TICKET_VIEW")));
		typesItems.add(new SelectItem(
				DepartmentSelector.FAQ_VIEW_SELECTION,
				getString("DEPARTMENT_SELECTION.TEXT.TYPE.FAQ_VIEW")));
		typesItems.add(new SelectItem(
				DepartmentSelector.SEARCH_SELECTION,
				getString("DEPARTMENT_SELECTION.TEXT.TYPE.SEARCH")));
		return typesItems;
	}

	/**
	 * Add an error message for compile eroors.
	 * @param e
	 */
	protected void addCompileErrorMessage(
			final Exception e) {
		addErrorMessage(null, "DEPARTMENT_SELECTION.MESSAGE.COMPILE_ERROR", e.getMessage());
	}

	/**
	 * Resolve the user.
	 * @return true if resolved.
	 */
	protected boolean resolveTestUser() {
		testUser = null;
		try {
			if (ldapUid == null) {
				addErrorMessage(null, "DEPARTMENT_SELECTION.MESSAGE.ENTER_ID");
				return false;
			}
			testUser = getUserStore().getUserFromRealId(ldapUid);
			return true;
		} catch (UserNotFoundException e) {
			addErrorMessage(null, "_.MESSAGE.USER_NOT_FOUND", ldapUid);
			return false;
		}
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String test() {
		clear();
		if (type == 0) {
			addErrorMessage(null, "DEPARTMENT_SELECTION.MESSAGE.ENTER_TYPE");
			return null;
		}
		boolean error = !resolveTestUser();
		try {
			if (computer != null) {
				testClient = InetAddress.getByName(computer);
				computer = testClient.getHostName();
			}
		} catch (UnknownHostException e) {
			addErrorMessage(null, "DEPARTMENT_SELECTION.MESSAGE.UNKNOWN_HOST", computer);
			error = true;
		}
		if (error) {
			return null;
		}
		userDefinedConditionsNode.eval(getDomainService(), testUser, testClient);
		rulesNode.eval(getDomainService(), testUser, testClient, type);
		if (rulesNode.getEvalResult().isEmpty()) {
			whenEmptyActionsNode.eval(getDomainService(), type);
		}
		return "tested";
	}

	/**
	 * JSF callback.
	 */
	public void downloadConfig() {
		downloadId = DownloadUtils.setDownload(
				configReader.export().getBytes(), "config.xml", "application/octet-stream");
	}

	/**
	 * @return true if the current user can edit the department selection config.
	 */
	@RequestCache
	public boolean isCurrentUserCanEditDepartmentSelection() {
		return getDomainService().userCanEditDepartmentSelection(getCurrentUser());
	}

	/**
	 * JSF callback.
	 */
	public void uploadConfig() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return;
		}
		if (uploadedFile == null) {
			addErrorMessage(null, "DEPARTMENT_SELECTION.MESSAGE.ENTER_FILE");
			return;
		}
		String filename = uploadedFile.getName();
		// a hack for IE
		if (filename.contains("\\")) {
			filename = filename.substring(filename.lastIndexOf('\\') + 1);
		}
 		if (uploadedFile.getSize() == 0) {
			addErrorMessage(null, "DEPARTMENT_SELECTION.MESSAGE.UPLOAD_ZERO");
			return;
		}
		byte [] fileContent;
 		try {
			fileContent = uploadedFile.getBytes();
		} catch (IOException e) {
			addErrorMessage(null, "DEPARTMENT_SELECTION.MESSAGE.UPLOAD_ERROR", e.getMessage());
			return;
		}
		try {
			setConfigReader(new DepartmentSelectionConfigReaderImpl(new String(fileContent, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			addCompileErrorMessage(
					new DepartmentSelectionCompileError(
                            "error while reading file [" + uploadedFile.getName() + "]", e));
			return;
		} catch (DepartmentSelectionCompileError e) {
			addCompileErrorMessage(e);
			return;
		}
		addInfoMessage(null, "DEPARTMENT_SELECTION.MESSAGE.CONFIG_UPLOADED");
		clear();
	}

	/**
	 * JSF callback.
	 */
	public void loadConfig() {
		try {
			setConfigReader(new DepartmentSelectionConfigReaderImpl(
					getDomainService().getDepartmentSelectionConfig().getData()));
		} catch (DepartmentSelectionCompileError e) {
			addCompileErrorMessage(e);
			return;
		}
		addInfoMessage(null, "DEPARTMENT_SELECTION.MESSAGE.CONFIG_LOADED");
		clear();
	}

	/**
	 * JSF callback.
	 */
	public void saveConfig() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return;
		}
		getDomainService().setDepartmentSelectionConfig(getCurrentUser(), configReader.toString());
		addInfoMessage(null, "DEPARTMENT_SELECTION.MESSAGE.CONFIG_SAVED");
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String editConfig() {
		clear();
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return null;
		}
		config = configReader.export();
		return "editConfig";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doEditConfig() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return "navigationDepartmentSelection";
		}
		try {
			setConfigReader(new DepartmentSelectionConfigReaderImpl(config));
		} catch (DepartmentSelectionCompileError e) {
			addCompileErrorMessage(e);
			return null;
		}
		addInfoMessage(null, "DEPARTMENT_SELECTION.MESSAGE.CONFIG_UPDATED");
		clear();
		return "configEdited";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String addUserDefinedCondition() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return null;
		}
		DepartmentSelectionConfigReader newConfigReader =
			new DepartmentSelectionConfigReaderImpl(configReader);
		newConfigReader.addNewUserDefinedCondition();
		indexToUpdate = newConfigReader.getUserDefinedConditions().size() - 1;
		UserDefinedCondition userDefinedCondition =
			newConfigReader.getUserDefinedConditions().getByOrder(indexToUpdate);
		nameToUpdate = userDefinedCondition.getName();
		previousName = userDefinedCondition.getName();
		setConfigReader(new DepartmentSelectionConfigReaderImpl(newConfigReader));
		clear();
		return editUserDefinedCondition();
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String editUserDefinedCondition() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return null;
		}
		UserDefinedCondition userDefinedCondition =
			configReader.getUserDefinedConditions().getByOrder(indexToUpdate);
		config = XmlUtils.format("<edit>" + userDefinedCondition.contentToString() + "</edit>", true);
		return "editUserDefinedCondition";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doEditUserDefinedCondition() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return "navigationDepartmentSelection";
		}
		try {
			DepartmentSelectionConfigReader newConfigReader =
				new DepartmentSelectionConfigReaderImpl(configReader);
			UserDefinedCondition userDefinedCondition = new UserDefinedCondition();
			if (nameToUpdate == null) {
				addErrorMessage(null,
						"DEPARTMENT_SELECTION.MESSAGE.NULL_USER_DEFINED_CONDITION_NAME");
				return null;
			}
			userDefinedCondition.setName(nameToUpdate);
			DigesterUtils.parseUserDefinedCondition(config, userDefinedCondition);
			newConfigReader.replaceUserDefinedCondition(indexToUpdate, userDefinedCondition);
			setConfigReader(new DepartmentSelectionConfigReaderImpl(newConfigReader));
		} catch (DepartmentSelectionCompileError e) {
			addCompileErrorMessage(e);
			return null;
		}
		addInfoMessage(null, "DEPARTMENT_SELECTION.MESSAGE.CONFIG_UPDATED");
		clear();
		return "userDefinedConditionEdited";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String addRule() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return null;
		}
		DepartmentSelectionConfigReader newConfigReader =
			new DepartmentSelectionConfigReaderImpl(configReader);
		newConfigReader.addNewRule();
		indexToUpdate = newConfigReader.getRules().getRules().size() - 1;
		Rule rule = newConfigReader.getRules().getRules().get(indexToUpdate);
		nameToUpdate = rule.getName();
		setConfigReader(new DepartmentSelectionConfigReaderImpl(newConfigReader));
		clear();
		return editRule();
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String editRule() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return null;
		}
		Rule rule = configReader.getRules().getRules().get(indexToUpdate);
		config = XmlUtils.format("<edit>" + rule.contentToString() + "</edit>", true);
		return "editRule";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doEditRule() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return "navigationDepartmentSelection";
		}
		try {
			DepartmentSelectionConfigReader newConfigReader =
				new DepartmentSelectionConfigReaderImpl(configReader);
			Rule rule = new Rule();
			rule.setName(nameToUpdate);
			DigesterUtils.parseRule(config, rule);
			newConfigReader.replaceRule(indexToUpdate, rule);
			setConfigReader(new DepartmentSelectionConfigReaderImpl(newConfigReader));
		} catch (DepartmentSelectionCompileError e) {
			addCompileErrorMessage(e);
			return null;
		}
		addInfoMessage(null, "DEPARTMENT_SELECTION.MESSAGE.CONFIG_UPDATED");
		clear();
		return "ruleEdited";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String editActions() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return null;
		}
		config = XmlUtils.format("<edit>"
				+ configReader.getWhenEmptyActions().contentToString() + "</edit>", true);
		return "editActions";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doEditActions() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return "navigationDepartmentSelection";
		}
		try {
			DepartmentSelectionConfigReaderImpl newConfigReader =
				new DepartmentSelectionConfigReaderImpl(configReader);
			newConfigReader.removeWhenEmptyActions();
			DigesterUtils.parseConfigReader(config, newConfigReader);
			newConfigReader.compile();
			setConfigReader(new DepartmentSelectionConfigReaderImpl(newConfigReader));
		} catch (DepartmentSelectionCompileError e) {
			addCompileErrorMessage(e);
			return null;
		}
		addInfoMessage(null, "DEPARTMENT_SELECTION.MESSAGE.CONFIG_UPDATED");
		clear();
		return "actionsEdited";
	}

	/**
	 * Move a user-defined condition.
	 * @return a String.
	 */
	public String moveUserDefinedCondition() {
		try {
			DepartmentSelectionConfigReader newConfigReader =
				new DepartmentSelectionConfigReaderImpl(configReader);
			newConfigReader.moveUserDefinedCondition(indexToUpdate, direction);
			setConfigReader(new DepartmentSelectionConfigReaderImpl(newConfigReader));
			clear();
			return "updated";
		} catch (DepartmentSelectionCompileError e) {
			addCompileErrorMessage(e);
			return null;
		}
	}

	/**
	 * Move a rule.
	 * @return a String.
	 */
	public String moveRule() {
		try {
			DepartmentSelectionConfigReader newConfigReader =
				new DepartmentSelectionConfigReaderImpl(configReader);
			newConfigReader.moveRule(indexToUpdate, direction);
			setConfigReader(new DepartmentSelectionConfigReaderImpl(newConfigReader));
			clear();
			return "updated";
		} catch (DepartmentSelectionCompileError e) {
			addCompileErrorMessage(e);
			return null;
		}
	}

	/**
	 * @see org.esupportail.commons.web.controllers.LdapSearchCaller#getLdapUid()
	 */
	@Override
	public String getLdapUid() {
		return ldapUid;
	}

	/**
	 * @see org.esupportail.commons.web.controllers.LdapSearchCaller#setLdapUid(java.lang.String)
	 */
	@Override
	public void setLdapUid(final String ldapUid) {
		this.ldapUid = StringUtils.nullIfEmpty(ldapUid);
	}

	/**
	 * @return the computer
	 */
	public String getComputer() {
		return computer;
	}

	/**
	 * @param computer the computer to set
	 */
	public void setComputer(final String computer) {
		this.computer = StringUtils.nullIfEmpty(computer);
	}

	/**
	 * @return the testClient
	 */
	public InetAddress getTestClient() {
		return testClient;
	}

	/**
	 * @param testClient the testClient to set
	 */
	protected void setTestClient(final InetAddress testClient) {
		this.testClient = testClient;
	}

	/**
	 * @return the testUser
	 */
	public User getTestUser() {
		return testUser;
	}

	/**
	 * @param testUser the testUser to set
	 */
	protected void setTestUser(final User testUser) {
		this.testUser = testUser;
	}

	/**
	 * @return the departmentSelector
	 */
	protected DepartmentSelector getDepartmentSelector() {
		return departmentSelector;
	}

	/**
	 * @param departmentSelector the departmentSelector to set
	 */
	public void setDepartmentSelector(final DepartmentSelector departmentSelector) {
		this.departmentSelector = departmentSelector;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(final int type) {
		this.type = type;
	}

	/**
	 * @return the userDefinedConditionsTree
	 */
	public TreeModelBase getUserDefinedConditionsTree() {
		return userDefinedConditionsTree;
	}

	/**
	 * @param userDefinedConditionsTree the userDefinedConditionsTree to set
	 */
	protected void setUserDefinedConditionsTree(final TreeModelBase userDefinedConditionsTree) {
		this.userDefinedConditionsTree = userDefinedConditionsTree;
	}

	/**
	 * @return the whenEmptyActionsTree
	 */
	public TreeModelBase getWhenEmptyActionsTree() {
		return whenEmptyActionsTree;
	}

	/**
	 * @param whenEmptyActionsTree the whenEmptyActionsTree to set
	 */
	protected void setWhenEmptyActionsTree(final TreeModelBase whenEmptyActionsTree) {
		this.whenEmptyActionsTree = whenEmptyActionsTree;
	}

	/**
	 * @return the rulesTree
	 */
	public TreeModelBase getRulesTree() {
		return rulesTree;
	}

	/**
	 * @param rulesTree the rulesTree to set
	 */
	protected void setRulesTree(final TreeModelBase rulesTree) {
		this.rulesTree = rulesTree;
	}

	/**
	 * @param downloadId the downloadId to set
	 */
	protected void setDownloadId(final Long downloadId) {
		this.downloadId = downloadId;
	}

	/**
	 * @return the downloadId
	 */
	public Long getDownloadId() {
		Long id = downloadId;
		downloadId = null;
		return id;
	}

	/**
	 * @param uploadedFile the uploadedFile to set
	 */
	public void setUploadedFile(final UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	/**
	 * @return the uploadedFile
	 */
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	/**
	 * @return the config
	 */
	public String getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(final String config) {
		this.config = config;
	}

	/**
	 * Toggle the advanced flag.
	 */
	public void toggleAdvanced() {
		if (!isCurrentUserCanEditDepartmentSelection()) {
			addUnauthorizedActionMessage();
			return;
		}
		advanced = !advanced;
	}

	/**
	 * @return the advanced
	 */
	public boolean isAdvanced() {
		return advanced;
	}

	/**
	 * @return the indexToUpdate
	 */
	protected int getIndexToUpdate() {
		return indexToUpdate;
	}

	/**
	 * @param indexToUpdate the indexToUpdate to set
	 */
	public void setIndexToUpdate(final int indexToUpdate) {
		this.indexToUpdate = indexToUpdate;
	}

	/**
	 * @return the direction
	 */
	protected String getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(final String direction) {
		this.direction = direction;
	}

	/**
	 * @return the nameToUpdate
	 */
	public String getNameToUpdate() {
		return nameToUpdate;
	}

	/**
	 * @param nameToUpdate the nameToUpdate to set
	 */
	public void setNameToUpdate(final String nameToUpdate) {
		this.nameToUpdate = StringUtils.nullIfEmpty(nameToUpdate);
	}

	/**
	 * @return the previousName
	 */
	protected String getPreviousName() {
		return previousName;
	}

	/**
	 * @param previousName the previousName to set
	 */
	public void setPreviousName(final String previousName) {
		this.previousName = StringUtils.nullIfEmpty(previousName);
	}

}
