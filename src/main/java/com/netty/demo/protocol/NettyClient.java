package com.netty.demo.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.netty.demo.protocol.codec.NettyMessageDecoder;
import com.netty.demo.protocol.codec.NettyMessageEncoder;

public class NettyClient {
	private ScheduledExecutorService executor = Executors
			.newScheduledThreadPool(1);

	private EventLoopGroup group = new NioEventLoopGroup();

	public void connect(final String host, final int port) throws Exception {
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel sc)
								throws Exception {
							sc.pipeline()
									.addLast(
											"messageDecoder",
											new NettyMessageDecoder(
													1024 * 1024, 4, 4,-8,0))
									.addLast("messageEncoder",
											new NettyMessageEncoder())
									.addLast("readTimeout",
											new ReadTimeoutHandler(50))
									.addLast("loginHandler",
											new LoginAuthReqHandler())
									.addLast("hearthBeatHandler",
											new HearthBeatReqHandler());
						}
					});
			ChannelFuture cf = b.connect(new InetSocketAddress(host, port),
					new InetSocketAddress("127.0.0.1", 7777)).sync();
			cf.channel().closeFuture().sync();
		} finally {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						//等待5秒发起重连
						TimeUnit.SECONDS.sleep(5);
						connect(host, port);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	public static void main(String[] args) {
		try {
			new NettyClient().connect("127.0.0.1", 8888);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
