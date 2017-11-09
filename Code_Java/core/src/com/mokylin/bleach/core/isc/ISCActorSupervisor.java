package com.mokylin.bleach.core.isc;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorInitializationException;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;

import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.msg.RegisterToRemote;
import com.mokylin.bleach.core.isc.msg.RegisterToRemoteResult;
import com.mokylin.bleach.core.isc.remote.DefaultRemoteFactory;
import com.mokylin.bleach.core.isc.remote.IRemoteFactory;
import com.mokylin.bleach.core.isc.remote.RemoteConnectFunc;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;
import com.mokylin.bleach.core.isc.remote.actorrefs.SingleTargetActorRef;

/**
 * 通用服务器Actor的监管类、父类。<p>
 * 
 * 该Actor对于监管的Actor的策略是resume。
 * 
 * @author pangchong
 *
 */
public class ISCActorSupervisor extends UntypedActor {
	
	public final static String ACTOR_NAME = "ISCActorSupervisor";
	
	private final Logger log = LoggerFactory.getLogger(ISCActorSupervisor.class);
	
	/** 本地服务器配置 */
	private final ServerConfig localConfig;
	
	private final ISCService iscService;
	
	/** 发送给远程的ActorRef的组装类 */
	private final IActorPackages localActorPackage;
	
	public ISCActorSupervisor(ServerConfig localConfig, ISCService iscService, String serverMsgFuncsPackage){
		this(localConfig, iscService, serverMsgFuncsPackage, new DefaultRemoteFactory());
	}
	
	public ISCActorSupervisor(ServerConfig localConfig, ISCService iscService, String serverMsgFuncsPackage, IRemoteFactory remoteFactory){
		this(localConfig, iscService, serverMsgFuncsPackage, remoteFactory, 1);
	}
	
	public ISCActorSupervisor(ServerConfig localConfig, ISCService iscService, String serverMsgFuncsPackage, IRemoteFactory remoteFactory, int defaultThreadNum){
		this.localConfig = checkNotNull(localConfig, "ISCActorSupervisor can not init with null server config!");
		this.iscService = checkNotNull(iscService, "ISCActorSupervisor can not init with null ISCService!");
		localActorPackage = new SingleTargetActorRef(this.localConfig.serverType, this.localConfig.serverId, 
				getContext().actorOf(Props.create(ISCServerActor.class, this.iscService, serverMsgFuncsPackage, remoteFactory, defaultThreadNum), ISCServerActor.ACTOR_NAME));
	}
	
	public SupervisorStrategy supervisorStrategy(){
		return new OneForOneStrategy(SupervisorStrategy.makeDecider(new Function<Throwable, SupervisorStrategy.Directive>() {			
			@Override
			public Directive apply(Throwable e) throws Exception {
				log.error("Actor {} error arised.", ISCActorSupervisor.this.getSelf().path().toString(), e);
				if(e instanceof ActorInitializationException) return SupervisorStrategy.escalate();
				if(e instanceof Exception) return SupervisorStrategy.resume();
				return SupervisorStrategy.escalate();
			}
		}));
	}
	
	private RemoteConnectFunc func = new RemoteConnectFunc() {
		@Override
		public IActorPackages assembleActorRefs() {
			return localActorPackage;
		}
	};

	@Override
	public void onReceive(Object msg) throws Exception {
		if(func.apply(msg, getSender())){
			return;
		}else if(msg instanceof RegisterToRemote) {
			RegisterToRemote rtr = (RegisterToRemote)msg;
			if(iscService.registerToRemote(rtr.serverType, rtr.serverId, localActorPackage)) {
				getSender().tell(new RegisterToRemoteResult(true), ActorRef.noSender());
			}else {
				getSender().tell(new RegisterToRemoteResult(false), ActorRef.noSender());
			}
		}else{
			this.unhandled(msg);
		}
	}
}