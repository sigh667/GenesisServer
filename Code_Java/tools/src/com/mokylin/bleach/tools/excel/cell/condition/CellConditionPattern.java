package com.mokylin.bleach.tools.excel.cell.condition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Excel每一个cell（单元格）条件的匹配Patter定义类。<p>
 * 
 * 定义一个Excel模板的时候，除了定义每一个cell对应的数据类型和名称外，还需要对每一个cell进行一些条件限定，<br>
 * 例如：<code>int level;[minValue=0;notNull=false] // level</code>，这里所解析的condition就是[]中的内容。<p>
 * 
 * 每一个Pattern的说明：<br>
 * <li>minValue: 对应模板文件的minValue=x, 其中x是数值, 表示该条件所限定的单元格最小的数值为x;</li>
 * <li>maxValue: 对应模板文件的maxValue=x, 其中x是数值, 表示该条件所限定的单元格最大的数值为x;</li>
 * <li>x: 对应模板文件的x=boolean，表示该变量为一个x坐标,在代码生成时会自动加上 <code>x = x * BlockData.DEFAULT_BLOCK_WIDTH;</code></li>
 * <li>y: 对应模板文件的y=boolean，表示该变量为一个y坐标,在代码生成时会自动加上 <code>y = y * BlockData.DEFAULT_BLOCK_HEIGHT;</code></li>
 * <li>annotation: 对应模板文件的collection(m,n), 其中m和n为数值,表示为一个集合，即字段类型为List,Map,Set或者数组, 第一个n表示组数,第二个n表示每组的个数;</li>
 * <li>notNull: 对应模板文件的nutNull=boolean, 如果不写这一项，默认该cell的notNull为true；</li>
 * <li>maxLen: 对应模板文件的maxLen=x, 其中x是数值, 表示最大长度;</li>
 * <li>minLen: 对应模板文件的minLen=x, 其中y是数值, 表示最小长度;</li>
 * <li>genLua: 对应模板文件的genLua=boolean, 表示该字段是否生成到对应的lua文件中;</li>
 * <li>multiLang: 对应模板文件的multiLang=boolean, 表示该字段是否需要多语言;</li>
 * 
 * @author pangchong
 *
 */
enum CellConditionPattern {
	
	minValue(Pattern.compile("minvalue=(\\d+)"), new MinValueAction()),
	maxValue(Pattern.compile("maxvalue=(\\d+)"), new MaxValueAction()),
	x(Pattern.compile("x=([^\\s]+)"), new XAction()),
	y(Pattern.compile("y=([^\\s]+)"), new YAction()),
	annotation(Pattern.compile("(collection[^\\s]+)|(object[^\\s]+)|(embedobject[^\\s]+)|(nottranslate)|(cell)"), new AnnotationAction()),
	notNull(Pattern.compile("notnull=([^\\s]+)"), new NotNullAction()),
	readAll(Pattern.compile("readall=([^\\s]+)"), new ReadAllAction()),
	maxLen(Pattern.compile("maxlen=(\\d+)"), new MaxLenAction()),
	minLen(Pattern.compile("minlen=(\\d+)"), new MinLenAction()),
	genLua(Pattern.compile("genlua=([^\\s]+)"), new LuaAction()),
	multiLang(Pattern.compile("multilang=(true)|(false)"), new MultiLangAction()),
	;
	
	private final Pattern pattern;
	private final IConditionAction action;
	
	CellConditionPattern(Pattern pattern, IConditionAction action){
		this.pattern = pattern;
		this.action = action;
	}
	
	boolean matcher(String s, Condition condition){
		Matcher m = this.pattern.matcher(s);
		if(!m.matches()) return false;
		this.action.act(s, m.group(1), condition);
		return true;
	}
	
	public static void match(String s, Condition condition){
		for(CellConditionPattern each : CellConditionPattern.values()){
			if(each.matcher(s, condition)) continue;
		}
	}
}
