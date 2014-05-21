package com.query.engine;

import com.query.engine.infra.KnowledgeBase;

public class Context {

	private static KnowledgeBase kb;

	public static KnowledgeBase getKB(final String path) {
		if (kb == null) {
			kb = new KnowledgeBase(path);
		}
		return kb;
	}
}
