# Import the needed credential and management objects from the libraries.
from azure.mgmt.resource import ResourceManagementClient
from azure.common.client_factory import get_client_from_cli_profile
import os
def getAllRes():
    # Acquire a credential object using CLI-based authentication.
    # Obtain the management object for resources.
    resource_client = get_client_from_cli_profile(ResourceManagementClient)
    # Retrieve the list of resource groups
    group_list = resource_client.resource_groups.list()
    # Show the groups in formatted output
    for group in list(group_list):
        # print(f"{group.name:<{column_width}}{group.location}")
        getSpeRes(group.name)

def getSpeRes(resName):

    # Acquire a credential object using CLI-based authentication.
    #请先通过az cli登录
    resource_client = get_client_from_cli_profile(ResourceManagementClient)
    # Retrieve the list of resources in "myResourceGroup" (change to any name desired).
    # The expand argument includes additional properties in the output.
    resource_list = resource_client.resources.list_by_resource_group(
        resName,
        expand="createdTime,changedTime"
    )

    for resource in list(resource_list):
        begin = resource.type.rfind("/")
        shortType = resource.type[begin+1:-1]
        print(resource.name+"/"+shortType+"@")

def getAllResourceGroup():
    # Acquire a credential object using CLI-based authentication.
    #请先通过az cli登录
    resource_client = get_client_from_cli_profile(ResourceManagementClient)
    # Retrieve the list of resource groups
    group_list = resource_client.resource_groups.list()
    # Show the groups in formatted output
    for group in list(group_list):
        print(group.name+"/"+group.location+"@")