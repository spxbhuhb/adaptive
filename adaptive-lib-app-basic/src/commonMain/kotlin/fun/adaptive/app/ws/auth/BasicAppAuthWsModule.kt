package `fun`.adaptive.app.ws.auth

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.signInTitle
import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.app.ws.auth.account.AccountEditorData
import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.app.AuthAppContext
import `fun`.adaptive.auth.app.AuthAppContext.Companion.authContext
import `fun`.adaptive.auth.app.AuthModule
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.builtin.account_circle
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstance

class BasicAppAuthWsModule<WT : Workspace> : AuthModule<WT>() {

    companion object {
        const val SIGN_IN_KEY: FragmentKey = "app:ws:auth:sign-in"

        suspend fun ClientApplication<*>.getAccountEditorData(): AccountEditorData? {

            val session = this.firstContext<AuthAppContext>().sessionOrNull ?: return null
            val principalId = session.principalOrNull ?: return null
            val principal = getService<AuthPrincipalApi>(transport).getOrNull(principalId) ?: return null
            val account = getService< AuthBasicApi>(transport).account() ?: return null

            return AccountEditorData(
                account.accountId,
                login = principal.name,
                name = account.name,
                email = account.email,
                activated = principal.spec.activated,
                locked = principal.spec.locked
            )
        }
    }

    lateinit var SIGN_IN_ITEM: SingularWsItem

    override fun frontendAdapterInit(adapter : AdaptiveAdapter)= with(adapter) {
        + WsAppAuthFragmentFactory
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
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

        if (application.authContext.sessionOrNull == null) {
            addContent(SIGN_IN_ITEM)
        } else {
            val appModule = application.modules.firstInstance<BasicAppWsModule<WT>>()
            checkNotNull(application) { "BasicAppWsModule not found" }
            addContent(appModule.HOME_CONTENT_ITEM)
        }
    }

}