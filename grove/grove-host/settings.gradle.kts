/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "grove-host"

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
includeBuild("../../core/core-ui")

includeBuild("../../grove/grove-lib")
includeBuild("../../grove/grove-runtime")

includeBuild("../../lib/lib-ktor")
includeBuild("../../lib/lib-auth")
includeBuild("../../lib/lib-graphics")
includeBuild("../../lib/lib-document")
includeBuild("../../lib/lib-ui")
includeBuild("../../lib/lib-value")
includeBuild("../../lib/lib-util")
includeBuild("../../lib/lib-app")

includeBuild("../../site/site-lib-cookbook")