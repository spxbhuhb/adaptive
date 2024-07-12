/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)

}

group = "hu.simplexion.adaptive"
version = libs.versions.adaptive.get()

val baseName = "adaptive-ui-designer"
val pomName = "Adaptive UI Designer"
val scmPath = "spxbhuhb/adaptive"

// this is ugly but I don't use JS dependencies anyway, 
// https://youtrack.jetbrains.com/issue/KT-50848/Kotlin-JS-inner-build-routines-are-using-vulnerable-NPM-dependencies-and-now-that-we-have-kotlin-js-store-github-audit-this
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().lockFileName = "skip-yarn-lock"
}

kotlin {

    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.adaptive.core)
            implementation(libs.adaptive.ui)
        }
    }
}