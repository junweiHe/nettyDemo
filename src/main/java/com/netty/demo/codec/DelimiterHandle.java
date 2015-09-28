package com.netty.demo.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class DelimiterHandle extends ChannelInitializer<SocketChannel>{
	
	private ChannelHandlerAdapter handler;
	private String limiter;
	public DelimiterHandle(ChannelHandlerAdapter handler,String limiter){
		this.handler = handler;
		this.limiter = limiter;
	}
	
	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
		ByteBuf buf = Unpooled.copiedBuffer(limiter.getBytes());
		arg0.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,buf))
		.addLast(new StringDecoder())
		.addLast(handler);
	}

}
