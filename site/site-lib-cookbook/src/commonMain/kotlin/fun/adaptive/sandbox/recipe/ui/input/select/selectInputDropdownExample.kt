package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.input.select.item.selectInputOptionIconAndText
import `fun`.adaptive.ui.input.select.item.selectInputValueIconAndText
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputDropdown
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.UUID

@Adaptive
fun selectInputDropdownExample() : AdaptiveFragment {

    val backend = selectInputBackend<Pair<UUID<Any>, String>> {
        this.options = optionsOptions
        toText = { it.second }
        toIcon = { Graphics.menu_book }
    }

    row {
        fill.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp }
            selectInputDropdown(backend, { selectInputOptionIconAndText(it) }) { selectInputValueIconAndText(it) }
        }

        markdown(
            """
            * "Options" dataset
            * icon and text renderers
            * dropdown variant
        """.trimIndent()
        )
    }

    return fragment()
}