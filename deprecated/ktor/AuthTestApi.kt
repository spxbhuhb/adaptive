package `fun`.adaptive.ktor.worker

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AuthTestApi {

    suspend fun publicFun(): String

    suspend fun loggedInFun(): String

}