package com.query.engine.infra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.query.engine.constants.KBConstants;
import com.query.engine.exceptions.KBException;
import com.query.engine.exceptions.KBFileWriteException;
import com.query.engine.exceptions.KBParseException;
import com.query.engine.filesys.QueryResultWriter;

public class KnowledgeBase {

	private HashMap<String, Map<String, ArrayList<String>>> map = new HashMap<String, Map<String, ArrayList<String>>>();

	/**
	 * 
	 * constructor takes input as file path from where the entries need to be
	 * loaded.
	 * 
	 * @param dbpath
	 */
	public KnowledgeBase(final String dbpath) {
		readFile(dbpath);
	}

	/**
	 * This method will run the query on database and write the result set in
	 * given result file name.
	 * 
	 * @param qstr
	 * @param resultfile
	 * @return
	 * @throws KBFileWriteException
	 */
	public QueryResultTable run_query(final String qstr, final String resultfile)
			throws KBFileWriteException {
		try {
			System.out.println("Executing Query : " + qstr);

			ArrayList<QueryResultTable> result = QueryUtils.db_run_query(map,
					qstr);
			QueryResultTable entry;
			if (result != null) {
				entry = result.get(result.size() - 1);
				entry.persist();
				return entry;
			}
		} catch (KBParseException e) {
			// e.printStackTrace();
			System.out.println("Failed to parse the query" + qstr);
		}
		return null;
	}

	/**
	 * 
	 * @param dbpath
	 */
	private void readFile(final String dbpath) {
		try {
			QueryUtils.readFileAndCreateKB(dbpath, map);
			JSONObject obj = new JSONObject(map);
			System.out.println(obj.toJSONString());
			new QueryResultWriter().persist_db(obj.toJSONString(),
					KBConstants.DB_PATH);
		} catch (KBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
