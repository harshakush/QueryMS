package com.query.engine.infra;

import java.util.ArrayList;

public class QueryWhereParams {
	private String where;
	private ArrayList<String> params;

	public QueryWhereParams(final ArrayList<String> pm, final String rel) {
		where = rel;
		params = new ArrayList<String>(pm);
	}

	public QueryWhereParams() {

	}

	public void setParams(final ArrayList<String> pm) {
		params = new ArrayList<String>(pm);
	}

	public ArrayList<String> getParams() {
		return params;
	}

	public void setRel(final String rel) {
		where = rel;
	}

	public String getWhereTable() {
		return where;
	}
}
