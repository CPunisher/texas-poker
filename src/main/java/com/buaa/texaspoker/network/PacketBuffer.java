package com.buaa.texaspoker.network;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;
import com.buaa.texaspoker.entity.player.PlayerProfile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 为了方便的读写自定义数据的{@link ByteBuf}的装饰器
 * @author CPunisher
 * @see ByteBuf
 */
public class PacketBuffer extends ByteBuf {

    /**
     * 被装饰的{@link ByteBuf}
     */
    private final ByteBuf byteBuf;

    /**
     * 通过装饰{@link ByteBuf}形成对他的扩展
     * @param byteBuf 被装饰的{@link ByteBuf}
     */
    public PacketBuffer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    /**
     * 序列化{@link Poker}对象，拆分为2个<code>int</code>数据写入字节缓冲
     * @param poker 写入的扑克牌对象
     */
    public void writePoker(Poker poker) {
        this.writeInt(poker.getPoint());
        this.writeInt(poker.getPokerType().ordinal());
    }

    /**
     * 反序列化{@link Poker}对象，读入2个<code>int</code>构造
     * @return 读取的扑克牌对象
     */
    public Poker readPoker() {
        return new Poker(this.readInt(), PokerType.values()[this.readInt()]);
    }

    /**
     * 序列化{@link PlayerProfile}对象，拆分为2个{@link String}
     * @param profile 写入的扑克牌对象
     */
    public void writeProfile(PlayerProfile profile) {
        this.writeString(profile.getUuid().toString());
        this.writeString(profile.getName());
    }

    /**
     * 反序列化{@link PlayerProfile}，读取2个{@link String}转化为{@link UUID}和玩家名称
     * @return 读取的玩家信息类对象
     */
    public PlayerProfile readProfile() {
        return new PlayerProfile(UUID.fromString(this.readString()), this.readString());
    }

    /**
     * 序列化{@link String}对象，写入字符串的长度，再以<code>UTF-8</code>编码为字节写入
     * @param str 写入的字符串对象
     */
    public void writeString(String str) {
        this.writeInt(str.length());
        this.writeCharSequence(str, StandardCharsets.UTF_8);
    }

    /**
     * 反序列化{@link String}对象，读取字符串长度，再以<code>UTF-8</code>编码构造
     * @return 读取的字符串对象
     */
    public String readString() {
        int length = this.readInt();
        return (String) this.readCharSequence(length, StandardCharsets.UTF_8);
    }

    @Override
    public int capacity() {
        return byteBuf.capacity();
    }

    @Override
    public ByteBuf capacity(int i) {
        return byteBuf.capacity(i);
    }

    @Override
    public int maxCapacity() {
        return byteBuf.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return byteBuf.alloc();
    }

    @Override
    public ByteOrder order() {
        return byteBuf.order();
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        return byteBuf.order(byteOrder);
    }

    @Override
    public ByteBuf unwrap() {
        return byteBuf.unwrap();
    }

    @Override
    public boolean isDirect() {
        return byteBuf.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return byteBuf.isReadOnly();
    }

    @Override
    public ByteBuf asReadOnly() {
        return byteBuf.asReadOnly();
    }

    @Override
    public int readerIndex() {
        return byteBuf.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int i) {
        return byteBuf.readerIndex(i);
    }

    @Override
    public int writerIndex() {
        return byteBuf.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int i) {
        return byteBuf.writerIndex(i);
    }

    @Override
    public ByteBuf setIndex(int i, int i1) {
        return byteBuf.setIndex(i, i1);
    }

    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public int writableBytes() {
        return byteBuf.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return byteBuf.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return byteBuf.isReadable();
    }

    @Override
    public boolean isReadable(int i) {
        return byteBuf.isReadable(i);
    }

    @Override
    public boolean isWritable() {
        return byteBuf.isWritable();
    }

    @Override
    public boolean isWritable(int i) {
        return byteBuf.isWritable(i);
    }

    @Override
    public ByteBuf clear() {
        return byteBuf.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return byteBuf.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return byteBuf.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return byteBuf.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return byteBuf.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return byteBuf.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return byteBuf.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int i) {
        return byteBuf.ensureWritable(i);
    }

