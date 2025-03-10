package `fun`.adaptive.ui.tab

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.smallCloseIconTheme
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall

class TabTheme(
    tabListHeight: DPixel = 36.dp
) {

    val outerContainer = instructionsOf(
        rowTemplate(tabListHeight, 1.fr)
    )

    // subtract outerContainer surrounding
    val innerHeight = tabListHeight

    val separatorSize = 16.dp

    val tabActionSize = innerHeight

    val contextMenuSize = innerHeight

    val header = instructionsOf(
        height { innerHeight },
        borderBottom(colors.lightOutline)
    )

    val tabHandleList = instructionsOf(
        maxWidth,
        height { innerHeight }
    )

    val separator = instructionsOf(
        size(separatorSize, innerHeight),
        borderRight(colors.outline),
        margin(top = 8.dp, left = 8.dp, bottom = 8.dp, right = 7.dp)
    )

    val tabHandleContainerBase = instructionsOf(
        height { innerHeight },
        paddingLeft(12.dp),
        paddingRight(4.dp),
        paddingTop { 3.dp },
        alignItems.center
    )

    val tabHandleContainer = tabHandleContainerBase + paddingBottom { 4.dp }

    val activeTabHandleContainer = tabHandleContainerBase + borderBottom(colors.focusColor, 4.dp)

    val tabHandleIconContainer = instructionsOf(
        size(19.dp),
        paddingBottom { 1.dp }
    )

    val tabHandleIcon = instructionsOf(
        size(17.dp, 17.dp),
        svgWidth(17.dp),
        svgHeight(17.dp),
        fill(colors.info)
    )

    val tabHandleText = instructionsOf(
        textColors.onSurface,
        fontSize { 13.sp },
        paddingLeft { 4.dp },
        padding { 4.dp },
        noSelect
    )

    val tabHandleToolTip = instructionsOf(
        paddingVertical { 4.dp },
        paddingHorizontal { 12.dp },
        //border(colors.outline, 1.dp),
        cornerRadius(4.dp),
        backgrounds.reverse,
        popupAlign.afterBelow,
        zIndex { 100 },
        // this is buggy - dropShadow(colors.reverse.opaque(0.2f), 4.dp, 4.dp, 4.dp),
    )

    val tabHandleToolTipText = instructionsOf(
        textSmall,
        textColors.onReverse,
        noSelect
    )

    val tabHandleCloseContainer = instructionsOf(
        size(smallCloseIconTheme.containerSize)
    )

    companion object {
        var DEFAULT = TabTheme()
    }

}