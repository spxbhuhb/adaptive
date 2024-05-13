/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import example.service.CounterService
import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.setting.dsl.propertyFile
import hu.simplexion.adaptive.server.setting.dsl.settings

fun main(args: Array<String>) {

    adaptive(AdaptiveServerAdapter<Any>()) {

        settings { propertyFile { "./etc/example.properties" } }

        service { CounterService() }

    }

    while (true) {
        Thread.sleep(1000)
    }
}