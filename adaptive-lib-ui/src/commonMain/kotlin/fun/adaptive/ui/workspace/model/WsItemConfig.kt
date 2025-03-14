package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.builtin.empty

class WsItemConfig(
    val type : WsItemType,
    val icon : GraphicsResourceSet,
    val renderer : FragmentKey? = null
) {
    companion object {
        val DEFAULT = WsItemConfig("<anonymous>", Graphics.empty, null)
    }
}