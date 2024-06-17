/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.platform.withJsResources

fun main() {

    withJsResources()

    browser {
        layouts()
    }

}
