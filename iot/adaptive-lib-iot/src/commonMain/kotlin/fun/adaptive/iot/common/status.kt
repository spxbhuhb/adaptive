package `fun`.adaptive.iot.common

import `fun`.adaptive.adaptive_lib_iot.generated.resources.statusFaultShort
import `fun`.adaptive.adaptive_lib_iot.generated.resources.statusOkShort
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.value.item.AvStatus

@Adaptive
fun status(
    status: AvStatus,
    theme : AioTheme = AioTheme.DEFAULT
)  : AdaptiveFragment {
    val color = theme.statusColor(status)
    val border = theme.statusBorder(color)

    box {
        theme.statusContainer .. border
        text(status.localizedStatus()) .. theme.statusText .. textColor(color)
    }

    return fragment()
}

private fun AvStatus.localizedStatus() =
    when {
        isOk -> Strings.statusOkShort
        else -> Strings.statusFaultShort
    }