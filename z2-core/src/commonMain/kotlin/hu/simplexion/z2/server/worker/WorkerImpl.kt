package hu.simplexion.z2.server.worker

import kotlinx.coroutines.CoroutineScope

interface WorkerImpl<T : WorkerImpl<T>> {
    suspend fun run(scope: CoroutineScope)
}