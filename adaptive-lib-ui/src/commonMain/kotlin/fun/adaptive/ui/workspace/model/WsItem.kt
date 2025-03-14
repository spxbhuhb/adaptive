package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.adat.AdatClass

abstract class WsItem : AdatClass {
    abstract val name: String
    abstract val type: WsItemType
    abstract val tooltip: WsItemTooltip?
}