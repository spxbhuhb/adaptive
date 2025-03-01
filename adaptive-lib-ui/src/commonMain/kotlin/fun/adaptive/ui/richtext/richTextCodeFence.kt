package `fun`.adaptive.ui.richtext

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.codefence.codeFence

@Adaptive
fun richTextCodeFence(
    code: String,
    language: String? = null
) : AdaptiveFragment {

    val context = fragment().firstContext<RichText>()

    codeFence(code, language, context.theme.codeFenceTheme) .. context.theme.codeFence

    return fragment()
}