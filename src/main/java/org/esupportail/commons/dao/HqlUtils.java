/**
 * ESUP-Portail Commons - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-commons
 */
package org.esupportail.commons.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class to play with conditions.
 */
public abstract class HqlUtils {

	/**
	 * The AS keyword.
	 */
	public static final String AS_KEYWORD = " AS ";

	/**
	 * The COUNT(*) phrase.
	 */
	public static final String COUNT_ALL_PHRASE = " count(*) ";

	/**
	 * The always TRUE condition.
	 */
	private static final String TRUE = "true";

	/**
	 * The always FALSE condition.
	 */
	private static final String FALSE = "false";

	/**
	 * The '(' keyword.
	 */
	private static final String OPEN_PAREN_KEYWORD = " ( ";

	/**
	 * The ')' keyword.
	 */
	private static final String CLOSE_PAREN_KEYWORD = " ) ";

	/**
	 * The comma.
	 */
	private static final String COMMA_KEYWORD = " , ";

	/**
	 * The TRUE keyword.
	 */
	private static final String TRUE_KEYWORD = " true ";

	/**
	 * The FALSE keyword.
	 */
	private static final String FALSE_KEYWORD = " false ";

	/**
	 * The 'AND' keyword.
	 */
	private static final String AND_KEYWORD = " AND ";

	/**
	 * The 'OR' keyword.
	 */
	private static final String OR_KEYWORD = " OR ";

	/**
	 * The 'NOT' keyword.
	 */
	private static final String NOT_KEYWORD = " NOT ";

	/**
	 * The 'IN' keyword.
	 */
	private static final String IN_KEYWORD = " IN ";

	/**
	 * The 'IS NULL' phrase.
	 */
	private static final String IS_NULL_PHRASE = " IS NULL ";

	/**
	 * The 'IS NOT NULL' phrase.
	 */
	private static final String IS_NOT_NULL_PHRASE = " IS NOT NULL ";

	/**
	 * The '=' keyword.
	 */
	private static final String EQUALS_KEYWORD = " = ";

	/**
	 * The '>' keyword.
	 */
	private static final String GT_KEYWORD = " > ";

	/**
	 * The '<' keyword.
	 */
	private static final String LT_KEYWORD = " < ";

	/**
	 * The '>=' keyword.
	 */
	private static final String GE_KEYWORD = " >= ";

	/**
	 * The '<=' keyword.
	 */
	private static final String LE_KEYWORD = " <= ";

	/**
	 * The LIKE keyword.
	 */
	private static final String LIKE_KEYWORD = " LIKE ";

	/**
	 * The quote (') keyword.
	 */
	private static final String QUOTE_KEYWORD = "'";

	/**
	 * The ORDER BY keyword.
	 */
	private static final String ORDER_BY_KEYWORD = " ORDER BY ";

	/**
	 * The DESC keyword.
	 */
	private static final String DESC_KEYWORD = " DESC ";

	/**
	 * The ASC keyword.
	 */
	private static final String ASC_KEYWORD = " ASC ";

	/**
	 * The SELECT keyword.
	 */
	private static final String SELECT_KEYWORD = " SELECT ";

	/**
	 * The COUNT keyword.
	 */
	private static final String COUNT_KEYWORD = " COUNT ";

	/**
	 * The DISTINCT keyword.
	 */
	private static final String DISTINCT_KEYWORD = " DISTINCT ";

	/**
	 * The FROM keyword.
	 */
	private static final String FROM_KEYWORD = " FROM ";

	/**
	 * The WHERE keyword.
	 */
	private static final String WHERE_KEYWORD = " WHERE ";

	/**
	 * The EXISTS keyword.
	 */
	private static final String EXISTS_KEYWORD = " EXISTS ";

	/**
	 * The DELETE keyword.
	 */
	private static final String DELETE_KEYWORD = " DELETE ";

