package me.litz.mapper;

import me.litz.model.Query;

import java.util.List;

public interface QueryMapper {

    List<Query> listQueries(String id);

    Query getQuery(String id);

    void addQuery(Query entity) throws Throwable;

    void editQuery(Query entity) throws Throwable;

    void deleteQuery(String id) throws Throwable;
}
