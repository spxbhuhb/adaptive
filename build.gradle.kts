/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

val jsTasks = listOf(
    "other:kotlinUpgradeYarnLock", "nodejs:rootPackageJson"
)

val noJsBuilds = listOf(
    "core-gradle-plugin", "core-kotlin-plugin", "doc-main"
)

val publishTasks = listOf(
    "publishing:publishToMavenLocal", "publishing:publish"
)

val noPublishBuilds = listOf(
    "core-build",
    "doc-example",
    "doc-main",
    "grove-app",
    "grove-host",
    "sandbox-app",
    "sandbox-app-echo",
    "sandbox-app-mpw",
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

fun shorthand(buildName : String, taskName : String, shorthand:String, first: () -> Unit = {}) {
    tasks.register(shorthand) {
        group = "aaa"
        gradle.includedBuilds.forEach { build ->
            if (build.name != buildName) return@forEach
            dependsOn(build.task(":$taskName"))
            doFirst { first() }
        }
    }
}

shorthand("grove-app", "jsBrowserDevelopmentRun", "grove-js")
shorthand("grove-app", "jvmRun", "grove-jvm")
shorthand("grove-lib", "processAdaptiveResourcesCommonMain", "grove-resources")
shorthand("sandbox-app", "jsBrowserDevelopmentRun", "sandbox-js")
shorthand("sandbox-app", "jvmRun", "sandbox-jvm")
shorthand("sandbox-app-mpw", "jsBrowserDevelopmentRun", "mpw-js")
shorthand("sandbox-app-mpw", "jvmRun", "mpw-jvm")
shorthand("site-app", "jsBrowserDevelopmentRun", "site-js")
shorthand("site-app", "jvmRun", "site-jvm") {
    project.file("site/site-app/var/values").deleteRecursively()
}
shorthand("grove-doc", "compileAdaptiveDocumentation", "doc")

// If you add a shorthand, and it does not start the jvm/js double-check the project
// name, typically that's what's wrong.


tasks.register<Copy>("collectTrainingDocuments") {
    group = "training"

    val destinationDir = File(rootDir, "build/adaptive/training")
    into(destinationDir)

    // Clear the destination directory before copying
    doFirst {
        if (destinationDir.exists()) {
            destinationDir.deleteRecursively()
        }
        destinationDir.mkdirs()
    }

    // Collect all *_guide.md files from training dirs in subprojects
    gradle.includedBuilds.forEach { build ->
        val trainingDir = File(build.projectDir, "training")
        if (trainingDir.exists()) {
            from(trainingDir) {
                include("**/*")
                eachFile {
                    path = name
                }
            }
        }
    }

    includeEmptyDirs = false
}

tasks.register<Zip>("zipTrainingDocuments") {
    group = "training"
    dependsOn("collectTrainingDocuments")

    val guidesDir = File(rootDir, "build/adaptive/training")
    from(guidesDir)

    destinationDirectory.set(File(rootDir, "build/adaptive"))
    archiveFileName.set("training.zip")
}