package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.UUID

@Adaptive
fun selectInputTextExample() : AdaptiveFragment {

    val backend = selectInputBackend<Pair<UUID<Any>, String>> {
        options = optionsOptions
        withSurfaceContainer = true
        toText = { it.second }
    }

    row {
        fillStrategy.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp }
            selectInputList(backend, { selectInputOptionText(it) })
        }

        markdown(
            """
            * "Options" dataset    
            * text only option renderer
            * with surface container  
        """.trimIndent()
        )
    }

    return fragment()
}