package com.mokylin.td.clientmsg.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.NotSupportedException;

import com.mokylin.bleach.core.collection.BitArray;
import com.mokylin.td.clientmsg.commondata.RoleProto;


/**
 * 消息序列号工具类
 * <p>某些基础数据类型（比如无符号long等），Java中是没有的，因此无法支持
 * 
 * @author baoliang.shen
 *
 */
public class SerializationHelper {

	private enum AMF3SerializationDefine
	{
		Undefined,
		Null,
		BooleanFalse,
		BooleanTrue,
		Int,
		Number,
		String,
		XMLDoc,
		Date,
		Array,
		Object,
		XML,
		ByteArray,
		VectorInt,
		VectorUint,
		VectorNumber,
		VectorObject,
		Dictionary;
		
		static AMF3SerializationDefine valueOf(int value) {
			for (AMF3SerializationDefine type : AMF3SerializationDefine.values()) {
				if (type.ordinal() == value) {
					return type;
				}
			}
			return null;
		}
	};

	public static final int minU29Int = (int) -Math.pow(2, 28);
	public static final int maxU29Int = (int)Math.pow(2, 28) - 1;
	
	private static final Map<Class<?>, AMF3SerializationDefine> _typeExchange = new HashMap<>();
	static {
		_typeExchange.put(Undefined.class, AMF3SerializationDefine.Undefined);
//        _typeExchange.TryAdd(typeof(Nullable), AMF3SerializationDefine.Null);
//        _typeExchange.TryAdd(typeof(bool), AMF3SerializationDefine.BooleanFalse);
//        _typeExchange.TryAdd(typeof(byte), AMF3SerializationDefine.Int);
//        _typeExchange.TryAdd(typeof(sbyte), AMF3SerializationDefine.Int);
//        _typeExchange.TryAdd(typeof(short), AMF3SerializationDefine.Int);
//        _typeExchange.TryAdd(typeof(ushort), AMF3SerializationDefine.Int);
//        _typeExchange.TryAdd(typeof(int), AMF3SerializationDefine.Int);
//        _typeExchange.TryAdd(typeof(double), AMF3SerializationDefine.Number);
//        _typeExchange.TryAdd(typeof(float), AMF3SerializationDefine.Number);
//        _typeExchange.TryAdd(typeof(string), AMF3SerializationDefine.String);
//        _typeExchange.TryAdd(typeof(XmlDocument), AMF3SerializationDefine.XML);
//        _typeExchange.TryAdd(typeof(DateTime), AMF3SerializationDefine.Date);
//        _typeExchange.TryAdd(typeof(ArrayList), AMF3SerializationDefine.Array);
//        _typeExchange.TryAdd(typeof(IASObjectDefinition), AMF3SerializationDefine.Object);
//		//_typeExchange.TryAdd(typeof(XmlNode), AMF3SerializationDefine.XML);
//		_typeExchange.TryAdd(typeof(ByteArray), AMF3SerializationDefine.ByteArray);
//        _typeExchange.TryAdd(typeof(List<int>), AMF3SerializationDefine.VectorInt);
//        _typeExchange.TryAdd(typeof(List<uint>), AMF3SerializationDefine.VectorUint);
//        _typeExchange.TryAdd(typeof(List<double>), AMF3SerializationDefine.VectorNumber);
//        _typeExchange.TryAdd(typeof(List<float>), AMF3SerializationDefine.VectorNumber);
//        _typeExchange.TryAdd(typeof(List<object>), AMF3SerializationDefine.VectorObject);
//        _typeExchange.TryAdd(typeof(Hashtable), AMF3SerializationDefine.Dictionary);
	}
	//以下方法全用static即可

	public static boolean readBoolean(ByteBuf __bytes)
	{
		return __bytes.readBoolean();
	}
	public static void writeBoolean(ByteBuf __bytes, boolean __value)
	{
		__bytes.writeBoolean(__value);
	}

	public static byte readByte(ByteBuf __bytes)
	{
		return __bytes.readByte();
	}
	public static void writeByte(ByteBuf __bytes, byte __value)
	{
		__bytes.writeByte(__value);
	}
	public static short readUnsignedByte(ByteBuf __bytes)
	{
		return __bytes.readUnsignedByte();
	}
	//    public static void writeUnsignedByte(ByteBuf __bytes, sbyte __value)
	//    {
	//    	__bytes.writeByte(__value);
	//    }

