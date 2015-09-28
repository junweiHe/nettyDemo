package com.netty.demo.protobuf;

import com.netty.demo.codec.protobuf.SubscribeProto.SubscribeReq;
import com.netty.demo.codec.protobuf.SubscribeProto.SubscribeResp;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ProtobufServerHandle extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		SubscribeReq req = (SubscribeReq) msg;
		System.out.println("Client request:[" + req + "]");
		ctx.write(resp(req.getId()));
	}

	private SubscribeResp resp(int id) {
		// SubscribeResp.Builder builder =
		return SubscribeResp.newBuilder().setId(id).setRespCode(1200)
				.setMessage("success.").build();
		// return builder.build();
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
