package com.netty.demo.protocol;

import java.util.HashMap;
import java.util.Map;

public final class Header {
	/**
	 * 固定报文头
	 */
	private int crcCode = 0xabef0101;
	/**
	 * 报文长度
	 */
	private int length;
	/**
	 * 会话ID
	 */
	private long sessionID;
	/**
	 * 请求类型
	 */
	private byte type;
	/**
	 * 优先级
	 */
	private byte priority;
	/**
	 * 附件
	 */
	private Map<String, Object> attachment = new HashMap<String, Object>();

	public final int getCrcCode() {
		return crcCode;
	}

	public final void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}

	public final int getLength() {
		return length;
	}

	public final void setLength(int length) {
		this.length = length;
	}
	
	public long getSessionID() {
		return sessionID;
	}

	public void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}

	public final byte getType() {
		return type;
	}

	public final void setType(byte type) {
		this.type = type;
	}

	public final byte getPriority() {
		return priority;
	}

	public final void setPriority(byte priority) {
		this.priority = priority;
	}

	public final Map<String, Object> getAttachment() {
		return attachment;
	}

	public final void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}

	@Override
	public String toString() {
		return "Header[crcCode=" + crcCode + ",length=" + length
				+ ", sessionID=" + sessionID + ", type=" + type + ", priority="
				+ priority + ", attachment=" + attachment + "]";
	}
}
