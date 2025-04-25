package `fun`.adaptive.iot.device

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.app.WsItemTypes
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

    override suspend fun add(
        name: String,
        itemType: AvMarker,
        parentId: AvValueId?,
        spec: AioDeviceSpec,
        markers: Map<AvMarker, AvValueId?>?
    ): AvValueId {
        ensureLoggedIn()

        val itemId = markers?.get("migratedId")?.cast() ?: uuid7<AvValue>()

        val itemMarkers = markers?.toMutableMap() ?: mutableMapOf()

        itemMarkers[DeviceMarkers.DEVICE] = null
        itemMarkers[itemType] = null

        if (spec.virtual) {
            itemMarkers[DeviceMarkers.VIRTUAL_DEVICE] = null
        }

        worker.execute {

            val item = AvItem<AioDeviceSpec>(
                name,
                WsItemTypes.WSIT_DEVICE + ":$itemType",
                itemId,
                now(),
                AvStatus.OK,
                parentId,
                nextFriendlyId(DeviceMarkers.DEVICE, "DEV-"),
                markersOrNull = itemMarkers,
                spec = spec
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
        ensureLoggedIn()

        worker.updateItem(deviceId) {
            it.copy(timestamp = now(), name = name)
        }
    }

    override suspend fun moveUp(deviceId: AvValueId) {
        ensureLoggedIn()

        worker.execute {
            moveUp(deviceId, DeviceMarkers.SUB_DEVICES, DeviceMarkers.TOP_DEVICES)
        }
    }

    override suspend fun moveDown(deviceId: AvValueId) {
        ensureLoggedIn()

        worker.execute {
            moveDown(deviceId, DeviceMarkers.SUB_DEVICES, DeviceMarkers.TOP_DEVICES)
        }
    }

    override suspend fun setSpec(valueId: AvValueId, spec: AioDeviceSpec) {
        ensureLoggedIn()

        return worker.update<AvItem<AioDeviceSpec>>(valueId) {
            it.copy(timestamp = now(), spec = spec)
        }
    }

}