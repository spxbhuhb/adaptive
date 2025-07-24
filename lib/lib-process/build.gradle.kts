import `fun`.adaptive.internal.gradle.setupPublishing
import `fun`.adaptive.internal.gradle.skipYarnLock
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
description = "Adaptive Lib Process"

skipYarnLock()

kotlin {

    jvmToolchain(11)

    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

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
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

setupPublishing()