package com.mokylin.bleach.core.isc.remote.actorrefs.annotation;

import com.genesis.protobuf.MessageType.MessageTarget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageAcception {
    MessageTarget value();
}
