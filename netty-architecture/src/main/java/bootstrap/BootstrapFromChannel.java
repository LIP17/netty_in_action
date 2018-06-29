package bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/**
 * Suppose the server need to connect to third party, like proxy server
 */
// Listing 8.5
public class BootstrapFromChannel {

  public static void main(String[] args) {
    ServerBootstrap bootstrap = new ServerBootstrap();

    bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
        .channel(NioServerSocketChannel.class)
        .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
          ChannelFuture connectFuture;

          @Override
          public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                  @Override
                  protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                      ByteBuf byteBuf) throws Exception {
                    System.out.println("read in data");
                  }
                });
          }

          @Override
          protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf)
              throws Exception {
            if (connectFuture.isDone()) { /* do something */ }
          }
        });

    ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
    future.addListener(new ChannelFutureListener() {
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
          System.out.println("Server Bound");
        } else {
          System.err.println("Bind attempt failed");
          channelFuture.cause().printStackTrace();
        }
      }
    });
  }
}