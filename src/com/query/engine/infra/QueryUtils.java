/**
 * 
 */
/**
 * @author harshak
 *
 */
package com.query.engine.infra;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.query.engine.exceptions.KBFailedToCreateDataBase;
import com.query.engine.exceptions.KBFileReadException;
import com.query.engine.exceptions.KBParseException;

class QueryUtils {

	public static void readFileAndCreateKB(final String filePath,
			HashMap<String, Map<String, ArrayList<String>>> map)
			throws java.io.IOException, KBFileReadException,
			KBFailedToCreateDataBase {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			throw new KBFailedToCreateDataBase();

		}

		Pattern htmltag = Pattern.compile("<.*>");

		try {
			while (in.ready()) {
				String s = in.readLine();
				// System.out.println(s);
				Matcher tagmatch = htmltag.matcher(s);
				if (tagmatch.find()) {

					String retval[] = tagmatch.group().split(",", 3);
					String token1, tokenrel, token2;
					if (retval.length == 2)
						continue;
					token1 = retval[0];
					token2 = retval[2];
					tokenrel = retval[1];
					createDB(token1.trim(), tokenrel.trim(), token2.trim(), map);
				}

			}
			in.close();
		} catch (IOException e) {
			throw new KBFailedToCreateDataBase();
		}
	}

	private static void createDB(String token1, String rel, String token2,
			HashMap<String, Map<String, ArrayList<String>>> map) {
		// System.out.println("Toke1 :" + token1 + "Relation is : " + rel
		// + "Token 2: " + token2);
		Map<String, ArrayList<String>> recmap;
		String relations = rel.trim();
		recmap = map.get(relations);
		if (recmap == null) {
			recmap = new HashMap<String, ArrayList<String>>();
			ArrayList<String> newlist = new ArrayList<String>();
			newlist.add(token2);
			recmap.put(token1, newlist);
			map.put(relations, recmap);
		} else {

			ArrayList<String> newlist = recmap.get(token1);
			if (newlist == null) {
				newlist = new ArrayList<String>();
			}
			newlist.add(token2);
			recmap.put(token1, newlist);
			map.put(relations, recmap);
		}

	}

	public static ArrayList<QueryResultTable> db_run_query(
			final HashMap<String, Map<String, ArrayList<String>>> map,
			final String query) throws KBParseException {

		// first get the first level tokens
		// and then generate the below items.

		if (query.trim().startsWith("select")) {

			String[] tokens = get_first_level_token(query);
			if (tokens.length != 2) {
				return null;
			}
			QueryParams qp = new QueryParams(map, tokens);
			qp.parse();
			ArrayList<QueryResultTable> result = filter_records(qp, map);
			return result;
		} else {
			update_table();
		}
		return null;
	}

	public static String[] get_first_level_token(final String query)
			throws KBParseException {
		String regex = "select (.*) where \\{(.*)\\}";
		Pattern htmltag = Pattern.compile(regex);
		Matcher tagmatch = htmltag.matcher(query);
		String[] tokens = new String[2];
		int i = 0;
		while (tagmatch.find()) {
			tokens[i] = tagmatch.group(i + 1);
			i++;
			tokens[i] = tagmatch.group(i + 1);
		}
		if (tokens[0] == null || tokens[1] == null)
			throw new KBParseException();
		return tokens;
	}

	public static ArrayList<QueryResultTable> filter_records(QueryParams qp,
			final HashMap<String, Map<String, ArrayList<String>>> map) {

		ArrayList<String> select_pms = qp.getQuerySelectParams()
				.getQuerySelectParams();

		ArrayList<QueryWhereParams> where_pms = qp.getQueryWhereParams();
		ArrayList<QueryResultTable> result = new ArrayList<QueryResultTable>();

		for (QueryWhereParams entry : where_pms) {

			QueryResultTable result_table = new QueryResultTable("result");
			String table = entry.getWhereTable();
			Map<String, ArrayList<String>> table_con = map.get(table);
			if (table_con == null) {
				// throw exception;
				// To Do
				return null;
			}
			// System.out.println("Applying filter on table: " + table);
			ArrayList<String> where_clause = entry.getParams();

			String p1 = where_clause.get(0);
			String p2 = where_clause.get(1);

			String pattern_search = "";
			if (p1.startsWith("?")) {
				ArrayList<String> al = result_table.getTableContents().get(p1);
				if (al == null) {
					al = new ArrayList<String>();
					result_table.getTableContents().put(p1.trim(), al);
				}

			} else {
				pattern_search = p1;
			}

			if (p2.startsWith("?")) {
				ArrayList<String> al = result_table.getTableContents().get(p2);
				if (al == null) {
					al = new ArrayList<String>();
					result_table.getTableContents().put(p2.trim(), al);
				}
			} else {
				pattern_search = p2;
			}
			if (pattern_search.isEmpty()) {
				// copy all the records
				// and continue with next where clause
				// result_table
				// table_con

				for (Entry<String, ArrayList<String>> entrymap : table_con
						.entrySet()) {
					ArrayList<String> a1 = result_table.getTableContents().get(
							p1);
					ArrayList<String> a2 = result_table.getTableContents().get(
							p2);
					a2.addAll(entrymap.getValue());
					for (int i = 0; i < entrymap.getValue().size(); i++)
						a1.add(entrymap.getKey());
				
				}

			} else {
				// filter it out
				if (pattern_search.contentEquals(p1)) {
					// search in the first column.
					for (Entry<String, ArrayList<String>> entrymap : table_con
							.entrySet()) {
						if (p1.contentEquals(entrymap.getKey())) {

							ArrayList<String> a2 = result_table
									.getTableContents().get(p2);
							a2.addAll(entrymap.getValue());
						}
					}
				} else {
					// search in the second column.
					// search in the first column.
					for (Entry<String, ArrayList<String>> entrymap : table_con
							.entrySet()) {
						if (/* p2.contentEquals(entrymap.getValue().trim()) */entrymap
								.getValue().contains(p2)) {
							ArrayList<String> a1 = result_table
									.getTableContents().get(p1);
							a1.add(entrymap.getKey());
						}
					}
				}
			}
			result.add(result_table);
		}
		// merge results result_table
		// HashMap<String, ArrayList<String>> mps =
		// result_table.getTableContents();

		for (int two = 0; two < 2; two++) {
			for (int i = 0; i < result.size() - 1; i++) {
				// result
				HashMap<String, ArrayList<String>> table = result.get(i)
						.getTableContents();

				for (Entry<String, ArrayList<String>> entry : table.entrySet()) {
					String columnName1 = entry.getKey();
					for (int k = i + 1; k < result.size(); k++) {

						HashMap<String, ArrayList<String>> tableInner = result
								.get(k).getTableContents();

						for (Entry<String, ArrayList<String>> entryInner : tableInner
								.entrySet()) {
							String columnName2 = entryInner.getKey();
							// ///// ******************* //////
							// ///// Moment of truth //////
							// ///// Filter the records //////
							// ///// The entries need to //////
							// ///// be present in both //////
							// ///// the column //////
							// ///// to satisfy the //////
							// ///// condition //////
							// ///// ******************* //////

							if (columnName1.contentEquals(columnName2)) {
								// adjust entry and entry inner.
								// rule of thumb for common nodes.
								// if anything missing in any array
								// delete it from the other array too.
								ArrayList<String> diff_elements = get_difference_entries(
										table, tableInner, columnName1);

								for (String value_to_delete : diff_elements) {
									delete_diff_entries(table, columnName1,
											value_to_delete);
									delete_diff_entries(tableInner,
											columnName1, value_to_delete);
								}

							}

						}
					}
				}
			}
		}

		// populate the result in new table finally
		QueryResultTable final_result = new QueryResultTable("Final");

		for (String select_item : select_pms) {
			ArrayList<String> al;

			for (QueryResultTable table : result) {
				al = table.getTableContents().get("?" + select_item.trim());
				if (al != null) {
					final_result.getTableContents().put(
							"?" + select_item.trim(), al);
					break;
				}
			}

		}

		result.add(final_result);
		return result;

	}


	public static void delete_diff_entries(
			HashMap<String, ArrayList<String>> t1, final String common_column,
			final String element) {

		ArrayList<String> input_array = t1.get(common_column);
		int index;
		if (input_array.contains(element)) {
			index = input_array.indexOf(element);

			for (Entry<String, ArrayList<String>> entry : t1.entrySet()) {
				// Need to check the index bound
				// To Do. Throw exception saying db in inconsistent state.
				ArrayList<String> inner_input_array = entry.getValue();
				inner_input_array.remove(index);
			}
		}
		// to satisfy compiler

	}

	public static ArrayList<String> get_common_entries(
			HashMap<String, ArrayList<String>> t1,
			HashMap<String, ArrayList<String>> t2, final String common_column) {

		for (Entry<String, ArrayList<String>> entry : t1.entrySet()) {

			for (Entry<String, ArrayList<String>> entryInner : t2.entrySet()) {
				if (entry.getKey().contentEquals(entryInner.getKey())) {
					// start the updation
					ArrayList<String> entryArray = entry.getValue();
					ArrayList<String> entryInnerArray = entryInner.getValue();
					ArrayList<String> common = new ArrayList<String>(
							entryInnerArray);// (entryInnerArray);
					common.retainAll(entryArray);

					return common;
				}
			}
		}
		// to satisfy compiler
		return null;
	}

	public static ArrayList<String> get_difference_entries(
			HashMap<String, ArrayList<String>> t1,
			HashMap<String, ArrayList<String>> t2, final String common_column) {

		// start the updation
		ArrayList<String> entryArray = t1.get(common_column);
		ArrayList<String> entryInnerArray = t2.get(common_column);
		ArrayList<String> diff = new ArrayList<String>();

		for (int i = 0; i < entryArray.size(); i++) {

			if (!entryInnerArray.contains(entryArray.get(i).trim())) {
				diff.add(entryArray.get(i));
			}
		}

		for (int j = 0; j < entryInnerArray.size(); j++) {

			if (!entryArray.contains(entryInnerArray.get(j).trim())) {

				diff.add(entryInnerArray.get(j));
			}

		}

		return diff;
	}

	public static void parse_query_parameters(
			final HashMap<String, Map<String, ArrayList<String>>> map,
			final String[] tokens, QuerySelectParams select_params,
			ArrayList<QueryWhereParams> where_params) throws KBParseException {

		ArrayList<String> select = new ArrayList<String>();
		// Step 1:
		// populate select items first.
		if (tokens[0].trim().equals("*")) {
			// means select all.
			select.add("*");
		} else {
			String[] select_tokens = tokens[0].split("\\?");
			for (String tk : select_tokens) {
				String temp = tk;
				if (tk.trim().startsWith("\\?")) {
					temp = tk.trim().substring(1, tk.trim().length());
				}
				if (!temp.trim().isEmpty())
					select.add(temp);
			}
		}
		select_params.setQuerySelectParams(select);

		// Step 2:
		// populate where clause items second.
		// if there is no map associated with the
		// given key then we will assume the value
		// as literal meant to be compared and add it to literal values.
		String[] where_tokens = tokens[1].split("\\.");
		for (String each_token : where_tokens) {
			String regex = "(<.*?>)";
			Pattern htmltag = Pattern.compile(regex);
			Matcher tagmatch = htmltag.matcher(each_token);
			String whr = "";
			String rel = "";
			String p1 = "";
			String p2 = "";
			boolean literals_found = false;
			ArrayList<String> pms = new ArrayList<String>();

			while (tagmatch.find()) {
				whr = tagmatch.group();
				if (map.get(whr) == null) {
					// pms.add(whr.trim());
					p1 = whr;
					literals_found = true;
				} else {
					rel = whr.trim();
				}
			}

			String regexPlaceHolders = "(\\?.*)";
			Pattern placeHolder = Pattern.compile(regexPlaceHolders);
			Matcher pltagmatch = placeHolder.matcher(each_token.trim());

			if (pltagmatch.find()) {
				whr = pltagmatch.group();
				String[] temp_tokens = whr.split("\\s+");

				for (int i = 0; i < temp_tokens.length; i++) {

					if (!rel.contentEquals(temp_tokens[i])) {
						if (literals_found) {
							// pms.add(temp_tokens[i].trim());
							p2 = temp_tokens[i].trim();
							break;
						}
						if (p1.isEmpty()) {
							p1 = temp_tokens[i].trim();
							continue;
						}
						if (p2.isEmpty())
							p2 = temp_tokens[i].trim();
					}
				}
			} else {
				continue;
			}

			QueryWhereParams where_param = new QueryWhereParams();
			where_param.setRel(rel);
			if (each_token.trim().startsWith("?")) {
				if (p1.startsWith("?")) {
					pms.add(p1);
					pms.add(p2);
				} else {
					pms.add(p2);
					pms.add(p1);
				}
			} else if (each_token.trim().startsWith("<")) {
				if (p1.startsWith("<")) {
					pms.add(p1);
					pms.add(p2);
				} else {
					pms.add(p2);
					pms.add(p1);
				}
			}
			if (pms.size() < 2 | p1.trim().isEmpty() || p2.trim().isEmpty()
					|| (p1.trim().contentEquals(p2.trim()))) {
				throw new KBParseException();
			}
			where_param.setParams(pms);
			where_params.add(where_param);
		}

	}
	

	public static void update_table() {
		String simple_query = "UPDATE TBLENAME WHERE p1 <rel> <l>";

		String regex = "UPDATE (.*) WHERE (.*)";
		Pattern htmltag = Pattern.compile(regex);
		Matcher tagmatch = htmltag.matcher(simple_query);
		String whr = "";

		if(tagmatch.find()) {
			whr = tagmatch.group(1);
			String copy = tagmatch.group(2);
		}

	}
}