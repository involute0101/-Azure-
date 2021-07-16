package com.example.demo.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class LogService {
    public JSONArray getLogInGroup(String resourceGroup) throws IOException {
        String startCmd = String.format("az monitor activity-log list --resource-group %s ",resourceGroup);
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        JSONArray logArray = JSONArray.parseArray(sb.toString());
        JSONArray result = new JSONArray();
        for(Object o :logArray){
            JSONObject logInfo = (JSONObject) o;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("submissionTimestamp",logInfo.getString("submissionTimestamp"));
            jsonObject.put("operationId",logInfo.getString("operationId"));
            jsonObject.put("operationName",logInfo.getJSONObject("operationName").getString("localizedValue"));
            jsonObject.put("caller",logInfo.getString("caller"));
            jsonObject.put("tenantId",logInfo.getString("tenantId"));
            jsonObject.put("status",logInfo.getJSONObject("status").getString("value"));
            result.add(jsonObject);
        }
        return result;
    }

    /**
     * 得到所有日志
     * @return
     * @throws IOException
     */
    public JSONArray getAllLog() throws IOException {
        String startCmd = String.format("az security alert list");
        String vm[] = {"/bin/sh","-c",startCmd};
        StringBuilder sb =new StringBuilder();
        Process process = Runtime.getRuntime().exec(vm);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null) {
            sb.append(line);
        }
        JSONArray logArray = JSONArray.parseArray(sb.toString());
        if(logArray.isEmpty()){return logArray;};
        JSONArray result = new JSONArray();
        for(Object o :logArray){
            JSONObject logInfo = (JSONObject) o;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("submissionTimestamp",logInfo.getString("submissionTimestamp"));
            jsonObject.put("operationId",logInfo.getString("operationId"));
            jsonObject.put("operationName",logInfo.getJSONObject("operationName").getString("localizedValue"));
            jsonObject.put("caller",logInfo.getString("caller"));
            jsonObject.put("tenantId",logInfo.getString("tenantId"));
            jsonObject.put("status",logInfo.getJSONObject("status").getString("value"));
            result.add(jsonObject);
        }
        return result;
    }
}
