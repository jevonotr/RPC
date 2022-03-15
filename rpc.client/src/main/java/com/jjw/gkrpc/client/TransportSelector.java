package com.jjw.gkrpc.client;

import com.jjw.gkrpc.Peer;
import com.jjw.gkrpc.transport.TransportClient;
import sun.rmi.transport.TransportConstants;

import java.util.List;

/**
 * 表示选择哪个server去连接
 */
public interface TransportSelector {
    /**
     * 初始化select
     * @param peers 可以连接的server端点信息
     * @param count client与server建立多少个连接
     * @param clazz client实现clazz
     */
    void init(List<Peer> peers, int count, Class<? extends  TransportClient> clazz);

    /**
     * 选择一个transport与server做交互
     * @return 网络client
     */
    TransportClient select();

    /**
     * 释放用完的client
     * @param client
     */
    void  release(TransportClient client);


    void close();

}
