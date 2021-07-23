package com.example.demo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.Service.LogService;
import io.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @ClassName: LogController
 * @Author: 郭展
 * @Date: 2021/7/15
 * @Description: Azure日志管理接口
 */
@RestController
@RequestMapping(value = "/Log")
@NoArgsConstructor
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 查看资源组日志
     * @param resourceGroup 资源组名称
     * @return  查询的资源组日志
     *          JSONArray型
     * @throws IOException
     */
    @GetMapping(value = "/LogInGroup")
    public JSONArray getLogInGroup(String resourceGroup) throws IOException {
        return logService.getLogInGroup(resourceGroup);
    }

    /**
     * 查询所有警报
     * @return  查询所有警报信息
     *          JSONArray型
     * @throws IOException
     */
    @GetMapping(value = "/allAlert")
    public JSONArray getAllLog() throws IOException {
        return logService.getAllLog();
    }
}
