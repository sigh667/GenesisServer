package com.mokylin.bleach.gameserver.core.actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorInitializationException;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.actor.SupervisorStrategy.Directive;
import akka.japi.Function;

/**
 * 将发生Exception时的监管策略设置为resume。<p>
 * 
 * 首先，我们将Exception及其子类所引发的异常称之为可预测的异常，简称异常；将除Exception
 * 及其子类之外的Throwable称为错误。以下的描述中分别使用异常和错误来区别二者。<p>
 * 
 * 在子Actor启动的时候，如果出现了任何异常，都表示子Actor无法启动，此时要向上扩散启动异常，
 * 由上一层的Supervisor来决定。<p>
 * 
 * 在游戏服务器中，大部分情况下子系统发生了异常都不应该将其重启或者停止，比如公会系统、
 * 场景系统、竞技场系统等；这些系统在某些逻辑发生了异常之后应该继续执行，仅需要将异常
 * 记录到日志中即可。<p>
 * 
 * 但是，当子系统出现了错误时，则说明在系统存在了无法挽回的问题，这种错误一旦发生，我们
 * 无法让服务器继续运行下去，因为我们无法预测是否会引发更大的问题。此时将整个ActorSystem
 * 关闭可能是最好的选择。
 * 
 * @author pangchong
 */
public abstract class ResumeSupervisorStrategyActor extends UntypedActor {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public SupervisorStrategy supervisorStrategy(){
		return new OneForOneStrategy(SupervisorStrategy.makeDecider(new Function<Throwable, SupervisorStrategy.Directive>() {			
			@Override
			public Directive apply(Throwable e) throws Exception {
				log.error("Actor {} error arised.", ResumeSupervisorStrategyActor.this.getSelf().path().toString(), e);
				if(e instanceof ActorInitializationException) return SupervisorStrategy.escalate();
				if(e instanceof Exception) return SupervisorStrategy.resume();
				return SupervisorStrategy.escalate();
			}
		}));
	}
}
