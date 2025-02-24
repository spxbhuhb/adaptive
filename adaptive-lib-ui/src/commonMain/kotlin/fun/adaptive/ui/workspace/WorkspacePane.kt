package `fun`.adaptive.ui.workspace

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.utility.UUID

class WorkspacePane(
    val uuid: UUID<WorkspacePane>,
    val name: String,
    val icon: GraphicsResourceSet,
    val position: WorkspacePanePosition,
    val key : String,
    val shortcut: String? = null
)