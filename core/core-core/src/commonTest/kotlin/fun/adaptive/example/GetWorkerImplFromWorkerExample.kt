@file:Suppress("unused")

package `fun`.adaptive.example

import `fun`.adaptive.backend.builtin.WorkerImpl

class GetWorkerImplFromWorkerExample : WorkerImpl<GetWorkerImplFromWorkerExample>() {

    val worker by workerImpl<ExampleWorkerImpl>()

}