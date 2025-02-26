package `fun`.adaptive.cookbook.recipe.ui.text

import `fun`.adaptive.cookbook.support.example
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.paragraph.items.LinkParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.Paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphInstructionSet
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors

@Adaptive
fun paragraphRecipe(): AdaptiveFragment {
    column {
        gap { 16.dp } .. maxWidth

        example("simple paragraph") { paragraph(p) }

        example("link") { paragraph(link) .. width { 400.dp } }

        example("loerm ipsum") { paragraph(loermIpsum) .. width { 400.dp } }
    }

    return fragment()
}

val p = Paragraph(
    listOf(
        ParagraphInstructionSet(instructionsOf()),
        ParagraphInstructionSet(instructionsOf(boldFont))
    ),
    listOf(
        TextParagraphItem("Hello", 1),
        TextParagraphItem(" ", 0),
        TextParagraphItem("World!", 0),
    )
)

val link = Paragraph(
    listOf(
        ParagraphInstructionSet(instructionsOf()),
        ParagraphInstructionSet(instructionsOf(boldFont))
    ),
    listOf(
        TextParagraphItem("Hello", 1),
        TextParagraphItem(" ", 0),
        LinkParagraphItem("Adaptive", "https://adaptive.fun", 0),
        TextParagraphItem(" ", 0),
        TextParagraphItem("World!", 0)
    )
)

val loermIpsum = Paragraph(
    listOf(
        ParagraphInstructionSet(instructionsOf()),
        ParagraphInstructionSet(instructionsOf(textColors.onSurfaceFriendly)),
        ParagraphInstructionSet(instructionsOf(textColors.onSurfaceAngry)),
        ParagraphInstructionSet(instructionsOf(backgrounds.friendlyOpaque)),
        ParagraphInstructionSet(instructionsOf(borders.outline, cornerRadius { 4.dp }, paddingHorizontal { 4.dp }))
    ),
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        .split(" ")
        .mapIndexed { index, it ->
            TextParagraphItem(it, index % 5)
        }
        .flatMap { listOf(it, TextParagraphItem(" ", 0)) }

)