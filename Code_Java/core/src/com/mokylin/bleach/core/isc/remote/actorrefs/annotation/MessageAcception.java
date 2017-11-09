package com.mokylin.bleach.core.isc.remote.actorrefs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageAcception {
	MessageTarget value();
}
