package `fun`.adaptive.grove.apm.model

import `fun`.adaptive.grove.resources.folder
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem

class ApmProjectItem(
    val name: String,
    val path: String,
    val centerKey: String
) {
    fun toTreeItem(onClick: (TreeItem, modifiers : Set<EventModifier>) -> Unit): TreeItem =
        TreeItem(
            Graphics.folder,
            name,
            emptyList(),
            data = centerKey,
            onClick = onClick
        )
}