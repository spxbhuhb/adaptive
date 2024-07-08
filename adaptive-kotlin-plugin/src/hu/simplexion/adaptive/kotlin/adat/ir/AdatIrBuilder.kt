package hu.simplexion.adaptive.kotlin.adat.ir

import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder

interface AdatIrBuilder : AbstractIrBuilder {
    override val pluginContext: AdatPluginContext
}