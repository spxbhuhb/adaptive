package `fun`.adaptive.ui.input

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text

@Adaptive
fun <T> decoratedInput(
    focused: Boolean,
    viewBackend: InputViewBackend<T>,
    @Adaptive
    _KT_74337_content: (InputViewBackend<T>) -> Unit
): AdaptiveFragment {

    if (viewBackend.label != null) {
        withLabel(focused, viewBackend) { _KT_74337_content(viewBackend) }
    } else {
        _KT_74337_content(viewBackend)
    }

    return fragment()
}

@Adaptive
private fun <T> withLabel(
    focused: Boolean,
    viewBackend: InputViewBackend<T>,
    @Adaptive
    _KT_74337_content: (InputViewBackend<T>) -> Unit
): AdaptiveFragment {

    val themeInstructions = viewBackend.labelThemeInstructions(focused)

    column(instructions()) {
        text(viewBackend.label, themeInstructions)
        _KT_74337_content(viewBackend)
    }

    return fragment()
}