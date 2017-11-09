package com.mokylin.bleach.core.orm.hibernate;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.google.common.base.Predicate;
import com.mokylin.bleach.core.orm.BaseEntity;
import com.mokylin.bleach.core.orm.DataAccessException;
import com.mokylin.bleach.core.util.PackageUtil;

/**
 * 数据库访问服务
 * @author baoliang.shen
 *
 */
public class HibernateDBService {

	private SessionFactory sessionFactory;

	private final HibernamteTemplate transTemplate = new TranHibernateTemplate();

	/**
	 * 构建HibernateDBService
	 * @param packageName
	 * @param p
	 * @param hibernateCfgXmlUrl
	 * @param resourceNames
	 */
	public HibernateDBService(String packageName, Properties p, URL hibernateCfgXmlUrl, String... resourceNames) {

		Configuration cfg = new Configuration();

		// 数据服务配置
		cfg.addProperties(p);
		cfg.configure(hibernateCfgXmlUrl);
		if (resourceNames != null) {
			for (String _resourceName : resourceNames) {
				cfg.addResource(_resourceName);
			}
		}

		// 扫描加载指定包内的所有Entity
		if (packageName!=null && !packageName.isEmpty()) {
			Set<Class<?>> handlerSet = PackageUtil.getPackageClasses(packageName, new Predicate<Class<?>>() {
				@Override
				public boolean apply(Class<?> input) {
					return BaseEntity.class.isAssignableFrom(input);
				}
			});
			for (Class<?> class1 : handlerSet) {
				cfg.addAnnotatedClass(class1);
			}
		}

		sessionFactory = null;
		try {
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
			sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		} catch (Exception e) {
			e.printStackTrace();
			sessionFactory = null;
		}

		if (sessionFactory == null) {
			//TODO 记日志
			return;
		}
	}

	public void close() {
		if (sessionFactory != null && !sessionFactory.isClosed()) {
			sessionFactory.close();
		}
	}

	public interface HibernateCallback<T> {
		public T doCall(Session session);
	}
	public interface HibernateCallbackList<T> {
		public List<T> doCallList(Session session);
	}
	private interface HibernamteTemplate {
		public <T> T doGuestCall(HibernateCallback<T> callback);

		public <T> List<T> doGuestCallList(HibernateCallbackList<T> callback);
	}
	private final class TranHibernateTemplate implements HibernamteTemplate {

		@Override
		public <T> T doGuestCall(HibernateCallback<T> callback) {
			//此Session类并没有实现java.lang.AutoCloseable，所以无法使用java7的try-with-resources特性
			Session _session = null;
			Transaction _tr = null;
			T _result = null;

			try {
				_session = sessionFactory.openSession();
				//_session.setFlushMode(FlushMode.COMMIT);//
				_tr = _session.beginTransaction();
				_result = callback.doCall(_session);
				_tr.commit();
			} catch (Exception e) {
				if (_tr != null) {
					_tr.rollback();
				}
				throw new DataAccessException(e);
			} finally {
				try {
					if (_session != null) {
						_session.close();
					}
				} finally {
					// 在这里触发事件通知,避免影响连接的释放

				}
			}
			return _result;
		}
		
		@Override
		public <T> List<T> doGuestCallList(HibernateCallbackList<T> callback) {
			//此Session类并没有实现java.lang.AutoCloseable，所以无法使用java7的try-with-resources特性
			Session _session = null;
			Transaction _tr = null;
			List<T> _result = null;

			try {
				_session = sessionFactory.openSession();
				//_session.setFlushMode(FlushMode.COMMIT);//
				_tr = _session.beginTransaction();
				_result = callback.doCallList(_session);
				_tr.commit();
			} catch (Exception e) {
				if (_tr != null) {
					_tr.rollback();
				}
				throw new DataAccessException(e);
			} finally {
				try {
					if (_session != null) {
						_session.close();
					}
				} finally {
					// 在这里触发事件通知,避免影响连接的释放

				}
			}
			return _result;
		}

	}


