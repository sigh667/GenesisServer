package example.swing.tabbedpane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
/*由于ChangeEvent是属于Swing的事件，而不是AWT的事件，因此import Swing的事件类来处理
 *ChangeEvent事件。
 */
import javax.swing.event.ChangeListener;

public class JTabbedPane2 implements ActionListener,ChangeListener{

	int index=0;
	int newNumber=1;
	JTabbedPane tabbedPane=null;

	public JTabbedPane2(){
		JFrame f=new JFrame("JTabbedPane2");	
		Container contentPane=f.getContentPane();

		JLabel label1=new JLabel(new ImageIcon(".\\icons\\flower.jpg"));
		JPanel panel1=new JPanel();
		panel1.add(label1);

		JLabel label2=new JLabel("Label 2",JLabel.CENTER);
		label2.setBackground(Color.pink);
		label2.setOpaque(true);
		JPanel panel2=new JPanel();
		panel2.add(label2);

		JLabel label3=new JLabel("Label 3",JLabel.CENTER);
		label3.setBackground(Color.yellow);
		label3.setOpaque(true);
		JPanel panel3=new JPanel();
		panel3.add(label3);

		tabbedPane=new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.TOP);//设置标签置放位置。
		/*由于ChangeEvent是属于Swing的事件，而不是AWT的事件，因此import Swing的事件类来处理
		 *ChangeEvent事件。
		 */
		tabbedPane.addChangeListener(this);
		tabbedPane.addTab("Picture",null,panel1,"图案");
		tabbedPane.addTab("Label 2",panel2);
		tabbedPane.addTab("Label 3",null,panel3,"label");
		tabbedPane.setEnabledAt(2,false);//设Label 3标签为Disable状态
		JButton b=new JButton("新增标签");
		b.addActionListener(this);
		contentPane.add(b,BorderLayout.SOUTH);
		contentPane.add(tabbedPane,BorderLayout.CENTER);

		f.pack();
		f.show();
		f.addWindowListener(new WindowAdapter(){
			public void WindowClosing(WindowEvent e){
				System.exit(0);
			}
		});     
	}	
	/*实现ChangeListener方法，目的在使若左边的标签有点选过，右边的标签才会显示Enable状态。getSelectedIndex()方法可返回
	 *目前点选标签的index值，getTabCount()方法可返回JTabbedPane上目前共有几个标签，而setEnabledAt()方法则是使某个标签
	 *的状态为Enable或Disable(true为Enable,false为Disable).
	 */
	public void stateChanged(ChangeEvent e){
		if (index!=tabbedPane.getSelectedIndex()){
			if(index<tabbedPane.getTabCount()-1)
				tabbedPane.setEnabledAt(index+1,true);
		}
		index=tabbedPane.getSelectedIndex();
	}
	/*实现ActionListener接口,当用户按下"新增标签"按钮时，就
	 *会在tabbedPane上新增一个Disable状态的标签。
	 */
	public void actionPerformed(ActionEvent e){
		JPanel pane1=new JPanel();
		JLabel label4=new JLabel("new label"+newNumber,JLabel.CENTER);
		label4.setOpaque(true);
		pane1.add(label4);
		tabbedPane.addTab("new "+newNumber,pane1);
		tabbedPane.setEnabledAt(newNumber+2,false);
		newNumber++;
		tabbedPane.validate();
	}
	public static void main(String[] args){
		new JTabbedPane2();
	}
}
