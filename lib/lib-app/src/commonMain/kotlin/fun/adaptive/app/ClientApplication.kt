package `fun`.adaptive.app

import `fun`.adaptive.auth.model.AuthRole
import `fun`.adaptive.foundation.value.observableOf
import `fun`.adaptive.general.Observable
import `fun`.adaptive.runtime.AbstractClientApplication
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.utility.Url

abstract class ClientApplication<WT : FrontendWorkspace, BW : BackendWorkspace> : AbstractClientApplication<WT, BW>() {

    open val defaultFontName = "Open Sans"
    open val defaultFontSize = 16.sp
    open val defaultFontWeight = 300

    var knownRoles : List<AuthRole> = emptyList()

    open val navState = observableOf { NavState(Url.parse("/")) }

    open fun onSignIn() {

    }

    open fun onSignOut() {

    }

}