    @Override
    public int ensureWritable(int i, boolean b) {
        return byteBuf.ensureWritable(i, b);
    }

    @Override
    public boolean getBoolean(int i) {
        return byteBuf.getBoolean(i);
    }

    @Override
    public byte getByte(int i) {
        return byteBuf.getByte(i);
    }

    @Override
    public short getUnsignedByte(int i) {
        return byteBuf.getUnsignedByte(i);
    }

    @Override
    public short getShort(int i) {
        return byteBuf.getShort(i);
    }

    @Override
    public short getShortLE(int i) {
        return byteBuf.getShortLE(i);
    }

    @Override
    public int getUnsignedShort(int i) {
        return byteBuf.getUnsignedShort(i);
    }

    @Override
    public int getUnsignedShortLE(int i) {
        return byteBuf.getUnsignedShortLE(i);
    }

    @Override
    public int getMedium(int i) {
        return byteBuf.getMedium(i);
    }

    @Override
    public int getMediumLE(int i) {
        return byteBuf.getMediumLE(i);
    }

    @Override
    public int getUnsignedMedium(int i) {
        return byteBuf.getUnsignedMedium(i);
    }

    @Override
    public int getUnsignedMediumLE(int i) {
        return byteBuf.getUnsignedMediumLE(i);
    }

    @Override
    public int getInt(int i) {
        return byteBuf.getInt(i);
    }

    @Override
    public int getIntLE(int i) {
        return byteBuf.getIntLE(i);
    }

    @Override
    public long getUnsignedInt(int i) {
        return byteBuf.getUnsignedInt(i);
    }

    @Override
    public long getUnsignedIntLE(int i) {
        return byteBuf.getUnsignedIntLE(i);
    }

    @Override
    public long getLong(int i) {
        return byteBuf.getLong(i);
    }

    @Override
    public long getLongLE(int i) {
        return byteBuf.getLongLE(i);
    }

    @Override
    public char getChar(int i) {
        return byteBuf.getChar(i);
    }

    @Override
    public float getFloat(int i) {
        return byteBuf.getFloat(i);
    }

    @Override
    public double getDouble(int i) {
        return byteBuf.getDouble(i);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf) {
        return this.byteBuf.getBytes(i, byteBuf);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf, int i1) {
        return this.byteBuf.getBytes(i, byteBuf, i1);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf, int i1, int i2) {
        return this.byteBuf.getBytes(i, byteBuf, i1, i2);
    }

    @Override
    public ByteBuf getBytes(int i, byte[] bytes) {
        return this.byteBuf.getBytes(i, bytes);
    }

    @Override
    public ByteBuf getBytes(int i, byte[] bytes, int i1, int i2) {
        return this.byteBuf.getBytes(i, bytes, i1, i2);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuffer byteBuffer) {
        return this.byteBuf.getBytes(i, byteBuffer);
    }

    @Override
    public ByteBuf getBytes(int i, OutputStream outputStream, int i1) throws IOException {
        return this.byteBuf.getBytes(i, outputStream, i1);
    }

    @Override
    public int getBytes(int i, GatheringByteChannel gatheringByteChannel, int i1) throws IOException {
        return this.byteBuf.getBytes(i, gatheringByteChannel, i1);
    }

    @Override
    public int getBytes(int i, FileChannel fileChannel, long l, int i1) throws IOException {
        return this.byteBuf.getBytes(i, fileChannel, l, i1);
    }

    @Override
    public CharSequence getCharSequence(int i, int i1, Charset charset) {
        return this.byteBuf.getCharSequence(i, i1, charset);
    }

    @Override
    public ByteBuf setBoolean(int i, boolean b) {
        return this.byteBuf.setBoolean(i, b);
    }

    @Override
    public ByteBuf setByte(int i, int i1) {
        return this.byteBuf.setByte(i, i1);
    }

    @Override
    public ByteBuf setShort(int i, int i1) {
        return this.byteBuf.setShort(i, i1);
    }

    @Override
    public ByteBuf setShortLE(int i, int i1) {
        return this.byteBuf.setShortLE(i, i1);
    }

