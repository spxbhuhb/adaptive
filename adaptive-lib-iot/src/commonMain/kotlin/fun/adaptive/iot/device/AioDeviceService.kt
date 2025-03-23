package `fun`.adaptive.iot.device

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.item.AvStatus
import kotlinx.datetime.Clock.System.now

class AioDeviceService : AioDeviceApi, ServiceImpl<AioDeviceService> {

    companion object {
        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        worker = safeAdapter.firstImpl<AvValueWorker>()
    }

    override suspend fun add(name: String, itemType: AvMarker, parentId: AvValueId?): AvValueId {
        publicAccess()

        val itemId = uuid7<AvValue>()

        worker.execute {

            val item = AvItem<AioDeviceSpec>(
                name,
                AioWsContext.WSIT_DEVICE + ":$itemType",
                itemId,
                now(),
                AvStatus.OK,
                nextFriendlyId(DeviceMarkers.DEVICE, "DEV-"),
                markersOrNull = mutableMapOf(
                    DeviceMarkers.DEVICE to null,
                    itemType to null
                ),
                parentId = parentId,
                specific = AioDeviceSpec()
            )

            this += item

            if (parentId == null) {
                addTopList(itemId, DeviceMarkers.TOP_DEVICES)
            } else {
                addChild(parentId, itemId, DeviceMarkers.SUB_DEVICES)
            }
        }

        return itemId
    }

    override suspend fun rename(deviceId: AvValueId, name: String) {
        publicAccess()

        worker.updateItem(deviceId) {
            it.copy(timestamp = now(), name = name)
        }
    }

    override suspend fun moveUp(deviceId: AvValueId) {
        publicAccess()

        worker.execute {
            moveUp(deviceId, DeviceMarkers.SUB_DEVICES, DeviceMarkers.TOP_DEVICES)
        }
    }

    override suspend fun moveDown(deviceId: AvValueId) {
        publicAccess()

        worker.execute {
            moveDown(deviceId, DeviceMarkers.SUB_DEVICES, DeviceMarkers.TOP_DEVICES)
        }
    }

    override suspend fun setSpec(valueId: AvValueId, spec: AioDeviceSpec) {
        publicAccess()

        return worker.update<AvItem<AioDeviceSpec>>(valueId) {
            it.copy(timestamp = now(), specific = spec)
        }
    }

}