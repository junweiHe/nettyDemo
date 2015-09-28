package com.netty.demo.protocol;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class LoginAuthReqHandler extends ChannelHandlerAdapter{

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		
//		super.exceptionCaught(ctx, cause);
		System.out.println(cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(buildLoginReq());
	}
	
	private NettyMessage buildLoginReq(){
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGING_REQ.value());
		message.setHeader(header);
		message.setBody("This is login request.");
		return message;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage message = (NettyMessage)msg;  
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGING_RESP.value()){
        	byte body = (byte) message.getBody();
        	if(body != (byte)0){
        		System.out.println("握手失败,关闭连接");
        		ctx.close();
        	}else{
        		System.out.println("Received from server response");
        		ctx.fireChannelRead(msg); 
        	}
        } else 
        	ctx.fireChannelRead(msg); 
	}
}