	/**
	 * The UPDATE keyword.
	 */
	private static final String UPDATE_KEYWORD = " UPDATE ";

	/**
	 * The SET keyword.
	 */
	private static final String SET_KEYWORD = " SET ";

	/**
	 * Bean constructor.
	 */
	private HqlUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the always true condition.
	 */
	public static String alwaysTrue() {
		return TRUE;
	}

	/**
	 * @return the always false condition.
	 */
	public static String alwaysFalse() {
		return FALSE;
	}

	/**
	 * @param condition
	 * @return true if the condition is always true, false otherwise.
	 */
	public static boolean isAlwaysTrue(final String condition) {
		return TRUE.equals(condition);
	}

	/**
	 * @param condition
	 * @return true if the condition is always false, false otherwise.
	 */
	public static boolean isAlwaysFalse(final String condition) {
		return FALSE.equals(condition);
	}

	/**
	 * @param condition
	 * @return a parenth-ed condition.
	 */
	private static String parenth(final String condition) {
		return OPEN_PAREN_KEYWORD + condition + CLOSE_PAREN_KEYWORD;
	}

	/**
	 * @param condition
	 * @return a NOT condition.
	 */
	public static String not(final String condition) {
		if (isAlwaysTrue(condition)) {
			return FALSE;
		}
		if (isAlwaysFalse(condition)) {
			return TRUE;
		}
		return NOT_KEYWORD + parenth(condition);
	}

	/**
	 * @param conditions
	 * @return a AND condition.
	 */
	private static String dynamicAnd(final List<String> conditions) {
		if (conditions.isEmpty()) {
			return TRUE;
		}
		if (conditions.size() == 1) {
			return conditions.get(0);
		}
		String separator = "";
		String str = "";
		for (String condition : conditions) {
			str += separator + parenth(condition);
			separator = AND_KEYWORD;
		}
		return str;
	}

	/**
	 * @param conditions
	 * @return a AND condition.
	 */
	public static String and(final List<String> conditions) {
		List<String> dynamicConditions = new ArrayList<String>();
		for (String condition : conditions) {
			if (isAlwaysFalse(condition)) {
				return FALSE;
			}
			if (!isAlwaysTrue(condition)) {
				dynamicConditions.add(condition);
			}
		}
		return dynamicAnd(dynamicConditions);
	}

	/**
	 * @param conditions
	 * @return a AND condition.
	 */
	public static String and(final String [] conditions) {
		return and(Arrays.asList(conditions));
	}

	/**
	 * @param condition1
	 * @param condition2
	 * @return a AND condition.
	 */
	public static String and(
			final String condition1,
			final String condition2) {
		return and(new String[] {condition1, condition2});
	}

	/**
	 * @param condition1
	 * @param condition2
	 * @param condition3
	 * @return a AND condition.
	 */
	public static String and(
			final String condition1,
			final String condition2,
			final String condition3) {
		return and(new String[] {condition1, condition2, condition3});
	}

	/**
	 * @param condition1
	 * @param condition2
	 * @param condition3
	 * @param condition4
	 * @return a AND condition.
	 */
	public static String and(
			final String condition1,
			final String condition2,
			final String condition3,
			final String condition4) {
		return and(new String[] {condition1, condition2, condition3, condition4});
	}

	/**
	 * @param conditions
	 * @return a OR condition.
	 */
	private static String dynamicOr(final List<String> conditions) {
		if (conditions.isEmpty()) {
			return FALSE;
		}
		if (conditions.size() == 1) {
			return conditions.get(0);
		}
		String separator = "";
		String str = "";
		for (String condition : conditions) {
			str += separator + parenth(condition);
			separator = OR_KEYWORD;
		}
		return str;
	}

	/**
	 * @param conditions
	 * @return a OR condition.
	 */
	public static String or(final List<String> conditions) {
		List<String> dynamicConditions = new ArrayList<String>();
		for (String condition : conditions) {
			if (isAlwaysTrue(condition)) {
				return TRUE;
			}
			if (!isAlwaysFalse(condition)) {
				dynamicConditions.add(condition);
			}
		}
		return dynamicOr(dynamicConditions);
	}

