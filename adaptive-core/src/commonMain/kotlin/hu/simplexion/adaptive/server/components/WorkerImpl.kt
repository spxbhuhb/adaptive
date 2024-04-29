/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.components

import hu.simplexion.adaptive.services.ServiceImpl
import kotlinx.coroutines.CoroutineScope

interface WorkerImpl<T : WorkerImpl<T>> {

    suspend fun run(scope: CoroutineScope)

    fun <ST : StoreImpl<ST>> store(): ST {
        TODO()
    }

    fun <ST : ServiceImpl<ST>> service(): ST {
        TODO()
    }

    fun <WT : WorkerImpl<WT>> worker(): WT {
        TODO()
    }

}