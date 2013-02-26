/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.batch;

import org.esupportail.commons.batch.BatchException;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.application.ApplicationUtils;
import org.esupportail.commons.services.application.VersionningUtils;
import org.esupportail.commons.services.database.DatabaseUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.i18n.I18nUtils;
import org.esupportail.commons.services.ldap.LdapUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.portal.PortalUtils;
import org.esupportail.commons.services.smtp.SmtpUtils;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.services.archiving.Archiver;
import org.esupportail.helpdesk.services.expiration.Expirator;
import org.esupportail.helpdesk.services.feed.ErrorHolder;
import org.esupportail.helpdesk.services.feed.Feeder;
import org.esupportail.helpdesk.services.indexing.Indexer;
import org.esupportail.helpdesk.services.recall.Recaller;

/**
 * A class with a main method called by ant targets.
 */
public class Batch {

	/**
	 * A logger.
	 */
	private static final Logger LOG = new LoggerImpl(Batch.class);

	/**
	 * The name of the domain service bean.
	 */
	private static final String DOMAIN_SERVICE_BEAN = "domainService";

	/**
	 * The name of the indexer bean.
	 */
	private static final String INDEXER_BEAN = "indexer";

	/**
	 * The name of the archiver bean.
	 */
	private static final String ARCHIVER_BEAN = "archiver";

	/**
	 * The name of the expirator bean.
	 */
	private static final String EXPIRATOR_BEAN = "expirator";

	/**
	 * The name of the recaller bean.
	 */
	private static final String RECALLER_BEAN = "recaller";

	/**
	 * The name of the feeder bean.
	 */
	private static final String FEEDER_BEAN = "feeder";

	/**
	 * Bean constructor.
	 */
	private Batch() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Print the syntax and exit.
	 */
	private static void syntax() {
		throw new IllegalArgumentException(
				"syntax: " + Batch.class.getSimpleName() + " <options>"
				+ "\nwhere option can be:"
				+ "\n- test-beans: test the required beans"
				+ "\n- update-index: update the index"
				+ "\n- rebuild-index: rebuild the index"
				+ "\n- unlock-index: unlock the index"
				+ "\n- archive-tickets: archive old closed tickets"
				+ "\n- unlock-archive-tickets: unlock archive-tickets"
				+ "\n- expire-tickets: expire non approved tickets"
				+ "\n- expire-tickets-no-email: expire non approved tickets"
				+ "\n- unlock-expire-tickets: unlock expire-tickets"
				+ "\n- recall-tickets: recall postponed tickets"
				+ "\n- unlock-recall-tickets: unlock recall-tickets"
				+ "\n- test-department-selection: test the department selection"
				+ "\n- test-user-info: test the user info"
				+ "\n- test-search: test the search engine"
				+ "\n- send-ticket-reports: send ticket reports"
				+ "\n- send-faq-reports: send FAQ reports"
				+ "\n- feed: feed the database"
				+ "\n- delete-all-tickets: delete all the tickets"
				);
	}

	/**
	 * @return the domain service
	 */
	private static DomainService getDomainService() {
		return (DomainService) BeanUtils.getBean(DOMAIN_SERVICE_BEAN);
	}

	/**
	 * Test the required beans.
	 */
	private static void testBeans() {
		DatabaseUtils.test();
		ApplicationUtils.createApplicationService();
		I18nUtils.createI18nService();
		LdapUtils.createLdapService();
		PortalUtils.createPortalService();
		SmtpUtils.createSmtpService();
		VersionningUtils.createVersionningService();
	}


	/**
	 *  Initialize the database. 
	 */
	private static void initDatabase() {
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			DatabaseUtils.create(); 
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
		doUpgradeDatabase();
	}
	
	/**
	* Upgrade the database.
	*/
	private static void doUpgradeDatabase() {
		while (true) {
			try {
				DatabaseUtils.open();
				DatabaseUtils.begin();
				boolean recall = VersionningUtils.createVersionningService().upgradeDatabase();
				DatabaseUtils.commit();
				DatabaseUtils.close();
				if (!recall) {
					return;
				}
			} catch (Throwable t) {
				closeAndRethrowException(t);
			}
		}
	} 
	
