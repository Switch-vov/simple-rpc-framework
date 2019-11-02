package com.github.liyue2008.rpc.serialize.impl.json;

import com.alibaba.fastjson.JSON;
import com.github.liyue2008.rpc.client.stubs.RpcRequest;
import com.github.liyue2008.rpc.serialize.impl.Types;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author switch
 * @since 2019/11/2
 */
public class RpcRequestSerializer extends JSONSerializer<RpcRequest> {

    @Override
    public void serialize(RpcRequest entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        byte[] jsonBytes = JSON.toJSONString(entry).getBytes(StandardCharsets.UTF_8);
        buffer.putInt(jsonBytes.length);
        buffer.put(jsonBytes);
    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        int sizeOfJson = buffer.getInt();
        byte[] jsonBytes = new byte[sizeOfJson];
        buffer.get(jsonBytes);
        return JSON.parseObject(jsonBytes, getSerializeClass());
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getSerializeClass() {
        return RpcRequest.class;
    }
}
