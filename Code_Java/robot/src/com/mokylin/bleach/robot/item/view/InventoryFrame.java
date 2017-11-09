package com.mokylin.bleach.robot.item.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.protobuf.ItemMessage.CGItemSell;
import com.mokylin.bleach.robot.font.FontFactory;
import com.mokylin.bleach.robot.item.Inventory;
import com.mokylin.bleach.robot.item.Item;
import com.mokylin.bleach.robot.item.view.model.ItemModel;
import com.mokylin.bleach.robot.item.view.model.ItemToView;

/**
 * 背包界面
 * @author baoliang.shen
 *
 */
public class InventoryFrame extends JFrame {

	/***/
	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane;

	/**背包数据*/
	private Inventory inventory;
	
	/**每个页签中的道具显示对象*/
	private ArrayList<JTable> itemModelList = new ArrayList<>();
	/**被选中的页签索引*/
	private int selectTabIndex = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GlobalData.init("..\\resources\\scripts", false);
					Inventory inventory = new Inventory(null);
					for (int i = 0; i < 14; i++) {
						inventory.addItem(22+i, (i+1)*5);
					}
					InventoryFrame frame = new InventoryFrame(inventory);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param inventory 
	 */
	public InventoryFrame(Inventory inventoryParam) {
		this.inventory = inventoryParam;

		//setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 584, 341);

		tabbedPane = new JTabbedPane();
		Font f = FontFactory.getfSongPlain14();
		tabbedPane.setFont(f);


		addAllTab();

		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		//捕捉切换Tab页的事件
		tabbedPane.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				JTabbedPane tabbedPane = (JTabbedPane)e.getSource();
				selectTabIndex = tabbedPane.getSelectedIndex();
			}
		});
		

		JButton b=new JButton("使用");
		b.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onUse();
			}
		});
		b.setBounds(158, 240, 66, 23);
		b.setFont(f);
		JButton b_1=new JButton("详情");
		b_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onDetails();
			}
		});
		b_1.setBounds(346, 240, 66, 23);
		b_1.setFont(f);
		JButton b_2=new JButton("出售");
		b_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onSell();
			}
		});
		b_2.setBounds(446, 240, 66, 23);
		b_2.setFont(f);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(b);
		buttonPanel.add(b_1);
		buttonPanel.add(b_2);

		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	public void refresh(Inventory inventoryParam) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 添加所有Tab页
	 */
	private void addAllTab() {
		//1.0清空Tab页中的所有控件
		tabbedPane.removeAll();
		itemModelList.clear();
		selectTabIndex = 0;

		//2.0获取数据源
		List<Item> items = inventory.getAll();
		List<ItemToView> itemToViews = InventoryViewUtil.buildAndSortAll(items);

		//3.0添加各个Tab页
		for (InventoryType inventoryType : InventoryType.values()) {
			JPanel tabPanel = addTab(itemToViews, inventoryType);
			tabbedPane.addTab(inventoryType.getTitle(), null, tabPanel, inventoryType.getTip());
		}
	}
	/**
	 * 生成每个Tab页
	 * @param itemsAll
	 * @param inventoryType
	 * @return
	 */
	private JPanel addTab(List<ItemToView> itemsAll, InventoryType inventoryType) {
		final List<ItemToView> list = InventoryViewUtil.buildList(inventoryType, itemsAll);

		ItemModel dataModel = new ItemModel(list);
		JTable t=new JTable(dataModel);
		itemModelList.add(t);
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dataModel);  
		t.setRowSorter(sorter);  
		t.setPreferredScrollableViewportSize(new Dimension(550, 30));
		t.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		JScrollPane s = new JScrollPane(t);
		s.setBounds(0, 0, 563, 238);

		JPanel mainAll = new JPanel();
		mainAll.setLayout(null);
		mainAll.add(s);
		return mainAll;
	}

	/**
	 * 使用
	 */
	private void onUse() {
		// TODO Auto-generated method stub

	}

	/**
	 * 详情
	 */
	private void onDetails() {
		// TODO Auto-generated method stub

	}

	/**
	 * 出售
	 */
	private void onSell() {
		JTable jTable = itemModelList.get(selectTabIndex);
		int selectedRow = jTable.getSelectedRow();
		
		ItemModel model = (ItemModel) jTable.getModel();
		Item item = model.getItem(selectedRow);
		
		//先写成全卖，稍候做选卖出数量的面板
		CGItemSell msg = CGItemSell.newBuilder().setTemplateId(item.getTemplateId()).setAmount(item.getOverlap()).build();
		inventory.getHuman().sendMessage(msg);
	}

}
