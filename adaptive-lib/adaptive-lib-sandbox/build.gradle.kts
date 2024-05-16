/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
}

group = "hu.simplexion.adaptive"
version = libs.versions.adaptive.get()

val baseName = "adaptive-lib-sandbox"
val pomName = "Adaptive Lib Sandbox"
val scmPath = "spxbhuhb/adaptive"

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
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "Shared"
                isStatic = true
            }
        }
    }

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.adaptive.core)
                implementation(libs.adaptive.ui.common)
            }
        }
    }
}