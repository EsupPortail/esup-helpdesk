/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;

/**
 * A condition that is matched according to SQL query.
 */
public class ExternalDbCondition extends AbstractFinalCondition {

	/**
	 * Parameter used in query.
	 */
	private enum ParamType {

		/**
		 * Username.
		 */
		USER,
		/**
		 * IP address.
		 */
		IP,
		/**
		 * Computer hostname.
		 */
		HOSTNAME;

		/**
		 * @return Parameter form used in query.
		 */
		@Override
		public String toString() {
			switch (this) {
			case USER:
				return "%USER%";
			case HOSTNAME:
				return "%HOSTNAME%";
			case IP:
				return "%IP%";
			}
			return null;
		}

		/**
		 * Returns first parameter found in string.
		 *
		 * @param str the string to find in
		 * @return the found parameter; null on not found
		 */
		static ParamType getFirstParam(final String str) {
			int userPos = str.indexOf(USER.toString());
			int hostnamePos = str.indexOf(HOSTNAME.toString());
			int ipPos = str.indexOf(IP.toString());

			userPos = userPos < 0 ? Integer.MAX_VALUE : userPos;
			hostnamePos = hostnamePos < 0 ? Integer.MAX_VALUE : hostnamePos;
			ipPos = ipPos < 0 ? Integer.MAX_VALUE : ipPos;

			if ((userPos < hostnamePos) && (userPos < ipPos)) {
				return USER;
			}

			if ((hostnamePos < userPos) && (hostnamePos < ipPos)) {
				return HOSTNAME;
			}

			if ((ipPos < hostnamePos) && (ipPos < userPos)) {
				return IP;
			}

			return null;
		}
	}

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2868435305144749927L;
	/**
	 * The JNDI context to connect to.
	 */
	private String ctx;
	/**
	 * The JNDI source to connect to.
	 */
	private String jndi;
	/**
	 * The raw SQL query.
	 */
	private String sql;
	/**
	 * The SQL query with replaced parameters.
	 */
	private String sqlInternal;
	/**
	 * Parameters list of the query.
	 */
	private List<ParamType> params;
	/**
	 * The data source fetched via JNDI.
	 */
	private DataSource dataSource;

	/**
	 * Get the JNDI context.
	 *
	 * @return the context; null for default
	 */
	public String getCtx() {
		return ctx;
	}

	/**
	 * Set the JNDI context.
	 *
	 * @param ctx the context; null for default
	 */
	public void setCtx(final String ctx) {
		this.ctx = ctx;
	}

	/**
	 * Get the name of JNDI source.
	 *
	 * @return the name of JNDI source
	 */
	public String getJndi() {
		return jndi;
	}

	/**
	 * Set the name of JNDI source.
	 *
	 * @param jndi the name of JNDI source
	 */
	public void setJndi(final String jndi) {
		this.jndi = jndi;
	}

	/**
	 * Get the SQL query.
	 *
	 * @return the SQL query
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * Set the SQL query and translate it to form suitable for PreparedStatement
	 *
	 * @param sql the SQL query
	 */
	public void setSql(final String sql) {
		this.sql = sql;
		this.sqlInternal = sql;
		this.params = new ArrayList<ParamType>();
		ParamType paramType;
		while ((paramType = ParamType.getFirstParam(sqlInternal)) != null) {
			params.add(paramType);
			sqlInternal = sqlInternal.replaceFirst(paramType.toString(), "?");
		}
	}

	/**
	 * Get the connection to database from JNDI
	 *
	 * @return the connection
	 * @throws NamingException on JNDI context or JNDI source not found
	 * @throws SQLException on DB connection failure
	 */
	private Connection getConnection() throws NamingException, SQLException {
		if (dataSource == null) {
			Context ic = new InitialContext();
			Context context = (Context) ic.lookup(ctx == null ? "java:comp/env"
					: ctx);
			dataSource = (DataSource) context.lookup(jndi);
		}
		return dataSource.getConnection();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition
	 *      #isMatchedInternal(org.esupportail.helpdesk.domain.DomainService,
	 *      org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress)
	 */
	@Override
	protected boolean isMatchedInternal(
			@SuppressWarnings("unused") final DomainService domainService,
			final User user, final InetAddress client) {
        boolean isMatched = false;
        PreparedStatement stmt = null;
        Connection connection = null;
		try {
			connection = getConnection();
			stmt = connection.prepareStatement(sqlInternal);

			int paramIndex = 0;
			for (ParamType paramType : params) {
				paramIndex++;
				switch (paramType) {
				case USER:
					stmt.setString(paramIndex, user.getRealId());
					break;
				case HOSTNAME:
					stmt.setString(paramIndex, client.getHostName());
					break;
				case IP:
					stmt.setString(paramIndex, client.getHostAddress());
					break;
				}
			}

			ResultSet rs = stmt.executeQuery();
			if (rs.next() && (rs.getInt(1) > 0)) {
				isMatched = true;
			}
		} catch (Exception ex) {
            isMatched = false;
		}

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                //nothing to close
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                //nothing to close
            }
        }
		return isMatched;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	protected void checkInternal() throws DepartmentSelectionCompileError {
		if (getJndi() == null) {
			throw new DepartmentSelectionCompileError(
					"<external-db> tags should have 'jndi' attributes");
		}
		if (getSql() == null) {
			throw new DepartmentSelectionCompileError(
					"<external-db> tags should have 'sql' attributes");
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "externalDb";
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "<external-db";
		if (getCtx() != null) {
			str += " ctx=\"" + getCtx() + "\"";
		}
		str += " jndi=\"" + getJndi() + "\" sql=\"" + getSql() + "\" />";
		return str;
	}
}
