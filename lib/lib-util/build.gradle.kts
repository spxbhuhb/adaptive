import `fun`.adaptive.internal.gradle.setupPublishing
import `fun`.adaptive.internal.gradle.skipYarnLock

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
    signing
    alias(libs.plugins.gradleMavenPublish)
    id("fun.adaptive.internal.gradle")
}

group = "fun.adaptive"
version = libs.versions.adaptive.get()
description = "Adaptive Lib Utilities"

skipYarnLock()

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

    if (libs.versions.ios.support.get() != "none") {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        )
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.adaptive.core.core)
            implementation(libs.kotlinx.io)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlinx.coroutines.debug)
        }
    }
}

setupPublishing()