package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.select.item.selectInputItemCheckbox
import `fun`.adaptive.ui.input.select.selectInput
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
        fill.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp } .. verticalScroll .. padding { 8.dp }
            selectInput(backend, { selectInputItemCheckbox(it) })
        }

        markdown(
            """
            * "Roles" dataset
            * checkbox item renderer
            * manual styling
        """.trimIndent()
        )
    }

    return fragment()
}