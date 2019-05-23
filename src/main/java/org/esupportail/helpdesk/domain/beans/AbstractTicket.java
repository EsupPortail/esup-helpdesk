/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.awt.Label;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.ActionType;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.TicketScope;
import org.esupportail.helpdesk.services.statistics.StatisticsUtils;

/**
 * An abstract class for active and archived tickets.
 */
@SuppressWarnings("serial")
public abstract class AbstractTicket implements Serializable {

	/**
	 * Magic number.
	 */
	protected static final int THOUSAND = 1000;

	/**
	 * The duration of the creation stage of ticket in milliseconds.
	 */
	protected static final long CREATION_STAGE_MILLISECONDS = 1000;

	/**
	 * Primary key.
	 */
	private long id;

	/**
	 * Owner of ticket.
	 */
	private User owner;

	/**
	 * Manager of ticket.
	 */
	private User manager;

	/**
	 * Origin of ticket.
	 */
	private String origin;

	/**
	 * Department of the ticket.
	 */
	private Department department;

	/**
	 * the computer of the ticket.
	 */
	private String computer;

	/**
	 * The label of the ticket.
	 */
	private String label;

	/**
	 * The label of the ticket.
	 */
	private int sizeLabel;

	/**
	 * Level of priority.
	 */
	private int priorityLevel;

	/**
	 * The effective scope.
	 */
	private String effectiveScope;

	/**
	 * the visibility of the ticket (anonymous or not).
	 */
	private boolean anonymous;

	/**
	 * A ticket link, null if the ticket is not connected to another ticket.
	 */
	private Ticket connectionTicket;

	/**
	 * An archived ticket link, null if the ticket is not connected to another
	 * ticket.
	 */
	private ArchivedTicket connectionArchivedTicket;

	/**
	 * A link to a FAQ container.
	 * 
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	private DeprecatedFaqContainer deprecatedConnectionFaqContainer;

	/**
	 * A link to a FAQ entry.
	 * 
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	private DeprecatedFaqEntry deprecatedConnectionFaqEntry;

	/**
	 * A link to a FAQ.
	 */
	private Faq connectionFaq;

	/**
	 * Creation date of ticket.
	 */
	private Timestamp creationDate;

	/**
	 * Spent time on manage ticket (in minutes).
	 */
	private long spentTime;

	/**
	 * the creation department in order to ease statistics.
	 */
	private Department creationDepartment;

	/**
	 * the creator of the ticket, independent of the owner.
	 */
	private User creator;

	/**
	 * The time before the ticket is taken in charge (in seconds).
	 */
	private Integer chargeTime;

	/**
	 * The time before the ticket is closed (in seconds).
	 */
	private Integer closureTime;

	/**
	 * The creation year.
	 */
	private Integer creationYear;

	/**
	 * The creation month.
	 */
	private Integer creationMonth;

	/**
	 * The creation day.
	 */
	private Integer creationDay;

	/**
	 * The creation day of week.
	 */
	private Integer creationDow;

	/**
	 * The creation hour.
	 */
	private Integer creationHour;

	/**
	 * Default constructor.
	 */
	protected AbstractTicket() {
		setCreationDate(new java.sql.Timestamp(new Date().getTime()));
		setPriorityLevel(DomainService.DEFAULT_PRIORITY_VALUE);
		setSpentTime(-1);
		setAnonymous(false);
	}

	/**
	 * Constructor.
	 * 
	 * @param t
	 */
	protected AbstractTicket(final AbstractTicket t) {
		this();
		this.id = t.id;
		this.owner = t.owner;
		this.origin = t.origin;
		this.manager = t.manager;
		this.department = t.department;
		this.computer = t.computer;
		this.label = t.label;
		this.priorityLevel = t.priorityLevel;
		this.effectiveScope = t.effectiveScope;
		this.anonymous = t.anonymous;
		this.connectionTicket = t.connectionTicket;
		this.connectionArchivedTicket = t.connectionArchivedTicket;
		this.connectionFaq = t.connectionFaq;
		this.creationDate = t.creationDate;
		this.spentTime = t.spentTime;
		this.creationDepartment = t.creationDepartment;
		this.creator = t.creator;
		this.chargeTime = t.chargeTime;
		this.closureTime = t.closureTime;
		this.creationYear = t.creationYear;
		this.creationMonth = t.creationMonth;
		this.creationDay = t.creationDay;
		this.creationDow = t.creationDow;
		this.creationHour = t.creationHour;
	}

	/**
	 * Constructor.
	 * 
	 * @param owner
	 * @param origin
	 * @param creationDepartment
	 * @param department
	 * @param computer
	 * @param label
	 * @param priorityLevel
	 */
	public AbstractTicket(final User owner, final String origin, final Department creationDepartment,
			final Department department, final String label, final String computer, final int priorityLevel) {
		this();
		setOwner(owner);
		setOrigin(origin);
		setCreationDepartment(creationDepartment);
		setDepartment(department);
		setLabel(label);
		setComputer(computer);
		setPriorityLevel(priorityLevel);
		setCreator(owner);
	}

