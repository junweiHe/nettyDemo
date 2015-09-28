package com.netty.demo.nio.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {
	public void bind(int port) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workerGroup)
				  .channel(NioServerSocketChannel.class)
				  .option(ChannelOption.SO_BACKLOG, 1024)
				  .childHandler(new ChildChannelHandle());
			//绑定端口，等待同步
			ChannelFuture cf = server.bind(port).sync();
			//同步等待监听通道关闭事件
			cf.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		
	}
	private class ChildChannelHandle extends ChannelInitializer<SocketChannel> {
		/**
		 * 初始化通道,添加解码器
		 */
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			
			arg0.pipeline().addLast(new LineBasedFrameDecoder(1024))
			.addLast(new StringDecoder())
			.addLast(new TimeServerHandle());
		}
	}
	
	public static void main(String[] args) {
		try {
			new TimeServer().bind(8888);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