	/**
	 * @param conditions
	 * @return a OR condition.
	 */
	public static String or(final String [] conditions) {
		return or(Arrays.asList(conditions));
	}

	/**
	 * @param condition1
	 * @param condition2
	 * @return a OR condition.
	 */
	public static String or(
			final String condition1,
			final String condition2) {
		return or(new String[] {condition1, condition2});
	}

	/**
	 * @param condition1
	 * @param condition2
	 * @param condition3
	 * @return a OR condition.
	 */
	public static String or(
			final String condition1,
			final String condition2,
			final String condition3) {
		return or(new String[] {condition1, condition2, condition3});
	}

	/**
	 * @param condition1
	 * @param condition2
	 * @param condition3
	 * @param condition4
	 * @return a OR condition.
	 */
	public static String or(
			final String condition1,
			final String condition2,
			final String condition3,
			final String condition4) {
		return or(new String[] {condition1, condition2, condition3, condition4});
	}

	/**
	 * @param condition1
	 * @param condition2
	 * @param condition3
	 * @param condition4
	 * @param condition5
	 * @return a OR condition.
	 */
	public static String or(
			final String condition1,
			final String condition2,
			final String condition3,
			final String condition4,
			final String condition5) {
		return or(new String[] {condition1, condition2, condition3, condition4, condition5});
	}

	/**
	 * @param operand
	 * @param values
	 * @return a IN condition.
	 */
	public static String intIn(final String operand, final List<Integer> values) {
		if (values.isEmpty()) {
			return FALSE;
		}
		if (values.size() == 1) {
			return equals(operand, values.get(0));
		}
		String str = "";
		String separator = "";
		for (Integer value : values) {
			str += separator + value;
			separator = COMMA_KEYWORD;
		}
		return operand + IN_KEYWORD + parenth(str);
	}

	/**
	 * @param operand
	 * @param values
	 * @return a IN condition.
	 */
	public static String intIn(final String operand, final Integer [] values) {
		return intIn(operand, Arrays.asList(values));
	}

	/**
	 * @param operand
	 * @param values
	 * @return a IN condition.
	 */
	public static String longIn(final String operand, final List<Long> values) {
		if (values.isEmpty()) {
			return FALSE;
		}
		if (values.size() == 1) {
			return equals(operand, values.get(0));
		}
		String str = "";
		String separator = "";
		for (Long value : values) {
			str += separator + value;
			separator = COMMA_KEYWORD;
		}
		return operand + IN_KEYWORD + parenth(str);
	}

	/**
	 * @param operand
	 * @param values
	 * @return a IN condition.
	 */
	public static String longIn(final String operand, final Long [] values) {
		return longIn(operand, Arrays.asList(values));
	}

	/**
	 * @param operand
	 * @param values
	 * @return a IN condition.
	 */
	public static String stringIn(final String operand, final List<String> values) {
		if (values.isEmpty()) {
			return FALSE;
		}
		if (values.size() == 1) {
			return equals(operand, quote(values.get(0)));
		}
		String str = "";
		String separator = "";
		for (String value : values) {
			str += separator + quote(value);
			separator = COMMA_KEYWORD;
		}
		return operand + IN_KEYWORD + parenth(str);
	}

	/**
	 * @param operand
	 * @param values
	 * @return a IN condition.
	 */
	public static String stringIn(final String operand, final String [] values) {
		return stringIn(operand, Arrays.asList(values));
	}

	/**
	 * @param operand
	 * @return a IS NULL condition.
	 */
	public static String isNull(final String operand) {
		return operand + IS_NULL_PHRASE;
	}

	/**
	 * @param operand
	 * @return a IS NOT NULL condition.
	 */
	public static String isNotNull(final String operand) {
		return operand + IS_NOT_NULL_PHRASE;
	}

