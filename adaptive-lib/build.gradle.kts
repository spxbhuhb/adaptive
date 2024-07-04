/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

val jsOnlyTasks = listOf("other:kotlinUpgradeYarnLock", "nodejs:rootPackageJson")
val publishTasks = listOf("publishing:publishToMavenLocal", "publishing:publish")

fun register(name: String) {
    val (groupName, taskName) = name.split(":")
    tasks.register(taskName) {
        group = groupName

        dependsOn(gradle.includedBuild("adaptive-lib-auth").task(":$taskName"))
        dependsOn(gradle.includedBuild("adaptive-lib-email").task(":$taskName"))

        if (name !in jsOnlyTasks) {
            dependsOn(gradle.includedBuild("adaptive-lib-exposed").task(":$taskName"))
        }

        dependsOn(gradle.includedBuild("adaptive-lib-ktor").task(":$taskName"))
        dependsOn(gradle.includedBuild("adaptive-lib-sandbox").task(":$taskName"))
    }
}

register("build:clean")
register("build:build")
register("publishing:publishToMavenLocal")
register("publishing:publish")
register("other:kotlinUpgradeYarnLock")
register("nodejs:rootPackageJson") // the sandbox uses this one