/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.site

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.settings.dsl.propertyFile
import hu.simplexion.adaptive.settings.dsl.setting
import hu.simplexion.adaptive.settings.dsl.settings

fun main(args: Array<String>) {

    adaptive(AdaptiveServerAdapter()) {
        settings { propertyFile { "./etc/site.properties" } }
        val siteName = setting<String> { "SITE_NAME" }.value
        println(siteName)
    }

}