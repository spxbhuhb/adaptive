/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.sandbox.api

import hu.simplexion.adaptive.service.ServiceApi

@ServiceApi
interface CounterApi {

    suspend fun incrementAndGet() : Int

}