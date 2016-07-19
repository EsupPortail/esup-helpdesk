/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Response;
import org.springframework.util.StringUtils;

/**
 * The responses controller.
 */
public class ResponsesController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2863153063528865413L;

	/**
     * The global responses.
     */
    private List<Response> globalResponses;

	/**
     * The user responses.
     */
    private List<Response> userResponses;

    /**
     * The targetDepartment.
     */
    private Department targetDepartment;

	/**
     * The departments.
     */
    private List<Department> departments;

	/**
     * The department responses.
     */
    private Map<Department, List<Response>> departmentResponses;

	/**
     * True when the user can manage the department responses.
     */
    private Map<Department, Boolean> userCanManageDepartmentResponses;

	/**
     * The number of department responses.
     */
    private Map<Department, Integer> departmentResponsesNumber;

    /**
     * The response to add.
     */
    private Response responseToAdd;

    /**
     * The response to update.
     */
    private Response responseToUpdate;

    /**
     * The response to delete.
     */
    private Response responseToDelete;

	/**
	 * Bean constructor.
	 */
	public ResponsesController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		globalResponses = null;
		userResponses = null;
		responseToAdd = new Response();
		responseToDelete = null;
		responseToUpdate = null;
		departments = null;
		departmentResponses = new LinkedHashMap<Department, List<Response>>();
		departmentResponses = null;
		userCanManageDepartmentResponses = null;
		departmentResponsesNumber = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @return true if the current user is allowed to access the view.
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
		globalResponses = getDomainService().getGlobalResponses();
		userResponses = getDomainService().getUserResponses(getCurrentUser());
		departments = getDomainService().getManagedDepartments(getCurrentUser());
		departmentResponses = new LinkedHashMap<Department, List<Response>>();
		userCanManageDepartmentResponses = new LinkedHashMap<Department, Boolean>();
		departmentResponsesNumber = new LinkedHashMap<Department, Integer>();
		for (Department department : departments) {
			List<Response> responses = getDomainService().getDepartmentResponses(department);
			departmentResponses.put(
					department, responses);
			userCanManageDepartmentResponses.put(
					department,
					getDomainService().userCanManageDepartmentResponses(
							getCurrentUser(), department));
			departmentResponsesNumber.put(department, responses.size());
		}
		return "navigationResponses";
	}

	/**
	 * JSF callback.
	 * @return true if the user can manage global responses.
	 */
	@RequestCache
	public boolean isUserCanManageGlobalResponses() {
		return getDomainService().userCanManageGlobalResponses(getCurrentUser());
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String addGlobalResponse() {
		if (!isUserCanManageGlobalResponses()) {
			addUnauthorizedActionMessage();
			return null;
		}
		responseToAdd.setDepartment(null);
		responseToAdd.setUser(null);
		return "addResponse";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String addUserResponse() {
		responseToAdd.setDepartment(null);
		responseToAdd.setUser(getCurrentUser());
		return "addResponse";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String addDepartmentResponse() {
		responseToAdd.setDepartment(targetDepartment);
		responseToAdd.setUser(null);
		return "addResponse";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doAddResponse() {
		if (responseToAdd.isGlobalResponse() && !isUserCanManageGlobalResponses()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (responseToAdd.isDepartmentResponse()
				&& !userCanManageDepartmentResponses.get(responseToAdd.getDepartment())) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (!StringUtils.hasText(responseToAdd.getLabel())) {
			addErrorMessage(null, "RESPONSES.MESSAGE.ENTER_LABEL");
			return null;
		}
		if (!StringUtils.hasText(responseToAdd.getMessage())) {
			addErrorMessage(null, "RESPONSES.MESSAGE.ENTER_MESSAGE");
			return null;
		}
		getDomainService().addResponse(responseToAdd);
		responseToAdd = new Response();
		enter();
		return "responseAdded";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doDeleteResponse() {
		if (responseToDelete.isGlobalResponse() && !isUserCanManageGlobalResponses()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (responseToDelete.isDepartmentResponse()
				&& !userCanManageDepartmentResponses.get(responseToDelete.getDepartment())) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().deleteResponse(responseToDelete);
		enter();
		return "responseDeleted";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String editResponse() {
		if (responseToUpdate.isGlobalResponse() && !isUserCanManageGlobalResponses()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (responseToUpdate.isDepartmentResponse()
				&& !userCanManageDepartmentResponses.get(responseToUpdate.getDepartment())) {
			addUnauthorizedActionMessage();
			return null;
		}
		return "editResponse";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doEditResponse() {
		if (responseToUpdate.isGlobalResponse() && !isUserCanManageGlobalResponses()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (responseToUpdate.isDepartmentResponse()
				&& !userCanManageDepartmentResponses.get(responseToUpdate.getDepartment())) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().updateResponse(responseToUpdate);
		enter();
		return "responseUpdated";
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getUrlBuilder().getResponsesUrl(AuthUtils.APPLICATION);
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getUrlBuilder().getResponsesUrl(AuthUtils.CAS);
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getUrlBuilder().getResponsesUrl(AuthUtils.SHIBBOLETH);
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getUrlBuilder().getResponsesUrl(AuthUtils.SPECIFIC);
	}

	/**
	 * @return the globalResponses
	 */
	public List<Response> getGlobalResponses() {
		return globalResponses;
	}

	/**
	 * @return the number of globalResponses
	 */
	public int getGlobalResponsesNumber() {
		return globalResponses.size();
	}

	/**
	 * @param globalResponses the globalResponses to set
	 */
	protected void setGlobalResponses(final List<Response> globalResponses) {
		this.globalResponses = globalResponses;
	}

	/**
	 * @return the responseToAdd
	 */
	public Response getResponseToAdd() {
		return responseToAdd;
	}

	/**
	 * @param responseToAdd the responseToAdd to set
	 */
	protected void setResponseToAdd(final Response responseToAdd) {
		this.responseToAdd = responseToAdd;
	}

	/**
	 * @return the responseToUpdate
	 */
	public Response getResponseToUpdate() {
		return responseToUpdate;
	}

	/**
	 * @param responseToUpdate the responseToUpdate to set
	 */
	public void setResponseToUpdate(final Response responseToUpdate) {
		this.responseToUpdate = new Response(responseToUpdate);
	}

	/**
	 * @return the responseToDelete
	 */
	public Response getResponseToDelete() {
		return responseToDelete;
	}

	/**
	 * @param responseToDelete the responseToDelete to set
	 */
	public void setResponseToDelete(final Response responseToDelete) {
		this.responseToDelete = responseToDelete;
	}

	/**
	 * @return the userResponses
	 */
	public List<Response> getUserResponses() {
		return userResponses;
	}

	/**
	 * @return the number of userResponses
	 */
	public int getUserResponsesNumber() {
		return userResponses.size();
	}

	/**
	 * @param userResponses the userResponses to set
	 */
	protected void setUserResponses(final List<Response> userResponses) {
		this.userResponses = userResponses;
	}

	/**
	 * @return the departments
	 */
	public List<Department> getDepartments() {
		return departments;
	}

	/**
	 * @return the departmentResponses
	 */
	public Map<Department, List<Response>> getDepartmentResponses() {
		return departmentResponses;
	}

	/**
	 * @return the userCanManageDepartmentResponses
	 */
	public Map<Department, Boolean> getUserCanManageDepartmentResponses() {
		return userCanManageDepartmentResponses;
	}

	/**
	 * @param targetDepartment the targetDepartment to set
	 */
	public void setTargetDepartment(final Department targetDepartment) {
		this.targetDepartment = targetDepartment;
	}

	/**
	 * @return the departmentResponsesNumber
	 */
	public Map<Department, Integer> getDepartmentResponsesNumber() {
		return departmentResponsesNumber;
	}

	/**
	 * @param departmentResponsesNumber the departmentResponsesNumber to set
	 */
	protected void setDepartmentResponsesNumber(
			final Map<Department, Integer> departmentResponsesNumber) {
		this.departmentResponsesNumber = departmentResponsesNumber;
	}

}
