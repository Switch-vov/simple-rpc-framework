package com.github.liyue2008.rpc.serialize.impl.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.liyue2008.rpc.client.stubs.RpcRequestArgs;
import com.github.liyue2008.rpc.serialize.impl.Types;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author switch
 * @since 2019/11/2
 */
public class RpcRequestArgsSerializer extends JSONSerializer<RpcRequestArgs> {
    @Override
    public void serialize(RpcRequestArgs entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        byte[] jsonBytes = JSON.toJSONString(entry).getBytes(StandardCharsets.UTF_8);
        buffer.putInt(jsonBytes.length);
        buffer.put(jsonBytes);
    }

    @Override
    public RpcRequestArgs parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        int sizeOfJson = buffer.getInt();
        byte[] jsonBytes = new byte[sizeOfJson];
        buffer.get(jsonBytes);
        String jsonString = new String(jsonBytes, StandardCharsets.UTF_8);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        List<String> argClasses = jsonObject.getObject("argClasses", List.class);
        List args = jsonObject.getObject("args", List.class);
        List<Class<?>> classes = argClasses.stream().map(clazz -> {
            try {
                return Class.forName(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        List<Object> arg = IntStream.range(0, args.size())
                .mapToObj(index -> {
                    Object obj = args.get(index);
                    if (obj instanceof String) {
                        return obj;
                    } else {
                        return JSON.parseObject(JSON.toJSONString(obj), classes.get(index));
                    }

                })
                .collect(Collectors.toList());
        RpcRequestArgs requestArgs = new RpcRequestArgs(arg.size());
        requestArgs.setArgClasses(classes);
        requestArgs.setArgs(arg);
        return requestArgs;
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST_ARGS;
    }

    @Override
    public Class<RpcRequestArgs> getSerializeClass() {
        return RpcRequestArgs.class;
    }
}
