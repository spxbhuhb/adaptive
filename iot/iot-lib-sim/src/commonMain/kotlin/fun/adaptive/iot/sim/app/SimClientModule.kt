package `fun`.adaptive.iot.sim.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.iot.sim.ui.simNetworkConfig
import `fun`.adaptive.runtime.AbstractWorkspace

open class SimClientModule<WT : AbstractWorkspace> : SimDriverModule<WT>() {

    val networkConfigKey = "aio:driver:sim:config"

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(networkConfigKey, ::simNetworkConfig)
    }

}