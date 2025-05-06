/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "iot-lib-spxb"

pluginManagement {
    includeBuild("../../core/core-gradle-plugin")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

includeBuild("../../core/core-core")
includeBuild("../../core/core-ui")

includeBuild("../../lib/lib-document")
includeBuild("../../lib/lib-ktor")
includeBuild("../../lib/lib-ui")
includeBuild("../../lib/lib-util")
includeBuild("../../lib/lib-value")

includeBuild("../../iot/iot-lib-core")
includeBuild("../../iot/iot-lib-driver")