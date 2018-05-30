package channelhandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {

  // LISTING 6.13
  public void writeWithChannelFuture(Channel channel, String msg) {
    ChannelFuture future = channel.write(msg);
    future.addListener(new ChannelFutureListener() {
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if(!channelFuture.isSuccess()) {
          if(!channelFuture.isSuccess()) {
            channelFuture.cause().printStackTrace();
            channelFuture.channel().close();
          }
        }
      }
    });
  }

  // LISTING 6.14, which is same operation as the one in LISTING 6.13
  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
      throws Exception {
    promise.addListener(new ChannelFutureListener() {
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if(!channelFuture.isSuccess()) {
          channelFuture.cause().printStackTrace();
          channelFuture.channel().close();
        }
      }
    });
  }
}
