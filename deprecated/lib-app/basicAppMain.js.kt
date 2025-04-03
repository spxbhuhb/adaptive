package `fun`.adaptive.app.basic

import `fun`.adaptive.adaptive_lib_app.generated.resources.*
import `fun`.adaptive.app.basic.auth.api.BasicAccountApi
import `fun`.adaptive.app.basic.auth.ui.accountList
import `fun`.adaptive.app.basic.auth.ui.large.largeSignIn
import `fun`.adaptive.app.basic.auth.ui.responsive.responsive
import `fun`.adaptive.app.basic.auth.ui.small.smallSignIn
import `fun`.adaptive.app.basic.util.StringArgument
import `fun`.adaptive.app.basic.util.document
import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ktor.util.clientId
import `fun`.adaptive.resource.document.Documents
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.app.sidebar.ui.BasicAppData
import `fun`.adaptive.app.sidebar.ui.defaultAppLayout
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.form.FormFragmentFactory
import `fun`.adaptive.app.platform.BrowserHistoryStateListener
import `fun`.adaptive.app.sidebar.Routes
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.ui.uiCommon
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun basicAppMain() {

    CoroutineScope(Dispatchers.Default).launch {

        // Get the client id from the server. This request establishes a session with the server.
        // For browser clients this means a cookie.

        clientId()

        uiCommon()
        authBasicCommon()

        appData = BasicAppData().apply {

            this.appName = "Adaptive"

            this.largeAppIcon = Graphics.eco
            this.mediumAppIcon = Graphics.eco

            this.loginPage = Routes.signIn
            this.publicLanding = Routes.publicLanding
            this.memberLanding = Routes.memberLanding

            this.sidebarItems = listOf(
                SidebarItem(Graphics.group, "Accounts", Routes.accountList),
                SidebarItem(Graphics.local_police, "Roles", Routes.rolesList)
            )
        }

        val transport = webSocketTransport(window.location.origin)

        val localBackend = backend(transport) {
            auto()
            worker { SnackbarManager() }
        }

        appData.transport = transport
        appData.session = getService<AuthSessionApi>(transport).getSession()

        if (appData.session != null) {
            val account = getService<BasicAccountApi>(transport).account()
            checkNotNull(account)
            appData.userFullName = account.name
        }

        commonMainStringsStringStore0.load()

        BrowserHistoryStateListener(appData)

        appData.onLoginSuccess = suspend {
            window.location.reload()
        }

        appData.onLogout = suspend {
            appData.navState.update(Routes.publicLanding)
            window.location.reload()
        }

        browser(
            CanvasFragmentFactory,
            SvgFragmentFactory,
            FormFragmentFactory,
            backend = localBackend
        ) { adapter ->

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
        navState in Routes.signIn && appData.session != null -> navState?.goto(Routes.memberLanding)
    }

    box {
        maxSize .. padding { 16.dp } .. backgroundColor(0xFAFAFA)

        when (navState) {
            in Routes.signIn -> responsive(::largeSignIn, ::smallSignIn, ::smallSignIn)
            in Routes.signUp -> document(Documents.work_in_progress)
            in Routes.resetPassword -> document(Documents.work_in_progress)
            in Routes.activateAccount -> document(Documents.work_in_progress)
            in Routes.secondFactor -> document(Documents.work_in_progress)
            in Routes.accountList -> accountList()
            in Routes.accountEdit -> document(Documents.work_in_progress)
            in Routes.rolesList -> document(Documents.work_in_progress)
            in Routes.rolesEdit -> document(Documents.work_in_progress)
            in Routes.publicLanding -> document(Documents.landing)
            in Routes.memberLanding -> document(Documents.landing)
            else -> {
                document(
                    Documents.page_not_found,
                    StringArgument(navState?.segments?.lastOrNull() ?: Strings.noTechnicalInfo)
                )
            }
        }
    }
}