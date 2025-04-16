package `fun`.adaptive.iot.app

import `fun`.adaptive.iot.device.network.AioDriverDef
import `fun`.adaptive.iot.device.virtual.AioVirtualDeviceSpec
import `fun`.adaptive.iot.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.iot.history.model.*
import `fun`.adaptive.iot.point.computed.AioComputedPointSpec
import `fun`.adaptive.iot.point.conversion.number.DoubleMultiplyConversion
import `fun`.adaptive.iot.point.sim.AioSimPointSpec
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.utility.firstInstance
import `fun`.adaptive.wireformat.WireFormatRegistry

open class IotModule<WT : AbstractWorkspace> : AppModule<WT>() {

    val driverDefs = mutableListOf<AioDriverDef>()

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {

        + AioSpaceSpec
        + AioVirtualDeviceSpec
        + AioComputedPointSpec
        + AioSimPointSpec

        + AioDoubleHistoryRecord
        + AioBooleanHistoryRecord
        + AioStringHistoryRecord
        + AioHistoryMetadata
        + AioHistoryQuery

        + DoubleMultiplyConversion

    }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    operator fun plusAssign(driverDef: AioDriverDef) {
        driverDefs += driverDef
    }

    companion object {
        val AbstractApplication<*>.iotModule: IotModule<*>
            get() = modules.firstInstance<IotModule<*>>()
    }
}