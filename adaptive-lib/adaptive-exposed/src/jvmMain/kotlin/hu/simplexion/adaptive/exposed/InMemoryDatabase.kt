/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.server.components.WorkerImpl
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.exposed.sql.Database

class InMemoryDatabase : WorkerImpl<InMemoryDatabase> {

    override fun create() {
        Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    }

    override suspend fun run(scope: CoroutineScope) {

    }
}