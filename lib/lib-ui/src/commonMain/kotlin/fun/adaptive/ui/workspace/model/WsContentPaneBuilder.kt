package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.model.NamedItem

typealias WsContentPaneBuilder = (item: NamedItem) -> WsPane<*, *>