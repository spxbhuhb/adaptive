package hu.simplexion.adaptive.example.api

import hu.simplexion.adaptive.service.ServiceApi

@ServiceApi
interface CounterApi {

    suspend fun incrementAndGet() : Int

}