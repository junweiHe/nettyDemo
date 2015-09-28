package com.netty.demo.protocol;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class LoginAuthRespHandler extends ChannelHandlerAdapter{
	private Map<String,Boolean> nodeCheck = new ConcurrentHashMap<String,Boolean>();
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//删除缓存，以便客户端重连
//		nodeCheck.remove(ctx.channel().remoteAddress().toString());
		System.out.println(cause);
		ctx.close();
	}

//	@Override
//	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		// TODO Auto-generated method stub
//		super.channelActive(ctx);
//	}
//
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//删除缓存，以便客户端重连
		nodeCheck.remove(ctx.channel().remoteAddress().toString());
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage message = (NettyMessage) msg;
		//握手请求
		if(message.getHeader()!=null && message.getHeader().getType() == MessageType.LOGING_REQ.value()){
			System.out.println("Login is OK");
			String nodeIndex = ctx.channel().remoteAddress().toString();
			System.out.println("nodeIndex:["+nodeIndex+"]");
			NettyMessage resp = null;
			//验证...
			if(nodeCheck.containsKey(nodeIndex)){
				resp = buildLoginResp((byte)-1);
			}else{
				InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
				String ip = address.getAddress().getHostAddress();
				//IP验证...
				
				resp = buildLoginResp((byte)0);
			}
			String body = (String) message.getBody();
			System.out.println("Recevied message body from client is" + body);
			ctx.writeAndFlush(resp);
		}else{
			//透传到下层Handler处理
			ctx.fireChannelRead(msg);
		}
	}
	private NettyMessage buildLoginResp(byte response){
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGING_RESP.value());
		message.setBody(response);
		message.setHeader(header);
		return message;
	}
}
