package me.litz.model;

import me.litz.exception.QueryUploadException;

import java.util.ArrayList;
import java.util.List;

public class UploadFileRecursiveResult {

	private int totalFiles = 0;

	private final List<QueryUploadException> exceptions = new ArrayList<>();

	public int getTotalFiles() {
		return totalFiles;
	}

	public void addCount() {
		totalFiles++;
	}

	public List<QueryUploadException> getExceptions() {
		return exceptions;
	}

	public void addException(QueryUploadException e) {
		if (e != null) {
			exceptions.add(e);
		}
	}
}
