package com.mokylin.bleach.core.template.poi;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class SegmentRow implements Row {
	
	private Sheet sheet;
	
	private Row row;
	
	private String[] cellIndexSegment;
	
	public SegmentRow(Sheet sheet, Row row, String[] cellIndexSegment){
		this.sheet = sheet;
		this.row = row;
		this.cellIndexSegment = cellIndexSegment;
	}

	@Override
	public Iterator<Cell> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Cell createCell(int column) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Cell createCell(int column, int type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeCell(Cell cell) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setRowNum(int rowNum) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getRowNum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Cell getCell(int cellnum) {
		return row.getCell(Integer.parseInt(cellIndexSegment[cellnum]));
	}

	@Override
	public Cell getCell(int cellnum, MissingCellPolicy policy) {
		throw new UnsupportedOperationException();
	}

	@Override
	public short getFirstCellNum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public short getLastCellNum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPhysicalNumberOfCells() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHeight(short height) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setZeroHeight(boolean zHeight) {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean getZeroHeight() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHeightInPoints(float height) {
		throw new UnsupportedOperationException();

	}

	@Override
	public short getHeight() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getHeightInPoints() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFormatted() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellStyle getRowStyle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRowStyle(CellStyle style) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Iterator<Cell> cellIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Sheet getSheet() {
		return sheet;
	}

}
