/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend.builtin

import kotlinx.coroutines.*

abstract class WorkerImpl<T : WorkerImpl<T>> : BackendFragmentImpl() {

    // FIXME scope change in adaptive worker (this does not respect the change)
    val isActive: Boolean
        get() = scope.isActive

    val scope: CoroutineScope
        get() = (fragment as BackendWorker).scope

    open suspend fun run() = Unit

    /**
     * Launches a function in the scope of this worker.
     */
    open fun launch(function: suspend CoroutineScope.() -> Unit): Job =
        scope.launch {
            try {
                function(this)
            } catch (_: CancellationException) {
                scope.ensureActive()
            } catch (ex: Exception) {
                logger.error(ex)
            }
        }

}


