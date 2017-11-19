package nio_example;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ScatterGather {
    /**
     * you can use 1 channel to read file and write into multiple buffer,
     * when you read it, the channel will try to fill first buffer until full and then next buffer.
     * it is useful when you try to do different thing on partial data.
     *
     * Notice if the buffer size not fit your data, first line data may be written in second line buffer.
     * which is dangerous!
     * */
    private void read() throws IOException {
        ByteBuffer firstLine = ByteBuffer.allocate(12); // read in first line
        ByteBuffer secondLine = ByteBuffer.allocate(20); // handle eveything else

        ByteBuffer[] scatterBuf = {firstLine, secondLine};

        FileChannel fc = new RandomAccessFile("./src/main/resources/TestReadFile", "rw").getChannel();

        fc.read(scatterBuf);

        try {
            long bytesRead = 0;
            while(bytesRead != -1) {

                firstLine.flip();

                while(firstLine.hasRemaining()) {
                    System.out.print((char)firstLine.get());
                }

                firstLine.clear();
                bytesRead = fc.read(scatterBuf);
            }


        } catch (IOException ioe) {

        }

    }

    private void write() throws IOException {
        FileChannel fc = new RandomAccessFile("./src/main/resources/GatherWriteTest", "rw").getChannel();
        ByteBuffer firstLine = ByteBuffer.allocate(10);
        ByteBuffer secondLine = ByteBuffer.allocate(10);

        ByteBuffer[] gatherBuffers = {firstLine, secondLine};

        firstLine.put("firstline".getBytes());
        secondLine.put("secondline".getBytes());

        fc.write(gatherBuffers);
    }

    public static void main(String[] args) throws IOException {
        new ScatterGather().read();
        new ScatterGather().write();
    }


}
