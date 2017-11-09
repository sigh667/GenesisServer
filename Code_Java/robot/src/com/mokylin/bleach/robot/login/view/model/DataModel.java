package com.mokylin.bleach.robot.login.view.model;

import java.util.List;

import javax.swing.AbstractListModel;

public class DataModel extends AbstractListModel<RoleToView> {
	
	/***/
	private static final long serialVersionUID = 1L;
	
	private final List<RoleToView> rolesList;
	
	
	public DataModel(List<RoleToView> rolesList) {
		this.rolesList = rolesList;
	}

	@Override
	public int getSize() {
		return rolesList.size();
	}

	@Override
	public RoleToView getElementAt(int index) {
		return rolesList.get(index);
	}

}
