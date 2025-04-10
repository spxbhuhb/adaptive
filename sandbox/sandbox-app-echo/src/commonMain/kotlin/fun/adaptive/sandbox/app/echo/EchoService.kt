package `fun`.adaptive.cookbook.app.echo

import `fun`.adaptive.backend.builtin.ServiceImpl

class EchoService : ServiceImpl<EchoService>, EchoApi {

    override suspend fun send(message: String): String {
        return message.reversed()
    }

}