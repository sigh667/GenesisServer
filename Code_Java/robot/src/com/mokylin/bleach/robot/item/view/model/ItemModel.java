package com.mokylin.bleach.robot.item.view.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.mokylin.bleach.robot.item.Item;

public class ItemModel extends AbstractTableModel {
	
	/***/
	private static final long serialVersionUID = 1L;
	
	static final String columns[] = { "名称", "叠加数量", "品质", "使用等级" };
	
	/**要显示的道具*/
	private List<ItemToView> itemsAll;


	public ItemModel(List<ItemToView> itemsAll) {
		this.itemsAll = itemsAll;
	}

	@Override
	public int getRowCount() {
		return itemsAll.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ItemToView itemToView = itemsAll.get(rowIndex);
		return itemToView.get(columns[columnIndex]);
	}

	@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public Item getItem(int selectedRow) {
		return itemsAll.get(selectedRow).getItem();
	}
}
