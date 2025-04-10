package `fun`.adaptive.ui.select

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.*

open class SelectTheme(
    val itemWidth: DPixel = 400.dp
) : AbstractTheme() {

    var outerContainer = instructionsOf(
        height { inputHeightDp }, // keep it fixed so we won't re-layout it even if the select is open
        tabIndex { 0 }
    )

    var closedContainer = instructionsOf(
        width { itemWidth },
        height { inputHeightDp }
    )

    var open = cornerTopRadius(inputCornerRadiusDp)

    var closed = inputCornerRadius

    var base = instructionsOf(
        colTemplate(1.fr, 24.dp),
        height { inputHeightDp },
        alignItems.startCenter
    )

    var enabled = base + instructionsOf(
        borders.outline,
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 8.dp),
        textColor(colors.onSurface),
        backgroundColor(colors.surface)
    )

    var focused = base + instructionsOf(
        border(colors.primary, 2.dp),
        padding(left = 15.dp, right = 7.dp),
        textColor(colors.onSurface),
        backgroundColor(colors.surface),
    )

    var disabled = base + instructionsOf(
        borders.outline,
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 16.dp),
        backgroundColor(colors.surfaceVariant),
        textColor(colors.onSurfaceVariant)
    )

    var openContainer = instructionsOf(
        width { itemWidth },
        zIndex(2),
        backgrounds.surface
    )

    var itemsContainer = instructionsOf(
        position(inputHeightDp, 0.dp),
        maxWidth,
        verticalScroll,
        border(colors.outline, top = 0.dp),
        cornerBottomRadius(8.dp)
    )

    var item = instructionsOf(
        maxWidth,
        height { inputHeightDp },
        padding(16.dp)
    )

    fun itemColors(selected: Boolean, hover: Boolean) = colorsSurface(false, hover)

    companion object {
        var DEFAULT = SelectTheme()
    }
}