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
 * @author 郭展
 * @date 2021-07-15
 */
@RestController
@RequestMapping(value = "/Resource")
@Api(tags = "Azure-Resource-controller")
@NoArgsConstructor
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @GetMapping(value = "/getAllResource")
    public JSONArray getAllResource() throws IOException {
        return resourceService.getAllResourece();
    }

    @GetMapping(value = "getResourceInGroup")
    public JSONArray getResourceInGroup(String resourceGroup) throws IOException {
        return resourceService.getResourceInGroup(resourceGroup);
    }

    @GetMapping(value = "getAllGroup")
    public JSONArray getAllResourceGroup() throws IOException {
        return resourceService.getAllResourceGroup();
    }
}
