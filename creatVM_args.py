from azure.identity import AzureCliCredential
from azure.mgmt.resource import ResourceManagementClient
from azure.mgmt.network import NetworkManagementClient
from azure.mgmt.compute import ComputeManagementClient
from azure.cli.core import get_default_cli
import os
import sys

print(f"Provisioning a virtual machine...some operations might take a minute or two.")
subscription_id = sys.argv[1]
VNET_NAME = sys.argv[2]
VM_NAME = sys.argv[3]
USERNAME = sys.argv[4]
PASSWORD = sys.argv[5]
vm_size = sys.argv[6]
RESOURCE_GROUP_NAME = sys.argv[7]

# 获得凭证
credential = AzureCliCredential()

# T:从环境变量检索订阅ID。
#subscription_id = os.environ["AZURE_SUBSCRIPTION_ID"]#订阅ID
#####################################subscription_id="ec269b4d-93af-43c5-9fd6-9a5185235344"#填上订阅ID

# T:通过CLI登录凭据获取资源的管理对象
resource_client = ResourceManagementClient(credential, subscription_id)


#资源组名称
LOCATION = "australiaeast"#资源组位置

# Provision the resource group.
rg_result = resource_client.resource_groups.create_or_update(RESOURCE_GROUP_NAME,
    {
        "location": LOCATION
    }
)

#下面这个可以注释，用于测试
print(f"Provisioned resource group {rg_result.name} in the {rg_result.location} region")

# A virtual machine requires a network interface client (NIC). A NIC requires
# a virtual network and subnet along with an IP address. Therefore we must provision
# these downstream components first, then provision the NIC, after which we
# can provision the VM.

# Network and IP address names
###################VNET_NAME = "gztest"#虚拟网络名称,要改
SUBNET_NAME = "python-example-subnet"#子网名称
IP_NAME = "python-example-ip"#公有ip名称
IP_CONFIG_NAME = "python-example-ip-config"#公有ip配置
NIC_NAME = "python-example-nic"#另外的名称

# Obtain the management object for networks
network_client = NetworkManagementClient(credential, subscription_id)

# Provision the virtual network and wait for completion
poller = network_client.virtual_networks.begin_create_or_update(RESOURCE_GROUP_NAME,
    VNET_NAME,
    {
        "location": LOCATION,
        "address_space": {
            "address_prefixes": ["10.0.0.0/16"]#固定
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
compute_client = ComputeManagementClient(credential, subscription_id)

#可修改
###############VM_NAME = "GzUbuntuVm" #要改
###############USERNAME = "guozhan"
###############PASSWORD = "GUOzhan963."
offer="UbuntuServer"
#vm_size ="Standard_B1s"#硬盘规格，用B1s是免费的
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
