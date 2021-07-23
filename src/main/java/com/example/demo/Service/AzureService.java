package com.example.demo.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.LinkedHashMap;


/**
 * @ClassName: AzureService
 * @Author: 郭展
 * @Date: 2021/7/6
 * @Description: Azure管理Service层
 */
@Service
public class AzureService  {
    /**
     * 登录Azure(抛弃，使用python接口）
     * @return
     */
    public String loginAzure() throws IOException, InterruptedException {
        String startCmd = String.format("sudo -i;python3 /home/Aroot/pythonProject/virtualPy/ENV/testLogin.py");
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        System.out.println("login Now");
        Process process = Runtime.getRuntime().exec(vm);
        process.wait(1500);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * 获得用户信息
     * @throws IOException
     */
    public JSONObject getAccountInfo() throws IOException {
        String cmd = String.format("az account show");
        String vm[] = {"/bin/sh","-c",cmd};
        StringBuilder sb = new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while((line=bufferedReader.readLine())!=null){
            sb.append(line);
        }
        return JSONObject.parseObject(sb.toString());
    }

    /**
     * 创建Linux虚拟机
     * @param subscription_id 订阅号id
     * @param VNET_NAME 虚拟网络名称，每建立一个虚拟机虚拟网络名称不能一致
     * @param VM_NAME 虚拟机名称
     * @param USERNAME  虚拟机用户名(
     * @param PASSWORD  虚拟机密码（必须包含：大写，小写，数字，特殊字符）
     * @param VM_SIZE   虚拟机磁盘大小
     * @param RESOURCE_GROUP_NAME 资源组名称
     */
    public void createVmLinux(String subscription_id,String VNET_NAME,String VM_NAME,String USERNAME,String PASSWORD,
                              String VM_SIZE,String RESOURCE_GROUP_NAME) throws IOException, InterruptedException {

        String startCmd = String.format("python3 /home/Aroot/pythonProject/virtualPy/creatVM_args.py %s %s %s %s %s %s %s",
                subscription_id, VNET_NAME,VM_NAME,USERNAME,PASSWORD,VM_SIZE,RESOURCE_GROUP_NAME);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        System.out.println("\nExecuting python script file now.");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        System.out.println("进程结束");
    }

    /**
     *启动虚拟机
     * @param GROUP_NAME 资源组名称
     * @param OS_DISK_NAME  磁盘名称
     * @param VM_NAME   虚拟机名称
     * @return 启动结果
     * @throws IOException
     */
    public void startVM(String GROUP_NAME,String OS_DISK_NAME,String VM_NAME) throws IOException {
        String startCmd = String.format("python3 /home/Aroot/pythonProject/virtualPy/startVM.py %s %s %s",GROUP_NAME,OS_DISK_NAME,VM_NAME);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        System.out.println("StartVm");
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
    }

    /**
     * 停止虚拟机
     * @param GROUP_NAME 资源组名称
     * @param OS_DISK_NAME  磁盘名称
     * @param VM_NAME   虚拟机名称
     * @return 停止结果
     * @throws IOException
     */
    public void stopVM(String GROUP_NAME,String OS_DISK_NAME,String VM_NAME) throws IOException {
        String startCmd = String.format("python3 /home/Aroot/pythonProject/virtualPy/stopVM.py %s %s %s",GROUP_NAME,OS_DISK_NAME,VM_NAME);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        System.out.println("stopVM");
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
    }

    /**
     * 删除虚拟机
     * @param GROUP_NAME    资源组名称
     * @param OS_DISK_NAME  磁盘名称
     * @param VM_NAME   虚拟机名称
     * @return 停止结果
     * @throws IOException
     */
    public void deleteVM(String GROUP_NAME,String OS_DISK_NAME,String VM_NAME) throws IOException {
        String startCmd = String.format("python3 /home/Aroot/pythonProject/virtualPy/deleteVM.py %s %s %s",GROUP_NAME,OS_DISK_NAME,VM_NAME);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        System.out.println("deleteVM");
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
    }

    /**
     * 获取虚拟机的展示信息
     * @param resourceGroup 资源组名称
     * @param name  虚拟机名称
     * @return  展示信息
     * @throws IOException
     */
    public JSONObject getVMShowInfobyName(String resourceGroup, String name) throws IOException {
        if(resourceGroup == null || name == null ){throw new IllegalArgumentException("argument illegal;null");}
        String startCmd = String.format("az vm show --resource-group %s --name %s",resourceGroup,name);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        JSONObject VMinformation = JSONObject.parseObject(sb.toString());
        if(VMinformation == null || VMinformation.isEmpty()){return VMinformation;}

        JSONObject json_storageProfile = VMinformation.getJSONObject("storageProfile");
        JSONObject VMmachine = new JSONObject();
        VMmachine.put("name",VMinformation.getString("name"));
        VMmachine.put("location",VMinformation.getString("location"));
        VMmachine.put("resourceGroup",VMinformation.getString("resourceGroup"));
        VMmachine.put("OS_Disk_name",json_storageProfile.getJSONObject("osDisk").getString("name"));
        VMmachine.put("OS",json_storageProfile.getJSONObject("osDisk").
                getString("osType")+"("+json_storageProfile.getJSONObject("imageReference").
                getString("offer")+"  "+json_storageProfile.getJSONObject("imageReference").
                getString("exactVersion")+")");
        VMmachine.put("vmSize",VMinformation.getJSONObject("hardwareProfile").getString("vmSize"));
        return VMmachine;
    }

    /**
     * 获得所有虚拟机信息
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public JSONArray getAllVMlist() throws IOException {
        String startCmd = String.format("az vm list");
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        return JSONArray.parseArray(sb.toString());
    }

    /**
     * 获取订阅信息
     * @return 订阅id和订阅名称
     * @throws IOException
     */
    public JSONArray getSubscription() throws IOException {
        String startCmd = String.format("az account list");
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        JSONArray subscriptionList = JSONArray.parseArray(sb.toString());
        JSONArray result = new JSONArray();
        for(Object json : subscriptionList){
            JSONObject subscription = new JSONObject();
            subscription.put("subscriptionId",((JSONObject)json).getString("id"));
            subscription.put("subscriptionName",((JSONObject)json).getString("name"));
            result.add(subscription);
        }
        return result;
    }

    /**
     * 得到虚拟机运行状态
     * @param VMname    虚拟机名称
     * @param resourceGroup 资源组名称
     * @return  虚拟机的运行状态
     * @throws IOException
     */
    public String getVMStatus(String VMname,String resourceGroup) throws IOException {
        if(VMname == null || resourceGroup == null){throw new IllegalArgumentException("argument illegal;null");}
        String startCmd = String.format("az vm get-instance-view --name %s --resource-group %s " +
                "--query instanceView.statuses[1]",VMname,resourceGroup);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        JSONObject result = JSONObject.parseObject(sb.toString());
        if(result == null || result.isEmpty()){return "error!\n virtual machine or resource group isn't exist;";}
        return JSONObject.parseObject(sb.toString()).getString("displayStatus");
    }

    /**
     * 得到虚拟机的ip
     * @param VMname    虚拟机ip
     * @return
     */
    public String getVMip(String VMname) throws IOException {
        if(VMname == null){throw new IllegalArgumentException("argument illegal;null");}
        String ipCmd = String.format("az vm list-ip-addresses -n %s",VMname);
        String vmIP[] = {"/bin/sh","-c",ipCmd};
        StringBuilder sbIP =new StringBuilder();
        Process IPprocess = Runtime.getRuntime().exec(vmIP);
        BufferedReader IPbufferedReader = new BufferedReader(new InputStreamReader(IPprocess.getInputStream()));
        String lineIP;
        while ((lineIP=IPbufferedReader.readLine())!=null) {
            sbIP.append(lineIP);
        }
        JSONArray result = JSONArray.parseArray(sbIP.toString());
        if(result == null || result.isEmpty()){return "virtual machine isn't exist;";}
        JSONObject VMipJSon = result.getJSONObject(0);
        return VMipJSon.getJSONObject("virtualMachine").getJSONObject("network").
                getJSONArray("publicIpAddresses").getJSONObject(0).getString("ipAddress");
    }

    /**
     * 获得虚拟机的CPU指标
     * @param subscriptionId    订阅id
     * @param resourceGroup 资源组
     * @param vmName    虚拟机名称
     * @return
     * @throws IOException
     */
    public JSONObject getVmCpuData(String subscriptionId,String resourceGroup,String vmName) throws IOException {
        if(subscriptionId == null || resourceGroup == null || vmName == null){throw new IllegalArgumentException("argument illegal;null");}
        String startCmd = String.format("python3 /home/Aroot/pythonProject/virtualPy/cpuData.py %s %s %s",
                subscriptionId,resourceGroup,vmName);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        String cpuDataArray[] = sb.toString().split("@");
        JSONObject result = new JSONObject(new LinkedHashMap());
        try{
            for(String cpuData : cpuDataArray)
            {
                String oneDataInfo[] = cpuData.split("&&");
                result.put(oneDataInfo[0],oneDataInfo[1]);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            result.put("message","this subscriptionId or resourceGroup or vmName isn't exist;");
        }
        return result;
    }

    /**
     * 获取虚拟机网络指标
     * @param subscriptionId    订阅id
     * @param resourceGroup 资源组名称
     * @param vmName    虚拟机名称
     * @return  虚拟机网络指标，按小时分隔
     * @throws IOException
     */
    public JSONObject getVmNetworkData(String subscriptionId,String resourceGroup,String vmName) throws IOException {
        if(subscriptionId == null || resourceGroup == null || vmName == null){throw new IllegalArgumentException("argument illegal;null");}
        String startCmd = String.format("python3 /home/Aroot/pythonProject/virtualPy/networkData.py %s %s %s",
                subscriptionId,resourceGroup,vmName);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb = new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        String networdDataArray[] = sb.toString().split("@");
        JSONObject result = new JSONObject(new LinkedHashMap());
        try{
            for(String networkData : networdDataArray)
            {
                String oneDataInfo[] = networkData.split("&&");
                result.put(oneDataInfo[0],oneDataInfo[1]);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            result.put("message","this subscriptionId or resourceGroup or vmName isn't exist;");
        }
        return result;
    }

    /**
     * 设置默认订阅id
     * @param subscriptionId    订阅id
     * @return
     * @throws IOException
     */
    public void setDefaultSub(String subscriptionId) throws IOException {
        if(subscriptionId == null){throw new IllegalArgumentException("argument illegal;null");}
        String startCmd = String.format("az account set --subscription %s",subscriptionId);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
    }
}
