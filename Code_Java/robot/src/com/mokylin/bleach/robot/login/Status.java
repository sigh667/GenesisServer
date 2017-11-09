package com.mokylin.bleach.robot.login;

import com.mokylin.bleach.protobuf.MessageType.GCMessageType;

public enum Status {
	Init {
		@Override
		public boolean isCanProcess(int messageType) {
			if (messageType==GCMessageType.GC_CREATE_ROLE_VALUE
					|| messageType==GCMessageType.GC_ROLE_LIST_VALUE
					|| messageType==GCMessageType.GC_HUMAN_DETAIL_INFO_VALUE) {
				return true;
			}
			return false;
		}
	},
	CreatingRole {
		@Override
		public boolean isCanProcess(int messageType) {
			if (GCMessageType.GC_HUMAN_DETAIL_INFO_VALUE==messageType) {
				return true;
			}
			return false;
		}
	},
	SelectingRole {
		@Override
		public boolean isCanProcess(int messageType) {
			if (GCMessageType.GC_HUMAN_DETAIL_INFO_VALUE==messageType) {
				return true;
			}
			return false;
		}
	},
	Gaming {
		@Override
		public boolean isCanProcess(int messageType) {
			if (messageType==GCMessageType.GC_CREATE_ROLE_VALUE
					|| messageType==GCMessageType.GC_ROLE_LIST_VALUE
					|| GCMessageType.GC_HUMAN_DETAIL_INFO_VALUE==messageType) {
				return false;
			}
			return true;
		}
	},
	;

	public abstract boolean isCanProcess(int messageType);

}
