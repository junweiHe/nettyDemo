package com.netty.demo.nio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandle extends ChannelHandlerAdapter{
	
//	private final ByteBuf message;
	private byte[] req ;
	private int count = 0;
	public TimeClientHandle(){
		req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
		
//		message = Unpooled.buffer(order.length);
//		message.writeBytes(order);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf message = null;
		for (int i = 0; i < 100; i++) {
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
//		ByteBuf res = (ByteBuf) msg;
//		byte[] bytes = new byte[res.readableBytes()];
//		res.readBytes(bytes);
		String body = (String) msg;//new String(bytes,"UTF-8");
		System.out.println("NOW TIME : " + body + "the count is " + ++count);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		if(count == 100)
			ctx.close();
	}
	
}
