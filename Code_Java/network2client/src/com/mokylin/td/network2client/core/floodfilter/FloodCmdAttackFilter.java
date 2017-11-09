package com.mokylin.td.network2client.core.floodfilter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.mokylin.bleach.core.util.TimeUtils;

/**
 * 消息包攻击过滤器
 * @author baoliang.shen
 *
 */
public class FloodCmdAttackFilter extends ChannelInboundHandlerAdapter {

	/**IP地址*/
	private final String hostString;
	/**每分钟的包数限制*/
	private FloodCmdRecorder floodCmdRecorder60 = new FloodCmdRecorder(60*TimeUtils.SECOND, 1024);
	/**每2秒钟的包数限制*/
	private FloodCmdRecorder floodCmdRecorder2 = new FloodCmdRecorder(2*TimeUtils.SECOND, 64);

	public FloodCmdAttackFilter(String hostString) {
		this.hostString = hostString;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (FloodConstants.Inst.isOpenFloodFilter()
				&& !FloodFilterService.Inst.isInWhiteList(hostString)) {
			if (floodCmdRecorder2.onReceive()) {
				//超过流量限制，断开连接
				FloodFilterService.Inst.addForbiddenIp(hostString);
				ctx.disconnect();
				return;
			}

			if (floodCmdRecorder60.onReceive()) {
				//超过流量限制，断开连接
				FloodFilterService.Inst.addForbiddenIp(hostString);
				ctx.disconnect();
				return;
			}
		}

		ctx.fireChannelRead(msg);
	}
}
