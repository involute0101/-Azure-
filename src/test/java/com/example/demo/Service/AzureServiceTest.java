package com.example.demo.Service;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName: AzureServiceTest
 * @Author: 郭展
 * @Date: 2021/7/16
 * @Description: 测试
 */
class AzureServiceTest {

    //不自动配置，测试直接new
    private AzureService azureService = new AzureService();

    /**
     * 对getVMShowInfobyName测试
     * 包含正常、异常和边界测试
     */
    @Test
    void getVMShowInfobyName(){
        //异常测试
        try {
            JSONObject vmShowInfobyName = azureService.getVMShowInfobyName(null, null);
        }catch (Exception e)
        {
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            JSONObject vmShowInfobyName = azureService.getVMShowInfobyName("NologinTest", "hhhhh");
            assertTrue(vmShowInfobyName == null);
            JSONObject vmShowInfobyName2 = azureService.getVMShowInfobyName("hhh", "bushu");
            assertTrue(vmShowInfobyName2 == null);
            JSONObject vmShowInfobyName3 = azureService.getVMShowInfobyName("hhhh", "gggg");
            assertTrue(vmShowInfobyName3 == null);
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }

        //正常测试
        try{
            JSONObject vmShowInfobyName1 = azureService.getVMShowInfobyName("NologinTest", "bushu");
            assertTrue(!vmShowInfobyName1.isEmpty());
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }
    }

    /**
     * 对 getAllVMlist 测试
     * 包含正常、异常和边界测试
     */
    @Test
    void getAllVMlist() {
        try{
            azureService.getAllVMlist();
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }
    }

    /**
     * 对 getSubscription 测试
     * 包含正常、异常和边界测试
     */
    @Test
    void getSubscription() {
        try{
            azureService.getSubscription();
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }
    }

    /**
     * 对 getVMStatus 测试
     * 包含正常、异常和边界测试
     */
    @Test
    void getVMStatus() {
        //异常测试
        try{
            azureService.getVMStatus(null,null);
        }catch (Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            String vmStatus = azureService.getVMStatus("hhhh", "bushu");
            assertTrue(vmStatus.equals("error!\n virtual machine or resource group isn't exist;"));
            String vmStatus1 = azureService.getVMStatus("NologinTest", "shhshs");
            assertTrue(vmStatus1.equals("error!\n virtual machine or resource group isn't exist;"));
            String vmStatus2 = azureService.getVMStatus("sasa", "dwwdas");
            assertTrue(vmStatus.equals("error!\n virtual machine or resource group isn't exist;"));
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }

        //正常测试
        try{
            String vmStatus = azureService.getVMStatus("NologinTest", "bushu");
            assertTrue(vmStatus.equals("VM running"));
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }
    }

    /**
     * 对 getVMip 测试
     * 包含正常、异常和边界测试
     */
    @Test
    void getVMip() {
        //异常测试
        try{
            azureService.getVMip(null);
        }catch (Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            String vMip = azureService.getVMip("sasasa");
            assertTrue(vMip.equals("virtual machine isn't exist;"));
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }

        //正常测试
        try{
            String vMip = azureService.getVMip("bushu");
            assertTrue(vMip.equals("52.147.1.31"));
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }
    }

    /**
     * 对 getVmNetworkData 测试
     * 包含正常、异常和边界测试
     */
    @Test
    void getVmNetworkData() {
        //异常测试
        try{
            azureService.getVmNetworkData(null,null,null);
        }catch (Exception e)
        {
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            JSONObject VmNetworkData = azureService.getVmNetworkData("xxxx", "xxxx", "xxx");
            assertTrue(VmNetworkData.getString("message").equals("this subscriptionId or resourceGroup or vmName isn't exist;"));
            JSONObject VmNetworkData1 = azureService.getVmNetworkData("fc4bf4a7-37a5-46c5-bd67-002062908beb", "xxxx", "xxx");
            assertTrue(VmNetworkData1.getString("message").equals("this subscriptionId or resourceGroup or vmName isn't exist;"));
            JSONObject VmNetworkData2 = azureService.getVmNetworkData("fc4bf4a7-37a5-46c5-bd67-002062908beb", "NologinTest", "xxx");
            assertTrue(VmNetworkData2.getString("message").equals("this subscriptionId or resourceGroup or vmName isn't exist;"));
            JSONObject VmNetworkData3 = azureService.getVmNetworkData("fc4bf4a7-37a5-46c5-bd67-002062908beb", "NologinTest", "bushu");
            assertTrue(VmNetworkData3.getString("message").equals("this subscriptionId or resourceGroup or vmName isn't exist;"));
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }

        //正常测试
        try {
            JSONObject VmNetworkData = azureService.getVmNetworkData("fc4bf4a7-37a5-46c5-bd67-002062908beb", "NologinTest", "bushu");
            assertTrue(VmNetworkData.getString("message").isEmpty());
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }
    }

    /**
     * 对 getVmCpuData 测试
     * 包含正常、异常和边界测试
     */
    @Test
    void getVmCpuData() {
        //异常测试
        try{
            azureService.getVmCpuData(null,null,null);
        }catch (Exception e)
        {
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            JSONObject vmCpuData = azureService.getVmCpuData("xxxx", "xxxx", "xxx");
            assertTrue(vmCpuData.getString("message").equals("this subscriptionId or resourceGroup or vmName isn't exist;"));
            JSONObject vmCpuData1 = azureService.getVmCpuData("fc4bf4a7-37a5-46c5-bd67-002062908beb", "xxxx", "xxx");
            assertTrue(vmCpuData1.getString("message").equals("this subscriptionId or resourceGroup or vmName isn't exist;"));
            JSONObject vmCpuData2 = azureService.getVmCpuData("fc4bf4a7-37a5-46c5-bd67-002062908beb", "NologinTest", "xxx");
            assertTrue(vmCpuData2.getString("message").equals("this subscriptionId or resourceGroup or vmName isn't exist;"));
            JSONObject vmCpuData3 = azureService.getVmCpuData("fc4bf4a7-37a5-46c5-bd67-002062908beb", "NologinTest", "bushu");
            assertTrue(vmCpuData3.getString("message").equals("this subscriptionId or resourceGroup or vmName isn't exist;"));
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }

        //正常测试
        try {
            JSONObject vmCpuData = azureService.getVmCpuData("fc4bf4a7-37a5-46c5-bd67-002062908beb", "NologinTest", "bushu");
            assertTrue(vmCpuData.getString("message").isEmpty());
        }catch (Exception e){
            assertTrue(e instanceof IOException);
        }
    }
}