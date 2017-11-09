package com.mokylin.bleach.robot;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.mokylin.bleach.robot.core.Global;
import com.mokylin.bleach.robot.core.net.Connect;
import com.mokylin.bleach.robot.ui.PanelFactory;
import com.mokylin.bleach.robot.ui.loading.LoadingDialog;
import com.mokylin.bleach.robot.ui.loading.LoadingDlgType;

/**
 * 有UI的机器人main函数入口
 * @author baoliang.shen
 *
 */
public class RobotUIMain {

	private static final int accountMaxLength = 15;
	private JFrame frmBleach;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Global.init(true);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RobotUIMain window = new RobotUIMain();
					window.frmBleach.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RobotUIMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBleach = new JFrame();
		frmBleach.setTitle("BLEACH脱机挂");
		frmBleach.setBounds(100, 100, 450, 300);
		frmBleach.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		PanelFactory.setLoginFram(frmBleach);
		
		JButton btnNewButton = new JButton("login");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				login();
			}
		});
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				//最多值允许15个字符
				if (textField.getText().length() >= accountMaxLength) {
					e.consume();
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
		textField.setColumns(10);
		
		//为了让输入框刚开始就获得焦点
		frmBleach.addWindowListener( new WindowAdapter(){
			public void windowOpened(WindowEvent e) {
				textField.requestFocus();
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(frmBleach.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(152)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton)
					.addGap(91))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(127, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton))
					.addGap(114))
		);
		frmBleach.getContentPane().setLayout(groupLayout);
		
		//centerScreen();
	}
	
	private void login() {
		LoadingDialog logining = PanelFactory.getLoginingDialog();
		logining.setLoadingDlgType(LoadingDlgType.Logining);

		//必须先设置模态，后设置Visible
		//logining.setModal(true);
		logining.setLocationRelativeTo(frmBleach);
		logining.setVisible(true);
		logining.beginTimer();
		//建立连接
		Connect connect = new Connect("127.0.0.1",12306,textField.getText());
		connect.connectToServer();
	}

	/**
	 * 居于屏幕中间
	 */
	public   void   centerScreen()   {   
		Dimension   dim   =   frmBleach.getToolkit().getScreenSize();   
		Rectangle   abounds   =   frmBleach.getBounds();   
		frmBleach.setLocation((dim.width   -   abounds.width)   /   2,   
				(dim.height   -   abounds.height)   /   2);   
		frmBleach.requestFocus();   
	}   
}
