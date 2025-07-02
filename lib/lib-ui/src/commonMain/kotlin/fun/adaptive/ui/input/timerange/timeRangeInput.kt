package `fun`.adaptive.ui.input.timerange

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.lib.util.datetime.TimeRange
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.input.time.timeInputBackend
import `fun`.adaptive.ui.input.time.timeInputInner
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.localTime

@Adaptive
fun timeRangeInput(
    viewBackend: TimeRangeInputViewBackend
): AdaptiveFragment {

    val observed = observe { viewBackend }

    val startBackend = observe {
        timeInputBackend(viewBackend.inputValue?.start ?: localTime()) {
            onChange = { if (it != null) viewBackend.inputValue = TimeRange(it, viewBackend.inputValue?.end ?: localTime()) }
        }
    }
    val endBackend = observe {
        timeInputBackend(viewBackend.inputValue?.end ?: localTime()) {
            onChange = { if (it != null) viewBackend.inputValue = TimeRange(viewBackend.inputValue?.start ?: localTime(), it) }
        }
    }

    val focus = focus()

    decoratedInput(focus, observed) {
        row(instructions()) {
            gap { 8.dp }
            timeInputInner(startBackend)
            text(" - ")
            timeInputInner(endBackend)
        }
    }

    return fragment()
}