package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.select.item.selectInputItemIconAndText
import `fun`.adaptive.ui.input.select.selectInput
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders

@Adaptive
fun selectInputIconAndTextExample() : AdaptiveFragment {

    val backend = selectInputBackend<Option> {
        this.options = networkOptions.map { Option(it.first, it.second) }
        toText = { it.text }
        toIcon = { it.icon }
    }

    row {
        fill.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp } .. verticalScroll .. backgroundColor(0xffff00, 0.3) .. borders.outline .. padding { 8.dp }
            selectInput(backend, { selectInputItemIconAndText(it) })
        }

        markdown(
            """
            * "Networks" dataset
            * icon and text item renderer
            * manual styling, no focus, no feedback, etc.
        """.trimIndent()
        )
    }

    return fragment()
}