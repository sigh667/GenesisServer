package com.mokylin.bleach.core.isc;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

import com.google.common.base.Optional;
import com.mokylin.bleach.core.isc.executor.BindServerActorExecutorPool;
import com.mokylin.bleach.core.isc.executor.IActorExecutorPool;
import com.mokylin.bleach.core.isc.msg.ActorRefMessage;
import com.mokylin.bleach.core.isc.msg.Register;
import com.mokylin.bleach.core.isc.msg.ServerMessage;
import com.mokylin.bleach.core.isc.remote.DefaultRemoteFactory;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.isc.remote.IRemoteFactory;
import com.mokylin.bleach.core.isc.remote.actorrefs.annotation.MessageAcception;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.server.ServerMsgFunctionService;
import com.mokylin.bleach.core.msgfunc.server.ServerMsgFunctionUtil;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

@MessageAcception(MessageTarget.ISC_ACTOR)
class ISCServerActor extends UntypedActor {
	
	public final static String ACTOR_NAME = "ISCServerActor";
	
	private final Logger log = LoggerFactory.getLogger(ISCServerActor.class); 

	private ISCService iscService = null;
	
	private IActorExecutorPool executors = null;
	
	private IRemoteFactory remoteFactory = null;
	
	private ServerMsgFunctionService sMsgFuncs = null;
	
	public ISCServerActor(ISCService iscService, String serverMsgFuncsPackage){
		this(iscService, serverMsgFuncsPackage, new DefaultRemoteFactory());
	} 
	
	public ISCServerActor(ISCService iscService, String serverMsgFuncsPackage, IRemoteFactory remoteFactory){
		this(iscService, serverMsgFuncsPackage, remoteFactory, new BindServerActorExecutorPool());
	}
	
	public ISCServerActor(ISCService iscService, String serverMsgFuncsPackage, IRemoteFactory remoteFactory, int defaultThreadNum){
		this(iscService, serverMsgFuncsPackage, remoteFactory, new BindServerActorExecutorPool(defaultThreadNum));
	}
	
	public ISCServerActor(ISCService iscService, String serverMsgFuncsPackage, IRemoteFactory remoteFactory, IActorExecutorPool executorPool){
		this.iscService = checkNotNull(iscService, "ISCServerActor can not work with null isc service");
		this.executors = checkNotNull(executorPool, "ISCServerActor can not work with null executor");
		this.remoteFactory = checkNotNull(remoteFactory, "ISCServerActor can not work with null remote factory");
		this.sMsgFuncs = ServerMsgFunctionUtil.buildMsgFuncs(serverMsgFuncsPackage);
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg instanceof ServerMessage){
			final ServerMessage sMsg = (ServerMessage) msg;
			Executor executor = executors.select(sMsg.sType, sMsg.sId);
			executor.execute(new MsgWorker(sMsg.sType, sMsg.sId, sMsg.msg));
		}else if(msg instanceof Register){
			Register rMsg = (Register) msg;
			iscService.addRemote(rMsg.sourceServerType, rMsg.sourceServerId, remoteFactory.createRemote(iscService.getLocalConfig(), rMsg.localActorPackages));
		}else if(msg instanceof ActorRefMessage){
			final ActorRefMessage aMsg = (ActorRefMessage) msg;
			Executor executor = executors.select(aMsg.sourceServerType, aMsg.sourceServerId);
			executor.execute(new MsgWorker(aMsg.sourceServerType, aMsg.sourceServerId, aMsg.msg));
		}else{
			this.unhandled(msg);
		}
	}
	
	private class MsgWorker implements Runnable{
		
		final ServerType sType;
		final int sId;
		final Object msg;
		
		private MsgWorker(ServerType sType, int sId, Object msg){
			this.sType = sType;
			this.sId = sId;
			this.msg = msg;
		}

		@Override
		public void run() {
			Optional<IRemote> option = iscService.getRemote(sType, sId);
			if(option.isPresent()){
				sMsgFuncs.handle(MessageTarget.ISC_ACTOR, option.get(), msg, MsgArgs.nullArgs, MsgArgs.nullArgs);
			}else{
				log.warn("Recevied a message from unrecognised server: type {}, id {}", sType, sId);
			}
		}
		
	}
}
