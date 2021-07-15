import datetime
from azure.mgmt.monitor import MonitorManagementClient
from azure.common.client_factory import get_client_from_cli_profile
def getVMCpuDate(subscription_id,resource_group_name,vm_name):#per hour
    # Get the ARM id of your resource. You might chose to do a "get"
    # using the according management or to build the URL directly
    # Example for a ARM VM
    resource_id = (
        "subscriptions/{}/"
        "resourceGroups/{}/"
        "providers/Microsoft.Compute/virtualMachines/{}"
    ).format(subscription_id, resource_group_name, vm_name)
    # credentials = ServicePrincipalCredentials(  # 虚拟机用
    #     client_id="043f8d98-95d1-473f-9471-6bcc6fb38d30",
    #     secret="o2Ms_n_y1DMR6-j6H-j_tyzlGNXj8s3I14",
    #     tenant="fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6""fcf1c04c-90e4-4b45-b1a5-7c0dc46c6ad6"
    # )
    # create client
    client = get_client_from_cli_profile(MonitorManagementClient)

    # You can get the available metrics of this specific resource
    for metric in client.metric_definitions.list(resource_id):
        # azure.monitor.models.MetricDefinition
        print("{}: id={}, unit={}".format(
            metric.name.localized_value,
            metric.name.value,
            metric.unit
        ))

    # Example of result for a VM:
    # Percentage CPU: id=Percentage CPU, unit=Unit.percent
    # Network In: id=Network In, unit=Unit.bytes
    # Network Out: id=Network Out, unit=Unit.bytes
    # Disk Read Bytes: id=Disk Read Bytes, unit=Unit.bytes
    # Disk Write Bytes: id=Disk Write Bytes, unit=Unit.bytes
    # Disk Read Operations/Sec: id=Disk Read Operations/Sec, unit=Unit.count_per_second
    # Disk Write Operations/Sec: id=Disk Write Operations/Sec, unit=Unit.count_per_second

    # Get CPU total of yesterday for this VM, by hour

    today = datetime.datetime.now().date()
    yesterday = today - datetime.timedelta(days=1)

    metrics_data = client.metrics.list(
        resource_id,
        timespan="{}/{}".format(yesterday, today),
        interval='PT1H',
        metricnames='Percentage CPU',
        aggregation='Total'
    )

    for item in metrics_data.value:
        # azure.mgmt.monitor.models.Metric
        print(item.name)
        for timeserie in item.timeseries:
            for data in timeserie.data:
                # azure.mgmt.monitor.models.MetricData
                print("{}: {}".format(data.time_stamp, data.total))

    # Example of result:
    # Percentage CPU (percent)
    # 2016-11-16 00:00:00+00:00: 72.0
    # 2016-11-16 01:00:00+00:00: 90.59
    # 2016-11-16 02:00:00+00:00: 60.58
    # 2016-11-16 03:00:00+00:00: 65.78
    # 2016-11-16 04:00:00+00:00: 43.96
    # 2016-11-16 05:00:00+00:00: 43.96
    # 2016-11-16 06:00:00+00:00: 114.9
    # 2016-11-16 07:00:00+00:00: 45.4
getVMCpuDate("fc4bf4a7-37a5-46c5-bd67-002062908beb","NologinTest","bushu")