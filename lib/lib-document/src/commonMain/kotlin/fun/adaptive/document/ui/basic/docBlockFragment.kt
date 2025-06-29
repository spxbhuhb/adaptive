package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocBlockFragment
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.lib.util.url.Url
import `fun`.adaptive.lib.util.url.Url.Companion.parseUrl


@Adaptive
fun docBlockFragment(context: DocRenderContext, element: DocBlockFragment): AdaptiveFragment {

    val style = if (element.style >= 0) {
        context.styles[element.style]
    } else {
        context.theme.blockFragment
    }

    val url = processArguments(element)

    actualize(
        url.segments.joinToString("/"),
        null,
        arrayOf<Any>(emptyInstructions),
        url.parameters
    ) .. style

    return fragment()
}


private fun processArguments(element: DocBlockFragment): Url {
    try {
        return element.url.parseUrl()
    } catch (e: Exception) {
        getLogger("document").error(e)
        return Url(segments = listOf("aui:text", "actualize error: ${e.message}"))
    }
}