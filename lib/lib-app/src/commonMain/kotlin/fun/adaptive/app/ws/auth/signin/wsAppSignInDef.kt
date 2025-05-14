package `fun`.adaptive.app.ws.auth.signin

import `fun`.adaptive.lib_app.generated.resources.signInTitle
import `fun`.adaptive.app.ws.AppMainWsModule
import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.auth.app.AuthAppContext.Companion.authContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstance

fun MultiPaneWorkspace.wsAppSignInDef(
    application: AbstractApplication<*>,
    module : AppAuthWsModule<*>,
    item : SingularWsItem
) {

    addContentPaneBuilder(module.SIGN_IN_KEY) {
        WsPane(
            UUID(),
            this,
            Strings.signInTitle,
            Graphics.account_circle,
            WsPanePosition.Center,
            module.SIGN_IN_KEY,
            item,
            WsSingularPaneController(this, item),
            singularity = WsPaneSingularity.FULLSCREEN
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