/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform")
    id("hu.simplexion.adaptive")
}

group = "hu.simplexion.adaptive.example"
val baseName = "browser"
val pomName = "Adaptive Example - Browser"
val scmPath = "spxbhuhb/adaptive"

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets["jsMain"].dependencies {
        implementation(project(":common"))
    }
}