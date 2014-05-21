package com.query.engine.infra;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.query.engine.constants.KBConstants;
import com.query.engine.exceptions.KBFileWriteException;

public class QueryResultTable {

	private String tableName;
	private HashMap<String, ArrayList<String>> table_contents;

	public QueryResultTable(final String tableN) {
		this.tableName = tableN;
		table_contents = new HashMap<String, ArrayList<String>>();
	}

	public String getTableName() {
		return tableName;
	}

	public HashMap<String, ArrayList<String>> getTableContents() {
		return table_contents;
	}

	public void persist() throws KBFileWriteException {
		java.util.Date date = new java.util.Date();
		Timestamp time = new Timestamp(date.getTime());

		String path = KBConstants.QUERY_RESULT + time.getTime() + ".json";
		JSONObject obj = new JSONObject(table_contents);
		// new QueryResultWriter().persist_db(obj.toJSONString(), path);
		//System.out.println(obj.toJSONString());
		//return obj.toJSONString()
	}

}
