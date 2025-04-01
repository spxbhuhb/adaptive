/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

val jsTasks = listOf(
    "other:kotlinUpgradeYarnLock", "nodejs:rootPackageJson"
)

val noJsBuilds = listOf(
    "adaptive-gradle-plugin", "adaptive-kotlin-plugin", "test", "adaptive-iot-cli"
)

val publishTasks = listOf(
    "publishing:publishToMavenLocal", "publishing:publish"
)

val noPublishBuilds = listOf(
    "adaptive-grove",
    "adaptive-lib-cookbook",
    "adaptive-iot-app",
    "adaptive-iot-cli",
    "cookbook-app-echo",
    "grove",
    "cookbook",
    "sandbox",
    "site",
    "test"
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

shorthand("adaptive-kotlin-plugin", "generateTests", "plugin-generate-tests")
shorthand("adaptive-kotlin-plugin", "test", "plugin-test")
shorthand("grove", "jsBrowserDevelopmentRun", "grove-js")
shorthand("grove", "jvmRun", "grove-jvm")
shorthand("adaptive-grove", "processAdaptiveResourcesCommonMain", "grove-resources")
shorthand("sandbox", "jsBrowserDevelopmentRun", "sandbox-js")
shorthand("sandbox", "jvmRun", "sandbox-jvm")
shorthand("site", "jsBrowserDevelopmentRun", "site-js")
shorthand("site", "jvmRun", "site-jvm")
shorthand("adaptive-iot-app", "jsBrowserDevelopmentRun", "iot-js")
shorthand("adaptive-iot-app", "jvmRun", "iot-jvm")

// If you add a shorthand, and it does not start the jvm/js double-check the project
// name, typically that's what's wrong.