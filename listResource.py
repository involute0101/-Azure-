# Import the needed credential and management objects from the libraries.
from azure.common.credentials import ServicePrincipalCredentials
from azure.mgmt.resource import ResourceManagementClient
import os
AZURE_SUBSCRIPTION_ID="fc4bf4a7-37a5-46c5-bd67-002062908beb"
def getAllResGroup():
    # Acquire a credential object using CLI-based authentication.
    credential = ServicePrincipalCredentials(  # 虚拟机用
        client_id="043f8d98-95d1-473f-9471-6bcc6fb38d30",
        secret="o2Ms_n_y1DMR6-j6H-j_tyzlGNXj8s3I14",
        tenant="fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6")

    # Retrieve subscription ID from environment variable.
    subscription_id = "fc4bf4a7-37a5-46c5-bd67-002062908beb"

    # Obtain the management object for resources.
    resource_client = ResourceManagementClient(credential, subscription_id)

    # Retrieve the list of resource groups
    group_list = resource_client.resource_groups.list()

    # Show the groups in formatted output
    column_width = 40

    print("Resource Group".ljust(column_width) + "Location")
    print("-" * (column_width * 2))

    for group in list(group_list):
        print(f"{group.name:<{column_width}}{group.location}")

def getSpeRes(resName):

    # Acquire a credential object using CLI-based authentication.
    credential = ServicePrincipalCredentials(  # 虚拟机用
        client_id="043f8d98-95d1-473f-9471-6bcc6fb38d30",
        secret="o2Ms_n_y1DMR6-j6H-j_tyzlGNXj8s3I14",
        tenant="fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6")

    # Retrieve subscription ID from environment variable.
    subscription_id = "fc4bf4a7-37a5-46c5-bd67-002062908beb"

    # Obtain the management object for resources.
    resource_client = ResourceManagementClient(credential, subscription_id)

    # Retrieve the list of resources in "myResourceGroup" (change to any name desired).
    # The expand argument includes additional properties in the output.
    resource_list = resource_client.resources.list_by_resource_group(
        resName,
        expand="createdTime,changedTime"
    )

    # Show the resources in formatted output
    column_width = 36

    print("Resource".ljust(column_width) + "Type".ljust(column_width))
    print("-" * (column_width * 4))

    for resource in list(resource_list):
        print(f"{resource.name:<{column_width}}{resource.type:<{column_width}}")
# getAllRes()
getSpeRes("NologinTest")