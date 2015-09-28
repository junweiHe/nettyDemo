package com.netty.demo.protocol;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ClientChannelHandler extends ChannelHandlerAdapter{
	
	private ChannelGroup channels;
	
	public ClientChannelHandler(ChannelGroup channels) {
		this.channels = channels;
	}
	
	/**
	 * 记录客户端连接
	 * 
	 */
//	private static Map<String, ChannelHandlerContext> channels = new ConcurrentHashMap<String, ChannelHandlerContext>();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = address.getAddress().getHostAddress();
		int port = address.getPort();
//		channels.put(ip+"#"+port, ctx);
//		channels.add(ctx.channel());
		
		System.out.println("目前连接数:["+channels.size()+"]");
//		super.channelActive(ctx);
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
//		String ip = address.getAddress().getHostAddress();
//		int port = address.getPort();
//		channels.remove(ip+"#"+port);
//		System.out.println("目前连接数:["+channels.size()+"]");
////		super.channelInactive(ctx);
		channels.remove(ctx.channel());
		ctx.fireChannelInactive();
	}
	
	

//	public ChannelHandlerContext getChannel(String ip,int port){
//		return channels.get(ip+"#"+port);
//	}
}
