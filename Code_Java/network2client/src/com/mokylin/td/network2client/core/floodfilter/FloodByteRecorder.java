package com.mokylin.td.network2client.core.floodfilter;

import io.netty.buffer.ByteBuf;

import java.util.LinkedList;
import java.util.Queue;

import com.mokylin.bleach.core.time.TimeService;

/**
 * 流量记录器
 * @author baoliang.shen
 *
 */
public class FloodByteRecorder {

	/**限制在多少时间内，单位：毫秒*/
	private final long limitTime;
	/**在指定时间内限制多少流量，单位：字节*/
	private final int limitSize;

	/**一段时间内收到的流量记录*/
	private Queue<Node> queue = new LinkedList<>();
	/**一一段时间内累计的流量，单位：字节*/
	private int totalSize = 0;

	FloodByteRecorder(long limitTime, int limitSize) {
		this.limitTime = limitTime;
		this.limitSize = limitSize;
	}

	/**
	 * 是否超过限制
	 * @return
	 */
	public boolean isOverrun() {
		if (totalSize > limitSize) {
			return true;
		}
		return false;
	}

	/**
	 * 每当收到一个包的时候调用
	 * @param data
	 */
	public void onReceive(ByteBuf data) {
		long now = TimeService.Inst.now();
		checkOldBytes(now);

		final int bytesSize = data.readableBytes();
		add(new Node(now, bytesSize));
	}

	/**
	 * 添加新节点，并更新总字节数
	 * @param node
	 */
	private void add(Node node) {
		queue.offer(node);
		totalSize += node.size;
	}
	/**
	 * 删除头节点，并更新总字节数
	 */
	private void remove() {
		final Node head = queue.poll();
		totalSize -= head.size;
	}

	/**
	 * 检查之前收到的消息包，是否有过期的<p>
	 * 如果有，则删掉，并更新总字节数
	 * @param now 当前时间
	 */
	private void checkOldBytes(long now) {
		if (queue.isEmpty())
			return;

		Node head = queue.peek();
		while (now-head.time >= limitTime) {
			remove();

			if (queue.isEmpty()) {
				break;
			}
			head = queue.peek();
		}
	}

	private class Node {
		/**接收到消息包时的时间点*/
		final long time;
		/**消息包的大小*/
		final int size;

		public Node(long time, int size) {
			this.time = time;
			this.size = size;
		}
	}
}