    @Override
    public ByteBuf setMedium(int i, int i1) {
        return this.byteBuf.setMedium(i, i1);
    }

    @Override
    public ByteBuf setMediumLE(int i, int i1) {
        return this.byteBuf.setMediumLE(i, i1);
    }

    @Override
    public ByteBuf setInt(int i, int i1) {
        return this.byteBuf.setInt(i, i1);
    }

    @Override
    public ByteBuf setIntLE(int i, int i1) {
        return this.byteBuf.setIntLE(i, i1);
    }

    @Override
    public ByteBuf setLong(int i, long l) {
        return this.byteBuf.setLong(i, l);
    }

    @Override
    public ByteBuf setLongLE(int i, long l) {
        return this.byteBuf.setLongLE(i, l);
    }

    @Override
    public ByteBuf setChar(int i, int i1) {
        return this.byteBuf.setChar(i, i1);
    }

    @Override
    public ByteBuf setFloat(int i, float v) {
        return this.byteBuf.setFloat(i, v);
    }

    @Override
    public ByteBuf setDouble(int i, double v) {
        return this.byteBuf.setDouble(i, v);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf) {
        return this.byteBuf.setBytes(i, byteBuf);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf, int i1) {
        return this.byteBuf.setBytes(i, byteBuf, i1);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf, int i1, int i2) {
        return this.byteBuf.setBytes(i, byteBuf, i1, i2);
    }

    @Override
    public ByteBuf setBytes(int i, byte[] bytes) {
        return this.byteBuf.setBytes(i, bytes);
    }

    @Override
    public ByteBuf setBytes(int i, byte[] bytes, int i1, int i2) {
        return this.byteBuf.setBytes(i, bytes, i1, i2);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuffer byteBuffer) {
        return this.byteBuf.setBytes(i, byteBuffer);
    }

    @Override
    public int setBytes(int i, InputStream inputStream, int i1) throws IOException {
        return this.byteBuf.setBytes(i, inputStream, i1);
    }

    @Override
    public int setBytes(int i, ScatteringByteChannel scatteringByteChannel, int i1) throws IOException {
        return this.byteBuf.setBytes(i, scatteringByteChannel, i1);
    }

    @Override
    public int setBytes(int i, FileChannel fileChannel, long l, int i1) throws IOException {
        return this.byteBuf.setBytes(i, fileChannel, l, i1);
    }

    @Override
    public ByteBuf setZero(int i, int i1) {
        return this.byteBuf.setZero(i, i1);
    }

    @Override
    public int setCharSequence(int i, CharSequence charSequence, Charset charset) {
        return this.byteBuf.setCharSequence(i, charSequence, charset);
    }

    @Override
    public boolean readBoolean() {
        return this.byteBuf.readBoolean();
    }

    @Override
    public byte readByte() {
        return this.byteBuf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return this.byteBuf.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return this.byteBuf.readShort();
    }

    @Override
    public short readShortLE() {
        return this.byteBuf.readShortLE();
    }

    @Override
    public int readUnsignedShort() {
        return this.byteBuf.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return this.byteBuf.readUnsignedShortLE();
    }

    @Override
    public int readMedium() {
        return this.byteBuf.readMedium();
    }

    @Override
    public int readMediumLE() {
        return this.byteBuf.readMediumLE();
    }

    @Override
    public int readUnsignedMedium() {
        return this.byteBuf.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return this.byteBuf.readUnsignedMediumLE();
    }

    @Override
    public int readInt() {
        return this.byteBuf.readInt();
    }

    @Override
    public int readIntLE() {
        return this.byteBuf.readIntLE();
    }

    @Override
    public long readUnsignedInt() {
        return this.byteBuf.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return this.byteBuf.readUnsignedIntLE();
    }

    @Override
    public long readLong() {
        return this.byteBuf.readLong();
    }

    @Override
    public long readLongLE() {
        return this.byteBuf.readLongLE();
    }

    @Override
    public char readChar() {
        return this.byteBuf.readChar();
    }

    @Override
    public float readFloat() {
        return this.byteBuf.readFloat();
    }

