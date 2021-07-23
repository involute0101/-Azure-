package com.example.demo.Service;

import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName: ResourceServiceTest
 * @Author: 郭展
 * @Date: 2021/7/16
 * @Description: 测试
 */
class ResourceServiceTest {
    //不自动配置，测试直接new
    private ResourceService resourceService = new ResourceService();

    /**
     * 对 getAllResourece 测试
     * 包含正常、异常和边界测试
     */
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

    /**
     * 对 getAllResourceGroup 测试
     * 包含正常、异常和边界测试
     */
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

    /**
     * 对 getResourceInGroup 测试
     * 包含正常、异常和边界测试
     */
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