	/**
	 * @param operand
	 * @return a = TRUE condition.
	 */
	public static String isTrue(final String operand) {
		return operand + EQUALS_KEYWORD + TRUE_KEYWORD;
	}

	/**
	 * @param operand
	 * @return a = FALSE condition.
	 */
	public static String isFalse(final String operand) {
		return operand + EQUALS_KEYWORD + FALSE_KEYWORD;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a = condition.
	 */
	public static String equals(final String operand1, final String operand2) {
		return operand1 + EQUALS_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a = condition.
	 */
	public static String equals(final String operand1, final Integer operand2) {
		return operand1 + EQUALS_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a = condition.
	 */
	public static String equals(final String operand1, final Long operand2) {
		return operand1 + EQUALS_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a > condition.
	 */
	public static String gt(final String operand1, final String operand2) {
		return operand1 + GT_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a > condition.
	 */
	public static String gt(final String operand1, final Integer operand2) {
		return operand1 + GT_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a > condition.
	 */
	public static String gt(final String operand1, final Long operand2) {
		return operand1 + GT_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a < condition.
	 */
	public static String lt(final String operand1, final String operand2) {
		return operand1 + LT_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a < condition.
	 */
	public static String lt(final String operand1, final Integer operand2) {
		return operand1 + LT_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a < condition.
	 */
	public static String lt(final String operand1, final Long operand2) {
		return operand1 + LT_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a >= condition.
	 */
	public static String ge(final String operand1, final String operand2) {
		return operand1 + GE_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a >= condition.
	 */
	public static String ge(final String operand1, final Integer operand2) {
		return operand1 + GE_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a >= condition.
	 */
	public static String ge(final String operand1, final Long operand2) {
		return operand1 + GE_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a <= condition.
	 */
	public static String le(final String operand1, final String operand2) {
		return operand1 + LE_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a <= condition.
	 */
	public static String le(final String operand1, final Integer operand2) {
		return operand1 + LE_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a <= condition.
	 */
	public static String le(final String operand1, final Long operand2) {
		return operand1 + LE_KEYWORD + operand2;
	}

	/**
	 * @param operand1
	 * @param operand2
	 * @return a LIKE condition.
	 */
	public static String like(final String operand1, final String operand2) {
		return operand1 + LIKE_KEYWORD + operand2;
	}

	/**
	 * @param operand
	 * @return a quoted string.
	 */
	public static String quote(final String operand) {
		return QUOTE_KEYWORD + operand + QUOTE_KEYWORD;
	}

	/**
	 * @param values
	 * @return a ORDER BY operand
	 */
	private static String mergeOrderBy(final List<String> values) {
		if (values.isEmpty()) {
			return "";
		}
		String result = "";
		String separator = "";
		for (String value : values) {
			result += separator + value;
			separator = COMMA_KEYWORD;
		}
		return result;
	}

	/**
	 * @param values
	 * @return a ORDER BY operand
	 */
	private static String mergeOrderBy(final String [] values) {
		return mergeOrderBy(Arrays.asList(values));
	}

	/**
	 * @param from
	 * @return a HQL query
	 */
	public static String from(
			final String from) {
		return FROM_KEYWORD + from;
	}

	/**
	 * @param orderBy
	 * @return a ASC orderBy
	 */
	public static String asc(
			final String orderBy) {
		return orderBy + ASC_KEYWORD;
	}

	/**
	 * @param orderBy
	 * @return a DESC orderBy
	 */
	public static String desc(
			final String orderBy) {
		return orderBy + DESC_KEYWORD;
	}

	/**
	 * @param orderBy
	 * @return a ORDER BY clause
	 */
	public static String orderBy(
			final String orderBy) {
		return ORDER_BY_KEYWORD + orderBy;
	}

	/**
	 * @param orderBy
	 * @return a ORDER BY ASC clause
	 */
	public static String orderByAsc(
			final String orderBy) {
		return orderBy(asc(orderBy));
	}

	/**
	 * @param orderBy
	 * @return a ORDER BY DESC clause
	 */
	public static String orderByDesc(
			final String orderBy) {
		return orderBy(desc(orderBy));
	}

	/**
	 * @param from
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String fromOrderBy(
			final String from,
			final String orderBy) {
		return from(from) + orderBy(orderBy);
	}

	/**
	 * @param from
	 * @param orderBys
	 * @return a HQL query
	 */
	public static String fromOrderBy(
			final String from,
			final String [] orderBys) {
		return fromOrderBy(from, mergeOrderBy(orderBys));
	}

	/**
	 * @param from
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String fromOrderByAsc(
			final String from,
			final String orderBy) {
		return fromOrderBy(from, asc(orderBy));
	}

	/**
	 * @param from
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String fromOrderByDesc(
			final String from,
			final String orderBy) {
		return fromOrderBy(from, desc(orderBy));
	}

	/**
	 * @param from
	 * @param condition
	 * @return a HQL query
	 */
	public static String fromWhere(
			final String from,
			final String condition) {
		if (HqlUtils.isAlwaysTrue(condition)) {
			return from(from);
		}
		return from(from) + WHERE_KEYWORD + condition;
	}

	/**
	 * @param from
	 * @param condition
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String fromWhereOrderBy(
			final String from,
			final String condition,
			final String orderBy) {
		return fromWhere(from, condition) + orderBy(orderBy);
	}

	/**
	 * @param from
	 * @param condition
	 * @param orderBys
	 * @return a HQL query
	 */
	public static String fromWhereOrderBy(
			final String from,
			final String condition,
			final String [] orderBys) {
		return fromWhereOrderBy(from, condition, mergeOrderBy(orderBys));
	}

	/**
	 * @param from
	 * @param condition
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String fromWhereOrderByAsc(
			final String from,
			final String condition,
			final String orderBy) {
		return fromWhereOrderBy(from, condition, asc(orderBy));
	}

	/**
	 * @param from
	 * @param condition
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String fromWhereOrderByDesc(
			final String from,
			final String condition,
			final String orderBy) {
		return fromWhereOrderBy(from, condition, desc(orderBy));
	}

	/**
	 * @param select
	 * @param from
	 * @return a HQL query
	 */
	public static String selectFrom(
			final String select,
			final String from) {
		return SELECT_KEYWORD + select + from(from);
	}

	/**
	 * @param select
	 * @param from
	 * @param condition
	 * @return a HQL query
	 */
	public static String selectFromWhere(
			final String select,
			final String from,
			final String condition) {
		return SELECT_KEYWORD + select + fromWhere(from, condition);
	}

	/**
	 * @param select
	 * @param from
	 * @param condition
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String selectFromWhereOrderBy(
			final String select,
			final String from,
			final String condition,
			final String orderBy) {
		return SELECT_KEYWORD + select + fromWhere(from, condition) + orderBy(orderBy);
	}

	/**
	 * @param select
	 * @param from
	 * @param condition
	 * @param orderBys
	 * @return a HQL query
	 */
	public static String selectFromWhereOrderBy(
			final String select,
			final String from,
			final String condition,
			final String [] orderBys) {
		return selectFromWhereOrderBy(select, from, condition, mergeOrderBy(orderBys));
	}

	/**
	 * @param select
	 * @param from
	 * @param condition
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String selectFromWhereOrderByAsc(
			final String select,
			final String from,
			final String condition,
			final String orderBy) {
		return selectFromWhereOrderBy(select, from, condition, asc(orderBy));
	}

	/**
	 * @param select
	 * @param from
	 * @param condition
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String selectFromWhereOrderByDesc(
			final String select,
			final String from,
			final String condition,
			final String orderBy) {
		return selectFromWhereOrderBy(select, from, condition, desc(orderBy));
	}

	/**
	 * @param select
	 * @param from
	 * @param condition
	 * @return a HQL query
	 */
	public static String selectDistinctFromWhere(
			final String select,
			final String from,
			final String condition) {
		return selectFromWhere(DISTINCT_KEYWORD + select, from, condition);
	}

	/**
	 * @param select
	 * @param from
	 * @param condition
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String selectDistinctFromWhereOrderBy(
			final String select,
			final String from,
			final String condition,
			final String orderBy) {
		return selectFromWhereOrderBy(DISTINCT_KEYWORD + select, from, condition, orderBy);
	}

	/**
	 * @param select
	 * @param from
	 * @param condition
	 * @param orderBys
	 * @return a HQL query
	 */
	public static String selectDistinctFromWhereOrderBy(
			final String select,
			final String from,
			final String condition,
			final String [] orderBys) {
		return selectFromWhereOrderBy(DISTINCT_KEYWORD + select, from, condition, mergeOrderBy(orderBys));
	}

	/**
	 * @param select
	 * @param from
	 * @param condition
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String selectDistinctFromWhereOrderByAsc(
			final String select,
			final String from,
			final String condition,
			final String orderBy) {
		return selectFromWhereOrderBy(DISTINCT_KEYWORD + select, from, condition, asc(orderBy));
	}

	/**
	 * @param select
	 * @param from
	 * @param condition
	 * @param orderBy
	 * @return a HQL query
	 */
	public static String selectDistinctFromWhereOrderByDesc(
			final String select,
			final String from,
			final String condition,
			final String orderBy) {
		return selectFromWhereOrderByDesc(DISTINCT_KEYWORD + select, from, condition, asc(orderBy));
	}

	/**
	 * @param operand
	 * @return a COUNT clause.
	 */
	private static String count(final String operand) {
		return COUNT_KEYWORD + parenth(operand);
	}

	/**
	 * @param operand
	 * @param from
	 * @return a COUNT clause.
	 */
	public static String selectCountFrom(
			final String operand,
			final String from) {
		return selectFrom(count(operand), from);
	}

	/**
	 * @param operand
	 * @param from
	 * @param condition
	 * @return a COUNT clause.
	 */
	public static String selectCountFromWhere(
			final String operand,
			final String from,
			final String condition) {
		return selectFromWhere(count(operand), from, condition);
	}

	/**
	 * @param from
	 * @return a COUNT clause.
	 */
	public static String selectCountAllFrom(
			final String from) {
		return selectFrom(COUNT_ALL_PHRASE, from);
	}

	/**
	 * @param from
	 * @param condition
	 * @return a COUNT clause.
	 */
	public static String selectCountAllFromWhere(
			final String from,
			final String condition) {
		return selectFromWhere(COUNT_ALL_PHRASE, from, condition);
	}

	/**
	 * @param query
	 * @return an EXISTS condition.
	 */
	public static String exists(final String query) {
		return EXISTS_KEYWORD + parenth(query);
	}

	/**
	 * @param delete
	 * @return a HQL query
	 */
	public static String delete(
			final String delete) {
		return DELETE_KEYWORD + delete;
	}

	/**
	 * @param delete
	 * @param condition
	 * @return a HQL query
	 */
	public static String deleteWhere(
			final String delete,
			final String condition) {
		return delete(delete) + WHERE_KEYWORD + condition;
	}

	/**
	 * @param update
	 * @param set
	 * @return a HQL query
	 */
	public static String update(
			final String update,
			final String set) {
		return UPDATE_KEYWORD + update + SET_KEYWORD + set;
	}

	/**
	 * @param update
	 * @param set
	 * @param condition
	 * @return a HQL query
	 */
	public static String updateWhere(
			final String update,
			final String set,
			final String condition) {
		return update(update, set) + WHERE_KEYWORD + condition;
	}

}
