package nio_example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 */
public class ServerSocketChannelExample {

    public void nonBlockingAccept() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();

        ssc.socket().bind(new InetSocketAddress(9999));
        ssc.configureBlocking(false);

        while(true) {
            SocketChannel sc = ssc.accept();

            if(sc != null) {
                // start working on the connection
            }
        }
    }

}
