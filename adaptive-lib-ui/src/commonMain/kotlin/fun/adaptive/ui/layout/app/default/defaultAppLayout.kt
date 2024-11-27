package `fun`.adaptive.ui.layout.app.default

import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.auto.api.autoItemOrigin
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
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.mediaMetrics
import `fun`.adaptive.ui.api.noPointerEvents
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.NavStateOrigin
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem
import `fun`.adaptive.ui.navigation.sidebar.fullSidebar
import `fun`.adaptive.ui.navigation.sidebar.theme.fullSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.theme.thinSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.thinSidebar
import `fun`.adaptive.ui.platform.media.MediaMetrics
import `fun`.adaptive.ui.snackbar.activeSnacks
import `fun`.adaptive.ui.snackbar.snackList
import `fun`.adaptive.ui.snackbar.snackbarTheme
import `fun`.adaptive.ui.theme.colors

val appLayoutState = autoItemOrigin(AppLayoutState())

@Adaptive
fun defaultAppLayout(
    items: List<SidebarItem>,
    appNavState: NavStateOrigin,
    smallIcon: DrawableResource,
    mediumIcon: DrawableResource,
    largeIcon: DrawableResource,
    title: String,
    @Adaptive _fixme_adaptive_content: () -> Unit,
) {

    val metrics = mediaMetrics()

    val layoutState = autoItem(appLayoutState)
    val navState = autoItem(appNavState)

    val activeSnacks = autoCollection(activeSnacks) ?: emptyList()

    val snackbarPosition = position(
        metrics.viewHeight.dp - (snackbarTheme.snackHeight + snackbarTheme.snackGap) * activeSnacks.size,
        metrics.viewWidth.dp - snackbarTheme.snackWidth - 16.dp
    )

    grid(gridInst(metrics, layoutState, navState)) {
        maxSize

        box {
            maxSize
            when {
                navState?.fullScreen == true -> box { }
                metrics.isSmall -> small(smallIcon, title, items, appNavState, layoutState)
                metrics.isMedium -> medium(mediumIcon, title, items, appNavState, layoutState)
                metrics.isLarge -> large(largeIcon, title, items, appNavState, layoutState)
            }
        }

        _fixme_adaptive_content()
    }

    box {
        noPointerEvents .. maxSize
        snackList(activeSnacks) .. noPointerEvents .. snackbarPosition
    }
}

private fun gridInst(metrics: MediaMetrics, barState: AppLayoutState?, navState: NavState?): AdaptiveInstruction {

    if (navState?.fullScreen == true) {
        return colTemplate(0.dp, 1.fr)
    }

    if (metrics.isSmall) {
        return rowTemplate(48.dp, 1.fr)
    }

    if (metrics.isMedium) {
        if (barState?.mediumMode == SidebarUserMode.Open) {
            return colTemplate(fullSidebarTheme.width, 1.fr)
        } else {
            return colTemplate(thinSidebarTheme.width, 1.fr)
        }
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
    barState: AppLayoutState?,
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
    barState: AppLayoutState?,
) {
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
    barState: AppLayoutState?,
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
        rowTemplate(80.dp, 1.fr) .. maxHeight

        box {
            size(80.dp, 80.dp) .. alignItems.center

            box {
                size(48.dp, 48.dp)
                svg(icon) .. svgHeight(48.dp) .. svgWidth(48.dp) .. svgFill(colors.onSurface) .. onClick { toggle() }
            }
        }

        column {
            maxHeight .. verticalScroll
            thinSidebar(items, state)
        }
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
        rowTemplate(168.dp, 1.fr, 60.dp) .. maxHeight
        box {
            appIcon(icon) .. position(36.dp, 16.dp) .. onClick { toggle() }
            text(title) .. boldFont .. fontSize(28.sp) .. position(60.dp, 88.dp)
        }
        column {
            maxHeight .. verticalScroll
            fullSidebar(items, state)
        }
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
    val current = appLayoutState.value

    appLayoutState.update(
        current::smallMode to (if (current.smallMode == SidebarUserMode.Open) SidebarUserMode.Closed else SidebarUserMode.Open),
        current::mediumMode to SidebarUserMode.Closed,
        current::largeMode to SidebarUserMode.Closed
    )
}

private fun toggleMediumUserState() {
    val current = appLayoutState.value

    appLayoutState.update(
        current::smallMode to SidebarUserMode.Closed,
        current::mediumMode to (if (current.mediumMode == SidebarUserMode.Open) SidebarUserMode.Closed else SidebarUserMode.Open),
        current::largeMode to SidebarUserMode.Open
    )
}

private fun toggleLargeUserState() {
    val current = appLayoutState.value

    appLayoutState.update(
        current::smallMode to SidebarUserMode.Closed,
        current::mediumMode to SidebarUserMode.Closed,
        current::largeMode to (if (current.largeMode == SidebarUserMode.Open) SidebarUserMode.Closed else SidebarUserMode.Open)
    )
}