	/**
	* @param t
	* @throws ConfigException
	*/
	private static void closeAndRethrowException(final Throwable t) throws ConfigException {
		ConfigException ex = null;
		if (t instanceof ConfigException) {
			ex = (ConfigException) t;
		} else {
			ex = new ConfigException(t);
		}
		DatabaseUtils.close();
		throw ex;
	} 
	
	/**
	 * @return the indexer
	 */
	private static Indexer getIndexer() {
		return (Indexer) BeanUtils.getBean(INDEXER_BEAN);
	}

	/**
	 * Rebuild or update the index.
	 * @param rebuild true to rebuild, false to simply update
	 */
	private static void updateIndex(final boolean rebuild) {
		Indexer indexer = getIndexer();
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			if (rebuild) {
				indexer.removeIndex();
			}
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
		int totalIndexedEntries = 0;
		int indexedEntries = 0;
		do {
			try {
				DatabaseUtils.open();
				DatabaseUtils.begin();
				indexedEntries = indexer.updateIndex(!rebuild);
				totalIndexedEntries += indexedEntries;
				DatabaseUtils.commit();
				DatabaseUtils.close();
			} catch (Throwable t) {
				DatabaseUtils.rollback();
				DatabaseUtils.close();
				throw new BatchException(t);
			}
		} while (indexedEntries > 0);
		if (totalIndexedEntries > 0) {
			LOG.info(totalIndexedEntries + " entry(ies) indexed.");
		} else {
			LOG.info("no entry indexed.");
		}
	}

