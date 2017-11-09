package com.mokylin.bleach.tools.message;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.dyuproject.protostuff.parser.EnumGroup;
import com.dyuproject.protostuff.parser.EnumGroup.Value;
import com.dyuproject.protostuff.parser.Field;
import com.dyuproject.protostuff.parser.Message;
import com.dyuproject.protostuff.parser.Proto;
import com.dyuproject.protostuff.parser.ProtoUtil;

/**
 * lua消息代码生成器
 * @author baoliang.shen
 *
 */
public class ProtoGeneratorForLua {
	
	/**模板文件所在目录*/
	static String messageTemplatePath = "config\\message";
	
	private enum MsgCgOrGc {
		NONE,
		CG_MSG,
		GC_MSG,
		;
	}
	
	public static void main(String[] args) throws Exception {
		
		/**1.0设定目录*/
		//输入目录（proto文件的目录）
		String protoFilePath = "..\\protobuf\\proto";
		if (args.length>0) {
			//优先从args[0]中读取proto文件的目录，如果没有，就用上面写死的那个
			protoFilePath = args[0];
		}
		//输出目录（生成的lua文件目录）
		String luaFilePath = "..\\..\\..\\client\\Bleach\\Resources\\message";
		if (args.length>1) {
			//优先把args[1]作为输出目录，如果没有，就用上面写死的那个
			luaFilePath = args[1];
		}
		try {
			//清空输出目录
			FileUtils.del(luaFilePath);
		} catch (IOException e1) {
			System.out.println(luaFilePath + " delete fail.");
			e1.printStackTrace();
		}
		
		// 先编写MessageType.proto文件
		
		/**2.0遴选出所有proto文件*/
		File filePath = new File(protoFilePath);
		ArrayList<File> fileList = new ArrayList<File>();
		FileUtils.traversal(filePath, fileList);
		FileUtils.filtrate(fileList, "proto");
		
		List<RequireAllBean> requireAll = new ArrayList<>();
		/**3.0解析proto文件，生成lua代码*/
		for (File fi : fileList) {
			
			if (fi.getName().equalsIgnoreCase("descriptor.proto"))
				continue;
			
			Proto p = new Proto(fi);
			try {
				ProtoUtil.loadFrom(fi, p);
			} catch (Exception e) {
				System.out.println("error:" + fi.getAbsolutePath());
				e.printStackTrace();
			}
			
			/**3.1生成MessageType.lua*/
			if (fi.getName().equalsIgnoreCase("MessageType.proto")) {
				writeMessageType(luaFilePath, fi, p);
				continue; //MessageType.proto文件中只定义消息号
			}
			
			/**3.2本proto文件中的生成各个Message*/
			requireAll.add(new RequireAllBean(writeMessages(luaFilePath, fi, p).replace(".lua", "")));
		}
		
		VelocityContext vc = new VelocityContext();
		vc.put("requireAll", requireAll);
		FileUtils.writeToFile(textReplace("requireallmessagetemplate.lua", vc), luaFilePath, "RequireAllMessage.lua");
		System.out.println("build " + "RequireAllMessage.lua" + " completed");
		System.out.println("All lua message build success!");
	}

