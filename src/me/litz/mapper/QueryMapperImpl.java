package me.litz.mapper;

import me.litz.model.Query;
import me.litz.util.SessionUtils;
import me.litz.util.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryMapperImpl implements QueryMapper {

	public static final String PACKAGE = "me.litz.mapper.QueryMapper.";

	private final SqlSessionFactory factory;

	public QueryMapperImpl(SqlSessionFactory sqlSessionFactory) {
		this.factory = sqlSessionFactory;
	}

	@Override
	@SuppressWarnings({"unchecked"})
	public List<Query> listQueries(String id) {
		List<Query> result;
		SqlSession session = factory.openSession();
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("id", StringUtils.trim(id));
			result = (List<Query>) session.selectList(PACKAGE + "listQueries", params);
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public Query getQuery(String id) {
		Query entity;
		SqlSession session = factory.openSession();
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("id", StringUtils.trim(id));
			entity = (Query) session.selectOne(PACKAGE + "getQuery", params);
		} finally {
			session.close();
		}
		return entity;
	}

	@Override
	public void addQuery(Query entity) throws Throwable {
		Throwable t = null;
		SqlSession session = factory.openSession(false);
		try {
			entity.setRevision(getRevision(session, entity.getId()));
			entity.setCreator(SessionUtils.getUsername());
			entity.setUpdater(SessionUtils.getUsername());

			session.insert(PACKAGE + "addQuery", entity);
			session.insert(PACKAGE + "makeBackup", entity);

			session.commit();
		} catch (Throwable e) {
			t = e;
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}

		if (t != null) throw t;
	}

	@Override
	public void editQuery(Query entity) throws Throwable {
		Throwable t = null;
		SqlSession session = factory.openSession(false);
		try {
			Query data = getQuery(entity.getId());
			if (data == null) throw new Exception();

			entity.setRevision(getRevision(session, entity.getId()));
			entity.setId(data.getId());
			entity.setUpdater(SessionUtils.getUsername());

			session.update(PACKAGE + "editQuery", entity);
			session.insert(PACKAGE + "makeBackup", entity);

			session.commit();
		} catch (Throwable e) {
			t = e;
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}

		if (t != null) throw t;
	}

	@Override
	public void deleteQuery(String id) throws Throwable {
		Throwable t = null;
		SqlSession session = factory.openSession(false);
		try {
			Query entity = getQuery(id);
			if (entity == null) throw new Exception();

			session.delete(PACKAGE + "deleteQuery", entity);

			session.commit();
		} catch (Throwable e) {
			t = e;
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}

		if (t != null) throw t;
	}

	protected int getRevision(SqlSession session, String id) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		Integer revision = (Integer) session.selectOne(PACKAGE + "getRevisionNew", paramMap);
		session.update(PACKAGE + "updateRevisionNew");
		return revision;
//        Integer r = (Integer) session.selectOne(PACKAGE + "getRevision");
//        Map<String, Object> params = new HashMap<>();
//        if (r == null) {
//            params.put("revision", 1);
//            session.insert(PACKAGE + "insertRevision", params);
//            return 1;
//        } else {
//            params.put("previous", r);
//            params.put("current", r + 1);
//            session.update(PACKAGE + "updateRevision", params);
//            return r + 1;
//        }
	}
}
