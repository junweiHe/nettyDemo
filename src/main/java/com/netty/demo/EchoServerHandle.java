package com.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandle extends ChannelHandlerAdapter{
	private int count = 0;
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println(cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
//		String body = (String) msg;
//		System.out.println("This is " + ++count +" times receive client ["+body + "]");
//		body+="$_";
//		ByteBuf buf = Unpooled.copiedBuffer(body.getBytes());
//		ctx.writeAndFlush(buf);
		System.out.println("Recive client[" + msg + "]");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.name() + " Active.");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.name() + " Close.");
		super.channelInactive(ctx);
	}
	
}
