package com.example.demo.Service;

import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 郭展
 * @date 2021-07-16
 */
class SqlServiceTest {

    private SqlService sqlService = new SqlService();
    @Test
    void getAllDb() {
        JSONArray allDb = sqlService.getAllDb();
        assertTrue(allDb != null );
    }
}