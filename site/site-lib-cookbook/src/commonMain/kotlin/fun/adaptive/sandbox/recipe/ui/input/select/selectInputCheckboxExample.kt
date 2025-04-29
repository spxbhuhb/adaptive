package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.UUID

@Adaptive
fun selectInputCheckboxExample() : AdaptiveFragment {

    val backend = selectInputBackend<Pair<UUID<Any>, String>> {
        this.options = roleOptions
        toText = { it.second }
    }

    row {
        fillStrategy.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp } .. verticalScroll .. padding { 8.dp }
            selectInputList(backend, { selectInputOptionCheckbox(it) })
        }

        markdown(
            """
            * "Roles" dataset
            * checkbox option renderer
            * manual styling, no border
        """.trimIndent()
        )
    }

    return fragment()
}