package com.mokylin.bleach.gameserver.core.actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.actor.SupervisorStrategy.Directive;
import akka.japi.Function;

/**
 * 将发生Exception时的监管策略设置为stop。<p>
 * 
 * @author pangchong
 *
 */
public abstract class StopSupervisorStrategyActor extends UntypedActor {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public SupervisorStrategy supervisorStrategy(){
		return new OneForOneStrategy(SupervisorStrategy.makeDecider(new Function<Throwable, SupervisorStrategy.Directive>() {			
			@Override
			public Directive apply(Throwable e) throws Exception {
				log.error("Actor {} error arised.", StopSupervisorStrategyActor.this.getSelf().path().toString(), e);
				if(e instanceof Exception) return SupervisorStrategy.stop();
				return SupervisorStrategy.escalate();
			}
		}));
	}
}
