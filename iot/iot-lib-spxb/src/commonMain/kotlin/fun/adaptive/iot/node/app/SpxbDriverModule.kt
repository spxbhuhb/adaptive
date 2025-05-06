package `fun`.adaptive.iot.node.app

import `fun`.adaptive.iot.node.spec.SpxbControllerSpec
import `fun`.adaptive.iot.node.spec.SpxbNetworkSpec
import `fun`.adaptive.iot.node.spec.SpxbPointSpec
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.utility.ComponentKey
import `fun`.adaptive.wireformat.WireFormatRegistry

open class SpxbDriverModule<WT : AbstractWorkspace> : AppModule<WT>() {

    val driverKey: ComponentKey
        get() = "aio:driver:spxb"

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + SpxbNetworkSpec
        + SpxbControllerSpec
        + SpxbPointSpec
    }

}