package com.ishow.noah.utils;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOTest {
    private static final String TAG = "yhy";
    protected SocketChannel socketChannel;

    protected Selector selector;

    public void socketClientDemo() {
        Log.i(TAG, "socketClientDemo: start");
        try {
            //建立通道
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("www.baidu.com", 80));

            selector = Selector.open();

            /*
             * 第二个参数指定该选择器感兴趣的事件
             * 事件主要有：
             * {@link SelectionKey}
             * SelectionKey.OP_CONNECT 连接就绪
             * SelectionKey.OP_READ 读就绪
             * SelectionKey.OP_WRITE 写就绪
             * SelectionKey.OP_ACCEPT 接受就绪
             */
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            //监听通道事件
            boolean flg = true;
            while (flg) {
                /**
                 * selector选择就绪的通道
                 * selector.select(); 没有就绪通道时会阻塞
                 * selector.selectNow(); 没有就绪通道直接立即返回0
                 * selector.select(long timeout); 没有就绪通道，超时后立即返回0
                 */
                int readyChannels = selector.selectNow();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                //遍历事件
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isConnectable()) {
                        Log.i(TAG, " 连接就绪");
                        SocketChannel channel = (SocketChannel) key.channel();
                        channel.configureBlocking(false);

                        if (channel.finishConnect()) {
                            write(channel);
                            //数据写入完成后，设置监听read
                            channel.register(selector, SelectionKey.OP_READ);
                        }
                    } else if (key.isReadable()) {
                        Log.i(TAG, " 读取就绪");
                        SocketChannel channel = (SocketChannel) key.channel();

                        channel.configureBlocking(false);

                        if (channel.isConnected()) {
                            read((SocketChannel) key.channel());
                        }
                        //表示完成一次读写操作，退出循环
                        flg = false;
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //最后关闭通道，关闭选择器
            try {
                if (socketChannel != null) {
                    socketChannel.close();
                }
                if (selector != null) {
                    selector.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 向通道内写入数据
     *
     * @param socketChannel
     * @throws IOException
     */
    private void write(SocketChannel socketChannel) throws IOException {
        if (socketChannel.isConnected()) {
            //写入
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("GET / HTTP/1.1\r\n");
            stringBuffer.append("Host: www.baidu.com\r\n");
            stringBuffer.append("\r\n");

            ByteBuffer writeBuffer = ByteBuffer.wrap(stringBuffer.toString().getBytes());

            Log.i(TAG, " 开始写入通道");
            Log.i(TAG, " " + stringBuffer.toString());
            while (writeBuffer.hasRemaining()) {
                socketChannel.write(writeBuffer);
            }
        }
    }

    /**
     * 从通道内读取数据
     *
     * @param socketChannel
     * @throws IOException
     */
    private void read(SocketChannel socketChannel) throws IOException {
        if (socketChannel.isConnected()) {
            //读取
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

            StringBuffer result = new StringBuffer();

            int read = socketChannel.read(readBuffer);

            while (read > 0) {

                result.append(new String(readBuffer.array(),
                        0,
                        read,
                        "UTF-8"));

                // 清空缓冲,继续写入
                readBuffer.clear();

                read = socketChannel.read(readBuffer);
            }

            Log.i(TAG, " 开始读取数据");
            Log.i(TAG, " result.toString()" + result.toString());
        }
    }
}
