package `fun`.adaptive.app.sidebar

import `fun`.adaptive.ui.navigation.navState

object Routes {
    val signIn = navState("sign-in", title = "Sign-In", fullScreen = true)
    val signUp = navState("sign-up", title = "Sign-Up", fullScreen = true)
    val resetPassword = navState("reset-password", title = "Reset Password", fullScreen = true)
    val activateAccount = navState("activate-account", title = "Sign-Up", fullScreen = true)
    val secondFactor = navState("second-factor", title = "Second Factor", fullScreen = true)
    val accountList = navState("account-list", title = "Accounts")
    val accountEdit = navState("account-edit", title = "Account")
    val rolesList = navState("roles-list", title = "Roles")
    val rolesEdit = navState("roles-edit", title = "Role")
    val publicLanding = navState("public-landing", title = "Public Landing")
    val memberLanding = navState("member-landing", title = "Member Landing")
}