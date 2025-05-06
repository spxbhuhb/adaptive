package `fun`.adaptive.iot.node.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.iot.node.ui.spxbNetworkConfig
import `fun`.adaptive.runtime.AbstractWorkspace

open class SpxbClientModule<WT : AbstractWorkspace> : SpxbDriverModule<WT>() {

    val networkConfigKey = "aio:driver:spxb:config"

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(networkConfigKey, ::spxbNetworkConfig)
    }

}