from azure.common.client_factory import get_client_from_cli_profile
from azure.mgmt.resource import ResourceManagementClient
from azure.mgmt.sql import SqlManagementClient
from azure.mgmt.resource import ResourceManagementClient
from azure.common.client_factory import get_client_from_auth_file
from azure.mgmt.compute import ComputeManagementClient
def createSqlServe():
    RESOURCE_GROUP = "NologinTest"
    LOCATION = 'australiaeast'  # example Azure availability zone, should match resource group
    SQL_SERVER = 'sqltest6866'
    SQL_DB = 'YOUR_SQLDB_NAME'
    USERNAME = 'Aroot'
    PASSWORD = 'abc123456/'
    # client = get_client_from_auth_file(ComputeManagementClient,subscriptionId="fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6")
    # credentials = ServicePrincipalCredentials(  # 虚拟机用
    #     client_id="043f8d98-95d1-473f-9471-6bcc6fb38d30",
    #     secret="o2Ms_n_y1DMR6-j6H-j_tyzlGNXj8s3I14",
    #     tenant="fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6""fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6"
    # )
    # create resource client
    resource_client = get_client_from_cli_profile(ResourceManagementClient)
    # resource_client = ResourceManagementClient(credentials,"fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6")
    # create resource group
    resource_client.resource_groups.create_or_update(RESOURCE_GROUP, {'location': LOCATION})

    sql_client = get_client_from_cli_profile(SqlManagementClient)

    # Create a SQL server
    server = sql_client.servers.create_or_update(
        RESOURCE_GROUP,
        SQL_SERVER,
        {
            'location': LOCATION,
            'version': '12.0', # Required for create
            'administrator_login': USERNAME, # Required for create
            'administrator_login_password': PASSWORD # Required for create
        }
    )
    return sql_client

    # # Create a SQL database in the Basic tier
    # database = sql_client.databases.create_or_update(
    #     RESOURCE_GROUP,
    #     SQL_SERVER,
    #     SQL_DB,
    #     {
    #         'location': LOCATION,
    #         'collation': 'SQL_Latin1_General_CP1_CI_AS',
    #         'create_mode': 'default',
    #         'requested_service_objective_name': 'Basic'
    #     }
    # )

    # Open access to this server for IPs
    # firewall_rule = sql_client.firewall_rules.create_or_update(
    #     RESOURCE_GROUP,
    #     SQL_DB,
    #     "firewall_rule_name_123.123.123.123",
    #     "123.123.123.123", # Start ip range
    #     "167.220.0.235"  # End ip range
    # )
def createSql(SQL_SERVER,SQL_DB):
    RESOURCE_GROUP = "NologinTest"
    LOCATION = 'australiaeast'  # example Azure availability zone, should match resource group
    # SQL_DB = 'Thisissql'
    # SQL_SERVER = 'sqltest6866'
    # client = get_client_from_auth_file(ComputeManagementClient,subscriptionId="fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6")
    # credentials = ServicePrincipalCredentials(  # 虚拟机用
    #     client_id="043f8d98-95d1-473f-9471-6bcc6fb38d30",
    #     secret="o2Ms_n_y1DMR6-j6H-j_tyzlGNXj8s3I14",
    #     tenant="fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6""fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6"
    # )
    # create resource client
    # resource_client = get_client_from_cli_profile(ResourceManagementClient)
    # # resource_client = ResourceManagementClient(credentials,"fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6")
    # # create resource group
    # resource_client.resource_groups.create_or_update(RESOURCE_GROUP, {'location': LOCATION})

    sql_client = get_client_from_cli_profile(SqlManagementClient)

    # Create a SQL server
    # server = sql_client.servers.create_or_update(
    #     RESOURCE_GROUP,
    #     SQL_SERVER,
    #     {
    #         'location': LOCATION,
    #         'version': '12.0', # Required for create
    #         'administrator_login': USERNAME, # Required for create
    #         'administrator_login_password': PASSWORD # Required for create
    #     }
    # )

    # Create a SQL database in the Basic tier
    database = sql_client.databases.create_or_update(
        RESOURCE_GROUP,
        SQL_SERVER,
        SQL_DB,
        {
            'location': LOCATION,
            'collation': 'SQL_Latin1_General_CP1_CI_AS',
            'create_mode': 'default',
            'requested_service_objective_name': 'Basic'
        }
    )

    # Open access to this server for IPs
    # firewall_rule = sql_client.firewall_rules.create_or_update(
    #     RESOURCE_GROUP,
    #     SQL_DB,
    #     "firewall_rule_name_123.123.123.123",
    #     "123.123.123.123", # Start ip range
    #     "167.220.0.235"  # End ip range
    # )
# sqlClient=createSqlServe()
createSql('sqltest6866','Thisissql2')