package example.swing.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class TableModel2 implements ActionListener{

	JTable t = null;

	public TableModel2() {

		JFrame f = new JFrame("DataModel");
		JButton b1 = new JButton("数学老师");
		b1.addActionListener(this);
		JButton b2 = new JButton("学生阿呆");
		b2.addActionListener(this);
		JPanel panel = new JPanel();
		panel.add(b1);
		panel.add(b2);

		t=new JTable(new MyTable2(1));
		t.setPreferredScrollableViewportSize(new Dimension(550, 30));
		JScrollPane s = new JScrollPane(t);

		f.getContentPane().add(panel, BorderLayout.NORTH);
		f.getContentPane().add(s, BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("学生阿呆"))
			t.setModel(new MyTable2(1));
		if (e.getActionCommand().equals("数学老师"))
			t.setModel(new MyTable2(2));
		t.revalidate();
	}

	public static void main(String args[]) {

		new TableModel2();
	}
}

class MyTable2 extends AbstractTableModel{

	Object[][] p1 = {
			{"阿呆", "1234",new Integer(66), 
				new Integer(50), new Integer(116), new Boolean(false),new Boolean(false)}};

	String[] n1 = {"姓名","学号","语文","数学","总分","及格","作弊"};

	Object[][] p2 = {
			{"阿呆", "1234",new Integer(50), new Boolean(false),new Boolean(false),"01234"},
			{"阿瓜", "1235",new Integer(75), new Boolean(true),new Boolean(false),"05678"}};

	String[] n2 = {"姓名","学号","数学","及格","作弊","电话"};

	int model = 1;

	public MyTable2(int i){
		model = i;
	}

	public int getColumnCount() {
		if(model ==1)
			return n1.length;
		else
			return n2.length;
	}

	public int getRowCount() {
		if(model ==1)
			return p1.length;
		else
			return p2.length;
	}

	public String getColumnName(int col) {
		if(model ==1)
			return n1[col];
		else
			return n2[col];
	}

	public Object getValueAt(int row, int col) {
		if(model == 1)
			return p1[row][col];
		else
			return p2[row][col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}

