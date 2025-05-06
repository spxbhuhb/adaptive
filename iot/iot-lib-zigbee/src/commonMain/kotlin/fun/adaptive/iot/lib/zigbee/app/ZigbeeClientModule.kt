package `fun`.adaptive.iot.lib.zigbee.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.iot.lib.zigbee.ui.zigbeeNetworkConfig
import `fun`.adaptive.runtime.AbstractWorkspace

open class ZigbeeClientModule<WT : AbstractWorkspace> : ZigbeeDriverModule<WT>() {

    val networkConfigKey = "aio:driver:zigbee:config"

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(networkConfigKey, ::zigbeeNetworkConfig)
    }

}