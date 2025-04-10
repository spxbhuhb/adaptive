/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "iot-cli"

pluginManagement {
    includeBuild("../../core/core-gradle-plugin")
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
includeBuild("../../lib/lib-ktor")
includeBuild("../../lib/lib-auth")
includeBuild("../../lib/lib-value")
includeBuild("../../lib/lib-ui")
includeBuild("../../lib/lib-util")

includeBuild("../../iot/iot-lib-core")
includeBuild("../../iot/iot-lib-zigbee")