/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.EmailValidator;
import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.exceptions.WebFlowException;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.HttpUtils;
import org.esupportail.commons.utils.SystemUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.controllers.ExceptionController;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.reporting.PasswordSender;
import org.esupportail.helpdesk.services.authentication.Authenticator;

/**
 * A bean to memorize the context of the application.
 */
public class SessionController extends AbstractDomainAwareBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 351880286499400587L;

	/**
	 * Constant for state.
	 */
	private static final String NORMAL_STATE = "normal";

	/**
	 * Constant for state.
	 */
	private static final String CREATE_ACCOUNT_STATE = "createAccount";

	/**
	 * Constant for state.
	 */
	private static final String FORGOT_PASSWORD_STATE = "forgotPassword";

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The exception controller (called when logging in/out).
	 */
	private ExceptionController exceptionController;

	/**
	 * The authentication service.
	 */
	private Authenticator authenticator;

	/**
	 * The password sender.
	 */
	private PasswordSender passwordSender;

	/**
	 * The email.
	 */
	private String email;

	/**
	 * The id.
	 */
	private String id;

	/**
	 * The password.
	 */
	private String password;

	/**
	 * The display name.
	 */
	private String displayName;

	/**
	 * true to show the application login form.
	 */
	private boolean showApplicationLoginForm;

	/**
	 * true to show the specific login form.
	 */
	private boolean showSpecificLoginForm;

	/**
	 * The state.
	 */
	private String state;

	/**
	 * The params to use to build the login URL.
	 */
	private Map<String, String> loginParams;

	/**
	 * The CAS logout URL.
	 */
	private String casLogoutUrl;

	/**
	 * True to show short menus.
	 */
	private boolean showShortMenu;

	/**
	 * Url de l'ENT.
	 */
	private String urlEnt;
	

	/**
	 * Constructor.
	 */
	public SessionController() {
		super();
		email = null;
		password = null;
		showApplicationLoginForm = false;
		showSpecificLoginForm = false;
		state = NORMAL_STATE;
		displayName = null;
		loginParams = null;
		showShortMenu = false;
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(this.exceptionController, "property exceptionController of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.authenticator, "property authenticator of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.passwordSender, "property passwordSender of class "
				+ this.getClass().getName() + " can not be null");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getName() + "#" + hashCode();
	}

	/**
	 * @return the current user, or null if guest.
	 */
	@Override
	public User getCurrentUser() {
		return authenticator.getUser();
	}

	/**
	 * @return the current client, or null if undefined.
	 */
	@Override
	@RequestCache
	public InetAddress getClient() {
		try {
			return HttpUtils.getClient();
		} catch (UnknownHostException e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Unset the current user.
	 */
	public void unsetCurrentUser() {
		if (logger.isDebugEnabled()) {
			logger.debug("sessionController.unsetCurrentUser()");
		}
		authenticator.unsetUser();
		resetSessionLocale();
	}

	/**
	 * Set the current user.
	 * @param user
	 */
	public void setCurrentUser(final User user) {
		authenticator.setApplicationUser(user);
		resetSessionLocale();
	}

	/**
	 * @return true if the current user is an application user.
	 */
	public boolean isApplicationUser() {
		User user = getCurrentUser();
		return user != null && getUserStore().isApplicationUser(user);
	}

	/**
	 * @return true if the CAS login form should be printed.
	 */
	public boolean isPrintCasLoginForm() {
		if (!getUserStore().isCasAuthAllowed()) {
			return false;
		}
		if (getCurrentUser() != null) {
			return false;
		}
		return true;
	}

	/**
	 * @return true if the Shibboleth login form should be printed.
	 */
	public boolean isPrintShibbolethLoginForm() {
		if (!getUserStore().isShibbolethAuthAllowed()) {
			return false;
		}
		if (getCurrentUser() != null) {
			return false;
		}
		return true;
	}

	/**
	 * @return true if the application login form should be printed.
	 */
	public boolean isPrintApplicationLoginForm() {
		if (!getUserStore().isApplicationAuthAllowed()) {
			return false;
		}
		if (getCurrentUser() != null) {
			return false;
		}
		return true;
	}

	/**
	 * @return true if the specific login form should be printed.
	 */
	public boolean isPrintSpecificLoginForm() {
		if (!getUserStore().isSpecificAuthAllowed()) {
			return false;
		}
		if (getCurrentUser() != null) {
			return false;
		}
		return true;
	}

	/**
	 * @return true if the logout button should be printed.
	 */
	@RequestCache
	public boolean isPrintLogout() {
		if (getCurrentUser() == null) {
			return false;
		}
		if (ContextUtils.isServlet()) {
			return true;
		}
		// ContextUtils.isPortlet() == true
		return getUserStore().isApplicationUser(getCurrentUser())
		|| getUserStore().isSpecificUser(getCurrentUser());
	}

	/**
	 * Redirect after login.
	 * @param url
	 */
	protected void redirectAfterLogin(final String url) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		try {
			externalContext.redirect(url);
		} catch (IOException e) {
			throw new WebFlowException("could not redirect to URL [" + url + "]", e);
		}
		facesContext.responseComplete();
	}

	/**
	 * Add the enter param to the login params so the redirector will be called.
	 */
	protected void addEnterParam() {
		if (loginParams == null) {
			loginParams = new HashMap<String, String>();
		}
		loginParams.put("enter", "");
	}

	/**
	 * JSF callback.
	 */
	public void casLogin() {
		unsetCurrentUser();
		addEnterParam();
		redirectAfterLogin(getUrlBuilder().getUrl(AuthUtils.CAS, loginParams));
	}

	/**
	 * JSF callback.
	 */
	public void shibbolethLogin() {
		unsetCurrentUser();
		addEnterParam();
		redirectAfterLogin(getUrlBuilder().getUrl(AuthUtils.SHIBBOLETH, loginParams));
	}

	/**
	 * Authenticate thanks to the auth cookie.
	 * @return true if correctly authenticated.
	 */
	public boolean cookieLogin() {
		HttpServletRequest request = HttpUtils.getHttpServletRequest();
		if (request == null) {
			return false;
		}
		if (request.getCookies() == null) {
			return false;
		}
		for (Cookie cookie : request.getCookies()) {
			if (cookie != null && getUserStore().getAuthCookieName().equals(cookie.getName())) {
				User user = getUserStore().getUserWithAuthSecret(cookie.getValue());
				if (user == null) {
					return false;
				}
				setCurrentUser(user);
				state = NORMAL_STATE;
				return true;
			}
		}
		return false;
	}

	/**
	 * Install a auth cookie on the client.
	 */
	protected void installAuthCookie() {
		if (logger.isDebugEnabled()) {
			logger.debug("installAuthCookie()");
		}
		HttpServletResponse response = HttpUtils.getHttpServletResponse();
		if (response == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("response is null");
			}
			return;
		}
		User user = getCurrentUser();
		Cookie cookie = getUserStore().renewAuthSecret(user);
		response.addCookie(cookie);
		if (logger.isDebugEnabled()) {
			logger.debug("added cookie " + cookie);
		}
	}

	/**
	 * Remove the auth cookie on the client.
	 */
	protected void removeAuthCookie() {
		HttpServletResponse response = HttpUtils.getHttpServletResponse();
		if (response != null) {
			User user = getCurrentUser();
			Cookie cookie = getUserStore().removeAuthSecret(user);
			response.addCookie(cookie);
		}
	}

	/**
	 * JSF callback.
	 * @param redirect true to redirect after login
	 * @return true if correctly authenticated.
	 */
	protected boolean applicationLogin(final boolean redirect) {
		if (logger.isDebugEnabled()) {
			logger.debug("applicationLogin(" + redirect + ")");
		}
		String thePassword = password;
		password = null;
		if (email == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("email is null");
			}
			addErrorMessage("applicationLoginForm:email", "SESSION.MESSAGE.NULL_EMAIL");
			return false;
		}
		if (!EmailValidator.getInstance().isValid(email)) {
			if (logger.isDebugEnabled()) {
				logger.debug("email is not valid");
			}
			addErrorMessage(null, "SESSION.MESSAGE.INVALID_EMAIL", email);
			return false;
		}
		if (thePassword == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("password is null");
			}
			addErrorMessage("applicationLoginForm:password", "SESSION.MESSAGE.NULL_PASSWORD");
			return false;
		}
		try {
			User user = getUserStore().authenticateApplicationUser(email, thePassword);
			if (user != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("user successfully authenticated");
				}
				setCurrentUser(user);
				addInfoMessage(null, "SESSION.MESSAGE.LOGIN_SUCCESS");
				state = NORMAL_STATE;
				installAuthCookie();
				if (redirect) {
					redirectAfterLogin(
							getUrlBuilder().getUrl(AuthUtils.APPLICATION, loginParams));
				}
				return true;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("password [" + thePassword + "] does not match");
			}
		} catch (UserNotFoundException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("user not found");
			}
			// see below
		}
		addErrorMessage(null, "SESSION.MESSAGE.LOGIN_FAILURE");
		return false;
	}

	/**
	 * JSF callback.
	 */
	public void applicationLoginFromForm() {
		applicationLogin(true);
	}

	/**
	 * Try to login from the redirector.
	 */
	public void applicationLoginFromRedirector() {
		applicationLogin(false);
	}

	/**
	 * JSF callback.
	 */
	public void specificLogin() {
		if (logger.isDebugEnabled()) {
			logger.debug("specificLogin()");
		}
		String thePassword = password;
		password = null;
		if (id == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("id is null");
			}
			addErrorMessage("specificLoginForm:id", "SESSION.MESSAGE.NULL_ID");
			return;
		}
		if (thePassword == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("password is null");
			}
			addErrorMessage("specificLoginForm:password", "SESSION.MESSAGE.NULL_PASSWORD");
			return;
		}
		try {
			User user = getUserStore().authenticateSpecificUser(id, thePassword);
			if (user != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("user successfully authenticated");
				}
				setCurrentUser(user);
				addInfoMessage(null, "SESSION.MESSAGE.LOGIN_SUCCESS");
				state = NORMAL_STATE;
				installAuthCookie();
				redirectAfterLogin(
						getUrlBuilder().getUrl(AuthUtils.SPECIFIC, loginParams));
				return;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("password [" + thePassword + "] does not match");
			}
		} catch (UserNotFoundException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("user not found");
			}
			// see below
		}
		addErrorMessage(null, "SESSION.MESSAGE.LOGIN_FAILURE");
		return;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 * @throws IOException
	 */
	public String logout() throws IOException {
		if (ContextUtils.isPortlet()) {
			User user = getCurrentUser();
			Assert.notNull(user, "can not logout (not logged in)");
			Assert.isTrue(
					getDomainService().getUserStore().isApplicationUser(getCurrentUser()),
					"can not logout (CAS or Shibboleth user in portlet mode)");
			removeAuthCookie();
			unsetCurrentUser();
			// calling this method will reset all the beans of the application
			exceptionController.restart();
			return "navigationWelcome";
		}
		// servlet
		if (getDomainService().getUserStore().isApplicationUser(getCurrentUser())) {
			removeAuthCookie();
			unsetCurrentUser();
			exceptionController.restart();
			return "navigationWelcome";
		}
		if (getDomainService().getUserStore().isShibbolethUser(getCurrentUser())) {
			unsetCurrentUser();
			exceptionController.restart();
			return "navigationWelcome";
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		String returnUrl = request.getRequestURL().toString().replaceFirst("/stylesheets/[^/]*$", "");
		String forwardUrl;
		Assert.hasText(
				casLogoutUrl,
				"property casLogoutUrl of class " + getClass().getName() + " is null");
		forwardUrl = String.format(casLogoutUrl, StringUtils.utf8UrlEncode(returnUrl));
		// note: the session beans will be kept even when invalidating
		// the session so they have to be reset (by the exception controller).
		// We invalidate the session however for the other attributes.
		request.getSession().invalidate();
		request.getSession(true);
		// calling this method will reset all the beans of the application
		exceptionController.restart();
		externalContext.redirect(forwardUrl);
		facesContext.responseComplete();
		return null;
	}

	/**
	 * JSF callback.
	 */
	public void forgotPassword() {
		if (email == null) {
			addErrorMessage("forgotPasswordForm:email", "SESSION.MESSAGE.NULL_EMAIL");
			return;
		}
		if (!EmailValidator.getInstance().isValid(email)) {
			addErrorMessage(null, "SESSION.MESSAGE.INVALID_EMAIL", email);
			return;
		}
		try {
			passwordSender.sendPasswordEmail(
					getUserStore().getExistingApplicationUser(email), getCurrentUserLocale());
			addInfoMessage(null, "SESSION.MESSAGE.PASSWORD_SENT", email);
			state = NORMAL_STATE;
			return;
		} catch (UserNotFoundException e) {
			addErrorMessage(null, "SESSION.MESSAGE.UNKNOWN_LOGIN", email);
			state = CREATE_ACCOUNT_STATE;
		}
	}

	/**
	 * JSF callback.
	 */
	public void createAccount() {
		if (email == null) {
			addErrorMessage("createAccountForm:email", "SESSION.MESSAGE.NULL_EMAIL");
			return;
		}
		if (displayName == null) {
			addErrorMessage("createAccountForm:displayName", "SESSION.MESSAGE.NULL_DISPLAY_NAME");
			return;
		}
		if (!EmailValidator.getInstance().isValid(email)) {
			addErrorMessage(null, "SESSION.MESSAGE.INVALID_EMAIL", email);
			return;
		}
		try {
			getUserStore().getExistingApplicationUser(email);
			addErrorMessage(null, "SESSION.MESSAGE.ACCOUNT_EXISTS", email);
			gotoForgotPassword();
			return;
		} catch (UserNotFoundException e) {
			getUserStore().createApplicationUser(email, displayName, getLocale());
			addInfoMessage(null, "SESSION.MESSAGE.ACCOUNT_CREATED", email);
			state = NORMAL_STATE;
			return;
		}
	}

	/**
	 * JSF callback.
	 */
	public void gotoLogin() {
		state = NORMAL_STATE;
	}

	/**
	 * JSF callback.
	 */
	public void gotoForgotPassword() {
		password = null;
		state = FORGOT_PASSWORD_STATE;
	}

	/**
	 * JSF callback.
	 */
	public void gotoCreateAccount() {
		password = null;
		state = CREATE_ACCOUNT_STATE;
	}

	/**
	 * @return true to show the submit popup text.
	 */
	public boolean isShowSubmitPopupText() {
		if (getCurrentUser() == null) {
			return true;
		}
		return getCurrentUser().isShowSubmitPopupText();
	}

	/**
	 * @return true to show the submit popup image.
	 */
	public boolean isShowSubmitPopupImage() {
		if (getCurrentUser() == null) {
			return true;
		}
		return getCurrentUser().isShowSubmitPopupImage();
	}

	/**
	 * @return true to freeze the screen on submit.
	 */
	public boolean isFreezeScreenOnSubmit() {
		if (getCurrentUser() == null) {
			return true;
		}
		return getCurrentUser().isFreezeScreenOnSubmit();
	}

	/**
	 * @param exceptionController the exceptionController to set
	 */
	public void setExceptionController(final ExceptionController exceptionController) {
		this.exceptionController = exceptionController;
	}

	/**
	 * @param authenticator the authenticator
	 */
	public void setAuthenticator(final Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(final String email) {
		this.email = StringUtils.nullIfEmpty(email);
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = StringUtils.nullIfEmpty(password);
	}

	/**
	 * @return the showApplicationLoginForm
	 */
	public boolean isShowApplicationLoginForm() {
		return showApplicationLoginForm;
	}

	/**
	 * @param showApplicationLoginForm the showApplicationLoginForm to set
	 */
	public void setShowApplicationLoginForm(final boolean showApplicationLoginForm) {
		this.showApplicationLoginForm = showApplicationLoginForm;
	}

	/**
	 * @return the showSpecificLoginForm
	 */
	public boolean isShowSpecificLoginForm() {
		return showSpecificLoginForm;
	}

	/**
	 * @param showSpecificLoginForm the showSpecificLoginForm to set
	 */
	public void setShowSpecificLoginForm(final boolean showSpecificLoginForm) {
		this.showSpecificLoginForm = showSpecificLoginForm;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(final String displayName) {
		this.displayName = StringUtils.nullIfEmpty(displayName);
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return the loginParams
	 */
	protected Map<String, String> getLoginParams() {
		return loginParams;
	}

	/**
	 * @param loginParams the loginParams to set
	 */
	public void setLoginParams(final Map<String, String> loginParams) {
		this.loginParams = loginParams;
	}

	/**
	 * @return the exceptionController
	 */
	protected  ExceptionController getExceptionController() {
		return exceptionController;
	}

	/**
	 * @return the authenticator
	 */
	protected  Authenticator getAuthenticator() {
		return authenticator;
	}

	/**
	 * @param state the state to set
	 */
	protected void setState(final String state) {
		this.state = state;
	}

	/**
	 * Set the normal state.
	 */
	public void setNormalState() {
		setState(NORMAL_STATE);
	}

	/**
	 * @return the casLogoutUrl
	 */
	protected String getCasLogoutUrl() {
		return casLogoutUrl;
	}

	/**
	 * @param casLogoutUrl the casLogoutUrl to set
	 */
	public void setCasLogoutUrl(final String casLogoutUrl) {
		this.casLogoutUrl = StringUtils.nullIfEmpty(casLogoutUrl);
	}

	/**
	 * @return the current date.
	 */
	public Timestamp getNow() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Toggle the showShortMenu flag.
	 */
	public void toggleShowShortMenu() {
		showShortMenu = !showShortMenu;
	}

	/**
	 * @return the showShortMenu
	 */
	public boolean isShowShortMenu() {
		return showShortMenu;
	}

	/**
	 * @param showShortMenu the showShortMenu to set
	 */
	protected void setShowShortMenu(final boolean showShortMenu) {
		this.showShortMenu = showShortMenu;
	}

	/**
	 * @return the free memory (Mb).
	 */
	public long getFreeMemory() {
		return SystemUtils.getFreeMemory();
	}

	/**
	 * @return the total memory (Mb).
	 */
	public long getTotalMemory() {
		return SystemUtils.getTotalMemory();
	}

	/**
	 * @return the max memory (Mb).
	 */
	public long getMaxMemory() {
		return SystemUtils.getMaxMemory();
	}

	/**
	 * @return the passwordSender
	 */
	protected PasswordSender getPasswordSender() {
		return passwordSender;
	}

	/**
	 * @param passwordSender the passwordSender to set
	 */
	public void setPasswordSender(final PasswordSender passwordSender) {
		this.passwordSender = passwordSender;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final String id) {
		this.id = StringUtils.nullIfEmpty(id);
	}

	public String getUrlEnt() {
		return urlEnt;
	}

	public void setUrlEnt(String urlEnt) {
		this.urlEnt = urlEnt;
	}

}
