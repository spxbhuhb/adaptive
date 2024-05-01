/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.component

import kotlinx.coroutines.CoroutineScope

interface WorkerImpl<T : WorkerImpl<T,BT>,BT> : ServerFragmentImpl<BT> {

    suspend fun run(scope: CoroutineScope)

}
