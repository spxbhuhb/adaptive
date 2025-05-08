package my.project.example.server

import `fun`.adaptive.backend.builtin.WorkerImpl

class ExampleWorker : WorkerImpl<ExampleWorker> {

    override suspend fun run() {
        logger.info("${adapter?.application}")
    }

}