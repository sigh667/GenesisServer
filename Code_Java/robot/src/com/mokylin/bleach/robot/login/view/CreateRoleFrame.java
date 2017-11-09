package com.mokylin.bleach.robot.login.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.mokylin.bleach.protobuf.PlayerMessage.CGCreateRole;
import com.mokylin.bleach.robot.font.FontFactory;
import com.mokylin.bleach.robot.robot.Robot;
import com.mokylin.bleach.robot.ui.PanelFactory;
import com.mokylin.bleach.robot.ui.loading.LoadingDialog;
import com.mokylin.bleach.robot.ui.loading.LoadingDlgType;

public class CreateRoleFrame extends JFrame {

	/**角色名字长度上限，目前不区分中英文*/
	private static final int roleNameLength = 8;
	/***/
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateRoleFrame frame = new CreateRoleFrame(null,"");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param robot 
	 * @param name 
	 */
	public CreateRoleFrame(final Robot robot, String name) {
		Font f = FontFactory.getfSongPlain14();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(95, 109, 134, 21);
		textField.setFont(f);
		contentPane.add(textField);
		textField.setText(name);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				//控制字符数
				if (textField.getText().length() >= roleNameLength) {
					e.consume();
				}
			}

		});
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("创建角色");
		btnNewButton.setFont(f);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				onCreateRole(robot);
			}
		});
		btnNewButton.setBounds(264, 108, 93, 23);
		contentPane.add(btnNewButton);
	}
	
	private void onCreateRole(final Robot robot) {
		String text = textField.getText();
		if (text==null || text.isEmpty()) {
			return;
		}
		CGCreateRole msg = CGCreateRole.newBuilder().setName(text).build();
		if (robot!=null)
		{
			//启动模态对话框
			LoadingDialog logining = PanelFactory.getLoginingDialog();
			logining.setLoadingDlgType(LoadingDlgType.CreatingRole);
			//必须先设置模态，后设置Visible
			//logining.setModal(true);
			logining.setLocationRelativeTo(this);
			logining.setVisible(true);
			logining.beginTimer();
			
			robot.sendMessage(msg);
		}
	}
}
