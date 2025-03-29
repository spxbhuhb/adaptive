package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.app.AuthModule
import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.lib.util.testing.AbstractTestSupport
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class AuthTestSupport : AbstractTestSupport(
    workerClass = AuthSessionWorker::class
) {

    override val serverServices = listOf(
        AuthPrincipalService(),
        AuthRoleService(),
        AuthSessionService()
    )

    override val serverWorkers = listOf(
        AvValueWorker(trace = true),
        AuthWorker(),
        AuthSessionWorker()
    )

    val valueWorker
        get() = serverBackend.firstImpl<AvValueWorker>()

    val authWorker
        get() = serverBackend.firstImpl<AuthWorker>()

    suspend fun addPrincipal(name: String, password: String?, spec: PrincipalSpec = PrincipalSpec()): AuthPrincipalId {

        val principalId = authWorker.getPrincipalService().addPrincipal(name, spec, PASSWORD, password)

        waitForReal(1.seconds) { valueWorker.isIdle }

        return principalId
    }

    companion object {

        @OptIn(ExperimentalCoroutinesApi::class)
        fun authTest(
            timeout: Duration = 10.seconds,
            testFun: suspend AuthTestSupport.() -> Unit
        ) =
            runTest(timeout = timeout) {

                with(AuthModule<Unit, Unit>()) { WireFormatRegistry.initWireFormats() }

                with(AuthTestSupport()) {
                    test { testFun() }
                }
            }
    }
}