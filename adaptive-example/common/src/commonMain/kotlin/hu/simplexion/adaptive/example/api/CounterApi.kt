/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.example.api

import hu.simplexion.adaptive.service.Service

interface CounterApi : Service {

    suspend fun put(count : Int)

    suspend fun get() : Int

}