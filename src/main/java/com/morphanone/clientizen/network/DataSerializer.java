package com.morphanone.clientizen.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataSerializer {

    private ByteBuf buf;

    public DataSerializer() {
        this.buf = Unpooled.buffer();
    }

    public void writeBoolean(boolean b) {
        buf.writeBoolean(b);
    }

    public void writeUnsignedByte(int b) {
        buf.writeByte(b);
    }

    public void writeInt(int i) {
        buf.writeInt(i);
    }

    public void writeLong(long l) {
        buf.writeLong(l);
    }

    public void writeByteArray(byte[] bytes) {
        if (bytes == null) {
            writeInt(0);
        }
        else {
            writeInt(bytes.length);
            buf.writeBytes(bytes);
        }
    }

    public void writeString(String string) {
        if (string == null) {
            writeInt(0);
        }
        else {
            try {
                writeByteArray(string.getBytes("UTF-8"));
            }
            catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void writeStringList(Collection<String> stringList) {
        if (stringList == null) {
            writeInt(0);
        }
        else {
            writeInt(stringList.size());
            for (String string : stringList) {
                writeString(string);
            }
        }
    }

    public void writeStringMap(Map<String, String> stringMap) {
        if (stringMap == null) {
            writeInt(0);
        }
        else {
            writeInt(stringMap.size());
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                writeString(entry.getKey());
                writeString(entry.getValue());
            }
        }
    }

    public void writeStringListMap(Map<String, List<String>> stringListMap) {
        if (stringListMap == null) {
            writeInt(0);
        }
        else {
            writeInt(stringListMap.size());
            for (Map.Entry<String, List<String>> entry : stringListMap.entrySet()) {
                writeString(entry.getKey());
                writeStringList(entry.getValue());
            }
        }
    }

    public PacketBuffer toPacketBuffer() {
        return new PacketBuffer(buf);
    }
}
