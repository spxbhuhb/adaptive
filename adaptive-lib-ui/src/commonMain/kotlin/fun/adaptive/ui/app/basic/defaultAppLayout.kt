package `fun`.adaptive.ui.app.basic

import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.power_settings_new
import `fun`.adaptive.ui.builtin.settings
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.open
import `fun`.adaptive.ui.navigation.sidebar.fullSidebar
import `fun`.adaptive.ui.navigation.sidebar.theme.fullSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.theme.thinSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.thinSidebar
import `fun`.adaptive.ui.platform.media.MediaMetrics
import `fun`.adaptive.ui.snackbar.activeSnacks
import `fun`.adaptive.ui.snackbar.snackList
import `fun`.adaptive.ui.snackbar.snackbarTheme
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import kotlinx.coroutines.launch

@Adaptive
fun defaultAppLayout(
    appData: BasicAppData,
    @Adaptive _fixme_adaptive_content: () -> Unit
) {

    val metrics = mediaMetrics()

    val layoutState = autoItem(appData.layoutState)
    val navState = autoItem(appData.navState)

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
                metrics.isSmall -> small(appData, layoutState)
                metrics.isMedium -> medium(appData, layoutState)
                metrics.isLarge -> large(appData, layoutState)
            }
        }

        _fixme_adaptive_content()
    }

    box {
        noPointerEvents .. maxSize
        snackList(activeSnacks) .. noPointerEvents .. snackbarPosition
    }
}

private fun gridInst(metrics: MediaMetrics, barState: DefaultLayoutState?, navState: NavState?): AdaptiveInstruction {

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
    appData: BasicAppData,
    layoutState: DefaultLayoutState?
) {
    if (layoutState?.smallMode == SidebarUserMode.Closed) {
        top(appData)
    } else {
        full(appData) { toggleSmallUserState(appData) }
    }
}

@Adaptive
private fun medium(
    appData: BasicAppData,
    layoutState: DefaultLayoutState?
) {
    if (layoutState?.mediumMode == SidebarUserMode.Closed) {
        thin(appData) { toggleMediumUserState(appData) }
    } else {
        full(appData) { toggleMediumUserState(appData) }
    }
}

@Adaptive
private fun large(
    appData: BasicAppData,
    layoutState: DefaultLayoutState?
) {
    if (layoutState?.largeMode == SidebarUserMode.Closed) {
        thin(appData) { toggleLargeUserState(appData) }
    } else {
        full(appData) { toggleLargeUserState(appData) }
    }
}

@Adaptive
private fun top(
    appData: BasicAppData
) {
    grid {
        colTemplate(48.dp, 1.fr, 48.dp) .. height { 48.dp } .. alignItems.center

        svg(appData.smallAppMenuIcon) .. svgFill(colors.onSurface)
        text(appData.appName) .. boldFont .. fontSize(28.sp)
        svg(appData.smallSettingsAppIcon) .. svgFill(colors.onSurface)
    }
}

@Adaptive
private fun thin(
    appData: BasicAppData,
    toggle: () -> Unit
) {
    val appIcon = appData.mediumAppIcon
    val sidebarItems = appData.sidebarItems
    val footerHeight = if (appData.userFullName != null) thinSidebarTheme.itemHeight * 3 else thinSidebarTheme.itemHeight

    grid {
        rowTemplate(80.dp, 1.fr, footerHeight) .. maxHeight

        box {
            size(80.dp, 80.dp) .. alignItems.center

            box {
                size(48.dp, 48.dp)
                if (appIcon != null) {
                    svg(appIcon) .. svgHeight(48.dp) .. svgWidth(48.dp) .. svgFill(colors.onSurface) .. onClick { toggle() }
                }
            }
        }

        column {
            maxHeight .. verticalScroll .. borderBottom(colors.outline) .. borderTop(colors.outline)
            thinSidebar(sidebarItems, appData.navState)
        }

        thinFooter(appData)
    }
}

