package com.github.liyue2008.rpc.serialize.impl.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.liyue2008.rpc.nameservice.Metadata;
import com.github.liyue2008.rpc.serialize.impl.Types;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author switch
 * @since 2019/11/1
 */
public class MetadataSerializer extends JSONSerializer<Metadata> {

    @Override
    public void serialize(Metadata entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        byte[] jsonBytes = JSON.toJSONString(entry).getBytes(StandardCharsets.UTF_8);
        buffer.putInt(jsonBytes.length);
        buffer.put(jsonBytes);
    }

    @Override
    public Metadata parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        int sizeOfJson = buffer.getInt();
        byte[] jsonBytes = new byte[sizeOfJson];
        buffer.get(jsonBytes);
        String jsonString = new String(jsonBytes, StandardCharsets.UTF_8);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Metadata metadata = new Metadata();
        jsonObject.entrySet()
                .forEach(entry -> {
                    List<String> uriList = (List<String>) entry.getValue();
                    List<URI> uris = uriList.stream()
                            .map(URI::create)
                            .collect(Collectors.toList());
                    metadata.put(entry.getKey(), uris);
                });
        return metadata;
    }

    @Override
    public byte type() {
        return Types.TYPE_METADATA;
    }

    @Override
    public Class<Metadata> getSerializeClass() {
        return Metadata.class;
    }
}