	/**
	 * 根据主键取Entity
	 * @param entityClass
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public <T extends BaseEntity> T getById(final Class<T> entityClass, final Serializable id) throws DataAccessException {
		return transTemplate.doGuestCall(new HibernateCallback<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T doCall(Session session) {
				return (T) session.get(entityClass, id);
			}
		});
	}
	/**
	 * 根据一组主键取Entity
	 * @param entityClass
	 * @param idList
	 * @return
	 * @throws DataAccessException
	 */
	public <T extends BaseEntity,IdType extends Serializable> List<T> getByIdBatch(final Class<T> entityClass, final List<IdType> idList) throws DataAccessException {
		return transTemplate.doGuestCallList(new HibernateCallbackList<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<T> doCallList(Session session) {
				List<T> tempList = new ArrayList<T>();
				for (Serializable id : idList) {
					T tempT = (T) session.get(entityClass, id);
					tempList.add(tempT);
				}
				return tempList;
			}
		});
	}
	/**
	 * 取某表中的所有Entity
	 * @param entityClass
	 * @return
	 * @throws DataAccessException
	 */
	public <T extends BaseEntity> List<T> getAll(final Class<T> entityClass) throws DataAccessException {
		final String _className = entityClass.getSimpleName();
		final String _sql = String.format("from %s ", _className);
		return transTemplate.doGuestCallList(new HibernateCallbackList<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doCallList(Session session) {
				Query _query = session.createQuery(_sql);
				return _query.list();
			}
		});
	}

	/**
	 * 更新Entity
	 * @param entity
	 * @throws DataAccessException
	 */
	public void update(final BaseEntity entity) throws DataAccessException {
		transTemplate.doGuestCall(new HibernateCallback<Object>() {
			@Override
			public Object doCall(Session session) {
				session.update(entity);
				return null;
			}
		});

	}
	
	/**
	 * 批量更新Entity
	 * @param allToUpdateList
	 * @throws DataAccessException
	 */
	public void updateBatch(final List<BaseEntity> allToUpdateList) throws DataAccessException {
		transTemplate.doGuestCall(new HibernateCallback<Object>() {
			@Override
			public Object doCall(Session session) {
				for (BaseEntity baseEntity : allToUpdateList) {
					session.update(baseEntity);
				}
				return null;
			}
		});

	}

	/**
	 * 新插入一条数据
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public Serializable save(final BaseEntity entity) throws DataAccessException {
		return transTemplate.doGuestCall(new HibernateCallback<Serializable>() {
			@Override
			public Serializable doCall(Session session) {
				return session.save(entity);
			}
		});
	}
	
	/**
	 * 批量插入数据
	 * @param allToSaveList
	 * @return
	 * @throws DataAccessException
	 */
	public Serializable[] saveBatch(final List<BaseEntity> allToSaveList) throws DataAccessException {
		return transTemplate.doGuestCall(new HibernateCallback<Serializable[]>() {
			@Override
			public Serializable[] doCall(Session session) {
				List<Serializable> ids = new ArrayList<Serializable>();
				for (BaseEntity baseEntity : allToSaveList) {
					Serializable id = session.save(baseEntity);
					ids.add(id);
				}
				return ids.toArray(new Serializable[ids.size()]);
			}
		});
	}

	/**
	 * 无则插入，有则更新
	 * @param entity
	 * @throws DataAccessException
	 */
	public void saveOrUpdate(final BaseEntity entity) throws DataAccessException {
		this.transTemplate.doGuestCall(new HibernateCallback<Void>() {
			@Override
			public Void doCall(Session session) {
				session.saveOrUpdate(entity);
				return null;
			}
		});
	}
	
	/**
	 * 批量 无则插入，有则更新
	 * @param entityArray
	 * @throws DataAccessException
	 */
	public void saveOrUpdateBatch(final List<BaseEntity> entityArray) throws DataAccessException {
		this.transTemplate.doGuestCall(new HibernateCallback<Void>() {
			@Override
			public Void doCall(Session session) {
				for (BaseEntity baseEntity : entityArray) {
					session.saveOrUpdate(baseEntity);
				}
				return null;
			}
		});
	}

	/**
	 * 删除一条记录
	 * @param entity
	 * @throws DataAccessException
	 */
	public void delete(final BaseEntity entity) throws DataAccessException {
		transTemplate.doGuestCall(new HibernateCallback<Void>() {
			@Override
			public Void doCall(Session session) {
				session.delete(entity);
				return null;
			}
		});
	}
	
	/**
	 * 批量删除记录
	 * @param allToDeleteList
	 * @throws DataAccessException
	 */
	public void deleteBatch(final List<BaseEntity> allToDeleteList) throws DataAccessException {
		transTemplate.doGuestCall(new HibernateCallback<Void>() {
			@Override
			public Void doCall(Session session) {
				for (BaseEntity baseEntity : allToDeleteList) {
					session.delete(baseEntity);
				}
				return null;
			}
		});
	}

	/**
	 * 删除某条记录，根据以下参数
	 * @param entityClass	Entity类型
	 * @param id			主键
	 */
	public void deleteById(Class<? extends BaseEntity> entityClass, final Serializable id) {
		final String _className = entityClass.getSimpleName();
		final String _sql = String.format("delete from %s where id=?", _className);
		transTemplate.doGuestCall(new HibernateCallback<Void>() {
			@Override
			public Void doCall(Session session) {
				Query _query = session.createQuery(_sql);
				_query.setParameter(0, id);
				_query.executeUpdate();
				return null;
			}
		});
	}

	/**
	 * 批量删除，按照ID
	 * @param delList
	 */
	public void deleteByIdBatch(final List<EntityToDelete> delList) {
		transTemplate.doGuestCall(new HibernateCallback<Void>() {
			@Override
			public Void doCall(Session session) {
				for (EntityToDelete entityToDelete : delList) {
					final String _className = entityToDelete.entityClass.getSimpleName();
					final String _sql = String.format("delete from %s where id=?", _className);
					Query _query = session.createQuery(_sql);
					_query.setParameter(0, entityToDelete.id);
					_query.executeUpdate();
				}
				return null;
			}
		});
	}

	/**
	 * 执行带参数的HQL
	 * @param queryName
	 * @param paramNames
	 * @param values
	 * @return
	 * @throws DataAccessException
	 */
	public int queryForUpdate(final String queryName, final String[] paramNames, final Object[] values) throws DataAccessException {
		if (arrayLength(paramNames) != arrayLength(values)) {
			throw new IllegalArgumentException("The paramNames length != values length");
		}
		return transTemplate.doGuestCall(new HibernateCallback<Integer>() {
			@Override
			public Integer doCall(Session session) {
				Query _query = session.getNamedQuery(queryName);
				prepareQuery(paramNames, values, _query);
				return _query.executeUpdate();
			}
		});
	}
	
	/**
	 * 执行命名的HQL语句，把符合要求的数据全取出来
	 * @param entityClass	返回的数据类型
	 * @param queryName		HQL语句名
	 * @param paramNames	参数名数组
	 * @param values		参数数组
	 * @return
	 */
	public <T extends BaseEntity> List<T> findByNamedQueryAndNamedParamAllT(
			final Class<T> entityClass, final String queryName, final String[] paramNames, final Object[] values) {
		return findByNamedQueryAndNamedParamT(entityClass,queryName,paramNames,values,-1,-1);
	}
	public <T extends BaseEntity> List<T> findByNamedQueryAndNamedParamT(final Class<T> entityClass,
			final String queryName, final String[] paramNames, final Object[] values,
			final int maxResult, final int start)
			throws DataAccessException {
		if (arrayLength(paramNames) != arrayLength(values)) {
			throw new IllegalArgumentException("The paramNames length != values length");
		}
		return transTemplate.doGuestCall(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doCall(Session session) {
				Query _query = session.getNamedQuery(queryName);
				if (maxResult > -1) {
					_query.setMaxResults(maxResult);
				}
				if (start > -1) {
					_query.setFirstResult(start);
				}
				prepareQuery(paramNames, values, _query);
				return _query.list();
			}
		});
	}

	/**
	 * 执行给定的HQL语句，把符合要求的数据全取出来
	 * @param entityClass
	 * @param queryContent
	 * @param paramNames
	 * @param values
	 * @return
	 */
	public <T extends BaseEntity> List<T> findBySqlQueryAndParamAllT(
			final Class<T> entityClass, final String queryContent, final String[] paramNames, final Object[] values) {
		return findBySqlQueryAndParamT(entityClass,queryContent,paramNames,values,-1,-1);
	}
	public <T extends BaseEntity> List<T> findBySqlQueryAndParamT(final Class<T> entityClass,
			final String queryContent, final String[] paramNames, final Object[] values,
			final int maxResult, final int start)
			throws DataAccessException {
		if (arrayLength(paramNames) != arrayLength(values)) {
			throw new IllegalArgumentException("The paramNames length != values length");
		}
		return transTemplate.doGuestCall(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doCall(Session session) {
				Query _query = session.createQuery(queryContent);
				if (maxResult > -1) {
					_query.setMaxResults(maxResult);
				}
				if (start > -1) {
					_query.setFirstResult(start);
				}
				prepareQuery(paramNames, values, _query);
				return _query.list();
			}
		});
	}

	/**
	 * 直接执行命名的SQL语句
	 * @param queryName	SQL语句名（真正的SQL文在“hibernate_query.xml”这种文件里）
	 * @return
	 */
	public int doNamedSqlQueryForUpdate(final String queryName){
		return transTemplate.doGuestCall(new HibernateCallback<Integer>() {
			@Override
			public Integer doCall(Session session) {
				Query _query = session.getNamedQuery(queryName);
				String sqlStr = _query.getQueryString();
				SQLQuery sqlQuery = session.createSQLQuery(sqlStr);
				return sqlQuery.executeUpdate();
			}
		});
	}

	/**
	 * 直接执行SQL语句
	 * @param queryContent	SQL语句
	 * @return
	 */
	public int doSqlQueryForUpdate(final String queryContent){
		return transTemplate.doGuestCall(new HibernateCallback<Integer>() {
			@Override
			public Integer doCall(Session session) {
				SQLQuery sqlQuery = session.createSQLQuery(queryContent);
				return sqlQuery.executeUpdate();
			}
		});
	}
	
	/**
	 * 取得数组的长度
	 * 
	 * @param arrays
	 * @return
	 */
	private int arrayLength(Object[] arrays) {
		return arrays == null ? -1 : arrays.length;
	}
	
	private void prepareQuery(final String[] paramNames, final Object[] values, Query query) {
		for (int i = 0; paramNames != null && i < paramNames.length; i++) {
			if (values[i] instanceof Collection) {
				query.setParameterList(paramNames[i], (Collection<?>) values[i]);
			} else {
				query.setParameter(paramNames[i], values[i]);
			}
		}
	}
}
