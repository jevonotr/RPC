package com.jjw.gkrpc.client;

import com.jjw.gkrpc.Request;
import com.jjw.gkrpc.Response;
import com.jjw.gkrpc.ServiceDescriptor;
import com.jjw.gkrpc.codec.Decoder;
import com.jjw.gkrpc.codec.Encoder;
import com.jjw.gkrpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 调用远程服务的代理类
 */
@Slf4j
public class RemoteInvoker implements InvocationHandler {
    private Class clazz;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RemoteInvoker(Class clazz, Encoder encoder, Decoder decoder, TransportSelector selector){
        this.clazz = clazz;
        this.encoder = encoder;
        this.decoder = decoder;
        this.selector = selector;
    }

    @Override
    public Object invoke(Object proxy,
                         Method method,
                         Object[] args) throws Throwable {

        //创建request对象，通过网络传输调用远程服务，判断是否成功拿到response，成功的话返回data，远程调用结束。
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz,method));
        request.setParameters(args);

        Response response = invokeRemote(request);
        if (response.getCode() != 0 || response == null){
            throw new IllegalStateException("fail to invoke remote:" + response);
        }

        return response.getData();
    }

    private Response invokeRemote(Request request) {
        Response response = null;
        TransportClient client = null;

        try{
            //选择一个client
            client = selector.select();

            //将request序列化成byte数组
            byte[] outBytes = encoder.encode(request);
            InputStream revice =  client.write(new ByteArrayInputStream(outBytes));
            byte[] inBytes = IOUtils.readFully(revice, revice.available(), true);
            response = decoder.decode(inBytes, Response.class);

        }catch (IOException e){
            log.warn(e.getMessage(), e);
            response.setCode(1);
            response.setMessage("RpcClient got error: " + e.getClass() + " : " + e.getMessage());
        }
        finally {
            if (client != null){
                selector.release(client);
            }
        }
        return response;
    }
}
