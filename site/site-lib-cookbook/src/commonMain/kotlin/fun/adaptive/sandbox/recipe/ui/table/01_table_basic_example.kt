package `fun`.adaptive.sandbox.recipe.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.paragraph.items.LinkParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphViewBackend
import `fun`.adaptive.ui.generated.resources.north
import `fun`.adaptive.ui.generated.resources.south
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.table.TableCellDef
import `fun`.adaptive.ui.table.TableViewBackend
import `fun`.adaptive.ui.table.table

/**
 * # Basic Table
 */
@Adaptive
fun tableBasicExample(): AdaptiveFragment {

    val backend = TableViewBackend().apply {
        cells += TableCellDef(
            this,
            "Cell 1",
            100.dp
        ).also {
            it.rowMenu = listOf(
                MenuItem(Graphics.south, "Sort descending", "Sort descending"),
                MenuItem(Graphics.north, "Sort ascending", "Sort ascending")
            )
        }
        cells += TableCellDef(
            this,
            "Cell 2",
            100.dp
        )
    }

    table(backend)

    return fragment()
}