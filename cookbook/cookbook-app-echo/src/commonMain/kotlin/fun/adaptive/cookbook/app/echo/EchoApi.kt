package `fun`.adaptive.cookbook.app.echo

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface EchoApi {

    suspend fun send(message: String): String

}