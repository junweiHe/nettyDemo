package com.netty.demo.nio.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable{
	
	private Selector selector;
	
	private ServerSocketChannel servChannel;
	
	private volatile boolean stop;
	/**
	 * 初始化多路复用器，绑定端口号
	 * @param port
	 */
	public MultiplexerTimeServer(int port){
		try {
			selector = Selector.open();
			servChannel = ServerSocketChannel.open();
			servChannel.configureBlocking(false);
			servChannel.socket().bind(new InetSocketAddress(port),1024);
			servChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("The time server is start in port : "+port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void stop(){
		stop = true;
	}

	@Override
	public void run() {
		while(!stop){
			try {
				selector.select(1000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				SelectionKey key = null;
				while(it.hasNext()){
					key = it.next();
					it.remove();
					try {
						handleInput(key);
					} catch (IOException e) {
						if(key != null){
							key.cancel();
							if(key.channel()!=null){
								key.channel().close();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		//多路复用器关闭后，所有注册在上面的channel和 Pipe等资源都会被自动去注册并关闭
		if(selector!=null){
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			//处理新接入的请求
			if(key.isAcceptable()){
				//Accept the new connection
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				//Add the connection to the selector
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				//Read the data
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if(readBytes > 0){
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes,"UTF-8");
					System.out.println("The time server receive order : " + body);
					
					String currentTime = "QUERY TIME ORDER".equals(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
					//回复信息
					doWrite(sc,currentTime);
				}else if(readBytes<0){
					key.cancel();
					sc.close();
				}else ;
			}
		}
	}
	private void doWrite(SocketChannel sc,String response) throws IOException{
		if(response != null && response.trim().length()>0){
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			sc.write(writeBuffer);
		}
	}
	
	public static void main(String[] args) {
		int port = 8888;
		
		MultiplexerTimeServer server = new MultiplexerTimeServer(port);
		
		new Thread(server,"NIO Time Server - 1").start();
	}
}
