package channelhandler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

// LISTING 6.1, LISTING 6.4
@Sharable
public class ResourceReleasing extends ChannelInboundHandlerAdapter {

  /**
   * when chanelRead is override, channelRead has to handle release
   * memory associated with pooled ByteBuf instance.
   *
   * when extending SimpleChannelInboundHandler chanelRead0, no need to release resource
   *
   * */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ReferenceCountUtil.release(msg);
  }
}