	public static short readShort(ByteBuf __bytes)
	{
		return __bytes.readShort();
	}
	public static void writeShort(ByteBuf __bytes, short __value)
	{
		__bytes.writeShort(__value);
	}

	public static int readUnsignedShort(ByteBuf __bytes)
	{
		return __bytes.readUnsignedShort();
	}
	public static void writeUnsignedShort(ByteBuf __bytes, int __value)
	{
		__bytes.writeChar(__value);
	}

	/**
	 * 读取可变宽度int
	 * @param __bytes
	 * @return 支持的数值范围：-(2^28次方) 到 2^28次方-1 之间
	 */
	public static int readU29Int(ByteBuf __bytes)
	{
		int acc = __bytes.readUnsignedByte();
		int tmp;
		if (acc < 128)
			return acc;
		else
		{
			acc = (acc & 0x7f) << 7;
			tmp = __bytes.readUnsignedByte();
			if (tmp < 128)
				acc = acc | tmp;
			else
			{
				acc = (acc | tmp & 0x7f) << 7;
				tmp = __bytes.readUnsignedByte();
				if (tmp < 128)
					acc = acc | tmp;
				else
				{
					acc = (acc | tmp & 0x7f) << 8;
					tmp = __bytes.readUnsignedByte();
					acc = acc | tmp;
				}
			}
		}
		//To sign extend a value from some number of bits to a greater number of bits just copy the sign bit into all the additional bits in the new format.
		//convert/sign extend the 29bit two's complement number to 32 bit
		int mask = 1 << 28; // mask
		int r = -(acc & mask) | acc;
		return r;
	}
	/**
	 * 写入可变宽度int
	 * @param __bytes
	 * @param value	支持的数值范围：-(2^28次方) 到 2^28次方-1 之间
	 */
	public static void writeU29Int(ByteBuf __bytes, int value)
	{
		if (value < minU29Int || value > maxU29Int) {
			throw new IllegalArgumentException("writeU29Int，传入的参数超过范围【-(2^28次方) 到 2^28次方-1】！参数为==" + value);
		}
		//Sign contraction - the high order bit of the resulting value must match every bit removed from the number
		//Clear 3 bits 
		value &= 0x1fffffff;
		if (value < 0x80)
			__bytes.writeByte((byte)value);
		else
			if (value < 0x4000)
			{
				__bytes.writeByte((byte)(value >> 7 & 0x7f | 0x80));
				__bytes.writeByte((byte)(value & 0x7f));
			}
			else
			{
				if (value < 0x200000)
				{
					__bytes.writeByte((byte)(value >> 14 & 0x7f | 0x80));
					__bytes.writeByte((byte)(value >> 7 & 0x7f | 0x80));
					__bytes.writeByte((byte)(value & 0x7f));
				}
				else
				{
					__bytes.writeByte((byte)(value >> 22 & 0x7f | 0x80));
					__bytes.writeByte((byte)(value >> 15 & 0x7f | 0x80));
					__bytes.writeByte((byte)(value >> 8 & 0x7f | 0x80));
					__bytes.writeByte((byte)(value & 0xff));
				}
			}
	}

	public static int readInt(ByteBuf __bytes)
	{
		return __bytes.readInt();
	}
	public static void writeInt(ByteBuf __bytes, int __value)
	{
		__bytes.writeInt(__value);
	}

	/**
	 * Gets an unsigned 32-bit integer at the current readerIndex and increases the readerIndex by 4 in this buffer.
	 * 
	 * @param __bytes
	 * @return
	 */
	public static long readUnsignedInt(ByteBuf __bytes)
	{
		return __bytes.readUnsignedInt();
	}
	//    public static void writeUnsignedInt(ByteBuf __bytes, uint __value)
	//    {
	//        __bytes.writeUnsignedInt(__value);
	//    }

	public static long readLong(ByteBuf __bytes)
	{
		return __bytes.readLong();
	}
	public static void writeLong(ByteBuf __bytes, long __value)
	{
		__bytes.writeLong(__value);
	}

