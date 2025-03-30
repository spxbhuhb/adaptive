package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.backend.basic.AuthBasicService
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.auth.model.basic.BasicSignIn
import `fun`.adaptive.auth.model.basic.BasicSignUp
import `fun`.adaptive.runtime.ServerWorkspace
import `fun`.adaptive.wireformat.WireFormatRegistry

class AuthBasicServerModule<WT : ServerWorkspace> : AuthServerModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) {
        super.wireFormatInit(registry)

        with(registry) {
            + BasicAccountSpec
            + BasicAccountSummary
            + BasicSignIn
            + BasicSignUp
        }
    }

    override fun workspaceInit(workspace: WT, session: Any?) {
        super.workspaceInit(workspace, session)

        with(workspace) {
            + AuthBasicService()
        }
    }

}