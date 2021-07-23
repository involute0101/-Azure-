package com.example.demo.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @ClassName: LogService
 * @Author: 郭展
 * @Date: 2021/7/15
 * @Description: Azure日志service层
 */
@Service
public class LogService {
    /**
     * 获得所有资源组
     * @param resourceGroup 资源组名称
     * @return  返回所有资源组，
     *          JSONArray型
     * @throws IOException
     */
    public JSONArray getLogInGroup(String resourceGroup) throws IOException {
        if(resourceGroup == null){throw new IllegalArgumentException("argument illegal;null");}
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
        if(logArray == null || logArray.size() <=0){return result;}
        for(Object o :logArray){
            JSONObject logInfo = (JSONObject) o;
            if (logInfo == null || logInfo.isEmpty()){continue;}
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
     * @return 所有日志信息
     *          JSONArray型
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
            if(logInfo == null || logInfo.isEmpty()){continue;}
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
