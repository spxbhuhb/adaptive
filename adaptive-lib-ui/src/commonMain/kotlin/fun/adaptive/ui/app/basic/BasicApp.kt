package `fun`.adaptive.ui.app.basic

import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.builtin.settings
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem

open class BasicAppData {

    lateinit var transport: ServiceCallTransport

    var appName: String? = null
    var appInfos: List<String>? = null

    var userFullName : String? = null
    var session : Session? = null

    val navState = autoItemOrigin(NavState())

    val layoutState = autoItemOrigin(DefaultLayoutState())

    var smallAppMenuIcon = Graphics.menu
    var smallSettingsAppIcon = Graphics.settings
    var mediumAppIcon: GraphicsResourceSet? = null
    var largeAppIcon: GraphicsResourceSet? = null

    var sidebarItems = emptyList<SidebarItem>()

    var loginPage : NavState? = null
    var publicLanding : NavState? = null
    var memberLanding: NavState? = null

    var accountSettingsPage : NavState? = null

    var onLoginSuccess : suspend () -> Unit = {}
    var onLogout: suspend () -> Unit = {}

    fun open(newState: NavState) {
        this.navState.update(newState)
    }

}