package com.wusn.r.demo.service;

import org.rosuda.REngine.Rserve.RConnection;

/**
 * R语言连接服务
 *
 * @author wusn
 * @since 1.0.0
 */
public interface RConnectionService {

    /**
     * 启动R语言连接服务。
     */
    void start();

    /**
     * 关闭R语言连接服务。
     */
    void stop();

    /**
     * 重启R语言连接服务。
     */
    void reboot();

    /**
     * 获取R语言连接服务状态。
     */
    boolean getStatus();

    /**
     * 获取R语言连接对象。
     */
    RConnection getRConnection();

}