	//    public static ulong readUnsignedLong(ByteBuf __bytes)
	//    {
	//        return BitConverter.ToUInt64(((ByteArray)__bytes)._readStreamBytesEndian(8), 0);
	//    }
	//    public static void writeUnsignedLong(ByteBuf __bytes, ulong __value)
	//    {
	//        ((ByteArray)__bytes)._writeStreamBytesEndian(BitConverter.GetBytes(__value));
	//    }

	public static float readFloat(ByteBuf __bytes)
	{
		return __bytes.readFloat();
	}
	public static void writeFloat(ByteBuf __bytes, float __value)
	{
		__bytes.writeFloat(__value);
	}

	public static double readDouble(ByteBuf __bytes)
	{
		return __bytes.readDouble();
	}
	public static void writeDouble(ByteBuf __bytes, double __value)
	{
		__bytes.writeDouble(__value);
	}

	/**
	 * 
	 * @param __bytes
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String readUTF(ByteBuf __bytes) throws UnsupportedEncodingException
    {
		//因为AS3自动往里塞了一个UShort，所以其他语言只好用UShort
		final int length = readUnsignedShort(__bytes);
		final byte[] req = new byte[length];
		__bytes.readBytes(req);
		
        return new String(req,"UTF-8");
    }
    public static void writeUTF(ByteBuf __bytes, String __value) throws UnsupportedEncodingException
    {
    	if (__value==null || __value=="") {
    		writeUnsignedShort(__bytes, 0);
			return;
		}
    	
    	byte[] bytes = __value.getBytes("UTF-8");
    	final int length = bytes.length;
    	//因为AS3自动往里塞了一个UShort，所以其他语言只好用UShort
    	writeUnsignedShort(__bytes, length);
    	__bytes.writeBytes(bytes);
    }

    public static Object readObject(ByteBuf __bytes) throws NotSupportedException
    {
    	short typeCode = __bytes.readUnsignedByte();
    	return ReadAMF3Data(__bytes, AMF3SerializationDefine.valueOf(typeCode));
    }

	public static void writeObject(ByteBuf __bytes, Object __value) throws NotSupportedException
    {
		writeAMF3Object(__bytes, __value);
    }
	
	public static ByteBuf readByteArray(ByteBuf __bytes)
	{
		final int length = readU29Int(__bytes);
		ByteBuf __target = UnpooledByteBufAllocator.DEFAULT.heapBuffer(length);
		__bytes.readBytes(__target, length);
		return __target;
	}
	public static void writeByteArray(ByteBuf __bytes, ByteBuf __target)
	{
		final int length = __target.readableBytes();
		writeU29Int(__bytes, length);
		__bytes.writeBytes(__target);
	}
	
	public static BitArray readBitArray(ByteBuf __bytes) {
		final int length = readU29Int(__bytes);
		if (length==0) {
			return new BitArray(0);
		} else {
			byte[] dst = new byte[length];
			__bytes.readBytes(dst);
			return new BitArray(BitArray.BITS_PER_UNIT*length, dst);
		}
	}
	public static void writeBitArray(ByteBuf __bytes, BitArray __mask__) {
		final int length = __mask__.size();
		writeU29Int(__bytes, length);
		if (length > 0) {
			__bytes.writeBytes(__mask__.toByteArray());
		}
	}

	/**
	 * 序列化自定义对象
	 * 
	 * @param __targetBytes
	 * @param __object
	 * @throws NotSupportedException 
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("rawtypes")
	public static void customSerialization(ByteBuf __targetBytes, Object __object) throws NotSupportedException, UnsupportedEncodingException
    {
		if (__object instanceof Enum) {
			//此处依赖：由其他语言发来的枚举也必须从0开始顺序递增
            writeU29Int(__targetBytes, ((Enum)__object).ordinal());
        } else if(__object instanceof ICommunicationDataBase) {
            ((ICommunicationDataBase)__object).toBytes(__targetBytes);
        } else {
            writeObject(__targetBytes, __object);
        }
    }

	@SuppressWarnings("rawtypes")
	public static Object customDeserialization(ByteBuf __serializationBytes, Class<?> clazz)
			throws InstantiationException, IllegalAccessException, UnsupportedEncodingException, NotSupportedException {
		if (clazz==null) {
			throw new IllegalArgumentException("customDeserialization(ByteBuf __serializationBytes, Class<?> clazz) clazz can not be null!");
		}
//        if(clazz == null)
//        {
//            string __typeDefine = __serializationBytes.readUTF();
//            Assembly[] ass = AppDomain.CurrentDomain.GetAssemblies();
//            foreach (Assembly assembly in ass)
//            {
//                Type assemblyClassType = assembly.GetType(__typeDefine);
//                if (assemblyClassType != null)
//                {
//                    clazz = assemblyClassType;
//                    break;
//                }
//            }
//        }
        if(clazz.isEnum()) {
        	Class<? extends Enum> asSubclass = clazz.asSubclass(Enum.class);
        	;
        	int value = readU29Int(__serializationBytes);
        	for (Enum e : asSubclass.getEnumConstants()) {
        		//此处依赖：由其他语言发来的枚举也必须从0开始顺序递增
				if (e.ordinal() == value) {
					return e;
				}
			}
            return null;
        } else if (clazz.isAssignableFrom(ICommunicationDataBase.class)) {
        	ICommunicationDataBase __object = (ICommunicationDataBase)clazz.newInstance();
            __object.fromBytes(__serializationBytes);
            return __object;
        } else {
            return readObject(__serializationBytes);
        }
    }
    
	private static void writeAMF3Null(ByteBuf __bytes)
    {
		__bytes.writeByte(AMF3SerializationDefine.Null.ordinal());
    }
	private static void writeAMF3Object(ByteBuf __bytes, Object __object) throws NotSupportedException {
		
		if (__object == null)
        {
			writeAMF3Null(__bytes);
            return;
        }
		
		throw new NotSupportedException("NotSupported writeAMF3Object");
		// TODO Auto-generated method stub
	}

    private static Object ReadAMF3Data(ByteBuf __bytes, AMF3SerializationDefine typeCode) throws NotSupportedException {
    	if (typeCode==null) {
			throw new IllegalArgumentException("typeCode can not be null!");
		}
    	
    	Object returnResult = null;
    	switch (typeCode) {
		case Undefined:
		{
			returnResult = new Undefined();
			break;
		}
		case Null:
		{
			returnResult = null;
			break;
		}
		case BooleanFalse:
		{
			returnResult = false;
			break;
		}
		case BooleanTrue:
		{
			returnResult = true;
			break;
		}
		case Int:
		{
			returnResult = readU29Int(__bytes);
			break;
		}
		case Number:
		case String:
		case XMLDoc:
		case Date:
		case Array:
		case Object:
		case XML:
		case ByteArray:
		case VectorInt:
		case VectorUint:
		case VectorNumber:
		case VectorObject:
		case Dictionary:
		{
			// TODO
			throw new NotSupportedException("NotSupported AMF3SerializationDefine == " + typeCode.toString());
		}
		default:
			throw new NotSupportedException("UnKnown AMF3SerializationDefine == " + typeCode.toString());
		}
		return returnResult;
	}

    /**
     * 将__mask__的指定位置设置为true
     * 
     * @param __mask__
     * @param index
     */
	public static void writeMask(BitArray __mask__, int index) {
		__mask__.set(index, true);
	}
	/**
	 * 查询指定位置的值
	 * @param __mask__
	 * @param index
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static boolean readMask(BitArray __mask__, int index) throws ArrayIndexOutOfBoundsException {
		return __mask__.get(index);
	}

	public static void customSerializationVector(ByteBuf __bytes, List<RoleProto> _roleInfos)
			throws UnsupportedEncodingException, NotSupportedException {
		writeU29Int(__bytes, _roleInfos.size());
		for (RoleProto roleProto : _roleInfos) {
			customSerialization(__bytes, roleProto);
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<? extends ISerializationDataBase> customDeserializationVector(ByteBuf __bytes, Class<? extends ISerializationDataBase> clazz)
			throws InstantiationException, IllegalAccessException, UnsupportedEncodingException, NotSupportedException {
		final int size = readU29Int(__bytes);
		List list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			list.add(customDeserialization(__bytes, clazz));
		}
		return list;
	}

}
