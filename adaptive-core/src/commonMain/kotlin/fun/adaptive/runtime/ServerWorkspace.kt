package `fun`.adaptive.runtime

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.WorkerImpl

open class ServerWorkspace : AbstractWorkspace() {

    val services = mutableListOf<ServiceImpl<*>>()

    val workers = mutableListOf<WorkerImpl<*>>()

    operator fun ServiceImpl<*>.unaryPlus() { services += this }

    operator fun WorkerImpl<*>.unaryPlus() { workers += this }

}