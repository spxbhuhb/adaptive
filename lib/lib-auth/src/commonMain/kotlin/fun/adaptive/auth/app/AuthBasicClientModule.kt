package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.auth.model.basic.BasicSignIn
import `fun`.adaptive.auth.model.basic.BasicSignUp
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.wireformat.WireFormatRegistry

open class AuthBasicClientModule<FW : AbstractWorkspace, BW: BackendWorkspace> : AuthClientModule<FW,BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) {
        super.wireFormatInit(registry)

        with(registry) {
            + BasicAccountSpec
            + BasicAccountSummary
            + BasicSignIn
            + BasicSignUp
        }
    }

}