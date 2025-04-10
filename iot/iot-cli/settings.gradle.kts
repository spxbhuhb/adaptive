/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "adaptive-iot-cli"

pluginManagement {
    includeBuild("../../adaptive-gradle-plugin")
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

includeBuild("../../adaptive-core")
includeBuild("../../adaptive-lib-ktor")
includeBuild("../../adaptive-lib-auth")
includeBuild("../../adaptive-lib-value")
includeBuild("../../adaptive-lib-ui")
includeBuild("../../adaptive-lib-util")

includeBuild("../adaptive-iot-lib-core")
includeBuild("../adaptive-iot-lib-zigbee")