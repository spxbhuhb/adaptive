package `fun`.adaptive.cookbook.ui.layout.responsive

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.mediaMetrics
import `fun`.adaptive.ui.platform.media.MediaMetrics

@Adaptive
fun responsiveMain() {
    val metrics = mediaMetrics()
    //globalUIState.frontend.update(GlobalUIState(metrics))

    when {
        metrics.isSmall -> smallLayout()
        metrics.isMedium -> mediumLayout()
        metrics.isLarge -> largeLayout()
    }
}

enum class ScreenMode {
    Small,
    Medium,
    Large
}

@Adat
class GlobalUIState(
    val mode: ScreenMode = ScreenMode.Small
) {
    constructor(metrics: MediaMetrics) : this(
        when {
            metrics.isSmall -> ScreenMode.Small
            metrics.isMedium -> ScreenMode.Medium
            metrics.isLarge -> ScreenMode.Large
            else -> ScreenMode.Small
        }
    )
}

//val globalUIState = autoInstance(GlobalUIState())

