package com.query.engine.infra;

import java.util.ArrayList;

public class QueryResultColumnData {

	private String column_name;
	private ArrayList<String> data;
	
	QueryResultColumnData(final String colname)
	{
		column_name = colname;
		data = new ArrayList<String>();
	}
	
	public String getColumnName()
	{
		return column_name;
	}
	public ArrayList<String> getData()
	{
		return data;
	}
}
