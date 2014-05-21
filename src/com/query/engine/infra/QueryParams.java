package com.query.engine.infra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.query.engine.exceptions.KBParseException;

public class QueryParams {
	private QuerySelectParams select_params;
	private ArrayList<QueryWhereParams> where_params;
	private String[] tokens;
	private HashMap<String, Map<String, ArrayList<String>>> map;

	public QueryParams(
			final HashMap<String, Map<String, ArrayList<String>>> map,
			final String[] tokens) {
		select_params = new QuerySelectParams();
		where_params = new ArrayList<QueryWhereParams>();
		this.tokens = tokens;
		this.map = map;

	}

	public void parse() throws KBParseException {

		QueryUtils.parse_query_parameters(map, tokens, select_params,
				where_params);

		if (where_params.isEmpty())
			throw new KBParseException();

	}

	public QuerySelectParams getQuerySelectParams() {
		return select_params;
	}

	public ArrayList<QueryWhereParams> getQueryWhereParams() {
		return where_params;
	}
}