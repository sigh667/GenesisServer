package com.mokylin.bleach.core.serializer;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.UnpooledByteBufAllocator;

import org.apache.commons.lang3.SerializationException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
/**
 * kryo序列化类
 * @author yaguang.xiao
 *
 */
public class KryoSerializer implements ISerializer {
	
	private Kryo kryo = null;
	
	public KryoSerializer(Kryo kryo) {
		this.kryo = kryo;
	}

	@Override
	public byte[] serialize(Object o) {
		ByteBufOutputStream bbos = new ByteBufOutputStream(UnpooledByteBufAllocator.DEFAULT.heapBuffer());
		try(Output output = new Output(bbos)){
			kryo.writeObject(output, o);
			output.flush();
			return bbos.buffer().array();
		}catch(Exception e){
			throw new SerializationException("Serialize Exception!", e);
		}		
	}

	@Override
	public <T> T deserialize(byte[] b, Class<T> type) {
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
