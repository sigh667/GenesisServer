package com.mokylin.bleach.tools.excel.cell.condition;

import java.util.HashSet;
import java.util.Set;

public final class Condition {

	public boolean x = false;
	public boolean y = false;
	public int maxValue = -1;
	public int minValue = -1;
	public boolean notNull = true;
	public boolean readAll = false;
	public int minLen = -1;
	public int maxLen = -1;
	public boolean lua = false;
	public boolean lang = false;
	public StringBuilder annotation = new StringBuilder();
	public Set<String> importPackage = new HashSet<>();
}
