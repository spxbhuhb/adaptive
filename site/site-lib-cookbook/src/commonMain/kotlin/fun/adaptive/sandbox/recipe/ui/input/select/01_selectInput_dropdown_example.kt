package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.input.select.item.selectInputOptionIconAndText
import `fun`.adaptive.ui.input.select.item.selectInputValueIconAndText
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputDropdown
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid7

/**
 * # Dropdown
 *
 * - "Options" dataset
 * - icon and text renderers
 * - dropdown variant
 */
@Adaptive
fun selectInputDropdownExample(): AdaptiveFragment {

    val backend = selectInputBackend<Pair<UUID<Any>, String>> {
        options = listOf(
            uuid7<Any>() to "Option 1",
            uuid7<Any>() to "Option 2",
            uuid7<Any>() to "Option 3"
        )
        toText = { it.second }
        toIcon = { Graphics.menu_book }
    }

    selectInputDropdown(
        backend,
        { selectInputOptionIconAndText(it) },
        { selectInputValueIconAndText(it) }
    ) .. width { 240.dp }

    return fragment()
}