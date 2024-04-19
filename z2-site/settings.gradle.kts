/*
 * Copyright © 2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "z2-site"

pluginManagement {
    includeBuild("../z2-gradle-plugin")
}

includeBuild("../z2-core")
includeBuild("../z2-lib")
