package `fun`.adaptive.app

import `fun`.adaptive.app.JvmClientApplication.Companion.jvmClient
import `fun`.adaptive.app.app.AppMainModuleBasic
import `fun`.adaptive.auth.app.AuthClientModule
import `fun`.adaptive.lib.util.app.UtilModule
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.value.app.ValueClientModule
import kotlin.test.Test

class JvmClientApplicationTest {

    @Test
    fun testClientApplication() {
        jvmClient {
            module { UtilModule() }
            module { LibUiClientModule() }
            module { AuthClientModule() }
            module { ValueClientModule() }
            module { AppMainModuleBasic() }
        }
    }
}