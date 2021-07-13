package com.example.demo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.azure.core.annotation.Get;
import com.example.demo.Service.AzureService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.GET;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

/**
 * @author 郭展
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/Azure")
@Api(tags = "Azure-controller")
@NoArgsConstructor
public class AzureController {
    @Autowired
    private AzureService azureService;

    @ApiOperation("登录Azure")
    @GetMapping(value = "/login")
    public String azureLogin() throws IOException, InterruptedException {
        return azureService.loginAzure();
    }


    @ApiOperation("创建虚拟机")
    @ApiImplicitParam(name = "jsonParam",value = "创建虚拟机需要的参数，包括订阅id，虚拟网络名称，虚拟机名称，" +
            "虚拟机用户名，密码，虚拟机规格，资源组名称")
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

    @ApiOperation("得到所有虚拟机的信息")
    @GetMapping(value = "/allVM")
    public JSONArray getVMList() throws IOException, InterruptedException {
        return azureService.getAllVMlist();
    }

    @ApiOperation("得到所有虚拟机的展示信息")
    @GetMapping(value = "VmShow")
    public JSONObject getAllVmShowInfo(String resourceGroup, String name) throws IOException, InterruptedException {
        return azureService.getVMShowInfobyName(resourceGroup,name);
    }

    @ApiOperation("启动虚拟机")
    @ApiImplicitParam(name = "jsonParam",value = "启动虚拟机需要的参数，包括资源组名称，磁盘名称，虚拟机名称")
    @PostMapping(value = "/startVm")
    @ResponseBody
    public String startVM(@RequestBody JSONObject jsonParam) throws IOException {
        String GROUP_NAME = jsonParam.getString("GROUP_NAME");
        String OS_DISK_NAME = jsonParam.getString("OS_DISK_NAME");
        String VM_NAME = jsonParam.getString("VM_NAME");
        return azureService.startVM(GROUP_NAME,OS_DISK_NAME,VM_NAME);
    }

    @ApiOperation("停止虚拟机")
    @ApiImplicitParam(name = "jsonParam",value = "停止虚拟机需要的参数，包括资源组名称，磁盘名称，虚拟机名称")
    @PostMapping(value = "/stopVm")
    @ResponseBody
    public String stopVM(@RequestBody JSONObject jsonParam) throws IOException {
        String GROUP_NAME = jsonParam.getString("GROUP_NAME");
        String OS_DISK_NAME = jsonParam.getString("OS_DISK_NAME");
        String VM_NAME = jsonParam.getString("VM_NAME");
        return azureService.stopVM(GROUP_NAME,OS_DISK_NAME,VM_NAME);
    }

    @ApiOperation("得到订阅信息")
    @GetMapping(value = "/getSubscription")
    public JSONObject getSubscription() throws IOException
    {
        return azureService.getSubscription();
    }

    @ApiOperation("得到虚拟机状态")
    @GetMapping(value = "/getStatus")
    public String getVMStatus(String resourceGroup,String name) throws IOException {
        return azureService.getVMStatus(name,resourceGroup);
    }

    @ApiOperation("得到虚拟机ip")
    @GetMapping(value = "/getIp")
    public String getIp(String name) throws IOException {
        return azureService.getVMip(name);
    }

}
