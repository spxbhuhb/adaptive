/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

tasks.register("clean") {
    group = "build"
    dependsOn(gradle.includedBuild("adaptive-site").task(":clean"))
}

tasks.register("build") {
    group = "build"
    dependsOn(gradle.includedBuild("adaptive-site").task(":build"))
}