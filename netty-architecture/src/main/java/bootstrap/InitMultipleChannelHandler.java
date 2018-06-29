package bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import java.net.InetSocketAddress;

// Listing 8.6
public class InitMultipleChannelHandler {

  public static void main(String[] args) throws Exception {

    ServerBootstrap bootstrap = new ServerBootstrap();

    bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializerImpl());

    ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
    future.sync();
  }
}

/**
 * suggested way to add multiple channel handler.
 * */
final class ChannelInitializerImpl extends ChannelInitializer<io.netty.channel.Channel> {
  @Override
  protected void initChannel(Channel channel) throws Exception {
    ChannelPipeline cp = channel.pipeline();
    cp.addLast(new HttpClientCodec());
    cp.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
  }
}
