package example.swing.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
public class SimpleTable2{
	public SimpleTable2(){
		JFrame f=new JFrame();
		Object[][] p={
				{"阿呆",new Integer(66),new Integer(32),new Integer(98),new Boolean(false),new Boolean(false)},
				{"阿花",new Integer(82),new Integer(69),new Integer(128),new Boolean(true),new Boolean(false)},
		};	
		String[] n={"姓名","语文","数学","总分","及格","作弊"};
		TableColumn column=null;
		JTable table=new JTable(p,n);
		table.setPreferredScrollableViewportSize(new Dimension(550,30));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		for (int i=0;i<p.length;i++){
			//利用JTable中的getColumnModel()方法取得TableColumnModel对象;再利用TableColumnModel界面所定义的getColumn()方法取
			//TableColumn对象,利用此对象的setPreferredWidth()方法就可以控制字段的宽度.
			column=table.getColumnModel().getColumn(i);
			if ((i%2)==0)
				column.setPreferredWidth(150);
			else
				column.setPreferredWidth(50);
		}
		JScrollPane scrollPane=new JScrollPane(table);
		f.getContentPane().add(scrollPane,BorderLayout.CENTER);
		f.setTitle("Simple Table");
		f.pack();
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});                	
	}
	public static void main(String[] args){
		new SimpleTable2();
	}
}  

