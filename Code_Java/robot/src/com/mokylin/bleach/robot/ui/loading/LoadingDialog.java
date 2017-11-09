package com.mokylin.bleach.robot.ui.loading;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import com.mokylin.bleach.robot.font.FontFactory;

public class LoadingDialog extends JDialog {

	/***/
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	
	private JLabel label;
	
	private LoadingDlgType loadingDlgType;
	private int index = 0;
	private Timer timeAction;
	
	private static ArrayList<String> list = new ArrayList<>();

	static{
		list.add("请稍候");
		list.add(".请稍候.");
		list.add("..请稍候..");
		list.add("...请稍候...");
	}
	
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		try {
//			LoginingDialog dialog = new LoginingDialog(null,"");
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public LoadingDialog() {
		Font f = FontFactory.getfSongPlain14();
		setBounds(100, 100, 200, 100);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel dynamicLabel = new JLabel("",JLabel.CENTER);
		dynamicLabel.setBounds(20, 50, 160, 15);
		dynamicLabel.setFont(f);
		contentPanel.add(dynamicLabel);
		setTimer(dynamicLabel);
		
		label = new JLabel("",JLabel.CENTER);
		label.setBounds(20, 30, 160, 15);
		label.setFont(f);
		contentPanel.add(label);
		//setTimer(label);
		
		//以下是自己写的代码
		this.setUndecorated(true);
	}
	
	private void setTimer(JLabel jf){
		final JLabel varJf=jf;
		timeAction=new Timer(500,new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				ArrayList<String> strList = list;
				if (strList==null || strList.isEmpty())
					return;
				
				final int size = strList.size();
				if (index>=size) {
					index = 0;
				}
				String text = strList.get(index);
				if (text==null)
					return;
				varJf.setText(text);
				
				++index;
			}
		});
		timeAction.start();
	}

	public LoadingDlgType getLoadingDlgType() {
		return loadingDlgType;
	}

	public void setLoadingDlgType(LoadingDlgType loadingDlgType) {
		this.loadingDlgType = loadingDlgType;
		String text = loadingDlgType.getTitle();
		label.setText(text);
	}

	public void beginTimer() {
		if (timeAction!=null) {
			timeAction.stop();
		}
		index = 0;
		setTimer(label);
	}
}
