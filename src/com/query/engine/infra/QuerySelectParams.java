package com.query.engine.infra;

import java.util.ArrayList;

public class QuerySelectParams {
	private ArrayList<String> select_params;

	public QuerySelectParams() {

	}

	public void setQuerySelectParams(final ArrayList<String> list) {
		select_params = list;
	}

	public QuerySelectParams(final ArrayList<String> params) {
		select_params = params;
	}

	public ArrayList<String> getQuerySelectParams() {
		return select_params;
	}

}
