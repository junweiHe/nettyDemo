package com.netty.demo.protobuf;

import com.netty.demo.codec.protobuf.SubscribeProto.SubscribeReq;
import com.netty.demo.codec.protobuf.SubscribeProto.SubscribeResp;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ProtobufClientHandle extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i=1; i<= 10; i++){
			ctx.write(req(i));
		}
		ctx.flush();
	}

	private SubscribeReq req(int id) {
		return SubscribeReq.newBuilder()
				.setId(id)
				.setProductName("Test product")
				.setUserName("Hejunwei")
				.setAddress("Hang Zhou").build();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		SubscribeResp resp = (SubscribeResp) msg;
		System.out.println("Service response:["+resp+"]");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println(cause.getMessage());
		ctx.close();
	}

}
