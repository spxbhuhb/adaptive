/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.site

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.email.store.EmailQueue
import hu.simplexion.adaptive.email.store.EmailTable
import hu.simplexion.adaptive.email.worker.EmailWorker
import hu.simplexion.adaptive.exposed.HikariWorker
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.component.store
import hu.simplexion.adaptive.server.component.worker
import hu.simplexion.adaptive.settings.dsl.propertyFile
import hu.simplexion.adaptive.settings.dsl.settings

fun main(args: Array<String>) {

    adaptive(AdaptiveServerAdapter()) {

        settings { propertyFile { "./etc/site.properties" } }

        worker { HikariWorker() }

        store { EmailTable() }
        store { EmailQueue() }
        worker { EmailWorker() }
    }

}