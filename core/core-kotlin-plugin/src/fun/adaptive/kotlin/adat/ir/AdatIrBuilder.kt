package `fun`.adaptive.kotlin.adat.ir

import `fun`.adaptive.kotlin.common.AbstractIrBuilder

interface AdatIrBuilder : AbstractIrBuilder {
    override val pluginContext: AdatPluginContext
}