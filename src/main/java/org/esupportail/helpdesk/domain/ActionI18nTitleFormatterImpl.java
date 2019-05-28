/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain; 

import java.util.Locale;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userFormatting.UserFormattingService;
import org.esupportail.helpdesk.web.beans.OriginI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.PriorityI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.TicketScopeI18nKeyProvider;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A basic implementation of ActionI18nTitleFormatter.
 */ 
public class ActionI18nTitleFormatterImpl 
implements ActionI18nTitleFormatter, InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4363781891936383875L;

	/**
	 * The i18n prefix.
	 */
	private static final String PREFIX = "ACTION_TITLE.";
	
	/**
	 * The i18n service.
	 */
	private I18nService i18nService;
	
	/**
	 * The user formatting service.
	 */
	private UserFormattingService userFormattingService;
	
	/**
	 * Bean constructor.
	 */
	public ActionI18nTitleFormatterImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		Assert.notNull(this.i18nService, 
				"property i18nService of class " + this.getClass().getName() 
				+ " can not be null");
		Assert.notNull(this.userFormattingService, 
				"property userFormattingService of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleApproveClosure(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "APPROVE", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleAssign(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getUser() != null) {
			if (action.getManagerBefore() != null) {
				return i18nService.getString(PREFIX + "ASSIGN", locale, new Object [] {
						action.getDate(),
						userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
						userFormattingService.format(domainService, action.getTicket(), action.getManagerAfter(), action.getTicket().getAnonymous(), locale, currentUser),
						userFormattingService.format(domainService, action.getTicket(), action.getManagerBefore(), action.getTicket().getAnonymous(), locale, currentUser),
				});
			}
			return i18nService.getString(PREFIX + "ASSIGN_FREE_BEFORE", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
					userFormattingService.format(domainService, action.getTicket(), action.getManagerAfter(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		if (action.getManagerBefore() != null) {
			return i18nService.getString(PREFIX + "ASSIGN_APPLICATION", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getManagerAfter(), action.getTicket().getAnonymous(), locale, currentUser),
					userFormattingService.format(domainService, action.getTicket(), action.getManagerBefore(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		return i18nService.getString(PREFIX + "ASSIGN_APPLICATION_FREE_BEFORE", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getManagerAfter(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleCancel(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "CANCEL", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleCancelPostponement(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getUser() != null) {
			return i18nService.getString(PREFIX + "CANCEL_POSTPONEMENT", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		return i18nService.getString(PREFIX + "CANCEL_POSTPONEMENT_APPLICATION", locale, new Object [] {
				action.getDate(),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleChangeCategory(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getUser() != null) {
			if (action.getCategoryAfter() != null && action.getCategoryBefore() != null) {
				return i18nService.getString(PREFIX + "CHANGE_CATEGORY", locale, new Object [] {
						action.getDate(),
						userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
						action.getCategoryAfter().getLabel(),
						action.getCategoryBefore().getLabel(),
				});
			}
			if (action.getCategoryAfter() != null) {
				return i18nService.getString(PREFIX + "CHANGE_CATEGORY_FROM_NONE", 
						locale, new Object [] {
						action.getDate(),
						userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
						action.getCategoryAfter().getLabel(),
				});
			}
			return i18nService.getString(PREFIX + "CHANGE_CATEGORY_TO_NONE", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		if (action.getCategoryAfter() != null && action.getCategoryBefore() != null) {
			return i18nService.getString(PREFIX + "CHANGE_CATEGORY_APPLICATION", locale, new Object [] {
					action.getDate(),
					action.getCategoryAfter().getLabel(),
					action.getCategoryBefore().getLabel(),
			});
		}
		if (action.getCategoryAfter() != null) {
			return i18nService.getString(PREFIX + "CHANGE_CATEGORY_APPLICATION_FROM_NONE", 
					locale, new Object [] {
					action.getDate(),
					action.getCategoryAfter().getLabel(),
			});
		}
		return i18nService.getString(PREFIX + "CHANGE_CATEGORY_APPLICATION_TO_NONE", locale, new Object [] {
				action.getDate(),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleChangeComputer(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getComputerAfter() == null) {
			return i18nService.getString(PREFIX + "CHANGE_COMPUTER_NULL_AFTER", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
					action.getComputerBefore(),
			});
		}
		if (action.getComputerBefore() == null) {
			return i18nService.getString(PREFIX + "CHANGE_COMPUTER_NULL_BEFORE", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
					action.getComputerAfter(),
			});
		}
		return i18nService.getString(PREFIX + "CHANGE_COMPUTER", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
				action.getComputerAfter(),
				action.getComputerBefore(),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleChangeDepartment(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getUser() != null) {
			if (action.getDepartmentAfter() != null && action.getDepartmentBefore() != null) {
				return i18nService.getString(PREFIX + "CHANGE_DEPARTMENT", locale, new Object [] {
						action.getDate(),
						userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
						action.getDepartmentAfter().getLabel(),
						action.getDepartmentBefore().getLabel(),
				});
			}
			if (action.getDepartmentAfter() != null && action.getDepartmentBefore() != null) {
				return i18nService.getString(PREFIX + "CHANGE_DEPARTMENT_FROM_NONE", 
						locale, new Object [] {
						action.getDate(),
						userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
						action.getDepartmentAfter().getLabel(),
				});
			}
			return i18nService.getString(PREFIX + "CHANGE_DEPARTMENT_TO_NONE", 
					locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		if (action.getDepartmentAfter() != null && action.getDepartmentBefore() != null) {
			return i18nService.getString(PREFIX + "CHANGE_DEPARTMENT_APPLICATION", 
					locale, new Object [] {
					action.getDate(),
					action.getDepartmentAfter().getLabel(),
					action.getDepartmentBefore().getLabel(),
			});
		}
		if (action.getDepartmentAfter() != null && action.getDepartmentBefore() != null) {
			return i18nService.getString(PREFIX + "CHANGE_DEPARTMENT_APPLICATION_FROM_NONE", 
					locale, new Object [] {
					action.getDate(),
					action.getDepartmentAfter().getLabel(),
			});
		}
		return i18nService.getString(PREFIX + "CHANGE_DEPARTMENT_APPLICATION_TO_NONE", 
				locale, new Object [] {
				action.getDate(),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleChangeLabel(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "CHANGE_LABEL", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
				action.getLabelAfter(),
				action.getLabelBefore(),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleChangeOrigin(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "CHANGE_ORIGIN", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
				i18nService.getString(
						OriginI18nKeyProvider.getI18nKey(action.getOriginAfter()), 
						locale),
				i18nService.getString(
						OriginI18nKeyProvider.getI18nKey(action.getOriginBefore()), 
						locale),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleChangeOwner(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getUser() != null) {
			return i18nService.getString(PREFIX + "CHANGE_OWNER", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
					userFormattingService.format(domainService, action.getTicket(), action.getTicketOwnerAfter(), action.getTicket().getAnonymous(), locale, currentUser),
					userFormattingService.format(domainService, action.getTicket(), action.getTicketOwnerBefore(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		return i18nService.getString(PREFIX + "CHANGE_OWNER_APPLICATION", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getTicketOwnerAfter(), action.getTicket().getAnonymous(), locale, currentUser),
				userFormattingService.format(domainService, action.getTicket(), action.getTicketOwnerBefore(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleChangePriority(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "CHANGE_PRIORITY", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
				i18nService.getString(
						PriorityI18nKeyProvider.getI18nKey(action.getPriorityLevelAfter()), 
						locale),
				i18nService.getString(
						PriorityI18nKeyProvider.getI18nKey(action.getPriorityLevelBefore()), 
						locale),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleChangeScope(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "CHANGE_SCOPE", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
				i18nService.getString(TicketScopeI18nKeyProvider.getI18nKey(action.getScopeAfter()), 
						locale),
				i18nService.getString(TicketScopeI18nKeyProvider.getI18nKey(action.getScopeBefore()), 
						locale),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleChangeSpentTime(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "CHANGE_SPENT_TIME", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}
	
	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleClose(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "CLOSE", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleCreate(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "CREATE", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionDeleteFileInfo(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "DELETE_FILE_INFO", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser), 
				action.getFilename(),
		});
	}
	
	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleExpire(final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "EXPIRE", locale, new Object [] {
				action.getDate(),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleFree(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getUser() != null) {
			return i18nService.getString(PREFIX + "FREE", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
					userFormattingService.format(domainService, action.getTicket(), action.getManagerBefore(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		return i18nService.getString(PREFIX + "FREE_APPLICATION", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getManagerBefore(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleGiveInformation(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getUser() != null) {
			return i18nService.getString(PREFIX + "GIVE_INFORMATION", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		return i18nService.getString(PREFIX + "GIVE_INFORMATION_APPLICATION", locale, new Object [] {
				action.getDate(),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleInvite(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getUser() == null) {
			return i18nService.getString(PREFIX + "INVITE_APPLICATION", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getInvitedUser(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		if (action.getUser().equals(action.getInvitedUser())) {
			return i18nService.getString(PREFIX + "INVITE_SELF", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		return i18nService.getString(PREFIX + "INVITE", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
				userFormattingService.format(domainService, action.getTicket(), action.getInvitedUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleMonitoringInviteV2(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "MONITORING_INVITE_V2", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
				userFormattingService.format(domainService, action.getTicket(), action.getInvitedUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleRemoveInvitation(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getUser() == null) {
			return i18nService.getString(PREFIX + "REMOVE_INVITATION_APPLICATION", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getInvitedUser(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		if (action.getUser().equals(action.getInvitedUser())) {
			return i18nService.getString(PREFIX + "REMOVE_INVITATION_SELF", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		return i18nService.getString(PREFIX + "REMOVE_INVITATION", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
				userFormattingService.format(domainService, action.getTicket(), action.getInvitedUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitlePostpone(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "POSTPONE", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleRefuse(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "REFUSE", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleRefuseClosure(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "REFUSE_CLOSURE", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleReopen(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "REOPEN", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleRequestInformation(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		return i18nService.getString(PREFIX + "REQUEST_INFORMATION", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleTake(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getManagerBefore() != null) {
			return i18nService.getString(PREFIX + "TAKE", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
					userFormattingService.format(domainService, action.getTicket(), action.getManagerBefore(), action.getTicket().getAnonymous(), locale, currentUser),
			});
		}
		return i18nService.getString(PREFIX + "TAKE_FREE_BEFORE", locale, new Object [] {
				action.getDate(),
				userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
		});
	}

	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	protected String getActionTitleUpload(final DomainService domainService, final Action action, final Locale locale, User currentUser) {
		if (action.getUser() != null) {
			return i18nService.getString(PREFIX + "UPLOAD", locale, new Object [] {
					action.getDate(),
					userFormattingService.format(domainService, action.getTicket(), action.getUser(), action.getTicket().getAnonymous(), locale, currentUser),
					action.getFilename(),
			});
		}
		return i18nService.getString(PREFIX + "UPLOAD_APPLICATION", locale, new Object [] {
				action.getDate(),
				action.getFilename(),
		});
	}

	/**
	 * @see org.esupportail.helpdesk.domain.ActionI18nTitleFormatter#getActionTitle(
	 * org.esupportail.helpdesk.domain.beans.Action, java.util.Locale)
	 */
	public String getActionTitle(final DomainService domainService, final Action action, final Locale locale, final User currentUser) {
		if (ActionType.APPROVE_CLOSURE.equals(action.getActionType())) {
			return getActionTitleApproveClosure(domainService, action, locale, currentUser);
		}
		if (ActionType.ASSIGN.equals(action.getActionType())) {
			return getActionTitleAssign(domainService, action, locale, currentUser);
		}
		if (ActionType.CANCEL.equals(action.getActionType())) {
			return getActionTitleCancel(domainService, action, locale, currentUser);
		}
		if (ActionType.CANCEL_POSTPONEMENT.equals(action.getActionType())) {
			return getActionTitleCancelPostponement(domainService, action, locale, currentUser);
		}
		if (ActionType.CHANGE_CATEGORY.equals(action.getActionType())) {
			return getActionTitleChangeCategory(domainService, action, locale, currentUser);
		}
		if (ActionType.CHANGE_COMPUTER.equals(action.getActionType())) {
			return getActionTitleChangeComputer(domainService, action, locale, currentUser);
		}
		if (ActionType.CHANGE_DEPARTMENT.equals(action.getActionType())) {
			return getActionTitleChangeDepartment(domainService, action, locale, currentUser);
		}
		if (ActionType.CHANGE_LABEL.equals(action.getActionType())) {
			return getActionTitleChangeLabel(domainService, action, locale, currentUser);
		}
		if (ActionType.CHANGE_ORIGIN.equals(action.getActionType())) {
			return getActionTitleChangeOrigin(domainService, action, locale, currentUser);
		}
		if (ActionType.CHANGE_OWNER.equals(action.getActionType())) {
			return getActionTitleChangeOwner(domainService, action, locale, currentUser);
		}
		if (ActionType.CHANGE_PRIORITY.equals(action.getActionType())) {
			return getActionTitleChangePriority(domainService, action, locale, currentUser);
		}
		if (ActionType.CHANGE_SCOPE.equals(action.getActionType())) {
			return getActionTitleChangeScope(domainService, action, locale, currentUser);
		}
		if (ActionType.CHANGE_SPENT_TIME.equals(action.getActionType())) {
			return getActionTitleChangeSpentTime(domainService, action, locale, currentUser);
		}
		if (ActionType.CLOSE.equals(action.getActionType()) 
				|| ActionType.CLOSE_APPROVE.equals(action.getActionType())) {
			return getActionTitleClose(domainService, action, locale, currentUser);
		}
		if (ActionType.CREATE.equals(action.getActionType())) {
			return getActionTitleCreate(domainService, action, locale, currentUser);
		}
		if (ActionType.DELETE_FILE_INFO.equals(action.getActionType())) {
			return getActionDeleteFileInfo(domainService, action, locale, currentUser);
		}
		if (ActionType.EXPIRE.equals(action.getActionType())) {
			return getActionTitleExpire(action, locale, currentUser);
		}
		if (ActionType.FREE.equals(action.getActionType())) {
			return getActionTitleFree(domainService, action, locale, currentUser);
		}
		if (ActionType.GIVE_INFORMATION.equals(action.getActionType())) {
			return getActionTitleGiveInformation(domainService, action, locale, currentUser);
		}
		if (ActionType.INVITE.equals(action.getActionType())) {
			return getActionTitleInvite(domainService, action, locale, currentUser);
		}
		if (ActionType.REMOVE_INVITATION.equals(action.getActionType())) {
			return getActionTitleRemoveInvitation(domainService, action, locale, currentUser);
		}
		if (ActionType.POSTPONE.equals(action.getActionType())) {
			return getActionTitlePostpone(domainService, action, locale, currentUser);
		}
		if (ActionType.REFUSE.equals(action.getActionType())) {
			return getActionTitleRefuse(domainService, action, locale, currentUser);
		}
		if (ActionType.REFUSE_CLOSURE.equals(action.getActionType())) {
			return getActionTitleRefuseClosure(domainService, action, locale, currentUser);
		}
		if (ActionType.REOPEN.equals(action.getActionType())) {
			return getActionTitleReopen(domainService, action, locale, currentUser);
		}
		if (ActionType.REQUEST_INFORMATION.equals(action.getActionType())) {
			return getActionTitleRequestInformation(domainService, action, locale, currentUser);
		}
		if (ActionType.TAKE.equals(action.getActionType())) {
			return getActionTitleTake(domainService, action, locale, currentUser);
		}
		if (ActionType.MONITORING_INVITE_V2.equals(action.getActionType())) {
			return getActionTitleMonitoringInviteV2(domainService, action, locale, currentUser);
		}
		if (ActionType.UPLOAD.equals(action.getActionType())) {
			return getActionTitleUpload(domainService, action, locale, currentUser);
		}
		return "??? " + action.getActionType() + " ???";
	}

	/**
	 * @return the i18nService
	 */
	protected I18nService getI18nService() {
		return i18nService;
	}

	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

	/**
	 * @return the userFormattingService
	 */
	protected UserFormattingService getUserFormattingService() {
		return userFormattingService;
	}

	/**
	 * @param userFormattingService the userFormattingService to set
	 */
	public void setUserFormattingService(final UserFormattingService userFormattingService) {
		this.userFormattingService = userFormattingService;
	}

}

