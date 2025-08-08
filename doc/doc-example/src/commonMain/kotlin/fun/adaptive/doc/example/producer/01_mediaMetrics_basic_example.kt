package `fun`.adaptive.doc.example.producer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.mediaMetrics
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp

/**
 * # Media metrics
 *
 * Gets the metrics of the current media. Changes when the user changes
 * the application size or theme.
 *
 * **resize the window to see the change**
 */
@Adaptive
fun mediaMetricsBasicExample() : AdaptiveFragment {

    val media = mediaMetrics()
    val screenSize = "${media.viewWidth} x ${media.viewHeight}"

    column {
        gap { 16.dp }

        when {
            media.isSmall -> text("small screen ($screenSize)")
            media.isMedium -> text("medium screen ($screenSize)")
            media.isLarge -> text("large screen ($screenSize)")
        }
    }

    return fragment()
}