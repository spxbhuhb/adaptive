package `fun`.adaptive.ui.layout.app.default

import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.boldFont
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.mediaMetrics
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.navigation.NavStateOrigin
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem
import `fun`.adaptive.ui.navigation.sidebar.fullSidebar
import `fun`.adaptive.ui.navigation.sidebar.theme.fullSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.theme.thinSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.thinSidebar
import `fun`.adaptive.ui.platform.media.MediaMetrics
import `fun`.adaptive.ui.theme.colors

val sidebarState = autoInstance(SidebarState())

@Adaptive
fun defaultAppLayout(
    items: List<SidebarItem>,
    navState: NavStateOrigin,
    smallIcon: DrawableResource,
    mediumIcon: DrawableResource,
    largeIcon: DrawableResource,
    title: String,
    @Adaptive _fixme_adaptive_content: () -> Unit,
) {

    val metrics = mediaMetrics()
    val barState = autoInstance(sidebarState)

    grid(gridInst(metrics, barState)) {
        maxSize

        box {
            when {
                metrics.isSmall -> small(smallIcon, title, items, navState, barState)
                metrics.isMedium -> medium(mediumIcon, title, items, navState, barState)
                metrics.isLarge -> large(largeIcon, title, items, navState, barState)
            }
        }

        _fixme_adaptive_content()
    }
}

private fun gridInst(metrics: MediaMetrics, barState: SidebarState?): AdaptiveInstruction {
    if (metrics.isSmall) {
        return rowTemplate(48.dp, 1.fr)
    }

    if (metrics.isMedium) {
        return colTemplate(thinSidebarTheme.width, 1.fr)
    }

    if (barState?.largeMode == SidebarUserMode.Open) {
        return colTemplate(fullSidebarTheme.width, 1.fr)
    } else {
        return colTemplate(thinSidebarTheme.width, 1.fr)
    }
}

@Adaptive
private fun small(
    icon: DrawableResource,
    title: String,
    items: List<SidebarItem>,
    navState: NavStateOrigin,
    barState: SidebarState?,
) {
    if (barState?.smallMode == SidebarUserMode.Closed) {
        top(icon, title, items, navState)
    } else {
        full(icon, title, items, navState) { toggleSmallUserState() }
    }
}

@Adaptive
private fun medium(
    icon: DrawableResource,
    title: String,
    items: List<SidebarItem>,
    navState: NavStateOrigin,
    barState: SidebarState?,
) {
    println(barState?.mediumMode)
    if (barState?.mediumMode == SidebarUserMode.Closed) {
        thin(icon, title, items, navState) { toggleMediumUserState() }
    } else {
        full(icon, title, items, navState) { toggleMediumUserState() }
    }
}

@Adaptive
private fun large(
    icon: DrawableResource,
    title: String,
    items: List<SidebarItem>,
    navState: NavStateOrigin,
    barState: SidebarState?,
) {
    if (barState?.largeMode == SidebarUserMode.Closed) {
        thin(icon, title, items, navState) { toggleLargeUserState() }
    } else {
        full(icon, title, items, navState) { toggleLargeUserState() }
    }
}

@Adaptive
private fun top(
    icon: DrawableResource,
    title: String,
    items: List<SidebarItem>,
    state: NavStateOrigin,
) {
    grid {
        colTemplate(48.dp, 1.fr, 48.dp) .. height { 48.dp } .. alignItems.center

        svg(icon) .. svgFill(colors.onSurface)
        text(title) .. boldFont .. fontSize(28.sp)
        svg(icon) .. svgFill(colors.onSurface)
    }
}

@Adaptive
private fun thin(
    icon: DrawableResource,
    title: String,
    items: List<SidebarItem>,
    state: NavStateOrigin,
    toggle: () -> Unit,
) {
    grid {
        rowTemplate(80.dp, 1.fr)

        box {
            size(80.dp, 80.dp) .. alignItems.center

            box {
                size(48.dp, 48.dp)
                svg(icon) .. svgHeight(48.dp) .. svgWidth(48.dp) .. svgFill(colors.onSurface) .. onClick { toggle() }
            }
        }

        thinSidebar(items, state)
    }
}

@Adaptive
private fun full(
    icon: DrawableResource,
    title: String,
    items: List<SidebarItem>,
    state: NavStateOrigin,
    toggle: () -> Unit,
) {
    grid {
        rowTemplate(168.dp, 1.fr, 60.dp) .. maxSize
        box {
            appIcon(icon) .. position(36.dp, 16.dp) .. onClick { toggle() }
            text(title) .. boldFont .. fontSize(28.sp) .. position(60.dp, 88.dp)
        }
        fullSidebar(items, state)
    }
}

@Adaptive
fun appIcon(
    icon: DrawableResource,
    vararg instructions: AdaptiveInstruction,
): AdaptiveFragment {
    box(*instructions) {
        size(80.dp, 80.dp) .. alignItems.center

        box {
            size(48.dp, 48.dp)
            svg(icon) .. svgHeight(48.dp) .. svgWidth(48.dp) .. svgFill(colors.onSurface)
        }
    }
    return fragment()
}

// FIXME sidebar mode toggle
private fun toggleSmallUserState() {
    val current = sidebarState.frontend.value

    sidebarState.frontend.update(
        if (current.smallMode == SidebarUserMode.Open) {
            SidebarState(SidebarUserMode.Closed, SidebarUserMode.Closed, SidebarUserMode.Open)
        } else {
            SidebarState(SidebarUserMode.Open, SidebarUserMode.Closed, SidebarUserMode.Open)
        }
    )
}

private fun toggleMediumUserState() {
    val current = sidebarState.frontend.value

    sidebarState.frontend.update(
        if (current.mediumMode == SidebarUserMode.Open) {
            SidebarState(SidebarUserMode.Closed, SidebarUserMode.Closed, SidebarUserMode.Open)
        } else {
            SidebarState(SidebarUserMode.Closed, SidebarUserMode.Open, SidebarUserMode.Open)
        }
    )
}

private fun toggleLargeUserState() {
    val current = sidebarState.frontend.value

    sidebarState.frontend.update(
        if (current.largeMode == SidebarUserMode.Open) {
            SidebarState(SidebarUserMode.Closed, SidebarUserMode.Closed, SidebarUserMode.Closed)
        } else {
            SidebarState(SidebarUserMode.Closed, SidebarUserMode.Closed, SidebarUserMode.Open)
        }
    )
}