/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.builtin

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

interface WorkerImpl<T : WorkerImpl<T>> : ServerFragmentImpl {

    // FIXME scope change in adaptive worker (this does not respect the change)
    val isActive: Boolean
        get() = (fragment as ServerWorker).scope.isActive

    suspend fun run()

}

/**
 * Launches a function in the scope of this worker.
 */
fun WorkerImpl<*>.launch(function: suspend CoroutineScope.() -> Unit) {
    (fragment as ServerWorker)
        .scope.launch {
            try {
                function(this)
            } catch (ex : CancellationException) {
                logger.fine(ex)
            } catch (ex: Exception) {
                logger.error(ex)
            }
        }
}
