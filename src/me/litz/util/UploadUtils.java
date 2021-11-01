package me.litz.util;

import me.litz.exception.QueryUploadException;
import me.litz.mapper.QueryMapper;
import me.litz.model.Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UploadUtils {

	public static QueryUploadException uploadByFile(File file) {
		String id = file.getName().replaceAll("(\\.([Ss])([Qq])([Ll]))", "");
		String title = "", query = "";
		StringBuilder queryBuilder = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			title = reader.readLine().replaceAll("^/\\* Last Modified : .* by .+? - (.+)\\*/$", "$1");
			String line;
			while ((line = reader.readLine()) != null) {
				queryBuilder.append(line).append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		query = queryBuilder.toString();

		Query entity = new Query();
		entity.setId(id);
		entity.setTitle(title);
		entity.setQuery(query);

		System.out.println("id : " + id + ", title : " + title + "\r\n" + query + "\r\n");

//		Throwable e = tryInsert(entity);
//		if (e != null) {
//			e = tryUpdate(entity);
//		}
//		if (e != null) {
//			return new QueryUploadException(entity, e.getMessage());
//		}
		return null;
	}

	protected static Throwable tryInsert(Query entity) {
		try {
			QueryMapper queryMapper = MapperUtils.getQueryMapper();
			queryMapper.addQuery(entity);
			return null;
		} catch (Throwable e) {
			return e;
		}
	}

	protected static Throwable tryUpdate(Query entity) {
		try {
			QueryMapper queryMapper = MapperUtils.getQueryMapper();
			queryMapper.editQuery(entity);
			return null;
		} catch (Throwable e) {
			return e;
		}
	}
}
