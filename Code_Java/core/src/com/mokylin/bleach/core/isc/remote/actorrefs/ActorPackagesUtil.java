package com.mokylin.bleach.core.isc.remote.actorrefs;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import akka.actor.UntypedActor;

import com.google.common.collect.ImmutableMap;
import com.mokylin.bleach.core.isc.remote.actorrefs.annotation.MessageAcception;
import com.mokylin.bleach.core.isc.remote.actorrefs.exception.MessageTargetDuplicatedException;
import com.mokylin.bleach.core.util.PackageUtil;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public class ActorPackagesUtil {

	@SuppressWarnings("unchecked")
	public static ImmutableMap<MessageTarget, Class<? extends UntypedActor>> buildTargetClassMap(String packageName, boolean useCorePackage) {
		checkArgument(packageName!=null && !packageName.isEmpty(), "ActorPackagesUtil can not build with null packages!");
		
		Set<Class<?>> actorClasses = PackageUtil.getSubClass(packageName, UntypedActor.class);
		if(useCorePackage){
			actorClasses.addAll(PackageUtil.getSubClass("com.mokylin.bleach.core", UntypedActor.class));
		}
		
		Map<MessageTarget, Class<? extends UntypedActor>> map = new HashMap<>();
		for(Class<?> each : actorClasses){
			MessageAcception annotation = each.getAnnotation(MessageAcception.class);
			if(annotation == null) continue;
			MessageTarget target = annotation.value();
			if(map.containsKey(target)) throw new MessageTargetDuplicatedException("MessageTarge [" + target.name() + "] maps to multi actors!");
			map.put(target, (Class<? extends UntypedActor>) each);
		}
		return ImmutableMap.copyOf(map);
	}

}
