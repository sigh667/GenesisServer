package com.mokylin.bleach.robot.gm;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.protobuf.ChatMessage.CGGmCmdMessage;
import com.mokylin.bleach.protobuf.ChatMessage.CGGmCmdMessage.Builder;
import com.mokylin.bleach.protobuf.ChatMessage.GCGmCmd;
import com.mokylin.bleach.protobuf.HumanMessage.CGBuyEnergy;
import com.mokylin.bleach.robot.human.Human;

/**
 * GM命令调试窗口
 */
public class GMFrame extends JFrame {

	/***/
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextArea textArea;
	
	public static final String GEN_CG = "CG:";
	private final Human human;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GMFrame frame = new GMFrame(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GMFrame(Human human) {
		this.human = human;
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 480, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton submit = new JButton("发送");
		submit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendMGCommand();
			}
		});
		submit.setBounds(317, 320, 73, 23);
		contentPane.add(submit);
		
		JButton clean = new JButton("Clean");
		clean.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textArea.setText("");
			}
		});
		clean.setBounds(391, 320, 73, 23);
		contentPane.add(clean);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(0, 0, 464, 300);
		textArea.setLineWrap(true);
		//JScrollPane scroll = new JScrollPane(textArea); 
		//contentPane.add(scroll);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMGCommand();
				}
			}
		});
		textField.setBounds(0, 321, 317, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(0, 0, 464, 309);
		contentPane.add(scrollPane);
		
		//为了让输入框刚开始就获得焦点
		this.addWindowListener( new WindowAdapter(){
            public void windowOpened(WindowEvent e) {
            	textField.requestFocus();
            }
        });
	}

	/**
	 * 给服务器发送GM命令
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	private void sendMGCommand() {
		String str = textField.getText().trim();
		if (human!=null) {
			textArea.append(String.format("#CMD# %s\n", str));
			
			String[] strArray = str.split(" ");
			List<String> strList = new LinkedList<>();
			for (int i = 0; i < strArray.length; i++) {
				if (strArray[i]!=null && !strArray[i].isEmpty() && strArray[i]!=" ") {
					strList.add(strArray[i]);
				}
			}
			
			if (strList.isEmpty()) {
				return;
			}
			try {
				sendMsg(strList);
			} catch (Exception e) {
				textArea.append(String.format("Error: %s\n", e.getMessage()));
			}
		}
		
		textField.setText("");
		textField.requestFocus();
	}

	public void handle(GCGmCmd msg) {
		//如有必要清理当前窗口
		if (textArea.getLineCount() >=100 ) {
			textArea.setText("");
		}
		
		//显示本次的内容
		List<String> paramList = msg.getParamList();
		for (int i = 0; i < paramList.size(); i++) {
			textArea.append(paramList.get(i));
			textArea.append("\n");
		}
		textArea.append("\n");
		
		//滚动条定位到最底部
		int length = textArea.getText().length(); 
		textArea.setCaretPosition(length);
	}
	public void runCmd(String cmdStr, String... args){
		StringBuilder cmd = new StringBuilder(cmdStr);
		for (String arg : args) {
			cmd.append(" ").append(arg);
		}
		textField.setText(cmd.toString());
		sendMGCommand();
	}
	
	private void sendMsg(List<String> cmdList){
		GeneratedMessage msg = null;
		if ("CGBuyEnergy".equalsIgnoreCase(cmdList.get(0))) {
			if (cmdList.size() < 2) {
				textArea.append("参数错误！正确格式为：CGBuyEnergy Counts(已购买的体力次数)\n");
				return;
			}
			msg =  CGBuyEnergy.newBuilder().setCounts(Integer.parseInt(cmdList.get(1))).build();
		} else {
			//普通GM命令
			Builder builder = CGGmCmdMessage.newBuilder();
			String cmdName = cmdList.get(0);
			
			builder.setCmd(cmdName);
			for (int i = 1; i < cmdList.size(); i++) {
				builder.addParam(cmdList.get(i));
			}
			
			msg = builder.build();
		}
		
		human.sendMessage(msg);
	}
}
