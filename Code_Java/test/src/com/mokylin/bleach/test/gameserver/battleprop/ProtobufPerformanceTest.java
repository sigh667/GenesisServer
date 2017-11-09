package com.mokylin.bleach.test.gameserver.battleprop;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mokylin.bleach.protobuf.HeroMessage.GCChangedProps;
import com.mokylin.bleach.protobuf.HeroMessage.PropData;

public class ProtobufPerformanceTest {

	public static void main(String[] args) throws InvalidProtocolBufferException {
		GCChangedProps changedProps = null;
		long startTime = System.currentTimeMillis();
		for(int num = 0;num < 5000;num ++) {
			GCChangedProps.Builder changedPropsB = GCChangedProps.newBuilder();
			PropData.Builder propB =  PropData.newBuilder();
			for(int i = 0;i < 10;i ++) {
				propB.setPropId(i);
				propB.setValue(12.34f);
				changedPropsB.addChangedProps(propB.build());
			}
			changedProps = changedPropsB.build();
		}
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
		
		startTime = System.currentTimeMillis();
		for(int i = 0;i < 5000;i ++) {
			byte[] bytes = changedProps.toByteArray();
			GCChangedProps.PARSER.parseFrom(bytes);
		}
		endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
		
		startTime = System.currentTimeMillis();
		for(int i = 0;i < 5000;i ++) {
			ChangedPropDataM changed = new ChangedPropDataM();
			for(int j = 0;j < 10;j ++) {
				PropDataM p = new PropDataM(); 
				p.setId(j);
				p.setValue(12.34f);
				changed.add(p);
			}
		}
		endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
	}
}
