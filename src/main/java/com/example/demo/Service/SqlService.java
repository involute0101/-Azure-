package com.example.demo.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.rest.PagedIterable;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.resources.fluent.ResourceManagementClient;
import com.azure.resourcemanager.resources.implementation.ResourceManagementClientImpl;
import com.azure.resourcemanager.resources.models.ResourceGroup;
import com.azure.resourcemanager.resources.models.ResourceGroups;
import com.azure.resourcemanager.sql.models.SqlDatabase;
import com.azure.resourcemanager.sql.models.SqlServer;
import com.example.demo.Utils.SqlDBUtils;
import com.microsoft.azure.management.sql.implementation.DatabasesImpl;
import org.springframework.stereotype.Service;


import java.util.Iterator;

/**
 * @author 郭展
 * @date 2021-07-13
 */
@Service
public class SqlService {
    /**
     * 得到Azure资源管理，需要已经登录
     * @return
     */
    private AzureResourceManager getAzureResourceManager()
    {
        final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
        final TokenCredential credential = new DefaultAzureCredentialBuilder()
                .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
                .build();

        AzureResourceManager azureResourceManager = AzureResourceManager
                .configure()
                .withLogLevel(HttpLogDetailLevel.BASIC)
                .authenticate(credential, profile)
                .withDefaultSubscription();

        return azureResourceManager;
    }

    /**
     * 创建数据库
     * @param userName  用户名
     * @param password  密码
     * @param DBname    数据库名称
     * @param resourceGroupName 资源组名称
     */
    public void createDB(String userName,String password,String DBname,String resourceGroupName)
    {
        AzureResourceManager azureResourceManager = getAzureResourceManager();
        System.out.println("资源组：\n"+azureResourceManager.resourceGroups().list());
        System.out.println("公共ip：\n"+azureResourceManager.publicIpAddresses().list());
        System.out.println("订阅：\n"+azureResourceManager.subscriptions().list());
        System.out.println("虚拟机列表：\n"+azureResourceManager.virtualMachines().list());
        final String sqlServerName = ("SqlServer"+DBname).toLowerCase();
        final String rgName = resourceGroupName;
        final String administratorLogin = userName;
        final String administratorPassword = password;
        final String firewallRuleIPAddress = "10.0.0.1";
        final String firewallRuleStartIPAddress = "10.2.0.1";
        final String firewallRuleEndIPAddress = "10.2.0.10";
        final String databaseName = DBname;

        System.out.println("Creating a sqlServer");
        SqlServer sqlServer = azureResourceManager.sqlServers().define(sqlServerName)
                .withRegion(Region.JAPAN_EAST)
                .withNewResourceGroup(rgName)
                .withAdministratorLogin(administratorLogin)
                .withAdministratorPassword(administratorPassword)
                .defineFirewallRule("filewallRule1").withIpAddress(firewallRuleIPAddress).attach()
                .defineFirewallRule("filewallRule2")
                .withIpAddressRange(firewallRuleStartIPAddress, firewallRuleEndIPAddress).attach()
                .create();

        System.out.println("Creating a database");

        SqlDatabase database = sqlServer.databases()
                .define(databaseName)
                .create();
    }

    /**
     * 删除数据库
     * @param SqlServerId 数据库服务器id
     * @param DBname 数据库名称
     */
    public void deleteDb(String SqlServerId,String DBname)
    {
        AzureResourceManager azureResourceManager = getAzureResourceManager();
        SqlServer sqlServer = azureResourceManager.sqlServers().getById(SqlServerId);
        SqlDatabase database =sqlServer.databases().get(DBname);
        database.delete();
    }

    /**
     * 得到所有数据库
     * @return 数据库列表
     */
    public JSONArray getAllDb()
    {
        AzureResourceManager azureResourceManager = getAzureResourceManager();
        JSONArray result = new JSONArray();
        Iterator<SqlServer> serverIterator = azureResourceManager.sqlServers().list().iterator();
        while(serverIterator.hasNext()){
            SqlServer server = serverIterator.next();
            Iterator<SqlDatabase> dbIterator = server.databases().list().iterator();
            while(dbIterator.hasNext()){
                SqlDatabase sqlDatabase = dbIterator.next();
                if (sqlDatabase.name().equals("master")){continue;}
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("SqlServerId",server.id());
                jsonObject.put("sqlServerName",sqlDatabase.sqlServerName());
                jsonObject.put("resourceGroupName",sqlDatabase.resourceGroupName());
                jsonObject.put("name",sqlDatabase.name());
                jsonObject.put("status",sqlDatabase.status());
                jsonObject.put("creationDate",sqlDatabase.creationDate());
                jsonObject.put("regionName",sqlDatabase.regionName());
                jsonObject.put("maxSizeBytes",sqlDatabase.maxSizeBytes());
                jsonObject.put("id",sqlDatabase.databaseId());
                result.add(jsonObject);
            }
        }
        return result;
    }

}
