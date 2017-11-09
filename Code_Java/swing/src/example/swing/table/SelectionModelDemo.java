package example.swing.table;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SelectionModelDemo implements ActionListener,ListSelectionListener{
	JTable table=null;
	ListSelectionModel selectionMode=null;
	JLabel label=null;//显示用户选取表格之用
	public SelectionModelDemo(){
		JFrame f=new JFrame();
		String[] name={"字段1","字段2","字段3","字段4","字段5"};
		String[][] data=new String[5][5];
		int value=1;
		for(int i=0;i<data.length;i++){
			for (int j=0;j<data[i].length;j++){
				data[i][j]=String.valueOf(value++);
			}
		}
		table=new JTable(data,name);
		table.setPreferredScrollableViewportSize(new Dimension(400,80));
		table.setCellSelectionEnabled(true);//使得表格的选取是以cell为单位,而不是以列为单位.若你没有写此行,则在选取表格数
		//据时以整列为单位.
		selectionMode=table.getSelectionModel();//取得table的ListSelectionModel.
		selectionMode.addListSelectionListener(this);
		JScrollPane s=new JScrollPane(table);
		JPanel panel=new JPanel();
		JButton b=new JButton("单一选择");
		panel.add(b);
		b.addActionListener(this);
		b=new JButton("连续区间选择");
		panel.add(b);
		b.addActionListener(this);
		b=new JButton("多重选择");   	   
		panel.add(b);
		b.addActionListener(this);

		label=new JLabel("你选取:");

		Container contentPane=f.getContentPane();
		contentPane.add(panel,BorderLayout.NORTH);
		contentPane.add(s,BorderLayout.CENTER);
		contentPane.add(label,BorderLayout.SOUTH);

		f.setTitle("SelectionModelDemo");
		f.pack();
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});   	   
	}

	/*处理按钮事件,利用ListSelectionModel界面所定义的setSelectionMode()方法来设置表格选取模式.*/
	public void actionPerformed(ActionEvent e){
		if (e.getActionCommand().equals("单一选择"))
			selectionMode.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (e.getActionCommand().equals("连续区间选择"))
			selectionMode.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		if (e.getActionCommand().equals("多重选择"))
			selectionMode.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.revalidate();
	}    

	/*当用户选取表格数据时会触发ListSelectionEvent,我们实现ListSelectionListener界面来处理这一事件.ListSelectionListener界
	 *面只定义一个方法,那就是valueChanged().
	 */ 	
	public void valueChanged(ListSelectionEvent el){
		String tempString="";
		//JTable的getSelectedRows()与getSelectedColumns()方法会返回已选取表格cell的index Array数据.
		int[] rows=table.getSelectedRows();
		int[] columns=table.getSelectedColumns();

		//JTable的getValueAt()方法会返回某行的cell数据,返回值是Object数据类型,因此我们要自行转成String数据类型.
		for (int i=0;i<rows.length;i++){
			for (int j=0;j<columns.length;j++)
				tempString = tempString+" "+(String)table.getValueAt(rows[i], columns[j]);      
		}
		label.setText("你选取:"+tempString);
	}
	public static void main(String[] args){
		new SelectionModelDemo();
	}
}

