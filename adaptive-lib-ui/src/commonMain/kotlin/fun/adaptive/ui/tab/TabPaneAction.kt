package `fun`.adaptive.ui.tab

import `fun`.adaptive.resource.graphics.GraphicsResourceSet

class TabPaneAction(
    val icon : GraphicsResourceSet,
    val toolTip : String,
    val action : (tab : TabPane) -> Unit
)