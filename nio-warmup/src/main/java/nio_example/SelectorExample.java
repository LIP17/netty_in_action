package nio_example;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 */
public class SelectorExample {

    public void selectMethod() {

        Selector selector = null;
        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketCh = new ServerSocket().getChannel();
            // when using selector, channel should be non-blocking mode, file cha
            serverSocketCh.configureBlocking(false);
            SelectionKey sk = serverSocketCh.register(selector, SelectionKey.OP_READ);
        } catch (IOException ioe) {

        }

        while(true) {
            Set<SelectionKey> keySet = selector.selectedKeys();
            // get all selection keys registered
            Iterator<SelectionKey> it = keySet.iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();

                if (key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server
                } else if (key.isReadable()) {
                    // a channel is ready for reading
                } else if (key.isWritable()) {
                    // a channel is ready for writing
                }

                keySet.remove(key);
            }
        }
    }

}
