package com.netty.demo.nio.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {
	public void connect(String host,int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap client = new Bootstrap();
			client.group(group)
				  .channel(NioSocketChannel.class)
				  .option(ChannelOption.TCP_NODELAY, true)
				  .handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel sc)
							throws Exception {
						sc.pipeline().addLast(new LineBasedFrameDecoder(1024))
						.addLast(new StringDecoder())
						.addLast(new TimeClientHandle());
					}
				});
			
			ChannelFuture cf = client.connect(host, port).sync();
			
			cf.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		try {
			new TimeClient().connect("127.0.0.1", 8888);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
