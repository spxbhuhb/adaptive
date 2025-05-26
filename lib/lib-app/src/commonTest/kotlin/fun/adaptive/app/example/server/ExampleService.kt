@file:Suppress("unused")

package `fun`.adaptive.app.example.server

import `fun`.adaptive.app.example.ExampleApi
import `fun`.adaptive.backend.builtin.ServiceImpl

class ExampleService : ServiceImpl<ExampleService>(), ExampleApi {

    override suspend fun exampleFun() {
        TODO("Not yet implemented")
    }

}
