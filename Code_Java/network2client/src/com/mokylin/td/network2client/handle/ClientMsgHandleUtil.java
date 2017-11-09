package com.mokylin.td.network2client.handle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mokylin.bleach.core.function.FunctionUtil;
import com.mokylin.bleach.core.util.GenericityUtil;
import com.mokylin.bleach.core.util.PackageUtil;
import com.mokylin.td.clientmsg.core.ICommunicationDataBase;

/**
 * Handler扫描工具类
 * @author baoliang.shen
 *
 */
public class ClientMsgHandleUtil {

	/**
	 * 查找指定包下，所有继承自{@link IClientMsgHandle}的Handler
	 * @param clisentMsgFuncsPackage 所有消息所在的包
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Map<Integer, IClientMsgHandle<ICommunicationDataBase>> buildMsgHandle(String clisentMsgFuncsPackage) throws InstantiationException, IllegalAccessException {
		Map<Integer, IClientMsgHandle<ICommunicationDataBase>> map = new HashMap<>();
		Set<Class<?>> funcClassSet = PackageUtil.getSubClass(clisentMsgFuncsPackage, IClientMsgHandle.class);
		if (funcClassSet == null || funcClassSet.isEmpty())
			return map;

		for (Class<?> each : funcClassSet) {
			FunctionUtil.assertIsFunctionClass(each);
			Class<?> msgTypeClass = GenericityUtil.extractFirstGenericType(each, IClientMsgHandle.class);
			ICommunicationDataBase msgInstance = (ICommunicationDataBase) msgTypeClass.newInstance();
			Integer msgTypeId = msgInstance.getSerializationID();
			@SuppressWarnings("unchecked")
			IClientMsgHandle<ICommunicationDataBase> eachHandler = (IClientMsgHandle<ICommunicationDataBase>)each.newInstance();
			map.put(msgTypeId, eachHandler);
		}

		return map;
	}

}
