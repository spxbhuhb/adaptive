/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.adaptive) apply false
}

tasks.register("publishToMavenLocal") {
    group = "publishing"
    dependsOn(":adaptive-email:publishToMavenLocal")
    dependsOn(":adaptive-exposed:publishToMavenLocal")
    dependsOn(":adaptive-ktor:publishToMavenLocal")
    dependsOn(":adaptive-template:publishToMavenLocal")
    dependsOn(":adaptive-ui:publishToMavenLocal")
}