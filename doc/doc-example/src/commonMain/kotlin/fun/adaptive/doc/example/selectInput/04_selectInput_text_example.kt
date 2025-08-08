package `fun`.adaptive.doc.example.selectInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid7

/**
 * # Text
 *
 * - "Networks" dataset
 * - icon and text option renderer
 * - manual styling, no focus, no feedback, etc.
 */
@Adaptive
fun selectInputTextExample(): AdaptiveFragment {

    val backend = selectInputBackend<Pair<UUID<Any>, String>> {
        options = listOf(
            uuid7<Any>() to "Option 1",
            uuid7<Any>() to "Option 2",
            uuid7<Any>() to "Option 3"
        )
        withSurfaceContainer = true
        toText = { it.second }
    }


    selectInputList(backend, { selectInputOptionText(it) }) .. width { 240.dp }

    return fragment()
}