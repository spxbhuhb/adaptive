@file:Suppress("unused")

package `fun`.adaptive.example

import `fun`.adaptive.backend.builtin.ServiceImpl

class GetWorkerImplFromServiceExample : ServiceImpl<GetWorkerImplFromServiceExample>() {

    val worker by workerImpl<ExampleWorkerImpl>()

}