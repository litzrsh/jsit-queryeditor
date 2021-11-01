package me.litz.exception;

import me.litz.model.Query;

public class QueryUploadException {

	private final Query query;

	private final String message;

	public QueryUploadException(final Query query, final String message) {
		this.query = query;
		this.message = message;
	}

	public Query getQuery() {
		return query;
	}

	public String getMessage() {
		return message;
	}
}
