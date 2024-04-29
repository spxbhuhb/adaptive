/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "adaptive-email"

pluginManagement {
    includeBuild("../../adaptive-gradle-plugin")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}