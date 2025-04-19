package `fun`.adaptive.document.ui.direct

import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.text

@Adaptive
fun h1(content : String, theme : DocumentTheme = DocumentTheme.default) : AdaptiveFragment {
    text(content, instructions()) .. theme.h1
    return fragment()
}

@Adaptive
fun h2(content : String, theme : DocumentTheme = DocumentTheme.default): AdaptiveFragment {
    text(content, instructions()) .. theme.h2
    return fragment()
}

@Adaptive
fun h3(content : String, theme : DocumentTheme = DocumentTheme.default): AdaptiveFragment {
    text(content, instructions()) .. theme.h3
    return fragment()
}

@Adaptive
fun h4(content : String, theme : DocumentTheme = DocumentTheme.default): AdaptiveFragment {
    text(content, instructions()) .. theme.h4
    return fragment()
}

@Adaptive
fun h5(content : String, theme : DocumentTheme = DocumentTheme.default): AdaptiveFragment {
    text(content, instructions()) .. theme.h5
    return fragment()
}

@Adaptive
fun hN(content : String, theme : DocumentTheme = DocumentTheme.default): AdaptiveFragment {
    text(content, instructions()) .. theme.hN
    return fragment()
}