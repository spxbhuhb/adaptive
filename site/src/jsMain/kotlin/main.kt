/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.graphics.graphicsBrowser
import `fun`.adaptive.site.siteClientBackend
import `fun`.adaptive.site.siteCommon
import `fun`.adaptive.site.siteMain
import `fun`.adaptive.ui.browser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        siteCommon()

        browser(backend = siteClientBackend()) { adapter ->

            adapter.graphicsBrowser()
            adapter.siteCommon()

            siteMain()

        }
    }
}