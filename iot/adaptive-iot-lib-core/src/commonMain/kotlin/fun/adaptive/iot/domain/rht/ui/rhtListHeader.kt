package `fun`.adaptive.iot.domain.rht.ui

import `fun`.adaptive.iot.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.generated.resources.alarm
import `fun`.adaptive.iot.generated.resources.humidity
import `fun`.adaptive.iot.generated.resources.lastSeen
import `fun`.adaptive.iot.generated.resources.name
import `fun`.adaptive.iot.generated.resources.open_in_new
import `fun`.adaptive.iot.generated.resources.spxbId
import `fun`.adaptive.iot.generated.resources.status
import `fun`.adaptive.iot.generated.resources.temperature
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
        text(Strings.spxbId) .. textMedium .. normalFont .. maxWidth
        text(Strings.name) .. textMedium .. normalFont .. maxWidth

        text(Strings.temperature) .. textMedium .. normalFont .. maxWidth
        text(Strings.humidity) .. textMedium .. normalFont .. maxWidth
        text(Strings.status) .. alignSelf.center .. textMedium .. normalFont .. maxWidth
        text(Strings.alarm) .. textMedium .. normalFont .. maxWidth
        text(Strings.lastSeen) .. textMedium .. normalFont .. maxWidth
        icon(Graphics.open_in_new) .. svgWidth(24.dp) .. svgHeight(24.dp) .. alignSelf.center
    }

    return fragment()
}