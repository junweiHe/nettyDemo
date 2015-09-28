package com.netty.demo.nio.netty;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeServerHandle extends ChannelHandlerAdapter{
	private int count = 0;
	/**
	 * 异常处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//cause.printStackTrace();
		System.out.println(cause.getMessage());
		ctx.close();
	}
	
	/**
	 * 通道接通
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.name() + " active");
		super.channelActive(ctx);
	}
	
	/**
	 * 通道关闭
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.name() + " close");
		super.channelInactive(ctx);
	}
	
	/**
	 * 读取通道数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
//		ByteBuf bb = (ByteBuf) msg;
//		byte[] bytes = new byte[bb.readableBytes()];
//		bb.readBytes(bytes);
		String body = (String) msg;//new String(bytes,"UTF-8");
		System.out.println("The time server recive order：" + body + "count:" + ++count);
		String currentTime = "QUERY TIME ORDER".equals(body)?new Date(System.currentTimeMillis()).toString():"BADE ORDER";
		ByteBuf buf = Unpooled.copiedBuffer((currentTime+System.getProperty("line.separator")).getBytes());
//		ctx.write(buf);
		ctx.writeAndFlush(buf);
	}
	
	/**
	 * 数据读取完闭后flush通道内的数据
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
}
