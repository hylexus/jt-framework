//package io.github.hylexus.jt.utils;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.ByteBufAllocator;
//import io.netty.buffer.CompositeByteBuf;
//import io.netty.util.internal.PlatformDependent;
//
///**
// * @author hylexus
// * @see <a href="https://github.com/netty/netty/issues/6431">https://github.com/netty/netty/issues/6431</a>
// */
//public interface DelegateByteBufAllocator extends ByteBufAllocator {
//
//    DelegateByteBufAllocator DEFAULT = new DefaultDelegateByteBufAllocator();
//
//    int DEFAULT_MAX_COMPONENTS = 16;
//
//    ByteBufAllocator delegate();
//
//    @Override
//    CompositeByteBuf compositeBuffer();
//
//    @Override
//    CompositeByteBuf compositeBuffer(int maxNumComponents);
//
//    @Override
//    default CompositeByteBuf compositeHeapBuffer() {
//        return delegate().compositeHeapBuffer();
//    }
//
//    @Override
//    default CompositeByteBuf compositeHeapBuffer(int maxNumComponents) {
//        return delegate().compositeHeapBuffer(maxNumComponents);
//    }
//
//    @Override
//    default CompositeByteBuf compositeDirectBuffer() {
//        return delegate().compositeDirectBuffer();
//    }
//
//    @Override
//    default CompositeByteBuf compositeDirectBuffer(int maxNumComponents) {
//        return delegate().compositeDirectBuffer(maxNumComponents);
//    }
//
//    @Override
//    default ByteBuf buffer() {
//        return delegate().buffer();
//    }
//
//    @Override
//    default ByteBuf buffer(int initialCapacity) {
//        return delegate().buffer(initialCapacity);
//    }
//
//    @Override
//    default ByteBuf buffer(int initialCapacity, int maxCapacity) {
//        return delegate().buffer(initialCapacity, maxCapacity);
//    }
//
//    @Override
//    default ByteBuf ioBuffer() {
//        return delegate().ioBuffer();
//    }
//
//    @Override
//    default ByteBuf ioBuffer(int initialCapacity) {
//        return delegate().ioBuffer(initialCapacity);
//    }
//
//    @Override
//    default ByteBuf ioBuffer(int initialCapacity, int maxCapacity) {
//        return delegate().ioBuffer(initialCapacity, maxCapacity);
//    }
//
//    @Override
//    default ByteBuf heapBuffer() {
//        return delegate().heapBuffer();
//    }
//
//    @Override
//    default ByteBuf heapBuffer(int initialCapacity) {
//        return delegate().heapBuffer(initialCapacity);
//    }
//
//    @Override
//    default ByteBuf heapBuffer(int initialCapacity, int maxCapacity) {
//        return delegate().heapBuffer(initialCapacity, maxCapacity);
//    }
//
//    @Override
//    default ByteBuf directBuffer() {
//        return delegate().directBuffer();
//    }
//
//    @Override
//    default ByteBuf directBuffer(int initialCapacity) {
//        return delegate().directBuffer(initialCapacity);
//    }
//
//    @Override
//    default ByteBuf directBuffer(int initialCapacity, int maxCapacity) {
//        return delegate().directBuffer(initialCapacity, maxCapacity);
//    }
//
//    @Override
//    default boolean isDirectBufferPooled() {
//        return delegate().isDirectBufferPooled();
//    }
//
//    @Override
//    default int calculateNewCapacity(int minNewCapacity, int maxCapacity) {
//        return delegate().calculateNewCapacity(minNewCapacity, maxCapacity);
//    }
//
//    class DefaultDelegateByteBufAllocator implements DelegateByteBufAllocator {
//        private final boolean directByDefault;
//        private final ByteBufAllocator allocator;
//
//        public DefaultDelegateByteBufAllocator() {
//            this(ByteBufAllocator.DEFAULT, false);
//        }
//
//        public DefaultDelegateByteBufAllocator(ByteBufAllocator allocator) {
//            this(allocator, false);
//        }
//
//        public DefaultDelegateByteBufAllocator(ByteBufAllocator allocator, boolean preferDirect) {
//            this.directByDefault = preferDirect && PlatformDependent.hasUnsafe();
//            this.allocator = allocator;
//        }
//
//        @Override
//        public ByteBufAllocator delegate() {
//            return this.allocator;
//        }
//
//        @Override
//        public CompositeByteBuf compositeBuffer() {
//            return new CompositeByteBuf(delegate(), directByDefault, DEFAULT_MAX_COMPONENTS);
//        }
//
//        @Override
//        public CompositeByteBuf compositeBuffer(int maxNumComponents) {
//            return new CompositeByteBuf(delegate(), directByDefault, maxNumComponents);
//        }
//
//    }
//}
