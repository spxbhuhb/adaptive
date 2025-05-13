package `fun`.adaptive.sandbox.app.echo.api

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface EchoApi {

    suspend fun send(message: String): String

}