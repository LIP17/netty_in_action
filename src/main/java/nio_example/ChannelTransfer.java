package nio_example;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * transfer data from one channel to another
 */
public class ChannelTransfer {

    public void transfer() throws IOException {

        FileChannel in = new RandomAccessFile("./src/main/resources/TestReadFile", "rw").getChannel();
        FileChannel out = new RandomAccessFile("./src/main/resources/TransferDestination", "rw").getChannel();

        long position = 0;
        long count = in.size();

        out.transferFrom(in, 0, count);
    }

    public static void main(String[] args) throws IOException {
        new ChannelTransfer().transfer();
    }

}
