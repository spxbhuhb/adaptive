package `fun`.adaptive.iot.model

import `fun`.adaptive.iot.model.device.AioDevice
import `fun`.adaptive.iot.model.device.point.AioDevicePoint
import `fun`.adaptive.iot.model.measurement.AioMeasurementLocation
import `fun`.adaptive.iot.model.measurement.point.AioMeasurementPoint
import `fun`.adaptive.iot.model.network.AioNetwork
import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.utility.UUID

typealias AioDeviceId = UUID<AioDevice>
typealias AioDevicePointId = UUID<AioDevicePoint>
typealias AioMeasurementLocationId = UUID<AioMeasurementLocation>
typealias AioMeasurementPointId = UUID<AioMeasurementPoint>
typealias AioProjectId = UUID<AioProject>
typealias AioSpaceId = UUID<AioSpace>
typealias AioNetworkId = UUID<AioNetwork>
