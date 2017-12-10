package nio_example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 */
public class SocketChannelExample {

    public void nonBlocking() throws IOException {
        SocketChannel sc = SocketChannel.open();

        // when you set this to true, connect method may return before connection established
        sc.configureBlocking(true);
        sc.connect(new InetSocketAddress("http://", 80));

        while(! sc.finishConnect()) {
            // wait
        }

    }

}
