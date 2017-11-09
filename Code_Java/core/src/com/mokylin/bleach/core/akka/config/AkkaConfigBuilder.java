package com.mokylin.bleach.core.akka.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.google.common.base.Charsets;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Contains all method that build configuration object for akka.
 * 
 * @author yaguang.xiao
 *
 */

public class AkkaConfigBuilder {
	
	/**
	 * The configuration file that enable akka's remote ability.
	 */
	private static final String AKKA_REMOTE_CONF = "akka-remote.conf";
	/**
	 * The content in the akka-remote.conf file.
	 */
	private static String REMOTE_CONTENT;
	
	// read the content of the configuration file.
	static {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream in = classLoader.getResourceAsStream(AKKA_REMOTE_CONF);
			REMOTE_CONTENT = IOUtils.readAndClose(in, Charsets.UTF_8.name());
		} catch (IOException e) {
			// throw the unchecked exception.
			throw new RuntimeException("Read the akka configuration file fail!", e);
		}
	}
	
	/**
	 * Build a configuration object based on akka configuration.
	 * @param appName ActorSystem's name.
	 * @param host The hostName of the ActorSystem.
	 * @param port The port of the ActorSystem.
	 * @return Configuration object based on akka configuration.
	 */
	public static Config build(String host, int port) {
		VelocityContext context = new VelocityContext();
		context.put("hostname", host);
		context.put("port", port);
		
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "AkkaRemoteConf", REMOTE_CONTENT);
		return ConfigFactory.parseString(writer.toString());
	}
}
