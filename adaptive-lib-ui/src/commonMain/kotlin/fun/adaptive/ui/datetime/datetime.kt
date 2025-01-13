package `fun`.adaptive.ui.datetime

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.theme.emptyInst
import kotlinx.datetime.Instant

@Adaptive
fun datetime(timestamp: Instant?, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    text(timestamp?.toString()?.replace("T", " ")?.substringBefore('.') ?: "-", emptyInst(timestamp), instructions())
    return fragment()
}