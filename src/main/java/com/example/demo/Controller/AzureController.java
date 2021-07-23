package com.example.demo.Controller;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.Service.AzureService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/**
 * @ClassName: AzureController
 * @Author: 郭展
 * @Date: 2021/7/6
 * @Description: Azure管理接口
 */
@RestController
@RequestMapping(value = "/Azure")
@NoArgsConstructor
public class AzureController {
    @Autowired
    private AzureService azureService;

    /**
     *  登录
     * @return  登录设备码
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping(value = "/login")
    public String azureLogin() throws IOException, InterruptedException {
        return azureService.loginAzure();
    }

    /**
     * 得到账户信息
     * @return 账户信息
     * @throws IOException
     */
    @GetMapping(value = "/getAccountInfo")
    public JSONObject getAccountInfo() throws IOException {
        return azureService.getAccountInfo();
    }

    /**
     * 创建虚拟机
     * @param jsonParam 传入参数，包含七个参数，其中订阅id可由其他接口获得
     *      subscription_id 订阅号id
     *      VNET_NAME 虚拟网络名称，每建立一个虚拟机虚拟网络名称不能一致
     *      VM_NAME 虚拟机名称
     *      USERNAME  虚拟机用户名(
     *      PASSWORD  虚拟机密码（必须包含：大写，小写，数字，特殊字符）
     *      VM_SIZE   虚拟机磁盘大小
     *      RESOURCE_GROUP_NAME 资源组名称
     * @throws IOException
     * @throws InterruptedException
     */
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

    /**
     * 获取所有虚拟机
     * @return  虚拟机列表
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping(value = "/allVM")
    public JSONArray getVMList() throws IOException, InterruptedException {
        return azureService.getAllVMlist();
    }

    /**
     * 获取虚拟机的展示信息
     * @param resourceGroup 资源组名称
     * @param name  虚拟机名称
     * @return  虚拟机展示信息系
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping(value = "VmShow")
    public JSONObject getAllVmShowInfo(String resourceGroup, String name) throws IOException, InterruptedException {
        return azureService.getVMShowInfobyName(resourceGroup,name);
    }

    /**
     * 启动虚拟机
     * @param jsonParam 启动虚拟参数
     *      GROUP_NAME 资源组名称
     *      OS_DISK_NAME  磁盘名称
     *      VM_NAME   虚拟机名称
     * @throws IOException
     */
    @PostMapping(value = "/startVm")
    @ResponseBody
    public void startVM(@RequestBody JSONObject jsonParam) throws IOException {
        String GROUP_NAME = jsonParam.getString("GROUP_NAME");
        String OS_DISK_NAME = jsonParam.getString("OS_DISK_NAME");
        String VM_NAME = jsonParam.getString("VM_NAME");
        azureService.startVM(GROUP_NAME,OS_DISK_NAME,VM_NAME);
    }

    /**
     * 停止虚拟机
     * @param jsonParam 停止虚拟机参数
     *      GROUP_NAME 资源组名称
     *      OS_DISK_NAME  磁盘名称
     *     VM_NAME   虚拟机名称
     * @throws IOException
     */
    @PostMapping(value = "/stopVm")
    @ResponseBody
    public void stopVM(@RequestBody JSONObject jsonParam) throws IOException {
        String GROUP_NAME = jsonParam.getString("GROUP_NAME");
        String OS_DISK_NAME = jsonParam.getString("OS_DISK_NAME");
        String VM_NAME = jsonParam.getString("VM_NAME");
        azureService.stopVM(GROUP_NAME,OS_DISK_NAME,VM_NAME);
    }

    /**
     * 删除虚拟机
     * @param jsonParam 删除虚拟机参数
     *      GROUP_NAME    资源组名称
     *      OS_DISK_NAME  磁盘名称
     *      VM_NAME   虚拟机名称
     * @throws IOException
     */
    @PostMapping(value = "/deleteVm")
    @ResponseBody
    public void deleteVM(@RequestBody JSONObject jsonParam) throws IOException {
        String GROUP_NAME = jsonParam.getString("GROUP_NAME");
        String OS_DISK_NAME = jsonParam.getString("OS_DISK_NAME");
        String VM_NAME = jsonParam.getString("VM_NAME");
        azureService.deleteVM(GROUP_NAME,OS_DISK_NAME,VM_NAME);
    }

    /**
     * 获取订阅信息
     * @return 订阅信息
     * @throws IOException
     */
    @GetMapping(value = "/getSubscription")
    public JSONArray getSubscription() throws IOException
    {
        return azureService.getSubscription();
    }

    /**
     * 获得虚拟机状态
     * @param resourceGroup 资源组名称
     * @param name 虚拟机名称
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/getStatus")
    public String getVMStatus(String resourceGroup,String name) throws IOException {
        return azureService.getVMStatus(name,resourceGroup);
    }

    /**
     * 获取虚拟机公共ip
     * @param name  虚拟机名称
     * @return 公共ip地址
     * @throws IOException
     */
    @GetMapping(value = "/getIp")
    public String getIp(String name) throws IOException {
        return azureService.getVMip(name);
    }

    /**
     * 获取虚拟机CPU指标
     * @param jsonParam 传入参数
     *      subscriptionId    订阅id
     *      resourceGroup 资源组
     *      vmName    虚拟机名称
     * @return 虚拟机指标信息
     * @throws IOException
     */
    @PostMapping(value = "/getCpuData")
    @ResponseBody
    public JSONObject getCpuData(@RequestBody JSONObject jsonParam) throws IOException {
        String subscriptionId = jsonParam.getString("subscriptionId");
        String resourceGroup = jsonParam.getString("resourceGroup");
        String vmName = jsonParam.getString("vmName");
        return azureService.getVmCpuData(subscriptionId,resourceGroup,vmName);
    }

    /**
     * 获取虚拟机网络指标
     * @param jsonParam 虚拟机参数信息
     *      subscriptionId    订阅id
     *      resourceGroup 资源组名称
     *      vmName    虚拟机名称
     * @return  网络指标
     * @throws IOException
     */
    @PostMapping(value = "/getNetworkData")
    @ResponseBody
    public JSONObject getNetworkData(@RequestBody JSONObject jsonParam) throws IOException {
        String subscriptionId = jsonParam.getString("subscriptionId");
        String resourceGroup = jsonParam.getString("resourceGroup");
        String vmName = jsonParam.getString("vmName");
        return azureService.getVmNetworkData(subscriptionId,resourceGroup,vmName);
    }

    /**
     * 设置默认订阅信息
     * @param jsonParam 传入参数
     * @throws IOException
     */
    @PostMapping(value = "/setSubscription")
    @ResponseBody
    public void setDefaultSub(@RequestBody JSONObject jsonParam) throws IOException {
        String subscriptionId = jsonParam.getString("subscriptionId");
        azureService.setDefaultSub(subscriptionId);
    }
}
