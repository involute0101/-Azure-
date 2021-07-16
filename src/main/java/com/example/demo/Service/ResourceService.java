package com.example.demo.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author 郭展
 * @date 2021-07-15
 */
@Service
public class ResourceService {
    /**
     * 得到所有所有资源
     * @throws IOException
     */
    public JSONArray getAllResourece() throws IOException {
        String startCmd = String.format("python3 /home/Aroot/pythonProject/virtualPy/getAllResource.py ");
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        JSONArray result = new JSONArray();
        String resourceArray[] = sb.toString().split("@");
        for(String resource : resourceArray)
        {
            String resoucerInfo[] = resource.split("/");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",resoucerInfo[0]);
            jsonObject.put("type",resoucerInfo[1]);
            result.add(jsonObject);
        }
        return result;
    }

    /**
     * 得到所有资源组
     * @return
     * @throws IOException
     */
    public JSONArray getAllResourceGroup() throws IOException {
        String startCmd = String.format("python3 /home/Aroot/pythonProject/virtualPy/getAllResourceGroup.py ");
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        JSONArray result = new JSONArray();
        String resourceArray[] = sb.toString().split("@");
        for(String resource : resourceArray)
        {
            String resoucerInfo[] = resource.split("/");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",resoucerInfo[0]);
            jsonObject.put("location",resoucerInfo[1]);
            result.add(jsonObject);
        }
        return result;
    }

    /**
     * 得到资源组内所有资源
     * @param resName   资源组名称
     * @return
     * @throws IOException
     */
    public JSONArray getResourceInGroup(String resName) throws IOException {
        String startCmd = String.format("python3 /home/Aroot/pythonProject/virtualPy/getResourceInGroup.py %s",resName);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        JSONArray result = new JSONArray();
        String resourceArray[] = sb.toString().split("@");
        for(String resource : resourceArray)
        {
            String resoucerInfo[] = resource.split("/");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",resoucerInfo[0]);
            jsonObject.put("type",resoucerInfo[1]);
            result.add(jsonObject);
        }
        return result;
    }


}
