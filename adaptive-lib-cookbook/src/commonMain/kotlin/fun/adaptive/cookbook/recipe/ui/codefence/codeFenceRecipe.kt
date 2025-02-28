package `fun`.adaptive.cookbook.recipe.ui.codefence

import `fun`.adaptive.cookbook.support.example
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun codeFenceRecipe(): AdaptiveFragment {

    column {
        gap { 16.dp}

        example("simple code fence") { codeFence(code) .. width { 400.dp } }

        example("scrolling code fence") { codeFence(longCode) .. width { 400.dp } .. height { 200.dp }}
    }

    return fragment()
}

val code = """
val rc = RichTextContext(
    listOf(
        instructionsOf(),
        instructionsOf(boldFont)
    )
)    
""".trimIndent()

val longCode = """
    package `fun`.adaptive.cookbook.recipe.ui.codefence

    import `fun`.adaptive.foundation.Adaptive
    import `fun`.adaptive.foundation.AdaptiveFragment
    import `fun`.adaptive.foundation.fragment
    import `fun`.adaptive.ui.api.width
    import `fun`.adaptive.ui.codefence.codeFence
    import `fun`.adaptive.ui.instruction.dp

    @Adaptive
    fun codeFenceRecipe(): AdaptiveFragment {
        
        codeFence(code) .. width { 400.dp }

        return fragment()
    }
""".trimIndent()