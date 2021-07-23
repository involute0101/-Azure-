from azure.common.client_factory import get_client_from_cli_profile
from azure.mgmt.resource import ResourceManagementClient
from azure.mgmt.network import NetworkManagementClient
from azure.mgmt.compute import ComputeManagementClient

import os
import sys

def createVM():#修改一下即可
    print(f"Provisioning a virtual machine...some operations might take a minute or two.")

    # Acquire a credential object using CLI-based authentication.
    # credential = AzureCliCredential()#旧包无法获取token
    # newcredential = ClientSecretCredential(tenant_id="fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6",
    #                                                  client_id="043f8d98-95d1-473f-9471-6bcc6fb38d30",
    #                                                  client_secret="o2Ms_n_y1DMR6-j6H-j_tyzlGNXj8s3I14")
    # Retrieve subscription ID from environment variable.
    # T:从环境变量检索订阅ID。
    #subscription_id = os.environ["AZURE_SUBSCRIPTION_ID"]#订阅ID
    # subscription_id="fc4bf4a7-37a5-46c5-bd67-002062908beb"#填上订阅ID

    # Step 1: Provision a resource group

    # Obtain the management object for resources, using the credentials from the CLI login.
    # T:通过CLI登录凭据获取资源的管理对象
    resource_client = get_client_from_cli_profile(ResourceManagementClient)
    network_client = get_client_from_cli_profile(NetworkManagementClient)
    compute_client = get_client_from_cli_profile(ComputeManagementClient)
    # resource_client = ResourceManagementClient(credential, subscription_id)

    # Constants we need in multiple places: the resource group name and the region
    # in which we provision resources. You can change these values however you want.
    RESOURCE_GROUP_NAME = "Test1"#资源组名称，创建新的虚拟机时这个必须要改
    LOCATION = "australiaeast"#资源组位置
    # rg_result = resource_client.resource_groups.create_or_update(
    #     "PythonAzureExample-rg",
    #     {
    #         "location": LOCATION
    #     }
    # )
    # Provision the resource group.
    rg_result = resource_client.resource_groups.create_or_update(RESOURCE_GROUP_NAME,
        {
            "location": LOCATION
        }
    )

    #下面这个可以注释，用于测试
    print(f"Provisioned resource group {rg_result.name} in the {rg_result.location} region")

    # For details on the previous code, see Example: Provision a resource group
    # at https://docs.microsoft.com/azure/developer/python/azure-sdk-example-resource-group


    # Step 2: provision a virtual network

    # A virtual machine requires a network interface client (NIC). A NIC requires
    # a virtual network and subnet along with an IP address. Therefore we must provision
    # these downstream components first, then provision the NIC, after which we
    # can provision the VM.

    # Network and IP address names
    VNET_NAME = "test1234"#虚拟网络名称，创建新的虚拟机时这个必须要改
    SUBNET_NAME = "python"#子网名称
    IP_NAME = "python"#公有ip名称
    IP_CONFIG_NAME = "python"#公有ip配置
    NIC_NAME = "python"#另外的名称

    # Obtain the management object for networks


    # Provision the virtual network and wait for completion
    poller = network_client.virtual_networks.begin_create_or_update(RESOURCE_GROUP_NAME,
        VNET_NAME,
        {
            "location": LOCATION,
            "address_space": {
                "address_prefixes": ["10.0.0.0/16"]
            }
        }
    )

    vnet_result = poller.result()

    print(f"Provisioned virtual network {vnet_result.name} with address prefixes {vnet_result.address_space.address_prefixes}")

    # Step 3: Provision the subnet and wait for completion
    poller = network_client.subnets.begin_create_or_update(RESOURCE_GROUP_NAME,
        VNET_NAME, SUBNET_NAME,
        { "address_prefix": "10.0.0.0/24" }
    )
    subnet_result = poller.result()

    print(f"Provisioned virtual subnet {subnet_result.name} with address prefix {subnet_result.address_prefix}")

    # Step 4: Provision an IP address and wait for completion
    poller = network_client.public_ip_addresses.begin_create_or_update(RESOURCE_GROUP_NAME,
        IP_NAME,
        {
            "location": LOCATION,
            "sku": { "name": "Standard" },
            "public_ip_allocation_method": "Static",
            "public_ip_address_version" : "IPV4"
        }
    )

    ip_address_result = poller.result()

    print(f"Provisioned public IP address {ip_address_result.name} with address {ip_address_result.ip_address}")

    # Step 5: Provision the network interface client
    poller = network_client.network_interfaces.begin_create_or_update(RESOURCE_GROUP_NAME,
        NIC_NAME,
        {
            "location": LOCATION,
            "ip_configurations": [ {
                "name": IP_CONFIG_NAME,
                "subnet": { "id": subnet_result.id },
                "public_ip_address": {"id": ip_address_result.id }
            }]
        }
    )

    nic_result = poller.result()

    print(f"Provisioned network interface client {nic_result.name}")

    # Step 6: Provision the virtual machine

    # Obtain the management object for virtual machines


    #可修改
    VM_NAME = "ExampleVM"
    USERNAME = "azureuser"
    PASSWORD = "ChangePa$$w0rd24"
    offer="UbuntuServer"
    vm_size ="Standard_B1S"#硬盘规格，用B1S是免费的
    version = "latest"
    print(f"Provisioning virtual machine {VM_NAME}; this operation might take a few minutes.")

    # Provision the VM specifying only minimal arguments, which defaults to an Ubuntu 18.04 VM
    # on a Standard DS1 v2 plan with a public IP address and a default virtual network/subnet.

    #下面可改
    poller = compute_client.virtual_machines.begin_create_or_update(RESOURCE_GROUP_NAME, VM_NAME,
        {
            "location": LOCATION,
            "storage_profile": {
                "image_reference": {
                    "publisher": 'Canonical',
                    "offer": offer,
                    "sku": "16.04.0-LTS",
                    "version": version
                }
            },
            "hardware_profile": {
                "vm_size": vm_size
            },
            "os_profile": {
                "computer_name": VM_NAME,
                "admin_username": USERNAME,
                "admin_password": PASSWORD
            },
            "network_profile": {
                "network_interfaces": [{
                    "id": nic_result.id,
                }]
            }
        }
    )

    vm_result = poller.result()

    print(f"Provisioned virtual machine {vm_result.name}")
createVM()
