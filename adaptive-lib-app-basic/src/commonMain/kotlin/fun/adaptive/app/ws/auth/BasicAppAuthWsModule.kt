package `fun`.adaptive.app.ws.auth

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.signInTitle
import `fun`.adaptive.app.UiClientApplication
import `fun`.adaptive.app.UiClientApplicationData
import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.auth.authCommon
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.builtin.account_circle
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstance
import `fun`.adaptive.wireformat.WireFormatRegistry

class BasicAppAuthWsModule<AT : UiClientApplication<Workspace, UiClientApplicationData>> : AppModule<Workspace, AT>() {

    companion object {
        const val SIGN_IN_KEY: FragmentKey = "app:ws:auth:sign-in"
    }

    lateinit var SIGN_IN_ITEM: SingularWsItem

    override fun WireFormatRegistry.init() {
        authCommon()
    }

    override fun AdaptiveAdapter.init() {
        fragmentFactory += WsAppAuthFragmentFactory
    }

    override fun Workspace.init() {
        wsAppSignInDef()
    }

    fun Workspace.wsAppSignInDef() {

        SIGN_IN_ITEM = SingularWsItem(Strings.signInTitle, SIGN_IN_KEY)

        addContentPaneBuilder(SIGN_IN_KEY) {
            WsPane(
                UUID(),
                Strings.signInTitle,
                Graphics.account_circle,
                WsPanePosition.Center,
                SIGN_IN_KEY,
                SIGN_IN_ITEM,
                WsSingularPaneController(SIGN_IN_ITEM),
                singularity = WsPaneSingularity.FULLSCREEN
            )
        }

        if (application.appData.sessionOrNull == null) {
            addContent(SIGN_IN_ITEM)
        } else {
            val appModule = application.modules.firstInstance<BasicAppWsModule<AT>>()
            checkNotNull(application) { "BasicAppWsModule not found" }
            addContent(appModule.HOME_CONTENT_ITEM)
        }
    }

}