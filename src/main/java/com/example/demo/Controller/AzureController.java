package com.example.demo.Controller;

import com.example.demo.Service.AzureService;
import com.alibaba.fastjson.JSONObject;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/Azure")
@NoArgsConstructor
public class AzureController {
    @Autowired
    private AzureService azureService;

    @PostMapping(value = "/login")
    @ResponseBody
    public JSONObject azureLogin(@RequestBody JSONObject jsonParam)
    {
        return azureService.loginAzure(jsonParam.getString("userName"),jsonParam.getString("password"));
    }

    @PostMapping(value = "/createVm")
    @ResponseBody
    public void createVm(@RequestBody JSONObject jsonParam) throws IOException, InterruptedException {
        String subscription_id = jsonParam.getString("subscription_id");
        String VNET_NAME = jsonParam.getString("VNET_NAME");
        String VM_NAME = jsonParam.getString("VM_NAME");
        String USERNAME = jsonParam.getString("USERNAME");
        String PASSWORD = jsonParam.getString("PASSWORD");
        String VM_SIZE = jsonParam.getString("VM_SIZE");
        String RESOURCE_GROUP_NAME = jsonParam.getString("RESOURCE_GROUP_NAME");
        azureService.createVmLinux(subscription_id,VNET_NAME,VM_NAME,USERNAME,PASSWORD,VM_SIZE,RESOURCE_GROUP_NAME);
    }

}
