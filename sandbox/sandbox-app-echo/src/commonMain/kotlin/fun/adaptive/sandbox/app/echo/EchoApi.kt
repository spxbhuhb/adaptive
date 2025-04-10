package `fun`.adaptive.sandbox.app.echo

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface EchoApi {

    suspend fun send(message: String): String

}