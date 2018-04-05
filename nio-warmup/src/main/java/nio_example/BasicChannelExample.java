package nio_example;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * Useful channels: FileChannel(file), DatagramChannel(UDP), SocketChannel(TCP), ServerSocketChannel(TCP server)
 *
 */
public class BasicChannelExample {

    private void read() throws IOException {
        RandomAccessFile randomFile = new RandomAccessFile("./src/main/resources/TestReadFile", "rw");

        FileChannel fc = randomFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);

        try {
            int bytesReads = 0;
            while (bytesReads != -1) {
                System.out.println("Reads: " + bytesReads);
                buf.flip(); // make buffer ready for read from write mode

                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                buf.clear(); // make buffer ready for writing, will clear out everything in buf
                // buf.compact will only clear data already read.

                bytesReads = fc.read(buf);
            }
        } catch (IOException ioe) {

        }

        randomFile.close();
    }

    public static void main(String[] args) throws IOException {
        new BasicChannelExample().read();
    }
}
