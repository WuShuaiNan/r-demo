package com.wusn.r.demo.service.impl;

import com.wusn.r.demo.service.RConnectionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class RConnectionServiceImpl implements RConnectionService {

    public static final Logger LOGGER = LogManager.getLogger(RConnectionServiceImpl.class);

    private final Lock lock = new ReentrantLock();

    private boolean startFlag = false;

    private RConnection rConnection = null;
    
    @Value("${r.demo.rserve.host}")
    private String rServeHost;

    @Value("${r.demo.rserve.port}")
    private int rServePort;

    @Value("${r.demo.rserve.anonymous.enable}")
    private boolean rServeAnonymousEnable;

    @Value("${r.demo.rserve.username}")
    private String rServeUsername;

    @Value("${r.demo.rserve.password}")
    private String rServePassword;

    @Override
    public void start() {
        lock.lock();
        try {
            if (!startFlag) {
                startRConnection();
                startFlag = true;
            }
        } catch (Exception e) {
            LOGGER.error("启动R语言连接服务失败，失败原因如下：", e);
        } finally {
            lock.unlock();
        }
    }
    
    public void startRConnection() throws RserveException {
        LOGGER.info("启动R语言连接服务...");
        if (rServeAnonymousEnable) {
            rConnection = new RConnection(rServeHost, rServePort);
            rConnection.login(rServeUsername, rServePassword);
        } else {
            rConnection = new RConnection(rServeHost, rServePort);
        }
    }

    @Override
    public void stop() {
        lock.lock();
        try {
            if (startFlag) {
                stopRConnection();
                startFlag = false;
            }
        } catch (Exception e) {
            LOGGER.error("关闭R语言连接服务失败，失败原因如下：", e);
        } finally {
            lock.unlock();
        }
    }
    
    public void stopRConnection() {
        LOGGER.info("关闭R语言连接服务...");
        if (rConnection != null) {
            rConnection.close();
        }
    }

    @Override
    public void reboot() {
        lock.lock();
        try {
            stop();
            start();
        } catch (Exception e) {
            LOGGER.error("重启R语言连接服务失败，失败原因如下：", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean getStatus() {
        if (rConnection != null) {
            startFlag = rConnection.isConnected();
        }
        return startFlag;
    }

    @Override
    public RConnection getRConnection() {
        return rConnection;
    }
}
