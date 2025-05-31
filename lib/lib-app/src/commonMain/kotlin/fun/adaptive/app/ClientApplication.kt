package `fun`.adaptive.app

import `fun`.adaptive.auth.model.AuthRole
import `fun`.adaptive.runtime.AbstractClientApplication
import `fun`.adaptive.runtime.ClientWorkspace
import `fun`.adaptive.ui.instruction.sp

abstract class ClientApplication<WT : ClientWorkspace> : AbstractClientApplication<WT>() {

    open val defaultFontName = "Open Sans"
    open val defaultFontSize = 16.sp
    open val defaultFontWeight = 300

    var knownRoles : List<AuthRole> = emptyList()

    open fun onSignIn() {

    }

    open fun onSignOut() {

    }

}