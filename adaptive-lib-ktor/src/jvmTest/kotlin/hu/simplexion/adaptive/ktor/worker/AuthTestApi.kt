package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.service.ServiceApi

@ServiceApi
interface AuthTestApi {

    suspend fun publicFun(): String

    suspend fun loggedInFun(): String

}