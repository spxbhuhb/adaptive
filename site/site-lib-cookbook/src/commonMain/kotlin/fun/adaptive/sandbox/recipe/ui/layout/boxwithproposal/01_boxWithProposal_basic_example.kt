package `fun`.adaptive.sandbox.recipe.ui.layout.boxwithproposal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.boxWithProposal
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders

/**
 * # Box with a proposal
 *
 * Use [boxWithProposal](fragment://) when you need the [sizing proposal](def://) to
 * calculate the inner layout of a fragment.
 *
 * The parameter passed to the content function is an instance of [SizingProposal](class://)
 * that contains the minimum and maximum dimensions proposed for the box.
 */
@Adaptive
fun boxWithProposalBasicExample() : AdaptiveFragment {

    column {
        width { 300.dp } .. height { 100.dp } .. borders.outline

        boxWithProposal { proposal ->
            column {
                text("proposed minimum width: ${proposal.minWidth}")
                text("proposed maximum width: ${proposal.maxWidth}")
                text("proposed minimum height: ${proposal.minHeight}")
                text("proposed maximum height: ${proposal.maxHeight}")
            }
        }
    }

    return fragment()
}