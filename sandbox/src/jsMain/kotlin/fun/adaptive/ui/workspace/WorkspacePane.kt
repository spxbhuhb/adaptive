package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.utility.UUID

class WorkspacePane(
    val uuid: UUID<WorkspacePane>,
    val name: String,
    val icon: GraphicsResourceSet,
    val position: WorkspacePanePosition,
    @Adaptive // https://youtrack.jetbrains.com/issue/KT-74337/Local-Delegated-properties-dont-preserve-their-annotations-and-dont-show-up-in-reflection
    val _fixme_adaptive_content: () -> AdaptiveFragment,
    val shortcut: String? = null
)