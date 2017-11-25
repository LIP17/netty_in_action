package nio_example;

import jdk.management.resource.internal.inst.FileChannelImplRMHooks;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 */
public class FileChannelExample {

    public void write() throws IOException {

        FileChannel fc = new RandomAccessFile("./src/main/resources/TestReadFile", "rw").getChannel();
        String s = "write to file test";
        ByteBuffer buffer = ByteBuffer.allocate(48);

        buffer.clear();
        buffer.put(s.getBytes());

        while (buffer.hasRemaining()) {
            try {
                fc.write(buffer);
            } finally {
                fc.close();
            }
        }
    }

    /**
     * when you read file and position is set larger than the file size, you will get -1
     * when you write file starting larger than the file, you will get a file with file hole in it.
     *
     * */
    public void readFromPosition() throws IOException {
        FileChannel fc = new RandomAccessFile("./src/main/resources/TestReadFile", "rw").getChannel();

        long pos = fc.position();

        fc.position(pos + 1);

        ByteBuffer bf = ByteBuffer.allocate(48);

        int bytesRead = 0;

        while(bytesRead != -1) {
            fc.read(bf);

            bf.flip();

            System.out.println((char)bf.get());

            bf.clear();
        }

    }

    public static void main(String[] args) throws IOException {
//        new FileChannelExample().write();
        new FileChannelExample().readFromPosition();
    }

}
