/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.FaqScope;

/**
 * An abstract class that represents a FAQ entity (container or entry).
 * @deprecated
 */
@SuppressWarnings("serial")
@Deprecated
public abstract class AbstractDeprecatedFaqEntity implements Serializable {

    /**
     * Primary key.
     */
    private long id;

    /**
     * The parent, or null.
     */
    @SuppressWarnings("deprecation")
	private DeprecatedFaqContainer parent;

	/**
	 * The label.
	 */
	private String label;
	
	/**
	 * The scope.
	 */
	private String scope;
	
	/**
	 * The order.
	 */
	private int order;
	
	/**
	 * The content.
	 */
	private String content;

    /**
     * Date of last modification.
     */
    private Timestamp lastUpdate;
    
    /**
     * The effective scope.
     */
    private String effectiveScope;
	
	/**
	 * Bean constructor.
	 */
	public AbstractDeprecatedFaqEntity() {
		super();
		lastUpdate = new Timestamp(new Date().getTime());
		scope = FaqScope.DEFAULT;
	}

	/**
	 * Bean constructor.
	 * @param afe 
	 */
	public AbstractDeprecatedFaqEntity(final AbstractDeprecatedFaqEntity afe) {
		super();
		this.id = afe.id;
		this.parent = afe.parent;
		this.label = afe.label;
		this.scope = afe.scope;
		this.order = afe.order;
		this.content = afe.content;
		this.lastUpdate = afe.lastUpdate;
		this.effectiveScope = afe.effectiveScope;
	}

	/**
	 * @return the object converted to string.
	 */
	public String toStringInternal() {
		return "" 
		+ "id=[" + id + "]"
		+ "label=[" + label + "]"
		+ "scope=[" + scope + "]"
		+ "order=[" + order + "]"
		+ "content=[" + content + "]"
		+ "lastUpdate=[" + lastUpdate + "]"
		+ "";
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(final String content) {
		this.content = StringUtils.filterFckInput(content);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @return the lastUpdate
	 */
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(final Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * Set the last update now!
	 */
	public void setLastUpdateNow() {
		this.lastUpdate = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(final int order) {
		this.order = order;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(final String scope) {
		this.scope = scope;
	}

	/**
	 * @return the parent
	 */
	@SuppressWarnings("deprecation")
	public DeprecatedFaqContainer getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	@SuppressWarnings("deprecation")
	public void setParent(final DeprecatedFaqContainer parent) {
		this.parent = parent;
	}

	/**
	 * @return the effectiveScope
	 */
	public String getEffectiveScope() {
		return effectiveScope;
	}

	/**
	 * @param effectiveScope the effectiveScope to set
	 */
	public void setEffectiveScope(final String effectiveScope) {
		this.effectiveScope = effectiveScope;
	}

}