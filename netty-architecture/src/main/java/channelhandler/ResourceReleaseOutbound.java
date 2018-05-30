package channelhandler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import java.lang.ref.Reference;

@Sharable
public class ResourceReleaseOutbound extends ChannelOutboundHandlerAdapter {

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {

    ReferenceCountUtil.release(msg); // release resource
    promise.setSuccess(); // notify ChannelPromise that data was handled

  }

}
