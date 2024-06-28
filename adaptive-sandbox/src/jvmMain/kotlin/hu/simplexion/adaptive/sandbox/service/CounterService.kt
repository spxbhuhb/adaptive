/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.sandbox.service

import hu.simplexion.adaptive.lib.sandbox.api.CounterApi
import hu.simplexion.adaptive.sandbox.worker.CounterWorker
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.server.builtin.worker

class CounterService : CounterApi, ServiceImpl<CounterService> {

    val worker by worker<CounterWorker>()

    override suspend fun incrementAndGet(): Int {
        return worker.counter.incrementAndGet()
    }

}