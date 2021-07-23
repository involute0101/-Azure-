package com.example.demo.Service;

import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName: SqlServiceTest
 * @Author: 郭展
 * @Date: 2021/7/16
 * @Description: 测试
 */
class SqlServiceTest {

    //不自动配置，测试直接new
    private SqlService sqlService = new SqlService();

    /**
     * 对 getAllDb 测试
     * 包含正常、异常和边界测试
     */
    @Test
    void getAllDb() {
        JSONArray allDb = sqlService.getAllDb();
        assertTrue(allDb != null );
    }
}