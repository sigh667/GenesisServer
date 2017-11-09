package com.bleach.test.gameserver

import java.util.ArrayList
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import com.bleach.test.common.ActorTest
import com.mokylin.bleach.core.config.ServerConfig
import com.mokylin.bleach.core.isc.ServerType
import com.mokylin.bleach.core.redis.RedisService
import com.mokylin.bleach.gameserver.core.global.Globals
import com.mokylin.bleach.gameserver.server.ServerManagerActor
import com.mokylin.bleach.servermsg.gameserver.server.StartNewServer
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.TestKit
import org.scalatest.junit.JUnitRunner
import com.mokylin.bleach.core.akka.config.AkkaConfig

@PrepareForTest(Array(classOf[Globals]))
class ServerManagerActorTest extends TestKit(ActorSystem("System")) with ActorTest{
  
 
  @Rule val powerMockRule = new PowerMockRule
 
  PowerMockito.mockStatic(classOf[Globals])
  val g = mock[Globals]
  Mockito.when(Globals.redisService).thenReturn(new RedisService)
  
  val serverManager = system.actorOf(Props(new ServerManagerActor(new ArrayList)), "ServerManager")
  
  "ServerManagerActor" should "start a new server actor when it receives a valid StartNewServer message" in{
    serverManager.tell(new StartNewServer(new ServerConfig(ServerType.GAME_SERVER, 1, new AkkaConfig("127.0.0.1", 3306))), testActor)
    expectMsgPF(){
      case succeed : StartNewServer.Succeed => succeed.newServer.path.toString mustBe "akka://System/user/ServerManager/1"
      case otherReply => fail("Start New Server Failed")
    }
  }
  
  "ServerManagerActor" should "reply a fail message when the ServerConfig which in the StartNewServer message is null" in {
    serverManager.tell(new StartNewServer(null), testActor)
    expectMsgPF(){
      case failed : StartNewServer.Failed => failed.msg mustBe "ServerManagerActor can not start new server with null config"
      case otherReply => fail("ServerManagerActor did not failed to start a new server")
    }
  }
  
  "ServerManagerActor" should "reply a fail message when ServerType in ServerConfig is not GameServer" in {
    serverManager.tell(new StartNewServer(new ServerConfig(ServerType.DATA_SERVER, 2, new AkkaConfig("127.0.0.1", 3306))), testActor)
    expectMsgPF(){
      case failed : StartNewServer.Failed => failed.msg mustBe "ServerManagerActor can not start new server which is not game server"
      case otherReply => fail("ServerManagerActor did not failed to start a new server")
    }
  }
}