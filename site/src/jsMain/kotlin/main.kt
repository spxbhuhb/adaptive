/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.graphics.graphicsBrowser
import `fun`.adaptive.site.siteCommon
import `fun`.adaptive.site.siteMain
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun main() {

    fun siteClientBackend() =
        backend {
            auto()
            worker { SnackbarManager() }
        }

    CoroutineScope(Dispatchers.Default).launch {

        siteCommon()

        browser(backend = siteClientBackend()) { adapter ->

            adapter.graphicsBrowser()
            adapter.siteCommon()

            siteMain()

        }
    }
}