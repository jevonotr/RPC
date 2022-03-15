package com.jjw.gkrpc.server;

import com.jjw.gkrpc.codec.Decoder;
import com.jjw.gkrpc.codec.Encoder;
import com.jjw.gkrpc.codec.JSONDecoder;
import com.jjw.gkrpc.codec.JSONEncoder;
import com.jjw.gkrpc.transport.HTTPTransportServer;
import com.jjw.gkrpc.transport.TransportServer;
import lombok.Data;
import sun.rmi.transport.Transport;

/**
 * server配置
 */
@Data
public class RPCSetverConfig {
    private Class<? extends TransportServer> transportClass = HTTPTransportServer.class;
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private int port = 3000;

}
