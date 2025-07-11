package `fun`.adaptive.example

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface ExampleServiceApi {

    suspend fun getMessage(): String?

    suspend fun sendMessage(message: String)

}