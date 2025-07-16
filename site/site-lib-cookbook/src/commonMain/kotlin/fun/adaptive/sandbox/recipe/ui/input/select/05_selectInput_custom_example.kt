package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.select.item.selectInputOptionCustom
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid7

/**
 * # Custom options renderer
 *
 * Use [selectInputOptionCustom](fragment://) for custom option rendering.
 *
 * **NOTE** [SelectTheme](class://) uses fixed item height. You have to create
 *          a theme if you want options with different height.
 */
@Adaptive
fun selectInputCustomExample(): AdaptiveFragment {

    val backend = selectInputBackend<Pair<UUID<Any>, String>> {
        options = listOf(
            uuid7<Any>() to "Content Manager",
            uuid7<Any>() to "Editor",
            uuid7<Any>() to "Viewer"
        )
        toText = { it.second }
    }

    selectInputList(backend) { selectInputOptionCustom(it, ::optionRenderFun) }

    return fragment()
}

@Adaptive
fun optionRenderFun(
    option : Pair<UUID<Any>, String>
) {
    text("custom option renderer for ${option.second}") .. backgrounds.friendlyOpaque
}