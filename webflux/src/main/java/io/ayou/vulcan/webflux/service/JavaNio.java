package io.ayou.vulcan.webflux.service;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

/**
 * @author AYOU
 * @ClassName JavaNio
 */
public class JavaNio {
    public void serve(int port) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        ServerSocket serverSocket = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address);

        //打开Selector来处理Channel
        Selector selector = Selector.open();
        //将Serversocket注册到Selector以接受连接
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());

        for (; ; ) {
            try {
                //等待需要处理的新事件；阻塞将一直持续到下一个传入事件
                selector.select();
            } catch (IOException ex) {
                ex.printStackTrace();
                //handle exception
                break;
            }
            //获取所有接收事件的SelectionKey
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    //检查事件是否是一个新的已经就绪可以被接受的链接
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        //接受客户端并将他注册到选择器
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                        System.out.println("Accepted connection from " + client);
                    }
                    //检查套接字是否已经准备好写数据
                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        ByteBuffer rff = ByteBuffer.allocate(1024 * 1024);
                        //将数据写入已经连接的客户端
                        while (buffer.hasRemaining()) {
                            if (client.read(rff) == 0) {
                                break;
                            }
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                        System.out.println(getString(rff));
                        //关闭连接
                        client.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        //ignore on close
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        JavaNio jn = new JavaNio();
        try {
            jn.serve(9011);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static String getString(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            // charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
