package com.morphanone.clientizen.network;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDeserializer {

    private ByteBuf buf;

    public DataDeserializer(ByteBuf buf) {
        this.buf = buf;
    }

    public boolean readBoolean() {
        return buf.readBoolean();
    }

    public int readUnsignedByte() {
        return buf.readUnsignedByte();
    }

    public int readInt() {
        return buf.readInt();
    }

    public long readLong() {
        return buf.readLong();
    }

    public byte[] readByteArray() {
        byte[] bytes = new byte[readInt()];
        buf.readBytes(bytes);
        return bytes;
    }

    public String readString() {
        try {
            return new String(readByteArray(), "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String[] readStringArray() {
        String[] array = new String[readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = readString();
        }
        return array;
    }

    public List<String> readStringList() {
        List<String> stringList = new ArrayList<String>();
        int size = readInt();
        for (int i = 0; i < size; i++) {
            stringList.add(readString());
        }
        return stringList;
    }

    public Map<String, String> readStringMap() {
        Map<String, String> stringMap = new HashMap<String, String>();
        int size = readInt();
        for (int i = 0; i < size; i++) {
            String key = readString();
            String value = readString();
            stringMap.put(key, value);
        }
        return stringMap;
    }

    public Map<String, List<String>> readStringListMap() {
        Map<String, List<String>> stringListMap = new HashMap<String, List<String>>();
        int size = readInt();
        for (int i = 0; i < size; i++) {
            String key = readString();
            List<String> value = readStringList();
            stringListMap.put(key, value);
        }
        return stringListMap;
    }
}
