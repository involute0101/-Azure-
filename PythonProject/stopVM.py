import control_VM
import sys

GROUP_NAME = sys.argv[1]
OS_DISK_NAME = sys.argv[2]
VM_NAME = sys.argv[3]

vmManager = control_VM.VMController(GROUP_NAME,OS_DISK_NAME,VM_NAME)
vmManager.stopVM()