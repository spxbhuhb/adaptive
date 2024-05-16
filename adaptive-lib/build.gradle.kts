/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

fun register(name: String, js : Boolean = true) {
    val (groupName, taskName) = name.split(":")
    tasks.register(taskName) {
        group = groupName
        dependsOn(gradle.includedBuild("adaptive-lib-email").task(":$taskName"))
        if (!js) {
            dependsOn(gradle.includedBuild("adaptive-lib-exposed").task(":$taskName"))
        }
        dependsOn(gradle.includedBuild("adaptive-lib-ktor").task(":$taskName"))
        dependsOn(gradle.includedBuild("adaptive-lib-sandbox").task(":$taskName"))
    }
}

register("build:clean")
register("build:build")
register("publishing:publishToMavenLocal")
register("other:kotlinUpgradeYarnLock", js = true)
register("nodejs:rootPackageJson", js = true) // the sandbox uses this one