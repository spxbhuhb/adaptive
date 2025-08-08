package `fun`.adaptive.doc.example.codefence

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.instruction.dp

/**
 * # Code fence - small
 *
 * Code fence with small content, not scrolling.
 */
@Adaptive
fun codeFenceSmallExample() : AdaptiveFragment {

    val code =
        """
            val exampleCode = "Hello World!"
        """.trimIndent()

    codeFence(code) .. width { 400.dp }

    return fragment()
}
