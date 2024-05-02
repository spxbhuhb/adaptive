/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.site

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.email.service.EmailService
import hu.simplexion.adaptive.email.store.EmailQueue
import hu.simplexion.adaptive.email.store.EmailTable
import hu.simplexion.adaptive.email.worker.EmailWorker
import hu.simplexion.adaptive.exposed.HikariWorker
import hu.simplexion.adaptive.exposed.InMemoryDatabase
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.builtin.store
import hu.simplexion.adaptive.server.builtin.worker
import hu.simplexion.adaptive.server.setting.dsl.propertyFile
import hu.simplexion.adaptive.server.setting.dsl.setting
import hu.simplexion.adaptive.server.setting.dsl.settings

fun main(args: Array<String>) {

    adaptive(AdaptiveServerAdapter<Any>()) {

        settings { propertyFile { "./etc/site.properties" } }

        val inMemoryDatabase = setting<Boolean> { "IN_MEMORY_DATABASE" }.value

        if (inMemoryDatabase) {
            worker { InMemoryDatabase() }
        } else {
            worker { HikariWorker() }
        }

        store { EmailTable() }
        store { EmailQueue() }
        worker { EmailWorker() }
        service { EmailService() }

    }

    while (true) {
        Thread.sleep(1000)
    }
}