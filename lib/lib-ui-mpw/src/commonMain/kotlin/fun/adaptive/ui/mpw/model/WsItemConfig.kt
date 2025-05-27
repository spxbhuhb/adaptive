package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.generated.resources.empty

class WsItemConfig(
    val type : NamedItemType,
    val icon : GraphicsResourceSet,
    val renderer : FragmentKey? = null
) {
    companion object {
        val DEFAULT = WsItemConfig("<anonymous>", Graphics.empty, null)
    }
}