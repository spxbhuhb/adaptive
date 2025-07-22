@file:Suppress("unused")

package `fun`.adaptive.example

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceProvider

@ServiceProvider
class GetSessionIdExample : ServiceImpl<ExampleServiceImpl>() {

    fun getSessionIdExample() {
        println(serviceContext.sessionId)
    }

    fun getSessionIdOrNullExample() {
        println(serviceContext.sessionIdOrNull)
    }

}