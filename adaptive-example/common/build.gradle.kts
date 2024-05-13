/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform")
    id("hu.simplexion.adaptive")
}

val baseName = "common"
val pomName = "Adaptive Example - Common"
val scmPath = "spxbhuhb/adaptive"

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    jvmToolchain(11)

    jvm {
        withJava()
    }

    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets["commonMain"].dependencies {
        api("hu.simplexion.adaptive:adaptive-core:$version")
        api("hu.simplexion.adaptive:adaptive-ui:$version")
    }
}