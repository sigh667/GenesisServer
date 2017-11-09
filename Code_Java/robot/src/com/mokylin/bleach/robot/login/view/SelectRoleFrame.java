package com.mokylin.bleach.robot.login.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mokylin.bleach.protobuf.PlayerMessage.CGSelectRole;
import com.mokylin.bleach.protobuf.PlayerMessage.Role;
import com.mokylin.bleach.robot.font.FontFactory;
import com.mokylin.bleach.robot.login.view.model.DataModel;
import com.mokylin.bleach.robot.login.view.model.RoleToView;
import com.mokylin.bleach.robot.robot.Robot;
import com.mokylin.bleach.robot.ui.PanelFactory;
import com.mokylin.bleach.robot.ui.loading.LoadingDialog;
import com.mokylin.bleach.robot.ui.loading.LoadingDlgType;

public class SelectRoleFrame extends JFrame {

	/***/
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private Robot robot;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					List<RoleToView> rolesList = new ArrayList<>();
					for (Integer i = 0; i < 3; i++) {
						Role role = Role.newBuilder().setId(i).setName(i.toString()+"号人物").build();
						RoleToView roleToView = new RoleToView(role);
						rolesList.add(roleToView);
					}
					
					SelectRoleFrame frame = new SelectRoleFrame(null,rolesList);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param rolesList 
	 * @param robot 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SelectRoleFrame(Robot robot, List<RoleToView> rolesList) {
		this.robot = robot;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 565, 386);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//显示角色列表
		DataModel dataModel = new DataModel(rolesList);
		final JList list = new JList(dataModel);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(list.getSelectedIndex() != -1
						&& e.getClickCount() == 2) {
					twoClick(list.getSelectedValue());
				}
			}
		});
		list.setVisibleRowCount(rolesList.size());//设置程序一打开时所能看到的数据项个数。	
	    list.setBorder(BorderFactory.createTitledBorder("角色列表"));
	    Font f = FontFactory.getfSongPlain14();
	    list.setFont(f);
		list.setBounds(187, 103, 166, 115);
		contentPane.add(list);
		
		JLabel label = new JLabel("双击进入游戏");
		label.setBounds(217, 270, 90, 15);
		label.setFont(f);
		contentPane.add(label);
	}
	
	private void twoClick(Object selectedValue) {
		//获取被选中的角色
		RoleToView roleToView = (RoleToView)selectedValue;
		Role role = roleToView.getRole();
		
		//发送选择角色的消息
		CGSelectRole msg = CGSelectRole.newBuilder().setId(role.getId()).build();
		if(robot!=null)
			robot.sendMessage(msg);
		
		//启动模态对话框
		LoadingDialog logining = PanelFactory.getLoginingDialog();
		logining.setLoadingDlgType(LoadingDlgType.SelectingRole);
		//必须先设置模态，后设置Visible
		logining.setModal(true);
		logining.setLocationRelativeTo(this);
		logining.setVisible(true);
		logining.beginTimer();
	}
}
