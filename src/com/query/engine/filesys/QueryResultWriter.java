/**
 * 
 */
/**
 * @author harshak
 *
 */
package com.query.engine.filesys;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.query.engine.exceptions.KBFileWriteException;

public class QueryResultWriter {
	/**
	 * 
	 * Writes the db loaded contents from map to a file in json format.
	 * 
	 * @param store
	 * @throws KBFileWriteException
	 */
	public void persist_db(final String store, final String path)
			throws KBFileWriteException {
		try {
			File file = new File(path);

			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(store);
			bw.close();

		} catch (IOException e) {
			throw new KBFileWriteException();
		}

	}
}