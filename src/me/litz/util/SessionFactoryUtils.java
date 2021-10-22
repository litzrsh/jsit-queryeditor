package me.litz.util;

import me.litz.mapper.QueryMapperImpl;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class SessionFactoryUtils {

    private static String database = "EMS";

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            String resource = "mybatis-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);

            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder()
                        .build(reader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public static void setDatabase(String database) {
        SessionFactoryUtils.database = database;
        try {
            String resource = "mybatis-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);

            sqlSessionFactory = new SqlSessionFactoryBuilder()
                    .build(reader, database);
            MapperUtils.setQueryMapper(new QueryMapperImpl(sqlSessionFactory));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
