package com.example.demo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Service.SqlService;
import io.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: SqlDbController
 * @Author: 郭展
 * @Date: 2021/7/13
 * @Description: Azure数据库管理接口
 */
@RestController
@RequestMapping(value = "/DB")
@NoArgsConstructor
public class SqlDbController {
    @Autowired
    private SqlService sqlService;

    /**
     * 创建数据库
     * @param jsonParam 传入参数
     *      userName  用户名
     *      password  密码
     *      DBname    数据库名称
     *      resourceGroupName 资源组名称
     */
    @PostMapping(value = "/createDB")
    @ResponseBody
    public void createDB(@RequestBody JSONObject jsonParam)
    {
        String userName = jsonParam.getString("userName");
        String password = jsonParam.getString("password");
        String DBname = jsonParam.getString("DBname");
        String resourceGroupName = jsonParam.getString("resourceGroupName");
        sqlService.createDB(userName,password,DBname,resourceGroupName);
    }

    /**
     * 删除数据库
     * @param jsonParam 传入参数
     *      SqlServerId 数据库服务器id
     *      DBname 数据库名称
     */
    @PostMapping(value = "/deleteDB")
    @ResponseBody
    public void deleteDB(@RequestBody JSONObject jsonParam)
    {
        String SqlServerId = jsonParam.getString("SqlServerId");
        String DBname = jsonParam.getString("DBname");
        sqlService.deleteDb(SqlServerId,DBname);
    }

    /**
     * 查询所有数据库
     * @return  数据库列表
     */
    @GetMapping(value = "/allDB")
    public JSONArray getAllDb()
    {
        return sqlService.getAllDb();
    }
}
