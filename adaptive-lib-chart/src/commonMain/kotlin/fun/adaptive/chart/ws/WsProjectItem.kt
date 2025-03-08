package `fun`.adaptive.chart.ws

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

abstract class WsProjectItem : AdatClass {
    abstract val icon: GraphicsResourceSet
    abstract val name: String
}