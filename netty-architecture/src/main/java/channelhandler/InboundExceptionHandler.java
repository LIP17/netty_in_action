package channelhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class InboundExceptionHandler extends ChannelInboundHandlerAdapter {

  /**
   * 1. Default impl of exceptionCaught forwards the current exception to the next handler
   * 2. if exception arrive the end of pipeline, it is logged as unhandled
   * 3. define custom exception handling by overriding exceptionCaught()
   * */

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
