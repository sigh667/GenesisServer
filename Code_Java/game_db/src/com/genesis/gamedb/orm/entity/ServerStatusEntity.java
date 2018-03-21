package com.genesis.gamedb.orm.entity;

import com.genesis.gamedb.orm.EntityWithRedisKey;
import com.genesis.gamedb.redis.key.model.ServerStatusKey;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_server_status")
public class ServerStatusEntity implements EntityWithRedisKey<ServerStatusKey> {

    private static final long serialVersionUID = 1L;

    private int serverId;

    /** 服务器开服的时间 */
    private Timestamp serverOpenTime;

    @Override
    public ServerStatusKey newRedisKey(Integer serverId) {
        return new ServerStatusKey(this.serverId);
    }

    @Id
    @Column
    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Column
    public Timestamp getServerOpenTime() {
        return serverOpenTime;
    }

    public void setServerOpenTime(Timestamp serverOpenTime) {
        this.serverOpenTime = serverOpenTime;
    }

}
