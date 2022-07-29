package com.wusn.r.demo;

import com.wusn.r.demo.service.RConnectionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RDemoApplicationTests {
    
    @Autowired
    private RConnectionService service;
    
    private String rScript;
    
    private String executeResult;

    @BeforeEach
    public void setUp() {
        rScript = "helloWorld <- 'Hello World'";
        executeResult = "Hello World";
    }

    @AfterEach
    public void tearDown() {
        rScript = null;
        executeResult = null;
    }

    @Test
    void test4RConnectionService() throws RserveException, REXPMismatchException {
        try {
            assertFalse(service.getStatus());  // 验证R语言连接服务状态是否显示正确
            service.start();  // 开启R语言连接服务
            assertTrue(service.getStatus());  // 验证R语言连接服务状态是否显示正确
            RConnection rConnection = service.getRConnection();  // 获取R语言连接对象
            String result = rConnection.eval(rScript).asString();  // 执行R语言脚本，并将执行结果返回为Java字符串
            assertEquals(result, executeResult);  // 验证执行结果
        } finally {
            service.stop();
            assertFalse(service.getStatus());
        }
    }

}
