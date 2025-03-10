package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

abstract class WsItem : AdatClass {
    abstract val icon: GraphicsResourceSet
    abstract val name: String
    abstract val type: WsItemType
    abstract val tooltip: String?
}