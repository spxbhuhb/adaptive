/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.auth.authCommon
import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.cookbook.Routes
import `fun`.adaptive.cookbook.app.pageNotFound
import `fun`.adaptive.cookbook.app.landing
import `fun`.adaptive.cookbook.appData
import `fun`.adaptive.cookbook.auth.api.AccountApi
import `fun`.adaptive.cookbook.auth.authRecipe
import `fun`.adaptive.cookbook.auth.ui.responsive.signIn
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.cookbook.graphics.canvas.canvasRecipe
import `fun`.adaptive.cookbook.ui.dialog.dialogRecipe
import `fun`.adaptive.cookbook.ui.editor.editorRecipe
import `fun`.adaptive.cookbook.ui.event.eventRecipe
import `fun`.adaptive.cookbook.ui.form.formRecipe
import `fun`.adaptive.cookbook.ui.layout.box.boxRecipe
import `fun`.adaptive.cookbook.ui.layout.grid.gridRecipe
import `fun`.adaptive.cookbook.ui.layout.responsive.responsiveMain
import `fun`.adaptive.cookbook.ui.navigation.navigationRecipe
import `fun`.adaptive.cookbook.ui.select.selectRecipe
import `fun`.adaptive.cookbook.ui.sidebar.sideBarRecipe
import `fun`.adaptive.cookbook.ui.snackbar.snackbarRecipe
import `fun`.adaptive.cookbook.ui.svg.svgRecipe
import `fun`.adaptive.cookbook.ui.text.textRecipe
import `fun`.adaptive.cookbook.ui.tree.treeRecipe
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.app.basic.defaultAppLayout
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.form.FormFragmentFactory
import `fun`.adaptive.ui.form.platform.BrowserHistoryStateListener
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.platform.withJsResources
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.ui.uiCommon
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        clientId()

        uiCommon()
        authCommon()
        cookbookCommon()

        withJsResources()

        val transport = webSocketTransport(window.location.origin)

        val localBackend = backend(transport) {
            auto()
            worker { SnackbarManager() }
        }

        appData.transport = transport
        appData.session = getService<SessionApi>(transport).getSession()

        if (appData.session != null) {
            val account = getService<AccountApi>(transport).account()
            checkNotNull(account)
            appData.userFullName = account.name
        }

        BrowserHistoryStateListener(appData)

        appData.onLoginSuccess = suspend {
            window.location.reload()
        }

        appData.onLogout = suspend {
            appData.navState.update(Routes.publicLanding)
            window.location.reload()
        }

        appData.navState.update(Routes.box)

        browser(CanvasFragmentFactory, SvgFragmentFactory, FormFragmentFactory, backend = localBackend) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            defaultAppLayout(appData) {
                mainContent()
            }
        }
    }
}

@Adaptive
fun mainContent() {

    val navState = autoItem(appData.navState)

    when {
        navState?.segments?.isEmpty() == true -> navState.goto(Routes.publicLanding)
        navState in Routes.login && appData.session != null -> navState?.goto(Routes.memberLanding)
    }

    box {
        maxSize .. padding { 16.dp } .. backgroundColor(0xFAFAFA)

        when (navState) {
            in Routes.auth -> authRecipe(navState)
            in Routes.box -> boxRecipe()
            in Routes.canvas -> canvasRecipe()
            in Routes.dialog -> dialogRecipe()
            in Routes.editor -> editorRecipe()
            in Routes.empty -> box { }
            in Routes.event -> eventRecipe()
            in Routes.form -> formRecipe()
            in Routes.grid -> gridRecipe()
            in Routes.login -> signIn()
            in Routes.navigation -> navigationRecipe(navState)
            in Routes.publicLanding -> landing()
            in Routes.memberLanding -> landing()
            in Routes.select -> selectRecipe()
            in Routes.sidebar -> sideBarRecipe()
            in Routes.snackbar -> snackbarRecipe()
            in Routes.svg -> svgRecipe()
            in Routes.text -> textRecipe()
            in Routes.tree -> treeRecipe()
            else -> pageNotFound(navState)
        }
    }
}