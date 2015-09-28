package com.netty.demo.protobuf;

import com.netty.demo.codec.protobuf.SubscribeProto.SubscribeResp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ProtobufClient {
	public void connect(String host, int port) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel arg0)
								throws Exception {
							arg0.pipeline().addLast(new ProtobufVarint32FrameDecoder())
							.addLast(new ProtobufDecoder(SubscribeResp.getDefaultInstance()))
							.addLast(new ProtobufVarint32LengthFieldPrepender())
							.addLast(new ProtobufEncoder())
							.addLast(new ProtobufClientHandle());
						}
					});
			ChannelFuture cf = b.connect(host, port).sync();
			cf.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		try {
			new ProtobufClient().connect("127.0.0.1", 8888);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
