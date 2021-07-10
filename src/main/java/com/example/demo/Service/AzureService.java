package com.example.demo.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class AzureService  {
    /**
     * 登录Azure
     * @return
     */

    public String loginAzure()throws IOException, InterruptedException {
        synchronized (this){
//           String loginCmd = String.format("python3 /home/Aroot/testLogin.py");
            String loginCmd = String.format("python3 C:\\Users\\86178\\Desktop\\testLogin.py");
            System.out.println("-----------");
            String login[] = {"cmd","-c",loginCmd};
            StringBuilder sb =new StringBuilder();
            Process process = Runtime.getRuntime().exec(login);
            process.waitFor();
        }
        this.wait(500);
        return null;
    }


    private String getWarining(String filepath) {
        String result=null;
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(filepath, "r");
            long start = r.getFilePointer();
            long nextend = start + r.length() - 1;
            //System.out.println(nextend);
            r.seek(nextend);
            int c = -1;
            while (nextend >= start) {
                c = r.read();
                if (c == '\n' || c == '\r') {
                    result = r.readLine();
                    //return result;
                    if(result!=null) {
                        return result;//打印在控制台
                    }
                    //TODO 此处可以自行对result进行操作
                    nextend--;
                }
                nextend--;
                if(nextend>=0) {
                    r.seek(nextend);
                    if (nextend == 0) {// 当文件指针退至文件开始处，输出第一行
                        return r.readLine();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (r != null)
                try {
                    r.close();
                    File dir=new File("/home/Aroot/nohup.out");
                    dir.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return result;
        }
    }

    public String getCode() throws IOException, InterruptedException {

        String temp = getWarining("/home/Aroot/nohup.out");
        int a = temp.indexOf("code ")+5;
        String code = temp.substring(a,a+9);
        return code;
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
    public String createVmLinux(String subscription_id,String VNET_NAME,String VM_NAME,String USERNAME,String PASSWORD,
                              String VM_SIZE,String RESOURCE_GROUP_NAME) throws IOException, InterruptedException {
        /*String cmds = String.format("python3 /home/Aroot/pythonProject/createVMargs/venv/creatVM_args.py %s %s %s %s %s %s %s",
                subscription_id, VNET_NAME,VM_NAME,USERNAME,PASSWORD,VM_SIZE,RESOURCE_GROUP_NAME);
        String vm[] = {"/bin/sh","-c",cmds};
        System.out.println("\nExecuting python script file now.");
        Process pcs = Runtime.getRuntime().exec(vm);
        pcs.waitFor();*/

        String startCmd = String.format("python3 /home/Aroot/pythonProject/createVMargs/venv/creatVM_args.py %s %s %s %s %s %s %s",
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
        return "success";
    }

    /**
     *启动虚拟机
     * @param GROUP_NAME 资源组名称
     * @param OS_DISK_NAME  磁盘名称
     * @param VM_NAME   虚拟机名称
     * @return 启动结果
     * @throws IOException
     */
    public String startVM(String GROUP_NAME,String OS_DISK_NAME,String VM_NAME) throws IOException {
        String startCmd = String.format("python3 /home/Aroot/pythonProject/createVMargs/venv/startVM.py %s %s %s"
                ,GROUP_NAME,OS_DISK_NAME,VM_NAME);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        return  sb.toString();
    }

    /**
     * 停止虚拟机
     * @param GROUP_NAME 资源组名称
     * @param OS_DISK_NAME  磁盘名称
     * @param VM_NAME   虚拟机名称
     * @return 停止结果
     * @throws IOException
     */
    public String stopVM(String GROUP_NAME,String OS_DISK_NAME,String VM_NAME) throws IOException {
        String startCmd = String.format("python3 /home/Aroot/pythonProject/createVMargs/venv/stopVM.py %s %s %s",
                GROUP_NAME,OS_DISK_NAME,VM_NAME);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        return  sb.toString();
    }

    /**
     * 获得所有虚拟机信息
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public JSONArray getAllVMlist() throws InterruptedException, IOException {
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
}