	/**
	 * Unlock the index.
	 */
	private static void unlockIndex() {
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			getIndexer().unlockIndex();
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * @return the archiver
	 */
	private static Archiver getArchiver() {
		return (Archiver) BeanUtils.getBean(ARCHIVER_BEAN);
	}

	/**
	 * Archive tickets.
	 */
	private static void archiveTickets() {
		Archiver archiver = getArchiver();
		boolean recall;
		do {
			try {
				DatabaseUtils.open();
				DatabaseUtils.begin();
				VersionningUtils.checkVersion(true, false);
				recall = archiver.archive();
				DatabaseUtils.commit();
				DatabaseUtils.close();
			} catch (Throwable t) {
				DatabaseUtils.rollback();
				DatabaseUtils.close();
				throw new BatchException(t);
			}
		} while (recall);
	}

	/**
	 * Unlock archive-tickets.
	 */
	private static void unlockArchiveTickets() {
		Archiver archiver = getArchiver();
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			archiver.unlock();
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * @return the expirator
	 */
	private static Expirator getExpirator() {
		return (Expirator) BeanUtils.getBean(EXPIRATOR_BEAN);
	}

	/**
	 * Expire non approved tickets.
	 * @param alerts
	 */
	private static void expireTickets(final boolean alerts) {
		Expirator expirator = getExpirator();
		boolean recall;
		do {
			try {
				DatabaseUtils.open();
				DatabaseUtils.begin();
				VersionningUtils.checkVersion(true, false);
				recall = expirator.expire(alerts);
				DatabaseUtils.commit();
				DatabaseUtils.close();
			} catch (Throwable t) {
				DatabaseUtils.rollback();
				DatabaseUtils.close();
				throw new BatchException(t);
			}
		} while (recall);
	}

	/**
	 * Unlock expire-tickets.
	 */
	private static void unlockExpireTickets() {
		Expirator expirator = getExpirator();
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			expirator.unlock();
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * @return the recaller
	 */
	private static Recaller getRecaller() {
		return (Recaller) BeanUtils.getBean(RECALLER_BEAN);
	}

	/**
	 * Recall postponed tickets.
	 */
	private static void recallTickets() {
		Recaller recaller = getRecaller();
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			recaller.recall();
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * Unlock recall-tickets.
	 */
	private static void unlockRecallTickets() {
		Recaller recaller = getRecaller();
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			recaller.unlock();
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * Send reports.
	 */
	private static void sendTicketReports() {
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			getDomainService().sendTicketReports();
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * Send FAQ updates.
	 */
	private static void sendFaqReports() {
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			getDomainService().sendFaqReports();
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * Test the user info.
	 */
	private static void testUserInfo() {
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			getDomainService().testUserInfo();
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * Feed the database.
	 */
	private static void feed() {
		try {
			boolean versionChecked = false;
			Feeder feeder = (Feeder) BeanUtils.getBean(FEEDER_BEAN);
			ErrorHolder errorHolder = new ErrorHolder();
			while (true) {
				DatabaseUtils.open();
				DatabaseUtils.begin();
				if (!versionChecked) {
					VersionningUtils.checkVersion(true, false);
					versionChecked = true;
				}
				boolean commit = feeder.feed(errorHolder);
				if (commit) {
					DatabaseUtils.commit();
				} else {
					DatabaseUtils.rollback();
				}
				DatabaseUtils.close();
				if (!commit || errorHolder.hasErrors()) {
					break;
				}
			}
			if (errorHolder.hasErrors()) {
				errorHolder.addInfo(errorHolder.getErrorNumber() + " total error(s) found");
				LOG.error(errorHolder.getStrings());
			} else {
				errorHolder.addInfo("no error found");
				LOG.info(errorHolder.getStrings());
			}
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * Test the search engine.
	 */
	private static void testSearch() {
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			Indexer indexer = (Indexer) BeanUtils.getBean(
					INDEXER_BEAN);
			indexer.test();
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * Test the user info.
	 */
	private static void testConsistency() {
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			DomainService domainService = getDomainService();
			for (Ticket ticket : domainService.getTickets(0, Integer.MAX_VALUE)) {
				if (!ticket.getDepartment().equals(ticket.getCategory().getDepartment())) {
					LOG.error("error for ticket #" + ticket.getId());
				}
			}
			DatabaseUtils.rollback();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * Delete all the tickets.
	 */
	private static void deleteAllTickets() {
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			VersionningUtils.checkVersion(true, false);
			getDomainService().deleteAllTickets();
			getIndexer().updateIndex(true);
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			DatabaseUtils.rollback();
			DatabaseUtils.close();
			throw new BatchException(t);
		}
	}

	/**
	 * Dispatch depending on the arguments.
	 * @param args
	 */
	protected static void dispatch(final String[] args) {
		switch (args.length) {
		case 0:
			syntax();
			break;
		case 1:
			if ("test-beans".equals(args[0])) {
				testBeans();
			} else if ("init-data".equals(args[0])) {
				initDatabase();
			}else if ("update-index".equals(args[0])) {
				updateIndex(false);
			} else if ("rebuild-index".equals(args[0])) {
				updateIndex(true);
			} else if ("unlock-index".equals(args[0])) {
				unlockIndex();
			} else if ("archive-tickets".equals(args[0])) {
				archiveTickets();
			} else if ("unlock-archive-tickets".equals(args[0])) {
				unlockArchiveTickets();
			} else if ("expire-tickets".equals(args[0])) {
				expireTickets(true);
			} else if ("expire-tickets-no-email".equals(args[0])) {
				expireTickets(false);
			} else if ("unlock-expire-tickets".equals(args[0])) {
				unlockExpireTickets();
			} else if ("recall-tickets".equals(args[0])) {
				recallTickets();
			} else if ("unlock-recall-tickets".equals(args[0])) {
				unlockRecallTickets();
			} else if ("test-user-info".equals(args[0])) {
				testUserInfo();
			} else if ("feed".equals(args[0])) {
				feed();
			} else if ("send-reports".equals(args[0])) {
				throw new IllegalArgumentException(
						"argument send-reports is obsolete, please use sent-ticket-reports instead");
			} else if ("send-ticket-reports".equals(args[0])) {
				sendTicketReports();
			} else if ("send-faq-reports".equals(args[0])) {
				sendFaqReports();
			} else if ("test-search".equals(args[0])) {
				testSearch();
			} else if ("test-consistency".equals(args[0])) {
				testConsistency();
			} else if ("delete-all-tickets".equals(args[0])) {
				deleteAllTickets();
			} else {
				syntax();
			}
			break;
		default:
			syntax();
			break;
		}
	}

	/**
	 * The main method, called by ant.
	 * @param args
	 */
	public static void main(final String[] args) {
		try {
			ApplicationService applicationService = ApplicationUtils.createApplicationService();
			LOG.info(applicationService.getName() + " v" + applicationService.getVersion());
			dispatch(args);
		} catch (Throwable t) {
			ExceptionUtils.catchException(t);
		}
	}

}
