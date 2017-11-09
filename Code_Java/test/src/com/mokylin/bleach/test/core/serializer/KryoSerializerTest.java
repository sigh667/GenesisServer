package com.mokylin.bleach.test.core.serializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.SerializationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mokylin.bleach.core.util.PackageUtil;
import com.mokylin.bleach.test.core.serializer.mockentity.Message;
import com.mokylin.bleach.test.core.serializer.mockentity.Message1;
import com.mokylin.bleach.test.core.serializer.mockentity.Message2;
import com.mokylin.bleach.test.core.serializer.mockentity.Message3;
import com.mokylin.bleach.test.core.serializer.mockentity.FinalMessage;
import com.mokylin.bleach.test.core.serializer.mockentity.ListMessage;

public class KryoSerializerTest {

	@Test
	public void print_the_speed_information_of_kryo_register() {
		Set<Class<?>> classes = PackageUtil.getPackageClasses("com.mokylin.bleach", new Predicate<Class<?>>() {

			@Override
			public boolean apply(Class<?> input) {
				if(Enum.class.isAssignableFrom(input)) {
					return false;
				}
				
				return true;
			}
			
		});
		
		Kryo kryo = new Kryo();
		long startTime = System.currentTimeMillis();
		for(Class<?> clazz : classes) {
			kryo.register(clazz);
		}
		long spendTime = System.currentTimeMillis() - startTime;
		System.out.println("register " + classes.size() + " classes need " + spendTime + " milliseconds.");
		System.out.println(Set.class.getName());
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void the_objects_should_be_serialize_and_deserialize_uncorrectly() {
		
		thrown.expect(SerializationException.class);
		thrown.expectMessage("Deserialize Exception!");
		
		Kryo kryo1 = new Kryo();
		kryo1.register(Message1.class);
		kryo1.register(Message2.class);
		kryo1.register(Message3.class);
		
		Message1 e1 = new Message1();
		e1.setId(1);
		e1.setId2(2);
		e1.setId3(3);
		Message2 e2 = new Message2();
		e2.setC1("11");
		e2.setC2("22");
		e2.setC3("33");
		e2.setC4("44");
		Message3 e3 = new Message3();
		e3.setE1(e1);
		e3.setE2(e2);
		byte[] e3Bytes = serialize(kryo1, e3);
		
		Kryo kryo2 = new Kryo();
		kryo2.register(Message3.class);
		kryo2.register(Message2.class);
		kryo2.register(Message1.class);
		
		Message3 e33 = deserialize(kryo2, e3Bytes, Message3.class);
	}
	
	@Test
	public void the_objects_should_be_serialize_and_deserialize_correctly() {
		Kryo kryo = new Kryo();
		((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
		
		kryo.register(FinalMessage.class);
		
		FinalMessage e4 = new FinalMessage(1, "111");
		byte[] e4Bytes = serialize(kryo, e4);
		FinalMessage e44 = deserialize(kryo, e4Bytes, FinalMessage.class);
		
		assertThat(e44.id, is(1));
		assertThat(e44.c, is("111"));
	}
	
	@Test
	public void the_null_object_should_be_serialize_and_deserialize_correctly() {
		Kryo kryo = new Kryo();
		((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
		
		kryo.register(Message1.class);
		kryo.register(Message2.class);
		kryo.register(Message3.class);
		
		Message1 e1 = new Message1();
		e1.setId(1);
		e1.setId2(2);
		e1.setId3(3);
		Message2 e2 = new Message2();
		e2.setC1("11");
		e2.setC2("22");
		e2.setC3("33");
		e2.setC4("44");
		Message3 e3 = new Message3();
		e3.setE1(e1);
		e3.setE2(null);
		
		byte[] e3Bytes = serialize(kryo, e3);
		Message3 e33 = deserialize(kryo, e3Bytes, Message3.class);
		
		assertThat(e33.getE1().getId(), is(1));
		if(e33.getE2() != null) {
			throw new RuntimeException("e33.getE2() should return null.");
		}
	}
	
	@Test
	public void the_list_object_should_be_serialize_and_deserialize_correctly() {
		Kryo kryo = new Kryo();
		((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
		
		kryo.register(Message.class);
		kryo.register(ListMessage.class);
		
		List<Message> eList = Lists.newLinkedList();
		eList.add(new Message(1, "111"));
		eList.add(new Message(2, "死神"));
		eList.add(new Message(3, "しにがみ"));
		ListMessage listEntity = new ListMessage(eList);
		
		byte[] listeBytes = serialize(kryo, listEntity);
		ListMessage deListEntity = deserialize(kryo, listeBytes, ListMessage.class);
		List<Message> deEList = deListEntity.entities;
		
		assertThat(deEList.size(), is(3));
		
		assertThat(deEList.get(0).id, is(1));
		assertThat(deEList.get(0).c, is("111"));
		
		assertThat(deEList.get(1).id, is(2));
		assertThat(deEList.get(1).c, is("死神"));
		
		assertThat(deEList.get(2).id, is(3));
		assertThat(deEList.get(2).c, is("しにがみ"));
		
	}
	
	private static byte[] serialize(Kryo kryo, Object o) {
		ByteBufOutputStream bbos = new ByteBufOutputStream(UnpooledByteBufAllocator.DEFAULT.heapBuffer());
		try(Output output = new Output(bbos)){
			kryo.writeObject(output, o);
			return bbos.buffer().array();
		}catch(Exception e){
			throw new SerializationException("Serialize Exception!", e);
		}		
	}
	
	private static <T> T deserialize(Kryo kryo, byte[] b, Class<T> type) {
		if(b == null) return null;
		ByteBufInputStream bbis = new ByteBufInputStream(UnpooledByteBufAllocator.DEFAULT.heapBuffer().writeBytes(b));
		try(Input input = new Input(bbis)){
			T obj = kryo.readObject(input, type);
			return obj;
		}catch(Exception e){
			throw new SerializationException("Deserialize Exception!", e);
		}
	}

}
