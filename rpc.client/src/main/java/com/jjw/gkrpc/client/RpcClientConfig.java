package com.jjw.gkrpc.client;

import com.jjw.gkrpc.Peer;
import com.jjw.gkrpc.codec.Encoder;
import com.jjw.gkrpc.codec.Decoder;
import com.jjw.gkrpc.codec.JSONDecoder;
import com.jjw.gkrpc.codec.JSONEncoder;
import com.jjw.gkrpc.transport.HTTPTransportClient;
import com.jjw.gkrpc.transport.TransportClient;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * RpcClient的配置信息
 */
@Data
public class RpcClientConfig {
    /**
     * client实现类
     * 序列化和反序列化的类型
     * 路由选择策略（随机）
     * 每个server的peer需要建立连接个数
     * server地址信息
     */
    private Class<? extends TransportClient> transportClass = HTTPTransportClient.class;
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private Class<? extends TransportSelector> selectorClass = RandomTransportSelector.class;
    private int conneCount = 1;
    private List<Peer> servers = Arrays.asList(new Peer("127.0.0.1",3000));
}
