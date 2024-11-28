package `fun`.adaptive.ui.app.basic

import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.builtin.Res
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.builtin.settings
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem

open class BasicAppData {

    var appName: String? = null
    var appInfos: List<String>? = null

    var userFullName : String? = null
    var session : Session? = null

    val navState = autoItemOrigin(NavState())

    val layoutState = autoItemOrigin(DefaultLayoutState())

    var smallAppMenuIcon: DrawableResource = Res.drawable.menu
    var smallSettingsAppIcon: DrawableResource = Res.drawable.settings
    var mediumAppIcon: DrawableResource? = null
    var largeAppIcon: DrawableResource? = null

    var sidebarItems = emptyList<SidebarItem>()

    var loginPage : NavState? = null
    var publicLanding : NavState? = null
    val memberLanding : NavState? = null

    var accountSettingsPage : NavState? = null

    var onLoginSuccess : suspend () -> Unit = {}

    fun open(newState: NavState) {
        this.navState.update(newState)
    }

}