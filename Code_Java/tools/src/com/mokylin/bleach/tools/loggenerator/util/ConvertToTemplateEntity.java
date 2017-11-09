package com.mokylin.bleach.tools.loggenerator.util;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.tools.loggenerator.component.LogEntityForTemplate;
import com.mokylin.bleach.tools.loggenerator.component.LogField;
import com.mokylin.bleach.tools.loggenerator.component.LogTemplate;
import com.mokylin.bleach.tools.loggenerator.component.SpecialArg;

public class ConvertToTemplateEntity {
	
	public static List<LogEntityForTemplate> convertToTemplateEntities(
			List<LogTemplate> logTemplates) {
		List<LogEntityForTemplate> logEntities = Lists.newLinkedList();
		for (LogTemplate logTemplate : logTemplates) {
			logEntities.add(convertToTemplateEntity(logTemplate));
		}

		return logEntities;
	}

	private static LogEntityForTemplate convertToTemplateEntity(
			LogTemplate logTemplate) {
		String logName = logTemplate.logName;
		String logDescription = logTemplate.logDescprition;
		String logRemark = logTemplate.logRemark;
		StringBuilder argsB = new StringBuilder();
		StringBuilder prefixB = new StringBuilder();
		StringBuilder setterB = new StringBuilder();
		setterB.append("sb.append(\"").append(logName).append("\").append(\"|\")");
		StringBuilder fieldDescB = new StringBuilder();
		Set<String> importClasses = Sets.newHashSet();

		Set<SpecialArg> specialArgs = getSpecialArgs(logTemplate);
		for(SpecialArg specialArg : specialArgs) {
			if(specialArg.fromType != null) {
				importClasses.add(specialArg.fromType.getName());
			}
			importClasses.addAll(specialArg.importClasses);
		}

		Set<Class<?>> argsClasses = Sets.newHashSet();
		for(SpecialArg specialArg : specialArgs) {
			if(specialArg.fromType == null) continue;
			if(specialArg.fromType.equals(ServerGlobals.class)) continue;
			argsClasses.add(specialArg.fromType);
		}
		for(Class<?> clazz : argsClasses) {
			String typeName = clazz.getSimpleName();
			argsB.append(typeName).append(" ").append(getVariableName(typeName)).append(", ");
		}
		
		boolean needRemoveLastComma = false;
		boolean containsILoginWayField = containsILoginWayField(logTemplate);
		if(containsILoginWayField) {
			prefixB.append("sb.append(" + SpecialArg.iLoginWay.howToGet + ")");
		} else {
			prefixB.append("sb.append(\"0\")");
		}
		
		for (int i = 0; i < logTemplate.fields.size(); i++) {
			LogField field = logTemplate.fields.get(i);
			String fieldName = field.name;
			String type = field.type;
			boolean isDate = false;
			if (type.equals("datetime")) {
				type = "long";
				isDate = true;
			}
			if(!isSpecialArg(fieldName))
				argsB.append(type).append(" ").append(fieldName);
			
			setterB.append(".append(");
			
			if(isSpecialArg(fieldName)) {
				SpecialArg sArg = SpecialArg.get(fieldName);
				setterB.append(sArg.howToGet);
			} else {
				if (!isDate) {
					setterB.append(fieldName);
				} else {
					setterB.append("TimeUtils.convertToDateString(")
							.append(fieldName).append(")");
				}
			}
			
			setterB.append(")");
			
			if (!isLastField(i, logTemplate.fields)) {
				if(!isSpecialArg(fieldName))
					argsB.append(", ");
				
				setterB.append(".append(\"|\")");
			} else {
				setterB.append(";");
			}
			
			if(!isSpecialArg(fieldName))
				fieldDescB.append("* @param ").append(fieldName).append("\t").append(field.description).append("\t").append(field.remark).append("\n");
			if(isLastField(i, logTemplate.fields) && isSpecialArg(fieldName))
				needRemoveLastComma = true;
		}

		String args = argsB.toString();
		if(needRemoveLastComma)
			args = args.substring(0, args.length() - 2);
		
		return new LogEntityForTemplate(logName, logDescription, logRemark, fieldDescB.toString(),
				args, prefixB.toString(), setterB.toString(), importClasses);
	}
	
	private static boolean isSpecialArg(String name) {
		return SpecialArg.get(name) != null;
	}
	
	private static String getVariableName(String typeSimpleName) {
		return typeSimpleName.substring(0, 1).toLowerCase() + typeSimpleName.substring(1);
	}
	
	/**
	 * 获取特殊参数
	 * @param logTemplate
	 * @return
	 */
	private static Set<SpecialArg> getSpecialArgs(LogTemplate logTemplate) {
		Set<SpecialArg> specialArgs = Sets.newHashSet();
		for(LogField field : logTemplate.fields) {
			SpecialArg sArg = SpecialArg.get(field.name);
			if(sArg != null) {
				specialArgs.add(sArg);
			}
		}
		
		return specialArgs;
	}
	
	private static boolean containsILoginWayField(LogTemplate logTemplate) {
		for(LogField field : logTemplate.fields) {
			if(field.name.equals("iLoginWay"))
				return true;
		}
		
		return false;
	}
	
	private static boolean isLastField(int i, List<LogField> fields) {
		if(i == fields.size() - 1)
			return true;
		
		return false;
	}
}
