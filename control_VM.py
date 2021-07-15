import os
import traceback
from azure.mgmt.kusto import KustoManagementClient
from azure.mgmt.kusto.models import Cluster, AzureSku
from azure.mgmt.resource import ResourceManagementClient
from azure.mgmt.network import NetworkManagementClient
from azure.mgmt.compute import ComputeManagementClient
from azure.mgmt.compute.models import DiskCreateOption
from azure.common.client_factory import get_client_from_cli_profile


from msrestazure.azure_exceptions import CloudError

from haikunator import Haikunator

class VMController:
    def __init__(self,LOCATION,GROUP_NAME,OS_DISK_NAME,VM_NAME):
        self.haikunator = Haikunator()
        self.LOCATION=LOCATION
        self.GROUP_NAME=GROUP_NAME
        self.client_id="043f8d98-95d1-473f-9471-6bcc6fb38d30"
        self.secret="o2Ms_n_y1DMR6-j6H-j_tyzlGNXj8s3I14"
        self.tenant="fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6""fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6"
        self.subscription_id = "fc4bf4a7-37a5-46c5-bd67-002062908beb"
        self.OS_DISK_NAME=OS_DISK_NAME
        self.VM_NAME=VM_NAME
        #微软旧包
        # self.credentials = ServicePrincipalCredentials(#虚拟机用
        #     client_id=self.client_id,
        #     secret=self.secret,
        #     tenant=self.tenant
        # )#微软新包
        # self.newcredentials = ClientSecretCredential(  tenant_id=self.tenant,
        #                                             client_id=self.client_id,
        #                                             client_secret=self.secret)

        # 此处取得权限

        self.resource_client =get_client_from_cli_profile(ResourceManagementClient)
        self.compute_client = get_client_from_cli_profile(ComputeManagementClient)
        self.network_client = get_client_from_cli_profile(NetworkManagementClient)
        #Get Virtual Machine by Name
        self.virtual_machine = self.compute_client.virtual_machines.get(
            self.GROUP_NAME,
            self.VM_NAME
        )
        #下面是sql参数
        # self.sqlresource_client = ResourceManagementClient(self.newcredentials, self.subscription_id)
        # self.sqlcompute_client = ComputeManagementClient(self.newcredentials, self.subscription_id)
        # self.sqlnetwork_client = NetworkManagementClient(self.newcredentials, self.subscription_id)
        # self.sku_name = 'Standard_E2a_v4'#型号
        # self.capacity = 5#GB
        # self.sqlLocation = "australiaeast"
        # self.tier = "Standard"#SKU 层
        # self.resource_group_name = 'mysqlTest'#资源组名字
        # self.cluster_name = 'mykustocluster22233'#所需的群集名称
        # self.soft_delete_period = timedelta(days=10)
        # self.hot_cache_period = timedelta(days=10)
        # self.database_name = "mykustodatabase"
        # self.cluster = Cluster(location=self.sqlLocation, sku=AzureSku(name=self.sku_name, capacity=self.capacity, tier=self.tier))
        #需要创建一个管理
        # print('\nCreate (empty) managed Data Disk')
        # async_disk_creation = self.compute_client.disks.create_or_update(
        #     GROUP_NAME,
        #     'mydatadisk1',
        #     {
        #         'location': self.LOCATION,
        #         'disk_size_gb': self.disk_size_gb,
        #         'creation_data': {
        #             'create_option': DiskCreateOption.empty
        #         }
        #     }
        # )
        # self.data_disk = async_disk_creation.result()
    # Azure Datacenter
    # LOCATION = 'westus'
    #
    # # Resource Group
    # GROUP_NAME = 'azure-sample-group-virtual-machines'
    #
    # # Network
    # VNET_NAME = 'azure-sample-vnet'
    # SUBNET_NAME = 'azure-sample-subnet'
    #
    # # VM
    # OS_DISK_NAME = 'azure-sample-osdisk'
    # STORAGE_ACCOUNT_NAME = haikunator.haikunate(delimiter='')
    #
    # IP_CONFIG_NAME = 'azure-sample-ip-config'
    # NIC_NAME = 'azure-sample-nic'
    # USERNAME = 'userlogin'
    # PASSWORD = 'Pa$$w0rd91'
    # VM_NAME = 'VmName'

    # def get_credentials(subscription_id,client_id,secret,tenant):
    #     #subscription_id = os.environ['AZURE_SUBSCRIPTION_ID']
    #     #credentials = ServicePrincipalCredentials(
    #     #    client_id=os.environ['AZURE_CLIENT_ID'],
    #     #    secret=os.environ['AZURE_CLIENT_SECRET'],
    #     #    tenant=os.environ['AZURE_TENANT_ID']
    #     #)

    #这个我也不知道啥意思
    # def TagVM(self):
    #     print('\nTag Virtual Machine')
    #     async_vm_update = compute_client.virtual_machines.create_or_update(
    #         GROUP_NAME,
    #         VM_NAME,
    #         {
    #             'location': self.LOCATION,
    #             'tags': {
    #                 'who-rocks': 'python',
    #                 'where': 'on azure'
    #             }
    #         }
    #     )
    # async_vm_update.wait()

    # Create managed data disk



    # Get the virtual machine by name
    #print('\nGet Virtual Machine by Name')
    # virtual_machine = compute_client.virtual_machines.get(
    #     GROUP_NAME,
    #     VM_NAME
    # )

    # Attach data disk
    def attachDisk(self,lun,name):
        print('\nAttach Data Disk')
        self.virtual_machine.storage_profile.data_disks.append({
            'lun': lun,#逻辑单元号（Logic Unit Number）
            'name': name,
            'create_option': DiskCreateOption.attach,
            'managed_disk': {
                'id': self.data_disk.id
            }
        })
        async_disk_attach = self.compute_client.virtual_machines.create_or_update(
            self.GROUP_NAME,
            self.virtual_machine.name,
            self.virtual_machine
        )
        async_disk_attach.wait()

    # Detach data disk
    def detachDisk(self,diskname):
        print('\nDetach Data Disk')
        data_disks = self.virtual_machine.storage_profile.data_disks
        data_disks[:] = [
            disk for disk in data_disks if disk.name != diskname]
        async_vm_update = self.compute_client.virtual_machines.create_or_update(
            GROUP_NAME,
            VM_NAME,
            virtual_machine
        )
        virtual_machine = async_vm_update.result()

    # Deallocating the VM (in preparation for a disk resize)
    # 释放虚拟机(为调整磁盘大小做准备)
    def deallocatingVM(self):
        print('\nDeallocating the VM (to prepare for a disk resize)')
        async_vm_deallocate = self.compute_client.virtual_machines.deallocate(
            self.GROUP_NAME, self.VM_NAME)
        async_vm_deallocate.wait()

    # Increase OS disk size by 10 GB
    def increaseDisk(self,disk_size_gb):
        print('\nUpdate OS disk size')
        os_disk_name = self.virtual_machine.storage_profile.os_disk.name
        os_disk = self.compute_client.disks.get(GROUP_NAME, os_disk_name)
        if not os_disk.disk_size_gb:
            print(
                "\tServer is not returning the OS disk size, possible bug in the server?")
            # print("\tAssuming that the OS disk size is 30 GB")
            # os_disk.disk_size_gb = 30
            return

        os_disk.disk_size_gb += disk_size_gb

        async_disk_update =self.compute_client.disks.create_or_update(
            GROUP_NAME,
            os_disk.name,
            os_disk
        )
        async_disk_update.wait()

    # Start the VM
    def startVM(self):
        print('\nStart VM')
        async_vm_start = self.compute_client.virtual_machines.begin_start(
            self.GROUP_NAME, self.VM_NAME)
        async_vm_start.wait()

    # Restart the VM
    def restartVM(self):
        print('\nRestart VM')
        async_vm_restart = self.compute_client.virtual_machines.begin_restart(
            self.GROUP_NAME, self.VM_NAME)
        async_vm_restart.wait()

    # Stop the VM
    def stopVM(self):
        print('\nStop VM')
        async_vm_stop = self.compute_client.virtual_machines.begin_power_off(
            self.GROUP_NAME, self.VM_NAME)
        async_vm_stop.wait()

    # List VMs in subscription
    def listVM(self):
        print('\nList VMs in subscription')
        for vm in self.compute_client.virtual_machines.list_all():
            print("\tVM: {}".format(vm.name))

    # List VM in resource group
    def listResourcegroup(self):
        print('\nList VMs in resource group')
        for vm in self.compute_client.virtual_machines.list(self.GROUP_NAME):
            print("\tVM: {}".format(vm.name))

    # Delete VM
    def deleteVM(self):
        print('\nDelete VM')
        async_vm_delete = self.compute_client.virtual_machines.delete(
            self.GROUP_NAME, self.VM_NAME)
        async_vm_delete.wait()

    # Create Windows VM
    # print('\nCreating Windows Virtual Machine')
    # # Recycling NIC of previous VM
    # vm_parameters = create_vm_parameters(nic.id, VM_REFERENCE['windows'])
    # async_vm_creation = compute_client.virtual_machines.create_or_update(
    #     GROUP_NAME, VM_NAME, vm_parameters)
    # async_vm_creation.wait()
    # except CloudError:
    # print('A VM operation failed:\n{}'.format(traceback.format_exc()))
    # else:
    # print('All example operations completed successfully!')
    # finally:

    # Delete Resource group and everything in it
    def deleteResource(self):
        print('\nDelete Resource Group')
        delete_async_operation = self.resource_client.resource_groups.delete(
            self.GROUP_NAME)
        delete_async_operation.wait()
        print("\nDeleted: {}".format(GROUP_NAME))
    #sql操作从此处开始
    #创建群集
    #在创建数据库前需创建群集
    # def creatCluster(self):
    #     kusto_management_client = KustoManagementClient(self.newcredentials, self.subscription_id)
    #
    #     cluster_operations =kusto_management_client.clusters
    #
    #     poller = cluster_operations.begin_create_or_update(self.resource_group_name, self.cluster_name,self.cluster)
    #     poller.wait()
        #print(cluster_operations.get(resource_group_name = self.resource_group_name, cluster_name= self.cluster_name, custom_headers=None, raw=False))#判断是否创建成功
    # def creatSql(self):
    #     kusto_management_client = KustoManagementClient(self.newcredentials, self.subscription_id)
    #     database_operations = kusto_management_client.databases
    #     database = ReadWriteDatabase(location=self.LOCATION,
    #                                  soft_delete_period=self.soft_delete_period,
    #                                  hot_cache_period=self.hot_cache_period)
    #
    #     poller = database_operations.begin_create_or_update(resource_group_name=self.resource_group_name,
    #                                                   cluster_name=self.cluster_name, database_name=self.database_name,
    #                                                   parameters=database)
    #     poller.wait()
    #     #print(database_operations.get(resource_group_name = resource_group_name, cluster_name = cluster_name, database_name = database_name))
    # def deleteSql(self):
    #     cluster_operations.delete(resource_group_name=self.resource_group_name, cluster_name=self.cluster_name)


# test=VMController("australiaeast","Test1","ExampleVM_OsDisk_1_0d525b8269a541b5b6eec41d9c16e78b","ExampleVM")
# test.stopVM()
#for test
# test.creatSql()