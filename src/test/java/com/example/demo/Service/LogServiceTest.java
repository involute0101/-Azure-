package com.example.demo.Service;

import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName: LogServiceTest
 * @Author: 郭展
 * @Date: 2021/7/16
 * @Description: 测试
 */
class LogServiceTest {

    //不自动配置，测试直接new
    private LogService logService = new LogService();

    /**
     * 对 getLogInGroup 测试
     * 包含正常、异常和边界测试
     */
    @Test
    void getLogInGroup() {
        try{
            logService.getLogInGroup(null);
        }catch (Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            JSONArray sddaasad = logService.getLogInGroup("sddaasad");
            assertTrue(sddaasad != null);
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }

        try {
            JSONArray nologintest = logService.getLogInGroup("Nologintest");
            assertTrue(!nologintest.isEmpty());
        }catch (Exception e)
        {
            assertTrue(e instanceof IOException);
        }
    }

    /**
     * 对 getAllLog 测试
     * 包含正常、异常和边界测试
     */
    @Test
    void getAllLog() {
        try{
            JSONArray allLog = logService.getAllLog();
            assertTrue(allLog != null);
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }
    }
}