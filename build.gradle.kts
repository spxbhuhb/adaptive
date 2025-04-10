/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

val jsTasks = listOf(
    "other:kotlinUpgradeYarnLock", "nodejs:rootPackageJson"
)

val noJsBuilds = listOf(
    "core-gradle-plugin", "core-kotlin-plugin", "iot-cli"
)

val publishTasks = listOf(
    "publishing:publishToMavenLocal", "publishing:publish"
)

val noPublishBuilds = listOf(
    "grove-app",
    "iot-app",
    "iot-cli",
    "sandbox-app",
    "sandbox-app-echo",
    "site-app",
    "site-lib-cookbook"
)

fun registerTask(name: String) {
    val (groupName, taskName) = name.split(":")

    tasks.register(taskName) {
        group = groupName
        gradle.includedBuilds.forEach { build ->
            if (build.name in noPublishBuilds && name in publishTasks) return@forEach
            if (build.name in noJsBuilds && name in jsTasks) return@forEach
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

fun shorthand(buildName : String, taskName : String, shorthand:String) {
    tasks.register(shorthand) {
        group = "aaa"
        gradle.includedBuilds.forEach { build ->
            if (build.name != buildName) return@forEach
            dependsOn(build.task(":$taskName"))
        }
    }
}

shorthand("grove-app", "jsBrowserDevelopmentRun", "grove-js")
shorthand("grove-app", "jvmRun", "grove-jvm")
shorthand("grove-lib", "processAdaptiveResourcesCommonMain", "grove-resources")
shorthand("sandbox-app", "jsBrowserDevelopmentRun", "sandbox-js")
shorthand("sandbox-app", "jvmRun", "sandbox-jvm")
shorthand("site-app", "jsBrowserDevelopmentRun", "site-js")
shorthand("site-app", "jvmRun", "site-jvm")
shorthand("iot-app", "jsBrowserDevelopmentRun", "iot-js")
shorthand("iot-app", "jvmRun", "iot-jvm")

// If you add a shorthand, and it does not start the jvm/js double-check the project
// name, typically that's what's wrong.