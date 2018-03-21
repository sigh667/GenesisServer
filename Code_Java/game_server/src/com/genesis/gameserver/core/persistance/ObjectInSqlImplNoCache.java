package com.genesis.gameserver.core.persistance;

import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.core.persistance.task.PersistanceIntoRedisTask;
import com.genesis.gamedb.orm.EntityWithRedisKey;
import com.genesis.gamedb.persistance.IObjectInSql;
import com.genesis.gamedb.redis.DbOp;
import com.genesis.gamedb.redis.DirtyDataInfo;
import com.genesis.gamedb.redis.key.IRedisKey;
import com.genesis.gameserver.core.persistance.model.DirtyData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GameServer中的需要持久化的类的基类<p>
 * 如果要持久化的对象需要在每次改动之后立即存库，那么请继承本类<p>
 * 本类会在一定时间内缓冲需要保存的数据，然后一次性提交到Redis线程
 * <p>由于java不能多继承，如果你的类还需要继承别的类，那么你可以仿照本类自行实现IObjectInSql接口
 * @author Joey
 *
 * @param <IdType>
 * @param <T>
 */
public abstract class ObjectInSqlImplNoCache<IdType extends Serializable, T extends EntityWithRedisKey<?>>
        implements IObjectInSql<IdType, T> {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(ObjectInSqlImplNoCache.class);

    private ServerGlobals sGlobals;

    public ObjectInSqlImplNoCache(ServerGlobals sGlobals) {
        this.sGlobals = sGlobals;
    }

    @Override
    public void setModified() {
        save(DbOp.UPDATE);
    }

    @Override
    public void onDelete() {
        save(DbOp.DELETE);
    }

    private void save(DbOp dbOp) {
        DirtyData dirtyData = new DirtyData();

        try {
            EntityWithRedisKey<?> entity = this.toEntity();
            dirtyData.setEntity(entity);
        } catch (Exception e) {
            logger.error("ObjectInSqlImplNoCache.setModified() toEntity() throw Exception!", e);
            return;
        }

        DirtyDataInfo dirtyDataInfo = new DirtyDataInfo();
        dirtyDataInfo.setOperateType(dbOp);

        IRedisKey<? extends Serializable, ?> redisKey = null;
        try {
            redisKey = (IRedisKey<? extends Serializable, ?>) dirtyData.getEntity()
                    .newRedisKey(sGlobals.getServerId());
        } catch (Exception e) {
            logger.error("ObjectInSqlImplNoCache.setModified() newRedisKey() throw Exception!", e);
            return;
        }
        dirtyDataInfo.setRedisKey(redisKey);
        dirtyData.setDirtyDataInfo(dirtyDataInfo);

        List<DirtyData> dirtyList = new ArrayList<>();
        PersistanceIntoRedisTask task =
                new PersistanceIntoRedisTask(sGlobals.getServerId(), dirtyList,
                        sGlobals.getRedis());
        sGlobals.getRedisProcessUnit().submitTask(task);
    }

}