@Adaptive
private fun thinFooter(
    appData: BasicAppData
) {
    val loginPage = appData.loginPage
    val userFullName = appData.userFullName

    if (userFullName == null) {
        if (loginPage != null) {
            actionIcon(Graphics.power_settings_new) .. alignSelf.center .. onClick { appData.navState.open(loginPage) }
        }
    } else {
        grid {
            rowTemplate((thinSidebarTheme.itemHeight - 4.dp) repeat 3)
            alignItems.center .. paddingTop { 6.dp } .. paddingBottom { 6.dp }

            nameplate(userFullName)
            actionIcon(Graphics.settings)
            logout(appData)
        }
    }
}

@Adaptive
private fun full(
    appData: BasicAppData,
    toggle: () -> Unit,
) {
    val appIcon = appData.largeAppIcon
    val sidebarItems = appData.sidebarItems

    grid {
        rowTemplate(168.dp, 1.fr, fullSidebarTheme.itemHeight) .. maxHeight

        box {
            if (appIcon != null) {
                appIcon(appIcon) .. position(36.dp, 16.dp) .. onClick { toggle() }
            }
            text(appData.appName) .. boldFont .. fontSize(28.sp) .. position(60.dp, 88.dp)
        }

        column {
            maxHeight .. verticalScroll .. borderBottom(colors.outline) .. borderTop(colors.outline)
            fullSidebar(sidebarItems, appData.navState)
        }

        fullFooter(appData)
    }
}

@Adaptive
private fun fullFooter(
    appData: BasicAppData
) {
    val loginPage = appData.loginPage
    val userFullName = appData.userFullName

    if (userFullName == null) {
        if (loginPage != null) {
            button("Login", Graphics.power_settings_new) .. alignSelf.center .. onClick { appData.navState.open(loginPage) }
        }
    } else {
        grid {
            maxSize .. colTemplate(1.fr, 40.dp, 40.dp) .. paddingRight { 16.dp }
            row {
                maxHeight .. alignSelf.startCenter .. alignItems.startCenter .. gap { 12.dp } .. paddingLeft { 20.dp }
                nameplate(userFullName)
                text(userFullName) .. maxWidth
            }
            actionIcon(Graphics.settings) .. alignSelf.center
            logout(appData)
        }
    }
}

private val nameplateStyles = instructionsOf(
    textColors.onSuccessSurface,
    fontSize(18.sp),
    fontWeight(400),
    alignSelf.center,
    padding(top = 1.5.dp, left = 1.5.dp)
)

@Adaptive
private fun nameplate(name: String) {
    val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString()?.uppercase() }.take(2).joinToString("")
    box {
        size(34.dp, 34.dp) .. cornerRadius(18.dp) .. backgroundColor(colors.successSurface) .. alignItems.center
        text(initials) .. nameplateStyles
    }
}

@Adaptive
fun appIcon(
    icon: GraphicsResourceSet,
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

@Adaptive
fun logout(appData: BasicAppData) {
    actionIcon(Graphics.power_settings_new) .. alignSelf.center .. onClick {
        adapter().scope.launch {
            getService<SessionApi>(adapter().transport).logout()
            appData.onLogout()
        }
    }
}

// FIXME sidebar mode toggle
private fun toggleSmallUserState(appData: BasicAppData) {
    val current = appData.layoutState.value

    appData.layoutState.update(
        current::smallMode to (if (current.smallMode == SidebarUserMode.Open) SidebarUserMode.Closed else SidebarUserMode.Open),
        current::mediumMode to SidebarUserMode.Closed,
        current::largeMode to SidebarUserMode.Closed
    )
}

private fun toggleMediumUserState(appData: BasicAppData) {
    val current = appData.layoutState.value

    appData.layoutState.update(
        current::smallMode to SidebarUserMode.Closed,
        current::mediumMode to (if (current.mediumMode == SidebarUserMode.Open) SidebarUserMode.Closed else SidebarUserMode.Open),
        current::largeMode to SidebarUserMode.Open
    )
}

private fun toggleLargeUserState(appData: BasicAppData) {
    val current = appData.layoutState.value

    appData.layoutState.update(
        current::smallMode to SidebarUserMode.Closed,
        current::mediumMode to SidebarUserMode.Closed,
        current::largeMode to (if (current.largeMode == SidebarUserMode.Open) SidebarUserMode.Closed else SidebarUserMode.Open)
    )
}