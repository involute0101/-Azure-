package com.example.demo.Service;

import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 郭展
 * @date 2021-07-16
 */
class ResourceServiceTest {
    private ResourceService resourceService = new ResourceService();

    @Test
    void getAllResourece() {
        try{
            JSONArray allResourece = resourceService.getAllResourece();
            assertTrue(allResourece != null || !allResourece.isEmpty());
        }catch (Exception e)
        {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    void getAllResourceGroup() {
        try{
            JSONArray allResourceGroup = resourceService.getAllResourceGroup();
            assertTrue(allResourceGroup != null || !allResourceGroup.isEmpty());
        }catch (Exception e)
        {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    void getResourceInGroup() {
        try{
            resourceService.getResourceInGroup(null);
        }catch (Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            JSONArray hhhh = resourceService.getResourceInGroup("hhhh");
            assertTrue(hhhh != null || hhhh.isEmpty());
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }

        try{
            JSONArray nologinTest = resourceService.getResourceInGroup("NologinTest");
            assertTrue(!nologinTest.isEmpty());
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }
    }
}