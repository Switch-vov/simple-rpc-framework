package com.github.liyue2008.rpc.serialize.impl.json;

import com.alibaba.fastjson.JSON;
import com.github.liyue2008.rpc.serialize.Serializer;

import java.nio.charset.StandardCharsets;

/**
 * @author switch
 * @since 2019/11/2
 */
public abstract class JSONSerializer<T> implements Serializer<T> {
    @Override
    public int size(T entry) {
        return Integer.BYTES + JSON.toJSONString(entry).getBytes(StandardCharsets.UTF_8).length;
    }
}
