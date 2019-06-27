/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.tree2.TreeState;
import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.helpdesk.domain.FaqScope;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.FaqEvent;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.web.beans.AbstractFirstLastNode;
import org.esupportail.helpdesk.web.beans.FaqNode;
import org.esupportail.helpdesk.web.beans.FaqScopeI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.FaqTreeModel;
import org.springframework.util.StringUtils;

/**
 * A bean to manage FAQs.
 */
public class FaqsController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3933417227566291045L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The default FAQ scope i18n key suffix of the current element.
	 */
	private String defaultFaqScopeI18nSuffix;

	/**
	 * The current department.
	 */
	private Department department;

	/**
	 * The department to update.
	 */
	private Department departmentToUpdate;

	/**
	 * The current FAQ.
	 */
	private Faq faq;

	/**
	 * The current FAQ.
	 */
	private Faq faqAllDpt;

	/**
	 * The FAQ to update.
	 */
	private Faq faqToUpdate;

	/**
	 * The sub FAQs of the current element.
	 */
	private List<Faq> subFaqs;

	/**
	 * The tree model for FAQs.
	 */
	private FaqTreeModel viewTree;

	/**
	 * The tree model for FAQs.
	 */
	private FaqTreeModel viewTreeAllDpt;

	/**
	 * The tree model to move FAQs.
	 */
	private FaqTreeModel moveTree;

	/**
	 * True for the edit interface.
	 */
	private boolean editInterface;

	/**
	 * True for the edit interface.
	 */
	private boolean editInterfaceDpt;

	/**
	 * True if the current user can edit the current item.
	 */
	private boolean userCanEdit;

	/**
	 * The target department.
	 */
	private Department targetDepartment;
	
	/**
	 * The target FAQ.
	 */
	private Faq targetFaq;

	/**
	 * Bean constructor.
	 */
	public FaqsController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		viewTree = null;
		moveTree = null;
		department = null;
		faq = null;
		subFaqs = null;
		faqToUpdate = null;
		editInterface = false;
		editInterfaceDpt = false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @return true if the current user is allowed to acces the view.
	 */
	@RequestCache
	public boolean isPageAuthorized() {
		User user = getCurrentUser();
		if (getDomainService().userCanEditFaqs(user)) {
			return true;
		}
		if (getDomainService().hasVisibleFaq(user, getClient())) {
			return true;
		}
		return false;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			return null;
		}
		reset();
		updateInterface();
		return "navigationFaqs";
	}

	/**
	 * @return the departments visible for the FAQs.
	 */
	protected List<Department> getVisibleDepartments() {
		return getDomainService().getFaqViewDepartments(
					getCurrentUser(), getSessionController().getClient());
	}

	/**
	 * @param theFaq
	 * @return true if the current user can view a FAQ.
	 */
	@RequestCache
	public boolean userCanViewFaq(final Faq theFaq) {
		return getDomainService().userCanViewFaq(
				getCurrentUser(), theFaq, getVisibleDepartments());
	}

	/**
	 * @param authType
	 * @return the params for deep links on the page.
	 */
	protected String getPermLink(final String authType) {
		return getUrlBuilder().getFaqsUrl(authType, faq);
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getPermLink(AuthUtils.APPLICATION);
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getPermLink(AuthUtils.CAS);
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getPermLink(AuthUtils.SHIBBOLETH);
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getPermLink(AuthUtils.SPECIFIC);
	}

	/**
	 * @return the scopeItems for a department
	 */
	@RequestCache
	protected List<SelectItem> getDepartmentScopeItems() {
		if (!userCanEdit) {
			return null;
		}
		List<SelectItem> scopeItems = new ArrayList<SelectItem>();
		scopeItems.add(new SelectItem(
				FaqScope.DEFAULT,
				getString(FaqScopeI18nKeyProvider.getI18nKey(FaqScope.DEFAULT))));
		scopeItems.add(new SelectItem(
				FaqScope.ALL,
				getString(FaqScopeI18nKeyProvider.getI18nKey(FaqScope.ALL))));
		scopeItems.add(new SelectItem(
				FaqScope.AUTHENTICATED,
				getString(FaqScopeI18nKeyProvider.getI18nKey(FaqScope.AUTHENTICATED))));
		scopeItems.add(new SelectItem(
				FaqScope.MANAGER,
				getString(FaqScopeI18nKeyProvider.getI18nKey(FaqScope.MANAGER))));
		return scopeItems;
	}

	/**
	 * @param parentEffectiveScope
	 * @param root
	 * @return the scopeItems for a FAQ entity
	 */
	@RequestCache
	protected List<SelectItem> getFaqEntityScopeItems(
			final String parentEffectiveScope,
			final boolean root) {
		if (!userCanEdit) {
			return null;
		}
		List<SelectItem> scopeItems = new ArrayList<SelectItem>();
		scopeItems.add(new SelectItem(
				FaqScope.DEFAULT,
				getString(FaqScopeI18nKeyProvider.getI18nKey(FaqScope.DEFAULT))));
		if (FaqScope.ALL.equals(parentEffectiveScope)) {
			scopeItems.add(new SelectItem(
					FaqScope.ALL,
					getString(FaqScopeI18nKeyProvider.getI18nKey(FaqScope.ALL))));
		}
		if (FaqScope.ALL.equals(parentEffectiveScope) || FaqScope.AUTHENTICATED.equals(parentEffectiveScope)) {
			scopeItems.add(new SelectItem(
					FaqScope.AUTHENTICATED,
					getString(FaqScopeI18nKeyProvider.getI18nKey(FaqScope.AUTHENTICATED))));
		}
		if (root) {
			scopeItems.add(new SelectItem(
					FaqScope.MANAGER,
					getString(FaqScopeI18nKeyProvider.getI18nKey(FaqScope.ADMIN))));
		} else {
			if (FaqScope.ALL.equals(parentEffectiveScope)
					|| FaqScope.AUTHENTICATED.equals(parentEffectiveScope)
					|| FaqScope.DEPARTMENT.equals(parentEffectiveScope)) {
				scopeItems.add(new SelectItem(
						FaqScope.DEPARTMENT,
						getString(FaqScopeI18nKeyProvider.getI18nKey(FaqScope.DEPARTMENT))));
			}
			scopeItems.add(new SelectItem(
					FaqScope.MANAGER,
					getString(FaqScopeI18nKeyProvider.getI18nKey(FaqScope.MANAGER))));
		}
		return scopeItems;
	}

	/**
	 * @return the scopeItems
	 */
	@RequestCache
	public List<SelectItem> getScopeItems() {
		if (!userCanEdit) {
			return null;
		}
		if (department != null) {
			return getDepartmentScopeItems();
		}
		if (faq == null) {
			return null;
		}
		String parentEffectiveScope;
		boolean root = faq.getDepartment() == null;
		if (faq.getParent() == null) {
			if (root) {
				parentEffectiveScope = getDomainService().getDepartmentDefaultFaqScope();
			} else {
				parentEffectiveScope = faq.getDepartment().getEffectiveDefaultFaqScope();
			}
		} else {
			parentEffectiveScope = faq.getParent().getEffectiveScope();
		}
		return getFaqEntityScopeItems(parentEffectiveScope, root);
	}

	/**
	 * @return the interfaceItems
	 */
	@RequestCache
	public List<SelectItem> getInterfaceItems() {
		if (!getDomainService().userCanEditFaqs(getCurrentUser())) {
			editInterface = false;
			return null;
		}
		List<SelectItem> interfaceItems = new ArrayList<SelectItem>();
		interfaceItems.add(new SelectItem(
				Boolean.TRUE, getString("FAQS.INTERFACE_ITEM.EDIT")));
		interfaceItems.add(new SelectItem(
				Boolean.FALSE, getString("FAQS.INTERFACE_ITEM.VIEW")));
		return interfaceItems;
	}

	/**
	 * Add FAQs to the view tree.
	 * @param parentNode
	 * @param faqs
	 */
    @SuppressWarnings("unchecked")
	protected void addViewTreeFaqs(
			final FaqNode parentNode,
			final List<Faq> faqs) {
    	for (Faq theFaq : faqs) {
        	FaqNode faqNode = new FaqNode(theFaq);
        	addViewTreeFaqs(
        			faqNode,
        			getDomainService().getSubFaqs(theFaq));
    		if (faqNode.getChildCount() > 0) {
        		((FaqNode) faqNode.getChildren().get(0)).setFirst(true);
        		((FaqNode) faqNode.getChildren().get(
        				faqNode.getChildCount() - 1)).setLast(true);
    		}
    		parentNode.getChildren().add(faqNode);
    		parentNode.setLeaf(false);
    	}
    	AbstractFirstLastNode.markFirstAndLastChildNodes(parentNode);

    }

	/**
	 * @return the root node.
	 */
    @SuppressWarnings("unchecked")
	protected FaqNode buildRootViewNode() {
    	FaqNode rootNode = new FaqNode();
    	addViewTreeFaqs(rootNode, getDomainService().getRootFaqs());
    	rootNode.markFirstAndLastChildNodes();
    	return rootNode;
    }

	/**
	 * @return the root node.
	 */
    public FaqTreeModel viewTreeForCurrentUser() {
		List<Department> visibleDepartments = getDomainService().getFaqViewDepartments(
				getCurrentUser(), null);
    	FaqNode rootNode = new FaqNode();
    	for (Department department : getDomainService().getEnabledDepartments()) {
        	FaqNode departmentNode = new FaqNode(department);
        	addFaqTreeFaqs(
        			departmentNode,
        			getDomainService().getRootFaqs(department),
        			visibleDepartments);
    		if (departmentNode.getChildCount() > 0) {
            	rootNode.getChildren().add(departmentNode);
        		rootNode.setLeaf(false);
    		}
    	}
    	if (rootNode.getChildCount() == 0) {
    		return null;
    	}
    	AbstractFirstLastNode.markFirstAndLastChildNodes(rootNode);
    	return new FaqTreeModel(rootNode);
    }
    
	protected void addFaqTreeFaqs(
			final FaqNode parentNode,
			final List<Faq> faqs,
			final List<Department> visibleDepartments) {
		for (Faq faq : faqs) {
			if (getDomainService().userCanViewFaq(
					getCurrentUser(), faq, visibleDepartments)) {
				FaqNode faqNode = new FaqNode(faq);
				addFaqTreeFaqs(faqNode, getDomainService().getSubFaqs(faq), visibleDepartments);
				AbstractFirstLastNode.markFirstAndLastChildNodes(faqNode);
				parentNode.getChildren().add(faqNode);
				parentNode.setLeaf(false);
			}
		}
	}
	/**
	 * @return the root node.
	 */
	public FaqTreeModel getViewTreeDpt(Department department) {
    	FaqNode rootNode = new FaqNode(department);
		this.department = department;
		setDepartmentInternal(department);
		addViewTreeFaqs(rootNode, getDomainService().getRootFaqs(department));
		userCanEdit = getDomainService().userCanEditDepartmentFaqs(
				getCurrentUser(), department);
    	rootNode.markFirstAndLastChildNodes();
    	viewTree = new FaqTreeModel(rootNode);
		return viewTree;
    }
    
	/**
	 * JSF callback.
	 */
	public void refreshViewTree() {
		TreeState treeState = null;
		if (viewTree != null) {
			treeState = viewTree.getTreeState();
		}
		FaqNode rootNode = buildRootViewNode();
		viewTree = new FaqTreeModel(rootNode);
		if (treeState != null) {
			viewTree.setTreeState(treeState);
		}
	}

	/**
	 * Add FAQs to the move tree.
	 * @param parentNode
	 * @param faqToMove
	 * @param faqs
	 */
    @SuppressWarnings("unchecked")
	protected void addMoveTreeFaqs(
			final FaqNode parentNode,
			final Faq faqToMove,
			final List<Faq> faqs) {
    	for (Faq theFaq : faqs) {
    		if (!theFaq.equals(faqToMove)) {
	    		FaqNode faqNode = new FaqNode(theFaq);
	    		addMoveTreeFaqs(
	    				faqNode,
	    				faqToMove,
	    				getDomainService().getSubFaqs(theFaq));
	    		AbstractFirstLastNode.markFirstAndLastChildNodes(faqNode);
	    		parentNode.getChildren().add(faqNode);
	    		parentNode.setLeaf(false);
    		}
    	}
    }

	/**
	 * @param showEmptyDepartments
	 * @param faqToMove
	 * @return the root node.
	 */
    @SuppressWarnings("unchecked")
	protected FaqNode buildRootMoveNode(
			final boolean showEmptyDepartments,
			final Faq faqToMove) {
    	FaqNode rootNode = new FaqNode();
    	if (getDomainService().userCanEditRootFaqs(getCurrentUser())) {
    		addMoveTreeFaqs(rootNode, faqToMove, getDomainService().getRootFaqs());
    	}
		AbstractFirstLastNode.markFirstAndLastChildNodes(rootNode);
    	return rootNode;
    }

	/**
	 * Refresh the move tree.
	 * @param showEmptyDepartments
	 * @param faqToMove
	 */
	public void refreshMoveTree(
			final boolean showEmptyDepartments,
			final Faq faqToMove) {
		FaqNode rootNode = buildRootMoveNode(showEmptyDepartments, faqToMove);
		moveTree = new FaqTreeModel(rootNode);
		moveTree.setActiveNode(rootNode.getIdentifier());
		targetFaq = null;
		targetDepartment = null;
	}

	/**
	 * JSF callback.
	 */
	public void updateInterface() {
		if (logger.isDebugEnabled()) {
			logger.debug("updateInterface()...");
		}
		if(getDepartment() != null) {
			getViewTreeDpt(getDepartment());
		} else {
			refreshViewTree();
			viewTreeAllDpt = viewTreeForCurrentUser();
		}
		if (faq != null) {
			setFaq(faq);
		} else if (department != null) {
			setDepartment(department);
		} else {
			setRoot();
		}
	}

	/**
	 * JSF callback.
	 */
	public void moveFaqUp() {
		getDomainService().moveFaqUp(faqToUpdate);
		updateInterface();
	}

	/**
	 * JSF callback.
	 */
	public void moveFaqDown() {
		getDomainService().moveFaqDown(faqToUpdate);
		updateInterface();
	}

	/**
	 * JSF callback.
	 */
	public void moveFaqFirst() {
		getDomainService().moveFaqFirst(faqToUpdate);
		updateInterface();
	}

	/**
	 * JSF callback.
	 */
	public void moveFaqLast() {
		getDomainService().moveFaqLast(faqToUpdate);
		updateInterface();
	}

	/**
	 * Add a FAQ.
	 */
	public void addFaq() {
		Faq newFaq = new Faq();
		newFaq.setLabel(getString("FAQS.TEXT.NEW_FAQ_LABEL"));
		newFaq.setScope(FaqScope.DEFAULT);
		
		if (faq != null) {
			newFaq.setParent(faq);
			if(faq.getDepartment() != null) {
				newFaq.setDepartment(faq.getDepartment());
				getDomainService().addFaq(newFaq);
				getViewTreeDpt(faq.getDepartment());
			} else {
				getDomainService().addFaq(newFaq);
				updateInterface();
			}
		} else 
			if (department != null) {
			newFaq.setParent(null);
			newFaq.setDepartment(department);
			getDomainService().addFaq(newFaq);
			getViewTreeDpt(department);
		} else {
			getDomainService().addFaq(newFaq);
			updateInterface();
		}

	//	this.faq = newFaq;
		setFaq(newFaq);
		setFaqToUpdate(newFaq);
		getDomainService().addFaqEvent(FaqEvent.create(getCurrentUser(), newFaq));
	}

	/**
	 * JSF callback.
	 */
	public void updateFaq() {
		if (!StringUtils.hasText(faqToUpdate.getLabel())) {
			addErrorMessage(null, "FAQS.MESSAGE.ENTER_TITLE");
			return;
		}
		faqToUpdate.setLastUpdateNow();
		getDomainService().updateFaq(faqToUpdate);
		refreshViewTree();
		setFaq(faqToUpdate);
		getDomainService().addFaqEvent(FaqEvent.update(getCurrentUser(), faqToUpdate));
	}

	/**
	 * JSF callback.
	 */
	public void updateFaq(Department department) {
		if (!StringUtils.hasText(faqToUpdate.getLabel())) {
			addErrorMessage(null, "FAQS.MESSAGE.ENTER_TITLE");
			return;
		}
		faqToUpdate.setLastUpdateNow();
		getDomainService().updateFaq(faqToUpdate);
		getViewTreeDpt(department);
		setFaq(faqToUpdate);
		getDomainService().addFaqEvent(FaqEvent.update(getCurrentUser(), faqToUpdate));
	}
	/**
	 * JSF callback.
	 */
	public void deleteFaq() {
		getDomainService().deleteFaq(faqToUpdate);
		if(faqToUpdate.getDepartment() != null){
			getViewTreeDpt(faqToUpdate.getDepartment());
		} else {
			refreshViewTree();
		}
		if (faqToUpdate.getParent() != null) {
			setFaq(faqToUpdate.getParent());
		} else if (faqToUpdate.getDepartment() != null) {
			setDepartment(faqToUpdate.getDepartment());
		} else {
			setRoot();
		}
		getDomainService().addFaqEvent(FaqEvent.delete(getCurrentUser(), faqToUpdate));
	}

	public String back() {
		setEditInterfaceDpt(false);
		return "back";
	}
	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String moveFaq() {
		refreshMoveTree(true, faqToUpdate);
		return "moveFaq";
	}
	
	/**
	 * JSF callback.
	 */
	public String moveFaq(Department department) {
		moveTree = getViewTreeDpt(department);
		targetFaq = null;
		targetDepartment = null;
		return "moveFaqDpt";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doMoveFaq() {
		Department origDepartment = faqToUpdate.getDepartment();
		boolean move = false;
		if (origDepartment != null && !origDepartment.equals(targetDepartment)) {
			move = true;
		}
		if (targetDepartment != null && !targetDepartment.equals(origDepartment)) {
			move = true;
		}
		getDomainService().moveFaq(faqToUpdate, targetDepartment, targetFaq);
		updateInterface();
		if (move) {
			getDomainService().addFaqEvent(
					FaqEvent.moveFrom(getCurrentUser(), faqToUpdate, origDepartment));
			getDomainService().addFaqEvent(
					FaqEvent.moveTo(getCurrentUser(), faqToUpdate, origDepartment));
		}
		return "faqMoved";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doMoveFaqDpt() {
		//si on ne pointe pas sur la racine du service
		if(targetFaq != null) {
			if(faqToUpdate.getId() == targetFaq.getId()) {
				addErrorMessage(null, "FAQS.MESSAGE.MOVE_CAT_REFUSED", faqToUpdate.getLabel());
				return "faqMovedDpt";
			}
		}
		getDomainService().moveFaq(faqToUpdate, faqToUpdate.getDepartment(), targetFaq);
		getViewTreeDpt(faqToUpdate.getDepartment());
		getDomainService().addFaqEvent(
				FaqEvent.moveFrom(getCurrentUser(), faqToUpdate, faqToUpdate.getDepartment()));
		getDomainService().addFaqEvent(
				FaqEvent.moveTo(getCurrentUser(), faqToUpdate, faqToUpdate.getDepartment()));
		return "faqMovedDpt";
	}
	
	/**
	 * JSF callback.
	 */
	public void updateDepartment() {
		getDomainService().updateDepartment(departmentToUpdate);
		refreshViewTree();
		setDepartment(departmentToUpdate);
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param faqs
	 * @return only the visible FAQs
	 */
	protected List<Faq> filterSubFaqs(final List<Faq> faqs) {
		List<Faq> allowedFaqs = new ArrayList<Faq>();
		for (Faq theFaq : faqs) {
			if (getDomainService().userCanViewFaq(
					getCurrentUser(), theFaq, getVisibleDepartments())) {
				allowedFaqs.add(theFaq);
			}
		}
		return allowedFaqs;
	}

	/**
	 */
	protected void setRootInternal() {
		faq = null;
//		this.department = null;
		defaultFaqScopeI18nSuffix = null;
		setSubFaqs(filterSubFaqs(getDomainService().getRootFaqs()));
		userCanEdit = getDomainService().userCanEditRootFaqs(getCurrentUser());
	}

	/**
	 * @param theDepartment the department to set
	 */
	protected void setDepartmentInternal(final Department theDepartment) {
		setRootInternal();
		department = theDepartment;
		setDepartmentToUpdate(department);
		defaultFaqScopeI18nSuffix = getDomainService().getDepartmentDefaultFaqScope();
		setSubFaqs(filterSubFaqs(getDomainService().getRootFaqs(department)));
		userCanEdit = getDomainService().userCanEditDepartmentFaqs(
				getCurrentUser(), department);
	}

	/**
	 * @param theFaq
	 * @return the suffix for the default faq scope of a FAQ
	 */
	protected String getFaqDefaultFaqScopeI18nSuffix(final Faq theFaq) {
		String suffix;
		if (theFaq.getParent() == null) {
			if (theFaq.getDepartment() == null) {
				suffix = getDomainService().getDepartmentDefaultFaqScope();
			} else {
				suffix = theFaq.getDepartment().getEffectiveDefaultFaqScope();
			}
		} else {
			suffix = theFaq.getParent().getEffectiveScope();
		}
		if (theFaq.getDepartment() == null && FaqScope.MANAGER.equals(suffix)) {
			suffix = FaqScope.ADMIN;
		}
		return suffix;
	}

	/**
	 * @param theFaq the faq to set
	 */
	protected void setFaqInternal(final Faq theFaq) {
		setRootInternal();
		faq = theFaq;
		setFaqToUpdate(faq);
		defaultFaqScopeI18nSuffix = getFaqDefaultFaqScopeI18nSuffix(faq);
		setSubFaqs(filterSubFaqs(getDomainService().getSubFaqs(faq)));
		userCanEdit = getDomainService().userCanEditFaq(
				getCurrentUser(), faq);
	}

	/**
	 * @param node
	 */
	public void setNode(final FaqNode node) {
		if (node.getFaq() != null) {
			setFaqInternal(node.getFaq());
		} else if (node.getDepartment() != null) {
			setDepartmentInternal(node.getDepartment());
		} else {
			setRootInternal();
		}
		viewTree.setActiveNode(node.getIdentifier());
		viewTree.getTreeState().setSelected(node.getIdentifier());
	}

	/**
	 * @param node
	 */
	public void setNodeAllDpt(final FaqNode node) {
		if (node.getFaq() != null) {
			setFaqInternalAllDpt(node.getFaq());
		} else if (node.getDepartment() != null) {
			setDepartmentInternal(node.getDepartment());
		} else {
			setRootInternal();
		}
		viewTreeAllDpt.setActiveNode(node.getIdentifier());
		viewTreeAllDpt.getTreeState().setSelected(node.getIdentifier());
	}

	/**
	 * @param theFaq the faq to set
	 */
	protected void setFaqInternalAllDpt(final Faq theFaq) {
		setRootInternal();
		faqAllDpt = theFaq;
		setFaqToUpdate(faqAllDpt);
		defaultFaqScopeI18nSuffix = getFaqDefaultFaqScopeI18nSuffix(faqAllDpt);
		setSubFaqs(filterSubFaqs(getDomainService().getSubFaqs(faqAllDpt)));
	}

	/**
	 */
	public void setRoot() {
		setNode(viewTree.getRootNode());
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(final Department department) {
		if (viewTree == null) {
			refreshViewTree();
		}
		FaqNode node = viewTree.findNode(department);
		if (node != null) {
			setNode(node);
		} else {
			addErrorMessage(null, "FAQS.MESSAGE.DEPARTMENT_NOT_VISIBLE",
					department.getLabel());
			setNode(viewTree.getRootNode());
		}
	}

	/**
	 * @param faq the faq to set
	 */
	public void setFaq(final Faq faq) {
		if (viewTree == null) {
			refreshViewTree();
		}
		FaqNode node = viewTree.findNode(faq);
		if (node != null) {
			setNode(node);
		} else {
			addErrorMessage(null, "FAQS.MESSAGE.FAQ_NOT_VISIBLE",
					String.valueOf(faq.getId()));
			setNode(viewTree.getRootNode());
		}
	}

	/**
	 * @return the faq
	 */
	public Faq getFaq() {
		return faq;
	}
	
	/**
	 * @return the faq
	 */
	public Faq getFaqAllDpt() {
		return faqAllDpt;
	}

	/**
	 * @return the viewTree
	 */
	public FaqTreeModel getViewTree() {
		return viewTree;
	}

	/**
	 * @return the subFaqs
	 */
	public List<Faq> getSubFaqs() {
		return subFaqs;
	}

	/**
	 * @param subFaqs the subFaqs to set
	 */
	protected void setSubFaqs(final List<Faq> subFaqs) {
		this.subFaqs = subFaqs;
	}

	/**
	 * @return the faqToUpdate
	 */
	public Faq getFaqToUpdate() {
		return faqToUpdate;
	}

	/**
	 * @param faqToUpdate the faqToUpdate to set
	 */
	public void setFaqToUpdate(final Faq faqToUpdate) {
		this.faqToUpdate = new Faq(faqToUpdate);
	}

	/**
	 * @return the editInterface
	 */
	public boolean isEditInterface() {
		return editInterface;
	}

	/**
	 * @return the editInterface
	 */
	public boolean isEditInterfaceDpt() {
		return editInterfaceDpt;
	}

	/**
	 * @param editInterface the editInterface to set
	 */
	public void setEditInterface(final boolean editInterface) {
		this.editInterface = editInterface;
	}

	/**
	 * @param editInterface the editInterface to set
	 */
	public void setEditInterfaceDpt(final boolean editInterfaceDpt) {
		this.editInterfaceDpt = editInterfaceDpt;
	}

	/**
	 * @return the userCanEdit
	 */
	public boolean isUserCanEdit() {
		return userCanEdit;
	}

	/**
	 * @param userCanEdit the userCanEdit to set
	 */
	protected void setUserCanEdit(final boolean userCanEdit) {
		this.userCanEdit = userCanEdit;
	}

	/**
	 * @return the departmentToUpdate
	 */
	public Department getDepartmentToUpdate() {
		return departmentToUpdate;
	}

	/**
	 * @param departmentToUpdate the departmentToUpdate to set
	 */
	public void setDepartmentToUpdate(final Department departmentToUpdate) {
		this.departmentToUpdate = new Department(departmentToUpdate);
	}

	/**
	 * @return the defaultFaqScopeI18nSuffix
	 */
	public String getDefaultFaqScopeI18nSuffix() {
		return defaultFaqScopeI18nSuffix;
	}

	/**
	 * @return the moveTree
	 */
	public FaqTreeModel getMoveTree() {
		return moveTree;
	}

	/**
	 * @param targetDepartment the targetDepartment to set
	 */
	public void setTargetDepartment(final Department targetDepartment) {
		this.targetDepartment = targetDepartment;
	}

	/**
	 * @param targetFaq the targetFaq to set
	 */
	public void setTargetFaq(final Faq targetFaq) {
		this.targetFaq = targetFaq;
		this.targetDepartment = targetFaq.getDepartment();
	}

	public FaqTreeModel getViewTreeAllDpt() {
		return viewTreeAllDpt;
	}

	public void setViewTreeAllDpt(FaqTreeModel viewTreeAllDpt) {
		this.viewTreeAllDpt = viewTreeAllDpt;
	}

}
