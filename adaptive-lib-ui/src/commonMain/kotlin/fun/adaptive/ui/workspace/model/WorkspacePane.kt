package `fun`.adaptive.ui.workspace.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.utility.UUID

/**
 * @property   direct   A pane is a content pane when it is not opened in [position] but put into the center
 *                       are instead. With this flag you can add icons to the sidebars that opens content directly.
 */
class WorkspacePane(
    val uuid: UUID<WorkspacePane>,
    val name: String,
    val icon: GraphicsResourceSet,
    val position: WorkspacePanePosition,
    val key : FragmentKey,
    val tooltip: String? = null,
    val direct : Boolean = false,
    val actions : List<WorkspacePaneAction> = emptyList()
)