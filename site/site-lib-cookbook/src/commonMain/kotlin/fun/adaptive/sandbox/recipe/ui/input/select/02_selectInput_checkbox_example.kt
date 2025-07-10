package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid7

/**
 * # Checkbox renderer
 *
 * - "Roles" dataset
 * - checkbox option renderer
 * - manual styling, no border
 */
@Adaptive
fun selectInputCheckboxExample() : AdaptiveFragment {

    val backend = selectInputBackend<Pair<UUID<Any>, String>> {
        options = listOf(
            uuid7<Any>() to "Content Manager",
            uuid7<Any>() to "Editor",
            uuid7<Any>() to "Viewer"
        )
        toText = { it.second }
    }

    selectInputList(backend) { selectInputOptionCheckbox(it) }

    return fragment()
}