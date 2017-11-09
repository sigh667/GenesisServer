package com.mokylin.bleach.tools.lang.jframe;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.mokylin.bleach.tools.lang.Language;

/**
 * 选择语言的对话框
 * @author yaguang.xiao
 *
 */
public class SelectLanguageFrame {
	private JLabel languageLabel;
	private JComboBox<String> languageComboBox;

	public SelectLanguageFrame(final AfterSelectLanguage task) {
		final JFrame frame = new JFrame("选择语言");// 创建指定标题的JFrame窗口对象
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 关闭按钮的动作为退出窗口
		frame.setSize(350, 300);// 设置窗口大小
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();// 获得显示器大小对象
		Dimension frameSize = frame.getSize();// 获得窗口大小对象
		if (frameSize.width > displaySize.width)
			frameSize.width = displaySize.width;// 窗口的宽度不能大于显示器的宽度
		if (frameSize.height > displaySize.height)
			frameSize.height = displaySize.height;// 窗口的高度不能大于显示器的高度
		frame.setLocation((displaySize.width - frameSize.width) / 2,
				(displaySize.height - frameSize.height) / 2);// 设置窗口居中显示器显示
		frame.setTitle("选择语言");
		frame.setLayout(null);
		
		languageLabel = new JLabel("请选择语言：");
		languageLabel.setBounds(35, 120, 80, 20);
		
		languageComboBox = new JComboBox<String>(getLanguageNameArr());
		languageComboBox.setBounds(125, 120, 80, 20);
		languageComboBox.setSelectedIndex(0);
		
		final JButton btn = new JButton("确定");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btn) {
					frame.dispose();
					Language lan = Language.getByName(languageComboBox.getItemAt(languageComboBox.getSelectedIndex()));
					task.action(lan);
				}
			}
			
		});
		btn.setBounds(210, 120, 70, 20);
		
		frame.add(languageLabel);
		frame.add(languageComboBox);
		frame.add(btn);
		frame.setVisible(true);// 设置窗口为可见的，默认为不可见
	}
	
	/**
	 * 获取语言名字数组
	 * @return
	 */
	private String[] getLanguageNameArr() {
		String[] languageNames = new String[Language.values().length - 1];
		int index = 0;
		for(Language lan : Language.values()) {
			if(lan.equals(Language.zh_CN)) {
				continue;
			}
			languageNames[index] = lan.getName();
			index ++;
		}
		
		return languageNames;
	}

	public static void main(String[] args) {
		new SelectLanguageFrame(new AfterSelectLanguage() {

			@Override
			public void action(Language lan) {
				System.out.println(lan);
			}
			
		});
	}
}