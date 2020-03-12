package cn.xhzren.test.rocketmqtest;

import io.netty.buffer.ByteBuf;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

public class LookLogFile {

    static ByteBuffer buffer = ByteBuffer.allocate(1024 * 10000);
    static byte[] bytes = new byte[1024 * 10000];
    static String name = "C:\\Users\\admin\\Downloads\\catalina.out";

    public static void main(String[] args) throws Exception {
        FileChannel channel = new RandomAccessFile(name, "r").getChannel();

        channel.read(buffer, channel.size() - 1024*10000);
        buffer.flip();
        buffer.get(bytes);

        File file = new File("C:\\Users\\admin\\Downloads\\Compressed\\f\\test.out");
        file.createNewFile();
        FileChannel newFile = new RandomAccessFile(file, "rw").getChannel();
        newFile.write(ByteBuffer.wrap(bytes));
    }
}
