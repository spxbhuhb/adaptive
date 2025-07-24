/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.internal.gradle.setupPublishing
import `fun`.adaptive.internal.gradle.skipYarnLock

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
    signing
    alias(libs.plugins.gradleMavenPublish)
    id("fun.adaptive.internal.gradle")
}

group = "fun.adaptive"
version = libs.versions.adaptive.get()
description = "Adaptive Grove Documentation Builder"

skipYarnLock()

adaptive {
    resources {
        publicAccessors = true
        packageOfResources = "fun.adaptive.grove.doc.generated.resources"
    }
}

kotlin {
    jvmToolchain(11)

    jvm()

    js(IR) {
        browser {
            testTask {
                enabled = false
            }
        }
        binaries.library()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.adaptive.core.core)
                implementation(libs.adaptive.core.ui)

                implementation(libs.adaptive.lib.auth)
                implementation(libs.adaptive.lib.document)
                implementation(libs.adaptive.lib.ui)
                implementation(libs.adaptive.lib.ui.mpw)
                implementation(libs.adaptive.lib.util)
                implementation(libs.adaptive.lib.value)
            }
        }

        commonTest {
            dependencies {
                api(libs.kotlin.test)
            }
        }
    }

}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}

tasks.create<JavaExec>("compileAdaptiveDocumentation") {
    classpath = sourceSets["jvmMain"].runtimeClasspath
    mainClass.set("fun.adaptive.grove.doc.MainKt")

    val input = project.rootDir.resolve("../..") // this points to the root directory of the repo
    val mdOutput = input.resolve("build/adaptive/doc")
    val valuesOutput = input.resolve("site/site-app/var/values")

    args(input, mdOutput, valuesOutput)

    doFirst {
        mdOutput.deleteRecursively()
        valuesOutput.deleteRecursively()
    }
}

setupPublishing()