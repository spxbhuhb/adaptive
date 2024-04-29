/*
 * Copyright © 2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "adaptive-lib"

pluginManagement {
    includeBuild("../adaptive-gradle-plugin")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

includeBuild("../adaptive-core")
includeBuild("../adaptive-kotlin-plugin")