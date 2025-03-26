package `fun`.adaptive.iot.domain.rht.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.textMedium

@Adaptive
fun rhtListHeader(
    theme : AioTheme = AioTheme.DEFAULT
) : AdaptiveFragment {

    grid {
        theme.itemListHeader
        colTemplate(36.dp, 80.dp, 1.fr, 0.5.fr, 0.5.fr, 82.dp, 60.dp, 160.dp, 48.dp)

        box {  }
        text(Strings.spxbId) .. textMedium .. normalFont
        text(Strings.name) .. textMedium .. normalFont

        text(Strings.temperature) .. textMedium .. normalFont
        text(Strings.humidity) .. textMedium .. normalFont
        text(Strings.status) .. alignSelf.center .. textMedium .. normalFont
        text(Strings.alarm) .. textMedium .. normalFont
        text(Strings.lastSeen) .. textMedium .. normalFont
        icon(Graphics.open_in_new) .. svgWidth(24.dp) .. svgHeight(24.dp) .. alignSelf.center
    }

    return fragment()
}