    @Override
    public double readDouble() {
        return this.byteBuf.readDouble();
    }

    @Override
    public ByteBuf readBytes(int i) {
        return this.byteBuf.readBytes(i);
    }

    @Override
    public ByteBuf readSlice(int i) {
        return this.byteBuf.readSlice(i);
    }

    @Override
    public ByteBuf readRetainedSlice(int i) {
        return this.byteBuf.readRetainedSlice(i);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf) {
        return this.byteBuf.readBytes(byteBuf);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int i) {
        return this.byteBuf.readBytes(byteBuf, i);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int i, int i1) {
        return this.byteBuf.readBytes(byteBuf, i, i1);
    }

    @Override
    public ByteBuf readBytes(byte[] bytes) {
        return this.byteBuf.readBytes(bytes);
    }

    @Override
    public ByteBuf readBytes(byte[] bytes, int i, int i1) {
        return this.byteBuf.readBytes(bytes, i, i1);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        return this.byteBuf.readBytes(byteBuffer);
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int i) throws IOException {
        return this.byteBuf.readBytes(outputStream, i);
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int i) throws IOException {
        return this.byteBuf.readBytes(gatheringByteChannel, i);
    }

    @Override
    public CharSequence readCharSequence(int i, Charset charset) {
        return this.byteBuf.readCharSequence(i, charset);
    }

    @Override
    public int readBytes(FileChannel fileChannel, long l, int i) throws IOException {
        return this.byteBuf.readBytes(fileChannel, l, i);
    }

    @Override
    public ByteBuf skipBytes(int i) {
        return this.byteBuf.skipBytes(i);
    }

    @Override
    public ByteBuf writeBoolean(boolean b) {
        return this.byteBuf.writeBoolean(b);
    }

    @Override
    public ByteBuf writeByte(int i) {
        return this.byteBuf.writeByte(i);
    }

    @Override
    public ByteBuf writeShort(int i) {
        return this.byteBuf.writeShort(i);
    }

    @Override
    public ByteBuf writeShortLE(int i) {
        return this.byteBuf.writeShortLE(i);
    }

    @Override
    public ByteBuf writeMedium(int i) {
        return this.byteBuf.writeMedium(i);
    }

    @Override
    public ByteBuf writeMediumLE(int i) {
        return this.byteBuf.writeMediumLE(i);
    }

    @Override
    public ByteBuf writeInt(int i) {
        return this.byteBuf.writeInt(i);
    }

    @Override
    public ByteBuf writeIntLE(int i) {
        return this.byteBuf.writeIntLE(i);
    }

    @Override
    public ByteBuf writeLong(long l) {
        return this.byteBuf.writeLong(l);
    }

    @Override
    public ByteBuf writeLongLE(long l) {
        return this.byteBuf.writeLongLE(l);
    }

    @Override
    public ByteBuf writeChar(int i) {
        return this.byteBuf.writeChar(i);
    }

    @Override
    public ByteBuf writeFloat(float v) {
        return this.byteBuf.writeFloat(v);
    }

    @Override
    public ByteBuf writeDouble(double v) {
        return this.byteBuf.writeDouble(v);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf) {
        return this.byteBuf.writeBytes(byteBuf);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int i) {
        return this.byteBuf.writeBytes(byteBuf, i);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int i, int i1) {
        return this.byteBuf.writeBytes(byteBuf, i, i1);
    }

    @Override
    public ByteBuf writeBytes(byte[] bytes) {
        return this.byteBuf.writeBytes(bytes);
    }

    @Override
    public ByteBuf writeBytes(byte[] bytes, int i, int i1) {
        return this.byteBuf.writeBytes(bytes, i, i1);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer byteBuffer) {
        return this.byteBuf.writeBytes(byteBuffer);
    }

    @Override
    public int writeBytes(InputStream inputStream, int i) throws IOException {
        return this.byteBuf.writeBytes(inputStream, i);
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int i) throws IOException {
        return this.byteBuf.writeBytes(scatteringByteChannel, i);
    }

