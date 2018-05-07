package bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ByteProcessor;
import java.nio.channels.Channel;
import java.nio.charset.Charset;

public class LearningByteBuf {
  private static final String TEST_STRING = "abc";

  private void heapBufferPattern() {

    ByteBuf heapBuf = Unpooled.copiedBuffer(TEST_STRING.getBytes());
    if(heapBuf.hasArray()) { // when hasArray is false, access backing array will return UnsupportedOperationException()
      byte[] array = heapBuf.array();
      // TODO understand what is this calculation used for
      // as it is said it is the offset of the first byte
      int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
      int length = heapBuf.readableBytes();
      handleArray(array, offset, length);
    }
  }

  // buffer not lived on heap, need to be garbage collected manually
  // good for network I/O
  private void directBufferPattern() {
    ByteBuf directBuf = Unpooled.directBuffer();

    // if buf does not have backing array, then it is direct buffer
    if(!directBuf.hasArray()) {
      int length = directBuf.readableBytes();
      byte[] array = new byte[length];
      // because buf is not on array, so have to get a copy
      directBuf.getBytes(directBuf.readerIndex(), array);
      handleArray(array, 0, length);
    }
  }

  /**
   * 1. when there is only 1 buffer within composite buffer, hasArray() will work for that 1
   * instance, however, if it is more than 1, hasArray will always return false
   *
     * 2. Socket I/O is default optimized by compositeBuffer
   * */
  private void compositeBufferForHeaderAndBody() {
    CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
    ByteBuf headerBuf = null; // can be either direct or heap buffer
    ByteBuf bodyBuf = null; // can be either direct or heap buffer
    messageBuf.addComponents(headerBuf, bodyBuf);
    for(ByteBuf buf: messageBuf) {
      System.out.println(buf.toString());
    }
  }

  private void accessDataInsideCompositeByteBuf() {
    CompositeByteBuf compBuf = Unpooled.compositeBuffer();
    int length = compBuf.readableBytes();
    byte[] array = new byte[length];
    compBuf.getBytes(compBuf.readerIndex(), array);
    handleArray(array, 0, array.length);
  }

  public void resetReaderIndex() {
    ByteBuf b = Unpooled.buffer();
    b.clear(); // set reader and writer index to 0, no content is touched, more efficient than discardBytes()
    b.discardReadBytes(); // move reader index to 0, move writer index correspondingly, a array copy operation included
  }

  public void searchIndex() {
    ByteBuf b = Unpooled.buffer();
    int indexOfNull = b.forEachByte(ByteProcessor.FIND_NUL);
  }

  // The derived bytebuf will have new reader/writer index, but
  // share states with original one, so editing this is dangerous
  public void derivedBuffer() {
    ByteBuf b = Unpooled.buffer();
    b.duplicate();
    b.slice();
    b.slice(0, 1);
    Unpooled.unmodifiableBuffer(b);
  }

  public void sliceAByteBuf() {
    Charset utf8 = Charset.forName("UTF-8");
    ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
    ByteBuf sliced = buf.slice(0, 14);
    ByteBuf copy = buf.copy(0 ,14);
    System.out.println(sliced.toString(utf8));
    System.out.println(copy.toString(utf8));

    buf.setByte(0, (byte)'J');
    System.out.println(sliced.toString(utf8));
    System.out.println(copy.toString(utf8));
  }

  private void getByteBufAllocator() {
    ChannelHandlerContext ctx = null;
    ByteBufAllocator allocator = ctx.alloc();
  }

  private void referenceCounting() {
    // when the ref count is 0, it cannot be used

    ChannelHandlerContext ctx =null; // assuming this existing
    ByteBufAllocator bba = ctx.alloc();

    ByteBuf buf = bba.directBuffer();
    assert buf.refCnt() == 1; //

    buf.release(); // decrements the active ref to this object. At 0, object released and return true;
  }

  public static void main(String[] args) {
    new LearningByteBuf().sliceAByteBuf();
  }

  private void handleArray(byte[] array, int offset, int length) {}

}
