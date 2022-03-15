package com.jjw.gkrpc.server;

import com.jjw.gkrpc.Request;
import com.jjw.gkrpc.Response;
import com.jjw.gkrpc.codec.Decoder;
import com.jjw.gkrpc.codec.Encoder;
import com.jjw.gkrpc.common.utils.ReflectionUtils;
import com.jjw.gkrpc.transport.RequestHandler;
import com.jjw.gkrpc.transport.TransportServer;
import lombok.extern.slf4j.Slf4j;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class RpcServer {
    private RPCSetverConfig config;
    private TransportServer net;
    private Encoder encoder;
    private Decoder decoder;
    private ServiceManager serviceManager;
    private  ServiceInvoker serviceInvoker;

    public RpcServer(){
        this(new RPCSetverConfig());
    }

    public RpcServer(RPCSetverConfig config){
        this.config = config;

        //网络模块
        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        this.net.init(config.getPort(), this.handler);

        //序列化
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());

        //service
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();
    }

    //注册方法
    public <T> void register(Class<T> interfaceClass, T bean){
        serviceManager.register(interfaceClass, bean);
    }

    public void start(){
        this.net.start();
    }

    public void stop(){
        this.net.stop();
    }


    private RequestHandler handler = new RequestHandler() {
            /**
             *
             * @param recive 请求被序列化之后的二进制数据，拿到这个数据后通过invoker调用服务
             * @param toResp 之后通过toResp写回去
             */
        @Override
        public void onRequest(InputStream recive, OutputStream toResp) {
            Response resp = new Response();

            try {
                byte[] inBytes = IOUtils.readFully(recive,recive.available(),true);

                Request request = decoder.decode(inBytes,Request.class);

                log.info("get request: {}", request);

                ServiceInstance sis = serviceManager.lookup(request);
                Object ret = serviceInvoker.invoke(sis,request);
                resp.setData(ret);

            } catch (Exception e) {
                log.warn(e.getMessage(),e);
                resp.setCode(1);
                resp.setMessage("RpcServer got error" + e.getClass().getName() + " : " +e.getMessage());
            } finally {
                byte[] outBytes = encoder.encode(resp);
                try {
                    toResp.write(outBytes);

                    log.info("response client");
                } catch (IOException e) {
                    log.warn(e.getMessage(),e);
                }
            }
        }
    };
}
