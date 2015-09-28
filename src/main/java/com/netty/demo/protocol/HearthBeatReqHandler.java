package com.netty.demo.protocol;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class HearthBeatReqHandler extends ChannelHandlerAdapter {
	private volatile ScheduledFuture<?> schedule;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage message = (NettyMessage) msg;
		if (message.getHeader() != null
				&& message.getHeader().getType() == MessageType.LOGING_RESP
						.value()) {
			// 登陆成功，开始发心跳 5秒一次
			schedule = ctx.executor().scheduleAtFixedRate(
					new HearthBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
		} else if (message.getHeader() != null
				&& message.getHeader().getType() == MessageType.HEARTHBEAT_RESP
						.value()) {
			// 心跳应答
			System.out
					.println("Client recive server hearth beat message : --->"
							+ message);
		} else {
			// 其它消息透传到下层Handler
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		if (schedule != null) {
			schedule.cancel(true);
			schedule = null;
		}
		ctx.fireExceptionCaught(cause);
	}
	/**
	 * 心跳任务
	 * @author adminPC
	 *
	 */
	private class HearthBeatTask implements Runnable {
		private ChannelHandlerContext ctx;

		public HearthBeatTask(ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}

		@Override
		public void run() {
			NettyMessage hearthbeat = buildHearthBeat();
			System.out.println("Client send hearth beat recive to server: --->"
					+ hearthbeat);
			ctx.writeAndFlush(hearthbeat);
		}

		private NettyMessage buildHearthBeat() {
			NettyMessage hearthBeat = new NettyMessage();
			Header header = new Header();
			header.setType(MessageType.HEARTHBEAT_REQ.value());
			hearthBeat.setHeader(header);
			return hearthBeat;
		}
	}
}
