/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.esupportail.helpdesk.domain.beans.User;
import org.springframework.util.StringUtils;

/** 
 * A bean that gives to column to print at a given position.
 */ 
public class ControlPanelColumnOrderer extends HashMap<Integer, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6367418668009353140L;
	
	/**
	 * The columns.
	 */
	private static final String [] COLUMNS = {
		"ID",
		"CREATION_DATE_TIME",
		"CREATION_DATE",
		"DEPARTMENT",
		"CREATION_DEPARTMENT",
		"CATEGORY",
		"LABEL",
		"STATUS",
		"PRIORITY",
		"CHANGE_DATE",
		"CHANGE_DATE_TIME",
		"OWNER",
		"MANAGER",
	};
	
	/**
	 * The default columns.
	 */
	private static final String [] DEFAULT_COLUMNS = {
		"ID",
		"CREATION_DATE_TIME",
		"DEPARTMENT",
		"CATEGORY",
		"LABEL",
		"STATUS",
		"OWNER",
		"MANAGER",
	};
	
	/**
	 * The column names.
	 */
	private List<String> columnNames;

	/**
	 * Bean constructor.
	 * @param user
	 */
	public ControlPanelColumnOrderer(final User user) {
		super();
		String [] columnNamesArray = null;
		if (StringUtils.hasText(user.getControlPanelColumns())) {
			columnNamesArray = user.getControlPanelColumns().split(":");
		}
		if (columnNamesArray == null || columnNamesArray.length == 0) { 
			columnNamesArray = DEFAULT_COLUMNS;
		}
		columnNames = new ArrayList<String>();
		for (String columnName : columnNamesArray) {
			columnNames.add(columnName);
		}
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object order) {
		String result = null;
		if (order != null) {
			try {
				int n = Integer.parseInt(order.toString());
				if (n < columnNames.size() && n >= 0) {
					result = columnNames.get(n);
				}
			} catch (NumberFormatException e) {
				// return null
			}
		}
		return result;
	}

	/**
	 * @return the missing column names.
	 */
	public List<String> getMissingColumnNames() {
		List<String> missingColumnNames = new ArrayList<String>();
		for (String columnName : COLUMNS) {
			if (!columnNames.contains(columnName)) {
				missingColumnNames.add(columnName);
			}
		}
		return missingColumnNames;
	}
	
	/**
	 * Remove a column.
	 * @param index the index of the column to remove
	 */
	public void removeColumn(final int index) {
//		List<String> oldColumnNames = columnNames;
//		columnNames = new ArrayList<String>();
//		for (int i = 0; i < oldColumnNames.size(); i++) {
//			if (index != i) {
//				columnNames.add(oldColumnNames.get(i));
//			}
//		}
		columnNames.remove(index);
	}
	
	/**
	 * Move a column right.
	 * @param index the index of the column to move
	 */
	public void moveColumnRight(final int index) {
		if (index >= columnNames.size() || index < 0) {
			return;
		}
		String columnName = columnNames.get(index);
		columnNames.set(index, columnNames.get(index + 1));
		columnNames.set(index + 1, columnName);
	}
	
	/**
	 * Move a column left.
	 * @param index the index of the column to move
	 */
	public void moveColumnLeft(final int index) {
		if (index > columnNames.size() || index <= 0) {
			return;
		}
		String columnName = columnNames.get(index);
		columnNames.set(index, columnNames.get(index - 1));
		columnNames.set(index - 1, columnName);
	}
	
	/**
	 * Add a column.
	 * @param columnName the name of the column to add
	 */
	public void addColumn(final String columnName) {
		columnNames.add(columnName);
	}
	
	/**
	 * @return the orderer as a String.
	 */
	public String asString() {
		String separator = "";
		String result = "";
		for (String columnName : columnNames) {
			result += separator + columnName;
			separator = ":";
		}
		return result;
	}
	
	/**
	 * @return the number of columns
	 */
	public int getColumnsNumber() {
		return columnNames.size();
	}
	
}

