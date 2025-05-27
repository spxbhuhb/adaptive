package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.utility.UUID

typealias WsItemTooltip = String
typealias PaneId = UUID<PaneViewBackend<*>>
typealias WsPaneItem = Any
typealias PaneContentType = String
typealias ContentPaneBuilder = (item: WsPaneItem) -> PaneViewBackend<*>?
