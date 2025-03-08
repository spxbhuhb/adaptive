package `fun`.adaptive.ui.tab

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.utility.UUID

class TabPane(
    val uuid : UUID<TabPane>,
    val key: FragmentKey,
    val title: String? = null,
    val icon: GraphicsResourceSet? = null,
    val tooltip: String? = null,
    val closeable: Boolean = true,
    val actions: List<TabPaneAction> = emptyList()
)