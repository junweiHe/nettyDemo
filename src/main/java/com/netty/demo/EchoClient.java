package com.netty.demo;

import com.netty.demo.codec.DelimiterHandle;
import com.netty.demo.codec.FixedLengthHandle;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
	public void connect(String host,int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap client = new Bootstrap();
			client.group(group)
				  .channel(NioSocketChannel.class)
				  .option(ChannelOption.TCP_NODELAY, true)
//				  .handler(new DelimiterHandle(new EchoClientHandle(), "$_"))
				  .handler(new FixedLengthHandle(new EchoClientHandle(), 20));
			
			ChannelFuture cf = client.connect(host, port).sync();
			
			cf.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		try {
			new EchoClient().connect("127.0.0.1", 8888);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
