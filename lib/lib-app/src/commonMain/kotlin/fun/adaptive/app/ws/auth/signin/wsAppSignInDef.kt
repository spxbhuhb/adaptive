package `fun`.adaptive.app.ws.auth.signin

import `fun`.adaptive.app.ws.AppMainWsModule
import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.auth.app.AuthAppContext.Companion.authContext
import `fun`.adaptive.lib_app.generated.resources.signInTitle
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.SingularPaneItem
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.mpw.model.PaneSingularity
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstance

fun MultiPaneWorkspace.wsAppSignInDef(
    application: AbstractApplication<*>,
    module : AppAuthWsModule<*>,
    item : SingularPaneItem
) {

    addContentPaneBuilder(module.SIGN_IN_KEY) {
        PaneDef(
            UUID(),
            this,
            Strings.signInTitle,
            Graphics.account_circle,
            PanePosition.Center,
            module.SIGN_IN_KEY,
            UnitPaneViewBackend(this),
            singularity = PaneSingularity.FULLSCREEN
        )
    }

    if (application.authContext.sessionOrNull == null) {
        addContent(item)
    } else {
        val appModule = application.modules.firstInstance<AppMainWsModule<*>>()
        checkNotNull(application) { "AppWsModule not found" }
        addContent(appModule.HOME_CONTENT_ITEM)
    }
}