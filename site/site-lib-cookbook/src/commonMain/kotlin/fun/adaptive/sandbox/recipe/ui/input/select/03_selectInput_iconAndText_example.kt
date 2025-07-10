package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.cookbook.generated.resources.eco
import `fun`.adaptive.cookbook.generated.resources.zigbee
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.account_box
import `fun`.adaptive.ui.input.select.item.selectInputOptionIconAndText
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders

/**
 * # Icon and text renderer
 *
 * - "Networks" dataset
 * - icon and text option renderer
 * - manual styling, no focus, no feedback, etc.
 */
@Adaptive
fun selectInputIconAndTextExample(): AdaptiveFragment {

    val backend = selectInputBackend<Option> {
        options = listOf(
            "Zigbee" to Graphics.zigbee,
            "Modbus" to Graphics.account_box,
            "SPXB" to Graphics.eco
        ).map { Option(it.first, it.second) }
        toText = { it.text }
        toIcon = { it.icon }
    }

    row {
        fillStrategy.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp } .. verticalScroll .. backgroundColor(0xffff00, 0.3) .. borders.outline .. padding { 8.dp }
            selectInputList(backend, { selectInputOptionIconAndText(it) })
        }
    }

    return fragment()
}