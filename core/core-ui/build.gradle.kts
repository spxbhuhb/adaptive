/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */


import `fun`.adaptive.internal.gradle.setupPublishing
import `fun`.adaptive.internal.gradle.skipYarnLock
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.adaptive)
    signing
    alias(libs.plugins.gradleMavenPublish)
    id("fun.adaptive.internal.gradle")
}

group = "fun.adaptive"
version = libs.versions.adaptive.get()
description = "Adaptive UI Core Library"

skipYarnLock()

kotlin {

    androidTarget {
        publishLibraryVariants("release", "debug")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

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
            api(libs.adaptive.core.core)
        }

        commonTest.dependencies {
            api(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlinx.coroutines.debug)
        }

        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.constraintlayout)
            implementation(libs.android.material)
        }
    }
}

android {
    namespace = "fun.adaptive.ui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

setupPublishing()