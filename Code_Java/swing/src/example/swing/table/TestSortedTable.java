package example.swing.table;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TestSortedTable {  
	public static void main(String args[]) {  
		JFrame frame = new JFrame("JTable的排序测试");  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		// 表格中显示的数据  
		Object rows[][] = { { "王明", "中国", 44 }, { "姚明", "中国", 25 },  
				{ "赵子龙", "西蜀", 1234 }, { "曹操", "北魏", 2112 },  
				{ "Bill Gates", "美国", 45 }, { "Mike", "英国", 33 } };  
		String columns[] = { "姓名", "国籍", "年龄" };  
		TableModel model = new DefaultTableModel(rows, columns);  
		JTable table = new JTable(model);  
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);  
		table.setRowSorter(sorter);  
		JScrollPane pane = new JScrollPane(table);  
		frame.add(pane, BorderLayout.CENTER);  
		frame.setSize(300, 150);  
		frame.setVisible(true);  
	}
}  