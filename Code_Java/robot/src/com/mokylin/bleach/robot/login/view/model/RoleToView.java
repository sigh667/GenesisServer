package com.mokylin.bleach.robot.login.view.model;

import com.mokylin.bleach.protobuf.PlayerMessage.Role;

public class RoleToView {

	private final Role role;
	
	public RoleToView(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}
	
	public String toString() {
		return role.getName();
	}
}
