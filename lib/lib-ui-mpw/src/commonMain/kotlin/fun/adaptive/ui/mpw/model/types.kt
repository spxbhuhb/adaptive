package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.utility.UUID

typealias WsItemTooltip = String
typealias PaneId = UUID<Pane<*>>
typealias WsPaneItem = Any
typealias WsPaneItemType = String
typealias ContentPaneBuilder = (item: WsPaneItem) -> Pane<*>
