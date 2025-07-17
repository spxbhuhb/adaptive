package `fun`.adaptive.sandbox.recipe.document.markdown

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text
import kotlin.collections.iterator

/**
 * # Markdown Actualize
 *
 * - To include actual live fragments in your Markdown use the `actualize` scheme.
 * - You can pass parameters which the fragment receives in a map (parameters are optional).
 * - You have to register the fragment in the registry for this to work.
 */
@Adaptive
fun markdownActualizeExample(): AdaptiveFragment {

    markdown("[label](actualize://cookbook-actualized-example?p1=hello&p2=world)")

    return fragment()
}

@Adaptive
fun actualizedFromMarkdownExample(
    args : Map<String,String>
) : AdaptiveFragment {

    column {
        for (arg in args) {
            text("key: ${arg.key}, value: ${arg.value}")
        }
    }

    return fragment()
}