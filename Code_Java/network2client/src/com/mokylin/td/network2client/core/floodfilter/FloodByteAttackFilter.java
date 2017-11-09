package com.mokylin.td.network2client.core.floodfilter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.mokylin.bleach.core.util.TimeUtils;

/**
 * 流量攻击过滤器
 * @author baoliang.shen
 *
 */
public class FloodByteAttackFilter extends ChannelInboundHandlerAdapter {

	/**IP地址*/
	private final String hostString;
	/**每分钟的流量记录器*/
	private FloodByteRecorder floodByteRecorder60 = new FloodByteRecorder(60*TimeUtils.SECOND, 128*1024);
	/**每2秒的流量记录器*/
	private FloodByteRecorder floodByteRecorder2 = new FloodByteRecorder(2*TimeUtils.SECOND, 48*1024);

	public FloodByteAttackFilter(String hostString) {
		this.hostString = hostString;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (FloodConstants.Inst.isOpenFloodFilter()
				&& !FloodFilterService.Inst.isInWhiteList(hostString)
				&& msg instanceof ByteBuf) {
			ByteBuf data = (ByteBuf) msg;

			floodByteRecorder2.onReceive(data);
			if (floodByteRecorder2.isOverrun()) {
				//超过流量限制，断开连接
				FloodFilterService.Inst.addForbiddenIp(hostString);
				ctx.disconnect();
				return;
			}

			floodByteRecorder60.onReceive(data);
			if (floodByteRecorder60.isOverrun()) {
				//超过流量限制，断开连接
				FloodFilterService.Inst.addForbiddenIp(hostString);
				ctx.disconnect();
				return;
			}
		}

		ctx.fireChannelRead(msg);
	}

}
