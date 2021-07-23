import datetime
from azure.mgmt.monitor import MonitorManagementClient
from azure.common.client_factory import get_client_from_cli_profile
import sys

def getNetworkData(subscription_id,resource_group_name,vm_name):
    resource_id = (
        "subscriptions/{}/"
        "resourceGroups/{}/"
        "providers/Microsoft.Compute/virtualMachines/{}"
    ).format(subscription_id, resource_group_name, vm_name)

    client = get_client_from_cli_profile(MonitorManagementClient)
    # for log in client.activity_logs.list(resource_id):
    #     print("{}".format(
    #         log.name
    #         # metric.name.localized_value,
    #         # metric.name.value,
    #         # metric.unit
    #     ))

    # today = datetime.datetime.now().date()
    # yesterday = today - datetime.timedelta(days=1)

    # metrics_data = client.metrics.list(
    #     resource_id,
    #     timespan="{}/{}".format(yesterday, today),
    #     interval='PT1H',
    #     metricnames="",
    #     aggregation='Total'
    # )
    #

    today = datetime.datetime.now().time()
    yesterday = datetime.datetime.now() - datetime.timedelta(hours=24)

    metrics_data = client.metrics.list(
        resource_id,
        timespan="{}/{}".format(yesterday, today),
        interval='PT1H',
        metricnames='Network In',#此处可改获取其他资源指标
        aggregation='Total'
    )
    for item in metrics_data.value:
        # azure.mgmt.monitor.models.Metric
        # print(item.name)
        for timeserie in item.timeseries:
            for data in timeserie.data:
                # azure.mgmt.monitor.models.MetricData
                print("{}&&{}@".format(data.time_stamp, data.total))

subscriptionId = sys.argv[1]
resourceGroup = sys.argv[2]
VM_NAME = sys.argv[3]
getNetworkData(subscriptionId,resourceGroup,VM_NAME)