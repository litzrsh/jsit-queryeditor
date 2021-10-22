package me.litz.util;

import me.litz.mapper.QueryMapper;
import me.litz.mapper.QueryMapperImpl;

public class MapperUtils {

    private static QueryMapper queryMapper;

    static {
        queryMapper = new QueryMapperImpl(SessionFactoryUtils.getSqlSessionFactory());
    }

    public static QueryMapper getQueryMapper() {
        return queryMapper;
    }

    public static void setQueryMapper(QueryMapper queryMapper) {
        MapperUtils.queryMapper = queryMapper;
    }
}
