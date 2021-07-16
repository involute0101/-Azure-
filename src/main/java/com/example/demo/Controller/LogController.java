package com.example.demo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.Service.LogService;
import io.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/Log")
@Api(tags = "Azure-Log-controller")
@NoArgsConstructor
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping(value = "/LogInGroup")
    public JSONArray getLogInGroup(String resourceGroup) throws IOException {
        return logService.getLogInGroup(resourceGroup);
    }

    @GetMapping(value = "/allAlert")
    public JSONArray getAllLog() throws IOException {
        return logService.getAllLog();
    }
}
