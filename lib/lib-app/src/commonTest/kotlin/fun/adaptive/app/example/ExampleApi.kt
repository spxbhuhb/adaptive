package `fun`.adaptive.app.example

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface ExampleApi {

    suspend fun exampleFun()

}