	/**
	 * 生成各个Message
	 * @param luaFilePath
	 * @param fi
	 * @param p
	 */
	private static String writeMessages(String luaFilePath, File fi, Proto p) {
		StringBuffer commentSB = new StringBuffer();	//注释
		StringBuffer messagesSB = new StringBuffer();	//各个消息
		Collection<Message> ms = p.getMessages();
		
		//生成注释
		for(Message m : ms){
			
			commentSB.append(m.getName());
			commentSB.append("\n");
			LinkedHashMap<String, Object> extraMap = m.getExtraOptions();
			for (Entry<String, Object> entry : extraMap.entrySet()) {
				commentSB.append("\t");
				commentSB.append(entry.getKey());
				commentSB.append("=");
				commentSB.append(entry.getValue());
				commentSB.append("\n");
			}
			
			for(Field<?> f : m.getFields()){
				commentSB.append("\t");
				if (f.isRepeated()) {
					commentSB.append("repeated");
				} else if (f.isRequired()) {
					commentSB.append("required");
				} else if (f.isOptional()) {
					commentSB.append("optional");
				}
				commentSB.append("\t");
				commentSB.append(f.getJavaType());
				commentSB.append("\t");
				commentSB.append(f.getName());
				commentSB.append("=");
				commentSB.append(f.getNumber());
				commentSB.append("\n");
			}
		}
		
		//生成消息
		for(Message m : ms){
			LinkedHashMap<String, Object> extraMap = m.getExtraOptions();
			MsgCgOrGc optionMsgType = MsgCgOrGc.NONE; // 0初始值，1CG消息，2GC消息
			String messagetype = "";
			for (Entry<String, Object> entry : extraMap.entrySet()) {
				if (entry.getKey().equalsIgnoreCase("cg_message_type")) {
					optionMsgType = MsgCgOrGc.CG_MSG;
					messagetype = (String) entry.getValue();
					break;
				} else if (entry.getKey().equalsIgnoreCase("gc_message_type")) {
					optionMsgType = MsgCgOrGc.GC_MSG;
					messagetype = (String) entry.getValue();
					break;
				} else {
					throw new RuntimeException("Message:" + m.getName() + "missing messagetype!");
				}
			}
			
			VelocityContext msgContext = new VelocityContext();
			msgContext.put("messagename", m.getName());
			msgContext.put("messagetype", messagetype);
			msgContext.put("tab", "\t");
			String str = "";
			if (optionMsgType==MsgCgOrGc.CG_MSG) {
				//函数的参数列表
				StringBuffer argsSB = new StringBuffer();
				boolean isFirstParamPassed = false;
				for(Field<?> f : m.getFields()){
					if (isFirstParamPassed) {
						argsSB.append(",");
					} else {
						isFirstParamPassed = true;
					}
					argsSB.append(f.getName());
				}
				String args = argsSB.toString();
				msgContext.put("args", args);
				
				//CG消息encode
				makeProtoMessage(m, msgContext);
				
				if(!args.isEmpty()){
					StringBuffer encodemessage = new StringBuffer();
					encodemessage.append("protobuf.encode(\"");
					encodemessage.append(m.getFullName());
					encodemessage.append("\"");
					if(!args.isEmpty()){
						encodemessage.append(", ");
						encodemessage.append(m.getName());
					}

					encodemessage.append(")");
					msgContext.put("encodemessage", encodemessage.toString());
				}				
				
				str = textReplace("cgmessagetemplate.lua",msgContext);
			} else if (optionMsgType==MsgCgOrGc.GC_MSG) {
				//GC消息decode
				msgContext.put("decodemessage", m.getFullName());
				//函数的参数列表
				StringBuffer argsSB = new StringBuffer();
				StringBuilder varSB = new StringBuilder();
				for(Field<?> f : m.getFields()){
					argsSB.append("message.").append(f.getName()).append(", ");
					varSB.append(f.getName()).append(", ");
				}
				argsSB.delete(argsSB.length() - 2, argsSB.length());
				varSB.delete(varSB.length() - 2, varSB.length());
				msgContext.put("args", argsSB.toString());
				msgContext.put("vars", varSB.toString());
				
				str = textReplace("gcmessagetemplate.lua",msgContext);
			}
			messagesSB.append(str);
		}
		
		VelocityContext wholeMsgContext = new VelocityContext();
		wholeMsgContext.put("comment", commentSB.toString());
		wholeMsgContext.put("messages", messagesSB.toString());
		
		String strComment = textReplace("messagetemplate.lua", wholeMsgContext);
		
		String luaFileName = fi.getName().replaceAll(".proto", "");
		try {
			if(!luaFileName.toUpperCase().endsWith("MESSAGE") && !luaFileName.toUpperCase().endsWith("MSG")) luaFileName = luaFileName + "Message.lua";
			FileUtils.writeToFile(strComment, luaFilePath, luaFileName);
			System.out.println("build " + luaFileName + " completed");
			return luaFileName;
		} catch (IOException e) {
			System.out.println(luaFileName + " write fail.");
			throw new RuntimeException(e);
		}
	}

	private static void makeProtoMessage(Message m, VelocityContext msgContext) {
		if(m.getMessageFieldCount() == 0) {
			msgContext.put("protomessage", "");
			return;
		}
		StringBuilder protoMsg = new StringBuilder();
		protoMsg.append("local ").append(m.getName()).append(" = {\n");
		List<Field<?>> fieldsList = m.getFields();
		for(int i = 0; i < fieldsList.size(); i++){
			Field<?> f = fieldsList.get(i);
			protoMsg.append("\t\t");
			if(f.isRepeated()){
				protoMsg.append(f.getName()).append(" = ").append(f.getName()).append("");
			}else{
				protoMsg.append(f.getName()).append(" = ").append(f.getName());
			}
			if(i < fieldsList.size() - 1){
				protoMsg.append(",");
			}					
			protoMsg.append("\n");
		}
		protoMsg.append("\t}");
		msgContext.put("protomessage", protoMsg.toString());
	}
	
	/**
	 * 文本替换
	 * @param templateFileName	模板文件名
	 * @param velocityContext	文件替换规则
	 * @return 生成的字符串
	 */
	private static String textReplace(String templateFileName, VelocityContext velocityContext) {
		Properties _vp = new Properties();
		_vp.put("file.resource.loader.path", messageTemplatePath);
		try {
			Velocity.init(_vp);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		StringWriter _readWriter = new StringWriter();
		try {
			Velocity.mergeTemplate(templateFileName, "UTF-8", velocityContext, _readWriter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return _readWriter.toString();
	}

	/**
	 * 生产MessageType.lua
	 * @param luaFilePath
	 * @param fi
	 * @param p
	 */
	private static void writeMessageType(String luaFilePath, File fi, Proto p) {
		StringBuffer content = new StringBuffer();
		Map<String, EnumGroup> enumGroupMap = p.getEnumGroupMap();
		if (enumGroupMap!=null && !enumGroupMap.isEmpty()) {
			//CG消息
			EnumGroup enumGroup = p.getEnumGroup("CGMessageType");
			if (enumGroup!=null) {
				for (Value value : enumGroup.getValues()) {
					content.append(value.getName());
					content.append("=");
					content.append(value.getNumber());
					content.append(",\n");
				}
			}
			//GC消息
			enumGroup = p.getEnumGroup("GCMessageType");
			if (enumGroup!=null) {
				for (Value value : enumGroup.getValues()) {
					content.append(value.getName());
					content.append("=");
					content.append(value.getNumber());
					content.append(",\n");
				}
			}
		}
		
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("messageNums", content.toString());
		velocityContext.put("tab", "\t");
		String strFinal = textReplace("messagetypetemplate.lua", velocityContext);
		String messageTypeName = fi.getName().replaceAll(".proto", ".lua");
		try {
			FileUtils.writeToFile(strFinal, luaFilePath, messageTypeName);
			System.out.println("build " + messageTypeName + " completed");
		} catch (IOException e) {
			System.out.println(messageTypeName + " write fail.");
			e.printStackTrace();
		}
	}

}
