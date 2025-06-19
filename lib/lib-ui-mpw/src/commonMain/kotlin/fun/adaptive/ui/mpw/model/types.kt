package `fun`.adaptive.ui.mpw.model

import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.utility.UUID

typealias PaneId = UUID<PaneViewBackend<*>>
typealias PaneContentItem = Any
typealias PaneContentType = String
