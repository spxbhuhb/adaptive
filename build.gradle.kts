/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

val jsTasks = listOf(
    "other:kotlinUpgradeYarnLock", "nodejs:rootPackageJson"
)

val noJsBuilds = listOf(
    "adaptive-gradle-plugin", "adaptive-kotlin-plugin"
)

val publishTasks = listOf(
    "publishing:publishToMavenLocal", "publishing:publish"
)

val noPublishBuilds = listOf(
    "sandbox", "site", "test"
)

fun registerTask(name: String) {
    val (groupName, taskName) = name.split(":")

    tasks.register(taskName) {
        group = groupName
        gradle.includedBuilds.forEach { build ->
            if (build.name in noPublishBuilds && name in publishTasks) return@register
            if (build.name in noJsBuilds && name in jsTasks) return@register
            dependsOn(build.task(":$taskName"))
        }
    }
}

registerTask("build:clean")
registerTask("build:build")
registerTask("publishing:publishToMavenLocal")
registerTask("publishing:publish")
registerTask("other:kotlinUpgradeYarnLock")
registerTask("verification:jvmTest")
