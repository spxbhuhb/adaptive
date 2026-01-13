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
description = "Adaptive Lib Application"

skipYarnLock()

kotlin {

    jvmToolchain(11)

    jvm()

    js(IR) {
        browser()
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
            implementation(libs.adaptive.core.ui)

            implementation(libs.adaptive.lib.auth)
            implementation(libs.adaptive.lib.document)
            implementation(libs.adaptive.lib.graphics)
            implementation(libs.adaptive.lib.ktor)
            implementation(libs.adaptive.lib.ui)
            implementation(libs.adaptive.lib.ui.mpw)
            implementation(libs.adaptive.lib.util)
            implementation(libs.adaptive.lib.value)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmTest.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.okhttp)
        }
    }
}

setupPublishing()