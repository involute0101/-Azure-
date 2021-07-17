package com.example.demo.Service;

import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 郭展
 * @date 2021-07-16
 */
class LogServiceTest {

    private LogService logService = new LogService();

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