    @Override
    public int writeBytes(FileChannel fileChannel, long l, int i) throws IOException {
        return this.byteBuf.writeBytes(fileChannel, l, i);
    }

    @Override
    public ByteBuf writeZero(int i) {
        return this.byteBuf.writeZero(i);
    }

    @Override
    public int writeCharSequence(CharSequence charSequence, Charset charset) {
        return this.byteBuf.writeCharSequence(charSequence, charset);
    }

    @Override
    public int indexOf(int i, int i1, byte b) {
        return this.byteBuf.indexOf(i, i1, b);
    }

    @Override
    public int bytesBefore(byte b) {
        return this.byteBuf.bytesBefore(b);
    }

    @Override
    public int bytesBefore(int i, byte b) {
        return this.byteBuf.bytesBefore(i, b);
    }

    @Override
    public int bytesBefore(int i, int i1, byte b) {
        return this.byteBuf.bytesBefore(i, i1, b);
    }

    @Override
    public int forEachByte(ByteProcessor byteProcessor) {
        return this.byteBuf.forEachByte(byteProcessor);
    }

    @Override
    public int forEachByte(int i, int i1, ByteProcessor byteProcessor) {
        return this.byteBuf.forEachByte(i, i1, byteProcessor);
    }

    @Override
    public int forEachByteDesc(ByteProcessor byteProcessor) {
        return this.byteBuf.forEachByteDesc(byteProcessor);
    }

    @Override
    public int forEachByteDesc(int i, int i1, ByteProcessor byteProcessor) {
        return this.byteBuf.forEachByteDesc(i, i1, byteProcessor);
    }

    @Override
    public ByteBuf copy() {
        return this.byteBuf.copy();
    }

    @Override
    public ByteBuf copy(int i, int i1) {
        return this.byteBuf.copy(i, i1);
    }

    @Override
    public ByteBuf slice() {
        return this.byteBuf.slice();
    }

    @Override
    public ByteBuf retainedSlice() {
        return this.byteBuf.retainedSlice();
    }

    @Override
    public ByteBuf slice(int i, int i1) {
        return this.byteBuf.slice(i, i1);
    }

    @Override
    public ByteBuf retainedSlice(int i, int i1) {
        return this.byteBuf.retainedSlice(i, i1);
    }

    @Override
    public ByteBuf duplicate() {
        return this.byteBuf.duplicate();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return this.byteBuf.retainedDuplicate();
    }

    @Override
    public int nioBufferCount() {
        return this.byteBuf.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.byteBuf.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int i, int i1) {
        return this.byteBuf.nioBuffer(i, i1);
    }

    @Override
    public ByteBuffer internalNioBuffer(int i, int i1) {
        return this.byteBuf.internalNioBuffer(i, i1);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.byteBuf.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int i, int i1) {
        return this.byteBuf.nioBuffers(i, i1);
    }

    @Override
    public boolean hasArray() {
        return this.byteBuf.hasArray();
    }

    @Override
    public byte[] array() {
        return this.byteBuf.array();
    }

    @Override
    public int arrayOffset() {
        return this.byteBuf.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.byteBuf.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return this.byteBuf.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return this.byteBuf.toString(charset);
    }

    @Override
    public String toString(int i, int i1, Charset charset) {
        return this.byteBuf.toString(i, i1, charset);
    }

    @Override
    public int hashCode() {
        return this.byteBuf.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this.byteBuf.equals(o);
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        return this.byteBuf.compareTo(byteBuf);
    }

    @Override
    public String toString() {
        return this.byteBuf.toString();
    }

    @Override
    public ByteBuf retain(int i) {
        return this.byteBuf.retain(i);
    }

    @Override
    public ByteBuf retain() {
        return this.byteBuf.retain();
    }

    @Override
    public ByteBuf touch() {
        return this.byteBuf.touch();
    }

    @Override
    public ByteBuf touch(Object o) {
        return this.byteBuf.touch(o);
    }

    @Override
    public int refCnt() {
        return this.byteBuf.refCnt();
    }

    @Override
    public boolean release() {
        return this.byteBuf.release();
    }

    @Override
    public boolean release(int i) {
        return this.byteBuf.release(i);
    }
}
