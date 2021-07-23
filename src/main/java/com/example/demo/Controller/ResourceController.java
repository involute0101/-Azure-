package com.example.demo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.Service.ResourceService;
import io.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @ClassName: ResourceController
 * @Author: 郭展
 * @Date: 2021/7/15
 * @Description: Azure资源管理接口
 */
@RestController
@RequestMapping(value = "/Resource")
@NoArgsConstructor
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    /**
     * 获取所有资源
     * @return  查询的资源
     *          JSONArray型
     * @throws IOException
     */
    @GetMapping(value = "/getAllResource")
    public JSONArray getAllResource() throws IOException {
        return resourceService.getAllResourece();
    }

    /**
     * 获得资源组中的所有资源
     * @param resourceGroup 资源组名称
     * @return  查询出来的所有资源
     *          JSONArray型
     * @throws IOException
     */
    @GetMapping(value = "getResourceInGroup")
    public JSONArray getResourceInGroup(String resourceGroup) throws IOException {
        return resourceService.getResourceInGroup(resourceGroup);
    }

    /**
     * 获得的所有资源组
     * @return 资源组列表
     *          JSONArray型
     * @throws IOException
     */
    @GetMapping(value = "getAllGroup")
    public JSONArray getAllResourceGroup() throws IOException {
        return resourceService.getAllResourceGroup();
    }
}
