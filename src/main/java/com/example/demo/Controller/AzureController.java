package com.example.demo.Controller;

import com.alibaba.fastjson.JSONArray;
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

    @GetMapping(value = "/login")
    public String azureLogin() throws IOException, InterruptedException {
        return azureService.loginAzure();
    }

    @PostMapping(value = "/createVm")
    @ResponseBody
    public String createVm(@RequestBody JSONObject jsonParam) throws IOException, InterruptedException {
        String subscription_id = jsonParam.getString("subscription_id");
        String VNET_NAME = jsonParam.getString("VNET_NAME");
        String VM_NAME = jsonParam.getString("VM_NAME");
        String USERNAME = jsonParam.getString("USERNAME");
        String PASSWORD = jsonParam.getString("PASSWORD");
        String VM_SIZE = jsonParam.getString("VM_SIZE");
        String RESOURCE_GROUP_NAME = jsonParam.getString("RESOURCE_GROUP_NAME");
        return azureService.createVmLinux(subscription_id,VNET_NAME,VM_NAME,USERNAME,PASSWORD,VM_SIZE,RESOURCE_GROUP_NAME);
    }

    @GetMapping(value = "/allVM")
    public JSONArray getVMList() throws IOException, InterruptedException {
        return azureService.getAllVMlist();
    }

    @PostMapping(value = "/startVm")
    @ResponseBody
    public String startVM(@RequestBody JSONObject jsonParam) throws IOException {
        String GROUP_NAME = jsonParam.getString("GROUP_NAME");
        String OS_DISK_NAME = jsonParam.getString("OS_DISK_NAME");
        String VM_NAME = jsonParam.getString("VM_NAME");
        return azureService.startVM(GROUP_NAME,OS_DISK_NAME,VM_NAME);
    }

    @PostMapping(value = "/stopVm")
    @ResponseBody
    public String stopVM(@RequestBody JSONObject jsonParam) throws IOException {
        String GROUP_NAME = jsonParam.getString("GROUP_NAME");
        String OS_DISK_NAME = jsonParam.getString("OS_DISK_NAME");
        String VM_NAME = jsonParam.getString("VM_NAME");
        return azureService.stopVM(GROUP_NAME,OS_DISK_NAME,VM_NAME);
    }

}
