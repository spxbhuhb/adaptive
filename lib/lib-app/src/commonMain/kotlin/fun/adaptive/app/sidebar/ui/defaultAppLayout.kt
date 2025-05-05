package `fun`.adaptive.app.sidebar.ui

//
//@Adaptive
//fun defaultAppLayout(
//    appData: BasicAppData,
//    @Adaptive _fixme_adaptive_content: () -> Unit
//) {
//
//    val metrics = mediaMetrics()
//
//    val layoutState = autoItem(appData.layoutState)
//    val navState = autoItem(appData.navState)
//
//    val activeSnacks = autoCollection(activeSnackStore) ?: emptyList()
//
//    val theme = SnackbarTheme.DEFAULT
//
//    val snackbarPosition = position(
//        metrics.viewHeight.dp - (theme.snackHeight + theme.snackGap) * activeSnacks.size,
//        metrics.viewWidth.dp - theme.snackWidth - 16.dp
//    )
//
//    grid(gridInst(metrics, layoutState, navState)) {
//        maxSize
//
//        box {
//            maxSize
//            when {
//                navState?.fullScreen == true -> box { }
//                metrics.isSmall -> small(appData, layoutState)
//                metrics.isMedium -> medium(appData, layoutState)
//                metrics.isLarge -> large(appData, layoutState)
//            }
//        }
//
//        _fixme_adaptive_content()
//    }
//
//    box {
//        noPointerEvents .. maxSize
//        snackList(activeSnacks) .. noPointerEvents .. snackbarPosition
//    }
//}
//
//private fun gridInst(metrics: MediaMetrics, barState: DefaultLayoutState?, navState: NavState?): AdaptiveInstruction {
//
//    if (navState?.fullScreen == true) {
//        return colTemplate(0.dp, 1.fr)
//    }
//
//    if (metrics.isSmall) {
//        return rowTemplate(48.dp, 1.fr)
//    }
//
//    if (metrics.isMedium) {
//        if (barState?.mediumMode == SidebarUserMode.Open) {
//            return colTemplate(fullSidebarTheme.width, 1.fr)
//        } else {
//            return colTemplate(thinSidebarTheme.width, 1.fr)
//        }
//    }
//
//    if (barState?.largeMode == SidebarUserMode.Open) {
//        return colTemplate(fullSidebarTheme.width, 1.fr)
//    } else {
//        return colTemplate(thinSidebarTheme.width, 1.fr)
//    }
//
//}
//
//@Adaptive
//private fun small(
//    appData: BasicAppData,
//    layoutState: DefaultLayoutState?
//) {
//    if (layoutState?.smallMode == SidebarUserMode.Closed) {
//        top(appData)
//    } else {
//        full(appData) { toggleSmallUserState(appData) }
//    }
//}
//
//@Adaptive
//private fun medium(
//    appData: BasicAppData,
//    layoutState: DefaultLayoutState?
//) {
//    if (layoutState?.mediumMode == SidebarUserMode.Closed) {
//        thin(appData) { toggleMediumUserState(appData) }
//    } else {
//        full(appData) { toggleMediumUserState(appData) }
//    }
//}
//
//@Adaptive
//private fun large(
//    appData: BasicAppData,
//    layoutState: DefaultLayoutState?
//) {
//    if (layoutState?.largeMode == SidebarUserMode.Closed) {
//        thin(appData) { toggleLargeUserState(appData) }
//    } else {
//        full(appData) { toggleLargeUserState(appData) }
//    }
//}
//
//@Adaptive
//private fun top(
//    appData: BasicAppData
//) {
//    grid {
//        colTemplate(48.dp, 1.fr, 48.dp) .. height { 48.dp } .. alignItems.center
//
//        svg(appData.smallAppMenuIcon) .. fill(colors.onSurface)
//        text(appData.appName) .. boldFont .. fontSize(28.sp)
//        svg(appData.smallSettingsAppIcon) .. fill(colors.onSurface)
//    }
//}
//
//@Adaptive
//private fun thin(
//    appData: BasicAppData,
//    toggle: () -> Unit
//) {
//    val appIcon = appData.mediumAppIcon
//    val sidebarItems = appData.sidebarItems
//    val footerHeight = if (appData.userFullName != null) thinSidebarTheme.itemHeight * 3 else thinSidebarTheme.itemHeight
//
//    grid {
//        rowTemplate(80.dp, 1.fr, footerHeight) .. maxHeight
//
//        box {
//            size(80.dp, 80.dp) .. alignItems.center
//
//            box {
//                size(48.dp, 48.dp)
//                if (appIcon != null) {
//                    svg(appIcon) .. svgHeight(48.dp) .. svgWidth(48.dp) .. fill(colors.onSurface) .. onClick { toggle() }
//                }
//            }
//        }
//
//        column {
//            maxHeight .. verticalScroll .. borderBottom(colors.outline) .. borderTop(colors.outline)
//            thinSidebar(sidebarItems, appData.navState)
//        }
//
//        thinFooter(appData)
//    }
//}
//
//@Adaptive
//private fun thinFooter(
//    appData: BasicAppData
//) {
//    val loginPage = appData.loginPage
//    val userFullName = appData.userFullName
//
//    if (userFullName == null) {
//        if (loginPage != null) {
//            actionIcon(Graphics.power_settings_new) .. alignSelf.center .. onClick { appData.navState.open(loginPage) }
//        }
//    } else {
//        grid {
//            rowTemplate((thinSidebarTheme.itemHeight - 4.dp) repeat 3)
//            alignItems.center .. paddingTop { 6.dp } .. paddingBottom { 6.dp }
//
//            nameplate(userFullName)
//            actionIcon(Graphics.settings)
//            logout(appData)
//        }
//    }
//}
//
//@Adaptive
//private fun full(
//    appData: BasicAppData,
//    toggle: () -> Unit,
//) {
//    val appIcon = appData.largeAppIcon
//    val sidebarItems = appData.sidebarItems
//
//    grid {
//        rowTemplate(168.dp, 1.fr, fullSidebarTheme.itemHeight) .. maxHeight
//
//        box {
//            if (appIcon != null) {
//                appIcon(appIcon) .. position(36.dp, 16.dp) .. onClick { toggle() }
//            }
//            text(appData.appName) .. boldFont .. fontSize(28.sp) .. position(60.dp, 88.dp)
//        }
//
//        column {
//            maxHeight .. verticalScroll .. borderBottom(colors.outline) .. borderTop(colors.outline)
//            fullSidebar(sidebarItems, appData.navState)
//        }
//
//        fullFooter(appData)
//    }
//}
//
//@Adaptive
//private fun fullFooter(
//    appData: BasicAppData
//) {
//    val loginPage = appData.loginPage
//    val userFullName = appData.userFullName
//
//    if (userFullName == null) {
//        if (loginPage != null) {
//            button("Login", Graphics.power_settings_new) .. alignSelf.center .. onClick { appData.navState.open(loginPage) }
//        }
//    } else {
//        grid {
//            maxSize .. colTemplate(1.fr, 40.dp, 40.dp) .. paddingRight { 16.dp }
//            row {
//                maxHeight .. alignSelf.startCenter .. alignItems.startCenter .. gap { 12.dp } .. paddingLeft { 20.dp }
//                nameplate(userFullName)
//                text(userFullName) .. maxWidth
//            }
//            actionIcon(Graphics.settings) .. alignSelf.center
//            logout(appData)
//        }
//    }
//}
//
//private val nameplateStyles = instructionsOf(
//    textColors.onSuccessSurface,
//    fontSize(18.sp),
//    fontWeight(400),
//    alignSelf.center,
//    padding(top = 1.5.dp, left = 1.5.dp)
//)
//
//@Adaptive
//private fun nameplate(name: String) {
//    val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString()?.uppercase() }.take(2).joinToString("")
//    box {
//        size(34.dp, 34.dp) .. cornerRadius(18.dp) .. backgroundColor(colors.successSurface) .. alignItems.center
//        text(initials) .. nameplateStyles
//    }
//}
//
//@Adaptive
//fun appIcon(
//    icon: GraphicsResourceSet,
//    vararg instructions: AdaptiveInstruction,
//): AdaptiveFragment {
//    box(instructions()) {
//        size(80.dp, 80.dp) .. alignItems.center
//
//        box {
//            size(48.dp, 48.dp)
//            svg(icon) .. svgHeight(48.dp) .. svgWidth(48.dp) .. fill(colors.onSurface)
//        }
//    }
//    return fragment()
//}
//
//@Adaptive
//fun logout(appData: BasicAppData) {
//    actionIcon(Graphics.power_settings_new) .. alignSelf.center .. onClick {
//        adapter().scope.launch {
//            getService<AuthSessionApi>(adapter().transport).signOut()
//            appData.onLogout()
//        }
//    }
//}
//
//// FIXME sidebar mode toggle
//private fun toggleSmallUserState(appData: BasicAppData) {
//    val current = appData.layoutState.value
//
//    appData.layoutState.update(
//        current::smallMode to (if (current.smallMode == SidebarUserMode.Open) SidebarUserMode.Closed else SidebarUserMode.Open),
//        current::mediumMode to SidebarUserMode.Closed,
//        current::largeMode to SidebarUserMode.Closed
//    )
//}
//
//private fun toggleMediumUserState(appData: BasicAppData) {
//    val current = appData.layoutState.value
//
//    appData.layoutState.update(
//        current::smallMode to SidebarUserMode.Closed,
//        current::mediumMode to (if (current.mediumMode == SidebarUserMode.Open) SidebarUserMode.Closed else SidebarUserMode.Open),
//        current::largeMode to SidebarUserMode.Open
//    )
//}
//
//private fun toggleLargeUserState(appData: BasicAppData) {
//    val current = appData.layoutState.value
//
//    appData.layoutState.update(
//        current::smallMode to SidebarUserMode.Closed,
//        current::mediumMode to SidebarUserMode.Closed,
//        current::largeMode to (if (current.largeMode == SidebarUserMode.Open) SidebarUserMode.Closed else SidebarUserMode.Open)
//    )
//}