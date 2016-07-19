/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.test;

import org.esupportail.commons.batch.WebApplicationEnvironment;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.AbstractTransactionalSpringContextTests;

/**
 * Based on AbstractDependencyInjectionSpringContextTests 
 * 
 * AbstractDependencyInjectionSpringContextTests seems not take care of web
 * scopes ...)
 * 
 * This works with web scope spring beans (session and request scope) if you use WebApplicationFilter 
 * in your test classes.
 * 
 * To use it you can write something like this :
 * 
 * <PRE>
public class TestUserLoginBean extends AbstractTest {

	public void testAuthenticate() throws Exception {

		new WebApplicationFilter(getWebApplicationUtils(), new FilterChain() {

			public void doFilter(
					final ServletRequest arg0, 
					final ServletResponse arg1) 
			throws IOException, ServletException {
			

				UserLoginController userLogin = 
					(UserLoginController) applicationContext.getBean("userLoginController");
				userLogin.setUsername("invite");
				userLogin.setPassword("azerty");
				userLogin.authenticate();
				boolean isAuthenticated = userLogin.isAuthenticated();
				assertTrue(isAuthenticated);
			}

		).run();

	}
}
 *}</PRE>
 * 
 * 
 * @see WebApplicationEnvironment
 * 
 */
public abstract class AbstractTest extends AbstractTransactionalSpringContextTests {

//	/**
//	 * A logger.
//	 */
//  PA: if used, set assessors.
//	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * The application environment.
	 * PA: if used, set assessors.
	 */
	private WebApplicationEnvironment webApplicationUtils = new WebApplicationEnvironment();

	/**
	 * @return the WebApplicationEnvironment
	 */
	public WebApplicationEnvironment getWebApplicationUtils() {
		return webApplicationUtils;
	}


	/**
	 * Reinitialize a new request/response for every test method => a new WebApplicationUtils.
	 * @see org.springframework.test.AbstractTransactionalSpringContextTests#onSetUpBeforeTransaction()
	 */
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		webApplicationUtils = new WebApplicationEnvironment();
	}

	/**
	 * To commit, we call setComplete => to rollabck comment it.
	 * So override this method - default is to rollback (setComplete is not called).
	 * @see org.springframework.test.AbstractTransactionalSpringContextTests#onSetUpInTransaction()
	 */
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		// to commit, we call setComplete => to rollabck comment it
		//setComplete();
	}

	/** 
	 * @see org.springframework.test.AbstractSingleSpringContextTests#loadContextLocations(java.lang.String[])
	 */
	@Override
	protected ConfigurableApplicationContext loadContextLocations(final String [] locations) throws Exception {
		return webApplicationUtils.loadContextLocations(locations);
	}

	/**
	 * @see org.springframework.test.AbstractSingleSpringContextTests#getConfigLocations()
	 */
	@Override
	protected String[] getConfigLocations() {
		return WebApplicationEnvironment.CONFIG_LOCATIONS;
	}

}
