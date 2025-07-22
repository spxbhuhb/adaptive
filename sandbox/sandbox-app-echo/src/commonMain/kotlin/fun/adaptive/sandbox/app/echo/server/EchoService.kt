package `fun`.adaptive.sandbox.app.echo.server

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.sandbox.app.echo.api.EchoApi
import `fun`.adaptive.service.ServiceProvider

@ServiceProvider
class EchoService : ServiceImpl<EchoService>(), EchoApi {

    override suspend fun send(message: String): String {
        return message.reversed()
    }

}