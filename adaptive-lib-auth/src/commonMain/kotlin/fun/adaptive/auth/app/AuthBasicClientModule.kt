package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.auth.model.basic.BasicSignIn
import `fun`.adaptive.auth.model.basic.BasicSignUp
import `fun`.adaptive.runtime.ClientWorkspace
import `fun`.adaptive.wireformat.WireFormatRegistry

open class AuthBasicClientModule<WT : ClientWorkspace> : AuthClientModule<WT>() {

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