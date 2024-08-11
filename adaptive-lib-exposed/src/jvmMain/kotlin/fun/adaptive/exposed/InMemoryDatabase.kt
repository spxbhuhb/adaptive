/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.exposed

import `fun`.adaptive.server.builtin.WorkerImpl
import org.jetbrains.exposed.sql.Database

class InMemoryDatabase(
    val name: String = "db"
) : WorkerImpl<InMemoryDatabase> {

    override fun create() {
        Database.connect("jdbc:h2:mem:$name;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    }

    override suspend fun run() {

    }
}