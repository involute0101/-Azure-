import os
import traceback

from azure.common.credentials import ServicePrincipalCredentials
from azure.mgmt.resource import ResourceManagementClient
from azure.mgmt.network import NetworkManagementClient
from azure.mgmt.compute import ComputeManagementClient
from azure.mgmt.compute.models import DiskCreateOption

from msrestazure.azure_exceptions import CloudError

from haikunator import Haikunator

class VMController:
    def __init__(self, LOCATION, GROUP_NAME,VNET_NAME,SUBNET_NAME,OS_DISK_NAME,IP_CONFIG_NAME,NIC_NAME,USERNAME,PASSWORD,VM_NAME):
        haikunator = Haikunator()
        self.LOCATION=LOCATION
        self.GROUP_NAME=GROUP_NAME
        self.VNET_NAME=VNET_NAME
        self.SUBNET_NAME=SUBNET_NAME
        self.OS_DISK_NAME=OS_DISK_NAME
        self.IP_CONFIG_NAME=IP_CONFIG_NAME
        self.NIC_NAME=NIC_NAME
        self.USERNAME=USERNAME
        self.PASSWORD=PASSWORD
        self.VM_NAME=VM_NAME
        credentials = ServicePrincipalCredentials(client_id, secret, tenant)

        # 此处取得权限
        self.credentials, self.subscription_id = get_credentials()
        self.resource_client = ResourceManagementClient(credentials, subscription_id)
        self.compute_client = ComputeManagementClient(credentials, subscription_id)
        self.network_client = NetworkManagementClient(credentials, subscription_id)
        #Get Virtual Machine by Name
        self.virtual_machine = compute_client.virtual_machines.get(
            GROUP_NAME,
            VM_NAME
        )
        #需要创建一个管理
        print('\nCreate (empty) managed Data Disk')
        self.async_disk_creation = compute_client.disks.create_or_update(
            GROUP_NAME,
            'mydatadisk1',
            {
                'location': self.LOCATION,
                'disk_size_gb': self.disk_size_gb,
                'creation_data': {
                    'create_option': DiskCreateOption.empty
                }
            }
        )
        self.data_disk = async_disk_creation.result()
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
    def TagVM(self):
        print('\nTag Virtual Machine')
        async_vm_update = compute_client.virtual_machines.create_or_update(
            GROUP_NAME,
            VM_NAME,
            {
                'location': self.LOCATION,
                'tags': {
                    'who-rocks': 'python',
                    'where': 'on azure'
                }
            }
        )
    async_vm_update.wait()

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
        virtual_machine.storage_profile.data_disks.append({
            'lun': lun,#逻辑单元号（Logic Unit Number）
            'name': name,
            'create_option': DiskCreateOption.attach,
            'managed_disk': {
                'id': self.data_disk.id
            }
        })
        async_disk_attach = compute_client.virtual_machines.create_or_update(
            self.GROUP_NAME,
            self.virtual_machine.name,
            self.virtual_machine
        )
        async_disk_attach.wait()

    # Detach data disk
    def detachDisk(self,diskname):
        print('\nDetach Data Disk')
        data_disks = virtual_machine.storage_profile.data_disks
        data_disks[:] = [
            disk for disk in data_disks if disk.name != diskname]
        async_vm_update = compute_client.virtual_machines.create_or_update(
            GROUP_NAME,
            VM_NAME,
            virtual_machine
        )
        virtual_machine = async_vm_update.result()

    # Deallocating the VM (in preparation for a disk resize)
    # 释放虚拟机(为调整磁盘大小做准备)
    def deallocatingVM(self):
        print('\nDeallocating the VM (to prepare for a disk resize)')
        async_vm_deallocate = compute_client.virtual_machines.deallocate(
            self.GROUP_NAME, self.VM_NAME)
        async_vm_deallocate.wait()

    # Increase OS disk size by 10 GB
    def increaseDisk(self,disk_size_gb):
        print('\nUpdate OS disk size')
        os_disk_name = virtual_machine.storage_profile.os_disk.name
        os_disk = compute_client.disks.get(GROUP_NAME, os_disk_name)
        if not os_disk.disk_size_gb:
            print(
                "\tServer is not returning the OS disk size, possible bug in the server?")
            # print("\tAssuming that the OS disk size is 30 GB")
            # os_disk.disk_size_gb = 30
            return

        os_disk.disk_size_gb += disk_size_gb

        async_disk_update = compute_client.disks.create_or_update(
            GROUP_NAME,
            os_disk.name,
            os_disk
        )
        async_disk_update.wait()

    # Start the VM
    def startVM(self):
        print('\nStart VM')
        async_vm_start = compute_client.virtual_machines.start(
            self.GROUP_NAME, self.VM_NAME)
        async_vm_start.wait()

    # Restart the VM
    def restartVM(self):
        print('\nRestart VM')
        async_vm_restart = compute_client.virtual_machines.restart(
            self.GROUP_NAME, self.VM_NAME)
        async_vm_restart.wait()

    # Stop the VM
    def stopVM(self):
        print('\nStop VM')
        async_vm_stop = compute_client.virtual_machines.power_off(
            self.GROUP_NAME, self.VM_NAME)
        async_vm_stop.wait()

    # List VMs in subscription
    def listVM(self):
        print('\nList VMs in subscription')
        for vm in compute_client.virtual_machines.list_all():
            print("\tVM: {}".format(vm.name))

    # List VM in resource group
    def listResourcegroup(self):
        print('\nList VMs in resource group')
        for vm in compute_client.virtual_machines.list(self.GROUP_NAME):
            print("\tVM: {}".format(vm.name))

    # Delete VM
    def deleteVM(self):
        print('\nDelete VM')
        async_vm_delete = compute_client.virtual_machines.delete(
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
        delete_async_operation = resource_client.resource_groups.delete(
            self.GROUP_NAME)
        delete_async_operation.wait()
        print("\nDeleted: {}".format(GROUP_NAME))

