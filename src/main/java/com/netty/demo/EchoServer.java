package com.netty.demo;

import java.nio.ByteOrder;

import com.netty.demo.codec.DelimiterHandle;
import com.netty.demo.codec.FixedLengthHandle;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.SwappedByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class EchoServer {
	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.DEBUG))
//					.childHandler(new DelimiterHandle(new EchoServerHandle(), "$_"))
					.childHandler(new FixedLengthHandle(new EchoServerHandle(), 20));
			// 绑定端口，等待同步
			ChannelFuture cf = server.bind(port).sync();
			// 同步等待监听通道关闭事件
			cf.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
//		try {
//			new EchoServer().bind(8888);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		String str = "68310068C93333111100027900000400D016";
		ByteBuf bb = Unpooled.buffer();
		bb.writeByte(0x68);  //帧头
		bb.writeShort(0x3100);//帧长度(数据区找度)
		bb.writeByte(0x68);//帧头
		bb.writeByte(0xC9);//控制码
		bb.writeInt(0x33331111);//地址域 A1 A2
		bb.writeByte(0x00); //地址域  A3         
		bb.writeByte(0x02);  //功能码 AFN
		bb.writeByte(0x79);  //帧序号
		bb.writeShort(0x0000); //DA
		bb.writeShort(0x0400); //DT
		//bb.writeByte(0x02); //数据单元
		bb.writeByte(0xD0);  //校验合  CS
		bb.writeByte(0x16);  //帧尾
		bb.writeMedium(0x000000);
//		System.out.println(ByteBufUtil.hexDump(bb));
//		System.out.println(ByteBufUtil.hexDump(bb.order(ByteOrder.BIG_ENDIAN)));
//		System.out.println(ByteBufUtil.hexDump(bb.order(ByteOrder.LITTLE_ENDIAN)));
//		int header = bb.readInt();
		SwappedByteBuf sbb = new SwappedByteBuf(bb);
		byte head = sbb.readByte();
		short len = (short) (sbb.readShort() >>> 2 & 0xffff);
		byte head2 = sbb.readByte();
		byte cs1 = 0;
		for(int i=0;i<len;i++){
			byte b = sbb.readByte();
			cs1 += b;
		}
//		cs1 = (byte)(cs1 % 256);
		System.out.println(cs1);
		byte cs2 = sbb.readByte();
		System.out.println(cs2);
		System.out.println(cs1 == cs2);
//		byte cn = sbb.readByte();
//		int add = sbb.readInt();
//		System.out.println(len);
//		System.out.println(Integer.toHexString(add));
//		System.out.println(( 00 & 0xFF) << 8);
//		System.out.println(0x31 >>> 2 & 0xffff);
//		System.out.println(s);
//		System.out.println(b);
//		System.out.println(ByteBufUtil.swapShort(s));
		
	}
}
