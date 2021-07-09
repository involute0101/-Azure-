package com.example.demo.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class AzureService {
    /**
     * 登录Azure
     * @param userName  用户名
     * @param password  密码
     * @return
     */
    public JSONObject loginAzure(String userName, String password)
    {
        String loginCmd = String.format("az login -u "+userName+" -p "+password);
        String cmd[] = {"cmd","/c",loginCmd};
        try{
            StringBuilder sb =new StringBuilder();
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line=bufferedReader.readLine())!=null) {
                sb.append(line);
            }
            JSONArray jsonArray = (JSONArray)JSONArray.parse(sb.toString());
            return jsonArray.getJSONObject(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
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
        String cmds = String.format("python D:/code/Azure/python/createVMargs/venv/creatVM_args.py %s %s %s %s %s %s %s",
                subscription_id, VNET_NAME,VM_NAME,USERNAME,PASSWORD,VM_SIZE,RESOURCE_GROUP_NAME);
        System.out.println("\nExecuting python script file now.");
        Process pcs = Runtime.getRuntime().exec(cmds);
        pcs.waitFor();

        String result = null;
        // 获取CMD的返回流
        BufferedInputStream in = new BufferedInputStream(pcs.getInputStream());
        // 字符流转换字节流
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        // 这里也可以输出文本日志

        String lineStr = null;
        while ((lineStr = br.readLine()) != null) {
            result = lineStr;
        }
        // 关闭输入流
        br.close();
        in.close();

        System.out.println(result);

        System.out.println("进程结束！");
    }
}
