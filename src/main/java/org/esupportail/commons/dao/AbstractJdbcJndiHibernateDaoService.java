/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.dao;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * A simple abstract DAO implementation.
 */
public abstract class AbstractJdbcJndiHibernateDaoService 
extends AbstractGenericHibernateDaoService {

	/**
	 * True to use JDBC.
	 */
	private boolean useJdbc;
	
	/**
	 * True to use JNDI.
	 */
	private boolean useJndi;
	
	/**
	 * The name of the JDBC Hibernate template bean.
	 */
	private String jdbcHibernateTemplateBeanName;
	
	/**
	 * The name of the JNDI Hibernate template bean.
	 */
	private String jndiHibernateTemplateBeanName;
	
	/**
	 * The JDBC Hibernate template. 
	 */
	private HibernateTemplate jdbcHibernateTemplate;

	/**
	 * The JNDI Hibernate template. 
	 */
	private HibernateTemplate jndiHibernateTemplate;

	/**
	 * Bean constructor.
	 */
	protected AbstractJdbcJndiHibernateDaoService() {
		super();
		useJdbc = false;
		useJndi = false;
	}

	/**
	 * @see org.esupportail.commons.dao.AbstractGenericHibernateDaoService#initDao()
	 */
	@Override
	public void initDao() throws Exception {
		if (jdbcHibernateTemplateBeanName == null && useJdbc ) {
			throw new ConfigException("property [jdbcHibernateTemplateBeanName] of "
					+ getClass().getName() + "] can not be null when property [useJdbc] is true");
		}
		if (jndiHibernateTemplateBeanName == null && useJndi ) {
			throw new ConfigException("property [jndiHibernateTemplateBeanName] of "
					+ getClass().getName() + "] can not be null when property [useJndi] is true");
		}
		if (!useJdbc && !useJndi) {
			throw new ConfigException("properties [useJdbc] and [useJndi] of class [" 
					+ getClass().getName() + "] can not be both false");
		}
	}

	/**
	 * @see org.esupportail.commons.dao.AbstractGenericHibernateDaoService#getHibernateTemplate()
	 */
	@Override
	protected HibernateTemplate getHibernateTemplate() {
		HibernateTemplate hibernateTemplate;
		if (ContextUtils.isWeb()) {
			if (useJndi) {
				hibernateTemplate = getJndiHibernateTemplate();
			} else {
				hibernateTemplate = getJdbcHibernateTemplate();
			}
		} else {
			if (!useJdbc) {
				throw new ConfigException(getClass() + ": batch commands "
						+ "can not be used when property [uesJdbc] "
						+ "is false");
			}
			hibernateTemplate = getJdbcHibernateTemplate();
		}
		return hibernateTemplate;
	}

	/**
	 * @param name 
	 * @return the Hibernate template that corresponds to a name.
	 */
	protected HibernateTemplate retrieveHibernateTemplate(
			final String name) {
		if (name == null) {
			return null;
		}
		return (HibernateTemplate) BeanUtils.getBean(name);
	}

	/**
	 * @return the jdbcHibernateTemplate
	 */
	protected HibernateTemplate getJdbcHibernateTemplate() {
		if (jdbcHibernateTemplate == null) {
			jdbcHibernateTemplate = retrieveHibernateTemplate(jdbcHibernateTemplateBeanName);
		}
		return jdbcHibernateTemplate;
	}

	/**
	 * @return the jndiHibernateTemplate
	 */
	protected HibernateTemplate getJndiHibernateTemplate() {
		if (jndiHibernateTemplate == null) {
			jndiHibernateTemplate = retrieveHibernateTemplate(jndiHibernateTemplateBeanName);
		}
		return jndiHibernateTemplate;
	}

	/**
	 * @return the jdbcHibernateTemplateBeanName
	 */
	protected String getJdbcHibernateTemplateBeanName() {
		return jdbcHibernateTemplateBeanName;
	}

	/**
	 * @param jdbcHibernateTemplateBeanName the jdbcHibernateTemplateBeanName to set
	 */
	public void setJdbcHibernateTemplateBeanName(
			final String jdbcHibernateTemplateBeanName) {
		this.jdbcHibernateTemplateBeanName = StringUtils.nullIfEmpty(jdbcHibernateTemplateBeanName);
	}

	/**
	 * @return the jndiHibernateTemplateBeanName
	 */
	protected String getJndiHibernateTemplateBeanName() {
		return jndiHibernateTemplateBeanName;
	}

	/**
	 * @param jndiHibernateTemplateBeanName the jndiHibernateTemplateBeanName to set
	 */
	public void setJndiHibernateTemplateBeanName(
			final String jndiHibernateTemplateBeanName) {
		this.jndiHibernateTemplateBeanName = StringUtils.nullIfEmpty(jndiHibernateTemplateBeanName);
	}

	/**
	 * @return the useJdbc
	 */
	protected boolean isUseJdbc() {
		return useJdbc;
	}

	/**
	 * @param useJdbc the useJdbc to set
	 */
	public void setUseJdbc(final boolean useJdbc) {
		this.useJdbc = useJdbc;
	}

	/**
	 * @return the useJndi
	 */
	protected boolean isUseJndi() {
		return useJndi;
	}

	/**
	 * @param useJndi the useJndi to set
	 */
	public void setUseJndi(final boolean useJndi) {
		this.useJndi = useJndi;
	}

}
