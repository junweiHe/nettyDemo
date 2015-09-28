package com.netty.demo.protocol;

public final class NettyMessage {
	/**
	 * 报文头
	 */
	private Header header;
	/**
	 * 报文体
	 */
	private Object body;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "NettyMessage[header=" + header + ",body=" + body + "]";
	}

}
