package lip.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * keep business logic and network logic separately
 */
@ChannelHandler.Sharable // safely shareable to multiple channels
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

  /*
   *  compare with SimpleChannelInboundHandler, when channelRead return, the underlying
    *  echo to client may not ended, so a channelReadComplete method is needed
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf in = (ByteBuf) msg;
    System.out.println("Server Received: " + in.toString(CharsetUtil.UTF_8));
    ctx.write(in); // write the received message to sender without flushing the outbounded messages.
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    // flushing pending messages to the remote peer and close channel
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
