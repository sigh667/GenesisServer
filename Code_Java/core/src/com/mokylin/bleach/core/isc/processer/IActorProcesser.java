package com.mokylin.bleach.core.isc.processer;

import com.mokylin.bleach.core.isc.executor.IActorExecutorPool;
import com.mokylin.bleach.core.isc.remote.IRemote;

/**
 * 消息处理器的接口。<p>
 * 
 * 服务器消息接收后的处理是一个链式过程。类似于:<br>
 * ProcesserA -> ProcesserB -> ... -> ProcesserN<br>
 * 每一个Processer都是该接口的一个实现，注意这些实现在整个服务器启动后的生命周期中是以单例的方式存在的。因此在实现该接口并将
 * 实现加入到一个服务器通信中去时，有必要考虑共享数据的多线程问题。ISC组件能够保证对于同一个来源的消息必定是顺序处理的，但是
 * 对于不同来源的消息，ISC不会保证其处理的顺序。具体参照{@link IActorExecutorPool}。<p>
 * 
 * @author pangchong
 *
 */
public interface IActorProcesser {

	ProcessResult onReceived(IRemote sender, Object msg);
}
