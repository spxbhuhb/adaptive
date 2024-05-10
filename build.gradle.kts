/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

tasks.register("clean") {
    group = "build"
    dependsOn(gradle.includedBuild("adaptive-site").task(":clean"))
}

tasks.register("publishToMavenLocal") {
    group = "publishing"
    dependsOn(gradle.includedBuild("adaptive-core").task(":publishToMavenLocal"))
    dependsOn(gradle.includedBuild("adaptive-gradle-plugin").task(":publishToMavenLocal"))
    dependsOn(gradle.includedBuild("adaptive-kotlin-plugin").task(":publishToMavenLocal"))
    dependsOn(gradle.includedBuild("adaptive-lib").task(":publishToMavenLocal"))
}