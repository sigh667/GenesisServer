package com.mokylin.bleach.test.gameserver.net;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteOrder;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.mokylin.bleach.core.net.NettyNetworkLayer;
import com.mokylin.td.network2client.core.channel.ChannelInitializerImpl;
import com.mokylin.td.network2client.core.channel.IChannelListener;
import com.mokylin.td.network2client.core.handle.ServerIoHandler;

public class NettyNetworkLayerTest {
	
	private static final int ID = 100;
	private static final String NAME = "LLGHFF";
	private static final long OTHER = 19334;

	@Test
	public void network_layer_should_received_message() {
		// TODO 这个测试暂时是跑不过的
		try {
			CountDownLatch latch = new CountDownLatch(1);
			
			final MockMessageProcess mp = new MockMessageProcess(latch);
			final IChannelListener rs = new RecordSessionImpl();
			
//			NetInfo netInfoToClient = config.netInfoToClient;
//			AgentClientMessageHandler mp = new AgentClientMessageHandler();
//			AgentServerChannelListener rs = new AgentServerChannelListener();
			final ServerIoHandler handler = new ServerIoHandler(mp, rs);
			ChannelHandler childHandler = new ChannelInitializerImpl(handler, MockDecoder.class);
			NettyNetworkLayer.configNet("127.0.0.1", 8781).start(handler);
			sendMessageToNetworkLayer();
			
			if(latch.await(3, TimeUnit.SECONDS)){
				assertThat(mp.id, is(ID));
				assertThat(mp.name, is(NAME));
				assertThat(mp.other, is(OTHER));
				assertThat(mp.count, is(1));
			}else{
				fail();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void sendMessageToNetworkLayer() throws IOException {
		MockCGMessage.CGTEST.Builder builder = MockCGMessage.CGTEST.newBuilder();
		builder.setId(ID);
		builder.setName(NAME);
		builder.setOther(OTHER);
		
		byte[] content = builder.build().toByteArray();
		
		ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
		buf.order(ByteOrder.LITTLE_ENDIAN).writeShort(content.length).writeShort(1).writeBytes(content);
					
		Socket so = new Socket();
		so.connect(new InetSocketAddress("127.0.0.1", 8781));
		so.getOutputStream().write(buf.array());
		so.close();
	}

}
