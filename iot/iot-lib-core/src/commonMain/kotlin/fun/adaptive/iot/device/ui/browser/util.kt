package `fun`.adaptive.iot.device.ui.browser

import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.ui.workspace.model.WsPane

fun WsPane<DeviceBrowserWsItem, *>.subDevices(): List<AvValueId> {
    val browserItem = this.data
    val toolController = browserItem.config.controller
    return toolController.valueTreeStore.getChildrenIds(browserItem.item.uuid)
}

fun WsPane<DeviceBrowserWsItem, *>.getDevice(deviceId: AvValueId): AvValue<*>? {
    val toolController = this.data.config.controller
    return toolController.valueTreeStore[deviceId]
}

fun DeviceBrowserWsItem.devicePathNames(): List<String> {
    val toolController = this.config.controller

    val deviceId = this.uuid
    val names = mutableListOf<String>()

    var device: AvValue<*>? = toolController.valueTreeStore[deviceId]
    while (device != null) {
        names.add(device.name)
        device = device.parentId?.let { toolController.valueTreeStore[it] }
    }

    return names.reversed()
}