import io.netty.bootstrap.ServerBootstrap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

// same echo server, use echo client to call this
public class NettyServer {

  public void server(int port, boolean isBlocking) throws Exception {

    final ByteBuf buf = Unpooled.unreleasableBuffer(
        Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"))
    );

    // for Nio and OIO server, the only difference are two
    // 1. different event loop group
    EventLoopGroup group = getEventLoopGroup(isBlocking);

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(group)
          .channel(getClassTag(isBlocking)) // 2. different SocketChannelClass
          .localAddress(new InetSocketAddress(port))
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              socketChannel.pipeline().addLast(
                  new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                      ctx.writeAndFlush(buf.duplicate())
                          .addListener(ChannelFutureListener.CLOSE);
                    }
                  }
              );
            }
          });

      ChannelFuture f = b.bind().sync();
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }

  public static void main(String[] args) throws Exception {
    new NettyServer().server(10000, false);
  }

  private static Class getClassTag(boolean isBlocking) {
    return isBlocking ? OioServerSocketChannel.class : NioServerSocketChannel.class;
  }

  private static EventLoopGroup getEventLoopGroup(boolean isBlocking) {
    return isBlocking ? new OioEventLoopGroup() : new NioEventLoopGroup();
  }
}
