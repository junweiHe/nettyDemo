package com.netty.demo.codec;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class FixedLengthHandle extends ChannelInitializer<SocketChannel>{
	private ChannelHandlerAdapter handler;
	private int length;
	public FixedLengthHandle(ChannelHandlerAdapter handler,int length){
		this.handler = handler;
		this.length = length;
	}
	
	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
//		ByteBuf buf = Unpooled.copiedBuffer(limiter.getBytes());
		arg0.pipeline().addLast(new FixedLengthFrameDecoder(length))
		.addLast(new StringDecoder())
		.addLast(handler);
	}

}
