/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

fun register(name: String) {
    val (groupName, taskName) = name.split(":")
    tasks.register(taskName) {
        group = groupName
        dependsOn(gradle.includedBuild("adaptive-email").task(":$taskName"))
        dependsOn(gradle.includedBuild("adaptive-exposed").task(":$taskName"))
        dependsOn(gradle.includedBuild("adaptive-ktor").task(":$taskName"))
        //dependsOn(gradle.includedBuild("adaptive-ui").task(":$taskName"))
    }
}

register("build:clean")
register("build:build")
register("publishing:publishToMavenLocal")