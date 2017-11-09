package example.swing.table;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class AddRemoveCells implements ActionListener
{
	JTable table = null;
	DefaultTableModel defaultModel = null;

	public AddRemoveCells()
	{
		JFrame f = new JFrame();
		String[] name = {"字段 1","字段 2","字段 3","字段 4","字段 5"};
		String[][] data = new String[5][5];
		int value =1;
		for(int i=0; i<data.length; i++)
		{
			for(int j=0; j<data[i].length ; j++)
				data[i][j] = String.valueOf(value++);
		}

		defaultModel = new DefaultTableModel(data,name);
		table=new JTable(defaultModel);
		table.setPreferredScrollableViewportSize(new Dimension(400, 80));
		JScrollPane s = new JScrollPane(table);

		JPanel panel = new JPanel();
		JButton b = new JButton("增加行");
		panel.add(b);
		b.addActionListener(this);
		b = new JButton("增加列");
		panel.add(b);
		b.addActionListener(this);
		b = new JButton("删除行");
		panel.add(b);
		b.addActionListener(this);
		b = new JButton("删除列");
		panel.add(b);
		b.addActionListener(this);

		Container contentPane = f.getContentPane();
		contentPane.add(panel, BorderLayout.NORTH);
		contentPane.add(s, BorderLayout.CENTER);

		f.setTitle("AddRemoveCells");
		f.pack();
		f.setVisible(true);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	/*要删除列必须使用TableColumnModel界面定义的removeColumn()方法。因此我闪先由JTable类的getColumnModel()方法取得
	 *TableColumnModel对象，再由TableColumnModel的getColumn()方法取得要删除列的TableColumn.此TableColumn对象当作是
	 *removeColumn()的参数。删除此列完毕后必须重新设置列数，也就是使用DefaultTableModel的setColumnCount()方法来设置。
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("增加列"))
			defaultModel.addColumn("增加列");
		if(e.getActionCommand().equals("增加行"))
			defaultModel.addRow(new Vector());
		if(e.getActionCommand().equals("删除列"))
		{
			int columncount = defaultModel.getColumnCount()-1;
			if(columncount >= 0)//若columncount<0代表已经没有任何列了。
			{
				TableColumnModel columnModel = table.getColumnModel();
				TableColumn tableColumn = columnModel.getColumn(columncount);
				columnModel.removeColumn(tableColumn);
				defaultModel.setColumnCount(columncount);
			}
		}
		if(e.getActionCommand().equals("删除行"))
		{
			int rowcount = defaultModel.getRowCount()-1;//getRowCount返回行数，rowcount<0代表已经没有任何行了。
			if(rowcount >= 0)
			{
				defaultModel.removeRow(rowcount);
				defaultModel.setRowCount(rowcount);//删除行比较简单，只要用DefaultTableModel的removeRow()方法即可。删除
				//行完毕后必须重新设置列数，也就是使用DefaultTableModel的setRowCount()方法来设置。
			}
		}
		table.revalidate();
	}

	public static void main(String args[]) {
		new AddRemoveCells();
	}
}
