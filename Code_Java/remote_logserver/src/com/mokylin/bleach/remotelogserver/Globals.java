package com.mokylin.bleach.remotelogserver;

import com.genesis.core.akka.Akka;
import com.genesis.core.akka.config.AkkaConfig;
import com.genesis.core.config.ConfigBuilder;
import com.genesis.core.config.ServerConfig;
import com.genesis.core.isc.ISCActorSupervisor;
import com.genesis.core.isc.ISCService;
import com.genesis.core.isc.RemoteActorManager;
import com.genesis.core.isc.ServerType;
import com.genesis.core.isc.remote.DefaultRemoteFactory;
import com.mokylin.bleach.remotelogserver.disruptor.LogManager;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import akka.actor.Props;
import scribe.thrift.scribe;

public class Globals {

    private static final ExecutorService mainThread =
            Executors.newSingleThreadExecutor(new ThreadFactory() {

                @Override
                public Thread newThread(Runnable r) {
                    Thread th = new Thread(r);
                    th.setName("main thread");
                    return th;
                }

            });
    private static RemoteLogServerConfig config;
    private static RemoteLogServerLogicConfig logicConfig;
    private static ServerConfig serverConfig;
    private static Akka akka = null;
    private static ISCService iscService = null;
    private static TServer server;

    public static void init() throws TTransportException {
        buildConfig();

        akka = new Akka(serverConfig.akkaConfig);
        iscService = new ISCService(new RemoteActorManager(akka), serverConfig);

        scribe.Processor<LogHandler> processor = new scribe.Processor<LogHandler>(new LogHandler(
                new LogManager(logicConfig.getThreadNum(), config.getLogPath(),
                        config.getGameName(), config.getLogLifeTime())));

        TNonblockingServerTransport trans = new TNonblockingServerSocket(config.getPort());
        TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(trans);
        args.transportFactory(new TFramedTransport.Factory());
        args.protocolFactory(new TBinaryProtocol.Factory());
        args.processor(processor);
        args.selectorThreads(4);
        args.workerThreads(32);
        server = new TThreadedSelectorServer(args);

        System.out.println("Starting the simple server...");
        server.serve();

        Globals.getAkka().getActorSystem().actorOf(
                Props.create(ISCActorSupervisor.class, serverConfig, iscService,
                        "com.mokylin.bleach.remotelogserver", new DefaultRemoteFactory()),
                ISCActorSupervisor.ACTOR_NAME);
    }

    private static void buildConfig() {
        config = ConfigBuilder
                .buildConfigFromFileName("remotelogserver.conf", RemoteLogServerConfig.class);
        logicConfig = ConfigBuilder
                .buildConfigFromFileName("logicConfig.conf", RemoteLogServerLogicConfig.class);

        AkkaConfig akkaConfig = new AkkaConfig(config.getIp(), config.getAkkaPort());
        serverConfig = new ServerConfig(ServerType.LOG, config.getServerId(), akkaConfig);
    }

    public static void shutdown() {
        server.stop();
        akka.getActorSystem().terminate();
    }

    public static RemoteLogServerConfig getConfig() {
        return config;
    }

    public static Akka getAkka() {
        return akka;
    }

    public static ISCService getIscService() {
        return iscService;
    }

    public static ExecutorService getMainthread() {
        return mainThread;
    }
}
