package com.netty.demo.protocol.codec;

import java.util.HashMap;
import java.util.Map;

import com.netty.demo.codec.marshalling.MarshallingCodeCFactory;
import com.netty.demo.codec.marshalling.NettyMarshallingDecoder;
import com.netty.demo.protocol.Header;
import com.netty.demo.protocol.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder{
	private NettyMarshallingDecoder decoder;
	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
				initialBytesToStrip);
		this.decoder = MarshallingCodeCFactory.buildMarshallingDecoder();
	}
	public NettyMessageDecoder(int maxFrameLength,int lengthFieldOffset,int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		this.decoder = MarshallingCodeCFactory.buildMarshallingDecoder();
	}
	public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception{  
        ByteBuf frame = (ByteBuf)super.decode(ctx, in);
        
        if(frame == null){
            return null;
        }
//        byte[] bytes = new byte[frame.readableBytes()];
//        frame.readBytes(bytes);
        System.out.println(ctx.channel().remoteAddress().toString()+ ":" + ByteBufUtil.hexDump(frame));
//        frame.clear();
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());
          
        int size = frame.readInt();  
        if(size > 0){  
            Map<String, Object> attach = new HashMap<String, Object>(size);  
            int keySize = 0;  
            byte[] keyArray = null;  
            String key = null;  
            for(int i=0; i<size; i++){  
                keySize = frame.readInt();  
                keyArray = new byte[keySize];  
                ByteBuf temp = in.readBytes(keyArray);  
                key = new String(keyArray, "UTF-8");  
                attach.put(key, decoder.decode(ctx, temp));  
            }  
            key = null;  
            keyArray = null;  
            header.setAttachment(attach);  
        }   
        if(frame.readableBytes() > 0){  
            message.setBody(decoder.decode(ctx, frame));  
        }  
        message.setHeader(header);  
        return message;  
    }  
	
	

}
