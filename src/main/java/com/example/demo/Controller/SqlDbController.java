package com.example.demo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Service.SqlService;
import io.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 郭展
 * @date 2021-07-13
 */
@RestController
@RequestMapping(value = "/DB")
@Api(tags = "AzureDB-controller")
@NoArgsConstructor
public class SqlDbController {
    @Autowired
    private SqlService sqlService;

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

    @DeleteMapping(value = "/deleteDB")
    @ResponseBody
    public void deleteDB(@RequestBody JSONObject jsonParam)
    {
        String SqlServerId = jsonParam.getString("SqlServerId");
        String DBname = jsonParam.getString("DBname");
        sqlService.deleteDb(SqlServerId,DBname);
    }

    @GetMapping(value = "/allDB")
    public JSONArray getAllDb()
    {
        return sqlService.getAllDb();
    }
}
