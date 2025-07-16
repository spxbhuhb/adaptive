package `fun`.adaptive.sandbox.recipe.ui.container

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.container.editableList
import `fun`.adaptive.ui.input.select.SelectInputTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors

/**
 * # Basic editable list
 *
 * Use [editableList](fragment://) for a list of items that can be added, removed etc.
 *
 * [editableList](fragment://) uses [selectInputList](fragment://) under the hood. The
 * renderer function is passed to [selectInputOptionCustom](fragment://).
 */
@Adaptive
fun editableListBasicExample() : AdaptiveFragment {

    editableList(listOf("1","2","3")) { option ->
        text("> $option (this is a custom item renderer)") .. SelectInputTheme.default.optionText .. textColors.onSurfaceAngry
    } .. height { 200.dp } .. borders.outline .. cornerRadius { 4.dp }

    return fragment()
}