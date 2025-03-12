import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.contextPopup
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.builtin.folder
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.contextMenu

@Adaptive
fun contextMenuMain() {
    var clickedOn = "-"

    column {
        maxSize .. padding { 16.dp } .. gap { 24.dp }

        contextMenu(menu { item, _ -> clickedOn = item.label; })

        box {
            text("right-click for context menu")
            contextPopup { hide ->
                popupAlign.afterBelow
                contextMenu(menu { item, _ -> clickedOn = item.label; hide() })
            }
        }

        text("last click on: $clickedOn")
    }
}

fun menu(onClick: (item: MenuItem<Unit>, modifiers: Set<EventModifier>) -> Unit) = listOf(
    MenuItem<Unit>(Graphics.folder, "Menu item 1", Unit, onClick = onClick),
    MenuItem<Unit>(null, "Menu item 2", Unit, onClick = onClick),
    MenuItem<Unit>(Graphics.folder, "Menu item 3", Unit, "⌘ 1", onClick = onClick),
    MenuItem<Unit>(null, "Menu item 4", Unit, "⌘ 2", onClick = onClick)
)