/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "grove"

pluginManagement {
    includeBuild("../adaptive-gradle-plugin")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

includeBuild("../adaptive-core")
includeBuild("../adaptive-ui")
includeBuild("../adaptive-lib-email")
includeBuild("../adaptive-lib-ktor")
includeBuild("../adaptive-lib-auth")
includeBuild("../adaptive-lib-auto")
includeBuild("../adaptive-lib-graphics")
includeBuild("../adaptive-lib-ui")
includeBuild("../adaptive-lib-exposed")
includeBuild("../adaptive-grove")
includeBuild("../adaptive-grove-runtime")

