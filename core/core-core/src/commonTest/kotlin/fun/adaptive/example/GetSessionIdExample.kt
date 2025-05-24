@file:Suppress("unused")

package `fun`.adaptive.example

import `fun`.adaptive.backend.builtin.ServiceImpl

class GetSessionIdExample : ServiceImpl<ExampleServiceImpl>() {

    fun getSessionIdExample() {
        println(serviceContext.sessionId)
    }

    fun getSessionIdOrNullExample() {
        println(serviceContext.sessionIdOrNull)
    }

}