/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.builtin.account_box
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Width
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.utility.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveRuntimeCommon()

        commonMainStringsStringStore0.load()

        browser(CanvasFragmentFactory, SvgFragmentFactory, GroveRuntimeFragmentFactory, backend = backend { }) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            box {
                maxSize

                panelIcons(true, panelConfig, panelTheme)
            }

        }
    }
}


val panelConfig = PanelGroupConfiguration(
    listOf(
        Panel(
            "Palette",
            Graphics.menu,
            PanelPosition.RightTop,
            "⌘ P"
        ),
        Panel(
            "Components",
            Graphics.account_box,
            PanelPosition.RightTop,
            "⌘ P"
        )
    ),
    listOf(
        Panel(
            "Palette",
            Graphics.menu,
            PanelPosition.RightMiddle
        ),
        Panel(
            "Components",
            Graphics.account_box,
            PanelPosition.RightMiddle,
            "⌘ P"
        )
    ),
    listOf(
        Panel(
            "Palette",
            Graphics.menu,
            PanelPosition.RightBottom,
            "⌘ P"
        ),
        Panel(
            "Components",
            Graphics.account_box,
            PanelPosition.RightBottom,
            "⌘ P"
        )
    ),
)

class PanelLayout(
    rightTop : UUID<Panel>?,
    rightMiddle : UUID<Panel>?,
    rightBottom : UUID<Panel>?,
    leftTop : UUID<Panel>?,
    leftMiddle : UUID<Panel>?,
    leftBottom : UUID<Panel>?
)

enum class PanelPosition {
    RightTop,
    RightMiddle,
    RightBottom,
    LeftTop,
    LeftMiddle,
    LeftBottom
}

class Panel(
    val name: String,
    val icon: GraphicsResourceSet,
    val position: PanelPosition,
    val shortcut: String? = null,
    val fragmentKey : String? = null
)

class PanelGroupConfiguration(
    val top: List<Panel>,
    val middle: List<Panel>,
    val bottom: List<Panel>
) {
    val divider: Boolean = top.isNotEmpty() && middle.isNotEmpty()
}

val panelTheme = PanelTheme()

class PanelTheme(
    width: DPixel = 40.dp
) {
    val iconColumn = instructionsOf(
        maxHeight,
        width { width },
        spaceBetween
    )

    val rightIconColumn = iconColumn + borderLeft(colors.outline)
    val leftIconColumn = iconColumn + borderRight(colors.outline)

    val divider = instructionsOf(
        width { width },
        height { 16.dp },
        borderTop(colors.outline),
        margin(top = 8.dp, left = 8.dp, right = 8.dp, bottom = 7.dp)
    )

    val panelIconContainer = instructionsOf(
        size(width),
        margin { 6.dp },
        cornerRadius { 4.dp },
        alignItems.center
    )

    val panelIcon = instructionsOf(

    )

    val tooltipContainer = instructionsOf(
        paddingHorizontal { 16.dp },
        paddingVertical { 8.dp },
        backgrounds.reverse,
        cornerRadius { 4.dp },
        gap { 8.dp }
    )

    val tooltipTextBase = instructionsOf(
        noSelect,
        textSmall
    )

    val tooltipName = tooltipTextBase + textColors.onReverse

    val tooltipShortcut = tooltipTextBase + textColors.onReverseVariant

}

@Adaptive
fun panelIcons(
    left : Boolean,
    configuration: PanelGroupConfiguration,
    theme: PanelTheme
) {

    column {
        if (left) theme.leftIconColumn else theme.rightIconColumn

        column {
            for (panel in configuration.top) {
                panelIcon(panel, theme)
            }
            if (configuration.divider) {
                box { theme.divider }
            }
            for (panel in configuration.middle) {
                panelIcon(panel, theme)
            }
        }

        column {
            for (panel in configuration.bottom) {
                panelIcon(panel, theme)
            }
        }
    }

}

@Adaptive
fun panelIcon(
    panel: Panel,
    theme: PanelTheme
) {
    box {
        theme.panelIconContainer

        icon(panel.icon)

        hoverPopup {
            popupAlign.afterCenter

            row {
                theme.tooltipContainer

                text(panel.name) .. theme.tooltipName

                if (panel.shortcut != null) {
                    text(panel.shortcut) .. theme.tooltipShortcut
                }
            }
        }
    }
}