	/**
	 * @return true if the effective scope of the ticket is PUBLIC.
	 */
	public boolean isPublicScope() {
		return TicketScope.PUBLIC.equals(effectiveScope);
	}

	/**
	 * @return true if the effective scope of the ticket is SUBJECT_ONLY.
	 */
	public boolean isSubjectOnlyScope() {
		return TicketScope.SUBJECT_ONLY.equals(effectiveScope);
	}

	/**
	 * @return true if the effective scope of the ticket is PRIVATE.
	 */
	public boolean isPrivateScope() {
		return TicketScope.PRIVATE.equals(effectiveScope);
	}

	/**
	 * @return true if the effective scope of the ticket is CAS.
	 */
	public boolean isCasScope() {
		return TicketScope.CAS.equals(effectiveScope);
	}

	/**
	 * @return the status.
	 */
	public abstract String getStatus();

	/**
	 * @return true if archived.
	 */
	public abstract boolean isArchived();

	/**
	 * Update the charge time.
	 * 
	 * @param creationStage
	 * @param actionType
	 * @param actionDate
	 * @param actionUser
	 * @param ticketManager
	 * @return true if the charge time was set.
	 */
	protected boolean updateTicketChargeTime(final boolean creationStage, final String actionType,
			final Timestamp actionDate, final User actionUser, final User ticketManager) {
		if (actionUser != null) {
			boolean setChargeTime = false;
			if (ActionType.ASSIGN.equals(actionType) || ActionType.TAKE.equals(actionType)
					|| ActionType.FREE.equals(actionType) || ActionType.POSTPONE.equals(actionType)
					|| ActionType.REFUSE.equals(actionType) || ActionType.REQUEST_INFORMATION.equals(actionType)) {
				setChargeTime = true;
			} else if (ActionType.CHANGE_CATEGORY.equals(actionType) || ActionType.CHANGE_DEPARTMENT.equals(actionType)
					|| ActionType.CHANGE_OWNER.equals(actionType)) {
				if (!creationStage) {
					setChargeTime = true;
				}
			} else if (ActionType.CHANGE_LABEL.equals(actionType) || ActionType.CHANGE_ORIGIN.equals(actionType)
					|| ActionType.CHANGE_PRIORITY.equals(actionType) || ActionType.CHANGE_SCOPE.equals(actionType)
					|| ActionType.CHANGE_SPENT_TIME.equals(actionType) || ActionType.CLOSE.equals(actionType)
					|| ActionType.GIVE_INFORMATION.equals(actionType) || ActionType.INVITE.equals(actionType)
					|| ActionType.UPLOAD.equals(actionType)) {
				setChargeTime = actionUser.equals(ticketManager);
			}
			if (setChargeTime) {
				setChargeTime((int) ((actionDate.getTime() - getCreationDate().getTime()) / THOUSAND));
				return true;
			}
		}
		return false;
	}

	/**
	 * Update the closure time.
	 * 
	 * @param actionType
	 * @param actionDate
	 */
	protected void updateTicketClosureTime(final String actionType, final Timestamp actionDate) {
		if (ActionType.CLOSE.equals(actionType) || ActionType.CLOSE_APPROVE.equals(actionType)
				|| ActionType.REFUSE.equals(actionType) || ActionType.CANCEL.equals(actionType)) {
			setClosureTime((int) ((actionDate.getTime() - getCreationDate().getTime()) / THOUSAND));
			return;
		}
		if (ActionType.REFUSE_CLOSURE.equals(actionType) || ActionType.REOPEN.equals(actionType)) {
			setClosureTime(null);
			return;
		}
	}

	/**
	 * @return the computer
	 */
	public String getComputer() {
		return computer;
	}

	/**
	 * @param computer
	 *            the computer to set
	 */
	public void setComputer(final String computer) {
		this.computer = StringUtils.nullIfEmpty(computer);
	}

	/**
	 * @return the connectionFaqContainer
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public DeprecatedFaqContainer getDeprecatedConnectionFaqContainer() {
		return deprecatedConnectionFaqContainer;
	}

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setDeprecatedConnectionFaqContainer(final DeprecatedFaqContainer x) {
		this.deprecatedConnectionFaqContainer = x;
	}

	/**
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public DeprecatedFaqEntry getDeprecatedConnectionFaqEntry() {
		return deprecatedConnectionFaqEntry;
	}

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setDeprecatedConnectionFaqEntry(final DeprecatedFaqEntry x) {
		this.deprecatedConnectionFaqEntry = x;
	}

	/**
	 * @return the connectionTicket
	 */
	public Ticket getConnectionTicket() {
		return connectionTicket;
	}

	/**
	 * @param connectionTicket
	 *            the connectionTicket to set
	 */
	public void setConnectionTicket(final Ticket connectionTicket) {
		this.connectionTicket = connectionTicket;
	}

