package com.mokylin.bleach.core.isc;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mokylin.bleach.core.akka.Akka;
import com.mokylin.bleach.core.isc.annotation.ISCSync;
import com.mokylin.bleach.core.isc.msg.Connect;
import com.mokylin.bleach.core.isc.msg.ConnectResult;
import com.mokylin.bleach.core.isc.remote.RemoteServerConfig;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;

/**
 * 服务器通信服务类。
 * 
 * @author pangchong
 *
 */
public class RemoteActorManager {
	
	private Logger log = LoggerFactory.getLogger(RemoteActorManager.class);

	private Akka akka = null;
	
	private Table<ServerType, Integer, IActorPackages> remoteActorPackages;
	
	public RemoteActorManager(Akka akka){
		this.akka = akka;
		remoteActorPackages = HashBasedTable.create();
	}
	
	@ISCSync
	public boolean connectRemote(final RemoteServerConfig remoteConfig, String remoteActorPath){
		checkNotNull(remoteConfig, "RemoteActorManager can not connect to a null remote config!");
		checkArgument(remoteActorPath!=null && !remoteActorPath.isEmpty(), "RemoteActorManager can not connect to a illegal actor path!");
		
		IActorPackages rs = remoteActorPackages.get(remoteConfig.serverType, remoteConfig.serverId);
		if(rs != null) return true;
		
		String selectionPath = this.buildServerActorPath(remoteConfig.host, remoteConfig.port, remoteActorPath);
		Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));
		
		try {
			Future<Object> future = Patterns.ask(akka.getActorSystem().actorSelection(selectionPath), Connect.INSTANCE, timeout);
			ConnectResult result = (ConnectResult)Await.result(future, timeout.duration());
			if(result == null || result.actorPackages == null) throw new RuntimeException("RemoteActorManager can not connect to remote cause remote replys null!");
			remoteActorPackages.put(remoteConfig.serverType, remoteConfig.serverId, result.actorPackages);
		} catch (Exception e) {
			log.error("Akka biRegisterSync arise exception:", e);
			return false;
		}
		return true;
	}
	
	public Optional<IActorPackages> getRemoteActorPackage(ServerType sType, int sId){
		return Optional.fromNullable(remoteActorPackages.get(sType, sId));
	}

	
	private String buildServerActorPath(String host, int port, String remoteActorPath) {
		StringBuilder path = new StringBuilder();
		path.append("akka.tcp://");
		path.append(Akka.ACTOR_SYSTEM_NAME);
		path.append("@");
		path.append(host);
		path.append(":");
		path.append(port);
		path.append("/");
		path.append("user/");
		path.append(remoteActorPath);
		return path.toString();
	}
	
	Optional<IActorPackages> getRemoteActorRef(ServerType sType, int sId){
		return Optional.fromNullable(remoteActorPackages.get(sType, sId));
	}
}
