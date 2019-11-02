package com.github.liyue2008.rpc.client.stubs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author switch
 * @since 2019/11/2
 */
public class RpcRequestArgs {
    private List<Class<?>> argClasses;
    private List<Object> args;

    public RpcRequestArgs(int capacity) {
        this.argClasses = new ArrayList<>(capacity);
        this.args = new ArrayList<>(capacity);
    }


    public void addClass(Class<?> clazz) {
        argClasses.add(clazz);
    }

    public void addObj(Object obj) {
        args.add(obj);
    }

    public List<Class<?>> getArgClasses() {
        return argClasses;
    }

    public void setArgClasses(List<Class<?>> argClasses) {
        this.argClasses = argClasses;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }
}