	/**
	 * @return the creationDate
	 */
	public Timestamp getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(final Timestamp creationDate) {
		this.creationDate = creationDate;
		if (creationDate != null) {
			this.creationYear = StatisticsUtils.getYear(creationDate);
			this.creationMonth = StatisticsUtils.getMonth(creationDate);
			this.creationDay = StatisticsUtils.getDay(creationDate);
			this.creationDow = StatisticsUtils.getDayOfWeek(creationDate);
			this.creationHour = StatisticsUtils.getHour(creationDate);
		}
	}

	/**
	 * @return the creationDepartment
	 */
	public Department getCreationDepartment() {
		return creationDepartment;
	}

	/**
	 * @param creationDepartment
	 *            the creationDepartment to set
	 */
	public void setCreationDepartment(final Department creationDepartment) {
		this.creationDepartment = creationDepartment;
	}

	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(final User creator) {
		this.creator = creator;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(final Department department) {
		this.department = department;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param label
	 *            the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @return the manager
	 */
	public User getManager() {
		return manager;
	}

	/**
	 * @param manager
	 *            the manager to set
	 */
	public void setManager(final User manager) {
		this.manager = manager;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(final String origin) {
		this.origin = origin;
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(final User owner) {
		this.owner = owner;
	}

	/**
	 * @return the priorityLevel
	 */
	public int getPriorityLevel() {
		return priorityLevel;
	}

	/**
	 * @param priorityLevel
	 *            the priorityLevel to set
	 */
	public void setPriorityLevel(final int priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	/**
	 * @return the spentTime
	 */
	public long getSpentTime() {
		return spentTime;
	}

	/**
	 * @param spentTime
	 *            the spentTime to set
	 */
	public void setSpentTime(final long spentTime) {
		this.spentTime = spentTime;
	}

	/**
	 * @param effectiveScope
	 *            the effectiveScope to set
	 */
	public void setEffectiveScope(final String effectiveScope) {
		this.effectiveScope = effectiveScope;
	}

	/**
	 * @return the effectiveScope
	 */
	public String getEffectiveScope() {
		return effectiveScope;
	}

	/**
	 * @return the connectionArchivedTicket
	 */
	public ArchivedTicket getConnectionArchivedTicket() {
		return connectionArchivedTicket;
	}

	/**
	 * @param connectionArchivedTicket
	 *            the connectionArchivedTicket to set
	 */
	public void setConnectionArchivedTicket(final ArchivedTicket connectionArchivedTicket) {
		this.connectionArchivedTicket = connectionArchivedTicket;
	}

	/**
	 * @return the chargeTime
	 */
	public Integer getChargeTime() {
		return chargeTime;
	}

	/**
	 * @param chargeTime
	 *            the chargeTime to set
	 */
	public void setChargeTime(final Integer chargeTime) {
		this.chargeTime = chargeTime;
	}

	/**
	 * @return the closureTime
	 */
	public Integer getClosureTime() {
		return closureTime;
	}

	/**
	 * @param closureTime
	 *            the closureTime to set
	 */
	public void setClosureTime(final Integer closureTime) {
		this.closureTime = closureTime;
	}

	/**
	 * @return the creationYear
	 */
	public Integer getCreationYear() {
		return creationYear;
	}

	/**
	 * @param creationYear
	 *            the creationYear to set
	 */
	public void setCreationYear(final Integer creationYear) {
		this.creationYear = creationYear;
	}

	/**
	 * @return the creationMonth
	 */
	public Integer getCreationMonth() {
		return creationMonth;
	}

	/**
	 * @param creationMonth
	 *            the creationMonth to set
	 */
	public void setCreationMonth(final Integer creationMonth) {
		this.creationMonth = creationMonth;
	}

	/**
	 * @return the creationDay
	 */
	public Integer getCreationDay() {
		return creationDay;
	}

	/**
	 * @param creationDay
	 *            the creationDay to set
	 */
	public void setCreationDay(final Integer creationDay) {
		this.creationDay = creationDay;
	}

	/**
	 * @return the creationDow
	 */
	public Integer getCreationDow() {
		return creationDow;
	}

	/**
	 * @param creationDow
	 *            the creationDow to set
	 */
	public void setCreationDow(final Integer creationDow) {
		this.creationDow = creationDow;
	}

	/**
	 * @return the creationHour
	 */
	public Integer getCreationHour() {
		return creationHour;
	}

	/**
	 * @param creationHour
	 *            the creationHour to set
	 */
	public void setCreationHour(final Integer creationHour) {
		this.creationHour = creationHour;
	}

	/**
	 * @return the connectionFaq
	 */
	public Faq getConnectionFaq() {
		return connectionFaq;
	}

	/**
	 * @param connectionFaq
	 *            the connectionFaq to set
	 */
	public void setConnectionFaq(final Faq connectionFaq) {
		this.connectionFaq = connectionFaq;
	}

	public boolean getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public int getSizeLabel() {
		return label.length();
	}

	public void setSizeLabel(int sizeLabel) {
		this.sizeLabel = sizeLabel;
	}

}
