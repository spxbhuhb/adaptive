@file:Suppress("unused")

package `fun`.adaptive.example

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.WorkerImpl

class ExampleWorkerImpl : WorkerImpl<ExampleWorkerImpl>()

class ExampleServiceImpl : ServiceImpl<ExampleServiceImpl>()

class GetWorkerImplFromServiceExample : ServiceImpl<GetWorkerImplFromServiceExample>() {

    val worker by workerImpl<ExampleWorkerImpl>()

}

class GetWorkerImplFromWorkerExample : ServiceImpl<GetWorkerImplFromWorkerExample>() {

    val worker by workerImpl<ExampleWorkerImpl>()

}

class GetSessionIdExample : ServiceImpl<ExampleServiceImpl>() {

    fun getSessionIdExample() {
        println(serviceContext.sessionId)
    }

    fun getSessionIdOrNullExample() {
        println(serviceContext.sessionIdOrNull)
    }

}

