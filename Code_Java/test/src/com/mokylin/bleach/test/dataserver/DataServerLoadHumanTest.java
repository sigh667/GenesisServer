package com.mokylin.bleach.test.dataserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.Props;

import com.mokylin.bleach.core.akka.Akka;
import com.mokylin.bleach.core.akka.config.AkkaConfig;
import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.ISCActorSupervisor;
import com.mokylin.bleach.core.isc.ISCService;
import com.mokylin.bleach.core.isc.RemoteActorManager;
import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.isc.remote.RemoteServerConfig;
import com.mokylin.bleach.core.isc.remote.actorrefs.SingleTargetActorRef;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.dataserver.globals.Globals;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;
import com.mokylin.bleach.gamedb.redis.key.model.HumanKey;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.bleach.servermsg.dataserver.LoadHumanDataMessage;
import com.mokylin.bleach.servermsg.gameserver.HumanDataMsg;
import com.mokylin.bleach.test.dataserver.gamedb.ArenaSnapTable;
import com.mokylin.bleach.test.dataserver.gamedb.HumanTable;
import com.mokylin.bleach.test.dataserver.gamedb.ItemTable;

public class DataServerLoadHumanTest extends AbstractTest {
	
	private static Semaphore sp = new Semaphore(1);

	@Test
	public void load_humandata_should_into_redis() throws InterruptedException, IOException {
		//启动模拟的其他Server
		ServerConfig config = new ServerConfig(ServerType.GAME_SERVER, 1, new AkkaConfig("127.0.0.1", 6666));
		sp.acquire();
		Akka akka = new Akka(config.akkaConfig);
		RemoteActorManager ram = new RemoteActorManager(akka);
		ISCService isc = new ISCService(ram, config);
		ActorRef ar = akka.getActorSystem().actorOf(Props.create(ISCActorSupervisor.class, config, isc, "com.mokylin.bleach.test.dataserver.DataServerLoadHumanTest"));

		//准备数据
		new HumanTable().insertToDB();
		new ArenaSnapTable().insertToDB();
		new ItemTable().insertToDB();
		//启动DataServer
		Globals.init();
		
		ram.connectRemote(new RemoteServerConfig(ServerType.DATA_SERVER, Globals.getServerConfig().serverConfig.serverId, Globals.getServerConfig().serverConfig.akkaConfig.ip, Globals.getServerConfig().serverConfig.akkaConfig.port), ISCActorSupervisor.ACTOR_NAME);
		isc.registerToRemote(ServerType.DATA_SERVER, Globals.getServerConfig().serverConfig.serverId, new SingleTargetActorRef(config.serverType, config.serverId, ar));
		
		Thread.sleep(1*1000);
		
		/**开始测试*/
		HumanKey humanKey = new HumanKey(2001,1L);
		HumanEntity humanEntity = redis.getHashOp().hget(humanKey.getKey(), humanKey.getField(), humanKey.getEntityType()).get();
		assertThat(humanEntity, equalTo(null));
		Handler.humanEntity = dbService.getById(HumanEntity.class, 1L);
		
		LoadHumanDataMessage msg = new LoadHumanDataMessage(1, Handler.humanEntity.getAccountId(), Handler.humanEntity.getChannel(), 1L, 2001);
		isc.getRemote(ServerType.DATA_SERVER, Globals.getServerConfig().serverConfig.serverId).get().sendMessage(msg);
		if(!sp.tryAcquire(500000000, TimeUnit.SECONDS)){
			Assert.fail();
		}
	}
	
	public static class Handler implements IServerMsgFunc<HumanDataMsg, MsgArgs, MsgArgs> {

		public static HumanEntity humanEntity;

		@Override
		public void handle(IRemote remote, HumanDataMsg msg, MsgArgs arg1, MsgArgs arg2) {
			assertThat(msg.humanData.humanEntity.getId(), equalTo(humanEntity.getId()));
			assertThat(msg.humanData.itemEntityList.size(), equalTo(2));
			sp.release();			
		}

		@Override
		public MessageTarget getTarget() {
			return MessageTarget.ISC_ACTOR;
		}
	}
}
