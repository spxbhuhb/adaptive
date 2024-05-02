/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package example.service

import example.api.CounterApi
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import java.util.concurrent.atomic.AtomicInteger

class CounterService : CounterApi, ServiceImpl<CounterService> {

    companion object {
        val counter = AtomicInteger(0)
    }

    // FIXME newInstance fragment copy
    override fun newInstance(serviceContext: ServiceContext): CounterService {
        return CounterService().also {
            it.fragment = this.fragment
        }
    }

    override suspend fun put(count : Int) {
        counter.set(count)
    }

    override suspend fun get() : Int {
        return counter.get()
    }

}